package com.bz.gists.dao.common;

/**
 * Created on 2019/1/17
 *
 * @author zhongyongbin
 */
public interface BaseMapper<T, PK> {

    /**
     * 插入记录，包括 null 值
     *
     * @param record 记录实体
     * @return 影响行数
     */
    int insert(T record);

    /**
     * 插入记录，不包括 null 值
     *
     * @param record 记录实体
     * @return 影响行数
     */
    int insertSelective(T record);

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 主键对应的实体
     */
    T selectByPrimaryKey(PK id);

    /**
     * 根据主键更新，包括 null 值
     *
     * @param record 记录实体
     * @return 影响行数
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新记录，不包括 null 值
     *
     * @param record 记录实体
     * @return 影响行数
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteByPrimaryKey(PK id);
}
