// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

public class ReferenceEqualityKey<K>
{
    private final /* synthetic */ K wrappedKey;
    
    public ReferenceEqualityKey(final K wrappedKey) {
        this.wrappedKey = wrappedKey;
    }
    
    @Override
    public int hashCode() {
        final K wrappedKey = this.wrappedKey;
        return (wrappedKey == null) ? 0 : System.identityHashCode(wrappedKey);
    }
    
    public K get() {
        return this.wrappedKey;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ReferenceEqualityKey && this.wrappedKey == ((ReferenceEqualityKey)o).wrappedKey);
    }
    
    @Override
    public String toString() {
        final K wrappedKey = this.wrappedKey;
        return (wrappedKey == null) ? "null" : wrappedKey.toString();
    }
}
