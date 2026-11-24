package org.example.javastreams;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class FindDuplicatesAndUnique {

    public static void main(String[] args) {

        List<Integer> nums = Arrays.asList(2, 5, 6, 7, 8, 9, 8, 7, 9);
        findDuplicatesAndUnique(nums);

    }

    public static void findDuplicatesAndUnique(List<Integer> nums) {

        Set<Integer> seen = new HashSet<>();

        Set<Integer> set = nums.stream()
                .filter(n -> !seen.add(n))
                .collect(Collectors.toSet());

        System.out.println(set);


        Set<Integer> set1 = nums.stream()
                .distinct()
                .collect(Collectors.toSet());

        System.out.println(set1);

    }
}
