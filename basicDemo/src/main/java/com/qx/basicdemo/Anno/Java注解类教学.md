# Java 注解类（Annotation）系统教学

## 0. 学习前提

你的当前基础：听说过 Java 注解，见过 `@Override`、`@Test`、`@Deprecated` 这类写法。

本次目标：达到「能熟练使用」——不仅能看懂常见注解，还能自己定义注解，并知道什么时候需要用反射读取注解。

熟悉语言：Java。本教程不依赖其它语言类比。

---

## 1. 一句话定义

Java 注解是一种写在类、方法、字段、参数等程序元素上的“元信息标记”，它本身通常不直接改变代码逻辑，而是给编译器、工具或框架提供额外信息。

例如：

```java
@Override
public String toString() {
    return "User";
}
```

这里的 `@Override` 告诉编译器：这个方法应该重写父类方法。如果写错方法名，编译器会报错。

---

## 2. 本次教学范围

### 包含

- 注解是什么
- Java 内置常见注解
- 自定义注解类：`@interface`
- 注解属性
- 元注解：`@Target`、`@Retention`、`@Documented`、`@Inherited`
- 运行时用反射读取注解
- 注解在项目中的典型用法
- 常见错误与面试点

### 不包含

- Spring 注解底层完整源码
- AOP 框架实现细节
- 编译期注解处理器 `Annotation Processor` 的完整开发

<aside>
编译期注解处理器是更高级内容，例如 Lombok、MapStruct 会用到。本教程只会简单说明它的存在，不展开教学。
</aside>

---

## 3. 知识点先导预览

### 3.1 必选知识点（使用率 ≥ 50%）

| 知识点 | 用途 |
|---|---|
| 注解基本概念 | 理解 `@xxx` 到底是什么 |
| 内置注解 | 看懂 `@Override`、`@Deprecated`、`@SuppressWarnings` |
| 自定义注解 | 自己写 `public @interface Xxx` |
| 注解属性 | 给注解传参数，例如 `@MyAnno(name = "Tom")` |
| `@Target` | 限制注解能写在哪里 |
| `@Retention` | 决定注解保留到哪个阶段 |
| 反射读取注解 | 运行时获取类、方法、字段上的注解信息 |

### 3.2 可选知识点（使用率 10% ~ 50%）

| 知识点 | 用途 |
|---|---|
| `@Documented` | 让注解出现在 Javadoc 文档中 |
| `@Inherited` | 子类是否能继承父类上的类级注解 |
| 重复注解 | 同一个位置使用多个同类型注解 |
| 类型注解 | 写在泛型、类型使用位置上的注解 |
| 编译期注解处理 | 在编译期生成代码或校验代码 |

---

## 4. 教学目录树

```text
├── 1. 注解基本概念
│   ├── 1.1 什么是注解
│   ├── 1.2 注解和普通代码的区别
│   ├── 1.3 注解的三种使用者：编译器、工具、框架
│   └── 1.4 注解默认不会自动执行逻辑
│
├── 2. Java 内置常见注解
│   ├── 2.1 @Override
│   ├── 2.2 @Deprecated
│   ├── 2.3 @SuppressWarnings
│   └── 2.4 @FunctionalInterface
│
├── 3. 自定义注解类
│   ├── 3.1 @interface 语法
│   ├── 3.2 注解属性
│   ├── 3.3 default 默认值
│   ├── 3.4 value 特殊属性
│   └── 3.5 注解属性支持的类型
│
├── 4. 元注解
│   ├── 4.1 @Target
│   ├── 4.2 @Retention
│   ├── 4.3 @Documented
│   └── 4.4 @Inherited
│
├── 5. 反射读取注解
│   ├── 5.1 读取类上的注解
│   ├── 5.2 读取方法上的注解
│   ├── 5.3 读取字段上的注解
│   └── 5.4 根据注解实现简单业务逻辑
│
└── 6. 综合应用
    ├── 6.1 简单权限校验注解
    ├── 6.2 简单字段校验注解
    └── 6.3 注解使用常见误区
```

