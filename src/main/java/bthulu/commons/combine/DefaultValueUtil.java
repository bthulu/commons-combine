package bthulu.commons.combine;

import java.util.*;

public abstract class DefaultValueUtil {
    public static <T> T ifNull(T t, T def) {
        return t == null ? def : t;
    }

    public static Boolean ifNull(Boolean t) {
        return t == null ? Boolean.FALSE : t;
    }

    public static Byte ifNull(Byte t) {
        return t == null ? 0 : t;
    }

    public static Short ifNull(Short t) {
        return t == null ? 0 : t;
    }

    public static Integer ifNull(Integer t) {
        return t == null ? 0 : t;
    }

    public static Long ifNull(Long t) {
        return t == null ? 0 : t;
    }

    public static Float ifNull(Float t) {
        return t == null ? 0 : t;
    }

    public static Double ifNull(Double t) {
        return t == null ? 0 : t;
    }

    public static String ifNull(String t) {
        return t == null ? "" : t;
    }

    public static <E> List<E> ifNull(List<E> t) {
        return t == null ? Collections.emptyList() : t;
    }

    public static <E> Set<E> ifNull(Set<E> t) {
        return t == null ? Collections.emptySet() : t;
    }

    public static <K, V> Map<K, V> ifNull(Map<K, V> t) {
        return t == null ? Collections.emptyMap() : t;
    }

    public static String ifEmpty(String t, String def) {
        return t == null || t.isEmpty() ? def : t;
    }

    public static <E> Collection ifEmpty(Collection<E> t, Collection<E> def) {
        return t == null || t.isEmpty() ? def : t;
    }

    public static boolean isNullOrDefault(Boolean t) {
        return t == null || !t;
    }

    public static boolean isNullOrDefault(Number t) {
        return t == null || t.intValue() == 0;
    }

    /**
     * 是否全为null或空值
     * @param t 检测对象
     * @return 是否全为null或空值
     */
    public static boolean isNullOrDefault(Number... t) {
        for (Number n : t) {
            if (!isNullOrDefault(n)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否含有null或空值
     * @param t 检测对象
     * @return 是否含有null或空值
     */
    public static boolean hasNullOrDefault(Number... t) {
        for (Number n : t) {
            if (isNullOrDefault(n)) {
                return true;
            }
        }
        return false;
    }
}
