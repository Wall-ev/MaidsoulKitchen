package com.github.wallev.maidsoulkitchen.util.debug.aspect;

import com.github.wallev.maidsoulkitchen.debug.annotation.IAspectAnnotation;
import com.github.wallev.maidsoulkitchen.util.ErrorUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class IAspect<AT extends Annotation> {

    protected abstract Class<AT> getAnnotation();

    protected abstract IAspectAnnotation getIAspectAnnotation(AT annotation);

    protected String getMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();
        AT annotation = method.getAnnotation(this.getAnnotation());
        IAspectAnnotation iAspectAnnotation = this.getIAspectAnnotation(annotation);

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
            AT annotation = method.getAnnotation(this.getAnnotation());

            IAspectAnnotation iAspectAnnotation = this.getIAspectAnnotation(annotation);
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
