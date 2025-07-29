package deque;

public class LinkedListDeque <T> implements Deque<T>{
    /* method needed: add, remove, get, size, empty deque, get(recursive)*/
    public class circularNode{
        public T item;
        public circularNode next;
        public circularNode prev;
        public circularNode(T i, circularNode n, circularNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }
    private circularNode sentinel;
    private int size = 0;

    /* empty circularNode*/
    public LinkedListDeque (){
        sentinel = new circularNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }
    /* add method include front and back, no looping or recursion */
    public void addLast(T element){
        /* end == true means back, false means front*/
        circularNode tmp = new circularNode(element, null, null);
        if(size == 0){
            addEmptyDeque(element);
            return;
        }
        tmp.prev = sentinel.prev;
        sentinel.prev.next = tmp;
        tmp.next = sentinel;
        sentinel.prev = tmp;
        size += 1;
    }

    public void addFirst(T element){
        circularNode tmp = new circularNode(element, null, null);
        if(size == 0){
            addEmptyDeque(element);
            return;
        }
        tmp.next = sentinel.next;
        tmp.prev = sentinel;
        sentinel.next.prev = tmp;
        sentinel.next = tmp;
        tmp = null;
        size += 1;
    }

    public void addEmptyDeque(T element){
        circularNode tmp = new circularNode(element, null, null);
        sentinel.prev = tmp;
        sentinel.next = tmp;
        tmp.next = sentinel;
        tmp.prev = sentinel;
        tmp = null;
        size += 1;
    }

    /* check whether it is empty*/


    /* print out the item of Deque*/
    public void printDeque(){
        circularNode tmp = sentinel;
        while(tmp.next != sentinel){
            System.out.print(tmp.next.item + " ");
            tmp = tmp.next;
        }
        tmp = null;
        System.out.println();
    }
    /* get by using iterator */
    public T get(int i){
        if(i < 0 || i >= size){
            return null;
        }
        circularNode tmp = sentinel.next;
        int count = 0;
        while(count < i){
            tmp = tmp.next;
            count += 1;
        }return tmp.item;
    }

    public T getRecursive(int i){
        if(i < 0 || i >= size){
            return null;
        }
        return getRecursiveHelp(i, sentinel);
    }
    /*helper func for recursion*/
    private T getRecursiveHelp(int i, circularNode s){
        if(i == 0){
            return s.next.item;
        }else{
            return getRecursiveHelp(i - 1, s.next);
        }
    }

    public T removeFirst(){
        if(this.isEmpty()){
            return null;
        }
        circularNode tmp = sentinel.next;
        sentinel.next = tmp.next;
        tmp.next.prev = sentinel;
        size -= 1;
        T x = tmp.item;
        tmp = null;
        return x;
    }

    public T removeLast(){
        if(this.isEmpty()){
            return null;
        }
        circularNode tmp = sentinel.prev;
        sentinel.prev = tmp.prev;
        tmp.prev.next = sentinel;
        size -= 1;
        T x = tmp.item;
        tmp = null;
        return x;
    }

    public int size(){
        return size;
    }

    /*return whether is equal*/
    public boolean equals(Object o){
        if(! (o instanceof LinkedListDeque<?>)){
            return false;
        }
        LinkedListDeque<?> obj = (LinkedListDeque<?>) o;
        if(obj.size() != this.size()){
            return false;
        }
        for(int i = 0; i < this.size(); i += 1){
            if(this.get(i) != obj.get(i)){
                return false;
            }
        }return true;
    }
}
