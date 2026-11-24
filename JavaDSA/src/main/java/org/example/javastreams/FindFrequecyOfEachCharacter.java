package org.example.javastreams;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FindFrequecyOfEachCharacter {

    public static void main(String[] args) {
        String s = new String("Arjun khade");
        findFrequecyOfEachCharactor((s));
    }

    public static void findFrequecyOfEachCharactor(String s) {
        Map<Character, Long> map = s.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        System.out.println("Approach 1: groupingBy + Function.identity()");
        System.out.println(map);
        System.out.println();


        Map<Character, Long> map1 = s.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        c -> c,
                        Collectors.counting()
                ));

        System.out.println("Approach 2: groupingBy + lambda");
        System.out.println(map1);
        System.out.println();

        Map<Character, Long> map2 = s.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(
                        c -> c,
                        c -> 1L,
                        Long::sum
                ));

        System.out.println("Approach 3 : toMap()");
        System.out.println(map2);
        System.out.println();

        char[] arr = s.toCharArray();
        Map<Character, Integer> charMaps = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            if (charMaps.containsKey(arr[i])) {
                charMaps.put(arr[i], charMaps.get(arr[i]) + 1);
            } else {
                charMaps.put(arr[i], 1);
            }
        }

        System.out.println("Approach 4 : Traditional for loop ");
        System.out.println(charMaps);
    }

}
