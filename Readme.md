#说明
项目基于Srping Boot，主要用到的有Spring MVC，SpringDateJpa等。运行项目需要在application.properties中将jdbc部分换成自己的。
sql文件夹下wp_test.sql为简单的初始数据。
###wp-common
公共依赖模块，项目公用的BEAN，REPO，UTILS可以放到这里面
###wp-web
WEB项目模块，在这里开发Service和Controller