---

# 1. 注解基本概念

## 1.1 什么是注解

### 概念与动机

注解是 Java 提供的一种“给代码添加说明信息”的机制。

普通代码是直接执行业务逻辑的，例如：

```java
System.out.println("hello");
```

注解本身通常不执行逻辑，它更像一个标记：

```java
@Deprecated
public void oldMethod() {
}
```

这表示 `oldMethod` 已经过时，调用它时 IDE 或编译器可能会提示警告。

### 简单使用示例

```java
public class Demo {
    @Override
    public String toString() {
        return "Demo";
    }
}
```

`@Override` 的作用是让编译器检查 `toString` 是否真的重写了父类方法。

### 教学例题

#### 例题 1：判断注解是否会自动执行

```java
@Deprecated
public class OldService {
    public void run() {
        System.out.println("running");
    }
}
```

问题：`@Deprecated` 会不会阻止 `run()` 执行？

答案：不会。它只表示这个类过时了，通常只产生警告。

#### 例题 2：注解的作用对象

```java
public class User {
    @Deprecated
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
```

这里 `@Deprecated` 写在字段上，`@Override` 写在方法上。不同注解可以限制不同使用位置。

#### 例题 3：为什么需要注解

如果没有 `@Override`：

```java
public class User {
    public String tostring() {
        return "User";
    }
}
```

`tostring` 写错了大小写，但编译器不会认为它是重写 `toString`。

加上 `@Override`：

```java
public class User {
    @Override
    public String tostring() {
        return "User";
    }
}
```

编译器会报错，帮助你提前发现问题。

### 本节练习题

1. `@Deprecated` 是否会让方法不能被调用？
2. `@Override` 的主要作用是什么？
3. 注解本身是否一定会改变程序运行结果？
4. 为什么注解经常被框架使用？
5. 注解可以写在哪些地方？举 3 个例子。
6. `@Override` 写错方法名时，谁会发现错误？
7. 注解和普通方法调用的区别是什么？
8. 一个类没有注解，是否还能正常运行？
9. 注解信息一定能在运行时读取到吗？
10. 你见过哪些 Java 注解？写出它们可能的用途。

---

# 2. Java 内置常见注解

## 2.1 `@Override`

### 概念与动机

`@Override` 表示当前方法必须重写父类或接口中的方法。

它的核心价值是：让编译器帮你检查方法名、参数、返回值是否正确。

### 简单使用示例

```java
public class Animal {
    public void sound() {
        System.out.println("animal sound");
    }
}

class Dog extends Animal {
    @Override
    public void sound() {
        System.out.println("wang wang");
    }
}
```

### 教学例题

#### 例题 1：方法名写错

```java
class Dog extends Animal {
    @Override
    public void sounds() {
        System.out.println("wang wang");
    }
}
```

`sounds` 不是父类中的 `sound`，编译器会报错。

#### 例题 2：参数列表不同

```java
class Dog extends Animal {
    @Override
    public void sound(String name) {
        System.out.println(name);
    }
}
```

父类没有 `sound(String name)`，这不是重写，编译器会报错。

#### 例题 3：接口方法实现

```java
interface Printable {
    void print();
}

class Report implements Printable {
    @Override
    public void print() {
        System.out.println("report");
    }
}
```

实现接口方法也可以使用 `@Override`。

---

## 2.2 `@Deprecated`

### 概念与动机

`@Deprecated` 表示某个类、方法、字段已经不推荐使用。

常见原因：

- 方法设计不合理
- 有更好的替代方法
- 存在安全问题
- 未来版本可能删除

### 简单使用示例

```java
public class UserService {
    @Deprecated
    public void loginByName(String name) {
        System.out.println("old login");
    }

    public void loginByPhone(String phone) {
        System.out.println("new login");
    }
}
```

