package bthulu.commons.combine.page;

import java.util.Collection;
import java.util.Collections;

/**
 * 分页结果, 仅含必要的返回信息
 * @author gejian at 2018/8/25 14:56
 */
public class PageResult<E> {
	private Collection<E> rows = Collections.emptyList();
	private long total;

	public PageResult() {
	}

	public PageResult(Collection<E> rows) {
		this.rows = rows;
	}

	public PageResult(Collection<E> rows, long total) {
		this.rows = rows;
		this.total = total;
	}

	public Collection<E> getRows() {
		return rows;
	}

	public void setRows(Collection<E> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "PageResult{" + "rows=" + rows + ", total=" + total + '}';
	}
}
