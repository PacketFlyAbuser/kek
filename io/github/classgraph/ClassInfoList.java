// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayDeque;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassInfoList extends MappableInfoList<ClassInfo>
{
    static final /* synthetic */ ClassInfoList EMPTY_LIST;
    private final /* synthetic */ boolean sortByName;
    private final transient /* synthetic */ Set<ClassInfo> directlyRelatedClasses;
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ ((this.directlyRelatedClasses == null) ? 0 : this.directlyRelatedClasses.hashCode());
    }
    
    public ClassInfoList getAssignableTo(final ClassInfo classInfo) {
        if (classInfo == null) {
            throw new IllegalArgumentException("assignableToClass parameter cannot be null");
        }
        final HashSet<Object> set = new HashSet<Object>();
        if (classInfo.isStandardClass()) {
            set.addAll(classInfo.getSubclasses());
        }
        else if (classInfo.isInterfaceOrAnnotation()) {
            set.addAll(classInfo.getClassesImplementing());
        }
        set.add(classInfo);
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return set.contains(classInfo);
            }
        });
    }
    
    public ClassInfoList exclude(final ClassInfoList list) {
        final LinkedHashSet<ClassInfo> set = new LinkedHashSet<ClassInfo>(this);
        final LinkedHashSet<ClassInfo> set2 = new LinkedHashSet<ClassInfo>(this.directlyRelatedClasses);
        set.removeAll(list);
        set2.removeAll(list.directlyRelatedClasses);
        return new ClassInfoList(set, set2, this.sortByName);
    }
    
    public String generateGraphVizDotFile(final float n, final float n2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        final ScanSpec scanSpec = this.get(0).scanResult.scanSpec;
        if (!scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return GraphvizDotfileGenerator.generateGraphVizDotFile(this, n, n2, b, b2, b3, b4, b5, b6, scanSpec);
    }
    
    public ClassInfoList(final Collection<ClassInfo> c) {
        this((c instanceof Set) ? c : new HashSet<ClassInfo>(c), null, true);
    }
    
    public ClassInfoList getEnums() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isEnum();
            }
        });
    }
    
    public String generateGraphVizDotFile(final float n, final float n2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        return this.generateGraphVizDotFile(n, n2, b, b2, b3, b4, b5, true);
    }
    
    public ClassInfoList directOnly() {
        return new ClassInfoList(this.directlyRelatedClasses, this.directlyRelatedClasses, this.sortByName);
    }
    
    public ClassInfoList() {
        super(1);
        this.sortByName = false;
        this.directlyRelatedClasses = new HashSet<ClassInfo>(2);
    }
    
    public ClassInfoList getInterfacesAndAnnotations() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isInterfaceOrAnnotation();
            }
        });
    }
    
    public ClassInfoList intersect(final ClassInfoList... array) {
        final ArrayDeque<ClassInfoList> arrayDeque = new ArrayDeque<ClassInfoList>();
        arrayDeque.add(this);
        int n = 0;
        for (final ClassInfoList e : array) {
            if (e.sortByName) {
                arrayDeque.add(e);
            }
            else if (n == 0) {
                n = 1;
                arrayDeque.push(e);
            }
            else {
                arrayDeque.add(e);
            }
        }
        final ClassInfoList c = arrayDeque.remove();
        final LinkedHashSet set = new LinkedHashSet<ClassInfo>(c);
        while (!arrayDeque.isEmpty()) {
            set.retainAll(arrayDeque.remove());
        }
        final LinkedHashSet<ClassInfo> set2 = new LinkedHashSet<ClassInfo>(this.directlyRelatedClasses);
        for (int length2 = array.length, j = 0; j < length2; ++j) {
            set2.retainAll(array[j].directlyRelatedClasses);
        }
        return new ClassInfoList((Set<ClassInfo>)set, set2, c.sortByName);
    }
    
    public ClassInfoList getAnnotations() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isAnnotation();
            }
        });
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassInfoList)) {
            return false;
        }
        final ClassInfoList list = (ClassInfoList)o;
        if (this.directlyRelatedClasses == null != (list.directlyRelatedClasses == null)) {
            return false;
        }
        if (this.directlyRelatedClasses == null) {
            return super.equals(list);
        }
        return super.equals(list) && this.directlyRelatedClasses.equals(list.directlyRelatedClasses);
    }
    
    public static ClassInfoList emptyList() {
        return ClassInfoList.EMPTY_LIST;
    }
    
    ClassInfoList(final ClassInfo.ReachableAndDirectlyRelatedClasses reachableAndDirectlyRelatedClasses, final boolean b) {
        this(reachableAndDirectlyRelatedClasses.reachableClasses, reachableAndDirectlyRelatedClasses.directlyRelatedClasses, b);
    }
    
    public List<Class<?>> loadClasses(final boolean b) {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayList list = new ArrayList<Object>();
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            final Class<?> loadClass = iterator.next().loadClass(b);
            if (loadClass != null) {
                list.add(loadClass);
            }
        }
        return (List<Class<?>>)(list.isEmpty() ? Collections.emptyList() : list);
    }
    
    static {
        (EMPTY_LIST = new ClassInfoList()).makeUnmodifiable();
    }
    
    public String generateGraphVizDotFileFromClassDependencies() {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        final ScanSpec scanSpec = this.get(0).scanResult.scanSpec;
        if (!scanSpec.enableInterClassDependencies) {
            throw new IllegalArgumentException("Please call ClassGraph#enableInterClassDependencies() before #scan()");
        }
        return GraphvizDotfileGenerator.generateGraphVizDotFileFromInterClassDependencies(this, 10.5f, 8.0f, scanSpec.enableExternalClasses);
    }
    
    public void generateGraphVizDotFile(final File file) throws IOException {
        try (final PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(this.generateGraphVizDotFile());
        }
    }
    
    public ClassInfoList filter(final ClassInfoFilter classInfoFilter) {
        final LinkedHashSet<ClassInfo> set = new LinkedHashSet<ClassInfo>(this.size());
        final LinkedHashSet<ClassInfo> set2 = new LinkedHashSet<ClassInfo>(this.directlyRelatedClasses.size());
        for (final ClassInfo classInfo : this) {
            if (classInfoFilter.accept(classInfo)) {
                set.add(classInfo);
                if (!this.directlyRelatedClasses.contains(classInfo)) {
                    continue;
                }
                set2.add(classInfo);
            }
        }
        return new ClassInfoList(set, set2, this.sortByName);
    }
    
    public ClassInfoList union(final ClassInfoList... array) {
        final LinkedHashSet<ClassInfo> set = new LinkedHashSet<ClassInfo>(this);
        final LinkedHashSet<ClassInfo> set2 = new LinkedHashSet<ClassInfo>(this.directlyRelatedClasses);
        for (final ClassInfoList list : array) {
            set.addAll((Collection<?>)list);
            set2.addAll((Collection<?>)list.directlyRelatedClasses);
        }
        return new ClassInfoList(set, set2, this.sortByName);
    }
    
    public <T> List<Class<T>> loadClasses(final Class<T> clazz, final boolean b) {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayList list = new ArrayList<Object>();
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            final Class<T> loadClass = iterator.next().loadClass(clazz, b);
            if (loadClass != null) {
                list.add(loadClass);
            }
        }
        return (List<Class<T>>)(list.isEmpty() ? Collections.emptyList() : list);
    }
    
    public ClassInfoList(final int n) {
        super(n);
        this.sortByName = false;
        this.directlyRelatedClasses = new HashSet<ClassInfo>(2);
    }
    
    public ClassInfoList getInterfaces() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isInterface();
            }
        });
    }
    
    ClassInfoList(final Set<ClassInfo> set, final boolean b) {
        this(set, null, b);
    }
    
    ClassInfoList(final Set<ClassInfo> set, final Set<ClassInfo> set2, final boolean sortByName) {
        super(set);
        this.sortByName = sortByName;
        if (sortByName) {
            CollectionUtils.sortIfNotEmpty((List<Comparable>)this);
        }
        this.directlyRelatedClasses = ((set2 == null) ? set : set2);
    }
    
    public String generateGraphVizDotFile() {
        return this.generateGraphVizDotFile(10.5f, 8.0f, true, true, true, true, true);
    }
    
    public ClassInfoList getImplementedInterfaces() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isImplementedInterface();
            }
        });
    }
    
    public ClassInfoList getRecords() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isRecord();
            }
        });
    }
    
    public ClassInfoList getStandardClasses() {
        return this.filter(new ClassInfoFilter() {
            @Override
            public boolean accept(final ClassInfo classInfo) {
                return classInfo.isStandardClass();
            }
        });
    }
    
    public String generateGraphVizDotFileFromInterClassDependencies(final float n, final float n2, final boolean b) {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        if (!this.get(0).scanResult.scanSpec.enableInterClassDependencies) {
            throw new IllegalArgumentException("Please call ClassGraph#enableInterClassDependencies() before #scan()");
        }
        return GraphvizDotfileGenerator.generateGraphVizDotFileFromInterClassDependencies(this, n, n2, b);
    }
    
    public List<Class<?>> loadClasses() {
        return this.loadClasses(false);
    }
    
    public String generateGraphVizDotFile(final float n, final float n2) {
        return this.generateGraphVizDotFile(n, n2, true, true, true, true, true);
    }
    
    public <T> List<Class<T>> loadClasses(final Class<T> clazz) {
        return this.loadClasses(clazz, false);
    }
    
    @FunctionalInterface
    public interface ClassInfoFilter
    {
        boolean accept(final ClassInfo p0);
    }
}
