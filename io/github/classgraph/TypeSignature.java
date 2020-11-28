// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Set;

public abstract class TypeSignature extends HierarchicalTypeSignature
{
    protected void findReferencedClassNames(final Set<String> set) {
        final String className = this.getClassName();
        if (className != null && !className.isEmpty()) {
            set.add(this.getClassName());
        }
    }
    
    static TypeSignature parse(final String s, final String s2) throws ParseException {
        final Parser parser = new Parser(s);
        final TypeSignature parse = parse(parser, s2);
        if (parse == null) {
            throw new ParseException(parser, "Could not parse type signature");
        }
        if (parser.hasMore()) {
            throw new ParseException(parser, "Extra characters at end of type descriptor");
        }
        return parse;
    }
    
    protected abstract String toStringInternal(final boolean p0);
    
    @Override
    protected final void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final HashSet<String> set2 = new HashSet<String>();
        this.findReferencedClassNames(set2);
        final Iterator<Object> iterator = set2.iterator();
        while (iterator.hasNext()) {
            final ClassInfo orCreateClassInfo = ClassInfo.getOrCreateClassInfo(iterator.next(), map);
            orCreateClassInfo.scanResult = this.scanResult;
            set.add(orCreateClassInfo);
        }
    }
    
    @Override
    public String toString() {
        return this.toStringInternal(false);
    }
    
    static TypeSignature parse(final Parser parser, final String s) throws ParseException {
        final ReferenceTypeSignature referenceTypeSignature = ReferenceTypeSignature.parseReferenceTypeSignature(parser, s);
        if (referenceTypeSignature != null) {
            return referenceTypeSignature;
        }
        final BaseTypeSignature parse = BaseTypeSignature.parse(parser);
        if (parse != null) {
            return parse;
        }
        return null;
    }
    
    public abstract boolean equalsIgnoringTypeParams(final TypeSignature p0);
    
    public String toStringWithSimpleNames() {
        return this.toStringInternal(true);
    }
    
    protected TypeSignature() {
    }
}
