# 第 004 节: 切点表达式 `execution`

## 这一节的目标

学完这节，你不用把 `execution(...)` 全背下来。

你只要做到一件事：

`看见一条 execution 表达式时，大概知道它在拦谁。`

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `OrderService.java`
- `ExecutionPointcutAspect.java`
- `ExecutionDemoRunner.java`
- `README.md`

## 本节的业务类

[OrderService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_pointcut_execution_004/OrderService.java)

```java
public class OrderService {

    public String createOrder(String productName) { ... }

    public String queryOrder(String orderId) { ... }

    public void cancelOrder(String orderId) { ... }
}
```

这里故意准备了 3 个方法：

- `createOrder(...)`
- `queryOrder(...)`
- `cancelOrder(...)`

这样我们就能观察：

- 拦“所有方法”时会发生什么
- 拦“特定前缀方法”时会发生什么
- 拦“某一个具体方法”时会发生什么

## 本节的切面类

[ExecutionPointcutAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_pointcut_execution_004/ExecutionPointcutAspect.java)

这一节最核心的内容就在这里。

## 第一条表达式：匹配全部方法

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_pointcut_execution_004.OrderService.*(..))")
public void allOrderMethods() {
}
```

这条可以先翻译成人话：

`匹配 OrderService 里的所有方法`

你可以先粗暴拆成这样：

- 第 1 个 `*`：返回值随意
- `OrderService`：指定类
- 第 2 个 `*`：任意方法名
- `(..)`：任意参数

所以：

- `createOrder(...)` 会被匹配
- `queryOrder(...)` 会被匹配
- `cancelOrder(...)` 也会被匹配

## 第二条表达式：匹配特定前缀方法

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_pointcut_execution_004.OrderService.create*(..))")
public void createMethods() {
}
```

这条可以翻译成人话：

`只匹配 OrderService 中方法名以 create 开头的方法`

所以：

- `createOrder(...)` 会被匹配
- `queryOrder(...)` 不会被匹配
- `cancelOrder(...)` 不会被匹配

这里最值得你记住的是：

`create*` 表示方法名前缀匹配。

## 第三条表达式：匹配某一个具体方法

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_pointcut_execution_004.OrderService.queryOrder(..))")
public void queryOrderMethod() {
}
```

这条可以翻译成人话：

`只匹配 OrderService.queryOrder(...) 这个方法`

所以：

- `queryOrder(...)` 会被匹配
- `createOrder(...)` 不会被匹配
- `cancelOrder(...)` 不会被匹配

## 用通知把“匹配结果”打印出来

为了让你看得更直观，这一节在同一个切面里给每个切点都配了一个 `@Before`：

```java
@Before("allOrderMethods()")
public void matchAllMethods(JoinPoint joinPoint) {
    System.out.println("[匹配全部方法] " + joinPoint.getSignature().getName());
}
```

```java
@Before("createMethods()")
public void matchCreateMethods(JoinPoint joinPoint) {
    System.out.println("[只匹配 create*] " + joinPoint.getSignature().getName());
}
```

```java
@Before("queryOrderMethod()")
public void matchQueryMethod(JoinPoint joinPoint) {
    System.out.println("[只匹配 queryOrder] " + joinPoint.getSignature().getName());
}
```

这节不是为了学通知，而是借通知把“谁被匹配了”打印出来。

## 本节怎么触发示例

[ExecutionDemoRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_pointcut_execution_004/ExecutionDemoRunner.java)

它会依次调用：

```java
orderService.createOrder("Java 并发编程");
orderService.queryOrder("ORDER-3001");
orderService.cancelOrder("ORDER-3002");
```

## 运行后你会看到什么

大致会出现这样的效果：

```text
[匹配全部方法] createOrder
[只匹配 create*] createOrder
执行业务方法: createOrder -> Java 并发编程

[匹配全部方法] queryOrder
[只匹配 queryOrder] queryOrder
执行业务方法: queryOrder -> ORDER-3001

[匹配全部方法] cancelOrder
执行业务方法: cancelOrder -> ORDER-3002
```

从这个结果里你能直接看到：

- `allOrderMethods()` 拦到了全部 3 个方法
- `createMethods()` 只拦到了 `createOrder(...)`
- `queryOrderMethod()` 只拦到了 `queryOrder(...)`

## 这一节最重要的直觉

你现在不用死背完整语法。

先建立 3 个最常用直觉就够了：

1. `*.*(..)` 这种写法，通常意味着“很多方法一起拦”
2. `create*(..)` 这种写法，通常意味着“按方法名前缀拦”
3. `queryOrder(..)` 这种写法，通常意味着“只拦某一个具体方法”

## 自测

看完这一节后，试着自己判断：

```java
execution(* com.qx.basicdemo.AOP.aop_pointcut_execution_004.OrderService.cancelOrder(..))
```

它会拦谁？

答案是：

`只拦 OrderService.cancelOrder(...)`

再看这一条：

```java
execution(* com.qx.basicdemo.AOP.aop_pointcut_execution_004.OrderService.*(..))
```

它会拦谁？

答案是：

`拦 OrderService 里的所有方法`

## 小结

这一节最后只留一句话：

`execution(...) 的本质，就是用一条规则描述“我要拦哪些方法”。`
