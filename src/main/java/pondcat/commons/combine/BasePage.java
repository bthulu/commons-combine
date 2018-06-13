package pondcat.commons.combine;

import java.util.List;

/**
 * 分页承载, 接收分页参数, 限制最大分页行数, 返回分页结果.
 * 建议项目中继承此类, 同时增加以下两个实例方法:
 * 1. toPage(boolean count)方法, 与具体分页插件接受参数类相互转换
 * 2. fromPage(Xxx)方法, 与具体分页插件返回的分页数据类相互转换
 */
public class BasePage<T> {

	private List<T> content;

	private int pageNumber;

	private int pageSize;

	private long total;

	private transient int maxPageSize = MAX_PAGE_SIZE_DEFAULT;

	private static final int MAX_PAGE_SIZE_DEFAULT = 100;

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

	@Override
	public String toString() {
		return "AbstractPage{" +
				"content=" + content +
				", pageNumber=" + pageNumber +
				", pageSize=" + pageSize +
				", total=" + total +
				", maxPageSize=" + maxPageSize +
				'}';
	}
}
