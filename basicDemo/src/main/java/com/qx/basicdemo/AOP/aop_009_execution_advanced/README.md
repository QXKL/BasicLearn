# 第 009 节: `execution` 进阶

## 这一节的目标

学完这节，你不只是“能看懂” `execution(...)`。

你要进一步做到两件事：

1. 能把一条 `execution(...)` 按维度拆开
2. 能预判它到底会命中哪些方法，不会命中哪些方法

这一节我们把 `execution(...)` 真正拆开讲。

## 本节代码在哪

这一节只看本目录下 5 个文件：

- `AOPApplication.java`
- `InventoryService.java`
- `ExecutionAdvancedAspect.java`
- `ExecutionAdvancedRunner.java`
- `README.md`

## 先看本节的业务类

[InventoryService.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_009_execution_advanced/InventoryService.java)

```java
public class InventoryService {

    public String getStock(String sku) { ... }

    public String getStock(String sku, int warehouseId) { ... }

    public void updateStock(String sku, int count) { ... }

    public boolean deleteStock(String sku) { ... }

    public int countAll() { ... }
}
```

这一节故意把方法设计得“差异很明显”，因为我们要观察这些维度：

- 返回值类型不同
- 方法名不同
- 参数个数不同
- 参数类型不同

只有这样，`execution(...)` 的匹配规则才会真正显出来。

## 一、`execution(...)` 的基本骨架

你可以先把它记成一个模板：

```java
execution(修饰符 返回值 包名.类名.方法名(参数))
```

注意这只是帮助理解的骨架，不是说每一部分都必须写死。

很多位置都可以用通配符。

例如：

```java
execution(public * com.demo.service.UserService.*(..))
```

它可以拆成：

- `public`：只匹配 `public` 方法
- `*`：返回值任意
- `com.demo.service.UserService`：指定类
- `*`：方法名任意
- `(..)`：参数任意

这就是 `execution(...)` 的阅读方式。

## 二、按“修饰符”匹配

先看这一条：

```java
@Pointcut("execution(public * com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(..))")
```

它的重点是最前面的 `public`。

这表示：

`只匹配 public 方法`

在本节代码里，`InventoryService` 的几个方法都是 `public`，
所以它们都会命中这条规则。

这条规则的真正价值不在于“好像很宽”，
而在于你开始知道：

`execution(...)` 可以把访问修饰符也纳入匹配条件。`

## 三、按“返回值类型”匹配

再看这一条：

```java
@Pointcut("execution(String com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(..))")
```

这表示：

`只匹配返回值类型为 String 的方法`

所以在本节代码里：

- `getStock(String)` 会命中
- `getStock(String, int)` 会命中
- `updateStock(String, int)` 不会命中，因为它返回 `void`
- `deleteStock(String)` 不会命中，因为它返回 `boolean`
- `countAll()` 不会命中，因为它返回 `int`

这一类规则非常重要，因为它说明：

`execution(...)` 不是只看方法名，它也看方法签名的一部分。`

## 四、按“方法名前缀”匹配

再看：

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.get*(..))")
```

它的关键在 `get*`。

这表示：

`只匹配方法名以 get 开头的方法`

所以：

- `getStock(String)` 命中
- `getStock(String, int)` 命中
- `updateStock(...)` 不命中
- `deleteStock(...)` 不命中

这一条你要建立一个很强的直觉：

`方法名前缀匹配在实战里非常常见。`

例如：

- `create*`
- `query*`
- `delete*`
- `save*`

这些都很常用。

## 五、按“参数列表”精确匹配

### 1. 匹配单个 `String` 参数

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(String))")
```

它表示：

`只匹配参数列表恰好是一个 String 的方法`

所以：

- `getStock(String)` 命中
- `deleteStock(String)` 命中
- `getStock(String, int)` 不命中
- `updateStock(String, int)` 不命中
- `countAll()` 不命中

这里有个很容易忽略的点：

`(String)` 不是“包含一个 String 参数”。

它是：

`参数列表恰好只有一个 String`

这点很关键。

### 2. 匹配 `(String, int)` 参数

```java
@Pointcut("execution(* com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.*(String, int))")
```

它表示：

`只匹配参数列表恰好是 (String, int) 的方法`

所以：

- `getStock(String, int)` 命中
- `updateStock(String, int)` 命中
- `getStock(String)` 不命中
- `deleteStock(String)` 不命中
- `countAll()` 不命中

这说明一件事：

