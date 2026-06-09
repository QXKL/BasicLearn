# 第 008 节: JDK 动态代理 vs CGLIB

## 这一节的目标

学完这节，你要能回答 3 个问题：

1. 一个类有接口时，Spring 常怎么代理
2. 一个类没接口时，Spring 常怎么代理
3. `proxyTargetClass` 改变的到底是什么

## 本节代码在哪

这一节只看本目录下这些文件：

- `PayService.java`
- `PayServiceImpl.java`
- `CouponService.java`
- `ProxyCompareAspect.java`
- `JdkProxyApplication.java`
- `CglibProxyApplication.java`
- `ProxyTypeRunner.java`
- `README.md`

## 先看两种服务

### 第一种：有接口的服务

[PayService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/PayService.java)

```java
public interface PayService {
    String pay(String orderId);
}
```

[PayServiceImpl.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/PayServiceImpl.java)

```java
@Service
public class PayServiceImpl implements PayService {
    ...
}
```

这是“有接口”的典型情况。

### 第二种：没有接口的服务

[CouponService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/CouponService.java)

```java
@Service
public class CouponService {
    public String sendCoupon(String username) { ... }
}
```

这是“普通类，没有接口”的情况。

## JDK 动态代理是什么

你先记一个最够用的版本：

`JDK 动态代理基于接口工作。`

也就是说：

- 它更适合代理“实现了接口”的对象
- 生成出来的代理对象，本质上是“实现同一接口的代理类”

所以如果你的服务类有接口，Spring 很常会优先用 JDK 动态代理。

## CGLIB 是什么

也先记最够用的版本：

`CGLIB 通过继承目标类来生成子类代理。`

所以它的特点是：

- 不依赖接口
- 更适合代理普通类

但它也会带来一个限制：

- 如果类是 `final`
- 或方法是 `final`

那通常就不方便这样代理

因为子类代理需要“继承”和“重写”。

## 这一节怎么观察两种代理

### 配置 1：尽量走 JDK 代理

[JdkProxyApplication.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/JdkProxyApplication.java)

```java
@EnableAspectJAutoProxy(proxyTargetClass = false)
```

这表示：

`不要强制使用 CGLIB。`

于是：

- 有接口的 `PayServiceImpl` 更可能走 JDK 代理
- 没接口的 `CouponService` 仍然只能走 CGLIB

### 配置 2：强制走 CGLIB

[CglibProxyApplication.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/CglibProxyApplication.java)

```java
@EnableAspectJAutoProxy(proxyTargetClass = true)
```

这表示：

`强制使用基于类的代理方式。`

于是：

- `PayServiceImpl` 即使有接口，也会倾向走 CGLIB
- `CouponService` 也还是 CGLIB

## 切面里打印了什么

[ProxyCompareAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/ProxyCompareAspect.java)

```java
System.out.println("[AOP] target 类型: " + joinPoint.getTarget().getClass().getName());
System.out.println("[AOP] proxy 类型: " + joinPoint.getThis().getClass().getName());
```

这两行的作用是：

`把目标对象类型和代理对象类型直接打印出来`

这样你就能看到：

- 目标对象原本是谁
- 外面包着的代理是谁

## Runner 里还额外打印了代理判断

[ProxyTypeRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_008_jdk_proxy_vs_cglib/ProxyTypeRunner.java)

```java
System.out.println("[接口服务] 是否 JDK 代理: " + AopUtils.isJdkDynamicProxy(payService));
System.out.println("[接口服务] 是否 CGLIB 代理: " + AopUtils.isCglibProxy(payService));
```

```java
System.out.println("[普通类服务] 是否 JDK 代理: " + AopUtils.isJdkDynamicProxy(couponService));
System.out.println("[普通类服务] 是否 CGLIB 代理: " + AopUtils.isCglibProxy(couponService));
```

所以这节你不是靠猜，而是直接看运行结果。

## 你应该如何理解结果

### 当 `proxyTargetClass = false`

通常你会看到：

- `PayService` 这类“有接口”的服务，更容易是 JDK 代理
- `CouponService` 这类“没接口”的服务，仍然是 CGLIB 代理

### 当 `proxyTargetClass = true`

通常你会看到：

- `PayService` 也变成 CGLIB 代理
- `CouponService` 还是 CGLIB 代理

## 这一节最重要的结论

先记这两条就够了：

1. `有接口` 时，Spring 常常可以用 JDK 动态代理
2. `没接口` 时，Spring 往往只能用 CGLIB

再加一条：

3. `proxyTargetClass = true` 常用来强制走 CGLIB

## 这一节先别过度担心的地方

很多初学者会问：

“那我到底该永远用接口，还是永远不用接口？”

这节先不用把问题想那么远。

你当前最重要的是先把代理机制分开：

- JDK 代理靠接口
- CGLIB 靠继承

## 自测

看完这节后，试着自己回答：

1. 为什么 `PayServiceImpl` 可能走 JDK 代理
2. 为什么 `CouponService` 不能靠 JDK 代理
3. `proxyTargetClass = true` 改变了什么

如果这 3 个问题你能说清楚，`008` 就过关了。
