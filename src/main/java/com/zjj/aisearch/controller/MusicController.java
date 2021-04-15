package com.zjj.aisearch.controller;

import com.zjj.aisearch.mapper.MusicMapper;
import com.zjj.aisearch.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("music")
public class MusicController {

    @Autowired
    private MusicMapper musicMapper;


    @RequestMapping("/setDayRecommend")
    public String setDayRecommend(@RequestBody Music music) {
//        System.out.println(music);


        return "success";
    }



}