`execution(...)` 对参数列表的匹配是非常严格的。`

## 六、多个条件叠加匹配

这一条是本节最值得你盯住的：

```java
@Pointcut("execution(boolean com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.delete*(..))")
```

这条规则不是只看一个维度，
而是同时看了两个维度：

- 返回值必须是 `boolean`
- 方法名必须以 `delete` 开头

于是它在本节里只会匹配：

- `deleteStock(String)`

这一点非常重要，因为真实项目里我们常常不是靠单一条件筛方法，
而是靠“多个条件一起收窄范围”。

也就是说：

`execution(...)` 的真正威力，不只是会写通配符，而是会组合约束。`

## 七、Runner 会怎样触发这些规则

[ExecutionAdvancedRunner.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_009_execution_advanced/ExecutionAdvancedRunner.java)

它会依次调用：

```java
inventoryService.getStock("SKU-100");
inventoryService.getStock("SKU-200", 3);
inventoryService.updateStock("SKU-300", 10);
inventoryService.deleteStock("SKU-400");
inventoryService.countAll();
```

而 [ExecutionAdvancedAspect.java](/E:/JavaProjects/BasicDemo/basicDemo/src/main/java/com/qx/basicdemo/AOP/aop_009_execution_advanced/ExecutionAdvancedAspect.java)
会给每一类匹配都打印一个标签。

所以你运行后看到的不是“有没有进切面”，
而是“到底是哪一条规则命中了这个方法”。

## 八、你应该如何读运行结果

举个例子：

如果调用的是：

```java
inventoryService.getStock("SKU-100");
```

它会同时满足：

- `public`
- 返回值 `String`
- 方法名 `get*`
- 参数 `(String)`

所以你会看到它被多条规则一起命中。

这恰恰说明：

`一个方法可以同时匹配多条 execution 规则。`

再比如：

```java
inventoryService.updateStock("SKU-300", 10);
```

它会满足：

- `public`
- 参数 `(String, int)`

但不会满足：

- 返回值 `String`
- 方法名前缀 `get*`
- 返回值 `boolean` 且方法名 `delete*`

这种“逐条排除”的能力，才是这节真正想训练你的地方。

## 九、`*`、`..`、精确签名，分别意味着什么

### `*`

通常表示：

`任意一个`

例如：

- 返回值任意
- 方法名任意

### `(..)`

表示：

`任意参数列表`

它比 `(*)` 宽得多。

`(..)` 可以匹配：

- 无参数
- 一个参数
- 多个参数

而 `(String)` 只匹配一个 `String` 参数的方法。

### 精确签名

例如：

```java
execution(* ..InventoryService.*(String, int))
```

这类写法的价值在于：

`把匹配范围明确限制到某种方法签名`

这种规则在“精细增强”时非常有用。

## 十、这节最容易犯的 4 个错

### 1. 把 `(String)` 理解成“只要有 String 参数就行”

不是。

它表示：

`参数列表恰好只有一个 String`

### 2. 把 `(..)` 和 `(*)` 混掉

- `(..)`：任意参数列表
- `(*)`：恰好一个参数，但类型任意

它们不是一回事。

### 3. 只看方法名，不看返回值

很多人写：

```java
execution(* ..delete*(..))
```

结果把所有删除相关方法全拦了。

如果你真正只想拦“返回 `boolean` 的删除方法”，
就要像本节一样把返回值也写进去。

### 4. 以为 `execution(...)` 匹配的是运行时参数值

不是。

它主要描述的是：

`方法签名结构`

也就是声明层面的类型、名字、参数形式。

至于“参数的实际值是什么”，那不是 `execution(...)` 负责的。

## 十一、这一节真正要形成的能力

学到这里，你应该能做两件事。

### 第一件事：从表达式反推命中方法

例如看到：

```java
execution(String ..InventoryService.get*(..))
```

你应该能马上反应：

`这是在拦返回值为 String、方法名以 get 开头的方法`

### 第二件事：从目标需求反写表达式

例如别人说：

“我只想拦 `InventoryService` 里返回 `boolean`、并且方法名以 `delete` 开头的方法。”

你应该能写出：

```java
execution(boolean com.qx.basicdemo.AOP.aop_009_execution_advanced.InventoryService.delete*(..))
```

这就是从“看懂”进阶到“能设计”的分界线。

## 小结

这一节最后只留一句最关键的话：

`execution(...) 的本质，是用一组对方法签名的约束条件来筛选目标方法。`

一旦你把“返回值、方法名、参数列表、修饰符”这些维度真正分开看，
`execution(...)` 就不会再像一整串咒语了。
