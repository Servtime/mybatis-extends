package org.ourutils.mybatisextends.core.objs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.utils.WrapUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.AbstractColumnWrap.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       包装类的抽象
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
public class AbstractColumnWrap<T> implements ColumnWrap<T> {
    /**
     * 需要忽略的列
     */
    private Set<Field> whereFields;

    /**
     * 更新的数据
     */
    private List<T> datas;

    /**
     * 需要操作的class
     */
    private Class tableClass;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 上下文
     */
    private ExecutorContext executorContext;

    /**
     * 是否进行了build
     */
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private boolean hasBuild = false;

    /**
     * 使用缓存进行快速获取
     */
    private static LoadingCache<Class, Map<Field, Field2ColumnBo>> loadingCache = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors() * 2)
            .maximumSize(2000).build(new CacheLoader<Class, Map<Field, Field2ColumnBo>>() {
                @Override
                public Map<Field, Field2ColumnBo> load(Class key) throws Exception {
                    return FieldUtils.getAllFieldsList(key).parallelStream()
                            .collect(Collectors.toMap(t -> t, t -> WrapUtils.doGetField2ColumnBo(t)));
                }
            });

    public AbstractColumnWrap(T data) {
        this(data, new ExecutorContext());
    }

    public AbstractColumnWrap(List<T> data) {
        this(data, new ExecutorContext());
    }

    public AbstractColumnWrap(T data, ExecutorContext executorContext) {
        this.datas = new ArrayList<>(1);
        if (data != null) {
            this.datas.add(data);
            this.tableClass = data.getClass();
            this.tableName = WrapUtils.doGetTableName(tableClass);
        }
        this.executorContext = executorContext;
    }

    /**
     * 获取PO对象，所有属性和属性描述信息映射的集合
     *
     * @return 属性和属性描述信息映射集合
     */
    public Map<Field, Field2ColumnBo> getField2ColumnBoMap() {

        return loadingCache.getUnchecked(this.tableClass);
    }

    public AbstractColumnWrap(List<T> data, ExecutorContext executorContext) {
        this.datas = new ArrayList<>(data.size());
        this.datas.addAll(data);
        if (CollectionUtils.isNotEmpty(data) && data.size() > 0) {
            this.tableClass = data.get(0).getClass();
            this.tableName = WrapUtils.doGetTableName(tableClass);
        }
        this.executorContext = executorContext;
    }


    /**
     * 完成构建
     *
     * @return 构建完毕的对象
     */
    public AbstractColumnWrap build() {

        this.hasBuild = true;

        return this;

    }

}
