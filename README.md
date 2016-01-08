# playframework with war
具体ruby代码实现在文件 `play2war.rb`中    
步骤如下：
1. `sbt package`，将该项目打成一个jar包
2. 在play项目的目录下，`sbt dist`打成一个zip包，目录结构如下：
```
tree target/universal/project-name-1.0-SNAPSHOT
.
|---bin
|__|---project-name
|__|---project-name.bat
|---conf
|__|---application.conf
|__|---logback.xml
|__|---routes
|--- lib
|__|---...
|__|---project-name.project-name-1.0-SNAPSHOT-assets.jar
|__|---project-name.project-name-1.0-SNAPSHOT-sans-externalized.jar
|__|---...

```
需要将conf下的东西也打到 `project-name.project-name-1.0-SNAPSHOT-sans-externalized.jar`中，   
下面就构件war的目录结构，将上述lib下的jar（包括改过的项目jar）都放到war目录的lib下，再把之前   
打的jar也放到里面。这样war的结构就好了。之后用jar命令打成war就可以了。  
war可以没有web.xml
