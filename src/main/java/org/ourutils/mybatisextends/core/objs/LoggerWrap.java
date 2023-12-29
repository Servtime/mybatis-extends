package org.ourutils.mybatisextends.core.objs;

import org.apache.ibatis.logging.Log;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.utils.LoggerConvertUtils.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       兼容性处理log工具类
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
public class LoggerWrap {

    /**
     * 包装类
     */
    private Log log;

    public LoggerWrap(Log log) {
        this.log = log;
    }

    /**
     * @author wsil
     * 
     * @return
     * <p>
     * 该方法的作用：
     *
     * </p>
     */
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }


    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }


    public void error(String s, Object... args) {
        if (args.length > 0) {
            Object last = args[args.length - 1];
            if (last instanceof Throwable) {
                Throwable lastThrow = (Throwable) last;
                args[args.length - 1] = "";
                log.error(String.format(s, args), lastThrow);
            }
        }
        log.error(String.format(s, args));

    }


    public void debug(String s, Object... args) {
        log.debug(String.format(s, args));
    }


    public void trace(String s, Object... args) {
        log.trace(String.format(s, args));
    }


    public void warn(String s, Object... args) {
        log.warn(String.format(s, args));
    }
}
