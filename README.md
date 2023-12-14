# mybatis-extends简介
mybatis-extends是基于mybatis组件进行的功能性拓展工具，该工具的作用是通过mybatis对外暴露的一些拓展点，进行拓展。通过提供公共mapper类CommonMapper让你可以很轻松的实现单表的增删改查，提升我们的生产力。
# mybatis-extends的规划
mybatis-extends的1.0.0版本仅仅只提供了基础的，针对单表的增删改查操作，但我们可不仅仅想要局限于此，我们下个版本必然要实现多表关联，而且我们将走多表在库里关联->多表在mybatis-extends关联这种渐进式实现，但这是个漫长的路。
# mybatis-extends的使用
mybatis-extends不会对你的项目造成任何侵入式的伤害，他非常独立，使用也非常简单，下面一个列子就展示了他的增删改查功能：

```
public class MybatisMains {

    /**
     * {@link MapperMethod}
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        String resource = "E:\\ideaspace\\demo\\src\\main\\java\\com\\example\\demo\\mybatisp\\config.xml";
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(new FileInputStream(new File(resource)));
        try (SqlSession session = sqlSessionFactory.openSession()) {

            CommonMapper mapper = session.getMapper(CommonMapper.class);

            addBatch(mapper);

            delBatch(mapper);

            selectBatch(mapper);

            updateBatch(mapper);

            session.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量更新功能
     *
     * @param mapper
     */
    private static void updateBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 109; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(i);
            lockRecordPo.setLockId(i + "updateDate");
            lockRecordPoList.add(lockRecordPo);
        }
        Integer size = mapper.updateObjBatch(new UpdateColumnWrap<LockRecordPo>(lockRecordPoList)
                .needUpdateFields(LockRecordPo::getLockId).whereFields(LockRecordPo::getId).build());
        System.out.println(size);
    }

    /**
     * 批量查询
     *
     * @param mapper
     */
    private static void selectBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 199; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(101);
            lockRecordPoList.add(lockRecordPo);
        }
//        List<LockRecordPo> list = mapper.selectList(new SelectColumnWrap<>(lockRecordPoList)
//                .ignoreFields(LockRecordPo::getLockTime).whereFields(LockRecordPo::getId).build());

        LockRecordPo one = mapper.selectOne(new SelectColumnWrap<>(lockRecordPoList)
                .ignoreFields(LockRecordPo::getLockTime).whereFields(LockRecordPo::getId).build());
        System.out.println(one);
    }

    /**
     * 批量删除
     *
     * @param mapper
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
     * 批量添加
     *
     * @param mapper
     */
    private static void addBatch(CommonMapper mapper) {

        List<LockRecordPo> lockRecordPoList = new ArrayList<>();
        for (int i = 100; i < 199; i++) {
            LockRecordPo lockRecordPo = new LockRecordPo();
            lockRecordPo.setId(i);
            lockRecordPo.setLockId("iii" + i);
            lockRecordPo.setLockType("1");
            lockRecordPo.setOperatorNo("826");
            lockRecordPo.setLockTime(new Date());
            lockRecordPo.setUnLockTime(new Date());
            lockRecordPoList.add(lockRecordPo);
        }
        System.out.println(mapper.addObjBatch(new InsertColumnWrap<>(lockRecordPoList).build()));
    }
}
```


