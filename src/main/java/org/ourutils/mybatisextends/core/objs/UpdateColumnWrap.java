package org.ourutils.mybatisextends.core.objs;

import org.apache.commons.collections.CollectionUtils;
import org.ourutils.mybatisextends.bo.UpdateField2ColumnBo;
import org.ourutils.mybatisextends.utils.WrapUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.UpdateColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       更新信息包装类
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
public class UpdateColumnWrap<T>  extends WhereColumnWrap<T> {

    /**
     * 需要更新的列
     */
    private Set<Field> needUpdateFields;


    /**
     * 需要忽略的列
     */
    private Set<Field> needIgnoreFields;

    /**
     * 列信息
     */
    private Map<Field, UpdateField2ColumnBo> needUpdateColumn;


    /**
     * 单个更新
     *
     * @param data
     */
    public UpdateColumnWrap(T data) {
        super(data);
        this.needUpdateFields = new HashSet<>();
        this.needIgnoreFields = new HashSet<>();
        init();
    }


    private void init() {
        this.needUpdateColumn = getField2ColumnBoMap().entrySet().parallelStream().map(t -> {
            UpdateField2ColumnBo updateField2ColumnBo = new UpdateField2ColumnBo(t.getValue().getColumn(), t.getValue().getJdbcType(), t.getValue().getFieldName()
                    , t.getValue().getField());
            t.setValue(updateField2ColumnBo);
            return t;
        }).collect(Collectors.toMap(kv -> kv.getKey(), kv -> (UpdateField2ColumnBo) kv.getValue()));
        ;
    }

    /**
     * 批量更新
     *
     * @param data
     */
    public UpdateColumnWrap(List<T> data) {
        super(data);
        this.needUpdateFields = new HashSet<>();
        this.needIgnoreFields = new HashSet<>();
        init();
    }


    /**
     * 单个更新
     *
     * @param data
     */
    public UpdateColumnWrap(T data, ExecutorContext executorContext) {
        super(data, executorContext);
        this.needUpdateFields = new HashSet<>();
        this.needIgnoreFields = new HashSet<>();

    }

    /**
     * 批量更新
     *
     * @param data
     */
    public UpdateColumnWrap(List<T> data, ExecutorContext executorContext) {
        super(data, executorContext);
        this.needUpdateFields = new HashSet<>();
        this.needIgnoreFields = new HashSet<>();

    }

    /**
     * 将bean的属性的get方法，作为lambda表达式传入时，获取get方法对应的属性Field
     *
     * @param fns lambda表达式，bean的属性的get方法
     * @return 属性对象
     */
    public UpdateColumnWrap<T> needUpdateFields(SFunction<T, ?>... fns) {
        this.needUpdateFields.addAll(WrapUtils.doCollectionField(Arrays.stream(fns).parallel()
                .distinct().collect(Collectors.toList())));
        return this;
    }


    /**
     * 更新需要忽略的信息
     *
     * @param fns
     * @return
     */
    public UpdateColumnWrap<T> ignoreFields(SFunction<T, ?>... fns) {
        this.needIgnoreFields.addAll(WrapUtils.doCollectionField(Arrays.stream(fns).parallel()
                .distinct().collect(Collectors.toList())));

        return this;
    }

    @Override
    public UpdateColumnWrap<T> build() {
        if (CollectionUtils.isNotEmpty(needUpdateFields)) {
            this.needUpdateColumn.entrySet().parallelStream().filter(t -> !needUpdateFields.contains(t.getKey()))
                    .peek(t -> t.getValue().setIgnore(true)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(needIgnoreFields)) {
            this.needUpdateColumn.entrySet().parallelStream().filter(t -> needIgnoreFields.contains(t.getKey()))
                    .peek(t -> t.getValue().setIgnore(true)).collect(Collectors.toList());
        }
        super.build();
        return this;
    }

    @Override
    public UpdateColumnWrap<T> whereFields(SFunction<T, ?>... fns) {
        super.whereFields(fns);
        return this;
    }

}
