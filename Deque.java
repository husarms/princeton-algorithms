/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private static final int DEFAULT_QUEUE_LENGTH = 8;
    private Item[] queue;
    private int itemCount;
    private int frontIndex;
    private int backIndex;

    // construct an empty deque
    public Deque() {
        queue = (Item[]) new Object[DEFAULT_QUEUE_LENGTH];
        itemCount = 0;
        frontIndex = DEFAULT_QUEUE_LENGTH / 2;
        backIndex = frontIndex;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return itemCount <= 0;
    }

    // return the number of items on the deque
    public int size() {
        return itemCount;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!isEmpty()) {
            frontIndex--;
        }
        queue[frontIndex] = item;
        itemCount++;
        maintainQueueSize();
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!isEmpty()) {
            backIndex++;
        }
        queue[backIndex] = item;
        itemCount++;
        maintainQueueSize();
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = queue[frontIndex];
        queue[frontIndex] = null;
        itemCount--;
        if (!isEmpty()) {
            frontIndex++;
        }
        maintainQueueSize();
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = queue[backIndex];
        queue[backIndex] = null;
        itemCount--;
        if (!isEmpty()) {
            backIndex--;
        }
        maintainQueueSize();
        return item;
    }

    private void maintainQueueSize() {
        // halve the size when array is on quarter full
        // if (queue.length > defaultQueueLength && (double) itemCount / queue.length <= 0.25) {
        //     halveSizeOfQueue();
        // }
        // double the size when array is full
        if (frontIndex == 0 || backIndex == queue.length - 1) {
            doubleSizeOfQueue();
        }
    }

    // private void halveSizeOfQueue() {
    //     int newLength = queue.length / 2;
    //     int padding = (newLength - itemCount) / 2;
    //     Item[] newQueue = (Item[]) new Object[newLength];
    //     for (int i = frontIndex; i < backIndex - 1; i++) {
    //         newQueue[i + padding] = queue[i];
    //     }
    //     frontIndex = padding;
    //     backIndex = frontIndex + itemCount;
    //     queue = newQueue;
    // }

    private void doubleSizeOfQueue() {
        int oldLength = queue.length;
        int newLength = oldLength * 2;
        int padding = (newLength - itemCount) / 2;
        Item[] newQueue = (Item[]) new Object[newLength];
        int newIndex = 0;
        for (int i = frontIndex; i <= backIndex; i++) {
            newQueue[newIndex + padding] = queue[i];
            newIndex++;
        }
        frontIndex = padding;
        backIndex = frontIndex + itemCount - 1;
        queue = newQueue;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(this);
    }

    private class DequeIterator implements Iterator<Item> {
        private final Deque deque;
        private int index;

        public DequeIterator(Deque d) {
            deque = d;
            index = d.frontIndex;
        }

        public boolean hasNext() {
            return index <= deque.backIndex;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i = index;
            index++;
            return (Item) deque.queue[i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        Object item = deque.removeFirst();     //==> 1
        System.out.println("Item = " + item);
        deque.addFirst(3);
        item = deque.removeFirst();    //==> 3
        System.out.println("Item = " + item);
        deque.addFirst(5);
        item = deque.removeFirst();     //==> 5
        System.out.println("Item = " + item);
        deque.addFirst(7);
        Iterator<Integer> iterator = deque.iterator();    //==> []
        item = iterator.next();
        System.out.println("Item = " + item);
    }
}
