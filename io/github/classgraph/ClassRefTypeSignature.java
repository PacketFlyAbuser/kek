// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collections;
import java.util.ArrayList;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.TypeUtils;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

public final class ClassRefTypeSignature extends ClassRefOrTypeVariableSignature
{
    private /* synthetic */ String fullyQualifiedClassName;
    private final /* synthetic */ List<TypeArgument> typeArguments;
    final /* synthetic */ String className;
    private final /* synthetic */ List<String> suffixes;
    private final /* synthetic */ List<List<TypeArgument>> suffixTypeArguments;
    
    @Override
    protected void findReferencedClassNames(final Set<String> set) {
        set.add(this.getFullyQualifiedClassName());
        final Iterator<TypeArgument> iterator = this.typeArguments.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassNames(set);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClassRefTypeSignature)) {
            return false;
        }
        final ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature)o;
        return classRefTypeSignature.className.equals(this.className) && classRefTypeSignature.typeArguments.equals(this.typeArguments) && classRefTypeSignature.suffixes.equals(this.suffixes);
    }
    
    public String getFullyQualifiedClassName() {
        if (this.fullyQualifiedClassName == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.className);
            for (final String str : this.suffixes) {
                sb.append('$');
                sb.append(str);
            }
            this.fullyQualifiedClassName = sb.toString();
        }
        return this.fullyQualifiedClassName;
    }
    
    @Override
    public int hashCode() {
        return this.className.hashCode() + 7 * this.typeArguments.hashCode() + 15 * this.suffixes.hashCode();
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeArguments != null) {
            final Iterator<TypeArgument> iterator = this.typeArguments.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
        if (this.suffixTypeArguments != null) {
            final Iterator<List<TypeArgument>> iterator2 = this.suffixTypeArguments.iterator();
            while (iterator2.hasNext()) {
                final Iterator<TypeArgument> iterator3 = iterator2.next().iterator();
                while (iterator3.hasNext()) {
                    iterator3.next().setScanResult(scanResult);
                }
            }
        }
    }
    
    public String getBaseClassName() {
        return this.className;
    }
    
    @Override
    protected String getClassName() {
        return this.getFullyQualifiedClassName();
    }
    
    static ClassRefTypeSignature parse(final Parser parser, final String s) throws ParseException {
        if (parser.peek() != 'L') {
            return null;
        }
        parser.next();
        if (!TypeUtils.getIdentifierToken(parser, '/', '.')) {
            throw new ParseException(parser, "Could not parse identifier token");
        }
        final String currToken = parser.currToken();
        final List<TypeArgument> list = TypeArgument.parseList(parser, s);
        List<String> emptyList;
        List<List<TypeArgument>> emptyList2;
        if (parser.peek() == '.') {
            emptyList = new ArrayList<String>();
            emptyList2 = new ArrayList<List<TypeArgument>>();
            while (parser.peek() == '.') {
                parser.expect('.');
                if (!TypeUtils.getIdentifierToken(parser, '/', '.')) {
                    throw new ParseException(parser, "Could not parse identifier token");
                }
                emptyList.add(parser.currToken());
                emptyList2.add(TypeArgument.parseList(parser, s));
            }
        }
        else {
            emptyList = Collections.emptyList();
            emptyList2 = Collections.emptyList();
        }
        parser.expect(';');
        return new ClassRefTypeSignature(currToken, list, emptyList, emptyList2);
    }
    
    @Override
    protected String toStringInternal(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        if (!b || this.suffixes.isEmpty()) {
            sb.append(b ? ClassInfo.getSimpleName(this.className) : this.className);
            if (!this.typeArguments.isEmpty()) {
                sb.append('<');
                for (int i = 0; i < this.typeArguments.size(); ++i) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(b ? this.typeArguments.get(i).toStringWithSimpleNames() : this.typeArguments.get(i).toString());
                }
                sb.append('>');
            }
        }
        for (int j = (b && !this.suffixes.isEmpty()) ? (this.suffixes.size() - 1) : 0; j < this.suffixes.size(); ++j) {
            if (!b) {
                sb.append('.');
            }
            sb.append(this.suffixes.get(j));
            final List<TypeArgument> list = this.suffixTypeArguments.get(j);
            if (!list.isEmpty()) {
                sb.append('<');
                for (int k = 0; k < list.size(); ++k) {
                    if (k > 0) {
                        sb.append(", ");
                    }
                    sb.append(b ? list.get(k).toStringWithSimpleNames() : list.get(k).toString());
                }
                sb.append('>');
            }
        }
        return sb.toString();
    }
    
    public Class<?> loadClass() {
        return super.loadClass();
    }
    
    public Class<?> loadClass(final boolean b) {
        return super.loadClass(b);
    }
    
    public List<TypeArgument> getTypeArguments() {
        return this.typeArguments;
    }
    
    public ClassInfo getClassInfo() {
        return super.getClassInfo();
    }
    
    @Override
    public boolean equalsIgnoringTypeParams(final TypeSignature typeSignature) {
        if (typeSignature instanceof TypeVariableSignature) {
            return typeSignature.equalsIgnoringTypeParams(this);
        }
        if (!(typeSignature instanceof ClassRefTypeSignature)) {
            return false;
        }
        final ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature)typeSignature;
        if (classRefTypeSignature.suffixes.equals(this.suffixes)) {
            return classRefTypeSignature.className.equals(this.className);
        }
        return classRefTypeSignature.getFullyQualifiedClassName().equals(this.getFullyQualifiedClassName());
    }
    
    public List<String> getSuffixes() {
        return this.suffixes;
    }
    
    public List<List<TypeArgument>> getSuffixTypeArguments() {
        return this.suffixTypeArguments;
    }
    
    private ClassRefTypeSignature(final String className, final List<TypeArgument> typeArguments, final List<String> suffixes, final List<List<TypeArgument>> suffixTypeArguments) {
        this.className = className;
        this.typeArguments = typeArguments;
        this.suffixes = suffixes;
        this.suffixTypeArguments = suffixTypeArguments;
    }
}
