package pondcat.commons.combine.io;

import com.google.common.graph.Traverser;
import com.google.common.io.Files;
import pondcat.commons.combine.text.WildcardMatcher;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileTreeWalker {

	/**
	 * 前序递归列出所有文件, 包含文件与目录，及根目录本身.
	 * 
	 * 前序即先列出父目录，在列出子目录. 如要后序遍历, 直接使用Files.fileTreeTraverser()
	 */
	public static List<File> listAll(File rootDir) {
		return getStream(rootDir).collect(Collectors.toList());
	}

	/**
	 * 前序递归列出所有文件, 只包含文件.
	 */
	public static List<File> listFile(File rootDir) {
		return getStream(rootDir).filter(File::isFile).collect(Collectors.toList());
	}

	private static Stream<File> getStream(File rootDir) {
		return StreamSupport.stream(fileTraverser().depthFirstPreOrder(rootDir).spliterator(), false);
	}

	/**
	 * 前序递归列出所有文件, 列出后缀名匹配的文件. （后缀名不包含.）
	 */
	public static List<File> listFileWithExtension(final File rootDir, final String extension) {
		return getStream(rootDir).filter(k -> k.isFile() && Objects.equals(extension, FileUtil.getFileExtension(extension)))
				.collect(Collectors.toList());
	}

	/**
	 * 前序递归列出所有文件, 列出文件名匹配通配符的文件
	 * 
	 * 如 ("/a/b/hello.txt", "he*") 将被返回
	 */
	public static List<File> listFileWithWildcardFileName(final File rootDir, final String fileNamePattern) {
		return getStream(rootDir).filter(k -> WildcardMatcher.match(k.getName(), fileNamePattern))
				.collect(Collectors.toList());
	}

	/**
	 * 前序递归列出所有文件, 列出文件名匹配正则表达式的文件
	 * 
	 * 如 ("/a/b/hello.txt", "he.*\.txt") 将被返回
	 */
	public static List<File> listFileWithRegexFileName(final File rootDir, final String regexFileNamePattern) {
		return getStream(rootDir).filter(k -> k.isFile() && rootDir.getName().matches(regexFileNamePattern))
				.collect(Collectors.toList());
	}

	/**
	 * 前序递归列出所有文件, 列出符合ant path风格表达式的文件
	 * 
	 * 如 ("/a/b/hello.txt", "he.*\.txt") 将被返回
	 */
	public static List<File> listFileWithAntPath(final File rootDir, final String antPathPattern) {
		return getStream(rootDir).filter(k -> WildcardMatcher
				.matchPath(k.getAbsolutePath(), FilePathUtil.concat(rootDir.getAbsolutePath(), antPathPattern)))
				.collect(Collectors.toList());
	}

	/**
	 * 直接使用Guava的TreeTraverser，获得更大的灵活度, 比如加入各类filter，前序/后序的选择，一边遍历一边操作
	 * 
	 * <pre>
	 * StreamSupport.stream(Files.fileTraverser().depthFirstPreOrder(rootDir).spliterator(), false)
	 * </pre>
	 */
	public static Traverser<File> fileTraverser() {
		return Files.fileTraverser();
	}

}
