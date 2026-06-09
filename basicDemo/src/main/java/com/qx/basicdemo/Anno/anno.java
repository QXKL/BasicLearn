package com.qx.basicdemo.Anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class anno {
    @Deprecated
    private static final int a = 1;

    public static void main(String[] args) throws Exception {
        System.out.println(anno.test());

        String str = "123";
        System.out.println(str.toString());

        System.out.println(a);

        System.out.println("------------------");

        anno anno = new anno();

        if (anno.hasAnno()) System.out.println("有annoWA注解");
    }

    static int test() {
        try {
            return 1;
        } finally {
            System.out.println("finally执行了");
        }
    }

    @Override
    public String toString() {
        return "Demo";
    }

    @annoWA(value = "Anno is here")
    public int WithAnnoWA() {
        return 1;
    }

    private boolean hasAnno() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("WithAnnoWA");

        annoWA annoWA = method.getAnnotation(annoWA.class);

        return annoWA.value().equals("Anno is here") || annoWA.value().equals("Anno~");
    }

}

@Retention(RetentionPolicy.RUNTIME)
@interface annoWA {
    String value() default "Anno~";
}
