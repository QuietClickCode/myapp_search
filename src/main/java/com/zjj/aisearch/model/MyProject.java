package com.zjj.aisearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: myapp_search
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021年2月28日 8:27:27 星期日
 **/
@Data
@Entity
public class MyProject {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String path;
    private String keyword;
    private String type;
    private String description;
    private String url;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private Integer updateCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime;
}
