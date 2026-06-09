# 第 002 节: AOP 的 3 个核心概念

## 这一节的目标

学完这节，你要能把这句话翻译成人话：

`切面通过切点选中目标方法，再用通知完成增强。`

## 本节代码在哪

这一节只看本目录下 4 个文件：

- `AOPApplication.java`
- `UserService.java`
- `ConceptAspect.java`
- `ConceptDemoRunner.java`

它们分别对应：

- 启动入口
- 业务类
- 切面类
- 演示入口

## 先看业务类

[UserService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_core_concepts_002/UserService.java)

```java
@Service
public class UserService {

    public String register(String username) {
        System.out.println("执行业务方法: 注册用户 -> " + username);
        return "注册成功: " + username;
    }
}
```

这就是一个很普通的业务类。

它的核心工作只有一个：

`注册用户`

## 1. Aspect 是什么

[ConceptAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_core_concepts_002/ConceptAspect.java)

```java
@Aspect
@Component
public class ConceptAspect {
    ...
}
```

这里的 `ConceptAspect` 就是切面。

它不是业务类，它不负责“注册用户”。

它负责的是“给业务方法加额外动作”。

所以你可以先记住：

`Aspect = 专门放增强逻辑的类`

## 2. Pointcut 是什么

还是看 [ConceptAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_core_concepts_002/ConceptAspect.java)：

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_core_concepts_002.UserService.*(..))")
public void userMethods() {
}
```

这段代码的作用是：

`把 UserService 里的方法选出来`

你现在先不用死抠 `execution(...)` 的细节。

这一节只要先明白：

`Pointcut = 决定拦哪些方法`

也就是说，切点回答的问题是：

`增强逻辑到底作用到谁身上？`

## 3. Advice 是什么

继续看 [ConceptAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_core_concepts_002/ConceptAspect.java)：

```java
@Before("userMethods()")
public void printMethodName(JoinPoint joinPoint) {
    System.out.println("[Advice] 方法执行前打印方法名: "
            + joinPoint.getSignature().getName());
}
```

这段代码会在目标方法执行前打印方法名。

这里的 `printMethodName(...)` 就是通知。

所以你可以记成：

`Advice = 拦到方法之后要执行的具体动作`

## 把三者串起来

这一节的示例可以这样拆：

1. `UserService.register()` 是目标业务方法
2. `ConceptAspect` 是切面
3. `userMethods()` 是切点
4. `printMethodName()` 是通知

流程就是：

1. 先调用 `register("tom")`
2. 切点先判断：这个方法是不是我要拦的
3. 如果命中，就执行通知
4. 然后再执行真正的业务方法

## 如果运行，会看到什么

[ConceptDemoRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_core_concepts_002/ConceptDemoRunner.java)

它会调用：

```java
userService.register("tom");
```

你大致会看到这样的输出顺序：

```text
========== 002 概念示例开始 ==========
[Advice] 方法执行前打印方法名: register
执行业务方法: 注册用户 -> tom
主流程收到返回值: 注册成功: tom
========== 002 概念示例结束 ==========
```

这段输出正好能帮你区分三者：

- 业务逻辑来自 `UserService`
- 增强动作来自 `ConceptAspect`
- 增强之所以会生效，是因为切点选中了 `register()`

## 这一节最重要的翻译

如果有人说：

`切面通过切点选中目标方法，再用通知完成增强。`

你现在就可以翻译成：

1. 先写一个“增强类”
2. 再指定它要拦哪些方法
3. 然后定义拦到之后执行什么动作

## 自测

看完本节代码后，试着自己回答这 3 个问题：

1. 本节的 `Aspect` 是哪个类
2. 本节的 `Pointcut` 是哪段代码
3. 本节的 `Advice` 是哪个方法

标准答案是：

- `Aspect`：`ConceptAspect`
- `Pointcut`：`userMethods()`
- `Advice`：`printMethodName(...)`

## 小结

这一节最后只留一句最重要的话：

`Aspect 是增强类，Pointcut 是筛选规则，Advice 是增强动作。`
