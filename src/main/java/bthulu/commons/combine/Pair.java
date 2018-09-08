package bthulu.commons.combine;

import java.util.*;

/**
 * <p>
 * 部分前端开发人员, 难以解析{@link Map}序列化后的json字符串, 他们更倾向于每个entry的key和value都有一个确定的名字,
 * 如:[{"k":1,"v":"lily"},{"k":2,"v":"lucy"}], 而不是:{1:"lily",2:"lucy"}.
 * </p>
 * 因commons-lang3的{@link org.apache.commons.lang3.tuple.Pair}继承自Entry, 会导致序列化行为与map类似,
 * 故予以改写. 对需返回三个变量的, 请使用{@link org.apache.commons.lang3.tuple.Triple}.
 */
public abstract class Pair<K, V> {

	/**
	 * <p>
	 * Obtains an mutable keyvalue of from two objects inferring the generic types.
	 * </p>
	 * @param k the key element, may be null
	 * @param v the value element, may be null
	 * @param <K> the key element type
	 * @param <V> the value element type
	 * @return a keyvalue formed from the two parameters, not null
	 */
	public static <K, V> Pair<K, V> of(final K k, final V v) {
		return new MutablePair<>(k, v);
	}

	/**
	 * <p>
	 * Obtains an keyvalue of from two objects inferring the generic types.
	 * </p>
	 * @param k the key element, may be null
	 * @param v the value element, may be null
	 * @param mutable is mutable
	 * @param <K> the key element type
	 * @param <V> the value element type
	 * @return a keyvalue formed from the two parameters, not null
	 */
	public static <K, V> Pair<K, V> of(final K k, final V v, final boolean mutable) {
		return mutable ? new MutablePair<>(k, v) : new ImmutablePair<>(k, v);
	}

	/**
	 * <p>
	 * Gets the key
	 * </p>
	 * @return the key, maybe null
	 */
	public abstract K getK();

	/**
	 * <p>
	 * Sets the key
	 * </p>
	 * @param k the key
	 */
	public abstract void setK(K k);

	/**
	 * <p>
	 * Gets the value
	 * </p>
	 * @return the value, maybe null
	 */
	public abstract V getV();

	/**
	 * <p>
	 * Sets the value
	 * </p>
	 * @param v the value
	 */
	public abstract void setV(V v);

	/**
	 * <p>
	 * gets list of keyvalue from map
	 * </p>
	 * @param map
	 * @return
	 */
	public static <K, V> List<Pair<K, V>> from(Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return Collections.emptyList();
		}
		List<Pair<K, V>> list = new ArrayList<>(map.size());
		for (Map.Entry<K, V> entry : map.entrySet()) {
			list.add(of(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	/**
	 * <p>
	 * gets keyvalue from map.entry
	 * </p>
	 * @param entry
	 * @return
	 */
	public static <K, V> Pair<K, V> from(Map.Entry<K, V> entry) {
		return of(entry.getKey(), entry.getValue());
	}

	/**
	 * <p>
	 * converts keyvalue collection to map
	 * </p>
	 * @param list
	 * @return
	 */
	public static <K, V> Map<K, V> to(Collection<Pair<K, V>> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<K, V> map = new HashMap<>((int) (list.size() / 0.75) + 1);
		for (Pair<K, V> kv : list) {
			map.put(kv.getK(), kv.getV());
		}
		return map;
	}

	/**
	 * <p>
	 * <code>String</code> representation of this <code>Pair</code>.
	 * </p>
	 *
	 * <p>
	 * The default name/value delimiter '=' is always used.
	 * </p>
	 * @return <code>String</code> representation of this <code>Pair</code>
	 */
	@Override
	public String toString() {
		return getK() + "=" + getV();
	}

	public static final class ImmutablePair<K, V> extends Pair<K, V> {

		private final K k;

		private final V v;

		public ImmutablePair(K k, V v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public K getK() {
			return k;
		}

		@Override
		public void setK(K k) {
			throw new UnsupportedOperationException();
		}

		@Override
		public V getV() {
			return v;
		}

		@Override
		public void setV(V v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof ImmutablePair) {
				ImmutablePair that = (ImmutablePair) o;
				return Objects.equals(k, that.k)
						&& Objects.equals(v, that.v);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(k, v);
		}

	}

	public static final class MutablePair<K, V> extends Pair<K, V> {

		private K k;

		private V v;

		public MutablePair() {
		}

		public MutablePair(K k, V v) {
			this.k = k;
			this.v = v;
		}

		@Override
		public K getK() {
			return k;
		}

		@Override
		public void setK(K k) {
			this.k = k;
		}

		@Override
		public V getV() {
			return v;
		}

		@Override
		public void setV(V v) {
			this.v = v;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof MutablePair) {
				MutablePair that = (MutablePair) o;
				return Objects.equals(k, that.k)
						&& Objects.equals(v, that.v);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(k, v);
		}

	}

}
