package pondcat.commons.combine.time;

import org.apache.commons.lang3.time.FastDateFormat;
import pondcat.commons.combine.constant.ConstDatePattern;
import pondcat.commons.combine.exception.ExceptionUtil;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于{@link Date}和String之间的互转, 采用Apache Common Lang中线程安全, 性能更佳的FastDateFormat.
 *
 * 对于java.time包下的日期时间类型和String之间的互转, 采用jdk8自带的线程安全的DateTimeFormatter.
 *
 * FastDateFormat自带缓存, 如提供的Formatter不能满足使用, 可使用{@link FastDateFormat#getInstance(String)}来获取.
 * DateTimeFormatter不带缓存, 请使用
 * 即便已经做了缓存，不会每次创建对象，但直接使用静态对象仍然能减少在缓存中的查找.
 */
public abstract class DateFormatter {

	public static final FastDateFormat FORMATTER_DEFAULT = FastDateFormat.getInstance(ConstDatePattern.DEFAULT);

	public static final FastDateFormat FORMATTER_DATE = FastDateFormat.getInstance(ConstDatePattern.DATE);

	public static final FastDateFormat FORMATTER_TIME = FastDateFormat.getInstance(ConstDatePattern.TIME);

	public static final FastDateFormat FORMATTER_TIME_MINUTE = FastDateFormat.getInstance(ConstDatePattern.TIME_MINUTE);

	public static final FastDateFormat FORMATTER_DATE_MINUTE = FastDateFormat.getInstance(ConstDatePattern.DATE_MINUTE);

	public static final DateTimeFormatter FORMATTER_DEFAULT_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DEFAULT);

	public static final DateTimeFormatter FORMATTER_DATE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DATE);

	public static final DateTimeFormatter FORMATTER_TIME_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.TIME);

	public static final DateTimeFormatter FORMATTER_TIME_MINUTE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.TIME_MINUTE);

	public static final DateTimeFormatter FORMATTER_DATE_MINUTE_JDK8 = DateTimeFormatter.ofPattern(ConstDatePattern.DATE_MINUTE);

	private static final ConcurrentHashMap<String, DateTimeFormatter> CACHE = new ConcurrentHashMap<>(5);

	static {
		CACHE.put(ConstDatePattern.DEFAULT, FORMATTER_DEFAULT_JDK8);
		CACHE.put(ConstDatePattern.DATE, FORMATTER_DATE_JDK8);
		CACHE.put(ConstDatePattern.TIME, FORMATTER_TIME_JDK8);
		CACHE.put(ConstDatePattern.TIME_MINUTE, FORMATTER_TIME_JDK8);
		CACHE.put(ConstDatePattern.DATE_MINUTE, FORMATTER_TIME_MINUTE_JDK8);
	}

	/**
	 * 转换日期字符串为日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用DateFormats中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static Date parseDate(@Nonnull String pattern, @Nonnull String text) {
		try {
			return FastDateFormat.getInstance(pattern).parse(text);
		} catch (ParseException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 转换日期字符串为日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用DateFormats中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static LocalDateTime parseLocalDateTime(@Nonnull String pattern, @Nonnull String text) {
		return LocalDateTime.parse(text, getInstance(pattern));
	}

	/**
	 * 转换日期字符串为日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用DateFormats中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static LocalDate parseLocalDate(@Nonnull String pattern, @Nonnull String text) {
		return LocalDate.parse(text, getInstance(pattern));
	}

	/**
	 * 转换日期字符串为日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用DateFormats中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static LocalTime parseLocalTime(@Nonnull String pattern, @Nonnull String text) {
		return LocalTime.parse(text, getInstance(pattern));
	}

	/**
	 * 格式化日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用本类中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static String format(@Nonnull String pattern, @Nonnull Date date) {
		return FastDateFormat.getInstance(pattern).format(date);
	}

	/**
	 * 格式化日期, 仅用于pattern不固定的情况.
	 *
	 * 否则直接使用本类中封装好的FastDateFormat/DateTimeFormatter.
	 *
	 * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
	 */
	public static String format(@Nonnull String pattern, @Nonnull TemporalAccessor temporal) {
		return getInstance(pattern).format(temporal);
	}

	/**
	 * 从缓存中获取默认地区的新日期格式化器
	 * @return jdk8日期的格式化器, 地区:{@link java.util.Locale#getDefault(java.util.Locale.Category)}, {@link java.util.Locale.Category#FORMAT}
	 */
	public static DateTimeFormatter getInstance(@Nonnull String pattern) {
		DateTimeFormatter formatter = CACHE.get(pattern);
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern(pattern);
			DateTimeFormatter format = CACHE.putIfAbsent(pattern, formatter);
			if (format != null) {return format;}
		}
		return formatter;
	}
}
