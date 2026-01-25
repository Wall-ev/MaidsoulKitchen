package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.clickharvest;

import net.minecraft.world.level.block.Block;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class BlockUseMethodDetector {
    // Block.popResource的ASM签名（根据MC版本调整）
    private static final String POP_RESOURCE_OWNER = "net/minecraft/block/Block";
    private static final String POP_RESOURCE_NAME = "popResource";
    private static final String POP_RESOURCE_DESC = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V";

    // Block.use的ASM签名（根据MC版本调整）
    private static final String USE_METHOD_NAME = "use";
    private static final String USE_METHOD_DESC = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;";

    // 已检测的类（避免循环递归）
    private final Set<String> checkedClasses = new HashSet<>();
    // 已检测的静态方法（避免循环递归）
    private final Set<String> checkedStaticMethods = new HashSet<>();


    /**
     * 检测Block子类的use方法（含继承/静态调用链）是否调用popResource
     * @param clazz Block子类
     * @return true=调用了，false=未调用
     */
    public boolean detectPopResourceCall(Class<? extends Block> clazz) throws IOException {
        return detectPopResourceCall(clazz.getName());
    }

    /**
     * 检测Block子类的use方法（含继承/静态调用链）是否调用popResource
     * @param className Block子类全类名（如net.minecraft.block.SweetBerryBushBlock）
     * @return true=调用了，false=未调用
     */
    public boolean detectPopResourceCall(String className) throws IOException {
        checkedClasses.clear();
        checkedStaticMethods.clear();
        // 将类名转为ASM格式（. → /）
        return checkClass(className.replace('.', '/'));
    }

    /**
     * 递归检测类的use方法（含父类）
     */
    private boolean checkClass(String className) throws IOException {
        if (checkedClasses.contains(className)) {
            return false;
        }
        checkedClasses.add(className);

        // 读取类字节码
        ClassReader classReader = getClassReader(className);
        if (classReader == null) {
            return false;
        }

        // 检测当前类的use方法
        UseMethodVisitor visitor = new UseMethodVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // 场景1：当前类use方法直接调用popResource
        if (visitor.isPopResourceCalled()) {
            return true;
        }

        // 场景2：当前类use方法调用了其他静态方法 → 递归检测这些静态方法
        for (StaticMethodCall call : visitor.getStaticMethodCalls()) {
            if (checkStaticMethod(call.owner, call.name, call.desc)) {
                return true;
            }
        }

        // 场景3：当前类重写了use且调用super.use → 检测父类
        if (visitor.isSuperUseCalled()) {
            String superClassName = classReader.getSuperName();
            if (superClassName != null && !superClassName.equals("java/lang/Object")) {
                if (checkClass(superClassName)) {
                    return true;
                }
            }
        }

        // 场景4：当前类未重写use → 直接检测父类
        if (!visitor.isUseMethodPresent()) {
            String superClassName = classReader.getSuperName();
            if (superClassName != null && !superClassName.equals("java/lang/Object")) {
                return checkClass(superClassName);
            }
        }

        return false;
    }

    /**
     * 递归检测静态方法是否调用popResource（含静态方法调用链）
     */
    private boolean checkStaticMethod(String owner, String name, String desc) throws IOException {
        // 生成唯一标识：owner+name+desc，避免重复检测
        String methodKey = owner + "#" + name + "#" + desc;
        if (checkedStaticMethods.contains(methodKey)) {
            return false;
        }
        checkedStaticMethods.add(methodKey);

        // 读取静态方法所属类的字节码
        ClassReader classReader = getClassReader(owner);
        if (classReader == null) {
            return false;
        }

        // 检测该静态方法是否调用popResource或其他静态方法
        StaticMethodVisitor visitor = new StaticMethodVisitor(name, desc);
        classReader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // 场景1：静态方法直接调用popResource
        if (visitor.isPopResourceCalled()) {
            return true;
        }

        // 场景2：静态方法调用了其他静态方法 → 递归检测
        for (StaticMethodCall call : visitor.getStaticMethodCalls()) {
            if (checkStaticMethod(call.owner, call.name, call.desc)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 从类加载器读取字节码，创建ClassReader（兼容多类加载器环境）
     */
    private ClassReader getClassReader(String className) throws IOException {
        String resourcePath = className + ".class";
        InputStream is = null;

        // 尝试系统类加载器
        is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            // 尝试当前类的类加载器（模组环境）
            is = BlockUseMethodDetector.class.getClassLoader().getResourceAsStream(resourcePath);
        }
        if (is == null) {
            // 尝试线程上下文类加载器（兼容多线程环境）
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        }

        if (is == null) {
            return null;
        }

        try (InputStream inputStream = is) {
            return new ClassReader(inputStream);
        }
    }

    /**
     * 静态方法调用的封装类
     */
    private static class StaticMethodCall {
        final String owner;
        final String name;
        final String desc;

        public StaticMethodCall(String owner, String name, String desc) {
            this.owner = owner;
            this.name = name;
            this.desc = desc;
        }
    }

    /**
     * 检测Block的use方法：是否调用popResource、是否调用super.use、是否调用其他静态方法
     */
    private static class UseMethodVisitor extends ClassVisitor {
        private boolean popResourceCalled = false;
        private boolean superUseCalled = false;
        private boolean useMethodPresent = false;
        private final Set<StaticMethodCall> staticMethodCalls = new HashSet<>();

        public UseMethodVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            // 只处理use方法
            if (USE_METHOD_NAME.equals(name) && USE_METHOD_DESC.equals(desc)) {
                useMethodPresent = true;
                return new UseMethodInsnVisitor(Opcodes.ASM9, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        public boolean isPopResourceCalled() {
            return popResourceCalled;
        }

        public boolean isSuperUseCalled() {
            return superUseCalled;
        }

        public boolean isUseMethodPresent() {
            return useMethodPresent;
        }

        public Set<StaticMethodCall> getStaticMethodCalls() {
            return staticMethodCalls;
        }

        /**
         * 拦截use方法内的指令：检测popResource、super.use、静态方法调用
         */
        private class UseMethodInsnVisitor extends AdviceAdapter {
            public UseMethodInsnVisitor(int api, MethodVisitor methodVisitor) {
                super(api, methodVisitor, 0, USE_METHOD_NAME, USE_METHOD_DESC);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);

                // 1. 检测静态方法调用（如CaveVines.use）
                if (opcode == Opcodes.INVOKESTATIC) {
                    // 检测是否是popResource直接调用
                    if (POP_RESOURCE_OWNER.equals(owner) && POP_RESOURCE_NAME.equals(name) && POP_RESOURCE_DESC.equals(desc)) {
                        popResourceCalled = true;
                    } else {
                        // 记录其他静态方法调用，后续递归检测
                        staticMethodCalls.add(new StaticMethodCall(owner, name, desc));
                    }
                }

                // 2. 检测super.use调用（INVOKESPECIAL + 父类use方法）
                if (opcode == Opcodes.INVOKESPECIAL) {
                    if (USE_METHOD_NAME.equals(name) && USE_METHOD_DESC.equals(desc)) {
                        superUseCalled = true;
                    }
                }
            }
        }
    }

    /**
     * 检测静态方法：是否调用popResource、是否调用其他静态方法
     */
    private static class StaticMethodVisitor extends ClassVisitor {
        private final String targetMethodName;
        private final String targetMethodDesc;
        private boolean popResourceCalled = false;
        private final Set<StaticMethodCall> staticMethodCalls = new HashSet<>();

        public StaticMethodVisitor(String targetMethodName, String targetMethodDesc) {
            super(Opcodes.ASM9);
            this.targetMethodName = targetMethodName;
            this.targetMethodDesc = targetMethodDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            // 只处理目标静态方法
            if (targetMethodName.equals(name) && targetMethodDesc.equals(desc) && (access & Opcodes.ACC_STATIC) != 0) {
                return new StaticMethodInsnVisitor(Opcodes.ASM9, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        public boolean isPopResourceCalled() {
            return popResourceCalled;
        }

        public Set<StaticMethodCall> getStaticMethodCalls() {
            return staticMethodCalls;
        }

        /**
         * 拦截静态方法内的指令：检测popResource、其他静态方法调用
         */
        private class StaticMethodInsnVisitor extends AdviceAdapter {
            public StaticMethodInsnVisitor(int api, MethodVisitor methodVisitor) {
                super(api, methodVisitor, Opcodes.ACC_STATIC, targetMethodName, targetMethodDesc);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);

                // 1. 检测popResource静态调用
                if (opcode == Opcodes.INVOKESTATIC) {
                    if (POP_RESOURCE_OWNER.equals(owner) && POP_RESOURCE_NAME.equals(name) && POP_RESOURCE_DESC.equals(desc)) {
                        popResourceCalled = true;
                    } else {
                        // 2. 记录其他静态方法调用，后续递归检测
                        staticMethodCalls.add(new StaticMethodCall(owner, name, desc));
                    }
                }
            }
        }
    }
}