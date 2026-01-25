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

public class BlockUseMethodDetectorVER1 {
    // Block.popResource的ASM签名（根据MC版本调整）
    private static final String POP_RESOURCE_OWNER = "net/minecraft/block/Block";
    private static final String POP_RESOURCE_NAME = "popResource";
    private static final String POP_RESOURCE_DESC = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V";

    // Block.use的ASM签名（根据MC版本调整）
    private static final String USE_METHOD_NAME = "use";
    private static final String USE_METHOD_DESC = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;";

    private final Set<String> checkedClasses = new HashSet<>();

    /**
     * 检测Block子类的use方法（含继承）是否调用popResource
     * @param clazz Block子类
     * @return true=调用了，false=未调用
     */
    public boolean detectPopResourceCall(Class<? extends Block> clazz) throws IOException {
        return detectPopResourceCall(clazz.getName());
    }

    /**
     * 检测Block子类的use方法（含继承）是否调用popResource
     * @param className Block子类
     * @return true=调用了，false=未调用
     */
    public boolean detectPopResourceCall(String className) throws IOException {
        checkedClasses.clear();
        // 将类名转为ASM格式（. → /）
        return checkClass(className.replace('.', '/'));
    }

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

        // 场景1：当前类重写了use且直接调用popResource
        if (visitor.isPopResourceCalled()) {
            return true;
        }

        // 场景2：当前类重写了use且调用super.use → 检测父类
        if (visitor.isSuperUseCalled()) {
            String superClassName = classReader.getSuperName();
            if (superClassName != null && !superClassName.equals("java/lang/Object")) {
                if (checkClass(superClassName)) {
                    return true;
                }
            }
        }

        // 场景3：当前类未重写use → 直接检测父类
        if (!visitor.isUseMethodPresent()) {
            String superClassName = classReader.getSuperName();
            if (superClassName != null && !superClassName.equals("java/lang/Object")) {
                return checkClass(superClassName);
            }
        }

        return false;
    }

    /**
     * 从类加载器读取字节码，创建ClassReader
     */
    private ClassReader getClassReader(String className) throws IOException {
        String resourcePath = className + ".class";
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                return null;
            }
            return new ClassReader(is);
        }
    }

    /**
     * 检测use方法是否存在、是否调用popResource、是否调用super.use
     */
    private static class UseMethodVisitor extends ClassVisitor {
        private boolean popResourceCalled = false;
        private boolean superUseCalled = false;
        private boolean useMethodPresent = false;

        public UseMethodVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
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

        /**
         * 拦截方法指令，检测调用行为
         */
        private class UseMethodInsnVisitor extends AdviceAdapter {
            public UseMethodInsnVisitor(int api, MethodVisitor methodVisitor) {
                super(api, methodVisitor, 0, USE_METHOD_NAME, USE_METHOD_DESC);
            }

            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);

                // 检测静态调用：Block.popResource
                if (opcode == Opcodes.INVOKESTATIC) {
                    if (POP_RESOURCE_OWNER.equals(owner)
                            && POP_RESOURCE_NAME.equals(name)
                            && POP_RESOURCE_DESC.equals(desc)) {
                        popResourceCalled = true;
                        return;
                    }

                    // 直接调用父类的静态方法: popResource
                    if (POP_RESOURCE_NAME.equals(name) && POP_RESOURCE_DESC.equals(desc)) {
                        try {
                            // 检查调用的类是否是Block的子类
                            Class<?> callerClass = Class.forName(owner.replace('/', '.'));
                            if (Block.class.isAssignableFrom(callerClass)) {
                                popResourceCalled = true;
                                return;
                            }
                        } catch (ClassNotFoundException e) {
                            // 类加载失败，忽略
                        }
                    }
                }

                // 检测super.use调用（INVOKESPECIAL + 父类use方法）
                if (opcode == Opcodes.INVOKESPECIAL) {
                    if (USE_METHOD_NAME.equals(name) && USE_METHOD_DESC.equals(desc)) {
                        superUseCalled = true;
                        return;
                    }
                }
            }
        }
    }
}