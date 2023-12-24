package org.ourutils.mybatisextends.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.constants.enums.ExceptionEnums.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       异常信息记录的枚举类
 * 限制
 *       无。
 * 注意事项
 *       无。
 * 修改历史
 * -----------------------------------------------------------------------------
 *         VERSION       DATE            @author       CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *         1.0.0        2023/12/6          wsil      create
 * -----------------------------------------------------------------------------
 * </PRE>
 **/
@Getter
@AllArgsConstructor
public enum ExceptionEnums {

    SMARTSQL_PARASE_ERROR("%s自动生成SQL异常,原因:%s", 2),
    PARASE_ERROR("解析错误,原因:%s", 1),
    CONFIG_SETTING_ERROR("配置异常!", 0);

    /**
     * 异常信息
     */
    private String msg;


    /**
     * 占位符数量
     */
    private int palaceHolder;


    /**
     * 拼接异常信息
     *
     * @param msgs
     * @return
     */
    public String convertOrginMsg(String... msgs) {

        //等于0直接返回
        if (palaceHolder == 0) {
            return msg;
        } else if (msgs.length == palaceHolder) {
            //相等直接替换
            return String.format(msg, msgs);
        } else if (palaceHolder > msgs.length) {
            //小于真实的则进行填充
            String[] exMsgs = new String[palaceHolder];
            System.arraycopy(msgs, 0, exMsgs, 0, msgs.length);
            for (int i = msgs.length; i < palaceHolder; i++) {
                exMsgs[i] = "";
            }
            return String.format(msg, exMsgs);
        } else {
            //大于的则进行截断
            String[] exMsgs = new String[palaceHolder];
            System.arraycopy(msgs, 0, exMsgs, 0, palaceHolder);
            return String.format(msg, exMsgs);
        }
    }
}
