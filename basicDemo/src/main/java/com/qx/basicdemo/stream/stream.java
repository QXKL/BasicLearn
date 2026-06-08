package com.qx.basicdemo.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。
 * Stream 使用一种类似用 SQL 语句从数据库查询数据的直观方式来提供一种对 Java 集合运算和表达的高阶抽象。
 * Stream API可以极大提高Java程序员的生产力，让程序员写出高效率、干净、简洁的代码。
 * 这种风格将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。
 * 元素流在管道中经过中间操作（intermediate operation）的处理，最后由最终操作(terminal operation)得到前面处理的结果。
 * /
 * +--------------------+       +------+   +------+   +---+   +-------+
 * | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
 * +--------------------+       +------+   +------+   +---+   +-------+
 * /
 * 以上的流程转换为 Java 代码为：
 * /
 * List<Integer> transactionsIds =
 * widgets.stream()
 *              .filter(b -> b.getColor() == RED)
 *              .sorted((x,y) -> x.getWeight() - y.getWeight())
 *              .mapToInt(Widget::getWeight)
 *              .sum();
 */
public class stream {
    /**
     * 什么是 Stream？
     * Stream（流）是一个来自数据源的元素队列并支持聚合操作（队列啊，那难怪叫做流）
     * /
     * 元素是特定类型的对象，形成一个队列。 Java中的Stream并不会存储元素，而是按需计算。
     * 数据源 流的来源。可以是集合，数组，I/O channel，产生器generator 等。
     * 聚合操作 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。
     * /
     * 和以前的Collection操作不同， Stream操作还有两个基础的特征：
     * Pipelining: 中间操作都会返回流对象本身。
     *          这样多个操作可以串联成一个管道，如同流式风格（fluent style）。
     *          这样做可以对操作进行优化，比如延迟执行(laziness)和短路(short-circuiting)。
     * 内部迭代: 以前对集合遍历都是通过Iterator或者For-Each的方式, 显式的在集合外部进行迭代，这叫做外部迭代。
     *          Stream提供了内部迭代的方式，通过访问者模式(Visitor)实现。
     */

    public static void main(String[] args) {
        //=====================================
        // 创建流
        /**
         * 在 Java 8 中, 集合接口有两个方法来创建流：
         * 1 stream() − 为集合创建串行流。
         * 2 parallelStream() − 为集合创建并行流。
         */
        //=====================================

        List<String> stringsB = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filteredB = stringsB.stream().filter(string -> !string.isEmpty()).toList();

        System.out.println("筛选列表: " + filteredB);

        //=====================================
        // 中间操作
        /**
         * filter() − 通过设置的条件过滤出元素。
         * map() − 通过给定的函数将每个元素转换成新的元素。
         * limit(n) − 截断流，使其元素不超过给定数量。
         * sorted() −  对流进行排序。
         * distinct() − 通过流所生成元素的hashCode()和equals()去除重复元素。
         */
        //=====================================

        // forEach() − 对每个元素执行操作
        // 注意：forEach 是 terminal 操作。执行完此方法后，Stream 流会被“消耗”掉，无法再使用。
        Random randomN = new Random();
        randomN.ints().limit(10).forEach(System.out::println);

        // map() − 映射每个元素到对应的结果
        List<Integer> numbersSL = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> squaresListSL = numbersSL.stream().map( i -> i*i ).toList();

        System.out.println("squaresList: " + squaresListSL);

        // filter() − 通过设置的条件过滤出元素
        List<String> stringsL = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        // 获取空字符串的数量
        long countC = stringsL.stream().filter(String::isEmpty).count();

        System.out.println("countC: " + countC);

        // limit(n) − 截断流，使其元素不超过给定数量
        Random random1 = new Random();
        random1.ints().limit(5).forEach(System.out::println);

        // sorted() − 对流进行排序
        random1.ints().limit(5).sorted().forEach(System.out::println);

        // distinct() − 筛选，通过流所生成元素的hashCode()和equals()去除重复元素
        List<Integer> numsL = Arrays.asList(1, 1, 2, 2, 3, 3);
        numsL.stream().distinct().forEach(System.out::println);

        //=====================================
        // 并行（parallel）程序
        //
        // parallelStream 是流并行处理程序的代替方法。
        //=====================================

        List<String> stringsLA = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        // 获取空字符串的数量
        long countLA = stringsLA.parallelStream().filter(String::isEmpty).count();

        System.out.println("countLA: " + countLA);

        //=====================================
        // Collectors
        /**
         * Collectors 类实现了很多归约操作
         * /
         * Collectors 可用于返回列表或字符串
         */
        //=====================================

        List<String> stringsCL = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        Stream<String> filteredStreamCL = stringsCL.stream().filter(string -> !string.isEmpty());

        // 转换为列表
        List<String> filteredCL = filteredStreamCL.collect(Collectors.toList());    // 难受~

        System.out.println("筛选列表: " + filteredCL);
//        String mergedStringP = filteredStreamCL.collect(Collectors.joining(", "));   // 使用已经使用过的流会提示已被使用
        String mergedStringP = stringsCL.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedStringP);


