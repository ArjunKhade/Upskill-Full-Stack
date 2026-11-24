package org.example.sorting;

import java.util.Arrays;

public class MergeSort {

    public static void main(String[] args){
        int [] arr = new int []{8, 5, 8, 9 ,3 ,1};
        mergeSort(arr);
        for (int a :arr){
            IO.print(a+" ");
        }
    }

    public static void mergeSort(int [] arr){
        if(arr == null || arr.length<= 1) return;
        int start  = 0;
        int end  = arr.length;
        int mid = start + (end - start) / 2;

        int[] left = Arrays.copyOfRange(arr, start, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        mergeSort(left);

        mergeSort(right);

        merge(arr, left, right);

    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i=0, j=0, k=0;

        while (i<left.length && j< right.length){
            if(left[i]<= right[j]){
                arr[k++]= left[i++];
            }else {
                arr[k++] = right[j++];
            }
        }

        while (i<left.length){
            arr[k++] = left[i++];
        }

        while (j<right.length){
            arr[k++] = right[j++];
        }

    }

}
