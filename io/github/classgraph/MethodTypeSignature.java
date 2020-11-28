// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import nonapi.io.github.classgraph.types.ParseException;
import java.util.ArrayList;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Collections;
import java.util.List;

public final class MethodTypeSignature extends HierarchicalTypeSignature
{
    final /* synthetic */ List<TypeParameter> typeParameters;
    private final /* synthetic */ List<TypeSignature> parameterTypeSignatures;
    private final /* synthetic */ TypeSignature resultType;
    private final /* synthetic */ List<ClassRefOrTypeVariableSignature> throwsSignatures;
    
    static MethodTypeSignature parse(final String s, final String s2) throws ParseException {
        if (s.equals("<init>")) {
            return new MethodTypeSignature(Collections.emptyList(), Collections.emptyList(), BaseTypeSignature.VOID, Collections.emptyList());
        }
        final Parser parser = new Parser(s);
        final List<TypeParameter> list = TypeParameter.parseList(parser, s2);
        parser.expect('(');
        final ArrayList<TypeSignature> list2 = new ArrayList<TypeSignature>();
        while (parser.peek() != ')') {
            if (!parser.hasMore()) {
                throw new ParseException(parser, "Ran out of input while parsing method signature");
            }
            final TypeSignature parse = TypeSignature.parse(parser, s2);
            if (parse == null) {
                throw new ParseException(parser, "Missing method parameter type signature");
            }
            list2.add(parse);
        }
        parser.expect(')');
        final TypeSignature parse2 = TypeSignature.parse(parser, s2);
        if (parse2 == null) {
            throw new ParseException(parser, "Missing method result type signature");
        }
        Object emptyList;
        if (parser.peek() == '^') {
            emptyList = new ArrayList<ClassRefOrTypeVariableSignature>();
            while (parser.peek() == '^') {
                parser.expect('^');
                final ClassRefTypeSignature parse3 = ClassRefTypeSignature.parse(parser, s2);
                if (parse3 != null) {
                    ((List<ClassRefTypeSignature>)emptyList).add(parse3);
                }
                else {
                    final TypeVariableSignature parse4 = TypeVariableSignature.parse(parser, s2);
                    if (parse4 == null) {
                        throw new ParseException(parser, "Missing type variable signature");
                    }
                    ((List<ClassRefTypeSignature>)emptyList).add((ClassRefTypeSignature)parse4);
                }
            }
        }
        else {
            emptyList = Collections.emptyList();
        }
        if (parser.hasMore()) {
            throw new ParseException(parser, "Extra characters at end of type descriptor");
        }
        final MethodTypeSignature containingMethodSignature = new MethodTypeSignature(list, list2, parse2, (List<ClassRefOrTypeVariableSignature>)emptyList);
        final List list3 = (List)parser.getState();
        if (list3 != null) {
            final Iterator<TypeVariableSignature> iterator = list3.iterator();
            while (iterator.hasNext()) {
                iterator.next().containingMethodSignature = containingMethodSignature;
            }
        }
        return containingMethodSignature;
    }
    
    List<TypeSignature> getParameterTypeSignatures() {
        return this.parameterTypeSignatures;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MethodTypeSignature)) {
            return false;
        }
        final MethodTypeSignature methodTypeSignature = (MethodTypeSignature)o;
        return methodTypeSignature.typeParameters.equals(this.typeParameters) && methodTypeSignature.parameterTypeSignatures.equals(this.parameterTypeSignatures) && methodTypeSignature.resultType.equals(this.resultType) && methodTypeSignature.throwsSignatures.equals(this.throwsSignatures);
    }
    
    protected ClassInfo getClassInfo() {
        throw new IllegalArgumentException("getClassInfo() cannot be called here");
    }
    
    private MethodTypeSignature(final List<TypeParameter> typeParameters, final List<TypeSignature> parameterTypeSignatures, final TypeSignature resultType, final List<ClassRefOrTypeVariableSignature> throwsSignatures) {
        this.typeParameters = typeParameters;
        this.parameterTypeSignatures = parameterTypeSignatures;
        this.resultType = resultType;
        this.throwsSignatures = throwsSignatures;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (!this.typeParameters.isEmpty()) {
            sb.append('<');
            for (int i = 0; i < this.typeParameters.size(); ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(this.typeParameters.get(i).toString());
            }
            sb.append('>');
        }
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(this.resultType.toString());
        sb.append(" (");
        for (int j = 0; j < this.parameterTypeSignatures.size(); ++j) {
            if (j > 0) {
                sb.append(", ");
            }
            sb.append(this.parameterTypeSignatures.get(j).toString());
        }
        sb.append(')');
        if (!this.throwsSignatures.isEmpty()) {
            sb.append(" throws ");
            for (int k = 0; k < this.throwsSignatures.size(); ++k) {
                if (k > 0) {
                    sb.append(", ");
                }
                sb.append(this.throwsSignatures.get(k).toString());
            }
        }
        return sb.toString();
    }
    
    public TypeSignature getResultType() {
        return this.resultType;
    }
    
    public List<ClassRefOrTypeVariableSignature> getThrowsSignatures() {
        return this.throwsSignatures;
    }
    
    List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }
    
    @Override
    public int hashCode() {
        return this.typeParameters.hashCode() + this.parameterTypeSignatures.hashCode() * 7 + this.resultType.hashCode() * 15 + this.throwsSignatures.hashCode() * 31;
    }
    
    @Override
    protected String getClassName() {
        throw new IllegalArgumentException("getClassName() cannot be called here");
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
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
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeParameters != null) {
            final Iterator<TypeParameter> iterator = this.typeParameters.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
        if (this.parameterTypeSignatures != null) {
            final Iterator<TypeSignature> iterator2 = this.parameterTypeSignatures.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setScanResult(scanResult);
            }
        }
        if (this.resultType != null) {
            this.resultType.setScanResult(scanResult);
        }
        if (this.throwsSignatures != null) {
            final Iterator<ClassRefOrTypeVariableSignature> iterator3 = this.throwsSignatures.iterator();
            while (iterator3.hasNext()) {
                iterator3.next().setScanResult(scanResult);
            }
        }
    }
    
    protected void findReferencedClassNames(final Set<String> set) {
        for (final TypeParameter typeParameter : this.typeParameters) {
            if (typeParameter != null) {
                typeParameter.findReferencedClassNames(set);
            }
        }
        for (final TypeSignature typeSignature : this.parameterTypeSignatures) {
            if (typeSignature != null) {
                typeSignature.findReferencedClassNames(set);
            }
        }
        this.resultType.findReferencedClassNames(set);
        for (final ClassRefOrTypeVariableSignature classRefOrTypeVariableSignature : this.throwsSignatures) {
            if (classRefOrTypeVariableSignature != null) {
                classRefOrTypeVariableSignature.findReferencedClassNames(set);
            }
        }
    }
}
