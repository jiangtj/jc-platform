package com.jiangtj.platform.common.stream;

import java.util.function.Function;

public record DistinctWrapper<T, R>(T value, Function<T, R> equalHanler) {


    public R getCheckValue() {
        return equalHanler.apply(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DistinctWrapper wrapper) {
            return getCheckValue().equals(wrapper.getCheckValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return equalHanler.apply(value).hashCode();
    }
}