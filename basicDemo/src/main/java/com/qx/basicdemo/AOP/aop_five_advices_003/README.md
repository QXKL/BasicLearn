# 第 003 节: 5 种常见通知

## 这一节的目标

学完这节，你要能分清这 5 种通知分别在什么时候触发：

- `@Before`
- `@After`
- `@AfterReturning`
- `@AfterThrowing`
- `@Around`

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `PaymentService.java`
- `AdviceDemoAspect.java`
- `AdviceDemoRunner.java`
- `README.md`

## 本节的业务类

[PaymentService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_five_advices_003/PaymentService.java)

```java
@Service
public class PaymentService {

    public String pay(String orderId) {
        System.out.println("执行业务方法: 支付订单 -> " + orderId);
        return "支付成功: " + orderId;
    }

    public void refund(String orderId) {
        System.out.println("执行业务方法: 退款订单 -> " + orderId);
        throw new IllegalStateException("退款失败: 订单状态不允许退款 -> " + orderId);
    }
}
```

这里故意准备了两种情况：

- `pay(...)`：正常返回
- `refund(...)`：主动抛异常

这样 5 种通知的触发时机就能一次看全。

## 本节的切面类

[AdviceDemoAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_five_advices_003/AdviceDemoAspect.java)

这一个类里把 5 种通知都写全了。

### 1. `@Before`

```java
@Before("paymentMethods()")
public void beforeAdvice(JoinPoint joinPoint) {
    System.out.println("[@Before] 方法执行前: " + joinPoint.getSignature().getName());
}
```

含义：

`目标方法执行前触发`

所以它适合做：

- 打印参数
- 权限校验
- 调用前提示

### 2. `@After`

```java
@After("paymentMethods()")
public void afterAdvice(JoinPoint joinPoint) {
    System.out.println("[@After] 方法结束后: " + joinPoint.getSignature().getName());
}
```

含义：

`目标方法结束后触发`

最关键的一点：

`不管方法成功还是异常，它都会执行`

### 3. `@AfterReturning`

```java
@AfterReturning(pointcut = "paymentMethods()", returning = "result")
public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    System.out.println("[@AfterReturning] 正常返回后: " + joinPoint.getSignature().getName());
}
```

含义：

`只有方法正常返回时才触发`

如果方法抛异常，它不会执行。

### 4. `@AfterThrowing`

```java
@AfterThrowing(pointcut = "paymentMethods()", throwing = "exception")
public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
    System.out.println("[@AfterThrowing] 抛异常后: " + joinPoint.getSignature().getName());
}
```

含义：

`只有方法抛异常时才触发`

如果方法顺利返回，它不会执行。

### 5. `@Around`

```java
@Around("paymentMethods()")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("[@Around] 前半段开始: " + joinPoint.getSignature().getName());
    try {
        Object result = joinPoint.proceed();
        System.out.println("[@Around] 正常结束: " + joinPoint.getSignature().getName());
        return result;
    } finally {
        System.out.println("[@Around] 后半段收尾: " + joinPoint.getSignature().getName());
    }
}
```

含义：

`它能把整个目标方法包起来`

所以它既能在前面做事，也能在后面做事。

它也是能力最强的通知。

## 本节怎么触发示例

[AdviceDemoRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_five_advices_003/AdviceDemoRunner.java)

它会依次调用：

```java
paymentService.pay("ORDER-2001");
paymentService.refund("ORDER-2002");
```

第一个方法用来观察“成功路径”。

第二个方法用来观察“异常路径”。

## 成功路径会看到什么

调用：

```java
paymentService.pay("ORDER-2001");
```

大致会看到这样的顺序：

```text
[@Around] 前半段开始: pay
[@Before] 方法执行前: pay
执行业务方法: 支付订单 -> ORDER-2001
[@AfterReturning] 正常返回后: pay
[@AfterReturning] 返回值: 支付成功: ORDER-2001
[@After] 方法结束后: pay
[@Around] 正常结束: pay
[@Around] 后半段收尾: pay
```

从这里你能看出来：

- `@Before` 在业务方法前
- `@AfterReturning` 只在成功时出现
- `@After` 在结束后出现
- `@Around` 把整个过程包住了

## 异常路径会看到什么

调用：

```java
paymentService.refund("ORDER-2002");
```

大致会看到这样的顺序：

```text
[@Around] 前半段开始: refund
[@Before] 方法执行前: refund
执行业务方法: 退款订单 -> ORDER-2002
[@AfterThrowing] 抛异常后: refund
[@AfterThrowing] 异常信息: 退款失败: 订单状态不允许退款 -> ORDER-2002
[@After] 方法结束后: refund
[@Around] 后半段收尾: refund
主流程捕获异常: 退款失败: 订单状态不允许退款 -> ORDER-2002
```

从这里你能看出来：

- `@AfterThrowing` 只在异常时出现
- `@After` 依然会执行
- `@AfterReturning` 不会出现

## 这一节最容易混的地方

### `@After` 和 `@AfterReturning`

区别只有一句话：

- `@After`：不管成功还是异常，只要结束就执行
- `@AfterReturning`：只有成功返回才执行

### `@AfterReturning` 和 `@AfterThrowing`

它们是互斥的：

- 成功走 `@AfterReturning`
- 异常走 `@AfterThrowing`

## 快速记忆版

只留最短的一版就是：

- `@Before`：前
- `@After`：后
- `@AfterReturning`：成功后
- `@AfterThrowing`：异常后
- `@Around`：全包

## 自测

看完这节代码后，试着回答：

1. 哪个通知一定会在方法前面执行
2. 哪个通知不管成功还是异常都会执行
3. 哪个通知只在成功时出现
4. 哪个通知只在异常时出现
5. 哪个通知能包住整个调用过程

如果你能直接答出来，`003` 就过关了。
