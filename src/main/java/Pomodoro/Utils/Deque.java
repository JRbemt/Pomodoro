package Pomodoro.Utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class Deque<T> implements Iterable<T>{

	private T[] items;
	private int beginIndex;
	protected int size = 0;

	private boolean ordered = true;

	/**
	 * Constructs an empty unordered Deque with an initial capacity of 64.
	 */
	public Deque() {
		this(false, 64);
	}
	/**
	 * Constructs an empty Deque with:
	 * @param ordered is the set ordered (should order of addition be retained, performance of {@link #remove_internal})
	 * @param capacity the initial capacity of Deque
	 */
	public Deque(boolean ordered, int capacity) {
		items = (T[])new Object[capacity];
		this.ordered = ordered;
	}

	public Deque(boolean ordered, int capacity, Class arrayType){
        items = (T[]) Array.newInstance(arrayType, capacity);
        this.ordered = ordered;
    }

    public Deque(boolean ordered, Class arrayType){
	    this(ordered, 64, arrayType);
    }

    public Deque(T... items){
        this.items = items;
        size = items.length;
    }

    /** Returns if this array Deque value.
     * @param value May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return true if array contains value, false if it doesn't */
	public boolean contains(T value, boolean identity) {
		int i = size-1;
        if (identity || value == null){
            while (i >= beginIndex)
                if (items[i--] == value) return true;
        } else {
            while (i >= beginIndex)
                if (value.equals(items[i--])) return true;
        }
        return false;
	}
    /**
     * Amount of "empty" slots at the beginning of the backing Array.
     * Set this to 0 by calling {@link #shrink()}
     * */
    public int getBeginIndex() {
        return beginIndex;
    }
    /**
     * @param beginfill index where to begin with filling
     * @param beginread index where to begin reading
     * @param positions reads specified amount of positions or till end of Array
     **/
    private void shift(int beginfill, int beginread, int positions){
        int maxdist = Math.max(beginread, beginfill);
        System.arraycopy(this.items, beginread, this.items, beginfill, Math.min(positions, this.items.length - maxdist));
    }

    /** Returns the index of first occurrence of value in the Deque, or -1 if no such value exists.
     * @param value May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return An index of first occurrence of value in array or -1 if no such value exists */
    public int indexOf (T value, boolean identity) {
        int i = size-1;
        if (identity || value == null) {
            while (i >= beginIndex)
                if (items[i] == value) return i;
        } else {
            while (i >= beginIndex)
                if (value.equals(items[i])) return i;
        }
        return -1;
    }

	/**
	 * Returns the element at the specified position in the Deque.
	 * @param index index of the element to return
	 * @return the element at the specified position in bag
	 */
	public T get(int index) {
		return items[index(index)];
	}

    public boolean isOrdered() {
        return ordered;
    }

    /**
     * Returns the element at the specified position in the backing Array.
     * @param index index of the element to return
     * @return the element at the specified position in bag
     *
     * @throws ArrayIndexOutOfBoundsException
     */
    public T getAbsolute(int index) throws ArrayIndexOutOfBoundsException{
        return items[index];
    }

	/**
	 * @return the number of items in this Deque
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the current size of the backing Array
	 * @return the number of items Array can hold without growing
	 */
	public int getCapacity() {
		return items.length;
	}

	/**
	 * Returns true if this Deque contains no items.
	 * @return {@code true} if this Deque contains no items
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Adds the specified element to the end of this Deque.
	 * <p>
	 * If required, it also increases the capacity of the Deque.
	 * </p>
	 * @param value element to be added to this Deque
	 */
	public void push(T value) {
		if (size == items.length)
			grow((items.length * 7) / 4 + 1);

		items[index(size++)] = value;
	}
    /**
     * Adds the specified elements to the end of this Deque.
     * <p>
     * If required, it also increases the capacity of the Deque.
     * </p>
     * @param values elements to be added to this Deque
     */
    public void push(T... values) {
	    int al = values.length;
        if ((size+al) > items.length)
            grow((items.length * 7) / 4 + al);

        System.arraycopy(values, 0, items, size, al);
        size += al;
    }
	/**
	 * Python like loop-indexing, where -1 returns the last item,
	 * and size+1 the first etc.
	 * @return index of item
	 * @see #getAbsolute(int)
	 * */
	private int index(int relativeIndex) {
        int index = relativeIndex % size;
        if (index < 0) {
            assertNotEmpty();
            return beginIndex + size + index;
        } else return index + beginIndex;
    }

	/**
	 * Set element at specified index in the backing Array.
	 * @param index position of element
	 * @param value the element
	 */
	public void set(int index, T value) {
		if(index >= items.length) {
			grow((index * 7) / 4 + 1);
		}
		size = Math.max(size, index + 1);
		items[index(index)] = value;
	}

	/**
	 * Increase the capacity of the Deque.
	 * @param newCapacity new capacity to grow to
	 * @throws ArrayIndexOutOfBoundsException if new capacity is smaller than old
	 */
	private void grow(int newCapacity) throws ArrayIndexOutOfBoundsException {
        T[] newItems = (T[])Array.newInstance(items.getClass().getComponentType(), newCapacity);
        System.arraycopy(items, beginIndex, newItems, 0, Math.min(size, newItems.length));
		this.items = newItems;
		beginIndex = 0;
	}

	/**
	 * Check if an item, if added at the given item will fit into the bag.
	 * <p>
	 * If not, the bag capacity will be increased to hold an item at the index.
	 * </p>
	 * @param index index to check
	 */
	public void ensureCapacity(int index) {
		if(index >= items.length) {
			grow(index);
		}
	}
    public T[] range(int start, int end){
	    return range(start, end, false);
    }
	public T[] range(int start, int end, boolean endInclusive){
	    start = index(start);
	    end = index(end);
        int len = end-start;
        T[] range;
	    if (len >= 0) {
	        if (endInclusive){
	            len++;
            }
            range = (T[]) Array.newInstance(items.getClass().getComponentType(), len);
            System.arraycopy(items, start, range, 0, Math.min(size, len));
        } else {
	        if (endInclusive){
	            len--;
            }
            range = (T[]) Array.newInstance(items.getClass().getComponentType(), -len);
            for (int i = 0; i < -len; i++)
                range[i] = this.items[start-i];
        }
        return range;
    }
    /***
     * Shrink backing array to the minimal required space
     * */
    public void shrink(){
        slice(beginIndex, size);
    }
    public Deque<T> slice(int start, int end){
        return slice(start, end, false);
    }
    public Deque<T> slice(int start, int end, boolean endInclusive){
        this.items = range(start, end, endInclusive);
        this.beginIndex = 0;
        this.size = this.items.length;
        return this;
    }

	/**
	 * Removes all of the items from this bag.
	 * <p>
	 * The bag will be empty after this call returns.
	 * </p>
	 */
	public void clear() {
		Arrays.fill(items, 0, size, null);
		size = 0;
		beginIndex = 0;
	}

	/**
	 * Set the size.
	 * <p>
	 * This will not resize the Deque, nor will it clean up contents beyond the
	 * given size. Use with caution.
	 * </p>
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

    /** Removes and returns the first item.
     * */
	public T pop(int index) {
		assertNotEmpty();
        int i = index(index);
        if (i == 0){
            size--;
            return getAbsolute(beginIndex++);
        } else if (i == size-1){
            return getAbsolute(--size);
        } else {
            //TODO
            T value = getAbsolute(i);
            remove_internal(i);
            return value;
        }
	}

    /** Returns a random item from the array, or null if the array is empty. */
    public T random () {
        if (size == 0) return null;
        return items[new Random().nextInt(size)+beginIndex];
    }

    /** Returns the items as an array. Note the array is typed, so the {@link #(Class)} constructor must have been used.
     * Otherwise use {@link #toArray(Class)} to specify the array type. */
    public T[] toArray () {
        return (T[])toArray(items.getClass().getComponentType());
    }

    /**
	 * Makes a copy of the backing array
	 * */
    public <V> V[] toArray (Class type) {
        V[] result = (V[])Array.newInstance(type, size);
        System.arraycopy(items, 0, result, 0, size);
        return result;
    }


    /**
	 * When <code>unordered</code> and an item in the middle of the set is removed,
	 * the last item will simply be placed in the gap.
	 * When ordered the remaining items are shifted back.
     * @param absIndexStart removes item in the backing Array.
     *                 If it's in the Deque (between bounds) it will try filling the empty spot.
     *                 When ordered it will shift all following items 1 spot down.
     *                 When unordered it will be replaced by the last item in the Deque
     * */
	private void remove_internal(int absIndexStart) {
        if (absIndexStart >= this.beginIndex && absIndexStart < this.size) {
            if (this.ordered) {
                shift(absIndexStart, absIndexStart+1, this.beginIndex + this.size);
            } else {
                this.items[absIndexStart] = getAbsolute(this.size-1);
            }
            this.size--;
        }
    }
    /**
	 * TODO Test
	 * */
    public void removeRange(int start, int end){
		shift(index(start), index(end), this.beginIndex+this.size);
		size -= end-start;
	}
    /**
     * @return backing Array T[]
     * */
    public T[] getItems() {
        return items;
    }

    private void assertNotEmpty() {
		if (size == 0)
			throw new RuntimeException("Deque is empty.");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Deque other = (Deque) o;
		if (size != other.size) return false;

		for (int i = 0; size > i; i++)
			if (get(i) != other.get(i)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0, s = size; s > i; i++) {
			hash = (127 * hash) + items[i].hashCode();
		}

		return hash;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IntDeque(");
		for (int i = 0; size > i; i++) {
			if (i > 0) sb.append(", ");
			sb.append(items[index(i)]);
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
		    private int current = beginIndex;
            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            public T next() {
                return getAbsolute(current++);
            }
        };
	}
}