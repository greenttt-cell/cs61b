package bstmap;

import edu.princeton.cs.algs4.BST;

import java.security.Key;
import java.util.*;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{

    private class BSTNode{
        private K key;
        private V value;
        private BSTNode left, right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private  BSTNode node;
    private int size = 0;

    // emptyBSTMap
    public BSTMap(){
        node = null;
        size = 0;
    }
    @Override
    public void clear() {
        node = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelp(key, node);
    }
    private boolean containsKeyHelp(K key, BSTNode n){
        if (n == null) {
           return false;
        }
        int result = n.key.compareTo(key);
        if (result == 0) {
            return true;
        }
        else {
            if (result > 0) {
                return containsKeyHelp(key, n.left);
            } else {
                return containsKeyHelp(key, n.right);
            }
        }
    }

    @Override
    public V get(K key) {
        return getHelp(key, node);
    }
    private V getHelp(K key, BSTNode n) {
        if (n == null) {
            return null;
        }
        if (n.key.compareTo(key) == 0) {
            return n.value;
        }
        else if (n.key.compareTo(key) > 0){
            return getHelp(key, n.left);
        } else {
            return getHelp(key, n.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        node = putHelp(key, value, node);
        size += 1;
    }
    private BSTNode putHelp(K key, V value, BSTNode n) {
        if (n == null) {
            return new BSTNode(key, value);
        }
        if (n.key.compareTo(key) > 0) {
            n.left = putHelp(key, value, n.left);
        } else {
            n.right = putHelp(key, value, n.right);
        } return n;
    }

    @Override
    public Set<K> keySet() {
        return keySetHelp(node);
    }
    private Set<K> keySetHelp(BSTNode n) {
        Set<K> result = new TreeSet<>();
        if (n == null) {
            return result;
        }
        result.add(n.key);
        result.addAll(keySetHelp(n.right));
        result.addAll(keySetHelp(n.left));
        return result;
    }

    @Override
    public V remove(K key) {
        V value =  getHelp(key, node);
        if (value != null) {
            size -= 1;
        }
        node = removeHelp(key, node);
        return value;
    }

    private BSTNode removeHelp(K key, BSTNode n) {
        if (!containsKeyHelp(key, n)) {
            return n;
        }
        int result = n.key.compareTo(key);
        if (result == 0) {
            if (n.left == null) {
                return n.right;
            } else if (n.right == null) {
                return n.left;
            } else {
                BSTNode changeNode = getBSTNode(key, n.left);
                n.key = changeNode.key;
                n.value = changeNode.value;
                removeHelp(changeNode.key, changeNode);
                return n;
            }
        } else if (result > 0) {
            n.left = removeHelp(key, n.left);
        }else{
            n.right =  removeHelp(key, n.right);
        } return n;
    }
    private BSTNode getBSTNode(K key, BSTNode n) {
        BSTNode tmp = n;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        return tmp;
    }

    @Override
    public V remove(K key, V value) {
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public void printInOrder() {
        printInOrderHelp(node);
    }
    private void printInOrderHelp(BSTNode n) {
        if (n == null) {
            return ;
        }
        printInOrderHelp(n.left);
        System.out.println(n.key);
        printInOrderHelp(n.right);
    }
}


