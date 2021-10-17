package nz.ac.gitlab.mwa172.seng201.group8.gamelogic;

import nz.ac.gitlab.mwa172.seng201.group8.gamelogic.MaximumList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MaximumListTest {

    MaximumList<String> strings;
    MaximumList<Integer> integers;

    private static final int testSize = 5;

    @BeforeEach
    void setUp() {
        strings = new MaximumList<>(testSize);
        integers = new MaximumList<>(testSize);
    }

    @Test
    void canAddItem() {
        strings = new MaximumList<>(5);
        assertTrue(strings.add("a"));
    }

    /**
     * Fills a MaximumList, then tests to make sure it cannot take any more items.
     * Then expands the maximum size of the list by 1 and sees if it can add an item.
     * Then makes sure that the list does not contain items that were added when they
     * should not have been.
     */
    @Test
    void canChangeMaxSize() {
        for (int i = 0; i < strings.getMaxSize(); i++) {
            strings.add("a");
        }
        assertFalse(strings.add("b"));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            strings.add(0, "b");
        });
        strings.setMaxSize(strings.getMaxSize() + 1);
        assertTrue(strings.add("c"));
        assertTrue(!strings.contains("b") && strings.contains("c"));
    }

    /**
     * Tests to see if an IndexOutOfBoundsException is NOT thrown when trying to insert
     * at the front of a MaximumList that is NOT full.
     */
    @Test
    void canAddToNonFullList() {
        for (int i = 0; i < testSize - 1; i++) {
            integers.add(i);
        }
        assertDoesNotThrow(() -> {
            integers.add(0, testSize);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            integers.add(testSize, Integer.MAX_VALUE);
        });
    }

    /**
     * Tests to see if an IndexOutOfBoundsException is thrown when trying to insert
     * at the front of a MaximumList that is full.
     */
    @Test
    void cannotInsertWhenFull() {
        for (int i = 0; i < testSize; i++) {
            integers.add(i);
        }

        assertThrows(IndexOutOfBoundsException.class, () -> {
            integers.add(0, testSize);
        });
    }

    /**
     * Tests to see if we cannot add more than testSize items to a MaximumList.
     */
    @Test
    void cannotAddTooManyItems() {
        for (int i = 0; i < testSize; i++) {
            assertTrue(strings.add("a"));
        }
        assertFalse(strings.add("a"));

        String[] testArray = new String[testSize];
        Arrays.fill(testArray, "a");

        assertArrayEquals(strings.toArray(), testArray);
    }

    @Test
    void canRemoveFromIndex() {
        strings.add("a");
        assertEquals(strings.remove(0), "a");
        assertArrayEquals(strings.toArray(), new String[]{});
    }

    @Test
    void canRemoveObject() {
        strings.add("a");
        strings.add("b");
        assertTrue(strings.remove("b"));
        assertFalse(strings.remove("b"));

        assertDoesNotThrow(() -> {
            for (int i = 0; i < testSize - 1; i++)
                strings.add("c");
        });
        String[] compareArray = new String[testSize];
        compareArray[0] = "a";
        for (int i = 1; i < testSize; i++)
            compareArray[i] = "c";

        assertArrayEquals(strings.toArray(), compareArray);
    }

    @Test
    void toArrayIsValid() {
        String[] stringArray = new String[testSize];
        for (int i = 0; i < testSize; i++) {
            stringArray[i] = "a";
            strings.add("a");
        }

        assertArrayEquals(strings.toArray(), stringArray);
        assertArrayEquals(Arrays.copyOf(strings.toArray(), strings.size(), String[].class), stringArray);
    }
}