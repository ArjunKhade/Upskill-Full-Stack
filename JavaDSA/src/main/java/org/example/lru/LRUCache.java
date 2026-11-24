package org.example.lru;

import java.util.HashMap;
import java.util.Map;

 class Node{
    Node prev;
    Node next;
    int key;
    int value;
    public Node(int key, int value){
        this.key = key;
        this.value = value;
    }
}

public class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> cache;
    private final Node head;
    private final Node tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();

        this.head = new Node(0,0);
        this.tail = new Node(0,0);

        head.next = tail;
        tail.prev = head;
    }


    public int get(int key){
        // if cache does not contains key
        if(!this.cache.containsKey(key)){
            return -1;
        }
        //cache has key
        Node node = cache.get(key);
        //remove node from current position
        remove(node);
        //add to front to mark as most recent used
        addTOFront(node);
        return node.value;
    }

    public void put(int key, int value){

        if(cache.containsKey(key)){
            Node node = cache.get(key);
            node.value = value;
            remove(node);
            addTOFront(node);
            return;
        }

        Node node = new Node(key, value);
        cache.put(key, node);
        addTOFront(node);

        if (cache.size()> capacity){
            Node leastRecentUsed = tail.prev;
            remove(leastRecentUsed);
            cache.remove(leastRecentUsed.key);
        }

    }

    private void addTOFront(Node node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next =node;
    }

    private void remove(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public static void main(String[] args) {

        LRUCache cache = new LRUCache(2);

        cache.put(1, 100);
        cache.put(2, 200);
        cache.put(3, 300);
        System.out.println(cache.get(1));
        cache.put(4, 400);
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}
