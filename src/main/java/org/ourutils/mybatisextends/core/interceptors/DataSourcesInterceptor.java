package org.ourutils.mybatisextends.core.interceptors;

import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.constants.enums.ThreadLocalKeyEnums;
import org.ourutils.mybatisextends.core.objs.LoggerWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrapFactory;
import org.ourutils.mybatisextends.utils.DataSourceThreadLocalUtils;
import org.ourutils.mybatisextends.utils.MybatisExtentExceptionAssistUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.interceptors.DataSourcesInterceptor.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       获取数据源的执行类
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/24          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class})
})
public class DataSourcesInterceptor implements Interceptor {

    private static LoggerWrap log = LoggerWrapFactory.getLog(DataSourcesInterceptor.class);
    /**
     * 获取被代理的的对象
     */
    private Executor executor;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String oldSql = null;
        Connection connection = null;
        boolean needClose = false;
        boolean clearSqlVersion = false;
        switch (method.getName()) {
            case "update":
            case "query":
            case "queryCursor":
                Transaction transaction = executor.getTransaction();
                needClose = doCacularNeedClose(transaction);
                connection = transaction.getConnection();
                String sqlVersion = sqlVersion(connection);
                oldSql = DataSourceThreadLocalUtils.setAndGet(ThreadLocalKeyEnums.DATASOURCE_PRODUCT.name(), sqlVersion);
                clearSqlVersion = true;
                break;
        }
        try {
            Object result = method.invoke(invocation.getTarget(), invocation.getArgs());
            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            //清除设置的资源
            if (clearSqlVersion) {
                DataSourceThreadLocalUtils.setAndGet(ThreadLocalKeyEnums.DATASOURCE_PRODUCT.name(), oldSql);
            }
            //关闭资源
            if (needClose) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.warn("关闭获取数据库产品的连接失败,该信息仅做提醒，关闭失败也对程序运行无影响！");
                }
            }
        }
    }

    @SneakyThrows
    private boolean doCacularNeedClose(Transaction transaction) {
        List<Field> fields = FieldUtils.getAllFieldsList(transaction.getClass());
        for (Field one : fields) {
            if (Connection.class.isAssignableFrom(one.getType())) {
                Object result = FieldUtils.readField(one, transaction, true);
                return result == null;
            }
        }
        throw MybatisExtentExceptionAssistUtils.newInstanceWhthThreadLocal(ExceptionEnums.CONFIG_SETTING_ERROR);
    }


    @SneakyThrows
    private String sqlVersion(Connection connection) {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String productName = databaseMetaData.getDatabaseProductName();
        return productName;
    }


    @Override
    public Object plugin(Object target) {
        Object ob = target;
        if (ob instanceof Executor) {
            this.executor = (Executor) ob;
        }
        return Interceptor.super.plugin(target);
    }
}
