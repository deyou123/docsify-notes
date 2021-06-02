#### 论吃苦
和朋友聊天，说到了学习的问题。不知道何时听过一句话，***因为不想吃生活的苦，所以我忍受着学习的苦。生活的苦只要躺着就能吃到，而学习的苦却要我们逼着自己去吃。***每天下班回来等到孩子睡了开始学习，学的差不多了再开始写博客，然后发公众号，天天两三点让最近的身体有些吃不消，光坐班车睡过站就好几次，司机现在都认识我了，到站先喊下我。牛气...一分耕耘不一定会有一分收获，但要想有所收获，只能靠持续的付出才有机会...努力！
昨天吧python字符串相关的方法进行了总结，说实话零碎的知识点太多，统计、分类、整理、最后一个个的说明花了2个多小时的时间。但整体串过一遍，对之前的知识又进行了一次查漏补缺，温故而知新，还不错...
今天和大家聊聊python命令行参数，虽然使用场景没有字符串多，但细节觉得成败,当别人看到你提供更优雅的调用方式时，会对你的严谨点赞。此时，深藏功与名！升堂...

#### python命令行参数
在python开发中，我们经常会遇到在执行.py文件时，需要给代码传参的情况。
遇到传参的场景，最基础的传参方式为`*.py argv1 argv2 ...`,然后我们在代码中通过sya.argv来解析入参。
这样使用没有问题，但是不严谨、不正式，更不够帅气...谁知道你这些参数是干嘛的？
其实python基于sys.argv的命令行参数，存在三个模块getopt --> optprase --> argprase。今天咱们来说说处于命令行模块顶峰的**argprase**！

#### 入门五板斧
```python
# -*- coding: utf-8 -*-
# @Author   : 王翔
# @JianShu  : 清风Python
# @Date     : 2019/6/25 23:58
# @Software : PyCharm
# @version  ：Python 3.7.3
# @File     : 11.python_argparse_cookbook.py

# 导入模块
import argparse

# 实例化解析器对象
parser = argparse.ArgumentParser(description='python argparse cookbook')
# 添加参数
parser.add_argument('-v', '--version', help="get version")

# 获取参数集合
args = parser.parse_args()

# 打印参数值
print("User input code version is: {}".format(args.version))
```

#### 关于ArgumentParser
ArgumentParser用于创建解析器对象，它提供了很多可选参数：
>Object for parsing command line strings into Python objects.
Keyword Arguments:
- prog -- The name of the program (default: sys.argv[0])
- usage -- A usage message (default: auto-generated from arguments)
- description -- A description of what the program does
- epilog -- Text following the argument descriptions
- parents -- Parsers whose arguments should be copied into this one
- formatter_class -- HelpFormatter class for printing help messages
- prefix_chars -- Characters that prefix optional arguments
- fromfile_prefix_chars -- Characters that prefix files containing
    additional arguments
- argument_default -- The default value for all arguments
- conflict_handler -- String indicating how to handle conflicts
- add_help -- Add a -h/-help option
- allow_abbrev -- Allow long options to be abbreviated unambiguously

其中description用于添加对程序的描述，使用较多，其他的作为了解即可：
- prog - 程序的名字（默认：sys.argv[0]）
- usage - 描述程序用法的字符串（默认：从解析器的参数生成）
- description - 参数帮助信息之前的文本（默认：空）
- epilog - 参数帮助信息之后的文本（默认：空）
- parents - ArgumentParser 对象的一个列表，这些对象的参数应该包括进去
- formatter_class - 定制化帮助信息的类
- prefix_chars - 可选参数的前缀字符集（默认：‘-‘）
- fromfile_prefix_chars - 额外的参数应该读取的文件的前缀字符集（默认：None）
- argument_default - 参数的全局默认值（默认：None）
- conflict_handler - 解决冲突的可选参数的策略（通常没有必要）
- add_help - 给解析器添加-h/–help 选项（默认：True）

#### 精髓：add_argument
add_argument作为整个模块的重头戏，我们需要仔细学习。
完整格式如下：
`ArgumentParser.add_argument(name or flags...[, action][, nargs][, const][, default][, type][, choices][, required][, help][, metavar][, dest])`

