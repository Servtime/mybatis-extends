package org.ourutils.mybatisextends.core.objs;

import java.lang.reflect.Field;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.MybatisDefaultValue.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       默认值接口
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/21          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
public interface MybatisDefaultValue {

    /**
     * @author wsil
     * @date 2023/12/21
     * @param  mybatisDefaultValueContext 填充默认指的对象
     * @param field 列
     * @return
     * <p>
     * 该方法的作用：
     *    默认值填充回调
     * </p>
     */
    public void fillValue(MybatisDefaultValueContext mybatisDefaultValueContext, Field field);
}
