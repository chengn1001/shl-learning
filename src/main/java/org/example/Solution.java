package org.example;

import java.util.*;

/**
 * 【题目】Sheldon 买书（SHL 机考，Java 8，主类名必须为 Solution）
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
 *   两类分别做 0/1 背包：dp[c] = 预算 c 内"至少选一本"的最大星级（不可达记为 NEG）。
 *   再枚举恐怖书占用的预算 c，合并 h[c] + s[amount - c] 取最大。
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
public class Solution {
    static final long NEG = Long.MIN_VALUE / 4;

    // dp[c] = max rating using at least one book with total price <= c; NEG if impossible
    static long[] knap(int[][] books, int cap) {
        long[] dp = new long[cap + 1];
        Arrays.fill(dp, NEG);
        for (int[] b : books) {
            int r = b[0], p = b[1];
            for (int c = cap; c >= p; c--) {
                long base = Math.max(dp[c - p], 0);
                if (base + r > dp[c]) dp[c] = base + r;
            }
        }
        return dp;
    }

    public static long maxRatingBooks(int amount, int[][] horrorBooks, int[][] sciFiBooks) {
        long[] h = knap(horrorBooks, amount), s = knap(sciFiBooks, amount);
        long ans = -1;
        for (int c = 0; c <= amount; c++)
            if (h[c] >= 0 && s[amount - c] >= 0) ans = Math.max(ans, h[c] + s[amount - c]);
        return ans;
    }

    static int[][] read(Scanner in) {
        int n = in.nextInt(), m = in.nextInt();
        int[][] g = new int[n][m];
        for (int[] row : g) for (int j = 0; j < m; j++) row[j] = in.nextInt();
        return g;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int amount = in.nextInt();
        int[][] horror = read(in), sciFi = read(in);
        System.out.println(maxRatingBooks(amount, horror, sciFi));
    }
}
