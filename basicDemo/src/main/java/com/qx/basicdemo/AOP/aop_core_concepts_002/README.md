# 第 002 节: AOP 的 3 个核心概念

## 1. 切面 Aspect

切面就是“专门放增强逻辑的类”。

你项目里的 `LogAspect` 就是切面。

它不负责下单, 不负责取消订单。

它负责:

- 方法执行前打印日志
- 方法执行后打印日志
- 抛异常时记录异常

## 2. 切点 Pointcut

切点就是“到底拦哪些方法”。

例如:

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.service.*.*(..))")
```

这句话的意思可以先粗暴理解成:

拦截 `service` 包里的所有方法。

## 3. 通知 Advice

通知就是“拦到方法之后, 你具体要做什么”。

例如:

- `@Before`: 方法执行前做什么
- `@After`: 方法结束后做什么
- `@AfterReturning`: 方法正常返回后做什么
- `@AfterThrowing`: 方法抛异常后做什么
- `@Around`: 前后都能包起来控制

## 先形成一张脑图

- Aspect: 在哪里写增强逻辑
- Pointcut: 拦谁
- Advice: 拦到以后做什么

## 下一节要学

5 种通知分别在什么时候触发。
