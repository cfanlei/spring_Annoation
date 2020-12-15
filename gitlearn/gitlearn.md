# git&github

## 1.初始化

```java
git config --global user.name "cfanlei"
git config --global user.email cfanlei@126.com
git config --list
```

## 2.区域

* 工作区
* 暂存区
* 版本库

## 3.对象

* git对象
* 树对象
* 提交对象







#  git 目录

<img src="E:\笔记\gitlearn\img\gitmenu.png" style="zoom:67%;" />

*  hooks        		    目录包含客户端或服务端的钩子脚本
* info                         包含一个全局性的排除文件 
* logs                        保存日志信息           
* **objects**                 目录存储所有数据内容
* **refs**                        目录存储指向数据 （分支）的提交对象的指针
* config                     文件包含项目特有的配置选项
* description            用来显示对仓库的描述信息
* **HEAD**                     文件指示目前被检测的分支
* **index**                     文件保存的暂存信息

#  命令

##  1.本地仓库操作

* **git status**  显示当前目录所有文件的状态     -s 内容更简洁
* **git add xxx.xxx** 将为跟踪文件修改为已跟踪/将文件添加到暂存区
* **git reset xxx.xxx** 取消跟踪状态
* **git commit -m "提交信息"**    提交文件到版本库
  * -a
* **git rm xxx.xxx**   删除 工作区文件
* **touch  .gitignore**  创建.gitignore文件
* **git log** 产看日志记录

## 2.远程仓库操作

*  **git remote** 查看远程仓库，列出指定的远程服务器的简写，如果已经克隆了远程仓库，那么至少看到origin，这是git克隆的仓库服务器默认名称
  * -v 查看详细信息
  * **git remote show origin**  查看更加详细信息
* **git remote add origin git@xxxxx** 添加远程仓库（一个本地仓库可以添加多个远程地址）
* **git clone**
* **git remote rm origin** 移除本地配置的关联远程仓库
*  **git pull** ~  ,**git fetch origin maste**r ~从远程仓库拉取(pull)与抓取(fetch)
  *  fetch 不会自动合并(merge)数据
    *    **git merge origin/master**手动合并   ‘/’是命令部分
  *  pull 会自动合并
     * git pull origin master --allow-unrelated-histories  //允许合并，当从远端拉取报合并失败时添加--allow-unrelated-histories 强行拉取下来

* **git push origin master**  //origin代表远程仓库地址  ，master是远程仓库的分支   推送



## 3.分支

* **git branch** 查看本地分支
  * -r 查看远程分支git
  * -a 查看所有分支

* **git branch xxx** 创建xxx分支

* **git checkout xxx** 切换到xxx分支

* **git push origin xxxbranch** 推送到远端的xxx分支

### 1.合并分支

* **git merge b1**  需要在master分支下操作，将b1分支合并到master分支上
  * 合并并不是一直顺利，如在两个不同的分支中，对同一个文件的同一个部分进行不同的修改，git会提示文件冲突，此时需要打开文件修复冲突内容，最后执行git add命令来标识冲突已解决

### 2.删除本地分支

* **git branch -d xxxbranch** 删除本地xxxbranch
  * -d /D 删除/强制删除本地分支

### 3.删除远端分支

* **git push origin -d xxxbranch**   删除远端的xxx分支

### 注： 

* 1.创建分支的时候，是基于当前分支为基础创建的新分支
* 2.本地的主分支才能推送到远端的分支上

