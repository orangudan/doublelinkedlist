package com.dsadeghi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<T> {

    private DLLNode<T> head;

    public int size() {
        int size = 0;

        for (DLLNode<T> n = head; n.next() != null; n = n.next()) {
            size++;
        }

        return size;
    }

    public boolean isEmpty() {
        return (size() == 0);
    }

    public boolean contains(Object o) {
        for (DLLNode<T> n = head; n.next() != null; n = n.next()) {
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

        DLLNode<T> n = head;
        for (int i = 0; i < size(); i++) {
            array[i] = n;
            n = n.next();
        }

        return array;
    }

    public boolean add(T e) {
        ListIterator<T> iter = this.listIterator(size());
        iter.next(); //As for why we have to call next rather than starting at size() + 1, see quirk in add() implementation
        iter.add(e);
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
        ListIterator<T> iter = this.listIterator(index);
        return iter.next();
    }

    public T set(int index, T element) {
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
        T data = iter.next();
        iter.remove();

        return data;
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
        //Also when it calls methods, will it call the ones I defined or will it use the ones in java.util
        ListIterator<T> iter = this.listIterator();
        int index = 0;
        while (iter.hasNext()) {
            if (o.equals(iter.next())) {
                index = iter.nextIndex() - 1; //Is this really the best way to do this?
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
        if (fromIndex < 0 || fromIndex > size()) {
            throw new IndexOutOfBoundsException();
        }
        if (toIndex < 0 || toIndex > size()) {
            throw new IndexOutOfBoundsException();

        }

        ArrayList<T> list = new ArrayList<>();

        ListIterator<T> iter = this.listIterator(fromIndex);
        while (iter.nextIndex() != toIndex + 1) {
            list.add(iter.next());
        }

        return list;
    }












    private class DLLNode<E> {

        /*
            FIELDS
        */
        public E data;
        private DLLNode<E> next;
        private DLLNode<E> prev;
    

        /*
            CONSTRUCTORS
        */
    
        public DLLNode(E data, DLLNode<E> next, DLLNode<E> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    
        /**
         * Returns the reference to the next node linked to the calling node
         * @return The next node
         */
        public DLLNode<E> next() {
            return next;
        }
    
        /**
         * Returns the reference to the previous node linked to the calling node
         * @return The previous node
         */
        public DLLNode<E> prev() {
            return prev;
        }
    }
    
    private class DLLIterator implements ListIterator<T> {
        //Holds the reference to the next element
        private DLLNode<T> nextNode;
        //Whenever a method returns a node, it is cached in this variable
        private DLLNode<T> cachedNode;
        //Holds the index assigned to the next element
        private int index;
        private boolean canMutateList;

        public DLLIterator() {
            nextNode = head;
            index = 0;
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

        @Override
        public T next() {
            //Check if a next node exists first
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next node");
            }

            T data = nextNode.data;
            cachedNode = nextNode;
            nextNode = nextNode.next();
            canMutateList = true;
            index++;
            return data;
        }

        @Override
        public boolean hasPrevious() {
            return (nextNode.prev() != null);
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("There is no previous node");
            }

            T data = nextNode.prev.data;
            nextNode = nextNode.prev();
            canMutateList = true;
            index--;
            return data;
        }

        @Override
        public int nextIndex() {
            if (!hasNext()) {
                return size();
            } else {
                return index + 1;
            }
        }

        @Override
        public int previousIndex() {
            if (!hasPrevious()) {
                return 0;
            } else {
                return index - 1;
            }
        }

        @Override
        public void remove() {
            if (!canMutateList) {
                throw new IllegalStateException("Remove() must be preceded by next() or previous()");
            }

            cachedNode.prev.next = cachedNode.next();
            canMutateList = false;
            index--;
        }

        @Override
        public void set(T t) {
            if (!canMutateList) {
                throw new IllegalStateException("set() must be preceded by next() or previous()");
            }
            if (t == null) {
                throw new IllegalArgumentException("Data cannot be null");
            }

            cachedNode.data = t;
            canMutateList = false;
        }

        @Override
        public void add(T t) {
            
            if (t == null) {
                throw new IllegalArgumentException();
            }

            if (size() == 0) {                          // If list is empty
                head = new DLLNode<T>(t, null, null);
            } else if (!hasPrevious()) {                // Else if we are at the head of the list
                nextNode.prev = new DLLNode<T>(t, nextNode, null);
            } else if (!hasNext()) {                    // Else if we are at the tail of the list
                cachedNode.next = new DLLNode<T>(t, null, cachedNode);  //What happens here if we want to add at our initial position and haven't even called next() yet
            } else {
                cachedNode.next = new DLLNode<T>(t, nextNode, cachedNode);
            }
        }

    }
}
