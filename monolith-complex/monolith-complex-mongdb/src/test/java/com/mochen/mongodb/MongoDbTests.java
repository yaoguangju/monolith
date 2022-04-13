package com.mochen.mongodb;

import com.mochen.mongodb.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
public class MongoDbTests {

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void testCreateCollection(){
        mongoTemplate.createCollection("user");
    }

    @Test
    public void testDeleteCollection(){
        mongoTemplate.dropCollection("user");
    }

    @Test
    public void testAddDocument(){
        // 如果有相同数据sava会覆盖更新,insert会直接报错
        Student student = new Student(1L, "小明", 18);
//        mongoTemplate.save(student);
//        mongoTemplate.insert(student);
        List<Student> studentList = Arrays.asList(new Student(1L, "小明", 18), new Student(2L, "小红", 18), new Student(3L, "小张", 19), new Student(4L, "小华", 20));
        mongoTemplate.insertAll(studentList);
    }

    @Test
    public void testFindAll(){
        List<Student> studentList = mongoTemplate.findAll(Student.class);
        //        List<Student> studentList = mongoTemplate.findAll(Student.class,"student");
        studentList.forEach(System.out::println);
    }

    @Test
    public void testFindByQuery(){

        // 根据id查询
        mongoTemplate.findById(1L, Student.class);
        // 条件查询
        mongoTemplate.find(new Query(), Student.class);
        // 等值查询
        Query query = Query.query(Criteria.where("name").is("小明"));
        mongoTemplate.find(query, Student.class);
        // >< >= <=查询
        /**
         *  lt 小于
         *  lte 小于等于
         *  gt 大于
         *  gte 大于等于
         */
        Query query1 = Query.query(Criteria.where("age").lt(20));
        mongoTemplate.find(query1, Student.class);
        // and 查询
        Query query2 = Query.query(Criteria.where("age").is(20).and("name").is("小华"));
        mongoTemplate.find(query2, Student.class);
        // or 查询
        Criteria criteria = new Criteria();
        criteria.orOperator(
          Criteria.where("name").is("小明"),
          Criteria.where("name").is("小红"),
          Criteria.where("age").is(20)
        );
        mongoTemplate.find(Query.query(criteria), Student.class);
        // and or查询(不对)
        mongoTemplate.find(Query.query(Criteria.where("name").is("小明").orOperator(Criteria.where("age").is(18))), Student.class);
        // sort排序
        Query query3 = new Query();
        query3.with(Sort.by(Sort.Order.desc("age")));
        mongoTemplate.find(query3, Student.class);
        // skip limit 分页
        Query query4 = new Query();
        query4.skip(0).limit(2);
        List<Student> studentList = mongoTemplate.find(query4, Student.class);
        studentList.forEach(System.out::println);
    }

    @Test
    public void testUpdate(){
        Query query = Query.query(Criteria.where("age").is(18));
        Update update = new Update();
        update.set("age",21);
        // 单条更新
//        mongoTemplate.updateFirst(query,update,Student.class);
        // 多条更新
        mongoTemplate.updateMulti(query,update,Student.class);
    }

    @Test
    public void testDelete(){
        // 条件删除
//        mongoTemplate.remove(
//              Query.query(Criteria.where("age").is(19)),
//              Student.class
//        );
        // 全部删除
        mongoTemplate.remove(new Query(), Student.class);

    }
}
