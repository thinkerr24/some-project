# 多线程下载器

环境:idea2.21.2 community + openjdk11<br/>

创建一个普通的 java se 项目，名为 powerdownloader，不需要第三方 jar 包

##### 项目结构

<ul>
<li>constant：存放常量类的包</li>
<li>core：存放了下载器核心类的包</li>
<li>util：存放工具类的包</li>
<li>Main：主类</li>
</ul>
从互联网下载文件有点类似于我们将本地某个文件复制到另一个目录下，也会利用IO流进行操作。对于从互联网下载，还需要将本地和下载文件所在的服务器建立连接。

#### IDEA 快捷键

通过类名查找类: 点两下 shift
查看类结构: alt + 大键盘 7