### 教学例题

#### 例题 1：调用过时方法

```java
public class Main {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.loginByName("tom");
    }
}
```

通常可以运行，但 IDE 或编译器会提示警告。

#### 例题 2：带说明的过时注解

```java
public class UserService {
    @Deprecated(since = "1.2", forRemoval = true)
    public void oldLogin() {
    }
}
```

`since` 表示从哪个版本开始过时，`forRemoval` 表示未来是否准备删除。

---

## 2.3 `@SuppressWarnings`

### 概念与动机

`@SuppressWarnings` 用来告诉编译器：某些警告我知道，可以不要提示。

### 简单使用示例

```java
@SuppressWarnings("deprecation")
public class Main {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.loginByName("tom");
    }
}
```

这里会压制调用过时方法产生的警告。

### 教学例题

#### 例题 1：压制未检查转换警告

```java
import java.util.ArrayList;
import java.util.List;

public class Demo {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List list = new ArrayList();
        List<String> names = list;
        names.add("tom");
    }
}
```

这段代码不推荐这样写，但可以演示 `unchecked` 警告。

#### 例题 2：同时压制多个警告

```java
@SuppressWarnings({"deprecation", "unchecked"})
public class Demo {
}
```

---

## 2.4 `@FunctionalInterface`

### 概念与动机

`@FunctionalInterface` 表示一个接口必须是函数式接口：只能有一个抽象方法。

### 简单使用示例

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
}
```

这个接口可以配合 lambda 使用：

```java
public class Main {
    public static void main(String[] args) {
        Calculator calculator = (a, b) -> a + b;
        System.out.println(calculator.add(1, 2));
    }
}
```

### 教学例题

#### 例题 1：多写一个抽象方法

```java
@FunctionalInterface
interface Calculator {
    int add(int a, int b);
    int sub(int a, int b);
}
```

编译器会报错，因为它不再是函数式接口。

---

## 本章练习题

1. `@Override` 可以防止哪类错误？
2. `@Deprecated` 是否会导致程序运行失败？
3. `@Deprecated(since = "1.0")` 中 `since` 的含义是什么？
4. `@SuppressWarnings("deprecation")` 是什么意思？
5. `@FunctionalInterface` 标记的接口最多能有几个抽象方法？
6. 接口中的默认方法会不会破坏函数式接口？
7. 为什么不建议滥用 `@SuppressWarnings`？
8. `@Override` 能不能写在字段上？
9. 调用过时方法时，通常出现的是错误还是警告？
10. 写一个函数式接口 `Converter`，包含一个方法 `String convert(int value)`。

---

# 3. 自定义注解类

## 3.1 `@interface` 语法

### 概念与动机

自定义注解可以让你给代码添加自己的标记。

定义注解使用 `@interface`，不是 `class`，也不是 `interface`。

### 简单使用示例

```java
public @interface MyAnnotation {
}
```

使用它：

```java
@MyAnnotation
public class User {
}
```

### 教学例题

#### 例题 1：定义一个标记注解

```java
public @interface Important {
}

@Important
class OrderService {
}
```

`@Important` 没有属性，只表示一个标记。

#### 例题 2：定义方法级注解

```java
public @interface Loggable {
}

class UserService {
    @Loggable
    public void createUser() {
        System.out.println("create user");
    }
}
```

此时它只是一个标记，是否产生效果取决于有没有代码或框架读取它。

---

## 3.2 注解属性

### 概念与动机

注解可以带属性，用来保存更具体的信息。

注解属性的写法像“没有参数的方法”：

```java
public @interface Role {
    String name();
}
```

使用时：

```java
@Role(name = "admin")
public class AdminController {
}
```

### 简单使用示例

```java
public @interface Table {
    String name();
}

@Table(name = "user")
class User {
}
```

### 教学例题

#### 例题 1：多个属性

```java
public @interface Column {
    String name();
    int length();
}

