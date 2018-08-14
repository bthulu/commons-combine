package bthulu.commons.combine.time;

import bthulu.commons.combine.constant.ConstDatePattern;
import bthulu.commons.combine.exception.ExceptionUtil;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于{@link Date}和String之间的互转, 采用Apache Common Lang中线程安全, 性能更佳的FastDateFormat.
 * <p>
 * 对于java.time包下的日期时间类型和String之间的互转, 采用jdk8自带的线程安全的DateTimeFormatter.
 * <p>
 * FastDateFormat自带缓存, 如提供的Formatter不能满足使用,
 * 可使用{@link FastDateFormat#getInstance(String)}来获取. DateTimeFormatter不带缓存, 请使用
 * 即便已经做了缓存，不会每次创建对象，但直接使用静态对象仍然能减少在缓存中的查找.
 */
public abstract class DateFormatter {

	/**
	 * pattern:yyyy-MM-dd HH:mm:ss
	 *
	 * @see ConstDatePattern#DEFAULT
	 */
	public static final FastDateFormat DEFAULT = FastDateFormat.getInstance(ConstDatePattern.DEFAULT);

	/**
	 * pattern:yyyy-MM-dd
	 *
	 * @see ConstDatePattern#DATE
	 */
	public static final FastDateFormat DATE = FastDateFormat.getInstance(ConstDatePattern.DATE);

	/**
	 * pattern:HH:mm:ss
	 *
	 * @see ConstDatePattern#TIME
	 */
	public static final FastDateFormat TIME = FastDateFormat.getInstance(ConstDatePattern.TIME);

	/**
	 * pattern:HH:mm
	 *
	 * @see ConstDatePattern#TIME_MINUTE
	 */
	public static final FastDateFormat TIME_MINUTE = FastDateFormat.getInstance(ConstDatePattern.TIME_MINUTE);

	/**
	 * pattern:yyyy-MM-dd HH:mm
	 *
	 * @see ConstDatePattern#DATE_MINUTE
	 */
	public static final FastDateFormat DATE_MINUTE = FastDateFormat.getInstance(ConstDatePattern.DATE_MINUTE);

	/**
	 * pattern:yyyy-MM-dd HH:mm:ss
	 *
	 * @see ConstDatePattern#DEFAULT
	 */
	public static final DateTimeFormatter DEFAULT_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DEFAULT);

	/**
	 * pattern:yyyy-MM-dd
	 *
	 * @see ConstDatePattern#DATE
	 */
	public static final DateTimeFormatter DATE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DATE);

	/**
	 * pattern:HH:mm:ss
	 *
	 * @see ConstDatePattern#TIME
	 */
	public static final DateTimeFormatter TIME_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.TIME);

	/**
	 * pattern:HH:mm
	 *
	 * @see ConstDatePattern#TIME_MINUTE
	 */
	public static final DateTimeFormatter TIME_MINUTE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.TIME_MINUTE);

	/**
	 * pattern:yyyy-MM-dd HH:mm
	 *
	 * @see ConstDatePattern#DATE_MINUTE
	 */
	public static final DateTimeFormatter DATE_MINUTE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DATE_MINUTE);

	private static final ConcurrentHashMap<String, DateTimeFormatter> CACHE = new ConcurrentHashMap<>(9);

	static {
		CACHE.put(ConstDatePattern.DEFAULT, DEFAULT_JDK8);
		CACHE.put(ConstDatePattern.DATE, DATE_JDK8);
		CACHE.put(ConstDatePattern.TIME, TIME_JDK8);
		CACHE.put(ConstDatePattern.TIME_MINUTE, TIME_MINUTE_JDK8);
		CACHE.put(ConstDatePattern.DATE_MINUTE, DATE_MINUTE_JDK8);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @return 时间日期
	 */
	public static Date parseDate(@Nonnull String pattern, @Nullable String text) {
		return parseDate(pattern, text, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @return 时间日期
	 */
	public static Date parseDate(@Nonnull String pattern, @Nullable String text, boolean nullIfFailed) {
		try {
			if (text == null || text.isEmpty()) {return null;}
			return FastDateFormat.getInstance(pattern).parse(text);
		} catch (Exception e) {
			return handleFailed(e, nullIfFailed);
		}
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @return 时间日期
	 */
	public static Instant parseInstant(@Nonnull String pattern, @Nullable String text) {
		return parseInstant(pattern, text, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @return 时间日期
	 */
	public static Instant parseInstant(@Nonnull String pattern, @Nullable String text, boolean nullIfFailed) {
		return parse(pattern, text, Instant::from, nullIfFailed);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @return 时间日期
	 */
	public static LocalDateTime parseLocalDateTime(@Nonnull String pattern, @Nullable String text) {
		return parseLocalDateTime(pattern, text, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @return 时间日期
	 */
	public static LocalDateTime parseLocalDateTime(@Nonnull String pattern, @Nullable String text,
			boolean nullIfFailed) {
		return parse(pattern, text, LocalDateTime::from, nullIfFailed);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @return 时间日期
	 */
	public static LocalDate parseLocalDate(@Nonnull String pattern, @Nullable String text) {
		return parseLocalDate(pattern, text, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @return 时间日期
	 */
	public static LocalDate parseLocalDate(@Nonnull String pattern, @Nullable String text, boolean nullIfFailed) {
		return parse(pattern, text, LocalDate::from, nullIfFailed);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @return 时间日期
	 */
	public static LocalTime parseLocalTime(@Nonnull String pattern, @Nullable String text) {
		return parseLocalTime(pattern, text, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @return 时间日期
	 */
	public static LocalTime parseLocalTime(@Nonnull String pattern, @Nullable String text, boolean nullIfFailed) {
		return parse(pattern, text, LocalTime::from, nullIfFailed);
	}

	/**
	 * 转换时间日期字符串为指定时间日期类型, 目前仅支持LocalDateTime,LocalDate,LocalTime,Instant,Date, 其余类型抛异常UnsupportedOperationException
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param type 目标时间日期类型对应的type或class
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, @Nonnull Type type) {
		return parse(pattern, text, type, false);
	}

	/**
	 * 转换时间日期字符串为指定时间日期类型, 目前仅支持LocalDateTime,LocalDate,LocalTime,Instant,Date, 其余类型抛异常UnsupportedOperationException
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param type 目标时间日期类型对应的type或class
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, @Nonnull Type type,
			boolean nullIfFailed) {
		if (type == LocalDateTime.class) {
			return (T) parseLocalDateTime(pattern, text, nullIfFailed);
		} else if (type == Date.class) {
			return (T) parseDate(pattern, text, nullIfFailed);
		} else if (type == LocalDate.class) {
			return (T) parseLocalDate(pattern, text, nullIfFailed);
		} else if (type == Instant.class) {
			return (T) parseInstant(pattern, text, nullIfFailed);
		} else if (type == LocalTime.class) {
			return (T) parseLocalTime(pattern, text, nullIfFailed);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * 转换时间日期字符串为指定时间日期类型, 目前仅支持LocalDateTime,LocalDate,LocalTime,Instant,Date, 其余类型抛异常UnsupportedOperationException
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param tClass 目标时间日期类型对应的type或class
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, @Nonnull Class<T> tClass) {
		return parse(pattern, text, tClass, false);
	}

	/**
	 * 转换时间日期字符串为指定时间日期类型, 目前仅支持LocalDateTime,LocalDate,LocalTime,Instant,Date, 其余类型抛异常UnsupportedOperationException
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param tClass 目标时间日期类型对应的type或class
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, @Nonnull Class<T> tClass,
			boolean nullIfFailed) {
		return parse(pattern, text, tClass, nullIfFailed);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param query 时间日期类型转换, {@link LocalDateTime#from(TemporalAccessor)}, {@link Instant#from(TemporalAccessor)}...
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, TemporalQuery<T> query) {
		return parse(pattern, text, query, false);
	}

	/**
	 * 转换时间日期字符串为时间日期
	 * @param pattern 时间日期格式
	 * @param text 待转换字符串
	 * @param query 时间日期类型转换, {@link LocalDateTime#from(TemporalAccessor)}, {@link Instant#from(TemporalAccessor)}...
	 * @param nullIfFailed true, 转换失败返回null; false, 转换失败抛异常
	 * @param <T> 目标时间日期类型
	 * @return 时间日期
	 */
	public static <T> T parse(@Nonnull String pattern, @Nullable String text, TemporalQuery<T> query,
			boolean nullIfFailed) {
		try {
			if (text == null || text.isEmpty()) {return null;}
			return getInstance(pattern).parse(text, query);
		} catch (Exception e) {
			return handleFailed(e, nullIfFailed);
		}
	}

	/**
	 * 格式化日期, 仅用于pattern不固定的情况.
	 * <p>
	 * 否则直接使用本类中封装好的FastDateFormat/DateTimeFormatter.
	 * <p>
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static String format(@Nonnull String pattern, @Nullable Date date) {
		return FastDateFormat.getInstance(pattern).format(date);
	}

	/**
	 * 格式化日期, 仅用于pattern不固定的情况.
	 * <p>
	 * 否则直接使用本类中封装好的FastDateFormat/DateTimeFormatter.
	 * <p>
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static String format(@Nonnull String pattern, @Nonnull TemporalAccessor temporal) {
		return getInstance(pattern).format(temporal);
	}

	/**
	 * 从缓存中获取默认地区的新日期格式化器
	 *
	 * @return jdk8日期的格式化器,
	 * 地区:{@link java.util.Locale#getDefault(java.util.Locale.Category)},
	 * {@link java.util.Locale.Category#FORMAT}
	 */
	public static DateTimeFormatter getInstance(@Nonnull String pattern) {
		DateTimeFormatter formatter = CACHE.get(pattern);
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern(pattern);
			DateTimeFormatter format = CACHE.putIfAbsent(pattern, formatter);
			if (format != null) {
				return format;
			}
		}
		return formatter;
	}

	private static <T> T handleFailed(Exception e, boolean nullIfFailed) {
		if (nullIfFailed) {
			return null;
		} else {
			throw ExceptionUtil.unchecked(e);
		}
	}
}