- name or flags - 选项字符串的名字或者列表，例如 foo 或者 -f, --foo。
- action - 命令行遇到参数时的动作，默认值是 store。
- nargs - 应该读取的命令行参数个数，可以是具体的数字，或者是?号，当不指定值时对于 Positional argument 使用 default，对于 Optional - - - argument 使用 const；或者是 * 号，表示 0 或多个参数；或者是 + 号表示 1 或多个参数。
- const - action 和 nargs 所需要的常量值。
- default - 不指定参数时的默认值。
- type - 命令行参数应该被转换成的类型。
- choices - 参数可允许的值的一个容器。
- required - 可选参数是否可以省略 (仅针对可选参数)。
- help - 参数的帮助信息，当指定为 argparse.SUPPRESS 时表示不显示该参数的帮助信息.
- metavar - 在 usage 说明中的参数名称，对于必选参数默认就是参数名称，对于可选参数默认是全大写的参数名称.
- dest - 解析后的参数名称，默认情况下，对于可选参数选取最长的名称，中划线转换为下划线.
add_argument 用于添加入参，其中分为**定位参数**与**可选参数**，如何区别？

#### 定位参数
比如我们程序每次必须要求先输入用户名，那么我们可以这么写：
```python
import argparse

parser = argparse.ArgumentParser(description='python argparse cookbook')

parser.add_argument('user', help="input username")
parser.add_argument('pwd', help="input password")
parser.add_argument('-v', '--version', help="get version")

args = parser.parse_args()

print(args.user)
if args.version:
    print(args.version)

```
我们可以这么输入：
- a.py qingfeng 123
- a.py qingfeng 123 -v 1.0
- a.py -v 1.0 qingfeng 123
- a.py 123 -v 1.0 qingfeng # 注意这条

定位参数可以使用，但最好你的代码中，只存在一个定位参数，不然会出现最后一条例子的问题，你只要求我必须要输入参数，而没有要求我的输入顺序。这种情况我们还不如使用可选参数来明确规定**-u -p **来的更为直观，在理否？

#### 可选参数
何为可选参数？在add_argument中添加以一个或者两个中划线“-”开头的参数，为可选参数（前提是你无聊到修改ArgumentParser中的prefix_chars参数）。eg:'-v'或者'--version'。
这两种可选参数使用时需注意的是，如果同时存在-v 和--version。用户可以使用任意一种方式进行传参，但我们在获取参数的时候，只能使用**`args.version`**才能获取对应的值。

#### action='store_true'
我们平时在使用命令行参数时，有些参数是无需传值的，比如最简单的`ls -l`。
可如果这么操作，系统会报错啊。该如何是好？
`parser.add_argument('-v', '--version', action='store_true', help="get version")`
我们只需要将action赋值store_true，即可。
#### 参数的默认值
当我们通过add_argument添加一个参数时，parser.parse_args()中就会初始化一个对应的参数，并进行赋值。
默认为None。上面说的action='store_true'时，默认参数为False。
当然我们可以通过default来变更默认的None为我们所想要的值
#### 参数限制
这个用到的比较多，比如我们的代码，提供了对工具的安装、卸载、启动、停止等功能。我们可以这么操作...
`parser.add_argument('-t', '--type', choices=['install','uninstall','start','stop'])`
用户通过-t只能填写choices内部的指令，不然会提示invalid choice:入参错误
又比如，我们程序需要用户提供一个端口信息，端口必然是一个数字。针对端口是否为数字，我们可以获取参数后使用isinstance(port,int)来判断，但这样相当于重造了个轮子，argparse提供了type选项，可以在用户入参时，就直接校验，无需你再二次判断了！demo：
`parser.add_argument('-p', '--port', type=int)`
当我们输入-p abc时，模块会给出提示：*error: argument -p/--port: invalid int value: 'abc'*
#### 将可选参数变为必选
其实很简单：
`parser.add_argument('-u','--user',required=True,help="input username")`
如果为填写该参数，则会提示：*the following arguments are required: -u/--user*

#### The End
可能有些人要说，selenium系列是不太监了，只能说这个系列看的人太少了，有空了间隔着更新吧...
OK,今天的内容就到这里，如果觉得有帮助，欢迎将文章或我的微信公众号`【清风Python】`分享给更多喜欢python的人，谢谢。


