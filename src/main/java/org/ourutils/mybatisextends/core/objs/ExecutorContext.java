package org.ourutils.mybatisextends.core.objs;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.ExecutorContext.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       mybatis-extends的上下文
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/5          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@Data
@Accessors(chain = true)
public class ExecutorContext {

    /**
     * 执行的SQL类型
     */
    private SqlCommandType sqlCommandType;


    /**
     * 用于便捷创建一个上下文类
     *
     * @return
     */
    public static ExecutorContext newExecutorContext() {
        return new ExecutorContext();
    }

}
