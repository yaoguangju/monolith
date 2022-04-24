package com.mochen.resource.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mochen.core.common.xbo.Result;
import com.mochen.resource.entity.vo.StudentVO;
import com.mochen.resource.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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
@Slf4j
public class StudentController {


    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/pushStudentToElasticSearch")
    public Result pushStudentToElasticSearch() throws InterruptedException, IOException {

//        Integer page = 0;
        Integer limit = 10000;
        Integer count = studentMapper.selectCount(null);
        System.out.println(count);
        //计算
        int size = count / limit;
        int last = count % limit;
        System.out.println(size);
        System.out.println(last > 0 ? size + 1: size);


        CountDownLatch countDownLatch = new CountDownLatch(last > 0 ? size + 1: size);

        List<StudentVO> studentVOSList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < size; i++) {
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
        countDownLatch.await();


//        // 获取内容
//        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < studentVOSList.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("student")
                            .source(JSON.toJSONString(studentVOSList.get(i)), XContentType.JSON)
            );
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        return Result.success(studentVOSList.size());
    }


    @GetMapping("/pushStudentToElasticSearchSuggestion")
    public Result pushStudentToElasticSearchSuggestion() throws InterruptedException, IOException {

        Integer limit = 10000;
        Integer count = studentMapper.selectCount(null);
        System.out.println(count);
        //计算
        int size = count / limit;
        int last = count % limit;
        System.out.println(size);
        System.out.println(last > 0 ? size + 1: size);


        CountDownLatch countDownLatch = new CountDownLatch(last > 0 ? size + 1: size);

        List<StudentVO> studentVOSList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < size; i++) {
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
        countDownLatch.await();


//        // 获取内容
//        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (int i = 0; i < studentVOSList.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("student_completion")
                            .type("Completion")
                            .source(JSON.toJSONString(studentVOSList.get(i)), XContentType.JSON)
            );
        }
        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

        return Result.success(studentVOSList.size());
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

    @GetMapping("/getStudentByEsHighlight")
    public Result getStudentByEsHighlight() throws IOException {
        SearchRequest request = new SearchRequest("student");
        // 创建搜索源建造者对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 条件采用：高亮查询 通过keyword查字段name
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("name","姚");
        sourceBuilder.query(termsQueryBuilder);
        //构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");//设置标签前缀
        highlightBuilder.postTags("</font>");//设置标签后缀
        highlightBuilder.field("name");//设置高亮字段
        //设置高亮构建对象
        sourceBuilder.highlighter(highlightBuilder);
        //设置请求体
        request.source(sourceBuilder);

        // 执行查询，返回结果
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // 解析结果
        SearchHits hits = searchResponse.getHits();

        System.out.println("took::"+searchResponse.getTook());
        System.out.println("time_out::"+searchResponse.isTimedOut());
        System.out.println("total::"+hits.getTotalHits());
        System.out.println("max_score::"+hits.getMaxScore());
        System.out.println("hits::::>>");

        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            //打印高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            System.out.println(highlightFields);
        }
        System.out.println("<<::::");
        return Result.success();
    }

    @GetMapping("suggestion")
    public List<String> getSuggestions(@RequestParam("key") String prefix) {

        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("student");
            // 2.准备DSL
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("name")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)
            ));
            // 3.发起请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 4.解析结果
            Suggest suggest = response.getSuggest();
            // 4.1.根据补全查询名称，获取补全结果
            CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
            // 4.2.获取options
            List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
            // 4.3.遍历
            List<String> list = new ArrayList<>(options.size());
            for (CompletionSuggestion.Entry.Option option : options) {
                String text = option.getText().toString();
                list.add(text);
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

