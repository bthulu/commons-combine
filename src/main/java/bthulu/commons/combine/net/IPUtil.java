package bthulu.commons.combine.net;

import bthulu.commons.combine.exception.Asserts;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

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
		ByteBuffer buf = ByteBuffer.allocate(byteAddress.length).put(byteAddress);
		buf.flip();
		return buf.getInt();
	}

	/**
	 * Ipv4 String 转换到byte[]
	 * @return bytes, not null
	 */
	private static @Nonnull byte[] ipv4StringToBytes(String ipv4Str) {
		Supplier<String> assertMsg = () -> "illegal ipv4: %s" + ipv4Str;
		Asserts.isTrue(ipv4Str != null && !ipv4Str.isEmpty() && ipv4Str.length() <= 15, assertMsg);

		String[] split = StringUtils.split(ipv4Str, ".");
		Asserts.isTrue(split != null && split.length == 4, assertMsg);

		byte[] byteAddress = new byte[4];
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseInt(split[i]);
			Asserts.isTrue(tempInt > -1 && tempInt < 256, assertMsg);
			byteAddress[i] = (byte) tempInt;
		}
		return byteAddress;
	}

}