class User {
    @Column(name = "username", length = 20)
    private String username;
}
```

#### 例题 2：属性顺序不重要

```java
@Column(length = 20, name = "username")
private String username;
```

和下面等价：

```java
@Column(name = "username", length = 20)
private String username;
```

---

## 3.3 `default` 默认值

### 概念与动机

如果注解属性有默认值，使用注解时可以不写该属性。

### 简单使用示例

```java
public @interface Column {
    String name();
    int length() default 255;
}

class User {
    @Column(name = "nickname")
    private String nickname;
}
```

`length` 没写时默认是 `255`。

### 教学例题

#### 例题 1：覆盖默认值

```java
@Column(name = "password", length = 64)
private String password;
```

这里 `length` 使用显式传入的 `64`。

#### 例题 2：全部使用默认值

```java
public @interface EnableLog {
    boolean value() default true;
}

@EnableLog
class UserService {
}
```

---

## 3.4 `value` 特殊属性

### 概念与动机

如果注解只有一个属性，并且属性名叫 `value`，使用时可以省略 `value =`。

### 简单使用示例

```java
public @interface Tag {
    String value();
}

@Tag("controller")
class UserController {
}
```

等价于：

```java
@Tag(value = "controller")
class UserController {
}
```

### 教学例题

#### 例题 1：数组形式的 value

```java
public @interface Roles {
    String[] value();
}

@Roles({"admin", "manager"})
class AdminPage {
}
```

#### 例题 2：只有一个数组元素时

```java
@Roles("admin")
class AdminPage {
}
```

---

## 3.5 注解属性支持的类型

### 概念与动机

注解属性不是任何类型都能写，只支持固定类型。

支持：

- 基本类型：`int`、`boolean`、`double` 等
- `String`
- `Class`
- 枚举
- 注解
- 以上类型的一维数组

不支持：

- 普通对象
- 集合：`List`、`Map`
- 包装类：`Integer`、`Boolean`

### 简单使用示例

```java
enum Level {
    LOW, MIDDLE, HIGH
}

public @interface Task {
    String name();
    int priority();
    Level level();
    Class<?> type();
    String[] tags();
}
```

使用：

```java
@Task(
    name = "import data",
    priority = 1,
    level = Level.HIGH,
    type = String.class,
    tags = {"data", "important"}
)
class ImportTask {
}
```

### 本章练习题

1. 定义一个没有属性的注解 `@Todo`。
2. 定义一个注解 `@Author`，包含 `String name()`。
3. 使用 `@Author(name = "Tom")` 标记一个类。
4. 给 `@Author` 增加 `int age() default 18`。
5. 定义一个注解 `@Route`，只包含 `String value()`。
6. 使用 `@Route("/users")` 标记一个类。
7. 定义一个注解 `@Permission`，包含 `String[] value()`。
8. 使用 `@Permission({"user:add", "user:delete"})` 标记一个方法。
9. 注解属性能否使用 `List<String>`？为什么？
10. 注解属性能否使用 `Integer`？为什么？
11. 写一个包含枚举属性的注解。
12. 写一个包含 `Class<?>` 属性的注解。

---

# 4. 元注解

元注解就是“修饰注解的注解”。

例如：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Loggable {
}
```

这里 `@Target` 修饰的是 `Loggable` 这个注解。

---

## 4.1 `@Target`

### 概念与动机

`@Target` 用来限制注解可以写在哪里。

常见位置：

| ElementType | 含义 |
|---|---|
| `TYPE` | 类、接口、枚举、注解 |
| `METHOD` | 方法 |
| `FIELD` | 字段 |
| `PARAMETER` | 方法参数 |
| `CONSTRUCTOR` | 构造方法 |
| `LOCAL_VARIABLE` | 局部变量 |
| `ANNOTATION_TYPE` | 注解类 |

