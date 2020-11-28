// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.VariableElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.AnnotationValue;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;

public final class AnnotationHandle
{
    private final /* synthetic */ AnnotationMirror annotation;
    
    public static AnnotationHandle of(final AnnotationMirror annotationMirror) {
        return new AnnotationHandle(annotationMirror);
    }
    
    public <T> List<T> getList(final String s) {
        return unwrapAnnotationValueList((List<AnnotationValue>)this.getValue(s, Collections.emptyList()));
    }
    
    public AnnotationMirror asMirror() {
        return this.annotation;
    }
    
    protected AnnotationValue getAnnotationValue(final String s) {
        for (final ExecutableElement executableElement : this.annotation.getElementValues().keySet()) {
            if (executableElement.getSimpleName().contentEquals(s)) {
                return (AnnotationValue)this.annotation.getElementValues().get(executableElement);
            }
        }
        return null;
    }
    
    public boolean getBoolean(final String s, final boolean b) {
        return this.getValue(s, b);
    }
    
    public <T> T getValue(final String s) {
        return this.getValue(s, (T)null);
    }
    
    @Override
    public String toString() {
        if (this.annotation == null) {
            return "@{UnknownAnnotation}";
        }
        return "@" + (Object)this.annotation.getAnnotationType().asElement().getSimpleName();
    }
    
    protected static <T> List<T> unwrapAnnotationValueList(final List<AnnotationValue> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        final ArrayList<T> list2 = new ArrayList<T>(list.size());
        final Iterator<AnnotationValue> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add((T)iterator.next().getValue());
        }
        return list2;
    }
    
    public List<AnnotationHandle> getAnnotationList(final String s) {
        final AnnotationMirror value = this.getValue(s, (AnnotationMirror)null);
        if (value == null) {
            return Collections.emptyList();
        }
        if (value instanceof AnnotationMirror) {
            return (List<AnnotationHandle>)ImmutableList.of((Object)of(value));
        }
        final List<AnnotationValue> list = (List<AnnotationValue>)value;
        final ArrayList list2 = new ArrayList<AnnotationHandle>(list.size());
        final Iterator<AnnotationValue> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add(new AnnotationHandle((AnnotationMirror)iterator.next().getValue()));
        }
        return Collections.unmodifiableList((List<? extends AnnotationHandle>)list2);
    }
    
    public <T> T getValue() {
        return this.getValue("value", (T)null);
    }
    
    protected static AnnotationMirror getAnnotation(final Element element, final Class<? extends Annotation> clazz) {
        if (element == null) {
            return null;
        }
        final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        if (annotationMirrors == null) {
            return null;
        }
        for (final AnnotationMirror annotationMirror : annotationMirrors) {
            final Element element2 = annotationMirror.getAnnotationType().asElement();
            if (!(element2 instanceof TypeElement)) {
                continue;
            }
            if (((TypeElement)element2).getQualifiedName().contentEquals(clazz.getName())) {
                return annotationMirror;
            }
        }
        return null;
    }
    
    private AnnotationHandle(final AnnotationMirror annotation) {
        this.annotation = annotation;
    }
    
    public static AnnotationHandle of(final Element element, final Class<? extends Annotation> clazz) {
        return new AnnotationHandle(getAnnotation(element, clazz));
    }
    
    public <T> List<T> getList() {
        return this.getList("value");
    }
    
    static {
        MISSING = new AnnotationHandle(null);
    }
    
    public boolean exists() {
        return this.annotation != null;
    }
    
    public <T> T getValue(final String s, final T t) {
        if (this.annotation == null) {
            return t;
        }
        final AnnotationValue annotationValue = this.getAnnotationValue(s);
        if (!(t instanceof Enum) || annotationValue == null) {
            return (T)((annotationValue != null) ? annotationValue.getValue() : t);
        }
        final VariableElement variableElement = (VariableElement)annotationValue.getValue();
        if (variableElement == null) {
            return t;
        }
        return Enum.valueOf(t.getClass(), variableElement.getSimpleName().toString());
    }
    
    public AnnotationHandle getAnnotation(final String s) {
        final AnnotationValue value = this.getValue(s);
        if (value instanceof AnnotationMirror) {
            return of((AnnotationMirror)value);
        }
        if (value instanceof AnnotationValue) {
            final Object value2 = value.getValue();
            if (value2 instanceof AnnotationMirror) {
                return of((AnnotationMirror)value2);
            }
        }
        return null;
    }
}
