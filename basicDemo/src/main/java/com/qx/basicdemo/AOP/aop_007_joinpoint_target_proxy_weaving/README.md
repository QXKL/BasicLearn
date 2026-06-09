# 第 007 节: Join Point、Target、Proxy、Weaving

## 这一节的目标

学完这节，你要能把一次 AOP 方法调用拆成 4 个部分：

- `Join Point`
- `Target Object`
- `Proxy`
- `Weaving`

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `MessageService.java`
- `ProxyObserveAspect.java`
- `ProxyObserveRunner.java`
- `README.md`

## 先看业务类

[MessageService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_007_joinpoint_target_proxy_weaving/MessageService.java)

```java
@Service
public class MessageService {

    public String send(String receiver) {
        System.out.println("执行业务方法: send -> " + receiver);
        return "发送成功: " + receiver;
    }
}
```

这里很简单：

`send(...)` 就是目标业务方法。

## 1. 什么是 Target Object

`Target Object` 就是：

`真正执行业务逻辑的那个对象`

在这一节里，它就是 `MessageService` 的实例。

也就是说：

- 真正负责“发送消息”的，是目标对象
- 切面不是来替代它的
- 切面只是包在它外面加增强

## 2. 什么是 Proxy

在 Spring AOP 里，很多时候你拿到的不是“裸的目标对象”，而是：

`代理对象`

这个代理对象负责：

- 接住方法调用
- 先执行增强逻辑
- 再决定什么时候调用目标对象

看 [ProxyObserveRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_007_joinpoint_target_proxy_weaving/ProxyObserveRunner.java)：

```java
System.out.println("注入到 Runner 中的 bean 类型: " + messageService.getClass().getName());
System.out.println("是否为 AOP 代理: " + AopUtils.isAopProxy(messageService));
System.out.println("目标类类型: " + AopUtils.getTargetClass(messageService).getName());
```

这几行的作用就是直接观察：

- 你手里拿到的是不是代理
- 目标类原本是谁

如果当前 bean 被 AOP 增强了，`messageService` 往往就是代理对象。

## 3. 什么是 Join Point

`Join Point` 可以先理解成：

`程序执行过程中，一个可以放增强逻辑的位置`

在 Spring AOP 里，你现在最常接触到的 Join Point 基本就是：

`方法执行这个位置`

看 [ProxyObserveAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_007_joinpoint_target_proxy_weaving/ProxyObserveAspect.java)：

```java
@Before("messageMethods()")
public void printJoinPointInfo(JoinPoint joinPoint) {
    System.out.println("[JoinPoint] 当前方法: " + joinPoint.getSignature().getName());
    System.out.println("[JoinPoint] 目标对象类型: " + joinPoint.getTarget().getClass().getName());
    System.out.println("[JoinPoint] 代理对象类型: " + joinPoint.getThis().getClass().getName());
}
```

这里的 `JoinPoint` 能帮你拿到：

- 当前执行的是哪个方法
- `target` 是谁
- `this` 也就是当前代理对象是谁

所以这一节里你可以先记成：

`JoinPoint = 当前这次方法执行现场的信息`

## 4. 什么是 Weaving

`Weaving` 直译就是：

`织入`

意思是：

`把增强逻辑加到目标对象执行流程里的过程`

看这一节的 `@Around`：

```java
@Around("messageMethods()")
public Object observeWeaving(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("[Weaving] 增强逻辑开始织入: " + joinPoint.getSignature().getName());
    Object result = joinPoint.proceed();
    System.out.println("[Weaving] 增强逻辑结束织入: " + joinPoint.getSignature().getName());
    return result;
}
```

这里你能直接看到：

- 业务方法执行前，增强先进去
- 业务方法执行后，增强再出来

这就是“织入”的直观效果。

## 在 Spring AOP 里怎么理解 Weaving

这一节先只记当前够用的版本：

`Spring AOP 常见的是在运行期，通过代理对象把增强逻辑织入到方法调用链里。`

你现在不用急着和 AspectJ 的编译期织入做对比。

那部分会放到后面再展开。

## 运行后你会看到什么

[ProxyObserveRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_007_joinpoint_target_proxy_weaving/ProxyObserveRunner.java) 会调用：

```java
messageService.send("tom@example.com");
```

大致会看到类似输出：

```text
注入到 Runner 中的 bean 类型: ...
是否为 AOP 代理: true
目标类类型: com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving.MessageService
从代理中取出的目标对象类型: com.qx.basicdemo.AOP.aop_007_joinpoint_target_proxy_weaving.MessageService
[Weaving] 增强逻辑开始织入: send
[JoinPoint] 当前方法: send
[JoinPoint] 目标对象类型: ...
[JoinPoint] 代理对象类型: ...
执行业务方法: send -> tom@example.com
[Weaving] 增强逻辑结束织入: send
主流程收到返回值: 发送成功: tom@example.com
```

从这段输出里你能直接看到：

- 你手里拿到的是代理对象
- 代理对象背后包着目标对象
- JoinPoint 记录的是当前方法执行现场
- Weaving 就是增强逻辑插进调用链的过程

## 这 4 个词怎么用一句话串起来

你现在可以先这样说：

`当代理对象接到方法调用时，会在某个 Join Point 上把增强逻辑织入到目标对象的方法执行过程中。`

这句话说顺了，`007` 就已经掌握得不错了。

## 这一节先别混淆的两个点

### `target` 和 `proxy` 不是一回事

- `target`：真正干业务的对象
- `proxy`：包在外面负责拦截和增强的对象

### Spring AOP 里的 Join Point 先只盯“方法执行”

更广义的 AOP 世界里，Join Point 不止方法执行这一种。

但在 Spring AOP 里，你当前最重要的理解就是：

`Join Point 基本可以先当成方法执行点。`

## 自测

看完这节后，试着自己回答：

1. 本节的目标对象是谁
2. 本节的代理对象是谁
3. `JoinPoint` 在这节代码里提供了哪些信息
4. “织入”在这节代码里体现在哪里

如果这 4 个问题你都能说清楚，`007` 就过关了。
