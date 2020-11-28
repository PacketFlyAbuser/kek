// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Collection;

public class AnnotationInfoList extends MappableInfoList<AnnotationInfo>
{
    private /* synthetic */ AnnotationInfoList directlyRelatedAnnotations;
    static final /* synthetic */ AnnotationInfoList EMPTY_LIST;
    
    AnnotationInfoList(final AnnotationInfoList list, final AnnotationInfoList directlyRelatedAnnotations) {
        super(list);
        this.directlyRelatedAnnotations = directlyRelatedAnnotations;
    }
    
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassInfo(map, set);
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ ((this.directlyRelatedAnnotations == null) ? 0 : this.directlyRelatedAnnotations.hashCode());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnotationInfoList)) {
            return false;
        }
        final AnnotationInfoList list = (AnnotationInfoList)o;
        if (this.directlyRelatedAnnotations == null != (list.directlyRelatedAnnotations == null)) {
            return false;
        }
        if (this.directlyRelatedAnnotations == null) {
            return super.equals(list);
        }
        return super.equals(list) && this.directlyRelatedAnnotations.equals(list.directlyRelatedAnnotations);
    }
    
    public AnnotationInfoList(final AnnotationInfoList list) {
        this(list, list);
    }
    
    private static void findMetaAnnotations(final AnnotationInfo annotationInfo, final AnnotationInfoList list, final Set<ClassInfo> set) {
        final ClassInfo classInfo = annotationInfo.getClassInfo();
        if (classInfo != null && classInfo.annotationInfo != null && set.add(classInfo)) {
            for (final AnnotationInfo annotationInfo2 : classInfo.annotationInfo) {
                if (!annotationInfo2.getClassInfo().getName().startsWith("java.lang.annotation.")) {
                    list.add(annotationInfo2);
                    findMetaAnnotations(annotationInfo2, list, set);
                }
            }
        }
    }
    
    static {
        (EMPTY_LIST = new AnnotationInfoList()).makeUnmodifiable();
    }
    
    void handleRepeatableAnnotations(final Set<String> set, final ClassInfo classInfo, final ClassInfo.RelType relType, final ClassInfo.RelType relType2, final ClassInfo.RelType relType3) {
        List<AnnotationInfo> list = null;
        for (int i = this.size() - 1; i >= 0; --i) {
            final AnnotationInfo annotationInfo = this.get(i);
            if (set.contains(annotationInfo.getName())) {
                if (list == null) {
                    list = new ArrayList<AnnotationInfo>();
                }
                list.add(annotationInfo);
                this.remove(i);
            }
        }
        if (list != null) {
            final Iterator<AnnotationInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                final AnnotationParameterValueList parameterValues = iterator.next().getParameterValues();
                if (!parameterValues.isEmpty()) {
                    final AnnotationParameterValue annotationParameterValue = parameterValues.get("value");
                    if (annotationParameterValue == null) {
                        continue;
                    }
                    final Object value = annotationParameterValue.getValue();
                    if (!(value instanceof Object[])) {
                        continue;
                    }
                    for (final Object o : (Object[])value) {
                        if (o instanceof AnnotationInfo) {
                            final AnnotationInfo annotationInfo2 = (AnnotationInfo)o;
                            this.add(annotationInfo2);
                            if (relType != null && (relType2 != null || relType3 != null)) {
                                final ClassInfo classInfo2 = annotationInfo2.getClassInfo();
                                if (classInfo2 != null) {
                                    classInfo.addRelatedClass(relType, classInfo2);
                                    if (relType2 != null) {
                                        classInfo2.addRelatedClass(relType2, classInfo);
                                    }
                                    if (relType3 != null) {
                                        classInfo2.addRelatedClass(relType3, classInfo);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public AnnotationInfoList() {
    }
    
    public AnnotationInfoList filter(final AnnotationInfoFilter annotationInfoFilter) {
        final AnnotationInfoList list = new AnnotationInfoList();
        for (final AnnotationInfo annotationInfo : this) {
            if (annotationInfoFilter.accept(annotationInfo)) {
                list.add(annotationInfo);
            }
        }
        return list;
    }
    
    public AnnotationInfoList directOnly() {
        return (this.directlyRelatedAnnotations == null) ? this : new AnnotationInfoList(this.directlyRelatedAnnotations, null);
    }
    
    public AnnotationInfoList(final int n) {
        super(n);
    }
    
    static AnnotationInfoList getIndirectAnnotations(final AnnotationInfoList list, final ClassInfo classInfo) {
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>();
        final HashSet<ClassInfo> set2 = new HashSet<ClassInfo>();
        final AnnotationInfoList list2 = new AnnotationInfoList((list == null) ? 2 : list.size());
        if (list != null) {
            for (final AnnotationInfo annotationInfo : list) {
                set.add(annotationInfo.getClassInfo());
                list2.add(annotationInfo);
                findMetaAnnotations(annotationInfo, list2, set2);
            }
        }
        if (classInfo != null) {
            for (final ClassInfo classInfo2 : classInfo.getSuperclasses()) {
                if (classInfo2.annotationInfo != null) {
                    for (final AnnotationInfo annotationInfo2 : classInfo2.annotationInfo) {
                        if (annotationInfo2.isInherited() && set.add(annotationInfo2.getClassInfo())) {
                            list2.add(annotationInfo2);
                            final AnnotationInfoList list3 = new AnnotationInfoList(2);
                            findMetaAnnotations(annotationInfo2, list3, set2);
                            for (final AnnotationInfo annotationInfo3 : list3) {
                                if (annotationInfo3.isInherited()) {
                                    list2.add(annotationInfo3);
                                }
                            }
                        }
                    }
                }
            }
        }
        final AnnotationInfoList list4 = (list == null) ? AnnotationInfoList.EMPTY_LIST : new AnnotationInfoList(list);
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list4);
        final AnnotationInfoList list5 = new AnnotationInfoList(list2, list4);
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list5);
        return list5;
    }
    
    public static AnnotationInfoList emptyList() {
        return AnnotationInfoList.EMPTY_LIST;
    }
    
    public AnnotationInfoList getRepeatable(final String s) {
        boolean b = false;
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(s)) {
                b = true;
                break;
            }
        }
        if (!b) {
            return AnnotationInfoList.EMPTY_LIST;
        }
        final AnnotationInfoList list = new AnnotationInfoList(this.size());
        for (final AnnotationInfo annotationInfo : this) {
            if (annotationInfo.getName().equals(s)) {
                list.add(annotationInfo);
            }
        }
        return list;
    }
    
    @FunctionalInterface
    public interface AnnotationInfoFilter
    {
        boolean accept(final AnnotationInfo p0);
    }
}
