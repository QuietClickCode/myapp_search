package com.zjj.aisearch.controller;

import com.zjj.aisearch.dao.MyProjectRepository;
import com.zjj.aisearch.model.MyProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("myProject")
@CrossOrigin
public class MyProjectController {

    @Autowired
    private MyProjectRepository myProjectRepository;


    @RequestMapping("/insert")
    public MyProject insert(@RequestBody MyProject myProject) {
        myProject.setCreatetime(new Date());
        myProject.setUpdateCount(1);
        myProject.setUpdatetime(new Date());

        return myProjectRepository.save(myProject);
    }

    @RequestMapping("/selectAll")
    public List<MyProject> selectAll() {

        return myProjectRepository.findAll();
    }

    @RequestMapping("/inserOne")
    public MyProject inserOne() {

        MyProject myProject = new MyProject();
        myProject.setKeyword("xk");
        myProject.setDescription("测试");
        myProject.setCreatetime(new Date());
        return myProjectRepository.save(myProject);
    }


}
