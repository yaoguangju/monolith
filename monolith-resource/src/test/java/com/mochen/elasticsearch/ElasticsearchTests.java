package com.mochen.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
public class ElasticsearchTests {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testUpdateDocument() throws IOException {
//        UpdateRequest request = new UpdateRequest("liuyou_index", "1");
//        User user = new User("lmk",11);
//        request.doc(JSON.toJSONString(user), XContentType.JSON);
//        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
//        System.out.println(response.status()); // OK
//        restHighLevelClient.close();
    }
}
