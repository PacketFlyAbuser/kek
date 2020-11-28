// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.types.Parser;
import java.util.Set;

public class BaseTypeSignature extends TypeSignature
{
    private static final /* synthetic */ BaseTypeSignature INT;
    private static final /* synthetic */ BaseTypeSignature SHORT;
    static final /* synthetic */ BaseTypeSignature VOID;
    private final /* synthetic */ String typeSignatureChar;
    private static final /* synthetic */ BaseTypeSignature CHAR;
    private static final /* synthetic */ BaseTypeSignature LONG;
    private static final /* synthetic */ BaseTypeSignature DOUBLE;
    private static final /* synthetic */ BaseTypeSignature FLOAT;
    private static final /* synthetic */ BaseTypeSignature BOOLEAN;
    private static final /* synthetic */ BaseTypeSignature BYTE;
    private final /* synthetic */ String baseType;
    
    public String getTypeSignatureChar() {
        return this.typeSignatureChar;
    }
    
    private BaseTypeSignature(final String baseType, final char c) {
        this.baseType = baseType;
        this.typeSignatureChar = Character.toString(c);
    }
    
    public static BaseTypeSignature getTypeSignature(final String s) {
        switch (s) {
            case "byte": {
                return BaseTypeSignature.BYTE;
            }
            case "char": {
                return BaseTypeSignature.CHAR;
            }
            case "double": {
                return BaseTypeSignature.DOUBLE;
            }
            case "float": {
                return BaseTypeSignature.FLOAT;
            }
            case "int": {
                return BaseTypeSignature.INT;
            }
            case "long": {
                return BaseTypeSignature.LONG;
            }
            case "short": {
                return BaseTypeSignature.SHORT;
            }
            case "boolean": {
                return BaseTypeSignature.BOOLEAN;
            }
            case "void": {
                return BaseTypeSignature.VOID;
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof BaseTypeSignature && ((BaseTypeSignature)o).baseType.equals(this.baseType));
    }
    
    @Override
    protected String toStringInternal(final boolean b) {
        return this.baseType;
    }
    
    @Override
    protected String getClassName() {
        return this.baseType;
    }
    
    public Class<?> getType() {
        final String baseType = this.baseType;
        switch (baseType) {
            case "byte": {
                return Byte.TYPE;
            }
            case "char": {
                return Character.TYPE;
            }
            case "double": {
                return Double.TYPE;
            }
            case "float": {
                return Float.TYPE;
            }
            case "int": {
                return Integer.TYPE;
            }
            case "long": {
                return Long.TYPE;
            }
            case "short": {
                return Short.TYPE;
            }
            case "boolean": {
                return Boolean.TYPE;
            }
            case "void": {
                return Void.TYPE;
            }
            default: {
                throw new IllegalArgumentException("Unknown base type " + this.baseType);
            }
        }
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
    }
    
    @Override
    protected void findReferencedClassNames(final Set<String> set) {
    }
    
    protected ClassInfo getClassInfo() {
        return null;
    }
    
    @Override
    public boolean equalsIgnoringTypeParams(final TypeSignature typeSignature) {
        return typeSignature instanceof BaseTypeSignature && this.baseType.equals(((BaseTypeSignature)typeSignature).baseType);
    }
    
    @Override
    public int hashCode() {
        return this.baseType.hashCode();
    }
    
    @Override
    Class<?> loadClass() {
        return this.getType();
    }
    
    static {
        BYTE = new BaseTypeSignature("byte", 'B');
        CHAR = new BaseTypeSignature("char", 'C');
        DOUBLE = new BaseTypeSignature("double", 'D');
        FLOAT = new BaseTypeSignature("float", 'F');
        INT = new BaseTypeSignature("int", 'I');
        LONG = new BaseTypeSignature("long", 'J');
        SHORT = new BaseTypeSignature("short", 'S');
        BOOLEAN = new BaseTypeSignature("boolean", 'Z');
        VOID = new BaseTypeSignature("void", 'V');
    }
    
    @Override
     <T> Class<T> loadClass(final Class<T> clazz) {
        final Class<?> type = this.getType();
        if (!clazz.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Primitive class " + this.baseType + " cannot be cast to " + clazz.getName());
        }
        return (Class<T>)type;
    }
    
    public String getTypeStr() {
        return this.baseType;
    }
    
    static BaseTypeSignature parse(final Parser parser) {
        switch (parser.peek()) {
            case 'B': {
                parser.next();
                return BaseTypeSignature.BYTE;
            }
            case 'C': {
                parser.next();
                return BaseTypeSignature.CHAR;
            }
            case 'D': {
                parser.next();
                return BaseTypeSignature.DOUBLE;
            }
            case 'F': {
                parser.next();
                return BaseTypeSignature.FLOAT;
            }
            case 'I': {
                parser.next();
                return BaseTypeSignature.INT;
            }
            case 'J': {
                parser.next();
                return BaseTypeSignature.LONG;
            }
            case 'S': {
                parser.next();
                return BaseTypeSignature.SHORT;
            }
            case 'Z': {
                parser.next();
                return BaseTypeSignature.BOOLEAN;
            }
            case 'V': {
                parser.next();
                return BaseTypeSignature.VOID;
            }
            default: {
                return null;
            }
        }
    }
}
