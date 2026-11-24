package org.example.mystack;

import java.util.Arrays;

public class MyStack {

    int [] arr;
    int top;
    int size;

    public MyStack(){
        this.top = 0;
        this.size = 0;
        this.arr = new int[5];
    }

    //push pop peek
    public void push(int data){
        if(this.isFull()){
            throw  new RuntimeException("Stack is Full!!!");
        }
        arr[top++] = data;
        size++;
    }

    public int pop(){
        if(this.isEmpty()){
            throw new RuntimeException("Stack is Empty!!!");
        }

        int data = arr[--top];
        size--;
        return data;
    }

    public int peek(){
        if(!this.isEmpty()){
            return arr[top-1];
        }else{
            throw  new RuntimeException("Stack is Empty!");
        }
    }

    public boolean isEmpty(){
        return  size == 0;
    }

    public boolean isFull(){
        return size == arr.length;
    }

    public void print(){
        System.err.println(Arrays.toString(Arrays.copyOf(arr, size)));
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack();
        stack.push(23);
        stack.push(24);
        stack.push(23);
        stack.push(24);
        stack.push(24);
        stack.pop();
        stack.print();
    }
}
