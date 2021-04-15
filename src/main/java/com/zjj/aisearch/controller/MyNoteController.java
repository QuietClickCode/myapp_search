package com.zjj.aisearch.controller;

import com.zjj.aisearch.dao.MyNoteRepository;
import com.zjj.aisearch.model.MyNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("myNote")
@CrossOrigin
public class MyNoteController {

    @Autowired
    private MyNoteRepository myNoteRepository;


    @RequestMapping("/insert")
    public MyNote insert(@RequestBody MyNote myNote) {
        myNote.setCreatetime(new Date());
        return myNoteRepository.save(myNote);
    }
    @RequestMapping("/selectAll")
    public List<MyNote> selectAll() {
        return myNoteRepository.findAll();
    }
    @RequestMapping("/selectOne")
    public Optional<MyNote> selectOne(String id) {
        return myNoteRepository.findById(Integer.valueOf(id));
    }

}
