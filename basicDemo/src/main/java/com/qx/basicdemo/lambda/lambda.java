package com.qx.basicdemo.lambda;

import java.util.Arrays;
import java.util.List;

public class lambda {

    public static void main(String[] args) {
        //====================================
        // 使用 Lambda 表达式计算和
        //====================================

        // 使用 Lambda 表达式计算两个数的和
        MathOperation addition = (a, b) -> a + b;

        // 调用 Lambda 表达式
        int result = addition.operation(1, 2);

        System.out.println("1 + 2 = " + result);

        //====================================
        // 使用 Lambda 表达式遍历列表
        //====================================

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        names.forEach(name -> System.out.println(name));

        //====================================
        // Lambda 表达式提供了一种更为简洁的语法，尤其适用于函数式接口。
        // 相比于传统的匿名内部类，Lambda 表达式使得代码更为紧凑，减少了样板代码的编写。
        //
        // 下面是对比
        //====================================

        // 传统的匿名内部类
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!");
            }
        };

        // Lambda 表达式
        // () 表示不需要参数,返回值为 5
        Runnable runnable2 = () -> System.out.println("Hello World!");

        //====================================
        // Lambda 表达式是函数式编程的一种体现.
        // 它允许将函数当作参数传递给方法，或者将函数作为返回值，这种支持使得 Java 在函数式编程方面更为灵活，能够更好地处理集合操作、并行计算等任务。
        //====================================

        List<String> namesL = Arrays.asList("Alice", "Bob", "Charlie");
        namesL.forEach(name -> System.out.println(name));

        //====================================
        // Lambda 表达式可以访问外部作用域的变量，这种特性称为变量捕获，Lambda 表达式可以隐式地捕获 final 或事实上是 final 的局部变量。
        //====================================

        int x = 10;
        MathOperation2 addition2 = y -> System.out.println(x + y);
        addition2.operation2(5);

        //=====================================
        // Lambda 表达式可以通过方法引用进一步简化，方法引用允许直接引用现有类或对象的方法，而不用编写冗余的代码。
        //=====================================

        // 使用方法引用
        List<String> namesL2 = Arrays.asList("Alice", "Bob", "Charlie");
        namesL2.forEach(System.out::println);

        //=====================================
        // AllInOne
        //=====================================

        lambda java8LambdaTest = new lambda();

        // 类型声明
        MathOperation op = (int a, int b) -> a + b;

        // 不用类型声明
        MathOperation op2 = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation op3 = (int a, int b) -> a * b;

        // 没有大括号及返回语句
        MathOperation op4 = (a, b) -> a / b;

        System.out.println("10 + 5 = " + java8LambdaTest.operate(10, 5, op));
        System.out.println("10 - 5 = " + java8LambdaTest.operate(10, 5, op2));
        System.out.println("10 * 5 = " + java8LambdaTest.operate(10, 5, op3));
        System.out.println("10 / 5 = " + java8LambdaTest.operate(10, 5, op4));

        // 不使用括号
        GreetingService greetService1 = message -> System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) -> System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");

        //=====================================
        // Lambda 表达式主要用来定义行内执行的方法类型接口（例如，一个简单方法接口）。
        // 在上面例子中，我们使用各种类型的 Lambda 表达式来定义 MathOperation 接口的方法，然后我们定义了 operation 的执行。
        // Lambda 表达式免去了使用匿名方法的麻烦，并且给予 Java 真正函数式编程的能力。
        //=====================================


    }

    @FunctionalInterface
    interface MathOperation {
        int operation(int a, int b);
    }

    @FunctionalInterface
    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }
}
