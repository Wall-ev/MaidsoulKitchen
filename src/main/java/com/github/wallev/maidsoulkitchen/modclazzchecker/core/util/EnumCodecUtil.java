package com.github.wallev.maidsoulkitchen.modclazzchecker.core.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumCodecUtil {

    public static <E extends Enum<E> & IEnum> EnumCodec<E> fromEnum(Supplier<E[]> enums) {
        return fromEnumWithMapping(enums, (name) -> {
            return name;
        });
    }

    static <E extends Enum<E> & IEnum> EnumCodec<E> fromEnumWithMapping(Supplier<E[]> enums, Function<String, String> keyFun) {
        E[] ae = enums.get();
        if (ae.length > 16) {
            Map<String, E> map = Arrays.stream(ae).collect(Collectors.toMap((p_274905_) -> {
                return keyFun.apply(p_274905_.getSerializedName());
            }, (p_274903_) -> {
                return p_274903_;
            }));
            return new EnumCodec<>(ae, (p_216438_) -> {
                return (E)(p_216438_ == null ? null : map.get(p_216438_));
            });
        } else {
            return new EnumCodec<>(ae, (p_274908_) -> {
                for(E e : ae) {
                    if (keyFun.apply(e.getSerializedName()).equals(p_274908_)) {
                        return e;
                    }
                }

                return (E)null;
            });
        }
    }

    static Keyable keys(final IEnum[] enums) {
        return new Keyable() {
            public <T> Stream<T> keys(DynamicOps<T> p_184758_) {
                return Arrays.stream(enums).map(IEnum::getSerializedName).map(p_184758_::createString);
            }
        };
    }

    public static <E> Codec<E> stringResolverCodec(Function<E, String> p_184406_, Function<String, E> p_184407_) {
        return Codec.STRING.flatXmap((p_184404_) -> Optional.ofNullable(p_184407_.apply(p_184404_)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown element name:" + p_184404_)), (p_184401_) -> (DataResult)Optional.ofNullable((String)p_184406_.apply(p_184401_)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Element with unknown name: " + p_184401_)));
    }

    public static <E> Codec<E> orCompressed(final Codec<E> pFirst, final Codec<E> pSecond) {
        return new Codec<E>() {
            public <T> DataResult<T> encode(E p_184483_, DynamicOps<T> p_184484_, T p_184485_) {
                return p_184484_.compressMaps() ? pSecond.encode(p_184483_, p_184484_, p_184485_) : pFirst.encode(p_184483_, p_184484_, p_184485_);
            }

            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> p_184480_, T p_184481_) {
                return p_184480_.compressMaps() ? pSecond.decode(p_184480_, p_184481_) : pFirst.decode(p_184480_, p_184481_);
            }

            public String toString() {
                return pFirst + " orCompressed " + pSecond;
            }
        };
    }


    public static <E> Codec<E> idResolverCodec(ToIntFunction<E> p_184422_, IntFunction<E> p_184423_, int p_184424_) {
        return Codec.INT.flatXmap((p_184414_) -> {
            return Optional.ofNullable(p_184423_.apply(p_184414_)).map(DataResult::success).orElseGet(() -> {
                return DataResult.error(() -> {
                    return "Unknown element id: " + p_184414_;
                });
            });
        }, (p_274850_) -> {
            int i = p_184422_.applyAsInt(p_274850_);
            return i == p_184424_ ? DataResult.error(() -> {
                return "Element with unknown id: " + p_274850_;
            }) : DataResult.success(i);
        });
    }

    public static class EnumCodec<E extends Enum<E> & IEnum> implements Codec<E> {
        private final Codec<E> codec;
        private final Function<String, E> resolver;

        public EnumCodec(E[] enums, Function<String, E> resolver) {
            this.codec = EnumCodecUtil.orCompressed(EnumCodecUtil.stringResolverCodec((e) -> {
                return e.getSerializedName();
            }, resolver), EnumCodecUtil.idResolverCodec((e) -> {
                return e.ordinal();
            }, (p_216459_) -> {
                return (E)(p_216459_ >= 0 && p_216459_ < enums.length ? enums[p_216459_] : null);
            }, -1));
            this.resolver = resolver;
        }

        public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> pOps, T pInput) {
            return this.codec.decode(pOps, pInput);
        }

        public <T> DataResult<T> encode(E pInput, DynamicOps<T> pOps, T pPrefix) {
            return this.codec.encode(pInput, pOps, pPrefix);
        }

        @Nullable
        public E byName(@Nullable String pName) {
            return this.resolver.apply(pName);
        }

        public E byName(@Nullable String pName, E pDefaultValue) {
            return Objects.requireNonNullElse(this.byName(pName), pDefaultValue);
        }
    }

}
