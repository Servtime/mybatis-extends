package org.ourutils.mybatisextends.core.objs;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.utils.WrapUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.WhereColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *   条件入参包装类
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
@Setter
@Getter
public abstract class WhereColumnWrap<T> extends AbstractColumnWrap<T> {

    /**
     * 需要忽略的列
     */
    private Set<Field> whereFields;

    /**
     * 列的变化
     */
    private List<Field2ColumnBo> whereColumns;

    public WhereColumnWrap(T data) {
        super(data);
        this.whereFields = new HashSet<>();
        this.whereColumns = new ArrayList<>();
    }

    public WhereColumnWrap(List<T> data) {
        this(data, new ExecutorContext());
        this.whereFields = new HashSet<>();
        this.whereColumns = new ArrayList<>();
    }

    public WhereColumnWrap(T data, ExecutorContext executorContext) {
        super(data, executorContext);
    }

    public WhereColumnWrap(List<T> data, ExecutorContext executorContext) {
        super(data, executorContext);
    }


    /**
     * 用于搜寻查询条件
     *
     * @param fns 表映射实体的列函数
     * @return 当前对象
     */
    public WhereColumnWrap<T> whereFields(SFunction<T, ?>... fns) {
        List<Field> fields = WrapUtils.doCollectionField(Arrays.stream(fns).parallel()
                .distinct().collect(Collectors.toList()));
        this.whereFields.addAll(fields);
        return this;
    }

    @Override
    public AbstractColumnWrap<T> build() {
        if (CollectionUtils.isNotEmpty(whereFields)) {
            this.whereColumns = getField2ColumnBoMap().entrySet().parallelStream().filter(t -> whereFields.contains(t.getKey()))
                    .map(t -> t.getValue()).distinct().collect(Collectors.toList());
        } else {
            this.whereColumns = getField2ColumnBoMap().entrySet().parallelStream()
                    .map(t -> t.getValue()).distinct().collect(Collectors.toList());
        }
        super.build();
        return this;
    }
}
