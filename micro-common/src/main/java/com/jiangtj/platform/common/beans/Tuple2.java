package com.jiangtj.platform.common.beans;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Tuple2<T1, T2>(T1 t1, T2 t2) {

	public <V> V map(BiFunction<T1, T2, V> biFunction) {
		return biFunction.apply(t1, t2);
	}

	public <V> Tuple2<V, T2> map1(Function<T1, V> mapHandler) {
		V v = mapHandler.apply(t1);
		return Tuple2.of(v, t2);
	}

	public <V> Tuple2<T1, V> map2(Function<T2, V> mapHandler) {
		V v = mapHandler.apply(t2);
		return Tuple2.of(t1, v);
	}

	public static <V1,V2> Tuple2<V1, V2> of(V1 t1, V2 t2) {
		return new Tuple2<>(t1, t2);
	}

}
