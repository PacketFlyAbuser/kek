// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Collection;

public class AnnotationParameterValueList extends MappableInfoList<AnnotationParameterValue>
{
    static final /* synthetic */ AnnotationParameterValueList EMPTY_LIST;
    
    static {
        (EMPTY_LIST = new AnnotationParameterValueList()).makeUnmodifiable();
    }
    
    public AnnotationParameterValueList() {
    }
    
    public AnnotationParameterValueList(final int n) {
        super(n);
    }
    
    public Object getValue(final String s) {
        final AnnotationParameterValue annotationParameterValue = this.get(s);
        return (annotationParameterValue == null) ? null : annotationParameterValue.getValue();
    }
    
    public AnnotationParameterValueList(final Collection<AnnotationParameterValue> collection) {
        super(collection);
    }
    
    void convertWrapperArraysToPrimitiveArrays(final ClassInfo classInfo) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().convertWrapperArraysToPrimitiveArrays(classInfo);
        }
    }
    
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassInfo(map, set);
        }
    }
    
    public static AnnotationParameterValueList emptyList() {
        return AnnotationParameterValueList.EMPTY_LIST;
    }
}
