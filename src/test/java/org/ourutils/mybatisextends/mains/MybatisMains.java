package org.ourutils.mybatisextends.mains;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.ourutils.mybatisextends.core.mapper.CommonMapper;
import org.ourutils.mybatisextends.core.objs.DeleteColumnWrap;
import org.ourutils.mybatisextends.core.objs.InsertColumnWrap;
import org.ourutils.mybatisextends.core.objs.SelectColumnWrap;
import org.ourutils.mybatisextends.core.objs.UpdateColumnWrap;
import org.ourutils.mybatisextends.po.LockRecordPo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *******************************************************************************
 *    系统名称   ： mybatis-extends项目
 *    客户      ：  *
 *    文件名    ： org.ourutils.mybatisextends.mains.MybatisMains.java
 *              (C) Copyright 2023 our-utils Corporation
 *               All Rights Reserved.
 * *****************************************************************************
 * <PRE>
 * 作用
 *       指令测试程序的类
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
public class MybatisMains {

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        //读取配置
        String resource = "E:\\eclipsspace\\mybatis-extends\\src\\test\\resources\\configs\\config.xml";
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(new FileInputStream(new File(resource)));
        try (SqlSession session = sqlSessionFactory.openSession()) {

            CommonMapper mapper = session.getMapper(CommonMapper.class);
            //批量添加
            addBatch(mapper);
            //批量删除
//            delBatch(mapper);
//            //批量查询
//            selectBatch(mapper);
//            //批量修改
//            updateBatch(mapper);

            session.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @author wsil
     * @date 2023/12/14
     * @param  mapper
     * @return
     * <p>
     * 该方法的作用：
     *    批量更新
     * </p>
     */
    private static void updateBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 109; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(i);
            lockRecordPo.setDes(i + "updateDate");
            lockRecordPoList.add(lockRecordPo);
        }
        Integer size = mapper.updateObjBatch(new UpdateColumnWrap<LockRecordPo>(lockRecordPoList)
                .needUpdateFields(LockRecordPo::getDes).whereFields(LockRecordPo::getId).build());
        System.out.println(size);
    }

    /**
     * @author wsil
     * @date 2023/12/14
     * @param  mapper
     * @return
     * <p>
     * 该方法的作用：
     *    批量查询
     * </p>
     */
    private static void selectBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 199; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(101);
            lockRecordPoList.add(lockRecordPo);
        }

        LockRecordPo one = mapper.selectOne(new SelectColumnWrap<>(lockRecordPoList)
                .ignoreFields(LockRecordPo::getLockTime).whereFields(LockRecordPo::getId).build());
        System.out.println(one);
    }

    /**
     * @author wsil
     * @date 2023/12/14
     * @param  mapper
     * @return
     * <p>
     * 该方法的作用：
     *  批量删除
     * </p>
     */
    private static void delBatch(CommonMapper mapper) {
        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 199; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(i);
            lockRecordPoList.add(lockRecordPo);
        }
        System.out.println(mapper.deleteObj(new DeleteColumnWrap<>(lockRecordPoList).build()));
    }

    /**
     * @author wsil
     * @date 2023/12/14
     * @param  mapper
     * @return
     * <p>
     * 该方法的作用：
     *   批量新增
     * </p>
     */
    private static void addBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 199; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(i);
            lockRecordPo.setDes("123456");
            lockRecordPoList.add(lockRecordPo);
        }
        System.out.println(mapper.addObjBatch(new InsertColumnWrap<>(lockRecordPoList).build()));
    }
}
