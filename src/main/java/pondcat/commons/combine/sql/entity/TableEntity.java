package pondcat.commons.combine.sql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author gejian at 2018/7/13 12:58
 */
public abstract class TableEntity<T extends Serializable> implements TableBasic<T> {
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
		if (this == o)
			return true;
		if (!(o instanceof TableEntity))
			return false;
		TableEntity<?> that = (TableEntity<?>) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

}
