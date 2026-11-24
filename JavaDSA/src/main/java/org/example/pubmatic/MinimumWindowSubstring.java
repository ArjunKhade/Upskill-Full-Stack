package org.example.pubmatic;

import java.io.*;

public class MinimumWindowSubstring {
    public static String solve(String input) {
        String[] lines = input.split("\\r?\\n");
        String s = lines.length > 0 ? lines[0].trim() : "";
        String t = lines.length > 1 ? lines[1].trim() : "";

        int n = s.length(), m = t.length();
        if (m == 0) return "0";
        if (m > n) return "-1";

        int[] need = new int[65536];
        for (int i = 0; i < m; i++) need[t.charAt(i)]++;

        int missing = m;          // characters (with multiplicity) still needed
        int best = Integer.MAX_VALUE;
        int left = 0;

        for (int right = 0; right < n; right++) {
            char c = s.charAt(right);
            if (need[c] > 0) missing--;
            need[c]--;

            // shrink window while it is valid
            while (missing == 0) {
                best = Math.min(best, right - left + 1);
                char lc = s.charAt(left);
                need[lc]++;
                if (need[lc] > 0) missing++;
                left++;
            }
        }
        return best == Integer.MAX_VALUE ? "-1" : String.valueOf(best);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in)
        );

        String s = br.readLine();
        String t = br.readLine();

        String input = s + "\n" + t;

        System.out.println(solve(input));

//        ADOBECODEBANC
//        ABC
    }
}