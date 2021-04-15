package com.zjj.aisearch.controller;

import com.zjj.aisearch.entity.Todo;
import com.zjj.aisearch.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @program: elasticsearch7-demo
 * @description:
 * @author: zjj
 * @qq: 228602890@qq.com
 * @create: 2021-02-08 14:34
 **/
@RestController
@RequestMapping("/todo")
@CrossOrigin
public class TodoController {

    @Autowired
    private TodoMapper todoMapper;



    @RequestMapping("/selectAll")
    public List<Todo> selectAll() throws IOException, InterruptedException {
        List<Todo> todos = todoMapper.selectAll();
        return todos;
    }



}
