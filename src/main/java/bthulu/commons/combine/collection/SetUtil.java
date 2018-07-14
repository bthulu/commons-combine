package bthulu.commons.combine.collection;

import com.google.common.collect.Sets;
import com.google.common.math.IntMath;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 关于Set的工具集合. guava中的{@link Sets}, 包含了并集#union, 交集#intersection, 差集#differenceView,
 * 补集(反交集)#disjointView
 *
 * 1. ConcurrenHashSet的构建
 */
public class SetUtil {

	/**
	 * JDK并没有提供ConcurrenHashSet，考虑到JDK的HashSet也是基于HashMap实现的，因此ConcurrenHashSet也由ConcurrenHashMap完成
	 * @return 高性能, 并发安全的Set
	 */
	public static <T> Set<T> newConcurrentHashSet() {
		return Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	/**
	 * JDK并没有提供ConcurrenHashSet，考虑到JDK的HashSet也是基于HashMap实现的，因此ConcurrenHashSet也由ConcurrenHashMap完成
	 * @param initialCapacity 初始容量
	 * @return 高性能, 并发安全的Set
	 */
	public static <T> Set<T> newConcurrentHashSet(int initialCapacity) {
		return Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity));
	}

	/**
	 * JDK并没有提供ConcurrenHashSet，考虑到JDK的HashSet也是基于HashMap实现的，因此ConcurrenHashSet也由ConcurrenHashMap完成
	 * @param concurrentHashMap 提供ConcurrenHashSet实现
	 * @return 高性能, 并发安全的Set
	 */
	public static <T> Set<T> newConcurrentHashSet(
			ConcurrentHashMap<T, Boolean> concurrentHashMap) {
		return Collections.newSetFromMap(concurrentHashMap);
	}

	/**
	 * 集合中是否含有指定的元素. 这里的元素指2的非负整数次方的数, 如0b0001, 0b0010, 0b0100, 0b1000等. 集合指元素相加后的数,
	 * 如0b0001与0b0100的集合数就是0b0101.
	 * @param set 包含多个2的非负整数次方, >=0
	 * @param i 2的非负整数次方
	 * @return 集合中是否含有指定的元素
	 */
	public static boolean containsInSet(int set, int i) {
		if (!IntMath.isPowerOfTwo(i)) {
			throw new IllegalArgumentException("i:" + i + " is not power of two");
		}
		if (set < 0) {
			throw new IllegalArgumentException("set:" + set + " must >= 0");
		}

		return (set & i) != 0;
	}

}
