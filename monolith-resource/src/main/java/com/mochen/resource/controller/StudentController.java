package com.mochen.resource.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mochen.core.common.xbo.Result;
import com.mochen.resource.entity.vo.StudentVO;
import com.mochen.resource.mapper.StudentMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 学生 前端控制器
 * </p>
 *
 * @author 姚广举
 * @since 2022-04-02
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/pushStudentToElasticSearch")
    public Result pushStudentToElasticSearch() throws IOException {

//        Integer page = 0;
        Integer limit = 10000;
        Integer count = studentMapper.selectCount(null);
        //计算
        int size = count / limit;
        int last = count % limit;
        CountDownLatch countDownLatch = new CountDownLatch(last > 0 ? size + 1: size);
        List<StudentVO> studentVOSList = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                List<StudentVO> studentVOList = studentMapper.getStudentInfo(finalI * limit, limit);
                studentVOSList.addAll(studentVOList);
                countDownLatch.countDown();
            });
            thread.start();
        }
        if (last > 0) {
            Thread thread = new Thread(() -> {
                List<StudentVO> studentVOList = studentMapper.getStudentInfo(limit * size, last);
                studentVOSList.addAll(studentVOList);
                countDownLatch.countDown();
            });
            thread.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 获取内容
        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < studentVOSList.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("student")
                            .source(JSON.toJSONString(studentVOSList.get(i)), XContentType.JSON)
            );
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        return Result.success();
    }

    @GetMapping("/getStudentByMySQL")
    public Result getStudentByMySQL(){

        List<StudentVO> studentVOList = studentMapper.getStudentByMySQL();
        return Result.success(studentVOList);
    }

    @GetMapping("/getStudentByEs")
    public Result getStudentByEs() throws IOException {
        SearchRequest student = new SearchRequest("student");
        // 创建搜索源建造者对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 条件采用：精确查询 通过keyword查字段name
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "姚");
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));// 60s
        // 分页
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(5000);

        // 搜索源放入搜索请求中
        student.source(searchSourceBuilder);
        // 执行查询，返回结果
        SearchResponse searchResponse = restHighLevelClient.search(student, RequestOptions.DEFAULT);
        // 解析结果
        SearchHits hits = searchResponse.getHits();

        List<StudentVO> studentVOList = new ArrayList<>();
        // 解析结果
        for (SearchHit documentFields : hits.getHits()) {
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            JSONObject jsonObject = new JSONObject(sourceAsMap);
            String jsonString = jsonObject.toJSONString();
            StudentVO studentVO = JSON.parseObject(jsonString, StudentVO.class);
            studentVOList.add(studentVO);
        }
        return Result.success(studentVOList);
    }

}

