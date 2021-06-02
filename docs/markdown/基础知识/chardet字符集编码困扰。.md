#### 秋季注意卫生
最近朋友间出现各种在外面吃饭，导致腹泻、肠胃炎的情况。在此提醒大家，秋季是传染病高发季节，请大家务必注意饮食卫生，身体是搬砖的本钱啊！

#### 恼人的字符集
不论是什么编程语言，都免不了涉及到字符集的问题，我们经常在读写本文、获取网页数据等等各类情景下，需要和字符集编码打交道。这几天在公司就遇到了这么一个问题，由于软件需要初始化许多参数信息，所以使用**ConfigParser**模块进行配置文件的读写操作。本来一切OK，但当把这些.ini配置文件提交到git仓库后，再次下载使用时，默认的utf-8字符集编码，被git默认修改成了gbk编码。导致读取配置文件时默认使用的utf-8编码，最终导致异常报错。那么该如何解决读取文件时的字符集问题呢？Python有专门的字符集检测模块**`chardet`**，今天就带大家一起学习下它。

#### chardet入门
#### 模块介绍
**Chardet：通用字符编码检测器，Python版本：*需要Python 2.6,2.7或3.3+。***
检测字符集范围：
- ASCII，UTF-8，UTF-16（2种变体），UTF-32（4种变体）
- Big5，GB2312，EUC-TW，HZ-GB-2312，ISO-2022-CN（繁体中文和简体中文）
- EUC-JP，SHIFT_JIS，CP932，ISO-2022-JP（日文）
- EUC-KR，ISO-2022-KR（韩文）
- KOI8-R，MacCyrillic，IBM855，IBM866，ISO-8859-5，windows-1251（西里尔文）
- ISO-8859-5，windows-1251（保加利亚语）
- ISO-8859-1，windows-1252（西欧语言）
- ISO-8859-7，windows-1253（希腊语）
- ISO-8859-8，windows-1255（视觉和逻辑希伯来语）
- TIS-620（泰国语）

#### 安装
chardet在使用前，我们需要安装它：**`pip install chardet`**即可。

#### 命令行工具
安装好chardet后，模块会附带一个命令行的检测工具：
```python
% chardetect somefile someotherfile
somefile: windows-1252 with confidence 0.5
someotherfile: ascii with confidence 1.0
```
![chardetect 命令行](https://gitee.com/BreezePython/blogimages/raw/master/images/cd341d4a3ef811ebb2bf002b67682234.png)

#### 文档地址
对于用户，现在可以通过https://chardet.readthedocs.io/获取文档。

#### 入门例子
仿照官网的例子，我们针对脚本之家和百度两个网站进行内容的编码检测：
```python
# -*- coding: utf-8 -*-
# @Author   : 王翔
# @JianShu  : 清风Python
# @Date     : 2019/8/14 2:09
# @Software : PyCharm
# @version  ：Python 3.7.3
# @File     : str_coding.py

import requests
import chardet

urls = ['https://www.jb51.net', 'https://www.baidu.com/']
for url in urls:
    r = requests.get(url)
    print(url, chardet.detect(r.content))

output：
https://www.jb51.net {'encoding': 'GB2312', 'confidence': 0.99, 'language': 'Chinese'}
https://www.baidu.com/ {'encoding': 'utf-8', 'confidence': 0.99, 'language': ''}
```
可以看到结果脚本之家是gb2312而百度是utf-8.那么是否正确呢？我们只需要在对应的网页上右键点击查看网页源代码，通过检索html中**`<meta charset="xxx" />`**内容即可获取网站编码。
![网站编码](https://gitee.com/BreezePython/blogimages/raw/master/images/cd71ac463ef811eba9e1002b67682234.png)

可以看到统计结果正确...

#### 判断文本编码
刚才看到的是获取网站返回值的编码，那么文本的编码如何获取呢？
```python
import chardet
with open('strcoding.py','rb') as f:
    print(chardet.detect(f.read()))

# output:
{'encoding': 'utf-8', 'confidence': 0.9690625, 'language': ''}
```
这里需要注意，由于对于文本的编码的未知性，我们需要使用二进制的方式打开文本，之后再获取字符集。

#### 逐步检测编码
对于简短的网页或者文本内容，我们可以按照上述的方式进行操作，但如果我的文本是以G为单位计算的,如何能快速的获取文本的字符集内容呢？我们可以使用chardet模块的逐步检测编码方式，下面我们来对比下两者的差距，我这里就不用G级的数据了，那伏天氏小说的11MB内容就已经很能说明问题了：
```python
# 原始方法
import chardet
import time

t0 = time.process_time()
with open("伏天氏.txt",'rb') as f:
    print(chardet.detect(f.read()))
t1 = time.process_time()
print(t1-t0)

# output:
{'encoding': 'utf-8', 'confidence': 0.99, 'language': ''}
105.3786755

# 逐步检索方法：
import time
from chardet.universaldetector import UniversalDetector

detector = UniversalDetector()

t0 = time.process_time()
for line in open("伏天氏.txt", 'rb'):
    detector.feed(line)
    if detector.done:
        break
detector.close()
print(detector.result)
t1 = time.process_time()
print(t1 - t0)

# output:
{'encoding': 'utf-8', 'confidence': 0.99, 'language': ''}
45.1466894
```
我们可以看到，原始的方法，我们需要将所有的文本全部读取后，一行行的检测，最终获取结果，但使用UniversalDetector的方式，进行逐行判断，当系统读取进度觉得可以确定字符集编码时，就不再往下继续检测，从而返回结果。大大缩短了检测的时间
> 如果要检测多个文本的编码（例如单独的文件），则可以重复使用单个UniversalDetector对象。只需detector.reset()在每个文件的开头调用 ，根据需要调用detector.feed 多次，然后调用detector.close()并检查detector.result字典中的文件结果。

#### 时间计时
之前版本大家在进行时间计时是，经常使用到的就是time.time()和time.clock()两个模块。两者的差异在于time.clock()计算的是cpu时间差，而time.time()计算的是电脑的系统时间差。
但当你在python3.7版本使用time.clock()时，系统会给出如下提示：
> DeprecationWarning: time.clock has been deprecated in Python 3.3 and will be removed from Python 3.8: use time.perf_counter or time.process_time instead

意思比较简单，time.clock在python3.3以后不被推荐使用，该方法依赖操作系统，建议使用per_counter(返回系统运行时间)或process_time(返回进程运行时间)代替。

#### The End
OK,今天的内容就到这里，如果觉得内容对你有所帮助，欢迎点击文章右下角的“**`在看`**”。
期待你关注我的公众号**` 清风Python`**，如果觉得不错，希望能动动手指转发给你身边的朋友们。
