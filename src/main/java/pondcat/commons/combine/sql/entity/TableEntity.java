package pondcat.commons.combine.sql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库表对应实体类的接口, 不提供抽象类, 方便实体类可能需要继承自别的类. 所有实体类必须implements本接口.
 *
 * <p>与{@link TableCreateBy}区别在于无createBy字段, 多用于用户表, 常量表, 配置表, 第三方回调记录表等不需要创建者或者无创建者的场合</p>
 *
 * <p>用户表主键必须为unsigned bigint(可自增或非自增).
 * 其他表主键:必需单主键id, 类型统一为unsigned bigint(可自增或非自增)或者char(慎用, 需指定语言为latin1, 仅允许英文数字组合).
 * 使用bigint而不是int的原因是:方便后续扩展至分布式系统后的主键生成, 性能影响不大, 硬盘便宜.
 * </p>
 *
 * <p>createAt和updateAt限定日期类型为LocalDateTime, 可由数据库设默认值自行维护. 其他日期字段推荐使用{@link LocalDateTime},
 * {@link java.time.LocalDate}, {@link java.time.LocalTime}, 尽量不要用{@link java.util.Date}</p>
 *
 * <div>布尔类型:强制为unsigned tinyint(1), 不要定义为bit(1), bit(1)实际占用的也是1个字节而不是1位, 可参考mysql官网字段类型解释</div>
 */
public interface TableEntity<T extends Serializable> {

	T getId();

	void setId(T id);

	LocalDateTime getCreateAt();

	void setCreateAt(LocalDateTime createAt);

	LocalDateTime getUpdateAt();

	void setUpdateAt(LocalDateTime updateAt);
}
