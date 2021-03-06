# RedisDesktop----java实现Redis桌面工具

## 可执行jar:
* [2021-03-30 RedisGUI.exe](https://github.com/zhangsan0415/RedisDesktop/releases/download/20200330/RedisGUI.exe)
* [2021-03-30 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/20200330/RedisDesktop-1.0.0-jar-with-dependencies.jar)
* [2021-01-15 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/20210115/RedisDesktop-1.0.0-jar-with-dependencies.jar)
* [2020-12-10 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/ZSLREDISGUI-20201210/RedisDesktop-1.0.0-jar-with-dependencies.jar)
* [2020-11-18 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/20201118/RedisDesktop-1.0.0-jar-with-dependencies.jar)
* [2020-11-03 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/untagged-98a8096d55f1439893b9/RedisDesktop-1.0.0-jar-with-dependencies.jar)
* [2020-10-30 RedisDesktop-1.0.0-jar-with-dependencies.jar](https://github.com/zhangsan0415/RedisDesktop/releases/download/RedisDestktop/RedisDesktop-1.0.0-jar-with-dependencies.jar);

## 更新日志
### 2021-03-30
*   1、优化功能按钮布局以及名称
*   2、添加匹配删除功能（delete *key*）

### 2021-01-15
*   1、将新建链接从鼠标菜单中移到上方工具栏
*   2、JDK8之前与JDK8之后字体大学自动适配
*   3、改原来鼠标单击菜音为工具栏按钮
*   4、添加切换数据库时右侧数据面版清空
*   5、添加双击连接数据库
*   6、添加对不同分辩率的处理

### 2020-12-10
*   数据库数量由原来默认16修改为config get databases,如果获取数据库数量多于32则只展示32个数据库

### 2020-12-09
*   分支代码合并到Master
*   解决一个连接只能打开一个控制台的问题
*   错误日志区域添加清除功能
*   测试连接与连接超时设置为5秒
*   字休设置为宋体
*   控制台非Redis命令输入时错误优化

### 2020-11-18
*	修改控制台数据库切换不了的BUG
*	添加当前所处数据库展示
*	调整界面大小

### 2020-11-03
*	1、优化查询keys（scan）命令无匹配返回
*	2、界面添加删除键功能
*	3、优化控制台命令支持，添加命令行格式化显示

### 2020-10-30
* 添加Redis控制台功能
* 控制台clear清屏功能

## LICENSE
* jedis[LICENSE.txt](https://raw.githubusercontent.com/redis/jedis/master/LICENSE.txt)
* lettuce-core[license-2.0.txt](https://www.apache.org/licenses/LICENSE-2.0.txt)
* fastjson[license-2.0.txt](https://www.apache.org/licenses/LICENSE-2.0.txt)
