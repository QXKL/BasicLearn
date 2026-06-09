# Java 异常教学

## 知识点总览

**一句话定义：** Java 异常是程序运行时发生的意外事件，异常机制让你能将"错误处理逻辑"从"正常业务逻辑"中分离出来。

**教学范围：** 异常体系、try-catch-finally、checked/unchecked、throw/throws、自定义异常、try-with-resources

---

## 目录

- [第 1 章：异常基本概念](#第-1-章异常基本概念)
- [第 2 章：try-catch-finally](#第-2-章try-catch-finally)
- [第 3 章：checked vs unchecked](#第-3-章checked-vs-unchecked)
- [第 4 章：throw / throws](#第-4-章throw--throws)
- [第 5 章：自定义异常](#第-5-章自定义异常)
- [第 6 章：try-with-resources](#第-6-章try-with-resources)
- [第 7 章：综合练习](#第-7-章综合练习)

---

# 第 1 章：异常基本概念

## 1.1 什么是异常

程序运行时，有些情况"不在计划内"：读一个不存在的文件、数组下标越界、网络断开。这些情况叫**异常（Exception）**。

Java 用一个**对象**来表示异常——当错误发生时，JVM 创建一个异常对象，里面包含错误类型、错误信息、发生位置（调用栈）。

**示例：**

```java
int[] arr = new int[3];
System.out.println(arr[5]); // 抛出 ArrayIndexOutOfBoundsException
```

输出：
```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3
    at Main.main(Main.java:3)
```

**例题：**

**题1：** 下面哪行会抛出异常？输出什么？
```java
String s = null;
System.out.println(s.length());
```

<details>
<summary>答案</summary>

第2行抛出 `NullPointerException`，因为对 null 引用调用方法。
</details>

**题2：** 写一段代码，故意触发 `ArithmeticException`。

<details>
<summary>答案</summary>

```java
int result = 10 / 0;
```
</details>

**题3：** 下面代码会不会抛异常？
```java
double result = 10.0 / 0;
System.out.println(result);
```

<details>
<summary>答案</summary>

不会。浮点数除以零结果是 `Infinity`，Java 对浮点数有特殊处理。
</details>

---

## 1.2 异常 vs 普通错误返回值

在没有异常机制的语言（如 C）中，错误通常用返回值表示（返回 -1 代表失败）。调用者很容易忘记检查返回值，错误被静默忽略。

Java 的异常机制强制你要么处理，要么声明——**错误不会被静默忽略**。

```java
// C风格（容易忽略）
int code = doSomething();
if (code == -1) { /* 很多人忘了写这里 */ }

// Java风格（checked异常强制处理）
try {
    doSomething();
} catch (IOException e) {
    // 必须面对
}
```

**例题：**

**题1：** 为什么 Java 不用返回 -1 来表示"文件未找到"，而用抛异常？

<details>
<summary>答案</summary>

返回值方式下，调用者可以忽略返回值继续执行，导致后续代码在错误状态下运行，产生难以排查的 bug。异常会中断当前执行流，强制调用栈上的某一层处理它。
</details>

---

## 1.3 异常体系的顶层结构

```
Throwable
├── Error         // JVM级别严重错误，程序无法恢复
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception     // 程序可处理的异常
    ├── RuntimeException       // unchecked（运行时异常）
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   ├── ClassCastException
    │   └── ...
    └── 其他Exception          // checked（受检异常）
        ├── IOException
        ├── SQLException
        └── ...
```

**关键记忆点：**
- `Error`：不要捕获，程序已经无法恢复
- `RuntimeException` 及其子类：unchecked，编译器不强制处理
- 其余 `Exception` 子类：checked，编译器强制处理

**例题：**

**题1：** `NullPointerException` 属于哪一类？你需要在代码里强制处理它吗？

<details>
<summary>答案</summary>

属于 `RuntimeException`，是 unchecked 异常，编译器不强制处理。
</details>

**题2：** `IOException` 属于哪一类？

<details>
<summary>答案</summary>

直接继承 `Exception`，是 checked 异常，编译器强制你 try-catch 或 throws 声明。
</details>

**题3：** 能用 `catch (Throwable t)` 捕获 `OutOfMemoryError` 吗？

<details>
<summary>答案</summary>

语法上可以，但强烈不建议。`Error` 表示 JVM 级别严重故障，捕获后程序状态不可信，通常应让程序崩溃而非假装恢复。
</details>

---

## 1.4 异常发生时的默认行为（栈展开）

当异常被抛出且没有被任何 catch 捕获时，JVM 会**沿调用栈向上传播**，直到找到合适的 catch 或到达 main 方法——此时程序终止并打印栈轨迹。

```java
public class Main {
    public static void c() {
        int[] arr = new int[1];
        arr[5] = 10;
    }
    public static void b() { c(); }
    public static void a() { b(); }

    public static void main(String[] args) {
        a();
    }
}
```

输出：
```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 1
    at Main.c(Main.java:4)
    at Main.b(Main.java:7)
    at Main.a(Main.java:8)
    at Main.main(Main.java:11)
```

栈轨迹从下往上读：`main → a → b → c`，异常在 `c` 里抛出。

**例题：**

**题1：** 下面代码，如果 `foo()` 的调用者也没有 catch，最终发生什么？
```java
void foo() { bar(); }
void bar() { throw new RuntimeException("error"); }
```

<details>
<summary>答案</summary>

异常从 `bar()` 抛出，经过 `foo()`，传到调用者，若一直没有 catch，最终到达 JVM，程序打印 stack trace 并终止。
</details>

---

# 第 2 章：try-catch-finally

## 2.1 try 块

`try` 块标记"可能出错的代码区域"。一旦 try 块内某行抛出异常，**该行之后的代码不再执行**，控制权立即转移到对应的 catch 块。

```java
try {
    System.out.println("第1行");
    int x = 10 / 0;
    System.out.println("第2行"); // 不会执行
} catch (ArithmeticException e) {
    System.out.println("捕获到异常：" + e.getMessage());
}
```

输出：
```
第1行
捕获到异常：/ by zero
```

**例题：**

**题1：** 下面代码输出什么？
```java
try {
    System.out.println("A");
    String s = null;
    System.out.println(s.length());
    System.out.println("B");
} catch (NullPointerException e) {
    System.out.println("C");
}
System.out.println("D");
```

<details>
<summary>答案</summary>

```
A
C
D
```
B 不会输出（异常发生后 try 块剩余代码跳过）。
</details>

---

## 2.2 catch 块与异常类型匹配

catch 后面声明你想捕获的异常类型。JVM 检查抛出的异常是否是该类型**或其子类**，是则进入，否则继续向上传播。

```java
try {
    int[] arr = new int[3];
    arr[10] = 1;
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("数组越界：" + e.getMessage());
}
```

常用方法：
- `e.getMessage()` — 错误信息字符串
- `e.printStackTrace()` — 打印完整栈轨迹

**例题：**

**题1：** 下面能捕获到异常吗？
```java
try {
    throw new NullPointerException();
} catch (RuntimeException e) {
    System.out.println("caught");
}
```

<details>
<summary>答案</summary>

能。`NullPointerException` 是 `RuntimeException` 的子类，catch 匹配父类即可捕获。
</details>

**题2：** 下面能捕获到吗？
```java
try {
    throw new RuntimeException();
} catch (NullPointerException e) {
    System.out.println("caught");
}
```

<details>
<summary>答案</summary>

不能。`RuntimeException` 不是 `NullPointerException` 的子类，匹配失败，异常继续向上传播。
</details>

---

## 2.3 多个 catch 的顺序规则

JVM **从上到下**依次尝试匹配，匹配到第一个就进入，后面的全部跳过。**子类异常必须写在父类前面。**

```java
try {
    int[] arr = new int[3];
    arr[10] = 1;
} catch (ArrayIndexOutOfBoundsException e) { // 子类，先写
    System.out.println("数组越界");
} catch (RuntimeException e) {               // 父类，后写
    System.out.println("运行时异常");
} catch (Exception e) {
    System.out.println("通用异常");
}
```

**例题：**

**题1：** 下面代码能编译通过吗？
```java
try {
    throw new NullPointerException();
} catch (Exception e) {
    System.out.println("A");
} catch (NullPointerException e) {
    System.out.println("B");
}
```

<details>
<summary>答案</summary>

编译报错。`NullPointerException` 是 `Exception` 的子类，前面的 catch 已经能捕获它，后面的 catch 永远不可达。
</details>

**题2：** 下面输出什么？
```java
try {
    throw new NullPointerException();
} catch (NullPointerException e) {
    System.out.println("A");
} catch (RuntimeException e) {
    System.out.println("B");
}
```

<details>
<summary>答案</summary>

输出 `A`。第一个 catch 匹配成功，第二个跳过。
</details>

---

## 2.4 finally 块

`finally` 块里的代码**无论是否发生异常都会执行**，常用于释放资源。

```java
try {
    System.out.println("try");
    int x = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("catch");
} finally {
    System.out.println("finally");
}
```

输出：
```
try
catch
finally
```

**例题：**

**题1：** 异常没有被 catch 捕获时，finally 还会执行吗？
```java
try {
    throw new RuntimeException();
} finally {
    System.out.println("finally");
}
```

<details>
<summary>答案</summary>

会执行。输出 `finally`，之后异常继续向上传播。
</details>

---

## 2.5 finally 与 return 的优先级陷阱

当 try 或 catch 里有 `return`，finally **仍然会先执行**。如果 finally 里也有 `return`，它会覆盖 try/catch 的 return 值。

```java
// finally 不含 return：正常返回 1
static int test() {
    try {
        return 1;
    } finally {
        System.out.println("finally执行了");
    }
}

// finally 含 return：覆盖，返回 2
static int test() {
    try {
        return 1;
    } finally {
        return 2;
    }
}
```

> **实践建议：** 永远不要在 finally 里写 return，会吞掉异常和原返回值。

**例题：**

**题1：** 下面方法返回什么？
```java
static int foo() {
    try {
        return 10;
    } finally {
        return 20;
    }
}
```

<details>
<summary>答案</summary>

返回 `20`。finally 的 return 覆盖了 try 的 return。
</details>

**题2：** 下面方法会抛出异常吗？返回什么？
```java
static int foo() {
    try {
        throw new RuntimeException();
    } finally {
        return 42;
    }
}
```

<details>
<summary>答案</summary>

不会抛出异常！返回 `42`。finally 里的 return 把异常吞掉了——这是 finally 里写 return 最危险的地方。
</details>

---

# 第 3 章：checked vs unchecked

## 3.1 两者的本质区别

| | checked | unchecked |
|--|--|--|
| 继承自 | `Exception`（非RuntimeException） | `RuntimeException` 或 `Error` |
| 编译器态度 | 不处理则编译失败 | 编译器不管 |
| 典型例子 | `IOException`、`SQLException` | `NullPointerException`、`ArrayIndexOutOfBoundsException` |
| 代表含义 | 外部环境可能失败（文件、网络） | 程序逻辑错误，本不该发生 |

```java
// checked：不处理编译报错
public void readFile() throws IOException {     // 方案B：throws 向上抛
    FileReader fr = new FileReader("a.txt");
}

public void readFile() {                        // 方案A：try-catch
    try {
        FileReader fr = new FileReader("a.txt");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

**例题：**

**题1：** `NumberFormatException` 是 checked 还是 unchecked？

<details>
<summary>答案</summary>

unchecked。继承自 `IllegalArgumentException` → `RuntimeException`，编译器不强制处理。
</details>

---

## 3.2 编译器如何强制 checked 异常

对于 checked 异常，编译器要求调用链上**每一层**要么 catch，要么用 throws 继续声明。

```java
void b() throws IOException {
    throw new IOException();
}

void a() {
    b(); // 编译错误！
}

// 修复方案A
void a() {
    try { b(); } catch (IOException e) { ... }
}

// 修复方案B
void a() throws IOException {
    b();
}
```

**例题：**

**题1：** 下面能编译通过吗？
```java
void foo() {
    throw new IOException();
}
```

<details>
<summary>答案</summary>

不能。必须加 `throws IOException`。
</details>

**题2：** 下面能编译通过吗？
```java
void foo() {
    throw new RuntimeException();
}
```

<details>
<summary>答案</summary>

能。`RuntimeException` 是 unchecked，不需要 throws 声明。
</details>

---

## 3.3 何时用哪种

**用 checked 异常**，当：失败原因来自外部环境，调用者应该有机会恢复。

**用 unchecked 异常**，当：失败是程序逻辑错误，调用者无法合理恢复，应该修 bug。

> 现代 Java 项目（Spring 等）普遍偏向 unchecked 异常，避免 checked 异常导致的"异常污染"。

**例题：**

**题1：** 写一个"用户登录"方法，用户名不存在时应该抛 checked 还是 unchecked？

<details>
<summary>答案</summary>

看场景。现代项目更倾向自定义 unchecked（继承 `RuntimeException` 的 `UserNotFoundException`）。如果"用户名不存在"属于程序 bug，用 unchecked；如果是正常业务情况且调用者必须处理，用 checked。
</details>

---

# 第 4 章：throw / throws

## 4.1 throw：主动抛出

`throw` 用于在代码中**主动抛出一个异常对象**。`throw` 之后的代码不再执行。

```java
void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("年龄不能为负数：" + age);
    }
    this.age = age;
}
```

**例题：**

**题1：** 下面能编译通过吗？
```java
void foo() {
    throw new RuntimeException();
    System.out.println("hello");
}
```

<details>
<summary>答案</summary>

编译失败。`throw` 后的代码是不可达代码，编译器报错。
</details>

---

## 4.2 throws：方法声明

`throws` 写在方法签名上，声明"这个方法可能抛出某种异常，调用者自己处理"。

```java
void readFile(String path) throws IOException {
    FileReader fr = new FileReader(path);
}
```

**例题：**

**题1：** throws 可以声明多个异常吗？

<details>
<summary>答案</summary>

```java
void foo() throws IOException, SQLException { ... }
```
</details>

**题2：** 方法声明了 `throws IOException`，调用者一定要 catch 它吗？

<details>
<summary>答案</summary>

不一定。调用者可以继续用 `throws IOException` 向上声明，把责任交给更上层。
</details>

---

## 4.3 两者的配合与区别

| | 位置 | 作用 |
|--|--|--|
| `throw` | 方法体内 | 实际抛出一个异常对象 |
| `throws` | 方法签名 | 声明此方法可能抛出某异常 |

```java
void validate(int score) throws IllegalArgumentException {
    if (score < 0 || score > 100) {
        throw new IllegalArgumentException("分数非法：" + score);
    }
}
```

**例题：**

**题1：** 有 `throw` 就一定要有 `throws` 吗？

<details>
<summary>答案</summary>

不一定。只有 throw 的是 checked 异常时，才必须配 throws 声明。throw unchecked 异常不需要 throws。
</details>

---

# 第 5 章：自定义异常

## 5.1 为什么要自定义

内置异常太通用，无法表达具体业务含义。自定义异常让代码更可读，也让调用者能精确 catch 业务错误。

```java
// 不好
throw new RuntimeException("余额不足");

// 好
throw new InsufficientBalanceException("余额不足，当前余额：" + balance);
```

---

## 5.2 继承哪个类

- 继承 `RuntimeException` → unchecked（推荐，现代项目主流）
- 继承 `Exception` → checked

```java
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

```java
void withdraw(double amount) {
    if (amount > balance) {
        throw new InsufficientBalanceException("余额不足，当前：" + balance);
    }
    balance -= amount;
}
```

**例题：**

**题1：** 定义一个 checked 异常 `UserNotFoundException`。

<details>
<summary>答案</summary>

```java
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```
</details>

**题2：** 下面两种自定义异常，调用者使用上有什么区别？
```java
class AException extends Exception { ... }
class BException extends RuntimeException { ... }
```

<details>
<summary>答案</summary>

调用抛出 `AException` 的方法时，必须 try-catch 或 throws 声明；调用抛出 `BException` 的方法时，编译器不强制处理。
</details>

---

## 5.3 异常链（cause）

把底层异常包装成业务异常时，用异常链保留原始异常，方便排查 bug。

```java
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

void loadUser(int id) {
    try {
        db.query("SELECT ...");
    } catch (SQLException e) {
        throw new DataAccessException("加载用户失败，id=" + id, e);
    }
}
```

打印时会显示：
```
DataAccessException: 加载用户失败，id=42
    at ...
Caused by: java.sql.SQLException: Connection refused
    at ...
```

**例题：**

**题1：** 不用异常链，直接这样写有什么问题？
```java
} catch (SQLException e) {
    throw new DataAccessException("加载用户失败");
}
```

<details>
<summary>答案</summary>

原始的 `SQLException` 被丢弃，日志里缺少真正的错误信息，排查问题困难。
</details>

**题2：** 如何获取一个异常的 cause？

<details>
<summary>答案</summary>

```java
Throwable cause = e.getCause(); // 无则返回 null
```
</details>

---

# 第 6 章：try-with-resources

## 6.1 问题背景（资源泄漏）

操作文件、数据库连接时，用完必须关闭。传统 finally 写法繁琐：

```java
// 传统写法
FileReader fr = null;
try {
    fr = new FileReader("a.txt");
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (fr != null) {
        try { fr.close(); } catch (IOException e) { e.printStackTrace(); }
    }
}

// try-with-resources 写法
try (FileReader fr = new FileReader("a.txt")) {
    // 读文件...
} catch (IOException e) {
    e.printStackTrace();
}
// close() 自动调用
```

**例题：**

**题1：** try-with-resources 中，close() 是在什么时机调用的？

<details>
<summary>答案</summary>

try 块结束时（无论正常还是异常），在 catch 块执行之前，资源的 close() 被自动调用。
</details>

---

## 6.2 AutoCloseable 接口

try-with-resources 要求资源实现 `AutoCloseable` 接口（只有一个 `close()` 方法）。

```java
public class MyResource implements AutoCloseable {
    public void use() { System.out.println("使用资源"); }

    @Override
    public void close() { System.out.println("资源已关闭"); }
}

try (MyResource res = new MyResource()) {
    res.use();
}
// 输出：
// 使用资源
// 资源已关闭
```

**多个资源：** 按声明顺序的**逆序**关闭。

```java
try (MyResource a = new MyResource();
     MyResource b = new MyResource()) {
    // b 先关闭，a 后关闭
}
```

**例题：**

**题1：** 下面代码输出什么？
```java
try (MyResource res = new MyResource()) {
    System.out.println("try块");
    throw new RuntimeException();
} catch (RuntimeException e) {
    System.out.println("catch块");
}
```

<details>
<summary>答案</summary>

```
try块
资源已关闭
catch块
```
</details>

**题2：** 一个类没有实现 `AutoCloseable`，能放进 try 的括号里吗？

<details>
<summary>答案</summary>

不能，编译报错。
</details>

---

# 第 7 章：综合练习

## 题1 `[1.3 + 3 + 4]`

写一个方法 `parseInt(String s)`：
- s 为 null → 抛 `IllegalArgumentException`
- s 不是合法整数 → 抛自定义 checked 异常 `ParseException`，保留原始 `NumberFormatException` 作为 cause
- 否则返回解析后的 int

<details>
<summary>参考答案</summary>

```java
public class ParseException extends Exception {
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

int parseInt(String s) throws ParseException {
    if (s == null) throw new IllegalArgumentException("输入不能为null");
    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        throw new ParseException("非法整数：" + s, e);
    }
}
```

**常见错误：** 丢弃 cause，或把 `ParseException` 定义为 unchecked 却要求 throws 声明。
</details>

---

## 题2 `[2 + 5 + 6]`

写一个 `readFirstLine(String path)` 方法：
- 用 try-with-resources 读取文件第一行
- 文件不存在时，包装成自定义 unchecked 异常 `FileReadException` 抛出
- 保证资源一定被关闭

<details>
<summary>参考答案</summary>

```java
public class FileReadException extends RuntimeException {
    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}

String readFirstLine(String path) {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    } catch (IOException e) {
        throw new FileReadException("读取文件失败：" + path, e);
    }
}
```

**常见错误：** 不用 try-with-resources，或 catch 后直接打印不抛出。
</details>

---

## 题3 `[2.3 + 2.5 + 3 + 4]`

下面代码输出什么？解释每一步。

```java
static String test() {
    try {
        throw new IOException();
    } catch (RuntimeException e) {
        return "RuntimeException";
    } catch (Exception e) {
        return "Exception";
    } finally {
        System.out.println("finally");
    }
}

public static void main(String[] args) {
    System.out.println(test());
}
```

<details>
<summary>参考答案</summary>

输出：
```
finally
Exception
```

步骤：
1. 抛出 `IOException`（checked，继承 Exception）
2. 第一个 catch：`RuntimeException` 不匹配，跳过
3. 第二个 catch：`Exception` 匹配，准备 return "Exception"
4. finally 先于 return 执行，打印 "finally"
5. 最终返回 "Exception"
</details>

---

## 延伸阅读

- `Exception.getSuppressed()` — try-with-resources 中 close() 也抛异常时的处理机制
- 并发异常：`InterruptedException` 的正确处理方式
- Spring 的 `@ExceptionHandler` — Web 层统一异常处理
- `Optional` — 替代部分"找不到则抛异常"场景的另一种思路
