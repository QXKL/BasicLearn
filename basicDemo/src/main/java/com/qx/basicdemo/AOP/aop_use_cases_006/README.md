# 第 006 节: AOP 常见应用场景与注意事项

## 这一节的目标

学完这节，你要能回答 3 个问题：

1. AOP 适合拿来做什么
2. 什么场景不必硬上 AOP
3. 初学时最容易踩哪些坑

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `ReportService.java`
- `UseCaseAspect.java`
- `UseCaseRunner.java`
- `README.md`

## 本节演示了 3 个常见场景

### 场景 1：日志记录

[UseCaseAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_use_cases_006/UseCaseAspect.java)

```java
@Around("reportMethods()")
public Object logAndMeasure(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("[场景1-日志] 准备执行: " + joinPoint.getSignature().getName());
    System.out.println("[场景1-日志] 参数: " + Arrays.toString(joinPoint.getArgs()));
    ...
}
```

这段代码的价值是：

`不用把日志代码写进每个业务方法里，也能统一打印方法名和参数。`

这正是 AOP 最经典的用途之一。

### 场景 2：耗时统计

还是同一个 `@Around`：

```java
long start = System.currentTimeMillis();
...
long duration = System.currentTimeMillis() - start;
System.out.println("[场景2-耗时] ...");
```

这段代码说明：

`统计耗时` 也非常适合 AOP。

因为它往往会在很多方法里重复出现。

### 场景 3：权限检查

```java
@Before("deleteReportMethod()")
public void checkPermission(JoinPoint joinPoint) {
    String operator = String.valueOf(joinPoint.getArgs()[0]);
    if (operator.startsWith("guest")) {
        throw new IllegalArgumentException("无权限删除报表: " + operator);
    }
}
```

这里演示的是一个简化版权限校验。

意思是：

`deleteReport(...)` 在真正执行前，先做一次权限判断。`

如果没有权限，就直接拦下来。

## 本节的业务类

[ReportService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_use_cases_006/ReportService.java)

```java
public class ReportService {

    public String generateReport(String reportName) { ... }

    public void deleteReport(String operator, String reportName) { ... }
}
```

这里故意设计了两类方法：

- `generateReport(...)`：用来观察日志和耗时
- `deleteReport(...)`：用来观察权限检查

## 运行后你会看到什么

[UseCaseRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_use_cases_006/UseCaseRunner.java)

它会依次做 3 件事：

1. 生成报表
2. 让管理员删除报表
3. 让访客删除报表

大致会看到这样的效果：

```text
[场景1-日志] 准备执行: generateReport
[场景1-日志] 参数: [sales-2026]
执行业务方法: generateReport -> sales-2026
[场景1-日志] 执行成功: generateReport
[场景2-耗时] generateReport 耗时: ...

[场景1-日志] 准备执行: deleteReport
[场景1-日志] 参数: [admin_zhang, sales-2026]
[场景3-权限] 权限校验通过: admin_zhang
执行业务方法: deleteReport -> sales-2026
[场景1-日志] 执行成功: deleteReport
[场景2-耗时] deleteReport 耗时: ...

[场景1-日志] 准备执行: deleteReport
[场景1-日志] 参数: [guest_li, audit-2026]
主流程捕获异常: 无权限删除报表: guest_li
```

你会发现：

- 日志和耗时统计适合统一做
- 权限检查也适合在方法前统一拦截
- 这些逻辑都不是“报表业务本身”，但很多方法都可能要用

## 什么场景适合用 AOP

当一段逻辑同时满足下面两个条件时，通常就很适合：

1. 会在很多方法里重复出现
2. 它不是业务本身的核心目标

典型例子就是：

- 日志
- 权限
- 事务
- 耗时统计
- 异常记录

## 什么场景不必硬用 AOP

如果一段逻辑：

- 只服务于一个方法
- 和这个方法的业务含义强绑定

那直接写在业务方法里，往往更清晰。

AOP 不是为了“显得高级”，而是为了减少重复和提高可维护性。

## 初学者最常见的坑

### 1. 以为加了切面注解就一定生效

不是所有类、所有调用方式都会自动被切到。

你至少要保证：

- 类被 Spring 管理
- 切点确实匹配到了目标方法

### 2. 切点写偏了，结果拦错方法

例如你本来只想拦 `deleteReport(...)`，
结果切点写太宽，把整个类全拦了。

这类问题在 AOP 里很常见。

### 3. `@Around` 里忘记 `proceed()`

如果你写了：

```java
@Around(...)
public Object demo(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("before");
    return null;
}
```

那目标方法根本不会执行。

所以要特别记住：

`@Around` 想继续调用目标方法，就必须执行 `joinPoint.proceed()`。`

### 4. 把 `@After` 当成“成功后”

它不是成功后，它是：

`结束后`

成功和异常都会进。

## 这一节最后的判断标准

如果现在给你一段公共逻辑，比如：

- 每个接口都要记录访问日志
- 每个服务方法都要统计耗时
- 删除操作都要做权限校验

你能马上判断：

`这类逻辑适合交给 AOP`

那这一节就已经达标了。

## 小结

这一节最后只留一句最重要的话：

`AOP 最适合处理“很多地方都重复出现、但又不是业务核心”的公共逻辑。`
