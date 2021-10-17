package nz.ac.gitlab.mwa172.seng201.group8.gamelogic;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A list that has a maximum length. Allows appending and removal,
 * just like ArrayList, however the list has a maximum length that cannot
 * be exceeded.
 * <p>
 * The maximum length is mutable.
 *
 * @param <E> The type of objects stored inside this list.
 */
public class MaximumList<E> extends AbstractList<E> {

    /**
     * List that actually stores the items in this list.
     */
    private List<E> items;
    /**
     * The maximum size of this list.
     */
    private int maxSize;

    /**
     * Constructor for MaximumList. Initialises the items to an
     * empty ArrayList
     *
     * @param maxSize The maximum size of this list.
     */
    public MaximumList(int maxSize) {
        items = new ArrayList<>();
        setMaxSize(maxSize);
    }

    /**
     * Determines whether or not this list is full.
     *
     * @return Returns true if the number of items
     * in the list is greater than or equal to the max size.
     */
    public boolean isFull() {
        return items.size() >= maxSize;
    }

    /**
     * Sets the maximum size of this list. The number of items currently
     * in the list cannot exceed the new maximum size.
     *
     * @param maxSize The new maximum size of the list.
     * @throws IllegalArgumentException Thrown when attempting to set a maximum size less than the number
     *                                  of items currently in the list.
     */
    public void setMaxSize(int maxSize) throws IllegalArgumentException {
        if (items.size() > maxSize)
            throw new IllegalArgumentException("MaximumList cannot be set smaller than the number of items in the list!");
        this.maxSize = maxSize;
    }

    /**
     * @return Returns the maximum size of the list.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Gets the item at the specified index.
     *
     * @param index Index at which to get.
     * @return The item at the specified index.
     * @throws IndexOutOfBoundsException Thrown when attempting to grab an item from outside the bounds
     *                                   of the list.
     */
    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < maxSize) {
            return items.get(index);
        } else {
            throw new IndexOutOfBoundsException(outOfBoundsErrorMessage("get from"));
        }
    }

    /**
     * @return The number of items in the list.
     */
    @Override
    public int size() {
        return items.size();
    }

    /**
     * Appends the item onto the end of the list.
     *
     * @param item The item to append
     * @return Returns true if the item was added, false otherwise.
     */
    @Override
    public boolean add(E item) {
        if (items.size() < maxSize) {
            return items.add(item);
        }
        return false;
    }

    /**
     * Replaces the item at the specified index with a new item.
     *
     * @param index   Index at which to set the item.
     * @param element Item to set.
     * @return The element previously at the specified position.
     * @throws IndexOutOfBoundsException Thrown when attempting to replace an item at an index out of the bounds
     *                                   of this list.
     */
    @Override
    public E set(int index, E element) throws IndexOutOfBoundsException {
        if (index < maxSize) {
            return items.set(index, element);
        } else {
            throw new IndexOutOfBoundsException(outOfBoundsErrorMessage("set"));
        }
    }

    /**
     * Adds an item at a specified index and shifts the item currently
     * at that index and all the other items after it to the right.
     *
     * @param index Index at which to add the item.
     * @param item  Item to add.
     * @throws IndexOutOfBoundsException Thrown if attempting to add an item beyond the bounds of the list,
     *                                   or if attempting to add to a list that is already full.
     */
    @Override
    public void add(int index, E item) throws IndexOutOfBoundsException {
        if (index < maxSize && items.size() < maxSize) {
            items.add(index, item);
        } else {
            throw new IndexOutOfBoundsException(outOfBoundsErrorMessage("add"));
        }
    }

    /**
     * Removes the item at the specified index, and shifts all items
     * after it to the left.
     *
     * @param index The index at which to remove the item.
     * @return The item at the index that was removed.
     */
    @Override
    public E remove(int index) {
        return items.remove(index);
    }

    /**
     * Inserts all of the elements in the collection at the specified index,
     * and shifts every item after the index to the right.
     *
     * @param index      Index at which to insert the collection.
     * @param collection Collection to insert.
     * @return Returns true if the collection was added, false otherwise.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        if (index + collection.size() < maxSize) {
            return items.addAll(index, collection);
        }
        return false;
    }

    /**
     * Inserts all of the elements in the collection at the end of the list.
     *
     * @param collection Collection to insert.
     * @return Returns true if the collection was added, false otherwise.
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return addAll(items.size() - 1, collection);
    }

    /**
     * Removes all elements from the list that are contained in the
     * specified collection.
     *
     * @param collection Collection to remove.
     * @return Returns true if an item was removed, false otherwise.
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        return items.removeAll(collection);
    }

    /**
     * Generates an error message based on what caused the error.
     * Error message is of the format:
     * <b>Cannot {verb} beyond max size!</b>
     *
     * @param verb A verb that describes what the operation that generated an error was trying to do.
     *             E.g. set, add, get.
     * @return Returns a string error message.
     */
    private String outOfBoundsErrorMessage(String verb) {
        return String.format("Cannot %s beyond max size!", verb);
    }
}
