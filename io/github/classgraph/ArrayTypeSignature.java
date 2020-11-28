// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.lang.reflect.Array;
import java.util.Set;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Map;

public class ArrayTypeSignature extends ReferenceTypeSignature
{
    private final /* synthetic */ String typeSignatureStr;
    private /* synthetic */ ArrayClassInfo arrayClassInfo;
    private final /* synthetic */ int numDims;
    private final /* synthetic */ TypeSignature elementTypeSignature;
    private /* synthetic */ Class<?> elementClassRef;
    private /* synthetic */ String className;
    
    @Override
    public boolean equalsIgnoringTypeParams(final TypeSignature typeSignature) {
        if (this == typeSignature) {
            return true;
        }
        if (!(typeSignature instanceof ArrayTypeSignature)) {
            return false;
        }
        final ArrayTypeSignature arrayTypeSignature = (ArrayTypeSignature)typeSignature;
        return arrayTypeSignature.elementTypeSignature.equalsIgnoringTypeParams(this.elementTypeSignature) && arrayTypeSignature.numDims == this.numDims;
    }
    
    ArrayTypeSignature(final TypeSignature elementTypeSignature, final int numDims, final String typeSignatureStr) {
        this.elementTypeSignature = elementTypeSignature;
        this.numDims = numDims;
        this.typeSignatureStr = typeSignatureStr;
    }
    
    @Override
    protected String toStringInternal(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(b ? this.elementTypeSignature.toStringWithSimpleNames() : this.elementTypeSignature.toString());
        for (int i = 0; i < this.numDims; ++i) {
            sb.append("[]");
        }
        return sb.toString();
    }
    
    public TypeSignature getElementTypeSignature() {
        return this.elementTypeSignature;
    }
    
    public ArrayClassInfo getArrayClassInfo() {
        if (this.arrayClassInfo == null) {
            if (this.scanResult != null) {
                final String className = this.getClassName();
                this.arrayClassInfo = (ArrayClassInfo)this.scanResult.classNameToClassInfo.get(className);
                if (this.arrayClassInfo == null) {
                    final Map<String, ClassInfo> classNameToClassInfo = this.scanResult.classNameToClassInfo;
                    final String s = className;
                    final ArrayClassInfo arrayClassInfo = new ArrayClassInfo(this);
                    this.arrayClassInfo = arrayClassInfo;
                    classNameToClassInfo.put(s, arrayClassInfo);
                    this.arrayClassInfo.setScanResult(this.scanResult);
                }
            }
            else {
                this.arrayClassInfo = new ArrayClassInfo(this);
            }
        }
        return this.arrayClassInfo;
    }
    
    ArrayTypeSignature(final String s, final int numDims) {
        final BaseTypeSignature typeSignature = BaseTypeSignature.getTypeSignature(s);
        String str;
        if (typeSignature != null) {
            str = typeSignature.getTypeSignatureChar();
            this.elementTypeSignature = typeSignature;
        }
        else {
            str = "L" + s.replace('.', '/') + ";";
            try {
                this.elementTypeSignature = ClassRefTypeSignature.parse(new Parser(str), null);
                if (this.elementTypeSignature == null) {
                    throw new IllegalArgumentException("Could not form array base type signature for class " + s);
                }
            }
            catch (ParseException ex) {
                throw new IllegalArgumentException("Could not form array base type signature for class " + s);
            }
        }
        final StringBuilder sb = new StringBuilder(numDims + str.length());
        for (int i = 0; i < numDims; ++i) {
            sb.append('[');
        }
        sb.append(str);
        this.typeSignatureStr = sb.toString();
        this.numDims = numDims;
    }
    
    static ArrayTypeSignature parse(final Parser parser, final String s) throws ParseException {
        int n = 0;
        final int position = parser.getPosition();
        while (parser.peek() == '[') {
            ++n;
            parser.next();
        }
        if (n <= 0) {
            return null;
        }
        final TypeSignature parse = TypeSignature.parse(parser, s);
        if (parse == null) {
            throw new ParseException(parser, "elementTypeSignature == null");
        }
        return new ArrayTypeSignature(parse, n, parser.getSubsequence(position, parser.getPosition()).toString());
    }
    
    public String getTypeSignatureStr() {
        return this.typeSignatureStr;
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.elementTypeSignature != null) {
            this.elementTypeSignature.setScanResult(scanResult);
        }
        if (this.arrayClassInfo != null) {
            this.arrayClassInfo.setScanResult(scanResult);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayTypeSignature)) {
            return false;
        }
        final ArrayTypeSignature arrayTypeSignature = (ArrayTypeSignature)o;
        return arrayTypeSignature.elementTypeSignature.equals(this.elementTypeSignature) && arrayTypeSignature.numDims == this.numDims;
    }
    
    @Override
    protected void findReferencedClassNames(final Set<String> set) {
        this.elementTypeSignature.findReferencedClassNames(set);
    }
    
    protected ClassInfo getClassInfo() {
        return this.getArrayClassInfo();
    }
    
    public Class<?> loadClass() {
        return this.loadClass(false);
    }
    
    public Class<?> loadElementClass() {
        return this.loadElementClass(false);
    }
    
    @Override
    public int hashCode() {
        return this.elementTypeSignature.hashCode() + this.numDims * 15;
    }
    
    @Override
    protected String getClassName() {
        if (this.className == null) {
            this.className = this.toStringInternal(false);
        }
        return this.className;
    }
    
    public Class<?> loadElementClass(final boolean b) {
        if (this.elementClassRef == null) {
            if (this.elementTypeSignature instanceof BaseTypeSignature) {
                this.elementClassRef = ((BaseTypeSignature)this.elementTypeSignature).getType();
            }
            else if (this.scanResult != null) {
                this.elementClassRef = this.elementTypeSignature.loadClass(b);
            }
            else {
                final String fullyQualifiedClassName = ((ClassRefTypeSignature)this.elementTypeSignature).getFullyQualifiedClassName();
                try {
                    this.elementClassRef = Class.forName(fullyQualifiedClassName);
                }
                catch (Throwable cause) {
                    if (!b) {
                        throw new IllegalArgumentException("Could not load array element class " + fullyQualifiedClassName, cause);
                    }
                }
            }
        }
        return this.elementClassRef;
    }
    
    public Class<?> loadClass(final boolean b) {
        if (this.classRef == null) {
            Class<?> componentType = null;
            Label_0029: {
                if (b) {
                    try {
                        componentType = this.loadElementClass();
                        break Label_0029;
                    }
                    catch (IllegalArgumentException ex) {
                        return null;
                    }
                }
                componentType = this.loadElementClass();
            }
            if (componentType == null) {
                throw new IllegalArgumentException("Could not load array element class " + this.elementTypeSignature);
            }
            this.classRef = Array.newInstance(componentType, new int[this.numDims]).getClass();
        }
        return this.classRef;
    }
    
    public int getNumDimensions() {
        return this.numDims;
    }
}
