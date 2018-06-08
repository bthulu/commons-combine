package pondcat.commons.combine;

import java.util.*;

/**
 * <p>
 * 部分前端开发人员, 难以解析{@link Map}序列化后的json字符串, 他们更倾向于每个entry的key和value都有一个确定的名字,
 * 如:[{"left":1,"right":"lily"},{"left":2,"right":"lucy"}], 而不是:{1:"lily",2:"lucy"}.
 * </p>
 * 因commons-lang3的{@link org.apache.commons.lang3.tuple.Pair}继承自Entry, 会导致序列化行为与map类似,
 * 故予以改写. 对需返回三个变量的, 请使用{@link org.apache.commons.lang3.tuple.Triple}.
 */
public abstract class Pair<L, R> {

	/**
	 * <p>
	 * Obtains an mutable keyvalue of from two objects inferring the generic types.
	 * </p>
	 * @param l the key element, may be null
	 * @param r the value element, may be null
	 * @param <L> the key element type
	 * @param <R> the value element type
	 * @return a keyvalue formed from the two parameters, not null
	 */
	public static <L, R> Pair<L, R> of(final L l, final R r) {
		return new MutablePair<>(l, r);
	}

	/**
	 * <p>
	 * Obtains an keyvalue of from two objects inferring the generic types.
	 * </p>
	 * @param l the key element, may be null
	 * @param r the value element, may be null
	 * @param mutable is mutable
	 * @param <L> the key element type
	 * @param <R> the value element type
	 * @return a keyvalue formed from the two parameters, not null
	 */
	public static <L, R> Pair<L, R> of(final L l, final R r, final boolean mutable) {
		return mutable ? new MutablePair<>(l, r) : new ImmutablePair<>(l, r);
	}

	/**
	 * <p>
	 * Gets the key
	 * </p>
	 * @return the key, maybe null
	 */
	public abstract L getLeft();

	/**
	 * <p>
	 * Sets the key
	 * </p>
	 * @param l the key
	 */
	public abstract void setLeft(L l);

	/**
	 * <p>
	 * Gets the value
	 * </p>
	 * @return the value, maybe null
	 */
	public abstract R getRight();

	/**
	 * <p>
	 * Sets the value
	 * </p>
	 * @param r the value
	 */
	public abstract void setRight(R r);

	/**
	 * <p>
	 * gets list of keyvalue from map
	 * </p>
	 * @param map
	 * @return
	 */
	public static <L, R> List<Pair<L, R>> from(Map<L, R> map) {
		if (map == null || map.isEmpty()) {
			return Collections.emptyList();
		}
		List<Pair<L, R>> list = new ArrayList<>(map.size());
		for (Map.Entry<L, R> entry : map.entrySet()) {
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
	public static <L, R> Pair<L, R> from(Map.Entry<L, R> entry) {
		return of(entry.getKey(), entry.getValue());
	}

	/**
	 * <p>
	 * converts keyvalue collection to map
	 * </p>
	 * @param list
	 * @return
	 */
	public static <L, R> Map<L, R> to(Collection<Pair<L, R>> list) {
		if (list == null || list.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<L, R> map = new HashMap<>((int) (list.size() / 0.75) + 1);
		for (Pair<L, R> kv : list) {
			map.put(kv.getLeft(), kv.getRight());
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
		return getLeft() + "=" + getRight();
	}

	public static final class ImmutablePair<L, R> extends Pair<L, R> {

		private final L left;

		private final R right;

		private ImmutablePair(L l, R r) {
			this.left = l;
			this.right = r;
		}

		@Override
		public L getLeft() {
			return left;
		}

		@Override
		public void setLeft(L l) {
			throw new UnsupportedOperationException();
		}

		@Override
		public R getRight() {
			return right;
		}

		@Override
		public void setRight(R r) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof ImmutablePair) {
				ImmutablePair that = (ImmutablePair) o;
				return Objects.equals(left, that.left)
						&& Objects.equals(right, that.right);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(left, right);
		}

	}

	public static final class MutablePair<L, R> extends Pair<L, R> {

		private L left;

		private R right;

		private MutablePair() {
			// supports json deserialize
		}

		private MutablePair(L l, R r) {
			this.left = l;
			this.right = r;
		}

		@Override
		public L getLeft() {
			return left;
		}

		@Override
		public void setLeft(L l) {
			this.left = l;
		}

		@Override
		public R getRight() {
			return right;
		}

		@Override
		public void setRight(R r) {
			this.right = r;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o instanceof MutablePair) {
				MutablePair that = (MutablePair) o;
				return Objects.equals(left, that.left)
						&& Objects.equals(right, that.right);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(left, right);
		}

	}

}
