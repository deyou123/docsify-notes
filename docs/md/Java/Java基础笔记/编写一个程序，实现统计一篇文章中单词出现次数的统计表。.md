# Git 学习笔记

# 本地仓库的使用

```shell
git init //初始化本地仓库
---------------------------------------
git clone  https://github.com/yizutianya/shiyanlou.git//克隆远程仓库
cd shiyanlou //进入仓库内部
git status //查看仓库
touch one.txt //在仓库中添加一个文件
git status //再次查看仓库
-------------------------------------
git add one.txt // 添加文件到暂存区
git add . //把多个文件全部添加到暂存区
---------------------------------
git status //再次查看状态
--------------------------------
git reset -- one.txt //撤销暂存区的修改
git rm --cached one.txt //撤销暂存区的修改
--------------------------------------
git reset --   //把暂存区全部修改撤销
------------------------------------
git diff //修改一个文件后，执行此命令，会跳到工作区修改页
Q  // 推出修改详情页
git add .  // 将修改全部加入暂存区
git diff --cached //查看暂存区的全部修改
------------------------------------------
git log main   //查看分支的提交历史
git log        //查看当前分支的提交历史
git log --oneline    //查看一行显示提交历史
git log n            //查看最近几个提交历史
git log --author [贡献者名字]  //查看指定贡献者的提交信息
git log --graph      //图示法查看提交俩历史
--------------------------------------------------
git config -l //查看全部信息

本地配置
git config --global user.email "邮箱"
git config --global user.name "名字"
git config -l    //查看配置后的信息
就是主目录中的隐藏文件 .gitconfig.也是可以手动修改的
cat -n .git/config
--------------------------------------------
git status   //查看文件加入暂存区后
git commit  -m  '第一次提交'   //用来提供提交的备注
git status 
---------------------------------
git log    //查看提交信息，最近的一次在最上面

```

![image-20210310184823896](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310184823896.png)

```shell
git log --reverse      //按照时间顺序显示信息。
git branch -avv   //查看全部分支信息
```

![image-20210310185913438](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310185913438.png)

第一行 ： 带星号的，表示当前所在分支，绿色是分支名，后面是版本号，第三项括号里的蓝字表示此分支跟中的远程分支的名字。

第二行，是 Git 指针信息，它指向远程仓库的 master 分支，这行信息暂不重要。

第三行，远程分支信息，详见第一行的解释。

```shell
git push    //将本地新增推送到远程仓库
git branch -avv  //查看分支
```

![image-20210310192235096](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310192235096.png)

查看版本号可以发现远程仓库版本号已经可本地的同步。6eef284

后面现实的多少时间以前，以git commit 算起。

# 版本回退

发现已经推送的文件有问题，可以修改此文件，再次添加到暂存区，提交，推送。

也可以撤销最近的一次提交，修改文件后重新提交推送。

```shell
git reset --soft HEAD^  //撤销最后一次提交
git log                 //查看提交信息
git branch -avv         //再次查看分支信息
```

![image-20210310192918344](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310192918344.png)

```shell
git reset --soft HEAD^^  //回退2次
git reset --soft HEAD~n   //撤销n 次 ，这里n 为撤销次数
git status    //查看状态果然回退到版本未提交前
cat one.txt   //查看文件内容
echo 'Hello shiyanlou!' >> one.txt  //修改文件内容
cat one.txt   //查看内容
git add .      //重新放入暂存区
git commit -m '修改one.txt' 重新提交

git push  // 推送


```

![image-20210310200837997](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310200837997.png)

```shell
git psuh -f   //版本回撤，需要使用强制推送
```

如果发现之前正确，想回到第一次提交的状态 di yi ci ti jiao

```shell
git reflog       //查看本地仓库每次版本变化的记录
------------------------------------------
git reset --hard[6eff284]  //回到指定版本
也可以使用
git reset --hard HEAD@{2}
```

## SSH 关联授权

```shell
ssh-keygen              //执行命令按几次回车，生成公钥私钥存放在主目录.ssh 目录
```

找到id_rsa.pub 文件。用记事本打开，赋值到GitHub 账号中

https://github.com/settings/keys 中。就完成了授权。

# 为Git 命令设置别名



```shell 
git config --global alias.st status  // 查看状态
git st                            // 使用别名查看状态
git config --global alias.br 'branch -avv' //查看分支
```

![image-20210310203701194](C:\Users\zhaid\AppData\Roaming\Typora\typora-user-images\image-20210310203701194.png)

查看别名如图。

# git 分支管理

```shell
git fetch    // 将远程仓库的分支信息拉取到本地仓库
```

举例

```shell
echo 'third updare' >> one.txt
cat one.txt
git status
git add .
git commit -m 'thrid update'
git push
```

此时本地和远程仓库都有三个提交点。