package pondcat.commons.combine;

import org.apache.commons.lang3.StringUtils;
import pondcat.commons.combine.constant.ConstDelimiter;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 分页承载, 接收分页参数, 限制最大分页行数, 返回分页结果
 */
public class Paging<T> {
	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long total;
	private String sortBy;
	private String sortOrder;

	private transient int maxPageSize = MAX_PAGE_SIZE_DEFAULT;
	private static final int MAX_PAGE_SIZE_DEFAULT = 100;

	public static final Pattern SORT_BY_REGEX = Pattern.compile("^\\S+$");

	/**
	 * 放宽页最大行数限制到1000
	 */
	public final void liberalizePageSize() {
		maxPageSize = 1000;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public final int getPageSize() {
		return pageSize > maxPageSize ? maxPageSize : pageSize;
	}

	public final void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * 获取排序列
	 * @return 排序列
	 */
	public final String getSortBy() {
		return sortBy;
	}

	/**
	 * 设置排序列, 排序列不能含有空白字符, 如空格、制表符、换页符等
	 * @param sortBy 排序列, 不能含有空白字符, 如空格、制表符、换页符等
	 */
	public final void setSortBy(String sortBy) {
		if (StringUtils.isEmpty(sortOrder)) {
			return;
		}
		if (SORT_BY_REGEX.matcher(sortBy).matches()) {
			this.sortBy = sortBy;
		}
	}

	/**
	 * 获取排序顺序ASC或DESC
	 * @return 排序顺序: ASC, 或DESC
	 */
	public final String getSortOrder() {
		return sortOrder;
	}

	/**
	 * 设置排序顺序ASC或DESC
	 * @param sortOrder 以a或A开头, 排序ASC; 以d或D开头, 排序DESC
	 */
	public final void setSortOrder(String sortOrder) {
		if (StringUtils.isEmpty(sortOrder)) {
			return;
		}
		char c0 = sortOrder.charAt(0);
		if (c0 == 'a' || c0 == 'A') {
			this.sortOrder = "ASC";
		} else if (c0 == 'd' || c0 == 'D') {
			this.sortOrder = "DESC";
		}
	}

	public final String getOrderBy() {
		if (sortBy != null) {
			if (sortOrder == null) {
				return sortBy;
			}
			return sortBy + ConstDelimiter.SPACE_CHAR + sortOrder;
		}
		return null;
	}
}
