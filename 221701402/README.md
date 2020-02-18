# InfectStatistic-221701402

## 运行

1.在src目录下打开命令提示符

2.编译

```
javac -encoding UTF-8 InfectStatistic.java
```

3.运行

```
例：java InfectStatistic list -log D:/log/ -out D:/output.txt
```

## 功能简介

需要你的程序能够列出全国和各省在某日的感染情况

命令行（win+r cmd）cd到项目src下，之后输入命令：
```
例：java InfectStatistic list -log D:/log/ -out D:/output.txt
```
会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情况（全国总是排第一个，别的省按拼音先后排序）

list命令 支持以下命令行参数：

- -log 指定日志目录的位置，该项必会附带
- -out 指定输出文件路径和文件名，该项必会附带
- -date 指定日期，不设置则默认为所提供日志最新的一天
- -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择， 如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
- -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江

    此处输入代码

注：java InfectStatistic表示执行主类InfectStatistic，list为命令，-date代表该命令附带的参数，-date后边跟着具体的参数值，如2020-01-22。-type 的多个参数值会用空格分离，每个命令参数都在上方给出了描述，每个命令都会携带一到多个命令参数



## 博客链接

https://www.cnblogs.com/wxy-2020/p/12322525.html

## 作业链接

https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281