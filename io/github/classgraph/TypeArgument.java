// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.Parser;

public final class TypeArgument extends HierarchicalTypeSignature
{
    private final /* synthetic */ Wildcard wildcard;
    private final /* synthetic */ ReferenceTypeSignature typeSignature;
    
    private static TypeArgument parse(final Parser parser, final String s) throws ParseException {
        final char peek = parser.peek();
        if (peek == '*') {
            parser.expect('*');
            return new TypeArgument(Wildcard.ANY, null);
        }
        if (peek == '+') {
            parser.expect('+');
            final ReferenceTypeSignature referenceTypeSignature = ReferenceTypeSignature.parseReferenceTypeSignature(parser, s);
            if (referenceTypeSignature == null) {
                throw new ParseException(parser, "Missing '+' type bound");
            }
            return new TypeArgument(Wildcard.EXTENDS, referenceTypeSignature);
        }
        else if (peek == '-') {
            parser.expect('-');
            final ReferenceTypeSignature referenceTypeSignature2 = ReferenceTypeSignature.parseReferenceTypeSignature(parser, s);
            if (referenceTypeSignature2 == null) {
                throw new ParseException(parser, "Missing '-' type bound");
            }
            return new TypeArgument(Wildcard.SUPER, referenceTypeSignature2);
        }
        else {
            final ReferenceTypeSignature referenceTypeSignature3 = ReferenceTypeSignature.parseReferenceTypeSignature(parser, s);
            if (referenceTypeSignature3 == null) {
                throw new ParseException(parser, "Missing type bound");
            }
            return new TypeArgument(Wildcard.NONE, referenceTypeSignature3);
        }
    }
    
    @Override
    public String toString() {
        return this.toStringInternal(false);
    }
    
    @Override
    protected String getClassName() {
        throw new IllegalArgumentException("getClassName() cannot be called here");
    }
    
    public void findReferencedClassNames(final Set<String> set) {
        if (this.typeSignature != null) {
            this.typeSignature.findReferencedClassNames(set);
        }
    }
    
    private String toStringInternal(final boolean b) {
        switch (this.wildcard) {
            case ANY: {
                return "?";
            }
            case EXTENDS: {
                final String string = this.typeSignature.toString();
                return string.equals("java.lang.Object") ? "?" : ("? extends " + string);
            }
            case SUPER: {
                return "? super " + this.typeSignature.toString();
            }
            case NONE: {
                return b ? this.typeSignature.toStringWithSimpleNames() : this.typeSignature.toString();
            }
            default: {
                throw ClassGraphException.newClassGraphException("Unknown wildcard type " + this.wildcard);
            }
        }
    }
    
    private TypeArgument(final Wildcard wildcard, final ReferenceTypeSignature typeSignature) {
        this.wildcard = wildcard;
        this.typeSignature = typeSignature;
    }
    
    @Override
    public int hashCode() {
        return this.typeSignature.hashCode() + 7 * this.wildcard.hashCode();
    }
    
    protected ClassInfo getClassInfo() {
        throw new IllegalArgumentException("getClassInfo() cannot be called here");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TypeArgument)) {
            return false;
        }
        final TypeArgument typeArgument = (TypeArgument)o;
        return typeArgument.typeSignature.equals(this.typeSignature) && typeArgument.wildcard.equals(this.wildcard);
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
    }
    
    static List<TypeArgument> parseList(final Parser parser, final String s) throws ParseException {
        if (parser.peek() == '<') {
            parser.expect('<');
            final ArrayList<TypeArgument> list = new ArrayList<TypeArgument>(2);
            while (parser.peek() != '>') {
                if (!parser.hasMore()) {
                    throw new ParseException(parser, "Missing '>'");
                }
                list.add(parse(parser, s));
            }
            parser.expect('>');
            return list;
        }
        return Collections.emptyList();
    }
    
    public ReferenceTypeSignature getTypeSignature() {
        return this.typeSignature;
    }
    
    public String toStringWithSimpleNames() {
        return this.toStringInternal(true);
    }
    
    public Wildcard getWildcard() {
        return this.wildcard;
    }
    
    public enum Wildcard
    {
        SUPER, 
        NONE, 
        EXTENDS, 
        ANY;
    }
}
