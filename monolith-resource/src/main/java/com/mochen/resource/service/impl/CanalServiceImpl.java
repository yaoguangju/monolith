package com.mochen.resource.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mochen.resource.entity.vo.StudentVO;
import com.mochen.resource.mapper.StudentMapper;
import com.mochen.resource.service.ICanalService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

@Service
public class CanalServiceImpl implements ICanalService {

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public void synchronousData() throws IOException {
        //TODO 获取连接
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111), "example", "", "");
        while (true) {
            //TODO 连接
            canalConnector.connect();
            //TODO 订阅数据库
            canalConnector.subscribe("resource.*");
            //TODO 获取数据
            Message message = canalConnector.get(100);
            //TODO 获取Entry集合
            List<CanalEntry.Entry> entries = message.getEntries();
            //TODO 判断集合是否为空,如果为空,则等待一会继续拉取数据
            if (entries.size() <= 0) {
                System.out.println("当次抓取没有数据，休息一会。。。。。。" + DateUtil.now());
                ThreadUtil.sleep(1000);
            } else {
                //TODO 遍历entries，单条解析
                for (CanalEntry.Entry entry : entries) {
                    //1.获取表名
                    String tableName = entry.getHeader().getTableName();
                    //2.获取类型
                    CanalEntry.EntryType entryType = entry.getEntryType();
                    //3.获取序列化后的数据
                    ByteString storeValue = entry.getStoreValue();
                    //4.判断当前entryType类型是否为ROWDATA
                    if (CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                        //5.反序列化数据
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                        //6.获取当前事件的操作类型
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        //7.获取数据集
                        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                        //8.遍历rowDataList，并打印数据集
                        for (CanalEntry.RowData rowData : rowDataList) {
                            // 改变前数据
                            JSONObject beforeData = new JSONObject();
                            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            for (CanalEntry.Column column : beforeColumnsList) {
                                beforeData.put(column.getName(), column.getValue());
                            }
                            // 改变后数据
                            JSONObject afterData = new JSONObject();
                            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                            for (CanalEntry.Column column : afterColumnsList) {
                                afterData.put(column.getName(), column.getValue());
                            }

                            // todo 学生表改变
                            if(tableName.equals("student")){
                                // 如果是更新操作
                                if(eventType.toString().equals("UPDATE")){
                                    String studentId = beforeData.get("id").toString();
                                    SearchRequest searchRequest = new SearchRequest();
                                    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                                    // 精确查询
                                    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studentId", studentId);
                                    searchSourceBuilder.query(termQueryBuilder);
                                    // 3.添加条件到请求
                                    searchRequest.source(searchSourceBuilder);
                                    // 4.客户端查询请求
                                    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                                    // 5.查看返回结果
                                    SearchHits hits = search.getHits();
                                    System.out.println(JSON.toJSONString(hits));
                                    System.out.println("=======================");
                                    for (SearchHit documentFields : hits.getHits()) {
                                        System.out.println(documentFields.getSourceAsMap());
                                        System.out.println(documentFields.getId());

                                        UpdateRequest request = new UpdateRequest("student", documentFields.getId());
                                        StudentVO studentVO = studentMapper.getStudentVoById(Long.valueOf(studentId));
                                        request.doc(JSON.toJSONString(studentVO), XContentType.JSON);
                                        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
                                        System.out.println(response.status());
                                    }

                                }else if (eventType.toString().equals("INSERT")){
                                    String studentId = afterData.get("id").toString();
                                    StudentVO studentVO = studentMapper.getStudentVoById(Long.valueOf(studentId));

                                    BulkRequest bulkRequest = new BulkRequest();
                                    bulkRequest.timeout("2m"); // 可更具实际业务是指
                                    bulkRequest.add(
                                            new IndexRequest("student")
                                                    .source(JSON.toJSONString(studentVO), XContentType.JSON)
                                    );
                                    restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                                }else if (eventType.toString().equals("DELETE")){
                                    String studentId = beforeData.get("id").toString();
                                    SearchRequest searchRequest = new SearchRequest();
                                    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                                    // 精确查询
                                    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studentId", studentId);
                                    searchSourceBuilder.query(termQueryBuilder);
                                    // 3.添加条件到请求
                                    searchRequest.source(searchSourceBuilder);
                                    // 4.客户端查询请求
                                    SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
                                    // 5.查看返回结果
                                    SearchHits hits = search.getHits();
                                    System.out.println(JSON.toJSONString(hits));
                                    System.out.println("=======================");
                                    for (SearchHit documentFields : hits.getHits()) {
                                        System.out.println(documentFields.getSourceAsMap());
                                        System.out.println(documentFields.getId());

                                        DeleteRequest request = new DeleteRequest("student", documentFields.getId());
                                        request.timeout("1s");
                                        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
                                        System.out.println(response.status());// OK
                                    }
                                }

                            }else if (tableName.equals("school")){
                                if(eventType.toString().equals("UPDATE")){
                                    System.out.println("进入");
                                    String schoolId = beforeData.get("id").toString();
                                    String schoolName = afterData.get("name").toString();
                                    UpdateByQueryRequest request = new UpdateByQueryRequest("student");
                                    request.setQuery(new TermQueryBuilder("schoolId", schoolId));
                                    request.setScript(new Script("ctx._source['school']='" + schoolName + "';"));

                                    restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
                                }
                            }
                            //数据打印
                            System.out.println("Table:" + tableName +
                                    ",EventType:" + eventType +
                                    ",Before:" + beforeData);
                        }
                    } else {
                        System.out.println("当前操作类型为：" + entryType);
                    }
                }
            }
        }

    }
}