### 简单使用示例

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Loggable {
}
```

只能写在方法上：

```java
class UserService {
    @Loggable
    public void save() {
    }
}
```

### 教学例题

#### 例题 1：允许写在类和方法上

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Important {
}
```

使用：

```java
@Important
class OrderService {
    @Important
    public void createOrder() {
    }
}
```

#### 例题 2：写错位置会报错

```java
@Target(ElementType.METHOD)
public @interface Loggable {
}

@Loggable
class UserService {
}
```

`@Loggable` 只能写在方法上，写在类上会编译报错。

---

## 4.2 `@Retention`

### 概念与动机

`@Retention` 决定注解信息保留到哪个阶段。

| RetentionPolicy | 含义 | 是否能反射读取 |
|---|---|---|
| `SOURCE` | 只保留在源码中，编译后没有 | 不能 |
| `CLASS` | 保留到 `.class` 文件，运行时通常不能反射读取 | 通常不能 |
| `RUNTIME` | 保留到运行时 | 能 |

如果你想用反射读取注解，必须使用 `RetentionPolicy.RUNTIME`。

### 简单使用示例

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}
```

### 教学例题

#### 例题 1：运行时可读取

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    String value();
}
```

这个注解可以在运行时通过反射读取。

#### 例题 2：源码级注解

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface CheckStyle {
}
```

这种注解适合给编译器或源码检查工具使用，运行时读不到。

---

## 4.3 `@Documented`

### 概念与动机

`@Documented` 表示使用该注解的元素在生成 Javadoc 时会展示这个注解。

### 简单使用示例

```java
import java.lang.annotation.Documented;

@Documented
public @interface Api {
    String value();
}
```

---

## 4.4 `@Inherited`

### 概念与动机

`@Inherited` 表示子类可以继承父类上的“类级注解”。

注意：它只对类上的注解有效，对方法、字段无效。

### 简单使用示例

```java
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}

@Controller
class BaseController {
}

class UserController extends BaseController {
}
```

如果读取 `UserController.class` 上的 `@Controller`，可以读到继承来的注解。

### 本章练习题

1. `@Target(ElementType.METHOD)` 表示什么？
2. 如何让一个注解既能写在类上，也能写在方法上？
3. 如果注解想被反射读取，`@Retention` 应该设置为什么？
4. `RetentionPolicy.SOURCE` 的注解运行时能不能读取？
5. `RetentionPolicy.CLASS` 和 `RetentionPolicy.RUNTIME` 的区别是什么？
6. `@Documented` 的作用是什么？
7. `@Inherited` 是否能让字段注解被子类继承？
8. 写一个只能标记字段的注解 `@Column`。
9. 写一个运行时可读取的方法注解 `@Loggable`。
10. 写一个可以标记类和方法的注解 `@Permission`。

---

# 5. 反射读取注解

## 5.1 读取类上的注解

### 概念与动机

注解只是标记，如果想在运行时根据注解做事，需要用反射读取它。

### 简单使用示例

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface Table {
    String name();
}

@Table(name = "users")
class User {
}

public class Main {
    public static void main(String[] args) {
        Class<User> clazz = User.class;
        Table table = clazz.getAnnotation(Table.class);
        System.out.println(table.name());
    }
}
```

输出：

```text
users
```

### 教学例题

#### 例题 1：判断类上是否有注解

```java
if (User.class.isAnnotationPresent(Table.class)) {
    System.out.println("User has @Table");
}
```

#### 例题 2：没有注解时返回 null

```java
Table table = String.class.getAnnotation(Table.class);
System.out.println(table == null);
```

输出：

```text
true
```

---

## 5.2 读取方法上的注解

### 概念与动机

很多框架会读取方法上的注解，例如判断某个接口路径、权限、事务等。

### 简单使用示例

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@interface Route {
    String value();
}

