package org.ourutils.mybatisextends.core.interceptors;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.core.objs.AbstractColumnWrap;
import org.ourutils.mybatisextends.utils.MybatisExtentExceptionAssistUtils;

import java.lang.reflect.Method;

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
     * @param context
     * @return
     */
    public String addObj(ProviderContext context);

    /**
     * 批量生成对象的sql
     *
     * @param context
     * @return
     */
    public String addObjBatch(ProviderContext context);


    /**
     * 更新单个对象
     *
     * @param context
     * @return
     */
    public String updateObj(ProviderContext context);


    /**
     * 批量更新单个对象
     *
     * @param context
     * @return
     */
    public String updateObjBatch(ProviderContext context);


    /**
     * 批量通过id进行更新
     *
     * @param context
     * @return
     */
    public String updateObjBatchById(ProviderContext context);


    /**
     * 删除单个对象
     *
     * @param context
     * @return
     */
    public String deleteObj(ProviderContext context);

    /***
     * 查询单个对象
     * @param context
     * @return
     */
    public String selectOne(ProviderContext context);


    /**
     * 批量查询对象
     *
     * @param context
     * @return
     */
    public String selectList(ProviderContext context);

    /**
     * 获取入参
     *
     * @param context
     * @return
     */
    default AbstractColumnWrap getParamClass(ProviderContext context) {
        Class clazz = context.getMapperMethod().getParameterTypes()[0];
        if (AbstractColumnWrap.class.isAssignableFrom(clazz)) {
            return null;
        }
        throw MybatisExtentExceptionAssistUtils.newInstanceWhthThreadLocal(ExceptionEnums.SMARTSQL_PARASE_ERROR, context.getMapperMethod().getName(), "第一个入参不是AbstractColumnWrap的实现类!");
    }

    /**
     * 获取唯一性的SQL标识
     * <p>
     * 我们将PO对象当作映射的类，所以每个类的{@link org.ourutils.mybatisextends.core.mapper.CommonMapper}对应的方法也必然是唯一的
     *
     * @param abstractColumnWrap
     * @param method
     * @return
     */
    default String getSQLID(AbstractColumnWrap abstractColumnWrap, Method method) {
        return abstractColumnWrap.getTableClass().getName() + "#" + method.getName();
    }

    default String getCacheResult(ProviderContext context) {
        return context.getMapperMethod().toString();
    }
}
