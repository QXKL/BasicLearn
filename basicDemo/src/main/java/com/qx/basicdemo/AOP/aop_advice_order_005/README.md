# 第 005 节: 通知执行顺序与异常

## 正常返回时

一个方法正常执行时, 你通常会看到类似顺序:

1. `@Around` 前半段
2. `@Before`
3. 目标方法
4. `@AfterReturning`
5. `@After`
6. `@Around` 后半段

## 抛异常时

如果目标方法抛异常, 常见顺序会变成:

1. `@Around` 前半段
2. `@Before`
3. 目标方法
4. `@AfterThrowing`
5. `@After`
6. `@Around` 后半段

## 两个容易混的点

`@After` 不是“成功后”, 而是“结束后”。

`@AfterReturning` 和 `@AfterThrowing` 是互斥的:

- 成功才会进 `@AfterReturning`
- 异常才会进 `@AfterThrowing`

## 这一节意义

只有搞清楚顺序, 你写日志、事务、异常处理时才不会判断错位置。
