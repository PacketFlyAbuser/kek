// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;
import java.util.ArrayList;

class PotentiallyUnmodifiableList<T> extends ArrayList<T>
{
    /* synthetic */ boolean modifiable;
    
    PotentiallyUnmodifiableList(final int initialCapacity) {
        super(initialCapacity);
        this.modifiable = true;
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        if (!this.modifiable && !c.isEmpty()) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.addAll(index, c);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        if (!this.modifiable && !c.isEmpty()) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.removeAll(c);
    }
    
    PotentiallyUnmodifiableList() {
        this.modifiable = true;
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {
            final /* synthetic */ ListIterator val$iterator = super.listIterator();
            
            @Override
            public boolean hasNext() {
                return !PotentiallyUnmodifiableList.this.isEmpty() && this.val$iterator.hasNext();
            }
            
            @Override
            public boolean hasPrevious() {
                return !PotentiallyUnmodifiableList.this.isEmpty() && this.val$iterator.hasPrevious();
            }
            
            @Override
            public T previous() {
                return this.val$iterator.previous();
            }
            
            @Override
            public int nextIndex() {
                if (PotentiallyUnmodifiableList.this.isEmpty()) {
                    return 0;
                }
                return this.val$iterator.nextIndex();
            }
            
            @Override
            public T next() {
                return this.val$iterator.next();
            }
            
            @Override
            public void remove() {
                if (!PotentiallyUnmodifiableList.this.modifiable) {
                    throw new IllegalArgumentException("List is immutable");
                }
                this.val$iterator.remove();
            }
            
            @Override
            public void set(final T t) {
                if (!PotentiallyUnmodifiableList.this.modifiable) {
                    throw new IllegalArgumentException("List is immutable");
                }
                this.val$iterator.set(t);
            }
            
            @Override
            public void add(final T t) {
                if (!PotentiallyUnmodifiableList.this.modifiable) {
                    throw new IllegalArgumentException("List is immutable");
                }
                this.val$iterator.add(t);
            }
            
            @Override
            public int previousIndex() {
                if (PotentiallyUnmodifiableList.this.isEmpty()) {
                    return -1;
                }
                return this.val$iterator.previousIndex();
            }
        };
    }
    
    PotentiallyUnmodifiableList(final Collection<T> c) {
        super(c);
        this.modifiable = true;
    }
    
    @Override
    public void add(final int index, final T element) {
        if (!this.modifiable) {
            throw new IllegalArgumentException("List is immutable");
        }
        super.add(index, element);
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (!this.modifiable && !c.isEmpty()) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.addAll(c);
    }
    
    @Override
    public T set(final int index, final T element) {
        if (!this.modifiable) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.set(index, element);
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final /* synthetic */ Iterator val$iterator = super.iterator();
            
            @Override
            public void remove() {
                if (!PotentiallyUnmodifiableList.this.modifiable) {
                    throw new IllegalArgumentException("List is immutable");
                }
                this.val$iterator.remove();
            }
            
            @Override
            public T next() {
                return this.val$iterator.next();
            }
            
            @Override
            public boolean hasNext() {
                return !PotentiallyUnmodifiableList.this.isEmpty() && this.val$iterator.hasNext();
            }
        };
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public boolean remove(final Object o) {
        if (!this.modifiable) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.remove(o);
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        if (!this.modifiable && !this.isEmpty()) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.retainAll(c);
    }
    
    void makeUnmodifiable() {
        this.modifiable = false;
    }
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    @Override
    public void clear() {
        if (!this.modifiable && !this.isEmpty()) {
            throw new IllegalArgumentException("List is immutable");
        }
        super.clear();
    }
    
    @Override
    public T remove(final int index) {
        if (!this.modifiable) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.remove(index);
    }
    
    @Override
    public boolean add(final T e) {
        if (!this.modifiable) {
            throw new IllegalArgumentException("List is immutable");
        }
        return super.add(e);
    }
}
