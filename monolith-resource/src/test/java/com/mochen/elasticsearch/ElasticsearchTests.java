package com.mochen.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ElasticsearchTests {

    private static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        // 默认缓存限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory
                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Test
    public void testUpdateDocument() throws IOException {
        // 阿里云Elasticsearch集群需要basic auth验证。
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        //访问用户名和密码为您创建阿里云Elasticsearch实例时设置的用户名和密码，也是Kibana控制台的登录用户名和密码。
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "yAFcY2x2VYcdQGDN"));

        // 通过builder创建rest client，配置http client的HttpClientConfigCallback。
        // 单击所创建的Elasticsearch实例ID，在基本信息页面获取公网地址，即为ES集群地址。
        RestClientBuilder builder = RestClient.builder(new HttpHost("es-cn-7pp2okofe000ogcqt.public.elasticsearch.aliyuncs.com", 9200, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        // RestHighLevelClient实例通过REST low-level client builder进行构造。
        RestHighLevelClient highClient = new RestHighLevelClient(builder);

        try {
            // 创建request。
            Map<String, Object> jsonMap = new HashMap<>();
            // field_01、field_02为字段名，value_01、value_02为对应的值。
            jsonMap.put("{field_01}", "{value_01}");
            jsonMap.put("{field_02}", "{value_02}");
            //index_name为索引名称；type_name为类型名称,7.0及以上版本必须为_doc；doc_id为文档的id。
            IndexRequest indexRequest = new IndexRequest("{index_name}", "_doc", "{doc_id}").source(jsonMap);

            // 同步执行，并使用自定义RequestOptions（COMMON_OPTIONS）。
            IndexResponse indexResponse = highClient.index(indexRequest, COMMON_OPTIONS);

            long version = indexResponse.getVersion();

            System.out.println("Index document successfully! " + version);
            //index_name为索引名称；type_name为类型名称,7.0及以上版本必须为_doc；doc_id为文档的id。与以上创建索引的名称和id相同。
            DeleteRequest request = new DeleteRequest("{index_name}", "_doc", "{doc_id}");
            DeleteResponse deleteResponse = highClient.delete(request, COMMON_OPTIONS);

            System.out.println("Delete document successfully! \n" + deleteResponse.toString());

            highClient.close();

        } catch (IOException ioException) {
            // 异常处理。
        }
    }


}
