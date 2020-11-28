// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

public class AnnotationInfo extends ScanResultObject implements Comparable<AnnotationInfo>, HasName
{
    private /* synthetic */ String name;
    private transient /* synthetic */ boolean annotationParamValuesHasBeenConvertedToPrimitive;
    private transient /* synthetic */ AnnotationParameterValueList annotationParamValuesWithDefaults;
    private /* synthetic */ AnnotationParameterValueList annotationParamValues;
    
    @Override
    public int compareTo(final AnnotationInfo annotationInfo) {
        final int compareTo = this.getName().compareTo(annotationInfo.getName());
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.annotationParamValues == null && annotationInfo.annotationParamValues == null) {
            return 0;
        }
        if (this.annotationParamValues == null) {
            return -1;
        }
        if (annotationInfo.annotationParamValues == null) {
            return 1;
        }
        for (int i = 0; i < Math.max(this.annotationParamValues.size(), annotationInfo.annotationParamValues.size()); ++i) {
            if (i >= this.annotationParamValues.size()) {
                return -1;
            }
            if (i >= annotationInfo.annotationParamValues.size()) {
                return 1;
            }
            final int compareTo2 = this.annotationParamValues.get(i).compareTo(annotationInfo.annotationParamValues.get(i));
            if (compareTo2 != 0) {
                return compareTo2;
            }
        }
        return 0;
    }
    
    public AnnotationParameterValueList getParameterValues() {
        if (this.annotationParamValuesWithDefaults == null) {
            final ClassInfo classInfo = this.getClassInfo();
            if (classInfo == null) {
                return (this.annotationParamValues == null) ? AnnotationParameterValueList.EMPTY_LIST : this.annotationParamValues;
            }
            if (this.annotationParamValues != null && !this.annotationParamValuesHasBeenConvertedToPrimitive) {
                this.annotationParamValues.convertWrapperArraysToPrimitiveArrays(classInfo);
                this.annotationParamValuesHasBeenConvertedToPrimitive = true;
            }
            if (classInfo.annotationDefaultParamValues != null && !classInfo.annotationDefaultParamValuesHasBeenConvertedToPrimitive) {
                classInfo.annotationDefaultParamValues.convertWrapperArraysToPrimitiveArrays(classInfo);
                classInfo.annotationDefaultParamValuesHasBeenConvertedToPrimitive = true;
            }
            final AnnotationParameterValueList annotationDefaultParamValues = classInfo.annotationDefaultParamValues;
            if (annotationDefaultParamValues == null && this.annotationParamValues == null) {
                return AnnotationParameterValueList.EMPTY_LIST;
            }
            if (annotationDefaultParamValues == null) {
                return this.annotationParamValues;
            }
            if (this.annotationParamValues == null) {
                return annotationDefaultParamValues;
            }
            final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            for (final AnnotationParameterValue annotationParameterValue : annotationDefaultParamValues) {
                hashMap.put(annotationParameterValue.getName(), annotationParameterValue.getValue());
            }
            for (final AnnotationParameterValue annotationParameterValue2 : this.annotationParamValues) {
                hashMap.put(annotationParameterValue2.getName(), annotationParameterValue2.getValue());
            }
            if (classInfo.methodInfo == null) {
                throw new IllegalArgumentException("Could not find methods for annotation " + classInfo.getName());
            }
            this.annotationParamValuesWithDefaults = new AnnotationParameterValueList();
            final Iterator iterator3 = classInfo.methodInfo.iterator();
            while (iterator3.hasNext()) {
                final String name;
                final String s = name = iterator3.next().getName();
                switch (name) {
                    case "<init>":
                    case "<clinit>":
                    case "hashCode":
                    case "equals":
                    case "toString":
                    case "annotationType": {
                        continue;
                    }
                    default: {
                        final Object value = hashMap.get(s);
                        if (value != null) {
                            this.annotationParamValuesWithDefaults.add(new AnnotationParameterValue(s, value));
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        return this.annotationParamValuesWithDefaults;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    protected String getClassName() {
        return this.name;
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        super.findReferencedClassInfo(map, set);
        if (this.annotationParamValues != null) {
            final Iterator iterator = this.annotationParamValues.iterator();
            while (iterator.hasNext()) {
                iterator.next().findReferencedClassInfo(map, set);
            }
        }
    }
    
    public AnnotationParameterValueList getDefaultParameterValues() {
        return this.getClassInfo().getAnnotationDefaultParameterValues();
    }
    
    void toString(final StringBuilder sb) {
        sb.append('@').append(this.getName());
        final AnnotationParameterValueList parameterValues = this.getParameterValues();
        if (!parameterValues.isEmpty()) {
            sb.append('(');
            for (int i = 0; i < parameterValues.size(); ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                final AnnotationParameterValue annotationParameterValue = parameterValues.get(i);
                if (parameterValues.size() > 1 || !"value".equals(annotationParameterValue.getName())) {
                    annotationParameterValue.toString(sb);
                }
                else {
                    annotationParameterValue.toStringParamValueOnly(sb);
                }
            }
            sb.append(')');
        }
    }
    
    public Annotation loadClassAndInstantiate() {
        final Class<Annotation> loadClass = this.getClassInfo().loadClass(Annotation.class);
        return (Annotation)Proxy.newProxyInstance(loadClass.getClassLoader(), new Class[] { loadClass }, new AnnotationInvocationHandler(loadClass, this));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.toString(sb);
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int hashCode = this.getName().hashCode();
        if (this.annotationParamValues != null) {
            for (final AnnotationParameterValue annotationParameterValue : this.annotationParamValues) {
                hashCode = hashCode * 7 + annotationParameterValue.getName().hashCode() * 3 + annotationParameterValue.getValue().hashCode();
            }
        }
        return hashCode;
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.annotationParamValues != null) {
            final Iterator iterator = this.annotationParamValues.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AnnotationInfo && this.compareTo((AnnotationInfo)o) == 0);
    }
    
    void convertWrapperArraysToPrimitiveArrays() {
        if (this.annotationParamValues != null) {
            this.annotationParamValues.convertWrapperArraysToPrimitiveArrays(this.getClassInfo());
        }
    }
    
    AnnotationInfo() {
    }
    
    public ClassInfo getClassInfo() {
        return super.getClassInfo();
    }
    
    AnnotationInfo(final String name, final AnnotationParameterValueList annotationParamValues) {
        this.name = name;
        this.annotationParamValues = annotationParamValues;
    }
    
    public boolean isInherited() {
        return this.getClassInfo().isInherited;
    }
    
    private static class AnnotationInvocationHandler implements InvocationHandler
    {
        private final /* synthetic */ Class<? extends Annotation> annotationClass;
        private final /* synthetic */ Map<String, Object> annotationParameterValuesInstantiated;
        private final /* synthetic */ AnnotationInfo annotationInfo;
        
        AnnotationInvocationHandler(final Class<? extends Annotation> annotationClass) {
            this.annotationParameterValuesInstantiated = new HashMap<String, Object>();
            this.annotationClass = annotationClass;
            for (final AnnotationParameterValue annotationParameterValue : AnnotationInfo.this.getParameterValues()) {
                final Object instantiate = annotationParameterValue.instantiate(AnnotationInfo.this.getClassInfo());
                if (instantiate == null) {
                    throw new IllegalArgumentException("Got null value for annotation parameter " + annotationParameterValue.getName() + " of annotation " + AnnotationInfo.this.getName());
                }
                this.annotationParameterValuesInstantiated.put(annotationParameterValue.getName(), instantiate);
            }
        }
        
        @Override
        public Object invoke(final Object o, final Method method, final Object[] array) {
            final String name = method.getName();
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (((array == null) ? 0 : array.length) != parameterTypes.length) {
                throw new IllegalArgumentException("Wrong number of arguments for " + this.annotationClass.getName() + "." + name + ": got " + ((array == null) ? 0 : array.length) + ", expected " + parameterTypes.length);
            }
            if (array != null && parameterTypes.length == 1) {
                if (!"equals".equals(name) || parameterTypes[0] != Object.class) {
                    throw new IllegalArgumentException();
                }
                if (this == array[0]) {
                    return true;
                }
                if (!this.annotationClass.isInstance(array[0])) {
                    return false;
                }
                for (final Map.Entry<String, Object> entry : this.annotationParameterValuesInstantiated.entrySet()) {
                    final String s = entry.getKey();
                    final Object value = entry.getValue();
                    final Object invokeMethod = ReflectionUtils.invokeMethod(array[0], s, false);
                    if (value == null != (invokeMethod == null)) {
                        return false;
                    }
                    if (value == null && invokeMethod == null) {
                        return true;
                    }
                    if (value == null || !value.equals(invokeMethod)) {
                        return false;
                    }
                }
                return true;
            }
            else {
                if (parameterTypes.length != 0) {
                    throw new IllegalArgumentException();
                }
                final String s2 = name;
                switch (s2) {
                    case "toString": {
                        return this.annotationInfo.toString();
                    }
                    case "hashCode": {
                        int i = 0;
                        for (final Map.Entry<String, Object> entry2 : this.annotationParameterValuesInstantiated.entrySet()) {
                            final String s3 = entry2.getKey();
                            final Object[] value2 = entry2.getValue();
                            int n2;
                            if (value2 == null) {
                                n2 = 0;
                            }
                            else {
                                final Class<? extends byte[]> class1 = ((byte[])value2).getClass();
                                if (!class1.isArray()) {
                                    n2 = value2.hashCode();
                                }
                                else if (class1 == byte[].class) {
                                    n2 = Arrays.hashCode((byte[])value2);
                                }
                                else if (class1 == char[].class) {
                                    n2 = Arrays.hashCode((char[])value2);
                                }
                                else if (class1 == double[].class) {
                                    n2 = Arrays.hashCode((double[])value2);
                                }
                                else if (class1 == float[].class) {
                                    n2 = Arrays.hashCode((float[])value2);
                                }
                                else if (class1 == int[].class) {
                                    n2 = Arrays.hashCode((int[])value2);
                                }
                                else if (class1 == long[].class) {
                                    n2 = Arrays.hashCode((long[])value2);
                                }
                                else if (class1 == short[].class) {
                                    n2 = Arrays.hashCode((short[])value2);
                                }
                                else if (class1 == boolean[].class) {
                                    n2 = Arrays.hashCode((boolean[])value2);
                                }
                                else {
                                    n2 = Arrays.hashCode(value2);
                                }
                            }
                            i += (127 * s3.hashCode() ^ n2);
                        }
                        return i;
                    }
                    case "annotationType": {
                        return this.annotationClass;
                    }
                    default: {
                        final Object value3 = this.annotationParameterValuesInstantiated.get(name);
                        if (value3 == null) {
                            throw new IncompleteAnnotationException(this.annotationClass, name);
                        }
                        final Class<? extends String[]> class2 = ((String[])value3).getClass();
                        if (!class2.isArray()) {
                            return value3;
                        }
                        if (class2 == String[].class) {
                            return ((String[])value3).clone();
                        }
                        if (class2 == byte[].class) {
                            return ((byte[])value3).clone();
                        }
                        if (class2 == char[].class) {
                            return ((char[])value3).clone();
                        }
                        if (class2 == double[].class) {
                            return ((double[])value3).clone();
                        }
                        if (class2 == float[].class) {
                            return ((float[])value3).clone();
                        }
                        if (class2 == int[].class) {
                            return ((int[])value3).clone();
                        }
                        if (class2 == long[].class) {
                            return ((long[])value3).clone();
                        }
                        if (class2 == short[].class) {
                            return ((short[])value3).clone();
                        }
                        if (class2 == boolean[].class) {
                            return ((boolean[])value3).clone();
                        }
                        return ((String[])value3).clone();
                    }
                }
            }
        }
    }
}
