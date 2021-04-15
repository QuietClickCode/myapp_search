package com.zjj.aisearch.entity;

import com.zjj.aisearch.model.WebPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

/**
 * @program: elasticsearch7-demo
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021-02-08 14:23
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultData {

    private List<WebPage> data;
    private Integer total;
    private Integer time;


}
