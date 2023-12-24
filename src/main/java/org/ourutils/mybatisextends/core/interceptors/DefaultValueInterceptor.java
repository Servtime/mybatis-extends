package org.ourutils.mybatisextends.core.interceptors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.ourutils.mybatisextends.constants.annotations.DefaultValueFill;
import org.ourutils.mybatisextends.core.objs.InsertColumnWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrapFactory;
import org.ourutils.mybatisextends.core.objs.MybatisDefaultValue;
import org.ourutils.mybatisextends.core.objs.MybatisDefaultValueContext;
import org.ourutils.mybatisextends.core.objs.UpdateColumnWrap;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.interceptors.DefaultValueInterceptor.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       默认值自动填充拦截器
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
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class DefaultValueInterceptor implements Interceptor {

    private static LoggerWrap log = LoggerWrapFactory.getLog(SmartExecutorSql.class);

    private LoadingCache<Class<? extends MybatisDefaultValue>, MybatisDefaultValue> defaultValueObjCache = CacheBuilder.newBuilder().maximumSize(20000).
            concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build(new CacheLoader<Class<? extends MybatisDefaultValue>, MybatisDefaultValue>() {
                @Override
                public MybatisDefaultValue load(Class<? extends MybatisDefaultValue> tClass) throws Exception {
                    return tClass.newInstance();
                }

            });
    ;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] arges = invocation.getArgs();
        if (arges[0] instanceof MappedStatement) {
            MappedStatement mappedStatement = (MappedStatement) arges[0];
            switch (mappedStatement.getSqlCommandType()) {
                case INSERT:
                case UPDATE:
                    Object ob = arges[1];
                    if (ob instanceof MapperMethod.ParamMap) {
                        MapperMethod.ParamMap parameterMap = (MapperMethod.ParamMap) ob;
                        ob = parameterMap.get("param1");
                    }
                    Object fillObj = null;
                    Class findObjClass = null;
                    if (ob instanceof InsertColumnWrap) {
                        InsertColumnWrap insertColumnWrap = (InsertColumnWrap) ob;
                        fillObj = insertColumnWrap.getDatas();
                        findObjClass = insertColumnWrap.getTableClass();
                    } else if (ob instanceof UpdateColumnWrap) {
                        UpdateColumnWrap updateColumnWrap = (UpdateColumnWrap) ob;
                        fillObj = updateColumnWrap.getDatas();
                        findObjClass = updateColumnWrap.getTableClass();
                    }
                    if (fillObj instanceof Collection) {
                        doCollection(fillObj, mappedStatement.getSqlCommandType(), findObjClass);
                    }
                    break;
                default:
                    //do nothing
            }

        }


        return invocation.getMethod().invoke(invocation.getTarget(), invocation.getArgs());
    }

    /**
     * 批量处理
     *
     * @param ob
     */
    private void doCollection(Object ob, SqlCommandType sqlCommandType, Class tableClass) {

        Collection collection = (Collection) ob;

        ///获取需要填充值的列
        List<Field> fields = FieldUtils.getAllFieldsList(tableClass).parallelStream().filter(t ->
                t.isAnnotationPresent(DefaultValueFill.class)
        ).filter(t -> {
            DefaultValueFill defaultValueFill = t.getAnnotation(DefaultValueFill.class);
            boolean findMatch = false;
            for (SqlCommandType type : defaultValueFill.happendTime()) {
                if (type.equals(sqlCommandType)) {
                    findMatch = true;
                }
            }
            return findMatch;
        }).collect(Collectors.toList());
        collection.stream().forEach(t -> {
            doFillDefaultValue(t, sqlCommandType, collection, fields);
        });

    }

    private void doFillDefaultValue(Object ob, SqlCommandType sqlCommandType, Collection collection, List<Field> fields) {

        try {
            Table table = ob.getClass().getAnnotation(Table.class);
            if (table == null) {
                return;
            }
            MybatisDefaultValueContext mybatisDefaultValueContext = new MybatisDefaultValueContext();
            mybatisDefaultValueContext.setSqlCommandType(sqlCommandType);
            mybatisDefaultValueContext.setAllObj(collection);
            mybatisDefaultValueContext.setObj(ob);
            fields.parallelStream().peek(t -> {
                DefaultValueFill defaultValueFill = t.getAnnotation(DefaultValueFill.class);
                MybatisDefaultValue mybatisDefaultValue = defaultValueObjCache.getUnchecked(defaultValueFill.callBack());
                mybatisDefaultValueContext.setDefaultValueFill(defaultValueFill);
                mybatisDefaultValue.fillValue(mybatisDefaultValueContext, t);
            }).collect(Collectors.toList());

        } catch (Exception e) {
            //无需处理
            log.error("设置默认属性失败,原因:{}", e);
        }
    }
}
