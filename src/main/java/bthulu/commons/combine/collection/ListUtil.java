package bthulu.commons.combine.collection;

import bthulu.commons.combine.Pair;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * 关于List的工具集合.
 *
 * 1. 集合运算：交集，并集, 差集, 补集，from Commons Collection，但对其不合理的地方做了修正
 */
@SuppressWarnings("unchecked")
public class ListUtil {

	///////////////// 集合运算 ///////////////////

	/**
	 * list1,list2的并集（在list1或list2中的对象），产生新List
	 *
	 * 对比Apache Common Collection4 ListUtils, 优化了初始大小
	 */
	public static <E> List<E> union(final List<? extends E> list1,
			final List<? extends E> list2) {
		final List<E> result = new ArrayList<>(list1.size() + list2.size());
		result.addAll(list1);
		result.addAll(list2);
		return result;
	}

	/**
	 * list1, list2的交集（同时在list1和list2的对象），产生新List
	 *
	 * copy from Apache Common Collection4 ListUtils，但其做了不合理的去重，因此重新改为性能较低但不去重的版本
	 *
	 * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
	 */
	public static <T> List<T> intersection(final List<? extends T> list1,
			final List<? extends T> list2) {
		List<? extends T> smaller = list1;
		List<? extends T> larger = list2;
		if (list1.size() > list2.size()) {
			smaller = list2;
			larger = list1;
		}
		// todo 改为性能较好的版本
		// 克隆一个可修改的副本
		List<T> newSmaller = new ArrayList<>(smaller);
		List<T> result = new ArrayList<>(smaller.size());
		for (final T e : larger) {
			if (newSmaller.contains(e)) {
				result.add(e);
				newSmaller.remove(e);
			}
		}
		return result;
	}

	/**
	 * list1, list2的差集（在list1，不在list2中的对象），产生新List.
	 *
	 * 与List.removeAll()相比，会计算元素出现的次数，如"a"在list1出现两次，而在list2中只出现一次，则差集里会保留一个"a".
	 */
	public static <T> List<T> difference(final List<? extends T> list1,
			final List<? extends T> list2) {
		final List<T> result = new ArrayList<>(list1);
		final Iterator<? extends T> iterator = list2.iterator();

		while (iterator.hasNext()) {
			result.remove(iterator.next());
		}

		return result;
	}

	/**
	 * list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List.
	 *
	 * copy from Apache Common Collection4 ListUtils，但其并集－交集时，初始大小没有对交集*2，所以做了修改
	 */
	public static <T> List<T> disjoint(final List<? extends T> list1,
			final List<? extends T> list2) {
		List<T> intersection = intersection(list1, list2);
		List<T> towIntersection = union(intersection, intersection);
		return difference(union(list1, list2), towIntersection);
	}

	///////////////// 特殊的List类型 ///////////////////
	/**
	 * 排序的ArrayList.
	 *
	 * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends Comparable> SortedArrayList<T> newSortedArrayList() {
		return new SortedArrayList<>();
	}

	/**
	 * 排序的ArrayList.
	 *
	 * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
	 */
	public static <T> SortedArrayList<T> newSortedArrayList(Comparator<? super T> c) {
		return new SortedArrayList<>(c);
	}


	/**
	 * 对新旧数据进行对比, 得出新增数据, 共有数据, 及已删除数据
	 * @param old 旧数据
	 * @param now 新数据
	 * @param keyProvider 提供实际进行对比的对象
	 * @param <E> 数据类型
	 * @return 数组, 依次为新增数据(来自now), 共有数据(k, 来自old; v, 来自now), 已删除数据(来自old)
	 */
	@SuppressWarnings("unchecked")
	public static @Nonnull
	<E> CompareResult<E> compare(@Nonnull List<E> old, @Nonnull List<E> now, Function<E, Serializable> keyProvider) {
		if (old.isEmpty()) {
			return new CompareResult(now, null, null);
		}
		if (now.isEmpty()) {
			return new CompareResult(null, null, old);
		}

		Map<Serializable, E> oldMap = new HashMap<>(MapUtil.capacity(old.size()));
		for (E e : old) {
			oldMap.put(keyProvider.apply(e), e);
		}
		Map<Serializable, E> nowMap = new HashMap<>(MapUtil.capacity(now.size()));
		for (E e : now) {
			nowMap.put(keyProvider.apply(e), e);
		}
		List<Serializable>[] compare = SetUtil.compare(oldMap.keySet(), nowMap.keySet());

		List<E> add = new ArrayList<>(compare[0].size());
		for (Serializable key : compare[0]) {
			E e = nowMap.get(key);
			add.add(e);
		}
		List<Pair<E, E>> share = new ArrayList<>(compare[1].size());
		for (Serializable key : compare[1]) {
			Pair<E, E> pair = Pair.of(oldMap.get(key), nowMap.get(key));
			share.add(pair);
		}
		List<E> delete = new ArrayList<>(compare[2].size());
		for (Serializable key : compare[2]) {
			E e = oldMap.get(key);
			delete.add(e);
		}

		return new CompareResult(add, share, delete);
	}

	public static class CompareResult<E> {
		public final List<E> add;
		public final List<Pair<E, E>> share;
		public final List<E> delete;

		public CompareResult(List<E> add, List<Pair<E, E>> share, List<E> delete) {
			this.add = add == null ? new ArrayList<>() : add;
			this.share = share == null ? new ArrayList<>() : share;
			this.delete = delete == null ? new ArrayList<>() : delete;
		}

	}
}
