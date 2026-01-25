package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.fruitharvest;

import net.minecraft.world.level.block.Block;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class FruitBlockDetectorVER1 {
    // ========== 核心常量（根据MC版本调整） ==========
    // canSurvive方法签名
    private static final String CAN_SURVIVE_NAME = "canSurvive";
    private static final String CAN_SURVIVE_DESC = "(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z";

    // LevelReader.getBlockState方法签名
    private static final String GET_BLOCK_STATE_OWNER = "net/minecraft/world/level/LevelReader";
    private static final String GET_BLOCK_STATE_NAME = "getBlockState";
    private static final String GET_BLOCK_STATE_DESC = "(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;";

    // BlockPos.above方法签名（兼容重载）
    private static final String BLOCK_POS_OWNER = "net/minecraft/core/BlockPos";
    private static final String BLOCK_POS_ABOVE_NAME = "above";
    private static final String BLOCK_POS_ABOVE_DESC_EMPTY = "()Lnet/minecraft/core/BlockPos;";
    private static final String BLOCK_POS_ABOVE_DESC_INT = "(I)Lnet/minecraft/core/BlockPos;";

    // BlockState.is方法的关键特征（匹配TagKey/HolderSet参数）
    private static final String BLOCK_STATE_OWNER = "net/minecraft/world/level/block/state/BlockState";
    private static final String BLOCK_STATE_IS_NAME = "is";
    // is方法的参数类型特征（ASM格式）
    private static final String TAG_KEY_DESC = "Lnet/minecraft/tags/TagKey;";
    private static final String HOLDER_SET_DESC = "Lnet/minecraft/core/HolderSet;";
    private static final String PREDICATE_DESC = "Ljava/util/function/Predicate;";

    // 已检测的类（避免继承链循环）
    private final Set<String> checkedClasses = new HashSet<>();

    /**
     * 判断Block是否为果实方块（含继承检测）
     * @param clazz Block子类
     * @return true=果实方块，false=非果实方块
     * @throws IOException 字节码读取失败
     */
    public boolean isFruitBlock(Class<? extends Block> clazz) throws IOException {
        checkedClasses.clear();
        return checkClass(clazz.getName().replace('.', '/'));
    }

    /**
     * 判断Block是否为果实方块（含继承检测）
     * @param className Block全类名（如net.minecraft.block.AppleBlock）
     * @return true=果实方块，false=非果实方块
     * @throws IOException 字节码读取失败
     */
    public boolean isFruitBlock(String className) throws IOException {
        checkedClasses.clear();
        return checkClass(className.replace('.', '/'));
    }

    /**
     * 判断Block是否为果实方块（含继承检测）
     * @param block Block全类名（如net.minecraft.block.AppleBlock）
     * @return true=果实方块，false=非果实方块
     * @throws IOException 字节码读取失败
     */
    public boolean isFruitBlock(Block block) throws IOException {
        checkedClasses.clear();
        return checkClass(block.getClass().getName().replace('.', '/'));
    }

    /**
     * 递归检测类及其父类的canSurvive方法
     */
    private boolean checkClass(String className) throws IOException {
        if (checkedClasses.contains(className)) {
            return false;
        }
        checkedClasses.add(className);

        // 1. 读取类字节码
        ClassReader classReader = getClassReader(className);
        if (classReader == null) {
            return false;
        }

        // 2. 检测当前类的canSurvive方法
        CanSurviveMethodVisitor visitor = new CanSurviveMethodVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // 场景1：当前类重写了canSurvive且符合果实方块逻辑
        if (visitor.isFruitBlockLogic()) {
            return true;
        }

        // 场景2：当前类未重写canSurvive → 检测父类
        if (!visitor.isCanSurviveOverridden()) {
            String superClassName = classReader.getSuperName();
            if (superClassName != null && !superClassName.equals("java/lang/Object")) {
                return checkClass(superClassName);
            }
        }

        return false;
    }

    /**
     * 从类加载器读取字节码（兼容多类加载器环境）
     */
    private ClassReader getClassReader(String className) throws IOException {
        String resourcePath = className + ".class";
        InputStream is = null;

        // 依次尝试不同类加载器（适配MC模组环境）
        is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            is = FruitBlockDetectorVER1.class.getClassLoader().getResourceAsStream(resourcePath);
        }
        if (is == null) {
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
     * 检测canSurvive方法的核心Visitor
     */
    private static class CanSurviveMethodVisitor extends ClassVisitor {
        private boolean isCanSurviveOverridden = false; // 是否重写了canSurvive
        private boolean isFruitBlockLogic = false;     // 是否符合果实方块逻辑
        // 指令追踪标记
        private boolean calledBlockPosAbove = false;    // 是否调用了BlockPos.above
        private boolean calledGetBlockState = false;    // 是否调用了LevelReader.getBlockState
        private boolean calledBlockStateIsWithTag = false; // 是否调用了BlockState.is(Tag/HolderSet)

        public CanSurviveMethodVisitor() {
            super(Opcodes.ASM9);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            // 只处理canSurvive方法
            if (CAN_SURVIVE_NAME.equals(name) && CAN_SURVIVE_DESC.equals(desc)) {
                isCanSurviveOverridden = true;
                // 直接传递原始access参数给InsnVisitor，无需访问父类的private字段
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                return new CanSurviveInsnVisitor(Opcodes.ASM9, mv, access, name, desc);
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

        public boolean isCanSurviveOverridden() {
            return isCanSurviveOverridden;
        }

        public boolean isFruitBlockLogic() {
            // 必须同时满足：调用BlockPos.above → getBlockState → is(Tag/HolderSet)
            return calledBlockPosAbove && calledGetBlockState && calledBlockStateIsWithTag;
        }

        /**
         * 指令解析器：追踪canSurvive方法内的关键调用
         * 核心修复：不依赖GeneratorAdapter的private access字段，直接使用传入的access
         */
        private class CanSurviveInsnVisitor extends AdviceAdapter {
            /**
             * 构造函数：直接接收access、name、desc参数，传递给AdviceAdapter
             * 完全避开对GeneratorAdapter private字段的访问
             */
            protected CanSurviveInsnVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
                super(api, mv, access, name, desc);
            }

            /**
             * 拦截方法调用指令（核心检测逻辑）
             */
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);

                // 1. 检测是否调用BlockPos.above（含重载）
                if (BLOCK_POS_OWNER.equals(owner) && BLOCK_POS_ABOVE_NAME.equals(name)) {
                    if (BLOCK_POS_ABOVE_DESC_EMPTY.equals(desc) || BLOCK_POS_ABOVE_DESC_INT.equals(desc)) {
                        calledBlockPosAbove = true;
                    }
                }

                // 2. 检测是否调用LevelReader.getBlockState
                if (GET_BLOCK_STATE_OWNER.equals(owner) && GET_BLOCK_STATE_NAME.equals(name) && GET_BLOCK_STATE_DESC.equals(desc)) {
                    calledGetBlockState = true;
                }

                // 3. 检测是否调用BlockState.is，且参数包含TagKey/HolderSet
                if (BLOCK_STATE_OWNER.equals(owner) && BLOCK_STATE_IS_NAME.equals(name)) {
                    calledBlockStateIsWithTag = isIsMethodWithTagOrHolderSet(desc);
                }
            }

            /**
             * 判断BlockState.is方法的参数是否包含TagKey/HolderSet
             * @param methodDesc is方法的描述符
             * @return true=参数符合要求
             */
            private boolean isIsMethodWithTagOrHolderSet(String methodDesc) {
                // 解析方法描述符，获取参数类型
                Type[] argTypes = Type.getArgumentTypes(methodDesc);
                if (argTypes.length == 0) {
                    return false;
                }

                // 情况1：is(TagKey<Block>) 或 is(HolderSet<Block>)
                String firstArgDesc = argTypes[0].getDescriptor();
                if (firstArgDesc.startsWith(TAG_KEY_DESC) || firstArgDesc.startsWith(HOLDER_SET_DESC)) {
                    return true;
                }

                // 情况2：is(TagKey<Block>, Predicate<BlockState>)
                if (argTypes.length >= 2) {
                    String secondArgDesc = argTypes[1].getDescriptor();
                    return firstArgDesc.startsWith(TAG_KEY_DESC) && secondArgDesc.startsWith(PREDICATE_DESC);
                }

                return false;
            }
        }
    }
}