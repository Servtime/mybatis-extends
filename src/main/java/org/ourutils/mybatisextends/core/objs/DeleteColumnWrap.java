package org.ourutils.mybatisextends.core.objs;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.utils.MybatisExtentExceptionAssistUtils;

import java.util.List;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.DeleteColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       删除入参包装类
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
@Getter
@Setter
public class DeleteColumnWrap<T> extends WhereColumnWrap<T> {


    public DeleteColumnWrap(T data) {
        this(data, ExecutorContext.newExecutorContext().setSqlCommandType(SqlCommandType.DELETE));
    }

    public DeleteColumnWrap(List<T> data) {
        this(data, ExecutorContext.newExecutorContext().setSqlCommandType(SqlCommandType.DELETE));
    }

    private DeleteColumnWrap(T data, ExecutorContext executorContext) {
        super(data, executorContext);
    }

    private DeleteColumnWrap(List<T> data, ExecutorContext executorContext) {
        super(data, executorContext);
    }

    @Override
    public DeleteColumnWrap<T> build() {

        if (CollectionUtils.isEmpty(this.getDatas())) {
            throw MybatisExtentExceptionAssistUtils.newInstanceWhthThreadLocal(ExceptionEnums.SMARTSQL_PARASE_ERROR, "自动删除", "删除操作没有条件入参,这将会导致删全表数据，因此拒绝执行！");
        }
        super.build();
        return this;
    }
}
