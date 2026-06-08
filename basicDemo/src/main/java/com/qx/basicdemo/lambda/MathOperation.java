package com.qx.basicdemo.lambda;

// MathOperation 是一个函数式接口，包含抽象方法 operation...
// 这个FunctionalInterface注解 是用来声明这是一个 函数式接口的
// 函数式接口是只有、也必须有一个抽象方法的接口，可以隐式地被转换为lambda表达式
// 当出现多个抽象方法，这里会直接报错
@FunctionalInterface
public interface MathOperation {

    int operation(int a, int b);

//    int operation2(int a);
}
