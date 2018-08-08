package bthulu.commons.combine.io;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * IO Stream/Reader相关工具集. 固定encoding为UTF8.
 *
 * 建议使用Apache Commons IO和Guava关于IO的工具类(com.google.common.io.*), 在未引入Commons
 * IO时可以用本类做最基本的事情.
 *
 * 1. 读出InputStream/Reader内容到String或List<String> 2. 将String写到OutputStream/Writer 3.
 * InputStream/Reader与OutputStream/Writer之间的复制
 *
 */
public class IOUtil {

	/**
	 * 简单读取InputStream到String.
	 */
	public static String toString(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
		return toString(reader);
	}

	/**
	 * 简单读取InputStream到String.
	 */
	public static String toString(InputStream input, Charset cs) throws IOException {
		InputStreamReader reader = new InputStreamReader(input, cs);
		return toString(reader);
	}

	/**
	 * 简单读取Reader到String
	 *
	 * @see CharStreams#toString
	 */
	public static String toString(Reader input) throws IOException {
		return CharStreams.toString(input);
	}

	/**
	 * 简单读取Reader的每行内容到List<String>
	 */
	public static List<String> toLines(final InputStream input) throws IOException {
		return toLines(new InputStreamReader(input, StandardCharsets.UTF_8));
	}

	/**
	 * 简单读取Reader的每行内容到List<String>
	 */
	public static List<String> toLines(final InputStream input, Charset cs) throws IOException {
		return toLines(new InputStreamReader(input, cs));
	}

	/**
	 * 简单读取Reader的每行内容到List<String>
	 *
	 * @see CharStreams#readLines
	 */
	public static List<String> toLines(final Reader input) throws IOException {
		return CharStreams.readLines(toBufferedReader(input));
	}

	/**
	 * 简单写入String到OutputStream.
	 */
	public static void write(final String data, final OutputStream output)
			throws IOException {
		if (data != null) {
			output.write(data.getBytes(StandardCharsets.UTF_8));
		}
	}

	/**
	 * 简单写入String到OutputStream.
	 */
	public static void write(final String data, final OutputStream output, Charset cs) throws IOException {
		if (data != null) {
			output.write(data.getBytes(cs));
		}
	}

	/**
	 * 简单写入String到Writer.
	 */
	public static void write(final String data, final Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * 在Reader与Writer间复制内容
	 *
	 * @see CharStreams#copy
	 */
	public static long copy(final Reader input, final Writer output) throws IOException {
		return CharStreams.copy(input, output);
	}

	/**
	 * 在InputStream与OutputStream间复制内容
	 *
	 * @see ByteStreams#copy
	 */
	public static long copy(final InputStream input, final OutputStream output)
			throws IOException {
		return ByteStreams.copy(input, output);
	}

	public static BufferedReader toBufferedReader(final Reader reader) {
		return reader instanceof BufferedReader ? (BufferedReader) reader
				: new BufferedReader(reader);
	}

}
