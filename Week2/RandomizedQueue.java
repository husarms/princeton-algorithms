package Week2;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int itemCount;
    private int index;
    private final int defaultQueueLength = 8;

    // construct an empty deque
    public RandomizedQueue() {
        queue = (Item[]) new Object[defaultQueueLength];
        itemCount = 0;
        index = 0;
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
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!isEmpty()) {
            index++;
        }
        queue[index] = item;
        itemCount++;
        maintainQueueSize();
    }

    // remove and return the item from the back
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int size = size();
        if (size == 1) {
            Item item = queue[0];
            queue[0] = null;
            itemCount--;
            return item;
        }
        int lastIndex = size - 1;
        // Pick random element
        int randomIndex = StdRandom.uniform(0, lastIndex);
        Item randomItem = queue[randomIndex];

        // Switch with last element
        Item backItem = queue[lastIndex];
        queue[randomIndex] = backItem;

        // Remove last element
        queue[lastIndex] = null;
        index--;
        itemCount--;
        maintainQueueSize();
        return randomItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int size = size();
        int lastIndex = size - 1;

        if (lastIndex == 0) {
            Item item = queue[lastIndex];
            return item;
        }

        int randomIndex = StdRandom.uniform(0, lastIndex);
        Item randomItem = queue[randomIndex];
        return randomItem;
    }

    private void maintainQueueSize() {
        // halve the size when array is on quarter full
        if (queue.length > defaultQueueLength && (double) itemCount / queue.length <= 0.25) {
            halveSizeOfQueue();
        }
        // double the size when array is full
        if (index == queue.length - 1) {
            doubleSizeOfQueue();
        }
    }

    private void halveSizeOfQueue() {
        int newLength = queue.length / 2;
        Item[] newQueue = (Item[]) new Object[newLength];
        for (int i = 0; i < newLength; i++) {
            newQueue[i] = queue[i];
        }
        index = itemCount;
        queue = newQueue;
    }

    private void doubleSizeOfQueue() {
        int oldLength = queue.length;
        int newLength = oldLength * 2;
        Item[] newQueue = (Item[]) new Object[newLength];
        for (int i = 0; i < oldLength; i++) {
            newQueue[i] = queue[i];
        }
        index = itemCount - 1;
        queue = newQueue;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new RandomizedQueue.DequeIterator(this);
    }

    private class DequeIterator implements Iterator<Item> {
        private final RandomizedQueue queue;

        public DequeIterator(RandomizedQueue q) {
            queue = q;
        }

        public boolean hasNext() {
            return !queue.isEmpty();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (Item) queue.dequeue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        Object item;
        queue.enqueue(7);
        queue.enqueue(42);
        item = queue.dequeue();     //==> 7
        System.out.println("Item = " + item);
        queue.size();        //==> 1
        queue.size();        //==> 1
        queue.size();        //==> 1
        item = queue.dequeue();    // ==> 42
        System.out.println("Item = " + item);
        queue.isEmpty();     //==> true
        queue.enqueue(2);
        item = queue.dequeue();    //==> 42
        System.out.println("Item = " + item);

        queue = new RandomizedQueue<>();
        queue.enqueue(9);
        item = queue.sample();
        System.out.println("Item = " + item);

        queue = new RandomizedQueue<>();
        queue.enqueue(767);
        queue.enqueue(915);
        queue.enqueue(240);
        queue.enqueue(520);
        queue.enqueue(477);
        queue.enqueue(34);
        queue.enqueue(97);
        queue.enqueue(188);
        queue.enqueue(373);
        queue.enqueue(914);
        item = queue.sample();    // ==> null
        System.out.println("Item = " + item);
    }
}
