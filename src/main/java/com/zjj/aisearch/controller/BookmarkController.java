package com.zjj.aisearch.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjj.aisearch.entity.Bookmark;
import com.zjj.aisearch.entity.SearchResultData;
import com.zjj.aisearch.mapper.BookmarkMapper;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: elasticsearch7-demo
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021-02-08 14:34
 **/
@RestController
@RequestMapping("/bookmark")
@CrossOrigin
public class BookmarkController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private BookmarkMapper bookmarkMapper;


    @RequestMapping("/query")
    public SearchResultData query(@RequestParam String text, String page
    ) throws IOException {
        Request request = new Request("GET", "ai_file/_search");

        request.setJsonEntity(String.format("{\n" +
                "  \"from\": " + (Integer.parseInt(page) - 1) * 10 + "," +
                "  \"size\": " + 10 + "," +
                "  \"_source\": [\"createtime\",\"id\",\"fileName\",\"fileContent\"],\n" +
                "  \"query\": {\n" +
                "    \"multi_match\": {\n" +
                "      \"query\": \"%s\",\n" +
                "      \"fields\": [\"fileName\",\"fileContent\"]\n" +
                "    }\n" +
                "  },\n" +
                "  \"highlight\": {\n" +
                "  \"order\":\"score\"," +
                "    \"fields\": {\n" +
                "      \"fileName\": {},\n" +
                "      \"fileContent\": {},\n" +
                "      \"id\": {},\n" +
                "      \"createtime\": {}\n" +
                "    }\n" +
                "  }\n" +
                "}", text));
        System.out.println(text);

        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
//        System.out.println(jsonObject);

        JSONArray hits = jsonObject.getJSONObject("hits").getJSONArray("hits");
        Integer total = (Integer) jsonObject.getJSONObject("hits").getJSONObject("total").get("value");
        Integer time = (Integer) jsonObject.get("took");

        System.out.println("----------------");
        System.out.println(total);
        System.out.println(time);

        List<Bookmark> lists = new ArrayList<Bookmark>();
        for (int i = 0; i < hits.size(); i++) {
            Bookmark bookmark = new Bookmark();
            JSONObject hit = hits.getJSONObject(i);
            JSONObject highlight = hit.getJSONObject("highlight");
            JSONObject _source = hit.getJSONObject("_source");
            String _id = (String) hit.get("_id");
            JSONArray hightTitle = highlight.getJSONArray("fileName");
            JSONArray hightContent = highlight.getJSONArray("fileContent");
            if (hightTitle != null) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < hightTitle.size(); j++) {
                    String seg = hightTitle.getString(j);
                    stringBuffer.append(seg + "|");


                }
                bookmark.setTitle(stringBuffer.toString());

            } else {
                bookmark.setTitle(_source.getString("fileName"));

            }

            if (hightContent != null) {
                StringBuffer stringBufferContent = new StringBuffer();

                for (int j2 = 0; j2 < hightContent.size(); j2++) {
                    String seg = hightContent.getString(j2);
                    stringBufferContent.append(seg + "|");


                }
                bookmark.setContent(stringBufferContent.toString());

                System.out.println(bookmark.getContent() + "\n\n");

            } else {
                bookmark.setContent(_source.getString("fileContent"));

            }
            bookmark.setId(Integer.valueOf(_id));
            bookmark.setCreatetime(_source.getDate("createtime"));
            lists.add(bookmark);

        }
       /* SearchResultData searchResultData = new SearchResultData();
        searchResultData.setData(lists);
        searchResultData.setTotal(total);
        searchResultData.setTime(time);
        return searchResultData;
*/
        return null;
    }

    @RequestMapping("/bulkInsert")
    public String bulkInsert() throws IOException, InterruptedException {
        //1?????????es??????  cluster.name ????????????key??????my-application???ES???????????????
        org.elasticsearch.common.settings.Settings settings = org.elasticsearch.common.settings.Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                //??????es??????????????????ip??????????????????(???????????????????????????)
                .addTransportAddress(new org.elasticsearch.common.transport.TransportAddress(InetAddress.getByName("114.55.94.186"), 9300));
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            //todo beforeBulk??????????????????????????????
            public void beforeBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest) {
                System.out.println("---????????????" + bulkRequest.numberOfActions() + "?????????---");
            }

            //TODO ?????????afterBulk???????????????????????????????????????beforeBulk??????????????????????????????
            public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, BulkResponse bulkResponse) {
                System.out.println("---????????????" + bulkRequest.numberOfActions() + "???????????????---");
            }

            //TODO ?????????afterBulk???????????????????????????
            public void afterBulk(long l, org.elasticsearch.action.bulk.BulkRequest bulkRequest, Throwable throwable) {
                System.out.println("---????????????" + bulkRequest.numberOfActions() + "???????????????---");
            }

        })
                // 1w?????????????????????bulk
                .setBulkActions(10000)
                // 1gb?????????????????????bulk
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
                // ??????5s??????????????????
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                // ??????????????????, 0?????????, 1??????????????????
                .setConcurrentRequests(1)
                // ????????????, 100ms?????????, ????????????3???
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();


        List<Bookmark> bookmarks = bookmarkMapper.selectAll();
        System.out.println(bookmarks.size());
        for (Bookmark bookmark : bookmarks) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("title", bookmark.getTitle());
            m.put("content", bookmark.getContent());
            m.put("url", bookmark.getUrl());
            m.put("createtime", bookmark.getCreatetime());
            bulkProcessor.add(new IndexRequest("bookmark", "_doc", bookmark.getId().toString()).source(m));
        }
        bulkProcessor.flush();
        bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
        return "success";
    }

}
