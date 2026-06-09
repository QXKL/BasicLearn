# 第 005 节: 通知执行顺序与异常

## 这一节的目标

学完这节，你要能分清两件事：

1. 方法正常返回时，通知的顺序是什么
2. 方法抛异常时，通知的顺序是什么

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `AccountService.java`
- `OrderTraceAspect.java`
- `AdviceOrderRunner.java`
- `README.md`

## 本节的业务类

[AccountService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_005_advice_order/AccountService.java)

```java
public class AccountService {

    public String login(String username) { ... }

    public void lock(String username) { ... }
}
```

这里也故意准备了两条路径：

- `login(...)`：成功返回
- `lock(...)`：抛出异常

## 本节的切面类

[OrderTraceAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_005_advice_order/OrderTraceAspect.java)

这节最关键的设计是：

我把输出内容前面都加了编号。

这样你运行时不会只看到一堆日志，而是能直接看出谁先谁后。

## 成功路径的顺序

先看 `login("alice")`。

### `@Around`

```java
System.out.println("1. @Around 前半段 -> ...");
```

说明：

`@Around` 最外层先开始。

### `@Before`

```java
System.out.println("2. @Before -> ...");
```

说明：

`@Before` 在目标方法执行前运行。

### 目标方法

```java
System.out.println("3. 执行业务方法: login -> ...");
```

说明：

真正的业务逻辑在这里开始执行。

### `@AfterReturning`

```java
System.out.println("4. @AfterReturning -> ...");
System.out.println("5. 返回值 -> ...");
```

说明：

只有成功返回时，这里才会出现。

### `@After`

```java
System.out.println("6. @After -> ...");
```

说明：

只要方法结束，就会执行。

### `@Around` 后半段

```java
System.out.println("6. @Around 正常结束 -> ...");
System.out.println("7. @Around 后半段收尾 -> ...");
```

说明：

`@Around` 在最外层把整个流程包住，最后再收尾。

## 成功路径的大致输出

```text
1. @Around 前半段 -> login
2. @Before -> login
3. 执行业务方法: login -> alice
4. @AfterReturning -> login
5. 返回值 -> 登录成功: alice
6. @After -> login
6. @Around 正常结束 -> login
7. @Around 后半段收尾 -> login
主流程收到返回值: 登录成功: alice
```

你先不要纠结为什么有两个 `6`。

重点是顺序关系：

- `@AfterReturning` 在成功后出现
- `@After` 也会出现
- `@Around` 最后收尾

## 异常路径的顺序

再看 `lock("bob")`。

这时目标方法会抛异常。

### 前半段仍然一样

前面依然会先经过：

1. `@Around` 前半段
2. `@Before`
3. 目标方法

### 不同点在这里

成功路径会进入 `@AfterReturning`。

异常路径不会进入 `@AfterReturning`，而是进入：

```java
@AfterThrowing(...)
```

对应输出：

```java
System.out.println("4. @AfterThrowing -> ...");
System.out.println("5. 异常信息 -> ...");
```

### `@After` 仍然会执行

这也是这节最重要的点之一：

`@After` 不是“成功后”，而是“结束后”。

所以即使异常了，它也照样执行。

## 异常路径的大致输出

```text
1. @Around 前半段 -> lock
2. @Before -> lock
3. 执行业务方法: lock -> bob
4. @AfterThrowing -> lock
5. 异常信息 -> 账号锁定失败: 用户状态异常 -> bob
6. @After -> lock
7. @Around 后半段收尾 -> lock
主流程捕获异常: 账号锁定失败: 用户状态异常 -> bob
```

从这里你能直接看出来：

- `@AfterThrowing` 只在异常时出现
- `@AfterReturning` 没有出现
- `@After` 依然出现了

## 这节最重要的两句话

### 正常返回时

可以先粗略记成：

`@Around 前半段 -> @Before -> 目标方法 -> @AfterReturning -> @After -> @Around 后半段`

### 抛异常时

可以先粗略记成：

`@Around 前半段 -> @Before -> 目标方法 -> @AfterThrowing -> @After -> @Around 后半段`

## 这一节最容易错的地方

### `@After` 不是成功后

它是：

`结束后`

不管成功还是异常，只要方法结束了，它就会执行。

### `@AfterReturning` 和 `@AfterThrowing` 是互斥的

- 成功才会走 `@AfterReturning`
- 异常才会走 `@AfterThrowing`

它们不会同时出现。

## 自测

看完本节后，试着自己回答：

1. 为什么异常路径里没有 `@AfterReturning`
2. 为什么异常路径里还有 `@After`
3. 为什么 `@Around` 总像是在最外层

如果你能把这 3 个问题讲清楚，`005` 就过关了。
