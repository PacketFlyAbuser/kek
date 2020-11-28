// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.lang.reflect.Array;

class ObjectTypedValueWrapper extends ScanResultObject
{
    private /* synthetic */ ObjectTypedValueWrapper[] objectArrayValue;
    private /* synthetic */ AnnotationClassRef annotationClassRef;
    private /* synthetic */ Character characterValue;
    private /* synthetic */ Short shortValue;
    private /* synthetic */ Boolean booleanValue;
    private /* synthetic */ float[] floatArrayValue;
    private /* synthetic */ Double doubleValue;
    private /* synthetic */ short[] shortArrayValue;
    private /* synthetic */ AnnotationInfo annotationInfo;
    private /* synthetic */ double[] doubleArrayValue;
    private /* synthetic */ long[] longArrayValue;
    private /* synthetic */ String[] stringArrayValue;
    private /* synthetic */ AnnotationEnumValue annotationEnumValue;
    private /* synthetic */ int[] intArrayValue;
    private /* synthetic */ char[] charArrayValue;
    private /* synthetic */ Byte byteValue;
    private /* synthetic */ boolean[] booleanArrayValue;
    private /* synthetic */ Integer integerValue;
    private /* synthetic */ byte[] byteArrayValue;
    private /* synthetic */ String stringValue;
    private /* synthetic */ Long longValue;
    private /* synthetic */ Float floatValue;
    
