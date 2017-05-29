package cs143;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for the SortedDecimalMap class in order to find errors within the 
 * class methods and Iterator inner class
 * 
 * @author Phuc Hong Le
 * @author Wai Kwan Janette Chow
 * @author Zhaoyuan Xu
 * @version 5/17/2017
 */
public class SortedDecimalMapTest {

    SortedDecimalMap<Product> map = new SortedDecimalMap<>(7);

    public SortedDecimalMapTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of contains method, of class SortedDecimalMap.
     */
    @Test
    public void testContains() {
        map = new SortedDecimalMap(7);
        map.insert(new Product(4578, "Item:4578", 1, 1));
        map.insert(new Product(434578, "Item:434578", 1, 1));
        map.insert(new Product(6, "Item:6", 1, 1));
        //test if the key exists
        assertTrue(map.contains(4578));
        assertTrue(map.contains(434578));
        assertTrue(map.contains(6));
        //test keys that are not existed
        assertFalse(map.contains(7643));
        assertFalse(map.contains(4579));
        assertFalse(map.contains(457));
        assertFalse(map.contains(123456678));
    }

    /**
     * Test of get method, of class SortedDecimalMap.
     */
    @Test
    public void testGet() {
        map = new SortedDecimalMap(6);
        map.insert(new Product(4578, "Item:4578", 123, 1));
        map.insert(new Product(434578, "Item:434578", 456, 1));
        map.insert(new Product(6, "Item:6", 789, 1));
        //test if the tree get the right value
        assertTrue(map.get(4578).getIsle() == 123);
        assertTrue(map.get(434578).getIsle() == 456);
        assertTrue(map.get(6).getIsle() == 789);
        map.remove(6);
        assertNull(map.get(6));
        assertNull(map.get(7643));
        assertNull(map.get(4579));
        assertNull(map.get(457));
        assertNull(map.get(123456678));

    }

    /**
     * Test of insert method, of class SortedDecimalMap.
     */
    @Test
    public void testInsert() {
        map = new SortedDecimalMap(8);
        //test if it is inserted or not
        assertTrue(map.insert(new Product(4578, "Item:4578", 1234, 1)));
        assertTrue(map.insert(new Product(0, "Item:0", 456, 1)));
        assertTrue(map.insert(new Product(99999999, "Item:99999999", 123, 1)));
        //test products with wrong key number and existed key
        assertFalse(map.insert(new Product(123123123, "Item:123123123", 123, 1)));
        assertFalse(map.insert(new Product(4578, "Item:4578", 111, 1)));
        map.remove(4578);
        assertTrue(map.insert(new Product(4578, "Item:4578", 6666, 1)));
        assertTrue(map.get(4578).getIsle() == 6666);
        assertTrue(map.contains(4578));
        assertTrue(map.contains(99999999));
        assertTrue(map.contains(0));

    }

    /**
     * Test of remove method, of class SortedDecimalMap.
     */
    @Test
    public void testRemove() {
        map = new SortedDecimalMap(8);
        assertTrue(map.insert(new Product(4578, "Item:4578", 1234, 1)));
        assertTrue(map.insert(new Product(1, "Item:1", 456, 1)));
        assertTrue(map.insert(new Product(99999999, "Item:99999999", 123, 1)));
        //test if it is removed or not
        assertTrue(map.remove(4578));
        assertTrue(map.remove(1));
        assertTrue(map.remove(99999999));
        assertFalse(map.remove(4578));
        assertFalse(map.remove(23342));
        assertFalse(map.remove(123456789));
        assertFalse(map.contains(4578));
    }

    /**
     * Test of isEmpty method, of class SortedDecimalMap.
     */
    @Test
    public void testIsEmpty() {
        map = new SortedDecimalMap<>(5);
        assertFalse(map.insert(new Product(457333338, "Item:4578", 1234, 1)));
        assertFalse(map.insert(new Product(99999999, "Item:99999999", 123, 1)));
        assertTrue(map.insert(new Product(45578, "Item:4578", 1234, 1)));
        assertTrue(map.insert(new Product(12, "Item:1", 456, 1)));
        assertTrue(map.insert(new Product(999, "Item:99999999", 123, 1)));
        //test the tree is empty after removed some values
        assertTrue(map.remove(45578));
        assertFalse(map.isEmpty());
        assertTrue(map.remove(12));
        assertTrue(map.remove(999));
        //test if the whole tree is empty after removed all value
        assertTrue(map.isEmpty());

    }

    /**
     * Test of iterator method, of class SortedDecimalMap.
     */
    @Test
    public void testIterator() {
        map = new SortedDecimalMap<>(5);
        Random rand = new Random();
        Product[] list = new Product[50];
        int rng = 0;
        for (int i = 0; i < 50; i++) {
            rng = rand.nextInt(10000);
            map.insert(new Product(rng, "Item:" + rng, 1, 1));
        }
        ArrayList<Product> list2 = new ArrayList<>();
        Iterator<Product> i = map.iterator();
        while (i.hasNext()) {
            list2.add((Product) i.next());
        }
        System.out.println("list2:" + list2);
        for (int j = 1; j < list2.size(); j++) {
            assertTrue(list2.get(j).getKey() - list2.get(j - 1).getKey() > 0);
        }
        Iterator<Product> i2 = map.iterator();
        while (i2.hasNext()) {
            if (i2.next().getKey() % 2 == 0) {
                i2.remove();
            }

        }
        for (int k = 0; k < list2.size(); k++) {
            if (list2.get(k).getKey() % 2 == 0) {
                assertFalse(map.contains(list2.get(k).getKey()));
            } else {
                assertTrue(map.contains(list2.get(k).getKey()));
            }
        }
        ArrayList<Product> list3 = new ArrayList<>();
        Iterator<Product> i3 = map.iterator();
        while (i3.hasNext()) {
            list3.add((Product) i3.next());
        }
        for (int q = 1; q < list3.size(); q++) {
            assertTrue(list3.get(q).getKey() - list3.get(q - 1).getKey() > 0);
        }
    }
}
