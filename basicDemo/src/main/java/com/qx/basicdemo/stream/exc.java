package com.qx.basicdemo.stream;

import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class exc {
    public static void main(String[] args) {
        //---------------
        new Thread(() -> System.out.println("Lambda 线程执行")).start();

        //---------------
        List<String> data = Arrays.asList("Java", "Lambda", "Stream");
        data.forEach(System.out::println);


        //---------------
        List<Integer> numList = Arrays.asList(3, 1, 9, 5, 2);
        List<Integer> numList2 = numList.stream().sorted().toList();

        System.out.println("numList2: " + numList2);

        //---------------
        Predicate<Integer> p = x -> x > 20;

        System.out.println(p.test(25)); // 输出 true
        System.out.println(p.test(15)); // 输出 false

        //---------------
        Function<String, Integer> f = Integer::parseInt;

        System.out.println(f.apply("123")); // 输出 123

        //---------------
        List<Integer> list = Arrays.asList(1, 3, 7, 9, 2, 6);

        List<Integer> filteredList = list.stream().filter(x -> x > 5).toList();

        //---------------
        List<String> strList = Arrays.asList("java", "lambda", "stream");

        List<String> upperStrList = strList.stream().map(x -> x.toUpperCase()).toList();
//        List<String> upperStrList = strList.stream().map(String::toUpperCase).toList();

        //---------------
        List<Integer> nums = Arrays.asList(10, 20, 30, 40);
        int sum = nums.stream().reduce(0, Integer::sum);

        System.out.println(sum); // 输出 100

        //---------------
        List<Integer> listL = Arrays.asList(2, 2, 5, 3, 5, 1);
        Set<Integer> set = listL.stream().collect(Collectors.toSet());
//        Set<Integer> set2 = new HashSet<>(listL);

        System.out.println("set = " + set); // 输出 set = [1, 2, 3, 5]

        //---------------
        List<Integer> listE = Arrays.asList(5, 2, 9, 1, 6);

        List<Integer> sortedList = listE.stream().sorted().toList();

        System.out.println(sortedList); // 输出 [1, 2, 5, 6, 9]

        //---------------
        User user1 = new User(1L, "Alice", 25);
        User user2 = new User(2L, "Bob", 30);

        List<User> userList = Arrays.asList(user1, user2);

        Set<String> UserNames = userList.stream().map(User::getName).collect(Collectors.toSet());

        System.out.println("UserNames = " + UserNames); // 输出 UserNames = [Alice, Bob]


//        int counterForAgePo18 = userList.stream().filter(user -> user.getAge() > 18).collect(Collectors.toSet()).size();  // 这是很浪费性能的写法，不要学哦？这就好比为了知道一盒糖里有几颗，把糖全倒进另一个盒子里再数，极其浪费内存和性能
        int counterForAgePo18 = (int) userList.stream().filter(user -> user.getAge() > 18).count();

        System.out.println("counterForAgePo18 = " + counterForAgePo18); // 输出 counterForAgePo18 = 2

//        boolean hasUserNamedZS = !userList.stream().filter(user -> "张三".equals(user.getName())).collect(Collectors.toSet()).isEmpty();  // 这个也一样，浪费性能。总之，collect虽然强大，但是还是不能滥用。
        boolean hasUserNamedZS = userList.stream().anyMatch(user -> "张三".equals(user.getName()));

        System.out.println("hasUserNamedZS = " + hasUserNamedZS); // 输出 hasUserNamedZS = false

        User userOfMaxAge = userList.stream().max(Comparator.comparing(User::getAge)).orElse(null);
        User userOfMinAge = userList.stream().min(Comparator.comparing(User::getAge)).orElse(null);

        System.out.println("userOfMaxAge = " + userOfMaxAge.getName()); // 输出 userOfMaxAge = User(id=2, name=Bob, age=30)

        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));

        userMap.forEach((x, y) -> System.out.println(x + ": " + y.getName())); // 输出 1: Alice 2: Bob

        //---------------- 进阶题

        // 按照用户年龄分组
        Map<Integer, List<User>> userGroupByAge = userList.stream().collect(Collectors.groupingBy(User::getAge));

        System.out.println("userGroupByAge = " + userGroupByAge); // 输出 userGroupByAge = {18=[User(id=1, name=Alice, age=18)], 30=[User(id=2, name=Bob, age=30)]}

        // 所有用户平均年龄
        Double averageAge = userList.stream().collect(Collectors.averagingInt(User::getAge));

        System.out.println("averageAge = " + averageAge); // 输出 averageAge = 24.5

        // 依次完成：过滤大于2的元素 → 去重 → 升序排序(?集合为什么还需要排序)，最终收集为集合。
        List<Integer> dataL = Arrays.asList(1, 4, 2, 5, 2, 7, 4);

        Set<Integer> result = dataL.stream().filter(x -> x <= 2).collect(Collectors.toSet());

        System.out.println("result = " + result); // 输出 result = [1, 2]

        // 所有用户姓名拼合
        String names = userList.stream().map(User::getName).collect(Collectors.joining(", "));

        System.out.println("names = " + names); // 输出 names = Alice, Bob

        // 从 `userList` 中筛选出年龄在 20 ~ 30 岁区间的用户，提取对应姓名形成新集合。
        Set<String> namesForAgeRange20_30 = userList.stream().filter(x -> x.getAge() >= 20 && x.getAge() <= 30).map(User::getName).collect(Collectors.toSet());

        System.out.println("namesForAgeRange20_30 = " + namesForAgeRange20_30); // 输出 namesForAgeRange20_30 = [Bob, Alice]


        //------------------------- 高阶

        // 使用 `flatMap` 将其扁平化，转为单层整数集合。
        List<List<Integer>> nestedList = Arrays.asList(Arrays.asList(1,2), Arrays.asList(3,4), Arrays.asList(5,6));

        List<Integer> flatList = nestedList.stream().flatMap(List::stream).toList();  // 说明一下哦，这里是将内嵌List中的数据传为流，然后再将其转为集合。正好符合了流的心头好。

        // 从 `userList` 中提取所有用户 id，去重(?)后存入 `Set` 集合。
        Set<Long> userIds = userList.stream().map(User::getId).distinct().collect(Collectors.toSet());  // distinct() 去重。虽然但是，还是加上吧

        // 最小值
        List<Integer> num = Arrays.asList(8, 2, 15, 3, 7);
        Integer minNum = num.stream().min(Integer::compareTo).orElse(null);

        // 截取 `userList` 中前 3 个用户，生成新集合。
        List<User> userSetHas3 = userList.stream().limit(3).toList();

        // 跳过 `userList` 中前 2 个元素，获取剩余所有用户组成新集合。
        List<User> userSetSkip2 = userList.stream().skip(2).toList();

    }

    @Getter
    public static class User {
        private Long id;
        private String name;
        private int age;

        public User(Long id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }
}
