package org.ourutils.mybatisextends.core.objs;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.ourutils.mybatisextends.constants.annotations.DefaultValueFill;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.core.objs.DateFill.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       默认值填充，也就是填充数据库的默认值
 *       处理逻辑如下：
 *       1.当注解中的常量值,即{@link org.ourutils.mybatisextends.constants.annotations.DefaultValueFill#constant()}为空时，
 *         对于时间类型的，则取当前时间。对于数字类型取0，对于非此数据，则取对应的默认值
 *       2.不满足1，就会去对应的值进行转换
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/22          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
public class ColumnDefaultValueFill implements MybatisDefaultValue {

    @SneakyThrows
    @Override
    public void fillValue(MybatisDefaultValueContext mybatisDefaultValueContext, Field field) {

        DefaultValueFill defaultValueFill = mybatisDefaultValueContext.getDefaultValueFill();
        Class fieldClass = field.getType();
        String value = defaultValueFill.constant();
        Object targetValue = null;
        boolean blank = StringUtils.isBlank(value);
        switch (fieldClass.getName()) {
            case "java.lang.String":
                targetValue = value;
                break;
            case "java.lang.Integer":
                targetValue = blank ? 0 : Integer.valueOf(value);
                break;
            case "java.lang.Long":
                targetValue = blank ? 0L : Long.valueOf(value);
                break;
            case "java.lang.Short":
                targetValue = blank ? 0 : Short.valueOf(value);
                break;
            case "java.lang.Byte":
                targetValue = blank ? 0 : Byte.valueOf(value);
                break;
            case "java.lang.Dubbo":
                targetValue = blank ? 0d : Double.valueOf(value);
                break;
            case "java.lang.Float":
                targetValue = blank ? 0f : Float.valueOf(value);
                break;
            case "java.lang.Boolean":
                targetValue = blank ? false : Boolean.valueOf(value);
                break;
            case "java.lang.Char":
                targetValue = blank ? '0' : value.charAt(0);
                break;
            case "java.util.Date":
                targetValue = blank ? new Date() : DateUtils.parseDate(value, defaultValueFill.dateFormate());
                break;
            case "java.time.LocalDate":
                targetValue = blank ? LocalDate.now() : LocalDate.parse(value);
                break;
            case "java.time.LocalDateTime":
                targetValue = blank ? LocalDateTime.now() : LocalDateTime.parse(value);
                break;
            case "java.math.BigDecimal":
                targetValue = blank ? BigDecimal.valueOf(0L) : new BigDecimal(value);
                break;
            case "java.math.BigInteger":
                targetValue = blank ? BigInteger.valueOf(0L) : new BigInteger(value);
                break;
            default:
                targetValue = getParaseValue(mybatisDefaultValueContext, field);
                break;
        }
        FieldUtils.writeField(field, mybatisDefaultValueContext.getObj(), targetValue, true);
    }

    /**
     * @author wsil
     * @date 2023/12/24
     * @param  mybatisDefaultValueContext 上下文
     * @param  field 属性信息
     * @return
     * <p>
     * 该方法的作用：
     *   无法枚举为所有都适用的信息，因此获取默认值，留给拓展用
     * </p>
     */
    public Object getParaseValue(MybatisDefaultValueContext mybatisDefaultValueContext, Field field) {

        return null;
    }

}
