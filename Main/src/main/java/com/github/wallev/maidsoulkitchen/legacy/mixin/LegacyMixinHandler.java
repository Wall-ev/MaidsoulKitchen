package com.github.wallev.maidsoulkitchen.legacy.mixin;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.Objects;

/**
 * 通过 ASM ClassReader 读取 {@link LegacyTaskMixin} 注解的 value 属性值
 */
public class LegacyMixinHandler {

    private static final String LEGACY_MIXIN_ANNOTATION_DESC = LegacyTaskMixin.class.descriptorString();

    public static boolean canMixin(String mixinClazz) throws IOException {
        Mutable<String> stringMutable = new MutableObject<>();
        // 分析类的方法
        ClassReader cr = new ClassReader(mixinClazz);
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                if (descriptor.equals(LEGACY_MIXIN_ANNOTATION_DESC)) {
                    return new AnnotationVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitEnum(String name, String descriptor, String value) {
                            if (Objects.equals(name, "mod")) {
                                stringMutable.setValue(value);
                            }
                            super.visitEnum(name, descriptor, value);
                        }
                    };
                } else {
                    return super.visitAnnotation(descriptor, visible);
                }
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        String value = stringMutable.getValue();
        if (value != null && !value.isEmpty()) {
            return Mods.by(value).versionLoad();
        }

        return false;
    }
}
