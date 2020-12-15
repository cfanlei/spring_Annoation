# 1.@ComponentScan

## 1.ComponentScan里的内容

```java
@Configuration //告诉spring这是一个配置类
@ComponentScan(value = "cn.cfanlei",includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})
},useDefaultFilters = false
)
/**
 * includeFilters Filter[]
 * excludeFilters Filter[]
 */
public class MainConfig {
    @Bean
    public Person person(){
        return new Person("lisi",20);
    }
}
```

### 1.FilterType的取值

```java
    ANNOTATION,//给定的注解
    ASSIGNABLE_TYPE,//给定的类型
    ASPECTJ,//使用ASPECTJ表达式
    REGEX,根据正则表达式
    CUSTOM;自定义
```

### 2. 使用自定义的Filter

CUSTOM说明：

```
/** Filter candidates using a given custom
 * {@link org.springframework.core.type.filter.TypeFilter} implementation.
 */
```

```java
public class MyTypeFilter implements TypeFilter {
    /**
     *
     * @param metadataReader 读取到当前正在扫描类的类信息
     * @param metadataReaderFactory 可以获取到其他任何类信息
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取当前类注解的信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类资源（类的路径）
        Resource resource = metadataReader.getResource();

        String enclosingClassName = classMetadata.getClassName();
        System.out.println("----->"+enclosingClassName);
        /**
         * 逻辑处理，返回bool类型，表示匹配成功或失败
         */
        return false;
    }
}

```

# 2. @Scope



```
@Configuration
@ComponentScan(value = "cn.cfanlei")
public class ConfigScope {

    /**
     *      * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE 
     *      * @see ConfigurableBeanFactory#SCOPE_SINGLETON
     *      * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST  --web环境
     *      * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION  --web环境
     *      
     *     singleton --单实例
     *     prototype --多实例  
     *     request   --同一次请求创建一个实例
     *     session   --听一个session创建一个实例
     *     2. 懒加载
     *              单实例bean：默认在容器启动的时候创建对象
     *              懒加载：容器启动不创建对象第一次使用（获取）Bean创建对象，并初始化
     * @return
     */
    @Scope("prototype")
    @Lazy
    @Bean
    public Person person(){
        return new Person("cfanlei",22);
    }
}
```

# 3.Conditional

(按照一定的条件判断，满足条件给容器注册bean)

<ul style="color =#ff0000"> (按照一定的条件判断，满足条件给容器注册bean)</ul>

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {

	/**
	 * All {@link Condition} classes that must {@linkplain Condition#matches match}
	 * in order for the component to be registered.
	 */
	Class<? extends Condition>[] value();

}
由conditional注解类中可以看出 需要在@Conditional()传入Condition类型的数组，点进Condition类中的以下代码
```

```java
@FunctionalInterface
public interface Condition {

	/**
	 * Determine if the condition matches.
	 * @param context the condition context
	 * @param metadata the metadata of the {@link org.springframework.core.type.AnnotationMetadata class}
	 * or {@link org.springframework.core.type.MethodMetadata method} being checked
	 * @return {@code true} if the condition matches and the component can be registered,
	 * or {@code false} to veto the annotated component's registration
	 */
	boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);

}
可以看出Condition是个接口，则我们就可以实现Condition接口创建条件,接下来实现Condition接口
```

下面类实现了condition接口

```java

/**
 * 判断操作系统是否linux
 */
public class LinuxConditions implements Condition {
    /**
     *
     * @param context 判断条件能使用的上下文环境
     * @param metadata 当前标注了conditional注解的注释信息
     * @return 如果当前环境是linux环境，返回true 否则返回false
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //TODO 是否时linux系统
        //1.能获取到ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //获取到类加载器
        ClassLoader classLoader = context.getClassLoader();
        //获取当前环境信息
        Environment environment = context.getEnvironment();
        /**
         * 获取到bean定义的注册类 可以从BeanDefinitionRegistry里查有没有哪个备案的定义，
         * 或者用BeanDefinitionRegistry来注册/移除.. bean的定义
         */
        BeanDefinitionRegistry registry = context.getRegistry();
        String property = environment.getProperty("os.name");
        if(property.contains("Linux")){
            return true;
        }else{
            return false;
        }

    }
}
```

测试

```java
  @Test
    public void  testAnnoation02(){

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(ConfigScope.class);
        String[] beanNamesForType = annotationConfigApplicationContext.getBeanNamesForType(Person.class);

        Map<String, Person> persons = annotationConfigApplicationContext.getBeansOfType(Person.class);

        //获取环境
        ConfigurableEnvironment environment = annotationConfigApplicationContext.getEnvironment();
        //获取操作系统
        String osname = environment.getProperty("os.name");
        System.out.println(osname);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }
        System.out.println(persons);

    }
```

# 4.@Import

快速的给容器中导入组件

## 1. @import

#### 快速导入一/多个组件

#### import(Color.class) /import({xx.class,xxx.class})容器会自动注册组件，默认名为cn.cfanlei.bean.Color全类名

## 2.ImportSelector

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * {@link Configuration @Configuration}, {@link ImportSelector},
	 * {@link ImportBeanDefinitionRegistrar}, or regular component classes to import.
	 */
	Class<?>[] value();

}
根据@import接口的注释可以发现 @import中可以是ImportSelector 返回需要导入组件的全类名
```

````java
public class MyImportSelector implements ImportSelector {
    /**
     *
     * @param importingClassMetadata 当前标注@import注解的类的所有信息
     * @return 返回值就是导入到容器中的组件全类名
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];//返回的全类名就是需要注册到容器组件的全类名
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return null;
    }
}

````

@import()中写入

```java
@Import({xxx.class, MyImportSelector.class})
```



