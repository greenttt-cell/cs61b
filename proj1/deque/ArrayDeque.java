package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] item;
    private int nextFirst;
    private int nextLast;
    private int size;
    private final int factor = 2;
    //private float lbf = 0.25f;

    public ArrayDeque() {
        item = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }
    @Override
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int start = (nextFirst + 1) % item.length;
        for (int i = 0; i < size; i += 1) {
            a[i] = item[(start + i) % item.length];
        }
        nextFirst = capacity - 1;
        nextLast = size;
        item = a;
    }
    @Override
    public void addFirst(T element) {
        if ((double) size / item.length > 0.5) {
            resize(factor * item.length);
        }
        item[nextFirst] = element;
        nextFirst = (nextFirst - 1 + item.length) % item.length;
        size += 1;
    }
    @Override
    public void addLast(T element) {
        if ((double) size / item.length > 0.5) {
            resize(factor * item.length);
        }
        item[nextLast] = element;
        nextLast = (nextLast + 1) % item.length;
        size += 1;
    }
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i += 1) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        int index = (nextFirst + 1) % item.length;
        T a = item[index];
        item[index] = null;
        nextFirst = index;
        size -= 1;
        if (((double) size / item.length) < 0.25 && item.length > 16 && size > 0) {
            resize(Math.round(((float) item.length / factor)));
        }
        return a;
    }
    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        int index = (nextLast - 1 + item.length) % item.length;
        T a = item[index];
        item[index] = null;
        size -= 1;
        nextLast = index;
        if (((float) size / item.length) < 0.25 && item.length > 16 && size > 0) {
            resize(Math.round(((float) item.length / factor)));
        }
        return a;
    }
    @Override
    public T get(int i) {
        if (this.isEmpty()) {
            return null;
        }
        int index = (nextFirst + 1 + i) % item.length;
        return item[index];
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque<?>)) {
            return false;
        }
        Deque<?> other = (Deque<?>) o;
        if (other.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i += 1) {
            Object thisitem = this.get(i);
            Object otheritem = other.get(i);
            if (thisitem == null && otheritem != null) {
                return false;
            }
            if (thisitem != null && !thisitem.equals(otheritem)) {
                return false;
            }
        } return true;
    }


    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int order;

        ArrayDequeIterator() {
            order = 0;
        }

        public boolean hasNext() {
            return order < size;
        }

        public T next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            T item1 = get(order);
            order += 1;
            return item1;
        }
    }
}
