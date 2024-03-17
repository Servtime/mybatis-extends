package org.ourutils.mybatisextends.core.mapper;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.ourutils.mybatisextends.core.interceptors.SmartExecutorSql;
import org.ourutils.mybatisextends.core.objs.DeleteColumnWrap;
import org.ourutils.mybatisextends.core.objs.InsertColumnWrap;
import org.ourutils.mybatisextends.core.objs.SelectColumnWrap;
import org.ourutils.mybatisextends.core.objs.UpdateColumnWrap;

import java.util.List;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.mapper.CommonMapper.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       公共服务类
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/13          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
public interface CommonMapper {

    /**
     * 新增数据
     *
     * @param updateColumnWrap 执行插入时，相关插入信息的包装类
     * @param <T> 泛化参数，PO对象
     * @return 返回新增成功的数量
     */
    @InsertProvider(value = SmartExecutorSql.class)
    public <T> Integer addObj(@Param(value = "columnWrap") InsertColumnWrap<T> updateColumnWrap);


    /**
     * 批量添加
     *
     * @param updateColumnWrap  执行修改操作时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 插入数量
     */
    @InsertProvider(value = SmartExecutorSql.class)
    public <T> Integer addObjBatch(@Param(value = "columnWrap") InsertColumnWrap<T> updateColumnWrap);


    /**
     * 更新数据
     *
     * @param updateColumnWrap  执行更新时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 返回新增成功的数量
     */
    @UpdateProvider(value = SmartExecutorSql.class)
    public <T> Integer updateObj(@Param(value = "columnWrap") UpdateColumnWrap<T> updateColumnWrap);

    /**
     * 批量更新
     *
     * @param updateColumnWrap  执行批量更新时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 修改的数量
     */
    @UpdateProvider(value = SmartExecutorSql.class)
    public <T> Integer updateObjBatch(@Param(value = "columnWrap") UpdateColumnWrap<T> updateColumnWrap);


    /**
     * 根据ID批量更新
     *
     * @param updateColumnWrap  执行根据ID批量更新时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 修改的数量
     */
    @UpdateProvider(value = SmartExecutorSql.class)
    public <T> Integer updateObjBatchById(@Param(value = "columnWrap") UpdateColumnWrap<T> updateColumnWrap);


    /**
     * 更新数据
     *
     * @param updateColumnWrap  执行删除时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 返回新增成功的数量
     */
    @DeleteProvider(value = SmartExecutorSql.class)
    public <T> Integer deleteObj(@Param(value = "columnWrap") DeleteColumnWrap<T> updateColumnWrap);


    /**
     * 单个查询
     *
     * @param selectColumnWrap  执行查询时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 查询结果集
     */
    @SelectProvider(value = SmartExecutorSql.class)
    public <T> T selectOne(@Param(value = "columnWrap") SelectColumnWrap<T> selectColumnWrap);


    /**
     * 查询所有的
     *
     * @param selectColumnWrap  执行查询时，相关插入信息的包装类
     * @param <T> PO对象
     * @return 查询结果集
     */
    @SelectProvider(value = SmartExecutorSql.class)
    public <T> List<T> selectList(@Param(value = "columnWrap") SelectColumnWrap<T> selectColumnWrap);
}
