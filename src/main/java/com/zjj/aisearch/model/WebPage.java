package com.zjj.aisearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: elasticsearch7-demo
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021年4月10日 14:07:42 星期六
 **/
@Data
@Entity
public class WebPage {

    @Id
    @SequenceGenerator(name = "webPage_Sequence", sequenceName = "webPage_Sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "webPage_Sequence")
    private Integer id;


    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createtime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updatetime;

}
