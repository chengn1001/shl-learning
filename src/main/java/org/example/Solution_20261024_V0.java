package org.example;

import java.util.Scanner;

/**
 * 【题目】Sheldon 买书（SHL 机考，Java 8，主类名必须为 Solution）
 * 本文件为通用语法版本（索引 for 循环、逐行声明、全部带花括号），提交时请将类名改回 Solution 并删除 package 行。
 *
 * Sheldon 去书展，书分为 Horror（恐怖）和 Sci-fi（科幻）两类，每本书有星级和价格。
 * 他必须在两类中各至少买一本，总价不能超过手里的钱 amount，目标是使所买书的星级总和最大。
 * 如果无法做到两类各买至少一本，输出 -1。
 *
 * 【输入】
 *   第 1 行：amount —— Sheldon 的钱
 *   第 2 行：numHorror numH —— 恐怖书的数量，以及每本书的字段数（恒为 2）
 *   接下来 numHorror 行：hrating hprice —— 每本恐怖书的星级和价格
 *   再 1 行：numSciFi numS —— 科幻书的数量，以及每本书的字段数（恒为 2）
 *   接下来 numSciFi 行：srating sprice —— 每本科幻书的星级和价格
 *
 * 【输出】
 *   一个整数：最大星级总和；买不齐两类则输出 -1
 *
 * 【约束】
 *   1 <= numHorror, numSciFi <= 1000
 *   1 <= amount <= 10^5
 *   1 <= hrating, srating <= 10^6
 *   1 <= hprice, sprice <= 10^5
 *   numH = numS = 2
 *
 * 【思路】
 *   两类分别做 0/1 背包：dp[c] = 预算 c 内"至少选一本"的最大星级（不可达记为 IMPOSSIBLE）。
 *   再枚举恐怖书占用的预算 c，合并 horrorDp[c] + sciFiDp[amount - c] 取最大。
 *   复杂度 O((numHorror + numSciFi) * amount) ≈ 2×10^8，星级总和最大 2×10^9，用 long 防溢出。
 *
 * 【测试用例】
 *   1) 题目样例
 *      输入：
 *        50
 *        3 2
 *        5 10
 *        3 30
 *        6 20
 *        3 2
 *        6 30
 *        4 30
 *        2 10
 *      输出：13
 *      说明：恐怖书选第 1、3 本（5+6，花 30），科幻书选第 3 本（2，花 10），总价 40 <= 50
 *
 *   2) 极小边界：每类各 1 本，买得起
 *      输入：
 *        10
 *        1 2
 *        5 4
 *        1 2
 *        3 6
 *      输出：8
 *
 *   3) 极小边界：每类各 1 本，买不起
 *      输入：
 *        9
 *        1 2
 *        5 4
 *        1 2
 *        3 6
 *      输出：-1
 *
 *   4) 恰好花完预算
 *      输入：
 *        40
 *        2 2
 *        5 10
 *        6 20
 *        1 2
 *        6 10
 *      输出：17
 *
 *   5) 极大值 / 溢出边界：两类各 1000 本，星级均为 10^6，价格均为 1，amount = 10^5
 *      输出：2000000000（全部买下，超过 int 不安全范围，需 long）
 *
 *   6) 极大值：两类各 1000 本，价格均为 10^5，amount = 10^5
 *      输出：-1（买一本就花光，无法两类各买一本）
 */
public class Solution_20261024_V0 {

    // 表示"不可达"（该预算下一本书都买不到）的哨兵值。
    // 取一个足够小的负数即可：既不会和合法星级（>= 1）混淆，加上星级后也不会溢出。
    private static final long IMPOSSIBLE = -1000000000000L;

    /**
     * 对单一类别的书做 0/1 背包。
     * dp[c] = 预算不超过 c 时，至少买一本书能得到的最大星级总和；
     * 若预算 c 内一本都买不起，则 dp[c] = IMPOSSIBLE。
     *
     * @param books    该类别的书，每行为 {星级, 价格}
     * @param capacity 背包容量，即总预算 amount
     * @return 长度为 capacity + 1 的 dp 数组
     */
    private static long[] knapsack(int[][] books, int capacity) {
        // 初始状态：一本书都没选，所有预算下都是"不可达"
        long[] dp = new long[capacity + 1];
        for (int c = 0; c <= capacity; c++) {
            dp[c] = IMPOSSIBLE;
        }

        // 逐本书做决策：买 or 不买
        for (int i = 0; i < books.length; i++) {
            int rating = books[i][0];
            int price = books[i][1];
            // 0/1 背包必须倒序遍历预算，保证每本书在同一轮只被使用一次
            for (int c = capacity; c >= price; c--) {
                // 买下这本书之前，剩余预算 c - price 能得到的最优星级
                long previous = dp[c - price];
                if (previous < 0) {
                    previous = 0; // 之前一本都没选也合法（这本书就是"至少一本"）
                }
                // 买下这本书后的候选星级，比当前更优则更新
                long candidate = previous + rating;
                if (candidate > dp[c]) {
                    dp[c] = candidate;
                }
            }
        }
        return dp;
    }

    /**
     * 计算在预算 amount 内，恐怖、科幻两类各至少买一本时的最大星级总和；无解返回 -1。
     */
    public static long maxRatingBooks(int amount, int[][] horrorBooks, int[][] sciFiBooks) {
        // 两类书互不影响，分别做背包
        long[] horrorDp = knapsack(horrorBooks, amount);
        long[] sciFiDp = knapsack(sciFiBooks, amount);

        // 枚举恐怖书占用的预算 c，科幻书使用剩余预算 amount - c
        long answer = -1;
        for (int c = 0; c <= amount; c++) {
            long horrorBest = horrorDp[c];
            long sciFiBest = sciFiDp[amount - c];
            // 两边都必须真的买到了书（>= 0），才是合法方案
            if (horrorBest >= 0 && sciFiBest >= 0) {
                long total = horrorBest + sciFiBest;
                if (total > answer) {
                    answer = total;
                }
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 第 1 行：预算
        int amount = in.nextInt();

        // 第 2 行：恐怖书数量 numHorror 与每行字段数 numH（恒为 2），随后 numHorror 行 {星级, 价格}
        int horrorRows = in.nextInt();
        int horrorCols = in.nextInt();
        int[][] horrorBooks = new int[horrorRows][horrorCols];
        for (int i = 0; i < horrorRows; i++) {
            for (int j = 0; j < horrorCols; j++) {
                horrorBooks[i][j] = in.nextInt();
            }
        }

        // 接着：科幻书数量 numSciFi 与字段数 numS（恒为 2），随后 numSciFi 行 {星级, 价格}
        int sciFiRows = in.nextInt();
        int sciFiCols = in.nextInt();
        int[][] sciFiBooks = new int[sciFiRows][sciFiCols];
        for (int i = 0; i < sciFiRows; i++) {
            for (int j = 0; j < sciFiCols; j++) {
                sciFiBooks[i][j] = in.nextInt();
            }
        }

        // 计算并输出结果
        long result = maxRatingBooks(amount, horrorBooks, sciFiBooks);
        System.out.println(result);

        in.close();
    }
}
