package pondcat.commons.combine.sql.entity;

import java.io.Serializable;

/**
 * 记录表数据的最后一次修改者, 需要记录最后一次操作者id的表, 请实现该接口. 后续可能会对实现该接口的实体类做一些自动化操作,
 * 如自动setter当前请求中的用户id到{@link #setUpdateBy(Long)}
 */
public interface TableUpdateBy<T extends Serializable> extends TableCreateBy<T> {

	Long getUpdateBy();

	void setUpdateBy(Long updateBy);

}