    void convertWrapperArraysToPrimitiveArrays(final ClassInfo classInfo, final String s) {
        if (this.annotationInfo != null) {
            this.annotationInfo.convertWrapperArraysToPrimitiveArrays();
        }
        else if (this.objectArrayValue != null) {
            for (final ObjectTypedValueWrapper objectTypedValueWrapper : this.objectArrayValue) {
                if (objectTypedValueWrapper.annotationInfo != null) {
                    objectTypedValueWrapper.annotationInfo.convertWrapperArraysToPrimitiveArrays();
                }
            }
            if (this.objectArrayValue.getClass().getComponentType().isArray()) {
                return;
            }
            final String s3;
            final String s2 = s3 = (String)this.getArrayValueClassOrName(classInfo, s, (boolean)(0 != 0));
            switch (s3) {
                case "java.lang.String": {
                    this.stringArrayValue = new String[this.objectArrayValue.length];
                    for (int j = 0; j < this.objectArrayValue.length; ++j) {
                        this.stringArrayValue[j] = this.objectArrayValue[j].stringValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "int": {
                    this.intArrayValue = new int[this.objectArrayValue.length];
                    for (int k = 0; k < this.objectArrayValue.length; ++k) {
                        if (this.objectArrayValue[k] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.intArrayValue[k] = this.objectArrayValue[k].integerValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "long": {
                    this.longArrayValue = new long[this.objectArrayValue.length];
                    for (int l = 0; l < this.objectArrayValue.length; ++l) {
                        if (this.objectArrayValue[l] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.longArrayValue[l] = this.objectArrayValue[l].longValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "short": {
                    this.shortArrayValue = new short[this.objectArrayValue.length];
                    for (int n2 = 0; n2 < this.objectArrayValue.length; ++n2) {
                        if (this.objectArrayValue[n2] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.shortArrayValue[n2] = this.objectArrayValue[n2].shortValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "char": {
                    this.charArrayValue = new char[this.objectArrayValue.length];
                    for (int n3 = 0; n3 < this.objectArrayValue.length; ++n3) {
                        if (this.objectArrayValue[n3] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.charArrayValue[n3] = this.objectArrayValue[n3].characterValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "float": {
                    this.floatArrayValue = new float[this.objectArrayValue.length];
                    for (int n4 = 0; n4 < this.objectArrayValue.length; ++n4) {
                        if (this.objectArrayValue[n4] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.floatArrayValue[n4] = this.objectArrayValue[n4].floatValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "double": {
                    this.doubleArrayValue = new double[this.objectArrayValue.length];
                    for (int n5 = 0; n5 < this.objectArrayValue.length; ++n5) {
                        if (this.objectArrayValue[n5] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.doubleArrayValue[n5] = this.objectArrayValue[n5].doubleValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "boolean": {
                    this.booleanArrayValue = new boolean[this.objectArrayValue.length];
                    for (int n6 = 0; n6 < this.objectArrayValue.length; ++n6) {
                        if (this.objectArrayValue[n6] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.booleanArrayValue[n6] = this.objectArrayValue[n6].booleanValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
                case "byte": {
                    this.byteArrayValue = new byte[this.objectArrayValue.length];
                    for (int n7 = 0; n7 < this.objectArrayValue.length; ++n7) {
                        if (this.objectArrayValue[n7] == null) {
                            throw new IllegalArgumentException("Illegal null value for array of element type " + s2 + " in parameter " + s + " of annotation class " + ((classInfo == null) ? "<class outside accept>" : classInfo.getName()));
                        }
                        this.byteArrayValue[n7] = this.objectArrayValue[n7].byteValue;
                    }
                    this.objectArrayValue = null;
                    break;
                }
            }
        }
    }
    
    public ObjectTypedValueWrapper(final Object o) {
        if (o != null) {
            final Class<?> class1 = o.getClass();
            if (class1.isArray()) {
                if (class1 == String[].class) {
                    this.stringArrayValue = (String[])o;
                }
                else if (class1 == int[].class) {
                    this.intArrayValue = (int[])o;
                }
                else if (class1 == long[].class) {
                    this.longArrayValue = (long[])o;
                }
                else if (class1 == short[].class) {
                    this.shortArrayValue = (short[])o;
                }
                else if (class1 == boolean[].class) {
                    this.booleanArrayValue = (boolean[])o;
                }
                else if (class1 == char[].class) {
                    this.charArrayValue = (char[])o;
                }
                else if (class1 == float[].class) {
                    this.floatArrayValue = (float[])o;
                }
                else if (class1 == double[].class) {
                    this.doubleArrayValue = (double[])o;
                }
                else if (class1 == byte[].class) {
                    this.byteArrayValue = (byte[])o;
                }
                else {
                    final int length = Array.getLength(o);
                    this.objectArrayValue = new ObjectTypedValueWrapper[length];
                    for (int i = 0; i < length; ++i) {
                        this.objectArrayValue[i] = new ObjectTypedValueWrapper(Array.get(o, i));
                    }
                }
            }
            else if (o instanceof AnnotationEnumValue) {
                this.annotationEnumValue = (AnnotationEnumValue)o;
            }
            else if (o instanceof AnnotationClassRef) {
                this.annotationClassRef = (AnnotationClassRef)o;
            }
            else if (o instanceof AnnotationInfo) {
                this.annotationInfo = (AnnotationInfo)o;
            }
            else if (o instanceof String) {
                this.stringValue = (String)o;
            }
            else if (o instanceof Integer) {
                this.integerValue = (Integer)o;
            }
            else if (o instanceof Long) {
                this.longValue = (Long)o;
            }
            else if (o instanceof Short) {
                this.shortValue = (Short)o;
            }
            else if (o instanceof Boolean) {
                this.booleanValue = (Boolean)o;
            }
            else if (o instanceof Character) {
                this.characterValue = (Character)o;
            }
            else if (o instanceof Float) {
                this.floatValue = (Float)o;
            }
            else if (o instanceof Double) {
                this.doubleValue = (Double)o;
            }
            else {
                if (!(o instanceof Byte)) {
                    throw new IllegalArgumentException("Unsupported annotation parameter value type: " + class1.getName());
                }
                this.byteValue = (Byte)o;
            }
        }
    }
    
    public Object get() {
        return this.instantiateOrGet(null, null);
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        if (this.annotationEnumValue != null) {
            this.annotationEnumValue.findReferencedClassInfo(map, set);
        }
        else if (this.annotationClassRef != null) {
            final ClassInfo classInfo = this.annotationClassRef.getClassInfo();
            if (classInfo != null) {
                set.add(classInfo);
            }
        }
        else if (this.annotationInfo != null) {
            this.annotationInfo.findReferencedClassInfo(map, set);
        }
        else if (this.objectArrayValue != null) {
            final ObjectTypedValueWrapper[] objectArrayValue = this.objectArrayValue;
            for (int length = objectArrayValue.length, i = 0; i < length; ++i) {
                objectArrayValue[i].findReferencedClassInfo(map, set);
            }
        }
    }
    
    @Override
    protected String getClassName() {
        throw new IllegalArgumentException("getClassName() cannot be called here");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ObjectTypedValueWrapper)) {
            return false;
        }
        final ObjectTypedValueWrapper objectTypedValueWrapper = (ObjectTypedValueWrapper)o;
        return Objects.equals(this.annotationEnumValue, objectTypedValueWrapper.annotationEnumValue) && Objects.equals(this.annotationClassRef, objectTypedValueWrapper.annotationClassRef) && Objects.equals(this.annotationInfo, objectTypedValueWrapper.annotationInfo) && Objects.equals(this.stringValue, objectTypedValueWrapper.stringValue) && Objects.equals(this.integerValue, objectTypedValueWrapper.integerValue) && Objects.equals(this.longValue, objectTypedValueWrapper.longValue) && Objects.equals(this.shortValue, objectTypedValueWrapper.shortValue) && Objects.equals(this.booleanValue, objectTypedValueWrapper.booleanValue) && Objects.equals(this.characterValue, objectTypedValueWrapper.characterValue) && Objects.equals(this.floatValue, objectTypedValueWrapper.floatValue) && Objects.equals(this.doubleValue, objectTypedValueWrapper.doubleValue) && Objects.equals(this.byteValue, objectTypedValueWrapper.byteValue) && Arrays.equals(this.stringArrayValue, objectTypedValueWrapper.stringArrayValue) && Arrays.equals(this.intArrayValue, objectTypedValueWrapper.intArrayValue) && Arrays.equals(this.longArrayValue, objectTypedValueWrapper.longArrayValue) && Arrays.equals(this.shortArrayValue, objectTypedValueWrapper.shortArrayValue) && Arrays.equals(this.floatArrayValue, objectTypedValueWrapper.floatArrayValue) && Arrays.equals(this.byteArrayValue, objectTypedValueWrapper.byteArrayValue) && Arrays.deepEquals(this.objectArrayValue, objectTypedValueWrapper.objectArrayValue);
    }
    
    Object instantiateOrGet(final ClassInfo classInfo, final String s) {
        final boolean b = classInfo != null;
        if (this.annotationEnumValue != null) {
            return b ? this.annotationEnumValue.loadClassAndReturnEnumValue() : this.annotationEnumValue;
        }
        if (this.annotationClassRef != null) {
            return b ? this.annotationClassRef.loadClass() : this.annotationClassRef;
        }
        if (this.annotationInfo != null) {
            return b ? this.annotationInfo.loadClassAndInstantiate() : this.annotationInfo;
        }
        if (this.stringValue != null) {
            return this.stringValue;
        }
        if (this.integerValue != null) {
            return this.integerValue;
        }
        if (this.longValue != null) {
            return this.longValue;
        }
        if (this.shortValue != null) {
            return this.shortValue;
        }
        if (this.booleanValue != null) {
            return this.booleanValue;
        }
        if (this.characterValue != null) {
            return this.characterValue;
        }
        if (this.floatValue != null) {
            return this.floatValue;
        }
        if (this.doubleValue != null) {
            return this.doubleValue;
        }
        if (this.byteValue != null) {
            return this.byteValue;
        }
        if (this.stringArrayValue != null) {
            return this.stringArrayValue;
        }
        if (this.intArrayValue != null) {
            return this.intArrayValue;
        }
        if (this.longArrayValue != null) {
            return this.longArrayValue;
        }
        if (this.shortArrayValue != null) {
            return this.shortArrayValue;
        }
        if (this.booleanArrayValue != null) {
            return this.booleanArrayValue;
        }
        if (this.charArrayValue != null) {
            return this.charArrayValue;
        }
        if (this.floatArrayValue != null) {
            return this.floatArrayValue;
        }
        if (this.doubleArrayValue != null) {
            return this.doubleArrayValue;
        }
        if (this.byteArrayValue != null) {
            return this.byteArrayValue;
        }
        if (this.objectArrayValue != null) {
            final Class componentType = b ? ((Class)this.getArrayValueClassOrName(classInfo, s, true)) : null;
            final Object o = (componentType == null) ? new Object[this.objectArrayValue.length] : Array.newInstance(componentType, this.objectArrayValue.length);
            for (int i = 0; i < this.objectArrayValue.length; ++i) {
                if (this.objectArrayValue[i] != null) {
                    Array.set(o, i, this.objectArrayValue[i].instantiateOrGet(classInfo, s));
                }
            }
            return o;
        }
        return null;
    }
    
    protected ClassInfo getClassInfo() {
        throw new IllegalArgumentException("getClassInfo() cannot be called here");
    }
    
    private Object getArrayValueClassOrName(final ClassInfo classInfo, final String s, final boolean b) {
        final MethodInfoList list = (classInfo == null || classInfo.methodInfo == null) ? null : classInfo.methodInfo.get(s);
        if (classInfo != null && list != null && !list.isEmpty()) {
            if (list.size() > 1) {
                throw new IllegalArgumentException("Duplicated annotation parameter method " + s + "() in annotation class " + classInfo.getName());
            }
            final TypeSignature resultType = list.get(0).getTypeSignatureOrTypeDescriptor().getResultType();
            if (!(resultType instanceof ArrayTypeSignature)) {
                throw new IllegalArgumentException("Annotation parameter " + s + " in annotation class " + classInfo.getName() + " holds an array, but does not have an array type signature");
            }
            final ArrayTypeSignature arrayTypeSignature = (ArrayTypeSignature)resultType;
            if (arrayTypeSignature.getNumDimensions() != 1) {
                throw new IllegalArgumentException("Annotations only support 1-dimensional arrays");
            }
            final TypeSignature elementTypeSignature = arrayTypeSignature.getElementTypeSignature();
            if (elementTypeSignature instanceof ClassRefTypeSignature) {
                final ClassRefTypeSignature classRefTypeSignature = (ClassRefTypeSignature)elementTypeSignature;
                return b ? classRefTypeSignature.loadClass() : classRefTypeSignature.getFullyQualifiedClassName();
            }
            if (elementTypeSignature instanceof BaseTypeSignature) {
                final BaseTypeSignature baseTypeSignature = (BaseTypeSignature)elementTypeSignature;
                return b ? baseTypeSignature.getType() : baseTypeSignature.getTypeStr();
            }
        }
        else {
            for (final ObjectTypedValueWrapper objectTypedValueWrapper : this.objectArrayValue) {
                if (objectTypedValueWrapper != null) {
                    return (objectTypedValueWrapper.integerValue != null) ? (b ? Integer.class : "int") : ((objectTypedValueWrapper.longValue != null) ? (b ? Long.class : "long") : ((objectTypedValueWrapper.shortValue != null) ? (b ? Short.class : "short") : ((objectTypedValueWrapper.characterValue != null) ? (b ? Character.class : "char") : ((objectTypedValueWrapper.byteValue != null) ? (b ? Byte.class : "byte") : ((objectTypedValueWrapper.booleanValue != null) ? (b ? Boolean.class : "boolean") : ((objectTypedValueWrapper.doubleValue != null) ? (b ? Double.class : "double") : ((objectTypedValueWrapper.floatValue != null) ? (b ? Float.class : "float") : (b ? objectTypedValueWrapper.getClass() : objectTypedValueWrapper.getClass().getName()))))))));
                }
            }
        }
        return b ? Object.class : "java.lang.Object";
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.annotationEnumValue != null) {
            this.annotationEnumValue.setScanResult(scanResult);
        }
        else if (this.annotationClassRef != null) {
            this.annotationClassRef.setScanResult(scanResult);
        }
        else if (this.annotationInfo != null) {
            this.annotationInfo.setScanResult(scanResult);
        }
        else if (this.objectArrayValue != null) {
            for (final ObjectTypedValueWrapper objectTypedValueWrapper : this.objectArrayValue) {
                if (objectTypedValueWrapper != null) {
                    objectTypedValueWrapper.setScanResult(scanResult);
                }
            }
        }
    }
    
    public ObjectTypedValueWrapper() {
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.annotationEnumValue, this.annotationClassRef, this.annotationInfo, this.stringValue, this.integerValue, this.longValue, this.shortValue, this.booleanValue, this.characterValue, this.floatValue, this.doubleValue, this.byteValue, Arrays.hashCode(this.stringArrayValue), Arrays.hashCode(this.intArrayValue), Arrays.hashCode(this.longArrayValue), Arrays.hashCode(this.shortArrayValue), Arrays.hashCode(this.booleanArrayValue), Arrays.hashCode(this.charArrayValue), Arrays.hashCode(this.floatArrayValue), Arrays.hashCode(this.doubleArrayValue), Arrays.hashCode(this.byteArrayValue), Arrays.hashCode(this.objectArrayValue));
    }
}
