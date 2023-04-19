/*
 * MIT License
 *
 * Copyright (c) 2023, Atilt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.atilt.buddy;

import javax.annotation.Nonnegative;
import java.util.*;

class HelloWorld {

    private Test test = new Test();

    public static void main(String[] args) {

    }
    
    public class Test {

        public void go() {
            CursorList<String> list = new CursorList<>();
list.add("a");
list.add("b");
list.add("c");

// Set the cursor to the second element.
list.setCursor(1);

// Iterate over the list from the cursor position.
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);
}
        }
        
    } 

public static class CursorList<E> implements List<E> {

    private List<E> list;
    private int cursor;
    private int modCount;

    public CursorList() {
        list = new ArrayList<>();
        cursor = -1;
        modCount = 0;
    }

    public CursorList(List<E> list) {
        this.list = list;
        cursor = -1;
        modCount = 0;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        if (cursor < 0 || cursor >= list.size()) {
            throw new IndexOutOfBoundsException();
        }
        this.cursor = cursor;
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        E result = list.set(index, element);
        modCount++;
        return result;
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        modCount++;
        if (index <= cursor) {
            cursor++;
        }
    }

    @Override
    public boolean add(E element) {
        boolean result = list.add(element);
        modCount++;
        if (cursor == -1) {
            cursor = 0;
        }
        return result;
    }

    @Override
    public E remove(int index) {
        E result = list.remove(index);
        modCount++;
        if (index <= cursor) {
            cursor--;
        }
        return result;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new CursorIterator(this.cursor);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        int index = list.indexOf(o);
        if (index == -1) {
            return false;
        } else {
            remove(index);
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = list.addAll(c);
        modCount++;
        if (cursor == -1 && !isEmpty()) {
            cursor = 0;
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = list.addAll(index, c);
        modCount++;
        if (index <= cursor && !c.isEmpty()) {
            cursor += c.size();
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = list.removeAll(c);
        modCount++;
        if (result) {
            cursor = -1;
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = list.retainAll(c);
        modCount++;
        if (result) {
            cursor = -1;
        }
        return result;
    }

    @Override
    public void clear() {
        list.clear();
        cursor = -1;
        modCount++;
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new CursorListIterator(this.cursor);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > list.size()) {
            throw new IndexOutOfBoundsException();
        }
        return new CursorListIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > list.size() || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        CursorList<E> subList = new CursorList<>(list.subList(fromIndex, toIndex));
        subList.cursor = cursor - fromIndex;
        return subList;
    }

    public class CursorIterator implements Iterator<E> {
        protected int cursor;
        protected int expectedModCount;
        protected int lastRet;

        public CursorIterator(@Nonnegative int cursor) {
            this.cursor = cursor;
            this.expectedModCount = modCount;
            this.lastRet = -1;
        }

        public CursorIterator() {
            cursor = 1;
            expectedModCount = modCount;
            lastRet = -1;
        }

        public boolean hasNext() {
            return cursor + 1 != list.size();
        }

        public E next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            E element = get(cursor);
            cursor++;
            return element;
        }

        public void remove() {
            checkForComodification();
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            list.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
            expectedModCount = modCount;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    public class CursorListIterator extends CursorIterator implements ListIterator<E> {

        public CursorListIterator() {
            super();
        }

        public CursorListIterator(int cursor) {
            super(cursor);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0) {
                throw new NoSuchElementException();
            }
            cursor = i;
            lastRet = i;
            return list.get(i);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(E element) {
            checkForComodification();
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            list.set(lastRet, element);
        }

        @Override
        public void add(E element) {
            checkForComodification();
            int i = cursor;
            list.add(i, element);
            cursor = i + 1;
            lastRet = -1;
            expectedModCount++;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            list.remove(lastRet);
            if (lastRet < cursor) {
                cursor--;
            }
            lastRet = -1;
            expectedModCount++;
        }
    }
}
}