package pondcat.commons.combine.reflect;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.Reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取Class信息的工具类
 *
 * 1. 查看类是否存在, 还原cglib处理前的类, 获取类泛型
 *
 * 2. 获取类名，包名，循环向上的全部父类，全部接口, 其他便捷函数{@link org.apache.commons.lang3.ClassUtils}
 */
public class ClassUtil {

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 探测类是否存在于调用类的classloader的classpath中
	 */
	public static boolean isPresent(String className) {
		try {
			ClassLoader classLoader = Reflection.getCallerClass().getClassLoader();
			if (classLoader != null) {
				classLoader.loadClass(className);
				return true;
			}
		}
		catch (Throwable ignored) { // NOSONAR
			// Class or one of its dependencies is not present...
		}
		return false;
	}

	/**
	 * 探测类是否存在classpath中
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			classLoader.loadClass(className);
			return true;
		}
		catch (Throwable ex) { // NOSONAR
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	/**
	 * 获取CGLib处理过后的实体的原Class.
	 */
	public static Class<?> unwrapCglib(Object instance) {
		Validate.notNull(instance, "Instance must not be null");
		Class<?> clazz = instance.getClass();
		if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if ((superClass != null) && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型,
	 *
	 * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
	 *
	 * 如无法找到, 返回Object.class.
	 *
	 * eg. public UserDao extends HibernateDao<User>
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static <T> Class<T> getClassGenericType(final Class clazz) {
		return getClassGenericType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 *
	 * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
	 *
	 * 如无法找到, 返回Object.class.
	 *
	 * 如public UserDao extends HibernateDao<User,Long>
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic declaration, start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getClassGenericType(final Class clazz, final int index) {
		Preconditions.checkArgument(index >= 0, "index must >= 0");
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						clazz.getSimpleName() + "'s superclass not ParameterizedType");
			}
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Preconditions.checkArgument(index < params.length,
				"index:%s out of %s's generic params length", index, clazz);
		if (!(params[index] instanceof Class)) {
			if (logger.isDebugEnabled()) {
				logger.warn(clazz.getName()
						+ " not set the actual class on superclass generic parameter");
			}
			return Object.class;
		}

		return (Class) params[index];
	}

}
