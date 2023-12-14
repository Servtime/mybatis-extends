# mybatis-extends简介
mybatis-extends是基于mybatis组件进行的功能性拓展工具，该工具的作用是通过mybatis对外暴露的一些拓展点，进行拓展。通过提供公共mapper类CommonMapper让你可以很轻松的实现单表的增删改查，提升我们的生产力。
# mybatis-extends的规划
mybatis-extends的1.0.0版本仅仅只提供了基础的，针对单表的增删改查操作，但我们可不仅仅想要局限于此，我们下个版本必然要实现多表关联，而且我们将走多表在库里关联->多表在mybatis-extends关联这种渐进式实现，但这是个漫长的路。
# mybatis-extends的使用
mybatis-extends不会对你的项目造成任何侵入式的伤害，他非常独立，使用也非常简单，下面一个列子就展示了它的增删改查功能：

```
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
            delBatch(mapper);
            //批量查询
            selectBatch(mapper);
            //批量修改
            updateBatch(mapper);

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

            lockRecordPo.setLockTime(new Date());
            lockRecordPo.setDes("123456");
            lockRecordPoList.add(lockRecordPo);
        }
        System.out.println(mapper.addObjBatch(new InsertColumnWrap<>(lockRecordPoList).build()));
    }
}
```
其中lockRecordPo为PO对象，它对应的数据库里的建表语句是这样的
```
create table lock_record
(
    id        int auto_increment,
    lock_time datetime    not null,
    des       varchar(36) not null,
    constraint lock_record_id_uindex
        unique (id)
);
```
而它的PO类为
```
@Table(name = "lock_record")
@Data
public class LockRecordPo implements Serializable {

    @Id
    private Integer id;



    @Column(name = "lock_time")
    private Date lockTime;

    
    private String des;

}
```
以上展示的是非继承Spring的版本，而继承Spring的版本，你仅仅把它当成一个你自己创建的Mapper对象，交给Mybatis管理即可。

