package org.example;

import java.util.Scanner;

/**
 * 【题目】小镇名字 —— 最长公共前缀（SHL 机考，Java 8，主类名必须为 Solution）
 * 本文件为通用语法版本（索引 for 循环、逐行声明、全部带花括号），提交时请将类名改回 Solution 并删除 package 行。
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
 *   以第一个名字为基准，维护公共前缀长度 prefixLength；依次与后面每个名字逐字符比较（统一转小写），
 *   不相等或到达任一字符串末尾即停止，prefixLength 只会单调减小。总复杂度 O(所有字符总数)。
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
public class Solution_20261024_V2 {

    public static String getTown(String[] peopleNames) {
        if (peopleNames.length == 0) {
            return "";
        }

        String first = peopleNames[0];
        int prefixLength = first.length();

        for (int i = 1; i < peopleNames.length; i++) {
            String current = peopleNames[i];
            int j = 0;
            while (j < prefixLength && j < current.length()) {
                char a = Character.toLowerCase(first.charAt(j));
                char b = Character.toLowerCase(current.charAt(j));
                if (a != b) {
                    break;
                }
                j++;
            }
            prefixLength = j;
            if (prefixLength == 0) {
                break;
            }
        }

        String prefix = first.substring(0, prefixLength);
        return prefix.toLowerCase();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int peopleNamesSize = in.nextInt();
        String[] peopleNames = new String[peopleNamesSize];
        for (int idx = 0; idx < peopleNamesSize; idx++) {
            peopleNames[idx] = in.next();
        }

        String result = getTown(peopleNames);
        System.out.print(result);

        in.close();
    }
}
