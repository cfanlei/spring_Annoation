# Spring 自动配置原理

## 1. springBoot启动时加载主配置类，开启了自动配置功能@EnableAutoConfiguration

## 2.EnableAutoConfiguration 作用

* **== 利用AutoConfigurationImportSelector给容器导入一些组件 ==**

* ```
  AutoConfigurationImportSelector.selectImports()
  ```

* ```
  SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass()
  //扫描所有jar包  类路径下 META-INF/spring.factories
  ```