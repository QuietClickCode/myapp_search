package com.zjj.aisearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class Todo {

    private Long id;


    private String content;

    private Date createtime;

}
