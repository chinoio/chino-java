package io.chino.api.common;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A {@link List} whose content can not be modified.
 * Trying to call methods that change the content of this List will result in an {@link UnsupportedOperationException}.
 *
 * @param <T> The type of the objects in the list
 */
public class UnmodifiableList<T> implements List<T> {

    private final List<T> sourceList;

    /**
     * Create a new {@link UnmodifiableList} from the provided source {@link List}.
     * The content of the source will be accessible from this {@link UnmodifiableList},
     * but trying to change the content of the list will result in an {@link UnsupportedOperationException}.
     *
     * @param source the source {@link List}
     */
    public UnmodifiableList(List<T> source) {
        this.sourceList = Collections.unmodifiableList(source);
    }

    @Override
    public int size() {
        return sourceList.size();
    }

    @Override
    public boolean isEmpty() {
        return sourceList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return sourceList.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return sourceList.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return sourceList.toArray();
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return sourceList.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return sourceList.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return sourceList.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return sourceList.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return sourceList.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return sourceList.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return sourceList.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return sourceList.retainAll(c);
    }

    @Override
    public void clear() {
        sourceList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return sourceList.equals(o);
    }

    @Override
    public int hashCode() {
        return hashCode();
    }

    @Override
    public T get(int index) {
        return sourceList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return sourceList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        sourceList.add(index, element);

    }

    @Override
    public T remove(int index) {
        return sourceList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return sourceList.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return sourceList.listIterator();
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return sourceList.subList(fromIndex, toIndex);
    }
}
