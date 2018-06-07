package pondcat.commons.combine.hash;

import com.google.common.io.BaseEncoding;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 参考commons-codec的org.apache.commons.codec.digest.DigestUtils重新实现
 */
public abstract class DigestUtils {
	private static final int STREAM_BUFFER_LENGTH = 1024;

	/**
	 * Reads through a byte array and returns the digest for the data. Provided for symmetry with other methods.
	 *
	 * @param messageDigest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 */
	public static byte[] digest(final MessageDigest messageDigest, final byte[] data) {
		return messageDigest.digest(data);
	}

	/**
	 * Reads through a ByteBuffer and returns the digest for the data
	 *
	 * @param messageDigest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 */
	public static byte[] digest(final MessageDigest messageDigest, final ByteBuffer data) {
		messageDigest.update(data);
		return messageDigest.digest();
	}

	/**
	 * Reads through a File and returns the digest for the data
	 *
	 * @param messageDigest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] digest(final MessageDigest messageDigest, final File data) throws IOException {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through an InputStream and returns the digest for the data
	 *
	 * @param messageDigest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] digest(final MessageDigest messageDigest, final InputStream data) throws IOException {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Returns a <code>MessageDigest</code> for the given <code>algorithm</code>.
	 *
	 * @param algorithm
	 *            the name of the algorithm requested. See <a
	 *            href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA"
	 *            >Appendix A in the Java Cryptography Architecture Reference Guide</a> for information about standard
	 *            algorithm names.
	 * @return A digest instance.
	 * @see MessageDigest#getInstance(String)
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught.
	 */
	public static MessageDigest getDigest(final String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns a <code>MessageDigest</code> for the given <code>algorithm</code> or a default if there is a problem
	 * getting the algorithm.
	 *
	 * @param algorithm
	 *            the name of the algorithm requested. See
	 *            <a href="http://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA" >
	 *            Appendix A in the Java Cryptography Architecture Reference Guide</a> for information about standard
	 *            algorithm names.
	 * @param defaultMessageDigest
	 *            The default MessageDigest.
	 * @return A digest instance.
	 * @see MessageDigest#getInstance(String)
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught.
	 */
	public static MessageDigest getDigest(final String algorithm, final MessageDigest defaultMessageDigest) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (final Exception e) {
			return defaultMessageDigest;
		}
	}

