package org.example.javastreams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FindEvenAndMultiply {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 5, 4, 2, 6, 8, 9);
        findEvenAndMultiply(list);
    }


    public static void findEvenAndMultiply(List<Integer> list) {

        List<Integer> nums = list.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .collect(Collectors.toList());

        System.out.println(nums);
    }

}