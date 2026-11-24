package org.example.strings;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReverseWords {

    public static void main(String[] args){
        String s  = "the sky  is blue";
        System.out.println(reverseString(s));
        System.out.println(reverseStringUsingStream(s));
    }

    public static String reverseString(String s){

        String [] words = s.trim().split("\\s+");

        StringBuilder builder = new StringBuilder();

        for (int i = words.length-1; i >= 0 ; i--) {
          builder.append(words[i]);

          if(i != 0){
              builder.append(" ");
          }
        }
        return  builder.toString();
    }

    public static String reverseStringUsingStream(String s){
        String [] words = s.trim().split("\\s+");
        return IntStream.range(0, words.length)//transforms one integer into another integer.
                .mapToObj( i -> words[words.length - 1 - i]) //transforms an integer into an object, such as a String.
                .collect(Collectors.joining(" "));//This joins all the words into one String, adding a space between each word.
    }
}
