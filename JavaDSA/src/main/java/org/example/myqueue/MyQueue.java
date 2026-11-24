package org.example.myqueue;

import java.util.Arrays;

public class MyQueue {

    private int[] arr;
    private int front;
    private int rear;
    private int size;

    public MyQueue() {
        this.arr = new int[5];
        this.rear = 0;
        this.front = 0;
        this.size = 0;
    }

    //Adds an element at the rear (end) of the queue.
    public void enqueue(int data){
       if(!isFull()){
           arr[rear] = data;
           rear = (rear+1) % arr.length;
           size++;
       }else {
           throw new RuntimeException("Queue is Full!!!");
       }
    }



    //Removes and returns the front element.
    public int dequeue(){
        if(isEmpty()){
            throw new RuntimeException("Queue is Empty!!!");
        }else {
            int data = arr[front];
            front = (front + 1) % arr.length;
            size--;
            return data;
        }
    }

    //Returns the front element without removing it.
    public int peek(){
        if(!isEmpty()){
            return arr[front];
        }else {
            throw new RuntimeException("Queue is Empty!!!");
        }
    }

    private boolean isFull() {
        return this.size == arr.length;
    }

    public boolean isEmpty(){
     return size == 0;
    }

    public static void main(String[] args){
        MyQueue q = new MyQueue();

        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(8);
//        q.enqueue(9);
        q.dequeue();
        q.print();
    }

    private void print() {
        for (int i = 0; i < size; i++) {
            IO.print(arr[(front + i) % arr.length] + " ");
        }
        IO.println();
    }


}
