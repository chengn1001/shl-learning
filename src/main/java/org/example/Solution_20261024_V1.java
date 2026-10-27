package org.example;

import java.util.*;

/**
 * 【题目】小镇名字 —— 最长公共前缀（SHL 机考，Java 8，主类名必须为 Solution）
 * 本文件为语法精简版，提交时请将类名改回 Solution 并删除 package 行。
 *
 * 一个州里有 N 个人，每个人都把所在小镇的名字拼接在自己名字的前面。
 * 给定 N 个人的名字，求他们所在小镇的名字：即所有名字的最长公共前缀。
 * 名字大小写不敏感，输出全部用小写字母；若不存在公共前缀，则不输出任何内容。
 *
 * 【输入】
 *   第 1 行：size —— 人数 N
 *   第 2 行：N 个用空格分隔的字符串 —— 每个人的名字
 *
 * 【输出】
 *   一个小写字符串：最长公共前缀；若无公共前缀则什么都不打印
 *
 * 【约束】
 *   0 <= size <= 10^5
 *   0 < len < 10^5（len 为每个字符串的长度）
 *   名字由英文字母组成，大小写不敏感
 *
 * 【思路】
 *   以第一个名字为基准，维护公共前缀长度 len；依次与后面每个名字逐字符比较（统一转小写），
 *   不相等或到达任一字符串末尾即停止，len 只会单调减小。总复杂度 O(所有字符总数)。
 *   N = 0 时不读取第二行，直接输出空。
 *
 * 【测试用例】
 *   1) 题目样例
 *      输入：
 *        5
 *        Rosewood rose rosy rosemary roshh
 *      输出：ros
 *
 *   2) 极小边界：N = 1
 *      输入：
 *        1
 *        Hello
 *      输出：hello
 *
 *   3) 极小边界：N = 0
 *      输入：
 *        0
 *      输出：（空）
 *
 *   4) 无公共前缀
 *      输入：
 *        3
 *        abc xyz abd
 *      输出：（空）
 *
 *   5) 大小写混合
 *      输入：
 *        3
 *        ROSE rose RoSeWood
 *      输出：rose
 *
 *   6) 极大值：N = 10^5，每个名字均为 "abcdefghij"
 *      输出：abcdefghij
 *
 *   7) 极大值：N = 2，两个长度为 99999 的相同字符串 "aaa...a"
 *      输出：99999 个 a
 */
public class Solution_20261024_V1 {

    /** 返回所有名字的最长公共前缀（小写）；无公共前缀或无名字时返回空串。 */
    public static String getTown(String[] peopleNames) {
        if (peopleNames.length == 0) return "";             // N = 0：没有人，直接返回空
        String first = peopleNames[0];                      // 以第一个名字为比较基准
        int len = first.length();                           // 当前公共前缀长度，初始为基准全长
        for (String s : peopleNames) {
            int j = 0;
            // 逐字符比较（忽略大小写），遇到不同或任一字符串结束即停止
            while (j < len && j < s.length()
                    && Character.toLowerCase(first.charAt(j)) == Character.toLowerCase(s.charAt(j))) j++;
            len = j;                                        // 公共前缀只会越比越短
        }
        return first.substring(0, len).toLowerCase();       // 题目要求输出小写
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();                               // 第 1 行：人数 N
        String[] names = new String[n];
        for (int i = 0; i < n; i++) names[i] = in.next();   // 第 2 行：N 个名字（N = 0 时不会读取）
        System.out.print(getTown(names));                   // 与模板一致，用 print 不换行
    }
}
