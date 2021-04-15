package com.zjj.aisearch.utils;

/**
 * @program: myapp_search
 * @description: 文件变化监听
 * @author: zjj
 * @create: 2020-10-23 22:56:12
 **/

//这个可以监听文件内容变化
//    https://blog.csdn.net/u013223806/article/details/85858494
public class FileWatchUtils {
    public static void main(String[] args) throws Exception {
//        https://www.cnblogs.com/zishengY/p/6958564.html
//        https://www.oschina.net/question/100896_28983
        //https://www.cnblogs.com/songxingzhu/p/8961491.html
//        https://www.cnblogs.com/xunianchong/p/5832139.html
        MyFileAlterationMonitor monitor = new MyFileAlterationMonitor(
                "D:\\myapp\\AISearch",
                ".properties",
                new MyFileListenerAdaptor());
        monitor.start();

    }
}
