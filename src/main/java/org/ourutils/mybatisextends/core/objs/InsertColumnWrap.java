package org.ourutils.mybatisextends.core.objs;

import org.apache.commons.collections.CollectionUtils;
import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.utils.MybatisExtentExceptionAssistUtils;

import java.util.List;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.InsertColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       插入包装信息类
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
public class InsertColumnWrap<T> extends AbstractColumnWrap<T> {

    public InsertColumnWrap(T data) {
        super(data);
    }

    public InsertColumnWrap(List<T> data) {
        super(data);
    }

    private InsertColumnWrap(T data, ExecutorContext executorContext) {
        super(data, executorContext);
    }

    private InsertColumnWrap(List<T> data, ExecutorContext executorContext) {
        super(data, executorContext);
    }

    @Override
    public InsertColumnWrap<T> build() {
        if (CollectionUtils.isEmpty(this.getDatas())) {
            throw MybatisExtentExceptionAssistUtils.newInstanceWhthThreadLocal(ExceptionEnums.SMARTSQL_PARASE_ERROR, "增加操作",
                    "需要插入的数据为空，因此拒绝执行！");
        }
        super.build();
        return this;
    }
}
