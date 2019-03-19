package cs445.a1;
import java.util.Arrays;

public class Set<T> implements SetInterface<T> {

    private T[] contents;
    private int size;
    private static final int DEFAULT_CAPACITY = 30;
    

    //initializes the array to a given capacity
    public Set(int capacity) {
        @SuppressWarnings("unchecked")
        T[] temp = (T[]) new Object[capacity];
        contents = temp;
        size = 0;
    }
    
    //initializes the array to a reasonable starting capacity
    public Set() {
        this(DEFAULT_CAPACITY);
    }
    
    @Override
    public int getCurrentSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(T newEntry) throws SetFullException, IllegalArgumentException {
        boolean toReturn = false;
        if (newEntry != null) {
            if (!contains(newEntry)) {
                if (size == contents.length) {
                   contents = Arrays.copyOf(contents,size*2);
                }
                contents[size] = newEntry;
                size++;
                toReturn = true;
            } 
        }
        else {
            throw new java.lang.IllegalArgumentException();
        }
        return toReturn;
    }

    @Override
    public boolean remove(T entry) throws IllegalArgumentException {
        if (entry == null) {
            throw new java.lang.IllegalArgumentException();
        }
        else {
            int index = getIndexOf(entry);
            T result = removeEntry(index);
            return entry.equals(result);
        }
    }

    @Override
    public T remove() {
        T result = removeEntry(size-1);
        return result;
    }

    @Override
    public void clear() {
        while (!isEmpty())
            remove();
    }

    @Override
    public boolean contains(T entry) throws IllegalArgumentException {
        if (entry == null)    
            throw new java.lang.IllegalArgumentException(); 
        else {
            return getIndexOf(entry) != -1;
        }
    }

    @Override
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[size];
        result = Arrays.copyOf(contents, size);
        return result;
    }

    private int getIndexOf(T entry) {
        int where = -1, index = 0;
        while (where == -1 && (index < size)) {
            if (entry.equals(contents[index])) {
                where = index;
            }
            index++;
        }
        return where;
    }

    private T removeEntry(int index) {
        T result = null;
        if (!isEmpty() && index >= 0) {
            result = contents[index];
            contents[index] = contents[size-1];
            contents[size-1] = null;
            size--;
        }
        return result;
    }
}
