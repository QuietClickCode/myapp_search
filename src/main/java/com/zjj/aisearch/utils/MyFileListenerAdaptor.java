package com.zjj.aisearch.utils;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;

/***
 * 文件变动事件处理器类
 * @author Zane
 * @Time 2019年1月5日 上午11:03:35
 */
public class MyFileListenerAdaptor extends FileAlterationListenerAdaptor {
    ArrayList<String> arrayList = new ArrayList();

    public boolean isValid(File file) {
        arrayList.add(".java");
        arrayList.add(".log");
        arrayList.add(".txt");
        arrayList.add(".js");
        arrayList.add(".python");
        arrayList.add(".json");
        arrayList.add(".config");
        arrayList.add(".properties");
        arrayList.add(".go");
        arrayList.add(".php");
        arrayList.add(".html");
        arrayList.add(".md");
        arrayList.add(".bak");
        for (String s : arrayList) {
            if (file.getName().endsWith(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFileCreate(File file) {
        if (isValid(file)) {
            System.out.println("create.............");
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8082/createFulltext?filePath=" + file.getAbsolutePath();
            String jstoken = restTemplate.getForObject(url, String.class);
            System.out.println(jstoken);
            super.onFileCreate(file);
        }

    }

    @Override
    public void onFileDelete(File file) {
        if (isValid(file)) {
            System.out.println("delete.............");
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8082/deleteFulltext?filePath=" + file.getAbsolutePath();
            String jstoken = restTemplate.getForObject(url, String.class);
            System.out.println(jstoken);
            super.onFileDelete(file);
        }
    }

    @Override
    public void onFileChange(File file) {
        if (isValid(file)) {
            System.out.println("change.............");
            System.out.println(file.getAbsolutePath());
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8082/updateFulltext?filePath=" + file.getAbsolutePath();
            String jstoken = restTemplate.getForObject(url, String.class);
            System.out.println(jstoken);
            super.onFileChange(file);
        }
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
//		System.out.println("start");

        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
//		System.out.println("stop");
        super.onStop(observer);
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("dir change.........");
        super.onDirectoryChange(directory);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("dir create.........");
        super.onDirectoryCreate(directory);
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("dir delete.........");
        super.onDirectoryCreate(directory);
    }
}
