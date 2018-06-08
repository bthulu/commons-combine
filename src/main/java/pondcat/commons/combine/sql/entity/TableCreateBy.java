package pondcat.commons.combine.sql.entity;

import java.io.Serializable;

/**
 * 数据库表对应实体类的接口, 不提供抽象类, 方便实体类可能需要继承自别的类
 *
 * <p>
 * 与{@link TableEntity}区别在于有createBy字段, 多用于由用户操作而创建数据的表. 如还需记录最后一次修改者,
 * {@link TableUpdateBy}
 * </p>
 *
 */
public interface TableCreateBy<T extends Serializable> extends TableEntity<T> {

	Long getCreateBy();

	void setCreateBy(Long createBy);

}
