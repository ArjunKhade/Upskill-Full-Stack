package org.example.slidingwindow;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringWithoutRepeatingChar {

    public static void main(String[] args) {
        String s = "abcabcbb";
        // Output: 3
        System.out.println(lengthOfLongestSubstring(s));
    }

    public static int lengthOfLongestSubstring(String s) {
        int i = 0;
        int j = 0;
        Map<Character, Integer> map = new HashMap<>();
        char [] arr = s.toCharArray();
        int maxWS = 0;

        for(j=0; j<arr.length; j++){
            if(map.containsKey(arr[j]) && map.get(arr[j]) >= i){
                i = map.get(arr[j])+1;
            }

            map.put(arr[j], j);
            int curWS = j - i + 1;
            maxWS = Math.max(curWS,maxWS);
        }

        return maxWS;
    }
}
