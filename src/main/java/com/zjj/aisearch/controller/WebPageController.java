package com.zjj.aisearch.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjj.aisearch.dao.WebPageRepository;
import com.zjj.aisearch.entity.SearchResultData;
import com.zjj.aisearch.model.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: elasticsearch7-demo
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021-02-08 14:34
 **/
@RestController
@RequestMapping("/webpage")
@CrossOrigin
@Slf4j
public class WebPageController {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private WebPageRepository webPageRepository;

    @RequestMapping("/query")
    public SearchResultData query(@RequestParam String text, String page
    ) throws IOException {
        Request request = new Request("GET", "webpage/_search");

        request.setJsonEntity(String.format("{\n" +
                "  \"from\": " + (Integer.parseInt(page) - 1) * 10 + "," +
                "  \"size\": " + 10 + "," +
                "  \"_source\": [\"createtime\",\"id\",\"title\",\"content\"],\n" +
                "  \"query\": {\n" +
                "    \"multi_match\": {\n" +
                "      \"query\": \"%s\",\n" +
                "      \"fields\": [\"title\",\"content\"]\n" +
                "    }\n" +
                "  },\n" +
                "  \"highlight\": {\n" +
                "  \"order\":\"score\"," +
                "    \"fields\": {\n" +
                "      \"title\": {},\n" +
                "      \"content\": {},\n" +
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

        List<WebPage> lists = new ArrayList<WebPage>();
        for (int i = 0; i < hits.size(); i++) {
            WebPage webpage = new WebPage();
            JSONObject hit = hits.getJSONObject(i);
            JSONObject highlight = hit.getJSONObject("highlight");
            JSONObject _source = hit.getJSONObject("_source");
            String _id = (String) hit.get("_id");
            JSONArray hightTitle = highlight.getJSONArray("title");
            JSONArray hightContent = highlight.getJSONArray("content");
            if (hightTitle != null) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < hightTitle.size(); j++) {
                    String seg = hightTitle.getString(j);
                    stringBuffer.append(seg + "|");


                }
                webpage.setTitle(stringBuffer.toString());

            } else {
                webpage.setTitle(_source.getString("title"));

            }

            if (hightContent != null) {
                StringBuffer stringBufferContent = new StringBuffer();

                for (int j2 = 0; j2 < hightContent.size(); j2++) {
                    String seg = hightContent.getString(j2);
                    stringBufferContent.append(seg + "|");


                }
                webpage.setContent(stringBufferContent.toString());

                System.out.println(webpage.getContent() + "\n\n");

            } else {
                webpage.setContent(_source.getString("content"));

            }
            webpage.setId(Integer.valueOf(_id));
            webpage.setCreatetime(_source.getDate("createtime"));
            lists.add(webpage);

        }
        SearchResultData searchResultData = new SearchResultData();
        searchResultData.setData(lists);
        searchResultData.setTotal(total);
        searchResultData.setTime(time);
        return searchResultData;

    }

    @RequestMapping("/insert")
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody WebPage webPage) throws IOException {

        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        webPage.setCreatetime(new Date());
        webPage.setUpdatetime(new Date());
        WebPage save = webPageRepository.save(webPage);
        if (save != null) {
            Request request = new Request("POST", "webpage/_doc/" + save.getId());

            request.setJsonEntity(String.format("{\n" +
                    "  \"id\":" + save.getId() + ",\n" +
                    "  \"title\":\"%s\",\n" +
                    "  \"content\":\"%s\",\n" +
                    "  \"updatetime\":\"%s\",\n" +
                    "  \"createtime\":\"%s\"\n" +
                    "}", StringEscapeUtils.escapeJson(webPage.getTitle()), StringEscapeUtils.escapeJson(webPage.getContent()), sdf.format(webPage.getUpdatetime()), sdf.format(webPage.getCreatetime())));
            try {
                Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

        }
        return "成功";
    }

    @RequestMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public String update(@RequestBody WebPage webPage) throws IOException {

        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        webPage.setUpdatetime(new Date());
        WebPage one = webPageRepository.getOne(webPage.getId());
        webPage.setCreatetime(one.getCreatetime());
        WebPage save = webPageRepository.save(webPage);
        if (save != null) {
            Request request = new Request("POST", "webpage/_update/" + save.getId());

            request.setJsonEntity(String.format("{\n" +
                    "  \"doc\": {\n" +
                    "    \"title\": \"%s\",\n" +
                    "    \"content\": \"%s\",\n" +
                    "    \"updatetime\": \"%s\"\n" +
                    "    \n" +
                    "  }\n" +
                    "}", StringEscapeUtils.escapeJson(webPage.getTitle()), StringEscapeUtils.escapeJson(webPage.getContent()), sdf.format(webPage.getUpdatetime())));
            try {
                Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

        }
        return "成功";
    }

    @RequestMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public String delete(@RequestBody WebPage webPage) throws IOException {

        webPageRepository.delete(webPage);

        Request request = new Request("DELETE", "webpage/_doc/" + webPage.getId());

        request.setJsonEntity(String.format("{}"));
        try {
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return "成功";
    }

    @RequestMapping("/queryByPage")
    public Page<WebPage> queryByPage(Integer page) throws IOException {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, 10, sort); // （当前页， 每页记录数， 排序方式）
        Page<WebPage> all = webPageRepository.findAll(pageable);
        return all;
    }

}