	/**
	 * Returns an MD2 MessageDigest.
	 *
	 * @return An MD2 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because MD2 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getMd2Digest() {
		return getDigest("MD2");
	}

	/**
	 * Returns an MD5 MessageDigest.
	 *
	 * @return An MD5 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because MD5 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getMd5Digest() {
		return getDigest("MD5");
	}

	/**
	 * Returns an SHA-1 digest.
	 *
	 * @return An SHA-1 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because SHA-1 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getSha1Digest() {
		return getDigest("SHA-1");
	}

	/**
	 * Returns an SHA-256 digest.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @return An SHA-256 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because SHA-256 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getSha256Digest() {
		return getDigest("SHA-256");
	}

	/**
	 * Returns an SHA-384 digest.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @return An SHA-384 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because SHA-384 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getSha384Digest() {
		return getDigest("SHA-384");
	}

	/**
	 * Returns an SHA-512 digest.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @return An SHA-512 digest instance.
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught, which should never happen because SHA-512 is a
	 *             built-in algorithm
	 */
	public static MessageDigest getSha512Digest() {
		return getDigest("SHA-512");
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD2 digest
	 */
	public static byte[] md2(final byte[] data) {
		return getMd2Digest().digest(data);
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD2 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] md2(final InputStream data) throws IOException {
		return digest(getMd2Digest(), data);
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}
	 * @return MD2 digest
	 */
	public static byte[] md2(final String data) {
		return md2(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD2 digest as a hex string
	 */
	public static String md2Hex(final byte[] data) {
		return hexLowerCase(md2(data));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD2 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String md2Hex(final InputStream data) throws IOException {
		return hexLowerCase(md2(data));
	}

	/**
	 * Calculates the MD2 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD2 digest as a hex string
	 */
	public static String md2Hex(final String data) {
		return hexLowerCase(md2(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD5 digest
	 */
	public static byte[] md5(final byte[] data) {
		return getMd5Digest().digest(data);
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD5 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] md5(final InputStream data) throws IOException {
		return digest(getMd5Digest(), data);
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}}
	 * @return MD5 digest
	 */
	public static byte[] md5(final String data) {
		return md5(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(final byte[] data) {
		return hexLowerCase(md5(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String md5Hex(final InputStream data) throws IOException {
		return hexLowerCase(md5(data));
	}

	/**
	 * Calculates the MD5 digest and returns the value as a 32 character hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string
	 */
	public static String md5Hex(final String data) {
		return hexLowerCase(md5(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-1 digest
	 */
	public static byte[] sha1(final byte[] data) {
		return getSha1Digest().digest(data);
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-1 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] sha1(final InputStream data) throws IOException {
		return digest(getSha1Digest(), data);
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a <code>byte[]</code>.
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}
	 * @return SHA-1 digest
	 */
	public static byte[] sha1(final String data) {
		return sha1(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-1 digest as a hex string
	 */
	public static String sha1Hex(final byte[] data) {
		return hexLowerCase(sha1(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-1 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String sha1Hex(final InputStream data) throws IOException {
		return hexLowerCase(sha1(data));
	}

	/**
	 * Calculates the SHA-1 digest and returns the value as a hex string.
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-1 digest as a hex string
	 */
	public static String sha1Hex(final String data) {
		return hexLowerCase(sha1(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-256 digest
	 */
	public static byte[] sha256(final byte[] data) {
		return getSha256Digest().digest(data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-256 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] sha256(final InputStream data) throws IOException {
		return digest(getSha256Digest(), data);
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}}
	 * @return SHA-256 digest
	 */
	public static byte[] sha256(final String data) {
		return sha256(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-256 digest as a hex string
	 */
	public static String sha256Hex(final byte[] data) {
		return hexLowerCase(sha256(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-256 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String sha256Hex(final InputStream data) throws IOException {
		return hexLowerCase(sha256(data));
	}

	/**
	 * Calculates the SHA-256 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-256 digest as a hex string
	 */
	public static String sha256Hex(final String data) {
		return hexLowerCase(sha256(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-384 digest
	 */
	public static byte[] sha384(final byte[] data) {
		return getSha384Digest().digest(data);
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-384 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] sha384(final InputStream data) throws IOException {
		return digest(getSha384Digest(), data);
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}}
	 * @return SHA-384 digest
	 */
	public static byte[] sha384(final String data) {
		return sha384(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-384 digest as a hex string
	 */
	public static String sha384Hex(final byte[] data) {
		return hexLowerCase(sha384(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-384 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String sha384Hex(final InputStream data) throws IOException {
		return hexLowerCase(sha384(data));
	}

	/**
	 * Calculates the SHA-384 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-384 digest as a hex string
	 */
	public static String sha384Hex(final String data) {
		return hexLowerCase(sha384(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-512 digest
	 */
	public static byte[] sha512(final byte[] data) {
		return getSha512Digest().digest(data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-512 digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static byte[] sha512(final InputStream data) throws IOException {
		return digest(getSha512Digest(), data);
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a <code>byte[]</code>.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest; converted to bytes using {@link StandardCharsets#UTF_8}}
	 * @return SHA-512 digest
	 */
	public static byte[] sha512(final String data) {
		return sha512(data.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(final byte[] data) {
		return hexLowerCase(sha512(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-512 digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static String sha512Hex(final InputStream data) throws IOException {
		return hexLowerCase(sha512(data));
	}

	/**
	 * Calculates the SHA-512 digest and returns the value as a hex string.
	 * <p>
	 * Throws a <code>RuntimeException</code> on JRE versions prior to 1.4.0.
	 * </p>
	 *
	 * @param data
	 *            Data to digest
	 * @return SHA-512 digest as a hex string
	 */
	public static String sha512Hex(final String data) {
		return hexLowerCase(sha512(data));
	}

	/**
	 * Updates the given {@link MessageDigest}.
	 *
	 * @param messageDigest
	 *            the {@link MessageDigest} to update
	 * @param valueToDigest
	 *            the value to update the {@link MessageDigest} with
	 * @return the updated {@link MessageDigest}
	 */
	public static MessageDigest updateDigest(final MessageDigest messageDigest, final byte[] valueToDigest) {
		messageDigest.update(valueToDigest);
		return messageDigest;
	}

	/**
	 * Updates the given {@link MessageDigest}.
	 *
	 * @param messageDigest
	 *            the {@link MessageDigest} to update
	 * @param valueToDigest
	 *            the value to update the {@link MessageDigest} with
	 * @return the updated {@link MessageDigest}
	 */
	public static MessageDigest updateDigest(final MessageDigest messageDigest, final ByteBuffer valueToDigest) {
		messageDigest.update(valueToDigest);
		return messageDigest;
	}

	/**
	 * Reads through a File and updates the digest for the data
	 *
	 * @param digest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static MessageDigest updateDigest(final MessageDigest digest, final File data) throws IOException {
		final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(data));
		try {
			return updateDigest(digest, stream);
		} finally {
			stream.close();
		}
	}

	/**
	 * Reads through an InputStream and updates the digest for the data
	 *
	 * @param digest
	 *            The MessageDigest to use (e.g. MD5)
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
		final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

		while (read > -1) {
			digest.update(buffer, 0, read);
			read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}

		return digest;
	}

	/**
	 * Updates the given {@link MessageDigest} from a String (converted to bytes using UTF-8).
	 * <p>
	 * To update the digest using a different charset for the conversion,
	 * convert the String to a byte array using
	 * {@link StandardCharsets#UTF_8}
	 *
	 * @param messageDigest
	 *            the {@link MessageDigest} to update
	 * @param valueToDigest
	 *            the value to update the {@link MessageDigest} with;
	 *            converted to bytes using {@link StandardCharsets#UTF_8}}
	 * @return the updated {@link MessageDigest}
	 */
	public static MessageDigest updateDigest(final MessageDigest messageDigest, final String valueToDigest) {
		messageDigest.update(valueToDigest.getBytes(StandardCharsets.UTF_8));
		return messageDigest;
	}

	/**
	 * Test whether the algorithm is supported.
	 * @param messageDigestAlgorithm the algorithm name
	 * @return {@code true} if the algorithm can be found
	 */
	public static boolean isAvailable(final String messageDigestAlgorithm) {
		return getDigest(messageDigestAlgorithm, null) != null;
	}

	private final MessageDigest messageDigest;

	/**
	 * Creates an instance using the provided {@link MessageDigest} parameter.
	 *
	 * This can then be used to create digests using methods such as
	 * {@link #digest(byte[])} and {@link #digestAsHex(File)}.
	 *
	 * @param digest the {@link MessageDigest} to use
	 */
	public DigestUtils(final MessageDigest digest) {
		this.messageDigest = digest;
	}

	/**
	 * Creates an instance using the provided {@link MessageDigest} parameter.
	 *
	 * This can then be used to create digests using methods such as
	 * {@link #digest(byte[])} and {@link #digestAsHex(File)}.
	 *
	 * @param name the name of the {@link MessageDigest} to use
	 * @see #getDigest(String)
	 * @throws IllegalArgumentException
	 *             when a {@link NoSuchAlgorithmException} is caught.
	 */
	public DigestUtils(final String name) {
		this(getDigest(name));
	}

	/**
	 * Returns the message digest instance.
	 * @return the message digest instance
	 */
	public MessageDigest getMessageDigest() {
		return messageDigest;
	}

	/**
	 * Reads through a byte array and returns the digest for the data.
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest
	 */
	public byte[] digest(final byte[] data) {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through a byte array and returns the digest for the data.
	 *
	 * @param data
	 *            Data to digest treated as UTF-8 string
	 * @return the digest
	 */
	public byte[] digest(final String data) {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through a ByteBuffer and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest
	 *
	 */
	public byte[] digest(final ByteBuffer data) {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through a File and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public byte[] digest(final File data) throws IOException {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through an InputStream and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public byte[] digest(final InputStream data) throws IOException {
		return updateDigest(messageDigest, data).digest();
	}

	/**
	 * Reads through a byte array and returns the digest for the data.
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest as a hex string
	 */
	public String digestAsHex(final byte[] data) {
		return hexLowerCase(digest(data));
	}

	/**
	 * Reads through a byte array and returns the digest for the data.
	 *
	 * @param data
	 *            Data to digest treated as UTF-8 string
	 * @return the digest as a hex string
	 */
	public String digestAsHex(final String data) {
		return hexLowerCase(digest(data));
	}

	/**
	 * Reads through a ByteBuffer and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest as a hex string
	 *
	 */
	public String digestAsHex(final ByteBuffer data) {
		return hexLowerCase(digest(data));
	}

	/**
	 * Reads through a File and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public String digestAsHex(final File data) throws IOException {
		return hexLowerCase(digest(data));
	}

	/**
	 * Reads through an InputStream and returns the digest for the data
	 *
	 * @param data
	 *            Data to digest
	 * @return the digest as a hex string
	 * @throws IOException
	 *             On error reading from the stream
	 */
	public String digestAsHex(final InputStream data) throws IOException {
		return hexLowerCase(digest(data));
	}

	public static String hexLowerCase(byte[] bytes) {
		return BaseEncoding.base16().lowerCase().encode(bytes);
	}

	public static String hexUpperCase(byte[] bytes) {
		return hexLowerCase(bytes);
	}
}