package bthulu.commons.combine.page;

/**
 * 分页承载, 接收分页参数, 限制最大分页行数.
 * <smal>这里故意不提供是否查询总数, mysql查询总数性能偏低, 尽量在定义接口时约定是否要总数, 不接受前端动态入参决定是否查询总数.</smal>
 * <div>项目中采用第三方分页插件时, 建议继承此类, 同时增加toPage(boolean count)方法, 与具体分页插件接受参数类相互转换.</div>
 */
public class PageRequest {

	private static final int MAX_PAGE_SIZE_DEFAULT = 25;
	private int pageNo = 1;
	private int pageSize = 10;

	private transient int maxPageSize = MAX_PAGE_SIZE_DEFAULT;

	/**
	 * 限制页内最大行数
	 * @param maxPageSize 允许的最大行数
	 */
	public final void limitPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页码, 首页为1
	 * @param pageNo 页码, 首页为1, <1时为首页
	 */
	public void setPageNo(int pageNo) {
		if (pageNo > 1) {
			this.pageNo = pageNo;
		}
	}

	public final int getPageSize() {
		return pageSize;
	}

	public final void setPageSize(int pageSize) {
		if (pageSize < 1) {
			return;
		}
		this.pageSize = pageSize > maxPageSize ? maxPageSize : pageSize;
	}

	@Override
	public String toString() {
		return "PageRequest{" + "pageNo=" + pageNo + ", pageSize=" + pageSize + ", maxPageSize=" + maxPageSize + '}';
	}
}
