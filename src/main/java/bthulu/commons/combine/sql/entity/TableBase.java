package bthulu.commons.combine.sql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author gejian at 2018/7/13 12:58
 */
public abstract class TableBase<ID extends Serializable> implements TableBasic<ID> {
	private LocalDateTime ctime;

	@Override
	public LocalDateTime getCtime() {
		return ctime;
	}

	@Override
	public void setCtime(LocalDateTime ctime) {
		this.ctime = ctime;
	}

	@Override
	public boolean equals(Object o) {
		return equals0(o);
	}

	@Override
	public int hashCode() {
		return hashCode0();
	}

}
