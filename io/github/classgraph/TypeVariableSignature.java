// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.TypeUtils;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Iterator;

public final class TypeVariableSignature extends ClassRefOrTypeVariableSignature
{
    /* synthetic */ MethodTypeSignature containingMethodSignature;
    private final /* synthetic */ String definingClassName;
    private final /* synthetic */ String name;
    
    public String getName() {
        return this.name;
    }
    
    public TypeParameter resolve() {
        if (this.containingMethodSignature != null && this.containingMethodSignature.typeParameters != null && !this.containingMethodSignature.typeParameters.isEmpty()) {
            for (final TypeParameter typeParameter : this.containingMethodSignature.typeParameters) {
                if (typeParameter.name.equals(this.name)) {
                    return typeParameter;
                }
            }
        }
        final ClassInfo classInfo = this.getClassInfo();
        if (classInfo == null) {
            throw new IllegalArgumentException("Could not find ClassInfo object for " + this.definingClassName);
        }
        final ClassTypeSignature typeSignature = classInfo.getTypeSignature();
        if (typeSignature != null && typeSignature.typeParameters != null && !typeSignature.typeParameters.isEmpty()) {
            for (final TypeParameter typeParameter2 : typeSignature.typeParameters) {
                if (typeParameter2.name.equals(this.name)) {
                    return typeParameter2;
                }
            }
        }
        throw new IllegalArgumentException("Could not resolve " + this.name + " against parameters of the defining method or enclosing class");
    }
    
    private TypeVariableSignature(final String name, final String definingClassName) {
        this.name = name;
        this.definingClassName = definingClassName;
    }
    
    @Override
    public boolean equalsIgnoringTypeParams(final TypeSignature typeSignature) {
        if (!(typeSignature instanceof ClassRefTypeSignature)) {
            return this.equals(typeSignature);
        }
        if (((ClassRefTypeSignature)typeSignature).className.equals("java.lang.Object")) {
            return true;
        }
        TypeParameter resolve;
        try {
            resolve = this.resolve();
        }
        catch (IllegalArgumentException ex) {
            return true;
        }
        if (resolve.classBound == null && (resolve.interfaceBounds == null || resolve.interfaceBounds.isEmpty())) {
            return true;
        }
        if (resolve.classBound != null) {
            if (!(resolve.classBound instanceof ClassRefTypeSignature)) {
                return resolve.classBound instanceof TypeVariableSignature && this.equalsIgnoringTypeParams(resolve.classBound);
            }
            if (resolve.classBound.equals(typeSignature)) {
                return true;
            }
        }
        if (resolve.interfaceBounds != null) {
            for (final ReferenceTypeSignature referenceTypeSignature : resolve.interfaceBounds) {
                if (!(referenceTypeSignature instanceof ClassRefTypeSignature)) {
                    return referenceTypeSignature instanceof TypeVariableSignature && this.equalsIgnoringTypeParams(referenceTypeSignature);
                }
                if (referenceTypeSignature.equals(typeSignature)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    protected String getClassName() {
        return this.definingClassName;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public String toStringWithTypeBound() {
        try {
            return this.resolve().toString();
        }
        catch (IllegalArgumentException ex) {
            return this.name;
        }
    }
    
    @Override
    protected String toStringInternal(final boolean b) {
        return this.name;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof TypeVariableSignature && ((TypeVariableSignature)o).name.equals(this.name));
    }
    
    static TypeVariableSignature parse(final Parser parser, final String s) throws ParseException {
        if (parser.peek() != 'T') {
            return null;
        }
        parser.next();
        if (!TypeUtils.getIdentifierToken(parser)) {
            throw new ParseException(parser, "Could not parse type variable signature");
        }
        parser.expect(';');
        final TypeVariableSignature typeVariableSignature = new TypeVariableSignature(parser.currToken(), s);
        List<TypeVariableSignature> list = (List<TypeVariableSignature>)parser.getState();
        if (list == null) {
            parser.setState(list = new ArrayList<TypeVariableSignature>());
        }
        list.add(typeVariableSignature);
        return typeVariableSignature;
    }
    
    @Override
    protected void findReferencedClassNames(final Set<String> set) {
    }
}
