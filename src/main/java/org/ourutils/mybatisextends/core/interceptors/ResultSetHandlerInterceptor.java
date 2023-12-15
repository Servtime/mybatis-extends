package org.ourutils.mybatisextends.core.interceptors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.core.objs.SelectColumnWrap;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.interceptors.ResultSetHandlerInterceptor.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       返回处理类
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
@Intercepts(
        value = {
                @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
        }
)
public class ResultSetHandlerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object input = invocation.getTarget();
        ///
        DefaultResultSetHandler defaultResultSetHandler = null;
        if (!(input instanceof DefaultResultSetHandlerWrap)) {
            defaultResultSetHandler = doCreateResultSetHandlerWrap((DefaultResultSetHandler) input);
        } else {
            defaultResultSetHandler = (DefaultResultSetHandler) input;
        }
        return invocation.getMethod().invoke(defaultResultSetHandler, invocation.getArgs());
    }

    private DefaultResultSetHandlerWrap doCreateResultSetHandlerWrap(DefaultResultSetHandler defaultResultSetHandler) {

        return new DefaultResultSetHandlerWrap(readField(defaultResultSetHandler, "executor", Executor.class), readField(defaultResultSetHandler, "mappedStatement", MappedStatement.class),
                readField(defaultResultSetHandler, "parameterHandler", ParameterHandler.class), readField(defaultResultSetHandler, "resultHandler"
                , ResultHandler.class),
                readField(defaultResultSetHandler, "boundSql", BoundSql.class), readField(defaultResultSetHandler, "rowBounds", RowBounds.class));

    }

    private static <T> T readField(DefaultResultSetHandler defaultResultSetHandler, String fieldName, Class<T> inputClass) {

        try {
            return (T) FieldUtils.readField(defaultResultSetHandler, fieldName, true);
        } catch (Exception e) {
            //TODO 预料无异常
        }
        return null;
    }

    /**
     * DefaultResultSetHandlerde 的静态代理类，该类的作用是为了完成
     * <p>
     * defaultResultSetHandler的注入
     */
    public static class DefaultResultSetHandlerWrap extends DefaultResultSetHandler {


        /**
         * 集合锁
         */
        private LoadingCache<List<Field2ColumnBo>, ResultMap> resultMapCache = CacheBuilder.newBuilder().maximumSize(20000).concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build(new CacheLoader<List<Field2ColumnBo>, ResultMap>() {
                    @Override
                    public ResultMap load(List<Field2ColumnBo> key) throws Exception {
                        return doGetRealResultMap(key);
                    }
                });
        ;

        /**
         * 绑定的sql，一切尽在查询里
         */
        private BoundSql boundSql;
        /**
         * 全局配置对象
         */
        private Configuration configuration;

        /**
         * id
         */
        private String id;

        public DefaultResultSetHandlerWrap(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
            super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
            this.boundSql = boundSql;
            this.configuration = mappedStatement.getConfiguration();
            this.id = mappedStatement.getId();
        }


        /**
         * 重新注入默认的resultHandler
         *
         * @param rsw
         * @param resultMap
         * @param resultHandler
         * @param rowBounds
         * @param parentMapping
         * @throws SQLException
         */
        @Override
        public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
            if (resultHandler.getClass().equals(DefaultResultHandler.class)) {
                MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) boundSql.getParameterObject();
                if (paramMap.containsKey("columnWrap") && paramMap.get("columnWrap") instanceof SelectColumnWrap) {
                    SelectColumnWrap selectColumnWrap = (SelectColumnWrap) paramMap.get("columnWrap");
                    List<Field2ColumnBo> selectColumn = selectColumnWrap.getSelectColumn();
                    resultMap = resultMapCache.getUnchecked(selectColumn);
                    super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
                } else {
                    super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
                }
            } else {
                super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
            }

        }

        private ResultMap doGetRealResultMap(List<Field2ColumnBo> field2ColumnBos) {
            id = id + "_" + DigestUtils.md5Hex(field2ColumnBos.stream().map(t -> t.getColumn()).collect(Collectors.joining("_")));
            List<ResultMapping> resultMappings = field2ColumnBos.parallelStream().map(t -> {
                Set<ResultMapping> list = new HashSet<>();
                ResultMapping resultMapping = new ResultMapping.Builder(configuration, t.getFieldName(), t.getFieldName(), (Class) t.getField().getType())
                        .build();
                return resultMapping;
            }).collect(Collectors.toList());
            ResultMap resultMap = new ResultMap.Builder(configuration, id, field2ColumnBos.get(0).getField().getDeclaringClass(), resultMappings).build();
            return resultMap;
        }
    }
}
