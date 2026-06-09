# 第 004 节: 切点表达式 execution

## 先看现在项目里的表达式

```java
execution(* com.qx.basicdemo.AOP.service.*.*(..))
```

## 先拆开理解

- 第 1 个 `*`: 返回值任意
- `com.qx.basicdemo.AOP.service`: 指定包
- 第 2 个 `*`: 该包下任意类
- 第 3 个 `*`: 任意方法名
- `(..)`: 任意参数列表

## 粗略翻译

拦截 `service` 包下所有类的所有方法。

## 常见变体

```java
execution(public * com.qx.basicdemo.AOP.service.*.*(..))
```

只拦截 `public` 方法。

```java
import com.qx.basicdemo.AOP.aop_quick_start_000.OrderService;

execution(*OrderService.createOrder(..))
```

只拦截 `OrderService` 里的 `createOrder` 方法。

```java
execution(* com.qx.basicdemo.AOP.service.*.create*(..))
```

拦截方法名以 `create` 开头的方法。

## 这一节目标

不用一次全背下来。

你只要先能看懂现在这条表达式在拦谁, 就已经很好了。