class UserController {
    @Route("/users")
    public void listUsers() {
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Method method = UserController.class.getMethod("listUsers");
        Route route = method.getAnnotation(Route.class);
        System.out.println(route.value());
    }
}
```

输出：

```text
/users
```

### 教学例题

#### 例题 1：遍历所有方法

```java
for (Method method : UserController.class.getDeclaredMethods()) {
    if (method.isAnnotationPresent(Route.class)) {
        Route route = method.getAnnotation(Route.class);
        System.out.println(method.getName() + " -> " + route.value());
    }
}
```

#### 例题 2：方法没有注解时跳过

```java
class UserController {
    public void index() {
    }

    @Route("/users")
    public void listUsers() {
    }
}
```

遍历时只有 `listUsers` 会被处理。

---

## 5.3 读取字段上的注解

### 概念与动机

字段注解常用于对象映射、参数校验、数据库字段映射。

### 简单使用示例

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@interface Column {
    String name();
}

class User {
    @Column(name = "user_name")
    private String username;
}

public class Main {
    public static void main(String[] args) throws Exception {
        Field field = User.class.getDeclaredField("username");
        Column column = field.getAnnotation(Column.class);
        System.out.println(column.name());
    }
}
```

输出：

```text
user_name
```

---

## 5.4 根据注解实现简单业务逻辑

### 概念与动机

框架的很多功能都可以理解为：

1. 你写注解
2. 框架扫描类、方法、字段
3. 框架读取注解
4. 框架根据注解执行对应逻辑

### 综合示例：简单权限检查

```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@interface RequireRole {
    String value();
}

class UserService {
    @RequireRole("admin")
    public void deleteUser() {
        System.out.println("delete user success");
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Set<String> currentUserRoles = Set.of("user");

        UserService service = new UserService();
        Method method = UserService.class.getMethod("deleteUser");
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        if (requireRole != null && !currentUserRoles.contains(requireRole.value())) {
            System.out.println("permission denied");
            return;
        }

        method.invoke(service);
    }
}
```

输出：

```text
permission denied
```

把角色改成 `Set.of("admin")` 后，输出：

```text
delete user success
```

### 本章练习题

1. 反射读取类注解用哪个方法？
2. `getAnnotation` 找不到注解时返回什么？
3. `isAnnotationPresent` 的作用是什么？
4. 读取方法注解需要先得到什么对象？
5. 读取字段注解需要先得到什么对象？
6. 为什么运行时读取注解必须使用 `RetentionPolicy.RUNTIME`？
7. 写一个 `@Table(name = "users")` 注解并读取它。
8. 写一个 `@Route("/login")` 注解并读取它。
9. 写一个 `@Column(name = "user_name")` 注解并读取它。
10. 修改权限检查示例，让 `manager` 也能删除用户。
11. 如果方法没有 `@RequireRole`，应该允许执行还是拒绝执行？说明你的设计理由。
12. 为什么说“注解本身不执行逻辑，读取注解的代码才执行逻辑”？

---

# 6. 综合应用

## 6.1 简单字段校验注解

### 目标

使用注解标记字段不能为空，并通过反射检查对象是否合法。

### 完整示例

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface NotBlank {
    String message() default "字段不能为空";
}

class User {
    @NotBlank(message = "用户名不能为空")
    private String username;

    public User(String username) {
        this.username = username;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User("");
        validate(user);
    }

    static void validate(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            if (notBlank == null) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(object);
            if (value == null || value.toString().isBlank()) {
                System.out.println(notBlank.message());
            }
        }
    }
}
```

输出：

```text
用户名不能为空
```

---

## 6.2 简单路由注解

### 目标

用方法注解模拟“路径 -> 方法”的映射。

### 完整示例

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface GetMapping {
    String value();
}

class UserController {
    @GetMapping("/users")
    public void users() {
        System.out.println("user list");
    }

    @GetMapping("/orders")
    public void orders() {
        System.out.println("order list");
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        dispatch("/users");
    }

    static void dispatch(String path) throws Exception {
        UserController controller = new UserController();
        for (Method method : UserController.class.getDeclaredMethods()) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (mapping != null && mapping.value().equals(path)) {
                method.invoke(controller);
                return;
            }
        }
        System.out.println("404 not found");
    }
}
```

