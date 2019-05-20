package bthulu.commons.combine.collection;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * 关于Map的工具集合，
 *
 * 1. 常用函数(如是否为空, 两个map的Diff对比，针对value值的排序)
 *
 * 2. 便捷的构造函数(via guava,Java Collections，并增加了用数组，List等方式初始化Map的函数)
 *
 * 3. 来自Guava，Netty等的特殊Map类型
 *
 */
@SuppressWarnings("unchecked")
public class MapUtil {

	///////////////// from Guava的构造函数///////////////////

	/**
	 * 根据等号左边的类型, 构造类型正确的HashMap.
	 *
	 * 同时初始化第一个元素
	 */
	public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
		HashMap<K, V> map = newHashMapWithExpectedSize(1);
		map.put(key, value);
		return map;
	}

	/**
	 * 根据等号左边的类型, 构造类型正确的HashMap.
	 *
	 * 同时初始化元素.
	 */
	public static <K, V> HashMap<K, V> newHashMap(@Nonnull final K[] keys,
			@Nonnull final V[] values) {
		Validate.isTrue(keys.length == values.length,
				"keys.length is %d but values.length is %d", keys.length, values.length);

		HashMap<K, V> map = new HashMap(capacity(keys.length));

		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	public static int capacity(int expectedSize) {
		if (expectedSize < 3) {
			return expectedSize + 1;
		}
		if (expectedSize < (1 << (Integer.SIZE - 2))) {
			// This is the calculation used in JDK8 to resize when a putAll
			// happens; it seems to be the most conservative calculation we
			// can make.  0.75 is the default load factor.
			return (int) ((float) expectedSize / 0.75F + 1.0F);
		}
		return Integer.MAX_VALUE; // any large value
	}

	public static  <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
		return new HashMap<>(capacity(expectedSize));
	}

	/**
	 * 根据等号左边的类型, 构造类型正确的HashMap.
	 *
	 * 同时初始化元素.
	 */
	public static <K, V> HashMap<K, V> newHashMap(@Nonnull final Collection<K> keys,
			@Nonnull final Collection<V> values) {
		int keySize = keys.size();
		int valueSize = values.size();
		Validate.isTrue(keySize == valueSize,
				"keys.length is %s  but values.length is %s", keySize, valueSize);

		HashMap<K, V> map = newHashMapWithExpectedSize(keySize);
		Iterator<K> keyIt = keys.iterator();
		Iterator<V> valueIt = values.iterator();

		while (keyIt.hasNext()) {
			map.put(keyIt.next(), valueIt.next());
		}

		return map;
	}

	//////////// 按值排序及取TOP N的操作 /////////
	/**
	 * 对一个Map按Value进行排序，返回排序LinkedHashMap，多用于Value是Counter的情况.
	 * @param reverse 按Value的倒序 or 正序排列
	 */
	public static <K, V extends Comparable> Map<K, V> sortByValue(Map<K, V> map,
			final boolean reverse) {
		return sortByValueInternal(map, reverse
				? new ComparableEntryValueComparator<K, V>().reversed()
				: new ComparableEntryValueComparator<>());
	}

	/**
	 * 对一个Map按Value进行排序，返回排序LinkedHashMap.
	 */
	public static <K, V> Map<K, V> sortByValue(Map<K, V> map,
			final Comparator<? super V> comparator) {
		return sortByValueInternal(map, new EntryValueComparator<>(comparator));
	}

	private static <K, V> Map<K, V> sortByValueInternal(Map<K, V> map,
			Comparator<Entry<K, V>> comparator) {
		Set<Entry<K, V>> entrySet = map.entrySet();
		Entry<K, V>[] entryArray = entrySet.toArray(new Entry[0]);

		Arrays.sort(entryArray, comparator);

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : entryArray) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 对一个Map按Value进行排序，返回排序LinkedHashMap，最多只返回n条，多用于Value是Counter的情况.
	 * @param reverse 按Value的倒序 or 正序排列
	 */
	public static <K, V extends Comparable> Map<K, V> topNByValue(Map<K, V> map,
			final boolean reverse, int n) {

		return topNByValueInternal(map, n, reverse
				? new ComparableEntryValueComparator<K, V>().reversed()
				: new ComparableEntryValueComparator<>());
	}

	/**
	 * 对一个Map按Value进行排序，返回排序LinkedHashMap, 最多只返回n条，多用于Value是Counter的情况.
	 */
	public static <K, V> Map<K, V> topNByValue(Map<K, V> map,
			final Comparator<? super V> comparator, int n) {
		return topNByValueInternal(map, n, new EntryValueComparator<>(comparator));
	}

	private static <K, V> Map<K, V> topNByValueInternal(Map<K, V> map, int n,
			Comparator<Entry<K, V>> comparator) {
		Set<Entry<K, V>> entrySet = map.entrySet();
		Entry<K, V>[] entryArray = entrySet.toArray(new Entry[0]);
		Arrays.sort(entryArray, comparator);

		Map<K, V> result = new LinkedHashMap<>();
		int size = Math.min(n, entryArray.length);
		for (int i = 0; i < size; i++) {
			Entry<K, V> entry = entryArray[i];
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private static final class ComparableEntryValueComparator<K, V extends Comparable>
			implements Comparator<Entry<K, V>> {

		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return (o1.getValue()).compareTo(o2.getValue());
		}

	}

	private static final class EntryValueComparator<K, V>
			implements Comparator<Entry<K, V>> {

		private final Comparator<? super V> comparator;

		private EntryValueComparator(Comparator<? super V> comparator2) {
			this.comparator = comparator2;
		}

		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return comparator.compare(o1.getValue(), o2.getValue());
		}

	}

	/**
	 * 创建值为可更改的Integer的HashMap. 可更改的Integer在更改时不需要重新创建Integer对象，节约了内存
	 * @param initialCapacity 建议为16
	 * @param loadFactor 建议为0.5
	 */
	public static <K> HashMap<K, MutableInt> newMutableIntValueMap(int initialCapacity,
			float loadFactor) {
		return new HashMap<>(initialCapacity, loadFactor);
	}

	/**
	 * 创建值为可更改的Long的HashMap. 可更改的Long在更改时不需要重新创建Long对象，节约了内存
	 * @param initialCapacity 建议为16
	 * @param loadFactor 建议为0.5
	 */
	public static <K> HashMap<K, MutableLong> newMutableLongValueMap(int initialCapacity,
			float loadFactor) {
		return new HashMap<>(initialCapacity, loadFactor);
	}

}
