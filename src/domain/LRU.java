package domain;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    class DoubleNode{
        int key, val;
        DoubleNode prev, next;
        public DoubleNode(){}
        public DoubleNode(int key, int val){
            this.key = key;
            this.val = val;
        }
    }
    private void remove(DoubleNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
    }
    private void addToLast(DoubleNode node){
        node.next = dummyLast;
        node.prev = dummyLast.prev;
        dummyLast.prev.next = node;
        dummyLast.prev = node;
    }

    DoubleNode dummyHead, dummyLast;//虚假的队首(最久未使用)及队尾（最近使用）
    Map<Integer, DoubleNode> cache;
    int capacity;
    public LRUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        dummyHead = new DoubleNode();
        dummyLast = new DoubleNode();
        dummyHead.next = dummyLast;
        dummyLast.prev = dummyHead;
    }

    public int get(int key) {
        if(!cache.containsKey(key)){
            return -1;
        }
        DoubleNode node = cache.get(key);
        remove(node);//从双向链表中删除
        addToLast(node);//重新加入双向链表的队尾表示最近使用过
        return node.val;
    }

    public void put(int key, int value) {
        if(cache.containsKey(key)){//替换
            DoubleNode oldNode = cache.get(key);
            remove(oldNode);
            oldNode.val = value;
            addToLast(oldNode);
            cache.put(key, oldNode);
        } else {//加入
            if(cache.size()==capacity){
                //删除
                DoubleNode delNode = dummyHead.next;
                remove(delNode);
                cache.remove(delNode.key);
                //新增
                DoubleNode newNode = new DoubleNode(key, value);
                addToLast(newNode);
                cache.put(key, newNode);
            }
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
