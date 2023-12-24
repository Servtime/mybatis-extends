package org.ourutils.mybatisextends.core.interceptors;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.ourutils.mybatisextends.constants.enums.ThreadLocalKeyEnums;
import org.ourutils.mybatisextends.core.objs.DeleteColumnWrap;
import org.ourutils.mybatisextends.core.objs.InsertColumnWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrapFactory;
import org.ourutils.mybatisextends.core.objs.SelectColumnWrap;
import org.ourutils.mybatisextends.core.objs.UpdateColumnWrap;
import org.ourutils.mybatisextends.utils.DataSourceThreadLocalUtils;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.interceptors.SmartExecutorSql.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       核心类，用于核心sql的生成
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
public class SmartExecutorSql implements ProviderMethodResolver, CommonMapperMappings {

    private static LoggerWrap log = LoggerWrapFactory.getLog(SmartExecutorSql.class);

    private static VelocityEngine velocityEngine = new VelocityEngine();

    static {
        try {
            velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
            velocityEngine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, SmartExecutorSql.class.getClassLoader().getResource("templates").getPath());
            // 解决中文乱码问题
            velocityEngine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            velocityEngine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
            velocityEngine.init();
        } catch (Throwable throwable) {
            log.error("获取模板引擎异常！");
        }

    }

    private static LoadingCache<ProviderContext, String> loadingCache = CacheBuilder.newBuilder().maximumSize(20000).concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .build(new CacheLoader<ProviderContext, String>() {
                @Override
                public String load(ProviderContext key) throws Exception {
                    Class<?>[] classes = key.getMapperMethod().getParameterTypes();
                    return SmartExecutorSql.paraseColumnWrap(classes, key.getMapperMethod());
                }
            });

    /**
     * @param classes
     */
    private static String paraseColumnWrap(Class<?>[] classes, Method method) {
        Class clazz = classes[0];
        String databaseId = DataSourceThreadLocalUtils.getValue(ThreadLocalKeyEnums.DATASOURCE_PRODUCT.name());
        if (DeleteColumnWrap.class.equals(clazz)) {
            return doParaseDel(clazz, method, databaseId);
        } else if (SelectColumnWrap.class.equals(clazz)) {
            return doParaseSelect(clazz, method, databaseId);
        } else if (InsertColumnWrap.class.equals(clazz)) {
            return doParaseInsert(clazz, method, databaseId);
        } else if (UpdateColumnWrap.class.equals(clazz)) {
            return doParaseUpdate(clazz, method, databaseId);
        } else {
            log.error("不支持的入参类型");
        }
        return "";
    }

    /**
     * 改
     *
     * @param clazz
     */
    private static String doParaseUpdate(Class clazz, Method method, String databaseId) {
        Template template = velocityEngine.getTemplate("updateMapper.vm");
        StringWriter sw = new StringWriter();
        VelocityContext contentContext = new VelocityContext();
        contentContext.put("methodName", method.getName());
        String sql = StringUtils.containsIgnoreCase(databaseId, "mysql") ? "mysql" :
                (StringUtils.containsIgnoreCase(databaseId, "oracle") ? "oracle" : "");
        contentContext.put("sqlversion", sql);
        template.merge(contentContext, sw);
        return sw.toString();
    }

    /**
     * 增
     *
     * @param clazz
     */
    private static String doParaseInsert(Class clazz, Method method, String databaseId) {

        String result = "";
        String methodName = method.getName();
        switch (methodName) {
            case "addObj":
                //单个插入
                result = doParaseSigalInsert(clazz, method, databaseId);
                break;
            case "addObjBatch":
                //批量插入
                result = doParaseBatchInsert(clazz, method,databaseId);
                break;
        }

        return result;
    }

    /**
     * 批量增
     *
     * @param clazz
     * @param method
     */
    private static String doParaseBatchInsert(Class clazz, Method method, String databaseId) {

        Template template = velocityEngine.getTemplate("insertMapper.vm");
        StringWriter sw = new StringWriter();
        VelocityContext contentContext = new VelocityContext();
        contentContext.put("methodName", method.getName());
        String sql = StringUtils.containsIgnoreCase(databaseId, "mysql") ? "mysql" :
                (StringUtils.containsIgnoreCase(databaseId, "oracle") ? "oracle" : "");
        contentContext.put("sqlversion", sql);
        template.merge(contentContext, sw);
        return sw.toString();
    }

    /**
     * 单增
     *
     * @param clazz
     * @param method
     */
    private static String doParaseSigalInsert(Class clazz, Method method, String databaseId) {

        Template template = velocityEngine.getTemplate("insertMapper.vm");
        StringWriter sw = new StringWriter();
        VelocityContext contentContext = new VelocityContext();
        contentContext.put("methodName", method.getName());
        String sql = StringUtils.containsIgnoreCase(databaseId, "mysql") ? "mysql" :
                (StringUtils.containsIgnoreCase(databaseId, "oracle") ? "oracle" : "");
        contentContext.put("sqlversion", sql);
        template.merge(contentContext, sw);
        return sw.toString();

    }


    /**
     * 查
     *
     * @param clazz
     */
    private static String doParaseSelect(Class clazz, Method method, String databaseId) {

        Template template = velocityEngine.getTemplate("selectMapper.vm");
        StringWriter sw = new StringWriter();
        VelocityContext contentContext = new VelocityContext();
        contentContext.put("methodName", method.getName());
        String sql = StringUtils.containsIgnoreCase(databaseId, "mysql") ? "mysql" :
                (StringUtils.containsIgnoreCase(databaseId, "oracle") ? "oracle" : "");
        contentContext.put("sqlversion", sql);
        template.merge(contentContext, sw);
        return sw.toString();

    }

    /**
     * 删
     *
     * @param clazz
     */
    private static String doParaseDel(Class clazz, Method method, String databaseId) {
        Template template = velocityEngine.getTemplate("deleteMapper.vm");
        StringWriter sw = new StringWriter();
        VelocityContext contentContext = new VelocityContext();
        contentContext.put("methodName", method.getName());
        String sql = StringUtils.containsIgnoreCase(databaseId, "mysql") ? "mysql" :
                (StringUtils.containsIgnoreCase(databaseId, "oracle") ? "oracle" : "");
        contentContext.put("sqlversion", sql);
        template.merge(contentContext, sw);
        return sw.toString();

    }

    @Override
    public Method resolveMethod(ProviderContext context) {
        //自己写的方法，必然会找到
        List<Method> sameNameMethods = Arrays.stream(getClass().getMethods())
                .filter(m -> m.getName().equals(context.getMapperMethod().getName()))
                .collect(Collectors.toList());
        return sameNameMethods.get(0);
    }


    @Override
    public String addObj(ProviderContext context) {
        String result = getCacheResult(context);
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String addObjBatch(ProviderContext context) {
        String result = getCacheResult(context);
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String updateObj(ProviderContext context) {
        String result = getCacheResult(context);
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String updateObjBatch(ProviderContext context) {
        String result = getCacheResult(context);
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String updateObjBatchById(ProviderContext context) {
        String result = getCacheResult(context);
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String deleteObj(ProviderContext context) {
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String selectOne(ProviderContext context) {
        return loadingCache.getUnchecked(context);
    }

    @Override
    public String selectList(ProviderContext context) {
        return loadingCache.getUnchecked(context);
    }
}
