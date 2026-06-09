package com.qx.basicdemo.Anno;

import java.io.IOException;

public class anno {
    public static void main(String[] args) throws Exception {
        System.out.println(anno.test());

        anno.a();
    }

    static int test() {
        try {
            return 1;
        } finally {
            System.out.println("finally执行了");
        }
    }

    static void b() throws IOException {
        throw new IOException();
    }

    // 修复方案A
//    void a() {
//        try {
//            b();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // 修复方案B
    static void a() throws IOException {
        b();
    }
}
