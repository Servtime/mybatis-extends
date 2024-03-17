package org.ourutils.mybatisextends.core.objs;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.utils.WrapUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.SelectColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       查询封装
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
@Getter
@Setter
public class SelectColumnWrap<T> extends WhereColumnWrap<T> {

    /**
     * 需要查询的列信息
     */
    private Set<Field> selectFields = new HashSet<>();

    private List<Field2ColumnBo> selectColumn;

    public SelectColumnWrap(T data) {
        super(data);
    }

    public SelectColumnWrap(List<T> data) {
        super(data);
    }

    /**
     * 更新需要忽略的信息
     *
     * @param fns 需要忽略的列，即不需要查询的
     * @return 当前对象
     */
    public SelectColumnWrap<T> ignoreFields(SFunction<T, ?>... fns) {
        this.selectFields.addAll(WrapUtils.doCollectionField(Arrays.stream(fns).parallel()
                .distinct().collect(Collectors.toList())));
        return this;
    }

    @Override
    public SelectColumnWrap<T> build() {
        this.selectColumn = getField2ColumnBoMap().entrySet().stream().parallel().filter(t -> CollectionUtils.isEmpty(selectFields) || !selectFields.contains(t.getKey()))
                .map(t -> t.getValue()).distinct().collect(Collectors.toList());
        super.build();
        return this;
    }

    @Override
    public SelectColumnWrap<T> whereFields(SFunction<T, ?>... fns) {
        super.whereFields(fns);
        return this;
    }

}
