// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collections;
import nonapi.io.github.classgraph.types.ParseException;
import java.util.ArrayList;
import nonapi.io.github.classgraph.types.Parser;
import nonapi.io.github.classgraph.utils.Join;
import nonapi.io.github.classgraph.types.TypeUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

public final class ClassTypeSignature extends HierarchicalTypeSignature
{
    private final /* synthetic */ ClassRefTypeSignature superclassSignature;
    private final /* synthetic */ List<ClassRefTypeSignature> superinterfaceSignatures;
    final /* synthetic */ List<TypeParameter> typeParameters;
    private final /* synthetic */ ClassInfo classInfo;
    
    protected void findReferencedClassNames(final Set<String> set) {
        final Iterator<TypeParameter> iterator = this.typeParameters.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassNames(set);
        }
        if (this.superclassSignature != null) {
            this.superclassSignature.findReferencedClassNames(set);
        }
        final Iterator<ClassRefTypeSignature> iterator2 = this.superinterfaceSignatures.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().findReferencedClassNames(set);
        }
    }
    
    @Override
    public int hashCode() {
        return this.typeParameters.hashCode() + this.superclassSignature.hashCode() * 7 + this.superinterfaceSignatures.hashCode() * 15;
    }
    
    @Override
    protected String getClassName() {
        return (this.classInfo != null) ? this.classInfo.getName() : null;
    }
    
    private ClassTypeSignature(final ClassInfo classInfo, final List<TypeParameter> typeParameters, final ClassRefTypeSignature superclassSignature, final List<ClassRefTypeSignature> superinterfaceSignatures) {
        this.classInfo = classInfo;
        this.typeParameters = typeParameters;
        this.superclassSignature = superclassSignature;
        this.superinterfaceSignatures = superinterfaceSignatures;
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
    
    public List<TypeParameter> getTypeParameters() {
        return this.typeParameters;
    }
    
    String toString(final String str, final boolean b, final int n, final boolean b2, final boolean b3) {
        final StringBuilder sb = new StringBuilder();
        if (!b) {
            if (n != 0) {
                TypeUtils.modifiersToString(n, TypeUtils.ModifierType.CLASS, false, sb);
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(b2 ? "@interface" : (b3 ? "interface" : (((n & 0x4000) != 0x0) ? "enum" : "class")));
            sb.append(' ');
        }
        if (str != null) {
            sb.append(str);
        }
        if (!this.typeParameters.isEmpty()) {
            Join.join(sb, "<", ", ", ">", this.typeParameters);
        }
        if (!b) {
            if (this.superclassSignature != null) {
                final String string = this.superclassSignature.toString();
                if (!string.equals("java.lang.Object")) {
                    sb.append(" extends ");
                    sb.append(string);
                }
            }
            if (!this.superinterfaceSignatures.isEmpty()) {
                sb.append(b3 ? " extends " : " implements ");
                Join.join(sb, "", ", ", "", this.superinterfaceSignatures);
            }
        }
        return sb.toString();
    }
    
    static ClassTypeSignature parse(final String s, final ClassInfo classInfo) throws ParseException {
        final Parser parser = new Parser(s);
        final String s2 = null;
        final List<TypeParameter> list = TypeParameter.parseList(parser, s2);
        final ClassRefTypeSignature parse = ClassRefTypeSignature.parse(parser, s2);
        List<ClassRefTypeSignature> emptyList;
        if (parser.hasMore()) {
            emptyList = new ArrayList<ClassRefTypeSignature>();
            while (parser.hasMore()) {
                final ClassRefTypeSignature parse2 = ClassRefTypeSignature.parse(parser, s2);
                if (parse2 == null) {
                    throw new ParseException(parser, "Could not parse superinterface signature");
                }
                emptyList.add(parse2);
            }
        }
        else {
            emptyList = Collections.emptyList();
        }
        if (parser.hasMore()) {
            throw new ParseException(parser, "Extra characters at end of type descriptor");
        }
        return new ClassTypeSignature(classInfo, list, parse, emptyList);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClassTypeSignature)) {
            return false;
        }
        final ClassTypeSignature classTypeSignature = (ClassTypeSignature)o;
        return classTypeSignature.typeParameters.equals(this.typeParameters) && classTypeSignature.superclassSignature.equals(this.superclassSignature) && classTypeSignature.superinterfaceSignatures.equals(this.superinterfaceSignatures);
    }
    
    @Override
    public String toString() {
        return this.toString(this.classInfo.getName(), false, this.classInfo.getModifiers(), this.classInfo.isAnnotation(), this.classInfo.isInterface());
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
        if (this.superclassSignature != null) {
            this.superclassSignature.setScanResult(scanResult);
        }
        if (this.superinterfaceSignatures != null) {
            final Iterator<ClassRefTypeSignature> iterator2 = this.superinterfaceSignatures.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setScanResult(scanResult);
            }
        }
    }
    
    public ClassRefTypeSignature getSuperclassSignature() {
        return this.superclassSignature;
    }
    
    protected ClassInfo getClassInfo() {
        return this.classInfo;
    }
    
    public List<ClassRefTypeSignature> getSuperinterfaceSignatures() {
        return this.superinterfaceSignatures;
    }
}
