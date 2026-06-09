package com.qx.basicdemo.Anno;

import java.io.IOException;

public class anno {
    @Deprecated
    private static final int a = 1;

    public static void main(String[] args) throws Exception {
        System.out.println(anno.test());

        String str = "123";
        System.out.println(str.toString());

        System.out.println(a);
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
}
