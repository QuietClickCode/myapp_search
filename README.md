# AISearch
一个智能的搜索引擎


2019年10月6日:
基本架构:命令模式,便签模式,转发模式
每个操作都会形成日志记录下来,
登录注册,转发,查询

2019年10月7日
重构数据表结构,重构controller,精简前端页面,limit限制数据量
下一步考虑查询优化,上elasticsearch,完成其他几个日志页面的查询

2019年10月22日
shiro做登录,授权,系统日志记录用AOP实现,分页完成,各个列表的查询完成,基本实现前后端分离

2019年10月26日
aisearch前后端分离完成,这是前端工程vue-cli地址https://github.com/QuietClickCode/my-vuecli

并做了一些用户体验上的优化,包括布局样式,操作体验,修复一些小bug,比如分页问题,查询问题等

2019年10月28日
利用elasticsearch完成豆瓣电影搜索

2019年10月28日
利用elasticsearch完成csdn,简书,博客园搜索


2020年1月14日
文件上传解析为文本,导入到es中实现高亮搜索


2020年1月14日
集成vue-element-admin做AISearch的后台管理


2020年1月15日17:21:32
搜索引擎图像化改造完成

2020年1月16日11:53:53 1.0.3->1.0.4
tika支持各种类型的文件,doc,java,js,html,md,excel,pdf等

2020年2月9日18:02:00
发布1.1.0 后台管理完成,集成redis缓存和activemq消息中间件

2020年2月15日20:07:01
整合FastDFS实现图床和网盘功能

# todo
* 实现随机图片,随机段子,随机天气,随机英语,随机推荐,加入反馈机制
* 浏览器新标签和手机开屏启动都可以这么来
* 利用碎片化时间,加一点小游戏元素,反馈,记录等,随机内容可自定义和推荐