输出：

```text
user list
```

---

## 6.3 常见误区

### 误区 1：以为写了注解就自动有功能

错误理解：

```java
@RequireRole("admin")
public void deleteUser() {
}
```

只要写了这个注解，权限就自动生效。

正确理解：

必须有框架或代码读取 `@RequireRole`，并执行权限判断，注解才会产生实际效果。

### 误区 2：忘记写 `@Retention(RetentionPolicy.RUNTIME)`

```java
@interface Route {
    String value();
}
```

如果没有指定 `@Retention`，默认是 `CLASS`，运行时反射通常读不到。

### 误区 3：注解属性使用了不支持的类型

错误写法：

```java
import java.util.List;

public @interface Tags {
    List<String> value();
}
```

正确写法：

```java
public @interface Tags {
    String[] value();
}
```

### 误区 4：`@Inherited` 以为对方法也有效

`@Inherited` 只影响类级注解，不影响方法和字段。

---

# 7. 综合练习

## 练习 1：定义并使用类注解

所需章节：3、4、5

要求：

1. 定义 `@Table` 注解
2. 只能写在类上
3. 运行时可以读取
4. 包含属性 `String name()`
5. 标记 `User` 类为 `@Table(name = "users")`
6. 通过反射输出表名

参考答案：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Table {
    String name();
}

@Table(name = "users")
class User {
}

public class Main {
    public static void main(String[] args) {
        Table table = User.class.getAnnotation(Table.class);
        System.out.println(table.name());
    }
}
```

常见错误：

- 忘记 `@Retention(RetentionPolicy.RUNTIME)`
- 把 `@Target` 写成 `ElementType.METHOD`
- 使用时写成 `@Table("users")`，但属性名不是 `value`

---

## 练习 2：定义并读取方法权限注解

所需章节：3、4、5、6

要求：

1. 定义 `@RequirePermission`
2. 只能写在方法上
3. 运行时可读取
4. 包含 `String value()`
5. 标记 `deleteUser` 方法为 `@RequirePermission("user:delete")`
6. 判断当前用户权限集合中是否包含该权限

参考答案：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Set;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface RequirePermission {
    String value();
}

class UserService {
    @RequirePermission("user:delete")
    public void deleteUser() {
        System.out.println("delete success");
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        Set<String> permissions = Set.of("user:add");
        Method method = UserService.class.getMethod("deleteUser");
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);

        if (annotation != null && !permissions.contains(annotation.value())) {
            System.out.println("no permission");
            return;
        }

        method.invoke(new UserService());
    }
}
```

常见错误：

- `value` 写成 `name` 后，使用时仍然写 `@RequirePermission("xxx")`
- 没有处理 `annotation == null` 的情况
- 权限字符串拼写不一致

---

## 练习 3：字段长度校验

所需章节：3、4、5、6

要求：

1. 定义 `@MaxLength`
2. 只能写在字段上
3. 运行时可读取
4. 包含 `int value()` 和 `String message() default "长度超出限制"`
5. 如果字段字符串长度超过限制，输出错误信息

参考答案：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MaxLength {
    int value();
    String message() default "长度超出限制";
}

class User {
    @MaxLength(value = 5, message = "用户名最多 5 个字符")
    private String username;

    public User(String username) {
        this.username = username;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User("veryLongName");
        validate(user);
    }

