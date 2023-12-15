package org.ourutils.mybatisextends.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.type.JdbcType;
import org.ourutils.mybatisextends.bo.Field2ColumnBo;
import org.ourutils.mybatisextends.bo.UpdateField2ColumnBo;
import org.ourutils.mybatisextends.constants.enums.ExceptionEnums;
import org.ourutils.mybatisextends.core.objs.ExecutorContext;
import org.ourutils.mybatisextends.core.objs.LoggerWrap;
import org.ourutils.mybatisextends.core.objs.LoggerWrapFactory;
import org.ourutils.mybatisextends.core.objs.SFunction;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.utils.WrapUtils.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       工具类
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
public class WrapUtils {

    private static final LoggerWrap log = LoggerWrapFactory.getLog(WrapUtils.class);

    /**
     * 公共处理类，该类用于获取函数表达式的解析工作
     *
     * @param fns 属性函数
     * @return
     *  将函数转变成对应的field
     */
    public static List<Field> doCollectionField(List<SFunction> fns) {

        if (CollectionUtils.isEmpty(fns)) {
            return new ArrayList<>(0);
        }

        // 从function取出序列化方法
        return fns.parallelStream().map(fn -> {
            Method writeReplaceMethod;
            try {
                writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            // 从序列化方法取出序列化的lambda信息
            boolean isAccessible = writeReplaceMethod.isAccessible();
            writeReplaceMethod.setAccessible(true);
            SerializedLambda serializedLambda;
            try {
                serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            writeReplaceMethod.setAccessible(isAccessible);

            // 从lambda信息取出method、field、class等
            String implMethodName = serializedLambda.getImplMethodName();
            // 确保方法是符合规范的get方法，boolean类型是is开头
            if (!implMethodName.startsWith("is") && !implMethodName.startsWith("get")) {
                throw MybatisExtentExceptionAssistUtils.newInstanceWhthThreadLocal(ExceptionEnums.PARASE_ERROR, "get方法名称: " + implMethodName + ", 不符合java bean规范");
            }

            // get方法开头为 is 或者 get，将方法名 去除is或者get，然后首字母小写，就是属性名
            int prefixLen = implMethodName.startsWith("is") ? 2 : 3;

            String fieldName = implMethodName.substring(prefixLen);
            String firstChar = fieldName.substring(0, 1);
            fieldName = fieldName.replaceFirst(firstChar, firstChar.toLowerCase());
            Field field;
            try {
                field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

            return field;
        }).distinct().collect(Collectors.toList());

    }


    /**
     * 获取列名称
     *
     * @param field
     * @return
     */
    public static String doGetColumnName(Field field) {

        Column column = field.getAnnotation(Column.class);

        if (column != null && StringUtils.isNotBlank(column.name())) {
            return column.name();
        }

        return field.getName();
    }

    /**
     * @param field 属性
     * @return
     */
    public static JdbcType doGetJdbcType(Field field) {

        Column column = field.getAnnotation(Column.class);
        if (column != null && StringUtils.isNotBlank(column.columnDefinition())) {
            return JdbcType.valueOf(column.columnDefinition().toUpperCase(Locale.ROOT));
        }
        Class clazz = field.getType();

        if (clazz.equals(String.class)) {
            return JdbcType.VARCHAR;
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return JdbcType.INTEGER;
        } else if (clazz.equals(BigDecimal.class)) {
            return JdbcType.DECIMAL;
        } else if (clazz.equals(Date.class)) {
            return JdbcType.DATE;
        } else {
            throw new RuntimeException("未知jdbc类型,请手动处理！");
        }


    }


    /**
     * 转换
     *
     * @param field
     * @return
     */
    public static Field2ColumnBo doGetField2ColumnBo(Field field) {

        String column = doGetColumnName(field);

        JdbcType jdbcType = doGetJdbcType(field);

        String name = field.getName();

        return new Field2ColumnBo(column, jdbcType, name, field);

    }


    /**
     * @author wsil
     * @date 2023/12/6
     * @param  field 属性列
     * @return
     * <p>
     * 该方法的作用：
     *   从java的属性上，提取更新列的元信息
     * </p>
     */
    public static UpdateField2ColumnBo doGetUpdateField2ColumnBo(Field field) {

        String column = doGetColumnName(field);

        JdbcType jdbcType = doGetJdbcType(field);
        String name = field.getName();

        return new UpdateField2ColumnBo(column, jdbcType, name, field);

    }


    /**
     * 获取一个类上的表名
     *
     * @param tableClass 存放了表信息的类
     * @return
     *  获取表名
     */
    public static String doGetTableName(Class tableClass) {
        Annotation annotation = null;
        if ((annotation = tableClass.getAnnotation(Table.class)) != null) {
            Table table = (Table) annotation;
            return table.name();
        }
        return tableClass.getName();

    }

    /**
     * 判断属性在{@code data}是否存在非null的值
     *
     * @param field2ColumnBo
     * @param data
     * @param executorContext
     * @return
     */
    public static boolean hasValueIsNotNull(Field2ColumnBo field2ColumnBo, List data, ExecutorContext executorContext) {
        if (CollectionUtils.isEmpty(data)) {
            return false;
        } else {
            return data.parallelStream().filter(t -> {
                try {
                    return null != FieldUtils.readField(field2ColumnBo.getField(), t, true);
                } catch (Exception e) {
                    log.error("获取{}属性值异常:{}", field2ColumnBo.getFieldName(), e);
                }
                return false;
            }).findAny().isPresent();
        }
    }


    /**
     * 判断属性在{@code data}是否存在非null的值
     *
     * @param field2ColumnBo
     * @param data
     * @param executorContext
     * @return
     */
    public static boolean valueIsNotNull(Field2ColumnBo field2ColumnBo, Object data, ExecutorContext executorContext) {
        if (data == null) {
            return false;
        } else {
            try {
                return null != FieldUtils.readField(field2ColumnBo.getField(), data, true);
            } catch (Exception e) {
                log.error("获取{}属性值异常:{}", field2ColumnBo.getFieldName(), e);
            }
            return false;
        }
    }
}
