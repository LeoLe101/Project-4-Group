package cs143;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This is the concrete class that will create the Sorted Decimal Map by
 * implementing the Decimal Search Tree
 *
 * @author Phuc Hong Le
 * @author Wai Kwan Janette Chow
 * @author Zhaoyuan Xu
 * @version 5/17/2017
 * @param <E> generic type that will decided the object within the tree
 */
public class SortedDecimalMap<E extends DecimalSortable>
        implements DecimalSearchTree<E> {

    //private fields
    private DecimalNode root;
    private int digitCount;

    /**
     * Constructor.
     *
     * @param digitCount the largest number of digits a sorting key will contain
     * in this sorted decimal map
     */
    public SortedDecimalMap(int digitCount) {
        root = new DecimalNode();
        this.digitCount = digitCount;
    }

    /**
     * Check if the tree contains the value by its key number
     *
     * @param key the key number attached with the value inside of the tree
     * @return true if a value with given key exists, false if it is not
     */
    @Override
    public boolean contains(int key) {
        return (get(key) != null);
    }

     /**
     * Get the value with the given key number
     *
     * @param key the key number attached with the value inside of the tree
     * @return the value of its given key, null if the value does not exist
     */
    @Override
    public E get(int key) {
        //convert the key to a usable index
        String wholeNum = "" + key;
        if (wholeNum.length() > digitCount) {
            return null;
        } else {
            int diff = digitCount - wholeNum.length();
            for (int i = 0; i < diff; i++) {
                wholeNum = "0" + wholeNum;
            }
            //the variable whole number will be the filled number
            int childIndex = 0;
            DecimalNode<E> pointer = root;
            for (int j = 0; j < digitCount && pointer != null; j++) {
                childIndex = wholeNum.charAt(j) - 48;
                pointer = pointer.children[childIndex];
            }
            if (pointer == null) {
                return null;
            } else if (pointer.value == null) {
                return null;
            } else {
                return pointer.value;
            }
        }

    }

     /**
     * Insert the value which has a unique non-negative whole-number key to the
     * tree
     *
     * @param e the value to be inserted
     * @return true if the value has been inserted, false if is not
     */
    @Override
    public boolean insert(E e) {
        //Split the key to specific number according to the digit count
        int key = e.getKey();
        ArrayList<Integer> keyLayer = new ArrayList<>();
        int length = String.valueOf(key).length();
        if (length > digitCount) {
            return false;
        }
        //separate the digit in the key number into different layer
        if (key == 0) {
            keyLayer.add(0, 0);
        } else {
            while (key > 0) {
                int digit = key % 10;
                keyLayer.add(0, digit);
                key /= 10;
            }
        }
        //adding the missing digit according to the digitCount
        if (length < digitCount) {
            int frontNumber = digitCount - length;
            for (int i = 0; i < frontNumber; i++) {
                keyLayer.add(0, 0);
            }
        }
        //move through the tree to find the value at that key
        boolean added = false;
        DecimalNode temp = root;
        for (int i = 0; i < digitCount; i++) {
            int index = keyLayer.get(i);
            if (temp.children[index] == null) {
                temp.makeChild(index);
            }
            //insert the value if it is not already existed
            if (i == digitCount - 1) {
                if (temp.children[index].value != null) {
                    added = false;
                } else {
                    temp.children[index].value = e;
                    added = true;
                }
            } else {
                temp = temp.children[index];
            }
        }
        return added;
    }

     /**
     * Remove the value with the given key from the tree
     *
     * @param key the key number attached with the value inside of the tree
     * @return true if the value is found and removed, false if it is not
     */
    @Override
    public boolean remove(int key) {
        //Split the key to specific number according to the digit count
        ArrayList<Integer> keyLayer = new ArrayList<>();
        int length = String.valueOf(key).length();
        if (length > digitCount) {
            return false;
        }
        //separate the digit in the key number into different layer
        while (key > 0) {
            int digit = key % 10;
            keyLayer.add(0, digit);
            key /= 10;
        }
        //adding the missing digit according to the digitCount
        if (length < digitCount) {
            int frontNumber = digitCount - length;
            for (int i = 0; i < frontNumber; i++) {
                keyLayer.add(0, 0);
            }
        }
        //move through the tree to find the value at that key
        DecimalNode temp = root;
        int layer = 0;
        for (int i = 0; i < digitCount; i++) {
            layer = keyLayer.get(i);
            if (temp.children[layer] != null) {
                temp = temp.children[layer];
            }
        }
        //remove the value at specific key if it exists
        if (temp.value != null) {
            temp.value = null;
            return true;
        } else {
            return false;
        }

    }

    /**
     * Reports if the tree is empty or not.
     *
     * @return true if the tree is empty, false if not
     */
    @Override
    public boolean isEmpty() {
        return isEmpty(root, true);
    }

    /**
     * Private recursive method to determine if the tree is empty.
     *
     * @param node the current node the recursion is on
     * @return false if a value is found in the tree, true if recursion is
     * complete and no values were found
     */
    private boolean isEmpty(DecimalNode node, boolean empty) {
        if (node.value != null) {
            return false;
        } else {
            for (int i = 0; i < 10; i++) {
                if (node.children[i] != null) {
                    empty = empty && isEmpty(node.children[i], empty);
                }
            }
        }
        return empty;
    }

    /**
     * Provides access to a type-specific iterator.
     *
     * @return a new iterator object
     */
    @Override
    public Iterator<E> iterator() {
        return new DecimalIterator();
    }

    /**
     * An inner class that defines a type-specific iterator. Uses a queue
     * internally to manage iterating through the tree.
     */
    private class DecimalIterator implements Iterator<E> {

        //private fields
        private Queue<E> queue;
        private E lastRemoved;

        /**
         * Default constructor.
         */
        public DecimalIterator() {
            fillQueue();
        }

        /**
         * A private method used to fill the queue.
         */
        private void fillQueue() {
            queue = new LinkedList<>();
            fillQueue(root);
        }

        /**
         * A private recursive method to fill the queue with the value of each
         * node in sorted order.
         *
         * @param node the current node in the recursive process
         */
        private void fillQueue(DecimalNode node) {
            if (node == null) {
                return;
            } else if (node.value != null) {
                queue.offer((E) node.value);
            } else {
                for (int i = 0; i < node.children.length; i++) {
                    fillQueue(node.children[i]);
                }

            }
        }

        /**
         * Determines if there is a next value in the map.
         *
         * @return true if there is a next value, false if not
         */
        @Override
        public boolean hasNext() {
            if (queue.isEmpty()) {
                return false;
            }
            return true;
        }

        /**
         * Provides access to the next value in the map, in sorted order.
         *
         * @return the next value
         */
        @Override
        public E next() {
            lastRemoved = queue.poll();
            return lastRemoved;
        }

        /**
         * Removes the last reported value from the map.
         */
        @Override
        public void remove() {
            SortedDecimalMap.this.remove(lastRemoved.getKey());
        }
    }

    /**
     * An inner class that defines the decimal tree node.
     */
    private class DecimalNode<E> {

        /**
         * the value stored in this node - will be null for all nodes except the
         * lowest-level leaf nodes
         */
        public E value;

        /**
         * the array used to store the children of this node
         */
        public DecimalNode[] children;

        /**
         * Default constructor.
         */
        public DecimalNode() {
            children = new DecimalNode[10];
        }

        /**
         * A convenience method to create a new node at the given index of the
         * current node.
         *
         * @param index the index of the children array where the new node
         * should be stored
         */
        public void makeChild(int index) {
            children[index] = new DecimalNode();
        }
    }
}
