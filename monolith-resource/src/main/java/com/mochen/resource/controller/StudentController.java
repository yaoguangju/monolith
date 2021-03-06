package com.mochen.resource.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mochen.core.common.xbo.Result;
import com.mochen.resource.entity.vo.StudentVO;
import com.mochen.resource.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private Executor asyncServiceExecutor;

    @GetMapping("/pushStudentToElasticSearch")
    public Result pushStudentToElasticSearch() throws IOException {

        Integer limit = 10000;
        Integer count = studentMapper.selectCount(null);
        int size = count / limit;
        List<StudentVO> studentVOSList = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= size; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                List<StudentVO> studentVOList = studentMapper.getStudentInfo(finalI * limit, limit)
                        .parallelStream()
                        .map(studentVO -> {
                            StudentVO studentVONew = new StudentVO();
                            BeanUtils.copyProperties(studentVO, studentVONew);
                            studentVONew.setSuggestion(Arrays.asList(studentVO.getName(),studentVO.getSchool()));
                            return studentVONew;
                        }).collect(Collectors.toList());
                studentVOSList.addAll(studentVOList);
            },asyncServiceExecutor).join();
        }
        // 内容放入 es 中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m"); // 可更具实际业务是指
        for (StudentVO studentVO : studentVOSList) {
            bulkRequest.add(
                    new IndexRequest("student").source(JSON.toJSONString(studentVO), XContentType.JSON)
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
        searchSourceBuilder.size(500);

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

    @GetMapping("suggestion")
    public List<String> getSuggestions(@RequestParam("key") String prefix) {

        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest("student");
            // 2.准备DSL
            request.source().suggest(new SuggestBuilder().addSuggestion(
                    "suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(1000)
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

