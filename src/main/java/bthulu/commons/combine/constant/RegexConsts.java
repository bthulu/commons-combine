package bthulu.commons.combine.constant;

import java.util.regex.Pattern;

/**
 * 公共正则
 */
public class RegexConsts {

	/**
	 * 手机号, 1字头＋10位数字
	 */
	public static final String REGEX_MOBILE = "^[1]\\d{10}$";

	public static final Pattern PATTERN_REGEX_MOBILE = Pattern.compile(REGEX_MOBILE);

	/**
	 * 固定电话号码，可带区号，后接6至8位数字
	 */
	public static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";

	public static final Pattern PATTERN_REGEX_TEL = Pattern.compile(REGEX_TEL);

	/**
	 * 身份证号码15位, 数字且关于生日的部分必须正确
	 */
	public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

	public static final Pattern PATTERN_REGEX_ID_CARD15 = Pattern
			.compile(REGEX_ID_CARD15);

	/**
	 * 身份证号码18位, 数字且关于生日的部分必须正确
	 */
	public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

	public static final Pattern PATTERN_REGEX_ID_CARD18 = Pattern
			.compile(REGEX_ID_CARD18);

	/**
	 * 邮箱, 有效字符(不支持中文), 且中间必须有@，后半部分必须有
	 */
	public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

	public static final Pattern PATTERN_REGEX_EMAIL = Pattern.compile(REGEX_EMAIL);

	/**
	 * URL, 必须以"http://"或"https://"打头, 后面不能有空格
	 */
	public static final String REGEX_URL = "http[s]?://[^\\s]+";

	public static final Pattern PATTERN_REGEX_URL = Pattern.compile(REGEX_URL);

	/**
	 * 汉字, 至少一位
	 */
	public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";

	public static final Pattern PATTERN_REGEX_ZH = Pattern.compile(REGEX_ZH);

	/**
	 * IP地址
	 */
	public static final String REGEX_IP = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";

	public static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

	/**
	 * 中国邮编
	 */
	public static final String REGEX_CHINA_POSTAL_CODE = "^\\d{6}$";

	public static final Pattern PATTERN_REGEX_CHINA_POSTAL_CODE = Pattern
			.compile(REGEX_CHINA_POSTAL_CODE);

}
