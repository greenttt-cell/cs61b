package deque;

import java.util.Iterator;

public class ArrayDeque <T> implements Deque<T>{
    private T[] item;
    private int nextFirst;
    private int nextLast;
    private int size;
    private final int factor = 2;
    //private float lbf = 0.25f;

    public ArrayDeque(){
        item = (T[]) new Object[3];
        nextFirst = 1;
        nextLast = 2;
        size = 0;
    }
    @Override
    public int size(){
        return size;
    }

    private void resize(int capacity){
        T[] a = (T[]) new Object[capacity];
        int x = size - nextFirst - 1;
        if(nextLast == nextFirst + 1){
            System.arraycopy(item,nextFirst + 1 , a, 1, x);
            System.arraycopy(item, 0, a, x + 1, nextFirst + 1 );
        }else{
            System.arraycopy(item, 0, a, 1, size);
        }
        nextFirst = 0;
        nextLast = size + 1;
        item = a;
    }
    @Override
    public void addFirst(T element){
        if(nextFirst >= 0 && size < item.length){
            item[nextFirst] = element;
            nextFirst -= 1;
            size += 1;
            return;
        }
        if(size < item.length){
            item[item.length - 1] = element;
            nextFirst = item.length - 2;
            size += 1;
        }else{
            resize(factor * size);
            item[nextFirst] = element;
            nextFirst = item.length - 1;
            size += 1;
        }
    }
    @Override
    public void addLast(T element){
        if(size < item.length && nextLast < item.length){
            item[nextLast] = element;
            size += 1;
            nextLast += 1;
            return;
        }
        if(size < item.length){
            item[0] = element;
            nextLast = 1;
            size += 1;
        }else{
            resize(factor * size);
            item[nextLast] = element;
            nextLast += 1;
            size += 1;
        }
    }
    @Override
    public void printDeque(){
        if(nextFirst + 1 >= nextLast){
            for(int i = nextFirst + 1; i < item.length; i += 1){
                System.out.print(item[i] + " ");
            }
            for(int i = 0; i < nextLast; i += 1){
                System.out.print(item[i] + " ");
            }
        }else{
            for(int i = nextFirst + 1; i < nextLast; i += 1){
                System.out.print(item[i] + " ");
            }
        }
        System.out.println();
    }
    @Override
    public T removeFirst(){
        if(this.isEmpty()){
            return null;
        }
        int index = (nextFirst + 1) % item.length;
        T a = item[index];
        item[index] = null;
        nextFirst = index;
        size -= 1;
        return a;
    }
    @Override
    public T removeLast(){
        if(this.isEmpty()){
            return null;
        }
        int index = nextLast - 1;
        T a = item[index];
        item[index] = null;
        size -= 1;
        nextLast = index;
        return a;
    }
    @Override
    public T get(int i){
        if(this.isEmpty()){
            return null;
        }
        int index = (nextFirst + 1 + i) % item.length;
        return item[index];
    }
    @Override
    public boolean equals(Object o){
        if(! (o instanceof ArrayDeque<?>)){
            return false;
        }
        ArrayDeque<?> obj = (ArrayDeque<?>) o;
        if(obj.size() != size){
            return false;
        }
        for(int i = 0; i < size; i += 1){
            if(this.get(i) != obj.get(i)){
                return false;
            }
        }return true;
    }


    public Iterator<T> iterator(){
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        public int order;

        public ArrayDequeIterator() {
            order = 0;
        }

        public boolean hasNext() {
            return order < size;
        }

        public T next() {
            T item = get(order);
            order += 1;
            return item;
        }
    }