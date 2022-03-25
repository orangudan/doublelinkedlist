package com.dsadeghi;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> implements List<T>{

    private DLLNode head;
    private DLLNode tail;
    private boolean hasMutated;
    /**
     * Gets the size of the linked list
     * @return
     */
    public int size() {
        if (head == null) {
            return 0;
        }
        int size = 0;
        DLLNode temp = head;
        while (temp != null) {
            size++;
            temp = temp.next();
        }

        return size;
    }

    public DLLNode head() {
        return head;
    }

    public DLLNode tail() {
        if (!hasMutated) {
            return tail;
        }

        if (size() == 0 || size() == 1) {
            tail = head;
        } else {
            tail = head;
            while (tail.next != null) {
                tail = tail.next();
            }
        }

        return tail;
    }

    /**
     * Checks if linked list is empty
     * @return
     */
    public boolean isEmpty() {
        return (head == null);
    }

    /**
     * Checks if linked list contains the given object
     * @param o
     * @return
     */
    public boolean contains(Object o) {
        for (DLLNode n = head; n != null; n = n.next()) {
            if (o.equals(n.data)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    //This feels suspect
    public Object[] toArray() {
        Object[] array = new Object[size()];

        DLLNode n = head;
        for (int i = 0; i < size(); i++) {
            array[i] = n;
            n = n.next();
        }

        return array;
    }

    /**
     * Appends the element to the end of the list
     * @param e
     * @return
     */
    public boolean add(T e) {
        ListIterator<T> iter = listIterator(size());
        iter.add(e);
        hasMutated = true;
        return true;
    }

    public boolean remove(Object o) {
        if (indexOf(o) == -1) {
            return false;
        }
        ListIterator<T> iter = this.listIterator(indexOf(o));
        iter.next();
        iter.remove();
        return true;
    }

    public boolean containsAll(Collection<?> c) {

        for (Object object : c) {
            if (!this.contains(object)) {
                return false;
            }
        }

        return true;
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            this.add(t);
        }

        return true;
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        for (T t : c) {
            this.add(index, t);
        }

        return true;
    }

    public boolean removeAll(Collection<?> c) {
        boolean hasRemoved = false;
        for (Object object : c) {
            hasRemoved = this.remove(object);
        }

        return hasRemoved;
    }

    public void clear() {
        head = null;
    }

    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> iter = this.listIterator(index);
        return iter.next();
    }

    public T set(int index, T element) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        ListIterator<T> iter = this.listIterator(index);
        T prevElement = iter.next();
        iter.remove();
        iter.add(element);

        return prevElement;
    }

    public void add(int index, T element) {
        ListIterator<T> iter = this.listIterator(index);
        iter.add(element);
    }

    public T remove(int index) {
        ListIterator<T> iter = this.listIterator(index);
        if (!iter.hasNext()) {
            throw new IndexOutOfBoundsException();
        }

        T returnValue = iter.next();
        iter.remove();

        return returnValue;
    }

    public int indexOf(Object o) {
        ListIterator<T> iter = this.listIterator();
        while (iter.hasNext()) {
            if (o.equals(iter.next())) {
                return iter.nextIndex() - 1; //Is this really the best way to do this?
            }
        }

        return -1;
    }

    public int lastIndexOf(Object o) {
        //Am I supposed to be using DLLIterator here? What was the point of making it if not
        ListIterator<T> iter = this.listIterator(size());
        int index = -1;
        while (iter.hasPrevious()) {
            if (o.equals(iter.previous())) {
                index = iter.nextIndex();
                break;//Is this really the best way to do this?
            }
        }

        return index;
    }

    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return new DLLIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {


        if (fromIndex == toIndex) {
            return new ArrayList<T>();
        }
        if (fromIndex < 0 || fromIndex >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (toIndex < 0 || toIndex >= size()) {
            throw new IndexOutOfBoundsException();

        }

        ArrayList<T> list = new ArrayList<>();

        ListIterator<T> iter = this.listIterator(fromIndex);
        while (iter.nextIndex() != toIndex + 1) {
            list.add(iter.next());
        }

        return list;
    }

    public String toString() {
        String str = "[";

        for (int i = 0; i < size(); i++) {
            str += get(i);
            if (i != size() - 1) {
                str += ", ";
            }
        }
        str += "]";
        return str;
    }





    private class DLLNode {

        /*
            FIELDS
        */
        public T data;
        private DLLNode next;
        private DLLNode prev;


        /*
            CONSTRUCTORS
        */

        public DLLNode(DLLNode prev, T data, DLLNode next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public DLLNode(T data) {
            this.data = data;
        }

        /**
         * Returns the reference to the next node linked to the calling node
         * @return The next node
         */
        public DLLNode next() {
            return next;
        }

        /**
         * Returns the reference to the previous node linked to the calling node
         * @return The previous node
         */
        public DLLNode prev() {
            return prev;
        }
    }

    /**
     * Access fields using methods when available.
     * Invariant: Iterator's index must not
     */
    private class DLLIterator implements ListIterator<T> {
        //Holds the reference to the next element
        private DLLNode nextNode;
        //Whenever a method returns a node, it is cached in this variable
        private DLLNode cachedNode;
        private int index;
        private boolean isIncremented;

        //Whenever the size of the array is changed,
        // private boolean sizeChanged;

        public DLLIterator() {
            nextNode = head;
            index = 0;
            cachedNode = null;
        }

        //Constructs an iterator positioned just before the element at the given index
        public DLLIterator(int i) {
            if (i < 0 || i > size()) {
                throw new IndexOutOfBoundsException();
            }

            nextNode = head;
            //Loop uses index to also handle incrementing it
            for (index = 0; index < i; index++) {
                nextNode = nextNode.next();
            }
        }

        @Override
        public boolean hasNext() {
            return (nextNode != null);
        }

        public int index() {
            return index;
        }

        /**
         * Advances the iterator's index by 1 and returns the next node that it passes over
         * @return
         */
        @Override
        public T next() {
            //Check if a next node exists first
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next node");
            }

            cachedNode = nextNode;
            nextNode = nextNode.next();
            index++;
            isIncremented = true;
            return cachedNode.data;
        }

        @Override
        public boolean hasPrevious() {
            return (index != 0/*(nextNode == null && size() != 0) || nextNode.prev() != null*/);
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("There is no previous node");
            }
            if (!hasNext()) {
                cachedNode = tail();
            } else {
                cachedNode = nextNode.prev();
            }
            nextNode = cachedNode;
            index--;
            isIncremented = false;
            return cachedNode.data;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        /**
         * Only assign head and nextNode;
         */
        @Override
        public void remove() {
            tail();
            if (cachedNode == null) {
                throw new IllegalStateException("Remove() must be preceded by next() or previous()");
            }

            if (size() == 1) {                    //Single node case
                head = null;
                nextNode = null;
                index = 0;
            } else if (cachedNode == head()) {    //Head node case
                nextNode = cachedNode.next();
                nextNode.prev = null;
                head = nextNode;
                if (isIncremented) {
                    index--;
                }
            } else if (cachedNode == tail()) {    //Tail node case
                cachedNode.prev.next = null;
                if (isIncremented) {
                    index--;
                }
            } else {                               //Middle node case
                cachedNode.prev.next = cachedNode.next();
                cachedNode.next.prev = cachedNode.prev();
                if (isIncremented) {
                    index--;
                }
            }

            cachedNode = null;
        }

        @Override
        public void set(T t) {
            if (cachedNode == null) {
                throw new IllegalStateException("set() must be preceded by next() or previous()");
            }

            cachedNode.data = t;
            cachedNode = null; //Maybe?
        }

        /**
         * Inserts the element in a new node immediately after where the iterator is indexed at
         * @param t
         */
        @Override
        public void add(T t) {
            tail();

            if (isEmpty()) {                          // If list is empty
                head = new DLLNode(t);
            } else if (nextNode == head) {                // Else if we are at the head of the list
                DLLNode newNode = new DLLNode(t);
                newNode.next = nextNode;
                nextNode.prev = newNode;
                newNode.prev = null;
                head = newNode;
            } else if (nextNode == null) {                    // Else if we are at the tail of the list
                DLLNode newNode = new DLLNode(t);
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            } else {
                DLLNode newNode = new DLLNode(t);
                newNode.prev = nextNode.prev;
                nextNode.prev.next = newNode;
                newNode.next = nextNode;
                nextNode.prev = newNode;
            }

            index++;
            cachedNode = null;
        }

    }

    @Override
    public <E> E[] toArray(E[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }
}
