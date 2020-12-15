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

<img src="E:\笔记\img\gitmenu.png" style="zoom:66%;" />

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

* git status  显示当前目录所有文件的状态     -s 内容更简洁
* git add xxx.xxx 将为跟踪文件修改为已跟踪/将文件添加到暂存区
* git reset xxx.xxx 取消跟踪状态
* git commit -m "提交信息"    提交文件到版本库
* git rm xxx.xxx   删除 工作区文件
* touch  .gitignore  创建.gitignore文件
* git log 产看日志记录

## 2.远程仓库操作

*  git remote 查看远程仓库，列出指定的远程服务器的简写，如果已经克隆了远程仓库，那么至少看到origin，这是git克隆的仓库服务器默认名称
  * -v 查看详细信息
  * git remote show origin  查看更加详细信息
* git remote add origin git@xxxxx 添加远程仓库（一个本地仓库可以添加多个远程地址）
* git clone
* git remote rm origin 移除本地配置的关联远程仓库
* git pull ~  ,git fetch ~从远程仓库拉取(pull)与抓取(fetch)
  *  fetch 不会自动合并(merge)数据
    * git merge origin/master 手动合并
  * pull 会自动合并