package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.clickharvest;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * ASM工具类，用于检测方法内部是否调用了指定的方法
 */
public class AsmMethodCallDetector {

    /**
     * 检测指定方法内部是否调用了Block.popResource方法
     *
     * @param method 要检测的方法
     * @return 如果方法内部调用了Block.popResource则返回true，否则返回false
     * @throws IOException 如果读取类文件失败
     */
    public static boolean detectPopResourceCall(Method method) throws IOException {
        // 目标方法的描述符
        final String targetOwner = "net/minecraft/world/level/block/Block";
        final String targetName = "popResource";
        final String targetDesc = "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V";

        return detectMethodCall(method, (
                methodSignature) -> {
                    return methodSignature.owner.equals(targetOwner) && methodSignature.name.equals(targetName) && methodSignature.descriptor.equals(targetDesc);
                }
        );
    }

    /**
     * 检测指定方法内部是否调用了满足条件的方法
     *
     * @param method      要检测的方法
     * @param methodPredicate 方法匹配条件
     * @return 如果方法内部调用了满足条件的方法则返回true，否则返回false
     * @throws IOException 如果读取类文件失败
     */
    public static boolean detectMethodCall(Method method, Predicate<MethodSignature> methodPredicate) throws IOException {
        // 获取类的二进制名称
        String className = method.getDeclaringClass().getName().replace('.', '/');
        String methodName = method.getName();
        String methodDesc = Type.getMethodDescriptor(method);

        // 标记是否检测到目标方法调用
        final boolean[] found = {false};

        // 创建ClassReader
        try (InputStream is = method.getDeclaringClass().getResourceAsStream("/" + className + ".class")) {
            if (is == null) {
                throw new IOException("Cannot find class file for " + className);
            }

            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    // 只访问目标方法
                    if (name.equals(methodName) && descriptor.equals(methodDesc)) {
                        return new MethodVisitor(Opcodes.ASM9) {
                            @Override
                            public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                // 检查是否是静态方法调用
                                if (opcode == Opcodes.INVOKESTATIC) {

                                    // 检查是否直接调用Block.popResource
                                    if ("net/minecraft/world/level/block/Block".equals(owner) &&
                                            "popResource".equals(name) &&
                                            "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V".equals(descriptor)) {
                                        found[0] = true;
                                    }
                                    // 或者检查是否通过子类调用Block.popResource（如果子类隐藏了该方法）
                                    else if ("popResource".equals(name) &&
                                            "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V".equals(descriptor)) {
                                        try {
                                            // 检查调用的类是否是Block的子类
                                            Class<?> callerClass = Class.forName(owner.replace('/', '.'));
                                            if (net.minecraft.world.level.block.Block.class.isAssignableFrom(callerClass)) {
                                                found[0] = true;
                                            }
                                        } catch (ClassNotFoundException e) {
                                            // 类加载失败，忽略
                                        }
                                    }

//                                    // 检查是否匹配目标方法
//                                    if (methodPredicate.test(new MethodSignature(owner, name, descriptor))) {
//                                        found[0] = true;
//                                    }
//                                    // 或者检查是否通过子类调用Block.popResource（如果子类隐藏了该方法）
//                                    else if ("popResource".equals(name) &&
//                                            "(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V".equals(descriptor)) {
//                                        try {
//                                            // 检查调用的类是否是Block的子类
//                                            Class<?> callerClass = Class.forName(owner.replace('/', '.'));
//                                            if (net.minecraft.world.level.block.Block.class.isAssignableFrom(callerClass)) {
//                                                found[0] = true;
//                                            }
//                                        } catch (ClassNotFoundException e) {
//                                            // 类加载失败，忽略
//                                        }
//                                    }
                                }
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                            }
                        };
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }

        return found[0];
    }

    /**
         * 方法签名类，用于存储方法的所有者、名称和描述符
         */
        public record MethodSignature(String owner, String name, String descriptor) {

        @Override
            public @NotNull String toString() {
                return owner + "." + name + descriptor;
            }
        }
}