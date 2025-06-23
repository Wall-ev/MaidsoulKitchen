package com.github.wallev.maidsoulkitchen.util.debug.aspect;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.debug.annotation.IAspectAnnotation;
import com.github.wallev.maidsoulkitchen.debug.annotation.StackTraceRecord;
import com.github.wallev.maidsoulkitchen.util.ErrorUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.github.wallev.maidsoulkitchen.util.debug.aspect.StackTrackUtil.ALLOW;

@Aspect
@Component
public class StackTraceRecordAspect {

    @Around("@annotation(com.github.wallev.maidsoulkitchen.debug.annotation.StackTraceRecord)")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        try {

            Object proceed = joinPoint.proceed();

            String methodName = getMethodName(joinPoint);
            // 获取当前线程的堆栈跟踪信息
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            // 输出堆栈信息（通常用于调试）
            MaidsoulKitchen.LOGGER.debug("{}方法被以下调用链触发：", methodName);
            for (StackTraceElement element : stackTrace) {
                if (!ALLOW.contains(element.getModuleName())) {
                    continue;
                }
                MaidsoulKitchen.LOGGER.debug("  at {}.{}({}:{})", element.getClassName(), element.getMethodName(), element.getFileName(), element.getLineNumber());
            }

            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
            ErrorUtil.reportError2LocalPlayer(e);
            return getDefaultReturnValue(joinPoint);
        }
    }

    protected String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();
        StackTraceRecord annotation = method.getAnnotation(StackTraceRecord.class);
        IAspectAnnotation iAspectAnnotation = annotation.base();

        String methodName = iAspectAnnotation.name();
        if (methodName.isEmpty()) {
            String[] strings = signature.getDeclaringTypeName().split("\\.");
            methodName = strings[strings.length - 1] + "#" + signature.getName();
        }
        return methodName;
    }

    protected Object getDefaultReturnValue(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            StackTraceRecord annotation = method.getAnnotation(StackTraceRecord.class);
            IAspectAnnotation iAspectAnnotation = annotation.base();

            Class<?> returnType = method.getReturnType();

            // 处理基本类型

            if (returnType == String.class) {
                return iAspectAnnotation.stringVal();
            } else if (returnType == int.class || returnType == Integer.class) {
                return iAspectAnnotation.intVal();
            } else if (returnType == boolean.class || returnType == Boolean.class) {
                return iAspectAnnotation.booleanVal();
            }
            // 其他基本类型...

            // 处理复杂对象类型
            if (iAspectAnnotation.objectType() != Void.class) {
                try {
                    // 通过反射创建实例（需要无参构造函数）
                    return iAspectAnnotation.objectType().getDeclaredConstructor().newInstance();
                } catch (Exception ex) {
                    ErrorUtil.reportError2LocalPlayer(new RuntimeException("Failed to create default instance for " + iAspectAnnotation.objectType(), ex));
                }
            }

            // 对于数组、集合等复杂类型，可添加更具体的处理逻辑
            if (returnType.isArray()) {
                return java.lang.reflect.Array.newInstance(returnType.getComponentType(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 默认返回 null
        return null;
    }

}
