package org.example.javastreams;

import java.util.*;
import java.util.stream.Collectors;

public class FreqOfEachWord {
    public static void main(String[] args) {

        String str = "java spring java angular spring java";

        Map<String, Long> frequency = Arrays.stream(str.split("\\s+"))
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()
                ));

        System.out.println(frequency);
    }
}