    static void validate(Object object) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            MaxLength maxLength = field.getAnnotation(MaxLength.class);
            if (maxLength == null) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null && value.toString().length() > maxLength.value()) {
                System.out.println(maxLength.message());
            }
        }
    }
}
```

常见错误：

- 没有 `field.setAccessible(true)`，导致私有字段无法读取
- 没有判断 `value != null`
- 把 `value()` 写成 `length()`，使用时却省略了属性名

---

## 练习 4：组合使用多个注解

所需章节：2、3、4、5、6

要求：

1. 定义 `@Controller`，只能写在类上，运行时可读取
2. 定义 `@GetMapping`，只能写在方法上，运行时可读取
3. `@GetMapping` 包含 `String value()`
4. 标记一个控制器类和两个方法
5. 扫描并输出所有路径

参考答案：

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Controller {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface GetMapping {
    String value();
}

@Controller
class UserController {
    @GetMapping("/users")
    public void users() {
    }

    @GetMapping("/users/detail")
    public void detail() {
    }
}

public class Main {
    public static void main(String[] args) {
        Class<UserController> clazz = UserController.class;
        if (!clazz.isAnnotationPresent(Controller.class)) {
            return;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (mapping != null) {
                System.out.println(mapping.value() + " -> " + method.getName());
            }
        }
    }
}
```

常见错误：

- 只检查方法注解，忘记检查类上是否有 `@Controller`
- 把 `GetMapping` 的 `@Target` 写成 `TYPE`
- 忘记导入 `java.lang.reflect.Method`

---

# 8. 面试与项目常见问题

## 8.1 注解是什么？

注解是 Java 的元信息机制，用来给类、方法、字段、参数等元素添加标记或配置。它本身通常不直接执行业务逻辑，需要编译器、工具、框架或反射代码读取后才产生效果。

## 8.2 注解可以标记哪些位置？

由 `@Target` 决定，常见位置包括类、方法、字段、参数、构造方法、局部变量、注解类等。

## 8.3 `@Retention` 有哪些取值？

- `SOURCE`：只在源码中保留
- `CLASS`：保留到字节码，运行时通常不可反射读取
- `RUNTIME`：运行时保留，可反射读取

## 8.4 为什么 Spring 注解能生效？

因为 Spring 会在启动或运行过程中扫描类、方法、字段上的注解，然后根据注解创建对象、注入依赖、注册路由、开启事务等。

注解只是信息，Spring 的扫描和处理逻辑才是真正执行功能的部分。

## 8.5 自定义注解必须加 `@Retention(RetentionPolicy.RUNTIME)` 吗？

不一定。

如果只是给编译器或源码工具使用，可以是 `SOURCE`。

如果希望运行时用反射读取，就必须是 `RUNTIME`。

## 8.6 注解属性为什么不能用 `List<String>`？

Java 语言规范限制注解属性类型，只允许基本类型、`String`、`Class`、枚举、注解，以及这些类型的一维数组。

---

# 9. 学习路线建议

1. 先熟练掌握 `@Override`、`@Deprecated`、`@SuppressWarnings`
2. 再掌握 `@interface`、属性、`default`、`value`
3. 然后重点理解 `@Target` 和 `@Retention`
4. 最后练习通过反射读取类、方法、字段上的注解
5. 如果学习 Spring，再把 `@Controller`、`@Service`、`@Autowired`、`@RequestMapping` 理解成“框架读取注解后执行逻辑”

---

# 10. 最小记忆版总结

- 注解是代码上的元信息标记
- 注解本身通常不执行逻辑
- 自定义注解用 `@interface`
- 注解属性写法像无参方法
- 属性名叫 `value` 时，使用时可以省略 `value =`
- `@Target` 控制注解能写在哪里
- `@Retention` 控制注解保留到哪个阶段
- 想运行时反射读取，必须使用 `RetentionPolicy.RUNTIME`
- 读取类注解：`Class#getAnnotation`
- 读取方法注解：`Method#getAnnotation`
- 读取字段注解：`Field#getAnnotation`
- 框架注解的本质：框架扫描注解并执行对应逻辑
