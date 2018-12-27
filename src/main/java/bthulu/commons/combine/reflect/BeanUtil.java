package bthulu.commons.combine.reflect;

import bthulu.commons.combine.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 实现深度的BeanOfClasssA<->BeanOfClassB复制, 仅复制名字, 类型相同的字段. 不要是用Apache Common
 * BeanUtils进行类复制，每次就行反射查询对象的属性列表, 非常缓慢.
 */
public abstract class BeanUtil {

	private static final class BeanCopierHolder {

		private static final Map<Pair, Object> CACHE = new ConcurrentHashMap<>();

		private static byte copierType = 0;

		static {
			try {
				Class.forName("org.springframework.cglib.beans.BeanCopier");
				copierType = 1;
			}
			catch (Throwable e) {
				try {
					Class.forName("net.sf.cglib.beans.BeanCopier");
					copierType = 2;
				}
				catch (Throwable ignored) {
				}
			}
		}

	}

	private static Object create(Class source, Class target) {
		Pair key = Pair.of(source, target);
		Object copier = BeanCopierHolder.CACHE.get(key);
		if (copier == null) {
			if (BeanCopierHolder.copierType == 1) {
				copier = org.springframework.cglib.beans.BeanCopier.create(source, target,
						false);
			}
			else if (BeanCopierHolder.copierType == 2) {
				copier = net.sf.cglib.beans.BeanCopier.create(source, target, false);
			}
			else {
				throw new NullPointerException("needs cglib or spring cglib");
			}
			Object o = BeanCopierHolder.CACHE.putIfAbsent(key, copier);
			if (o != null) {
				return o;
			}
		}
		return copier;
	}

	/**
	 * 从source拷贝名称类型相同的属性至target对象并返回. 性能是apache
	 * commons或spring的BeanUtils#copyProperties的30倍以上.
	 * @param source 源
	 * @param target 目标对象
	 * @return 目标对象, 属性同source
	 */
	public static <S, T> T copyProperties(S source, T target) {
		if (BeanCopierHolder.copierType == 1) {
			org.springframework.cglib.beans.BeanCopier copier = (org.springframework.cglib.beans.BeanCopier) create(
					source.getClass(), target.getClass());
			copier.copy(source, target, null);
		}
		else {
			net.sf.cglib.beans.BeanCopier copier = (net.sf.cglib.beans.BeanCopier) create(
					source.getClass(), target.getClass());
			copier.copy(source, target, null);
		}
		return target;
	}

	/**
	 * 从source拷贝名称类型相同的属性至supplier.get()对象并返回. 性能是apache
	 * commons或spring的BeanUtils#copyProperties的30倍以上.
	 * @param source 源
	 * @param supplier 目标提供者
	 * @return supplier.get()对象, 属性同source
	 */
	public static <S, T> T copyProperties(S source, Supplier<T> supplier) {
		return copyProperties(source, supplier.get());
	}

	/**
	 * 批量拷贝名称类型相同的属性至supplier.get()对象并返回. 性能是apache
	 * commons或spring的BeanUtils#copyProperties的30倍以上.
	 * @param sources 源集合
	 * @param supplier 目标提供者
	 * @return supplier.get()对象, 属性同source
	 */
	public static <S, T> List<T> copyProperties(Collection<S> sources, Supplier<T> supplier) {
		if (sources == null) {
			return Collections.emptyList();
		}
		List<T> list = new ArrayList<>();
		for (S s : sources) {
			list.add(copyProperties(s, supplier));
		}
		return list;
	}

}