        //=====================================
        // 统计
        //=====================================

        List<Integer> numbersAC = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics statsS = numbersAC.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + statsS.getMax());
        System.out.println("列表中最小的数 : " + statsS.getMin());
        System.out.println("所有数之和 : " + statsS.getSum());
        System.out.println("平均数 : " + statsS.getAverage());

        //=====================================
        // AllInOne
        //=====================================

        System.out.println("使用 Java 7: ");

        // 计算空字符串
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        System.out.println("列表: " +strings);
        long count = getCountEmptyStringUsingJava7(strings);

        System.out.println("空字符数量为: " + count);
        count = getCountLength3UsingJava7(strings);

        System.out.println("字符串长度为 3 的数量为: " + count);

        // 删除空字符串
        List<String> filtered = deleteEmptyStringsUsingJava7(strings);
        System.out.println("筛选后的列表: " + filtered);

        // 删除空字符串，并使用逗号把它们合并起来
        String mergedString = getMergedStringUsingJava7(strings,", ");
        System.out.println("合并字符串: " + mergedString);
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        // 获取列表元素平方数
        List<Integer> squaresList = getSquares(numbers);
        System.out.println("平方数列表: " + squaresList);
        List<Integer> integers = Arrays.asList(1,2,13,4,15,6,17,8,19);

        System.out.println("列表: " +integers);
        System.out.println("列表中最大的数 : " + getMax(integers));
        System.out.println("列表中最小的数 : " + getMin(integers));
        System.out.println("所有数之和 : " + getSum(integers));
        System.out.println("平均数 : " + getAverage(integers));
        System.out.println("随机数: ");

        // 输出10个随机数
        Random random = new Random();

        for(int i=0; i < 10; i++){
            System.out.println(random.nextInt());
        }

        System.out.println("使用 Java 8: ");
        System.out.println("列表: " +strings);

        count = strings.stream().filter(String::isEmpty).count();
        System.out.println("空字符串数量为: " + count);

        count = strings.stream().filter(string -> string.length() == 3).count();
        System.out.println("字符串长度为 3 的数量为: " + count);

        filtered = strings.stream().filter(string ->!string.isEmpty()).toList();
        System.out.println("筛选后的列表: " + filtered);

        mergedString = strings.stream().filter(string ->!string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        squaresList = numbers.stream().map( i ->i*i).distinct().toList();
        System.out.println("Squares List: " + squaresList);
        System.out.println("列表: " +integers);

        IntSummaryStatistics stats = integers.stream().mapToInt((x) ->x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
        System.out.println("随机数: ");

        random.ints().limit(10).sorted().forEach(System.out::println);

        // 并行处理
        count = strings.parallelStream().filter(String::isEmpty).count();
        System.out.println("空字符串的数量为: " + count);
    }

    private static int getCountEmptyStringUsingJava7(List<String> strings){
        int count = 0;

        for(String string: strings){

            if(string.isEmpty()){
                count++;
            }
        }
        return count;
    }

    private static int getCountLength3UsingJava7(List<String> strings){
        int count = 0;

        for(String string: strings){

            if(string.length() == 3){
                count++;
            }
        }
        return count;
    }

    private static List<String> deleteEmptyStringsUsingJava7(List<String> strings){
        List<String> filteredList = new ArrayList<String>();

        for(String string: strings){

            if(!string.isEmpty()){
                filteredList.add(string);
            }
        }
        return filteredList;
    }

    private static String getMergedStringUsingJava7(List<String> strings, String separator){
        StringBuilder stringBuilder = new StringBuilder();

        for(String string: strings){

            if(!string.isEmpty()){
                stringBuilder.append(string);
                stringBuilder.append(separator);
            }
        }
        String mergedString = stringBuilder.toString();
        return mergedString.substring(0, mergedString.length()-2);
    }

    private static List<Integer> getSquares(List<Integer> numbers){
        List<Integer> squaresList = new ArrayList<>();

        for(Integer number: numbers){
            Integer square = number * number;

            if(!squaresList.contains(square)){
                squaresList.add(square);
            }
        }

        return squaresList;
    }

    private static int getMax(List<Integer> numbers){
        int max = numbers.getFirst();

        for(int i=1;i < numbers.size();i++){

            Integer number = numbers.get(i);

            if(number > max){
                max = number;
            }
        }
        return max;
    }

    private static int getMin(List<Integer> numbers){
        int min = numbers.getFirst();

        for(int i=1;i < numbers.size();i++){
            Integer number = numbers.get(i);

            if(number < min){
                min = number;
            }
        }
        return min;
    }

    private static int getSum(List<Integer> numbers){
        int sum = numbers.getFirst();

        for(int i=1;i < numbers.size();i++){
            sum += numbers.get(i);
        }
        return sum;
    }

    private static int getAverage(List<Integer> numbers){
        return getSum(numbers) / numbers.size();
    }
}
