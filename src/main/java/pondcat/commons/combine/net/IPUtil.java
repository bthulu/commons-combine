package pondcat.commons.combine.net;

import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddress工具类，基于Guava的InetAddresses.
 *
 * 主要包含int, String/IPV4String, InetAdress/Inet4Address之间的互相转换
 *
 * 先将字符串传换为byte[]再用InetAddress.getByAddress(byte[])，避免了InetAddress.getByName(ip)可能引起的DNS访问.
 *
 * InetAddress与String的转换其实消耗不小，如果是有限的地址，建议进行缓存.
 */
public class IPUtil {

	/**
	 * 从InetAddress转化到int, 传输和存储时, 用int代表InetAddress是最小的开销.
	 *
	 * InetAddress可以是IPV4或IPV6，都会转成IPV4.
	 *
	 * @see InetAddresses#coerceToInteger(InetAddress)
	 */
	public static int toInt(InetAddress address) {
		return InetAddresses.coerceToInteger(address);
	}

	/**
	 * InetAddress转换为String.
	 *
	 * InetAddress可以是IPV4或IPV6. 其中IPV4直接调用getHostAddress()
	 *
	 * @see InetAddresses#toAddrString(InetAddress)
	 */
	public static String toIpString(InetAddress address) {
		return InetAddresses.toAddrString(address);
	}

	/**
	 * 从int转换为Inet4Address(仅支持IPV4)
	 */
	public static Inet4Address fromInt(int address) {
		return InetAddresses.fromInteger(address);
	}

	/**
	 * 从String转换为InetAddress.
	 *
	 * IpString可以是ipv4 或 ipv6 string, 但不可以是域名.
	 *
	 * 先字符串传换为byte[]再调getByAddress(byte[])，避免了调用getByName(ip)可能引起的DNS访问.
	 */
	public static InetAddress fromIpString(String address) {
		return InetAddresses.forString(address);
	}

	/**
	 * 从IPv4String转换为InetAddress.
	 *
	 * IpString如果确定ipv4, 使用本方法减少字符分析消耗 .
	 *
	 * 先字符串传换为byte[]再调getByAddress(byte[])，避免了调用getByName(ip)可能引起的DNS访问.
	 */
	public static Inet4Address fromIpv4String(String address) {
		byte[] bytes = ipv4StringToBytes(address);
		try {
			return (Inet4Address) Inet4Address.getByAddress(bytes);
		}
		catch (UnknownHostException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * int转换到IPV4 String, from Netty NetUtil
	 */
	public static @Nonnull String intToIpv4String(int i) {
		return new StringBuilder(15).append((i >> 24) & 0xff).append('.')
				.append((i >> 16) & 0xff).append('.').append((i >> 8) & 0xff).append('.')
				.append(i & 0xff).toString();
	}

	/**
	 * int转换到IPV4 String, from Netty NetUtil
	 */
	public static @Nullable String intToIpv4String(@Nullable Integer i) {
		if (i == null) {
			return null;
		}
		return intToIpv4String(i.intValue());
	}

	/**
	 * Ipv4 String 转换到int, 转换出错会抛异常
	 */
	public static int ipv4StringToInt(String ipv4Str) {
		byte[] byteAddress = ipv4StringToBytes(ipv4Str);
		return Ints.fromByteArray(byteAddress);
	}

	/**
	 * Ipv4 String 转换到byte[]
	 * @return bytes, not null
	 */
	private static @Nonnull byte[] ipv4StringToBytes(String ipv4Str) {
		Preconditions.checkArgument(
				ipv4Str == null || ipv4Str.isEmpty() || ipv4Str.length() > 15,
				"illegal ipv4: %s", ipv4Str);

		String[] split = StringUtils.split(ipv4Str, ".");
		Preconditions.checkArgument(split != null && split.length == 4,
				"illegal ipv4: %s", ipv4Str);

		byte[] byteAddress = new byte[4];
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseInt(split[i]);
			Preconditions.checkArgument(tempInt > -1 && tempInt < 256, "illegal ipv4: %s",
					ipv4Str);
			byteAddress[i] = (byte) tempInt;
		}
		return byteAddress;
	}

}
