package org.ourutils.mybatisextends.core.interceptors;

import org.apache.ibatis.builder.annotation.ProviderContext;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.interceptors.CommonMapperMappings.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       核心接口
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
public interface CommonMapperMappings {

    /**
     * 生成增加单个对象的sql
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 插入对象的SQL
     */
    public String addObj(ProviderContext context);

    /**
     * 批量生成对象的sql
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 批量插入的SQL
     */
    public String addObjBatch(ProviderContext context);


    /**
     * 更新单个对象
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 修改对象的SQL
     */
    public String updateObj(ProviderContext context);


    /**
     * 批量更新单个对象
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 批量修改记录的SQL
     */
    public String updateObjBatch(ProviderContext context);


    /**
     * 批量通过id进行更新
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 通过ID修改记录的SQL
     */
    public String updateObjBatchById(ProviderContext context);


    /**
     * 删除单个对象
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 删除记录的SQL
     */
    public String deleteObj(ProviderContext context);

    /***
     * 查询单个对象
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 查询单个结果的SQL
     */
    public String selectOne(ProviderContext context);


    /**
     * 批量查询对象
     *
     * @param context Mybatis提供的生成SQL执行器的上下文信息
     * @return 查询多个结果的SQL
     */
    public String selectList(ProviderContext context);


}
