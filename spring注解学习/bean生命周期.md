# bean的生命周期

* bean创建------------>初始化------------>销毁过程

* 容器管理bean的生命周期 

* 我们可以自定义初始化和销毁方法；容器在bean进行到当前生命周期的时候来调用我们的自定义方法

  @Bean注解的代码如下：

```java

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

	@AliasFor("name")
	String[] value() default {};

	@AliasFor("value")
	String[] name() default {};

	@Deprecated
	Autowire autowire() default Autowire.NO;

	boolean autowireCandidate() default true;

	String initMethod() default "";

	String destroyMethod() default AbstractBeanDefinition.INFER_METHOD;

}

```

## 1.@Bean(initMethod="",destroyMethod="")

* 初始化方法执行的时机：对象创建完成并赋值好后调用初始化方法
* 销毁方法执行时机：
  * 单实例：容器关闭的时候执行 
  * 多实例：容器不会管理这个bean，容器不会调用销毁方法

## 2.通过让类实现initizllizingBean,DisposableBean

```java
@Component
public class Life4Bean02 implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.err.println("init....");
    }

    @Override
    public void destroy() throws Exception {
        System.err.println("destory....");
    }
}

```

##  3.通过注解

* 可以使用JSR250规范定义的注解

  * @PostConstruct :在bean创建完成并且赋值完成，来执行初始化方法
  * @PreDestory:在容器销毁之前通知我们进行销毁工作

  

```java
@Component
public class Life4Bean03 {
    public Life4Bean03(){
        System.out.println("Life4Bean  ...constructor");
    }
    @PostConstruct
    public void init(){
        System.out.println("Life4Bean  ...init");
    }
    @PreDestroy
    public void destory(){
        System.out.println("Life4Bean  ...destory");
    }
}
```

## 4.BeanPostProcessor接口

(后置处理器)任何初始化之前调用

```java

public interface BeanPostProcessor {
	//初始化之前调用
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	//初始化之后调用
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}

```

实现该接口

```java
@Component
public class Life4Bean04 implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后置处理器initbean："+bean.toString()+"name:"+beanName);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("后置处理器afterInit..bean："+bean.toString()+"name:"+beanName);

        return null;
    }
}

```

### 1.BeanPostProcessor执行流程

```java
populateBean(beanName, mbd, instanceWrapper);//给bean进行赋值
{
    //初始化
	applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
	invokeInitMethods(beanName, wrappedBean, mbd);//执行自定义初始化
	applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
}
```

### 2.spring底层对BeanPostProcessor 的使用

bean赋值，注入其他组件，@Autowired，生命周期注解功能，xxxBeanPostProcessor