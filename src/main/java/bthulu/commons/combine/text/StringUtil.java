package bthulu.commons.combine.text;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 尽量使用Common Lang StringUtils, 基本覆盖了所有类库的StringUtils
 *
 * 本类仅补充少量额外方法, 尤其是针对char的运算
 *
 * 1. split char/chars
 *
 * 2. 针对char的replace first/last, startWith,endWith 等
 *
 * @author calvin
 */
public class StringUtil {

	/////////// split char 相关 ////////

	/**
	 * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
	 *
	 * copy from Commons Lange 3.5 StringUtils 并做优化
	 *
	 * @see #split(String, char, int)
	 */
	public static List<String> split(@Nullable final String str,
			final char separatorChar) {
		return split(str, separatorChar, 10);
	}

	/**
	 * 高性能的Split，针对char的分隔符号，比JDK String自带的高效.
	 *
	 * copy from Commons Lange 3.5 StringUtils, 做如下优化:
	 *
	 * 1. 最后不做数组转换，直接返回List.
	 *
	 * 2. 可设定List初始大小.
	 *
	 * 3. preserveAllTokens 取默认值false
	 * @param expectParts 预估分割后的List大小，初始化数据更精准
	 * @return 如果为null返回null, 如果为""返回空数组
	 */
	public static List<String> split(@Nullable final String str, final char separatorChar,
			int expectParts) {
		if (str == null) {
			return null;
		}

		final int len = str.length();
		if (len == 0) {
			return Collections.emptyList();
		}

		final List<String> list = new ArrayList<String>(expectParts);
		int i = 0;
		int start = 0;
		boolean match = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}
		return list;
	}

	////////// 其他 char 相关 ///////////
	/**
	 * String 有replace(char,char)，但缺少单独replace first/last的
	 */
	public static String replaceFirst(@Nullable String s, char sub, char with) {
		if (s == null) {
			return null;
		}
		int index = s.indexOf(sub);
		if (index == -1) {
			return s;
		}
		char[] str = s.toCharArray();
		str[index] = with;
		return new String(str);
	}

	/**
	 * String 有replace(char,char)替换全部char，但缺少单独replace first/last
	 */
	public static String replaceLast(@Nullable String s, char sub, char with) {
		if (s == null) {
			return null;
		}

		int index = s.lastIndexOf(sub);
		if (index == -1) {
			return s;
		}
		char[] str = s.toCharArray();
		str[index] = with;
		return new String(str);
	}

	/**
	 * 判断字符串是否以字母开头
	 *
	 * 如果字符串为Null或空，返回false
	 */
	public static boolean startWith(@Nullable CharSequence s, char c) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		return s.charAt(0) == c;
	}

	/**
	 * 判断字符串是否以字母结尾
	 *
	 * 如果字符串为Null或空，返回false
	 */
	public static boolean endWith(@Nullable CharSequence s, char c) {
		if (StringUtils.isEmpty(s)) {
			return false;
		}
		return s.charAt(s.length() - 1) == c;
	}

	/**
	 * 如果结尾字符为c, 去除掉该字符.
	 */
	public static String removeEnd(@Nullable String s, char c) {
		if (endWith(s, c)) {
			return s.substring(0, s.length() - 1);
		}
		return s;
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Byte片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Byte cherryPickByte(@Nullable String s, Character delimiter, int index) {
		return cherryPickByte(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Byte片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Byte cherryPickByte(@Nullable String s, Character delimiter, int index, int expectedSize) {
		Integer i = cherryPickInt(s, delimiter, index, expectedSize);
		return i == null ? null : i.byteValue();
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Short片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Short cherryPickShort(@Nullable String s, Character delimiter, int index) {
		return cherryPickShort(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Short片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Short cherryPickShort(@Nullable String s, Character delimiter, int index, int expectedSize) {
		Integer i = cherryPickInt(s, delimiter, index, expectedSize);
		return i == null ? null : i.shortValue();
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Int片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Integer cherryPickInt(@Nullable String s, Character delimiter, int index) {
		return cherryPickInt(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Int片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Integer cherryPickInt(@Nullable String s, Character delimiter, int index, int expectedSize) {
		String s1 = cherryPick(s, delimiter, index, expectedSize);
		if (s1 != null && s1.length() > 0) {
			try {
				return Integer.valueOf(s1);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Long片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Long cherryPickLong(@Nullable String s, Character delimiter, int index) {
		return cherryPickLong(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Long片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Long cherryPickLong(@Nullable String s, Character delimiter, int index, int expectedSize) {
		String s1 = cherryPick(s, delimiter, index, expectedSize);
		if (s1 != null && s1.length() > 0) {
			try {
				return Long.valueOf(s1);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Float片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Float cherryPickFloat(@Nullable String s, Character delimiter, int index) {
		return cherryPickFloat(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Float片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Float cherryPickFloat(@Nullable String s, Character delimiter, int index, int expectedSize) {
		if (delimiter != null && delimiter == '.') {
			throw new IllegalArgumentException("delimiter is dot while parse float");
		}
		String s1 = cherryPick(s, delimiter, index, expectedSize);
		if (s1 != null && s1.length() > 0) {
			try {
				return Float.valueOf(s1);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Double片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Double cherryPickDouble(@Nullable String s, Character delimiter, int index) {
		return cherryPickDouble(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的Double片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static Double cherryPickDouble(@Nullable String s, Character delimiter, int index, int expectedSize) {
		String s1 = cherryPick(s, delimiter, index, expectedSize);
		if (s1 != null && s1.length() > 0) {
			try {
				return Double.valueOf(s1);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的字符片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static String cherryPick(@Nullable String s, Character delimiter, int index) {
		return cherryPick(s, delimiter, index, 6);
	}

	/**
	 * 提取由分隔符拼接而成的字符串中的字符片段, 与split(s, delimiter)[index]结果一致, 但性能更好, 内存占用更低些
	 * @param s 由分隔符拼接而成的字符串
	 * @param delimiter 分隔符
	 * @param index 被提取字符片段在字符串中的索引位置
	 * @param expectedSize 所取数据可能的字符长度, 不影响返回值
	 * @return 第index个分隔符到第index-1个分隔符之间的数据, 如index为0则为第index个分隔符前的所有字符
	 */
	public static String cherryPick(@Nullable String s, Character delimiter, int index, int expectedSize) {
		if (index < 0 || s == null || s.isEmpty()) {
			return null;
		}
		if (delimiter == null && s.length() > index) {
			return String.valueOf(s.charAt(index));
		}
		if (delimiter != null) {
			int matched = 0;
			int i = 0;
			StringBuilder sb = new StringBuilder(expectedSize);
			while (i < s.length()) {
				if (matched == index) {
					char c1 = s.charAt(i);
					if (c1 == delimiter) {
						break;
					}
					sb.append(s.charAt(i));
				}
				if (s.charAt(i) == delimiter) {
					matched++;
				}
				i++;
			}
			return sb.length() == 0 ? null : sb.toString();
		}
		return null;
	}
}
