// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.lang.annotation.Inherited;
import java.util.AbstractMap;
import java.net.URI;
import nonapi.io.github.classgraph.types.TypeUtils;
import java.util.LinkedList;
import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.ArrayList;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Collections;
import nonapi.io.github.classgraph.types.ParseException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import nonapi.io.github.classgraph.json.Id;
import java.util.List;

public class ClassInfo extends ScanResultObject implements Comparable<ClassInfo>, HasName
{
    /* synthetic */ FieldInfoList fieldInfo;
    /* synthetic */ ModuleInfo moduleInfo;
    private /* synthetic */ int modifiers;
    private transient /* synthetic */ ClassTypeSignature typeSignature;
    private /* synthetic */ ClassInfoList referencedClasses;
    /* synthetic */ AnnotationParameterValueList annotationDefaultParamValues;
    transient /* synthetic */ ClassLoader classLoader;
    protected /* synthetic */ boolean isScannedClass;
    private transient /* synthetic */ List<ClassInfo> overrideOrder;
    private /* synthetic */ int classfileMinorVersion;
    transient /* synthetic */ ClasspathElement classpathElement;
    @Id
    protected /* synthetic */ String name;
    /* synthetic */ AnnotationInfoList annotationInfo;
    private static final /* synthetic */ ReachableAndDirectlyRelatedClasses NO_REACHABLE_CLASSES;
    private /* synthetic */ Set<String> referencedClassNames;
    /* synthetic */ MethodInfoList methodInfo;
    private /* synthetic */ String fullyQualifiedDefiningMethodName;
    protected /* synthetic */ boolean isExternalClass;
    private /* synthetic */ Map<RelType, Set<ClassInfo>> relatedClasses;
    private /* synthetic */ boolean isRecord;
    private /* synthetic */ int classfileMajorVersion;
    /* synthetic */ PackageInfo packageInfo;
    protected transient /* synthetic */ Resource classfileResource;
    /* synthetic */ boolean isInherited;
    transient /* synthetic */ boolean annotationDefaultParamValuesHasBeenConvertedToPrimitive;
    protected /* synthetic */ String typeSignatureStr;
    
    public ClassInfoList getAnnotations() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(RelType.CLASS_ANNOTATIONS, false, new ClassType[0]);
        Set<ClassInfo> set = null;
        final Iterator iterator = this.getSuperclasses().iterator();
        while (iterator.hasNext()) {
            for (final ClassInfo classInfo : iterator.next().filterClassInfo(RelType.CLASS_ANNOTATIONS, false, new ClassType[0]).reachableClasses) {
                if (classInfo != null && classInfo.isInherited) {
                    if (set == null) {
                        set = new LinkedHashSet<ClassInfo>();
                    }
                    set.add(classInfo);
                }
            }
        }
        if (set == null) {
            return new ClassInfoList(filterClassInfo, true);
        }
        set.addAll(filterClassInfo.reachableClasses);
        return new ClassInfoList(set, filterClassInfo.directlyRelatedClasses, true);
    }
    
    public boolean isInterface() {
        return this.isInterfaceOrAnnotation() && !this.isAnnotation();
    }
    
    public boolean isRecord() {
        return this.isRecord;
    }
    
    public boolean isEnum() {
        return (this.modifiers & 0x4000) != 0x0;
    }
    
    public MethodInfoList getDeclaredMethodInfo(final String s) {
        return this.getDeclaredMethodInfo(s, false, false, false);
    }
    
    public MethodInfoList getMethodInfo(final String s) {
        return this.getMethodInfo(s, false, false, false);
    }
    
    public MethodInfoList getDeclaredConstructorInfo() {
        return this.getDeclaredMethodInfo(null, false, true, false);
    }
    
    public boolean isAbstract() {
        return (this.modifiers & 0x400) != 0x0;
    }
    
    public FieldInfoList getFieldInfo() {
        if (!this.scanResult.scanSpec.enableFieldInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableFieldInfo() before #scan()");
        }
        final FieldInfoList list = new FieldInfoList();
        final HashSet<String> set = new HashSet<String>();
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            for (final FieldInfo fieldInfo : iterator.next().getDeclaredFieldInfo()) {
                if (set.add(fieldInfo.getName())) {
                    list.add(fieldInfo);
                }
            }
        }
        return list;
    }
    
    public File getClasspathElementFile() {
        if (this.classpathElement == null) {
            throw new IllegalArgumentException("Classpath element is not known for this classpath element");
        }
        return this.classpathElement.getFile();
    }
    
    void setIsAnnotation(final boolean b) {
        if (b) {
            this.modifiers |= 0x2000;
        }
    }
    
    public boolean implementsInterface(final String s) {
        return this.getInterfaces().containsName(s);
    }
    
    public boolean isAnnotation() {
        return (this.modifiers & 0x2000) != 0x0;
    }
    
    public URL getClasspathElementURL() {
        if (this.classpathElement == null) {
            throw new IllegalArgumentException("Classpath element is not known for this classpath element");
        }
        try {
            return this.classpathElement.getURI().toURL();
        }
        catch (MalformedURLException cause) {
            throw new IllegalArgumentException("Could not get classpath element URL", cause);
        }
    }
    
    public ModuleRef getModuleRef() {
        if (this.classpathElement == null) {
            throw new IllegalArgumentException("Classpath element is not known for this classpath element");
        }
        return (this.classpathElement instanceof ClasspathElementModule) ? ((ClasspathElementModule)this.classpathElement).getModuleRef() : null;
    }
    
    public ClassInfo getSuperclass() {
        final Set<ClassInfo> obj = this.relatedClasses.get(RelType.SUPERCLASSES);
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        if (obj.size() > 2) {
            throw new IllegalArgumentException("More than one superclass: " + obj);
        }
        final ClassInfo classInfo = obj.iterator().next();
        if (classInfo.getName().equals("java.lang.Object")) {
            return null;
        }
        return classInfo;
    }
    
    void setIsRecord(final boolean isRecord) {
        if (isRecord) {
            this.isRecord = isRecord;
        }
    }
    
    @Override
    protected String getClassName() {
        return this.name;
    }
    
    public ClassInfoList getMethodAnnotations() {
        return this.getFieldOrMethodAnnotations(RelType.METHOD_ANNOTATIONS);
    }
    
    public ClassTypeSignature getTypeSignature() {
        if (this.typeSignatureStr == null) {
            return null;
        }
        if (this.typeSignature == null) {
            try {
                this.typeSignature = ClassTypeSignature.parse(this.typeSignatureStr, this);
                this.typeSignature.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeSignature;
    }
    
    static {
        ANNOTATION_CLASS_MODIFIER = 8192;
        NO_REACHABLE_CLASSES = new ReachableAndDirectlyRelatedClasses((Set)Collections.emptySet(), (Set)Collections.emptySet());
    }
    
    public MethodInfoList getMethodInfo() {
        return this.getMethodInfo(null, true, false, false);
    }
    
    void addSuperclass(final String s, final Map<String, ClassInfo> map) {
        if (s != null && !s.equals("java.lang.Object")) {
            final ClassInfo orCreateClassInfo = getOrCreateClassInfo(s, map);
            this.addRelatedClass(RelType.SUPERCLASSES, orCreateClassInfo);
            orCreateClassInfo.addRelatedClass(RelType.SUBCLASSES, this);
        }
    }
    
    public ClassInfoList getSuperclasses() {
        return new ClassInfoList(this.filterClassInfo(RelType.SUPERCLASSES, false, new ClassType[0]), false);
    }
    
    public FieldInfo getDeclaredFieldInfo(final String anObject) {
        if (!this.scanResult.scanSpec.enableFieldInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableFieldInfo() before #scan()");
        }
        if (this.fieldInfo == null) {
            return null;
        }
        for (final FieldInfo fieldInfo : this.fieldInfo) {
            if (fieldInfo.getName().equals(anObject)) {
                return fieldInfo;
            }
        }
        return null;
    }
    
    ClassInfo() {
        this.isExternalClass = true;
    }
    
    public boolean hasDeclaredMethod(final String s) {
        return this.getDeclaredMethodInfo().containsName(s);
    }
    
    public String getTypeSignatureStr() {
        return this.typeSignatureStr;
    }
    
    public boolean isAnonymousInnerClass() {
        return this.fullyQualifiedDefiningMethodName != null;
    }
    
    private static Set<ClassInfo> filterClassInfo(final Collection<ClassInfo> collection, final ScanSpec scanSpec, final boolean b, final ClassType... array) {
        if (collection == null) {
            return Collections.emptySet();
        }
        boolean b2 = array.length == 0;
        boolean b3 = false;
        int n = 0;
        int n2 = 0;
        boolean b4 = false;
        boolean b5 = false;
        for (final ClassType obj : array) {
            switch (obj) {
                case ALL: {
                    b2 = true;
                    break;
                }
                case STANDARD_CLASS: {
                    b3 = true;
                    break;
                }
                case IMPLEMENTED_INTERFACE: {
                    n = 1;
                    break;
                }
                case ANNOTATION: {
                    n2 = 1;
                    break;
                }
                case INTERFACE_OR_ANNOTATION: {
                    n2 = (n = 1);
                    break;
                }
                case ENUM: {
                    b4 = true;
                    break;
                }
                case RECORD: {
                    b5 = true;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown ClassType: " + obj);
                }
            }
        }
        if (b3 && n != 0 && n2 != 0) {
            b2 = true;
        }
        final LinkedHashSet<ClassInfo> set = new LinkedHashSet<ClassInfo>(collection.size());
        for (final ClassInfo classInfo : collection) {
            if ((b2 || (b3 && classInfo.isStandardClass()) || (n != 0 && classInfo.isImplementedInterface()) || (n2 != 0 && classInfo.isAnnotation()) || (b4 && classInfo.isEnum()) || (b5 && classInfo.isRecord())) && !scanSpec.classOrPackageIsRejected(classInfo.name) && (!classInfo.isExternalClass || scanSpec.enableExternalClasses || !b)) {
                set.add(classInfo);
            }
        }
        return set;
    }
    
    private List<ClassInfo> getOverrideOrder() {
        if (this.overrideOrder == null) {
            this.overrideOrder = this.getOverrideOrder(new HashSet<ClassInfo>(), new ArrayList<ClassInfo>());
        }
        return this.overrideOrder;
    }
    
    public boolean hasDeclaredMethodParameterAnnotation(final String s) {
        final Iterator iterator = this.getDeclaredMethodInfo().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasParameterAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    void addFullyQualifiedDefiningMethodName(final String fullyQualifiedDefiningMethodName) {
        this.fullyQualifiedDefiningMethodName = fullyQualifiedDefiningMethodName;
    }
    
    public Class<?> loadClass() {
        return super.loadClass(false);
    }
    
    void handleRepeatableAnnotations(final Set<String> set) {
        if (this.annotationInfo != null) {
            this.annotationInfo.handleRepeatableAnnotations(set, this, RelType.CLASS_ANNOTATIONS, RelType.CLASSES_WITH_ANNOTATION, null);
        }
        if (this.fieldInfo != null) {
            final Iterator iterator = this.fieldInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().handleRepeatableAnnotations(set);
            }
        }
        if (this.methodInfo != null) {
            final Iterator iterator2 = this.methodInfo.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().handleRepeatableAnnotations(set);
            }
        }
    }
    
    protected ClassInfo getClassInfo() {
        return this;
    }
    
    public ClassInfoList getClassDependencies() {
        if (!this.scanResult.scanSpec.enableInterClassDependencies) {
            throw new IllegalArgumentException("Please call ClassGraph#enableInterClassDependencies() before #scan()");
        }
        return (this.referencedClasses == null) ? ClassInfoList.EMPTY_LIST : this.referencedClasses;
    }
    
    public String getSimpleName() {
        return getSimpleName(this.name);
    }
    
    public String getPackageName() {
        return PackageInfo.getParentPackageName(this.name);
    }
    
    void addReferencedClassNames(final Set<String> referencedClassNames) {
        if (this.referencedClassNames == null) {
            this.referencedClassNames = referencedClassNames;
        }
        else {
            this.referencedClassNames.addAll(referencedClassNames);
        }
    }
    
    public FieldInfo getFieldInfo(final String s) {
        if (!this.scanResult.scanSpec.enableFieldInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableFieldInfo() before #scan()");
        }
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            final FieldInfo declaredFieldInfo = iterator.next().getDeclaredFieldInfo(s);
            if (declaredFieldInfo != null) {
                return declaredFieldInfo;
            }
        }
        return null;
    }
    
    public boolean isSynthetic() {
        return (this.modifiers & 0x1000) != 0x0;
    }
    
    public boolean hasDeclaredMethodAnnotation(final String s) {
        final Iterator iterator = this.getDeclaredMethodInfo().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.name == null) ? 0 : this.name.hashCode();
    }
    
    protected ClassInfo(final String name, final int modifiers, final Resource classfileResource) {
        this.isExternalClass = true;
        this.name = name;
        if (name.endsWith(";")) {
            throw new IllegalArgumentException("Bad class name");
        }
        this.setModifiers(modifiers);
        this.classfileResource = classfileResource;
        this.relatedClasses = new EnumMap<RelType, Set<ClassInfo>>(RelType.class);
    }
    
    public boolean hasMethod(final String s) {
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasDeclaredMethod(s)) {
                return true;
            }
        }
        return false;
    }
    
    public ClassInfoList getOuterClasses() {
        return new ClassInfoList(this.filterClassInfo(RelType.CONTAINED_WITHIN_OUTER_CLASS, false, new ClassType[0]), false);
    }
    
    public <T> Class<T> loadClass(final Class<T> clazz, final boolean b) {
        return super.loadClass(clazz, b);
    }
    
    public AnnotationInfoList getAnnotationInfoRepeatable(final String s) {
        return this.getAnnotationInfo().getRepeatable(s);
    }
    
    public boolean isFinal() {
        return (this.modifiers & 0x10) != 0x0;
    }
    
    public boolean hasDeclaredFieldAnnotation(final String s) {
        final Iterator iterator = this.getDeclaredFieldInfo().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasMethodParameterAnnotation(final String s) {
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasDeclaredMethodParameterAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    public FieldInfoList getDeclaredFieldInfo() {
        if (!this.scanResult.scanSpec.enableFieldInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableFieldInfo() before #scan()");
        }
        return (this.fieldInfo == null) ? FieldInfoList.EMPTY_LIST : this.fieldInfo;
    }
    
    public ClassInfoList getInnerClasses() {
        return new ClassInfoList(this.filterClassInfo(RelType.CONTAINS_INNER_CLASS, false, new ClassType[0]), true);
    }
    
    private void addFieldOrMethodAnnotationInfo(final AnnotationInfoList list, final boolean b, final int mod, final Map<String, ClassInfo> map) {
        if (list != null) {
            final Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                final ClassInfo orCreateClassInfo = getOrCreateClassInfo(iterator.next().getName(), map);
                orCreateClassInfo.setModifiers(8192);
                this.addRelatedClass(b ? RelType.FIELD_ANNOTATIONS : RelType.METHOD_ANNOTATIONS, orCreateClassInfo);
                orCreateClassInfo.addRelatedClass(b ? RelType.CLASSES_WITH_FIELD_ANNOTATION : RelType.CLASSES_WITH_METHOD_ANNOTATION, this);
                if (!Modifier.isPrivate(mod)) {
                    orCreateClassInfo.addRelatedClass(b ? RelType.CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION : RelType.CLASSES_WITH_NONPRIVATE_METHOD_ANNOTATION, this);
                }
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ClassInfo && this.name.equals(((ClassInfo)o).name));
    }
    
    static ClassInfo getOrCreateClassInfo(final String s, final Map<String, ClassInfo> map) {
        int n = 0;
        String s2;
        for (s2 = s; s2.endsWith("[]"); s2 = s2.substring(0, s2.length() - 2)) {
            ++n;
        }
        while (s2.startsWith("[")) {
            ++n;
            s2 = s2.substring(1);
        }
        if (s2.endsWith(";")) {
            s2 = s2.substring(s2.length() - 1);
        }
        final String replace = s2.replace('/', '.');
        ClassInfo classInfo = map.get(s);
        if (classInfo == null) {
            map.put(s, classInfo = ((n == 0) ? new ClassInfo(replace, 0, null) : new ArrayClassInfo(new ArrayTypeSignature(replace, n))));
        }
        return classInfo;
    }
    
    public MethodInfoList getConstructorInfo() {
        return this.getMethodInfo(null, false, true, false);
    }
    
    public boolean hasFieldAnnotation(final String s) {
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasDeclaredFieldAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    public ClassInfoList getClassesWithMethodParameterAnnotation() {
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>(this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_METHOD_PARAMETER_ANNOTATION));
        final Iterator iterator = this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_NONPRIVATE_METHOD_PARAMETER_ANNOTATION).iterator();
        while (iterator.hasNext()) {
            set.addAll((Collection<?>)iterator.next().getSubclasses());
        }
        return new ClassInfoList(set, new HashSet<ClassInfo>(this.getClassesWithMethodParameterAnnotationDirectOnly()), true);
    }
    
    private ReachableAndDirectlyRelatedClasses filterClassInfo(final RelType relType, final boolean b, final ClassType... array) {
        final Set<ClassInfo> c = this.relatedClasses.get(relType);
        if (c == null) {
            return ClassInfo.NO_REACHABLE_CLASSES;
        }
        final LinkedHashSet set = new LinkedHashSet<ClassInfo>(c);
        final LinkedHashSet set2 = new LinkedHashSet<Object>(set);
        if (relType == RelType.METHOD_ANNOTATIONS || relType == RelType.METHOD_PARAMETER_ANNOTATIONS || relType == RelType.FIELD_ANNOTATIONS) {
            final Iterator<ClassInfo> iterator = set.iterator();
            while (iterator.hasNext()) {
                set2.addAll((Collection<?>)iterator.next().filterClassInfo(RelType.CLASS_ANNOTATIONS, b, new ClassType[0]).reachableClasses);
            }
        }
        else if (relType == RelType.CLASSES_WITH_METHOD_ANNOTATION || relType == RelType.CLASSES_WITH_NONPRIVATE_METHOD_ANNOTATION || relType == RelType.CLASSES_WITH_METHOD_PARAMETER_ANNOTATION || relType == RelType.CLASSES_WITH_NONPRIVATE_METHOD_PARAMETER_ANNOTATION || relType == RelType.CLASSES_WITH_FIELD_ANNOTATION || relType == RelType.CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION) {
            final Iterator<ClassInfo> iterator2 = this.filterClassInfo(RelType.CLASSES_WITH_ANNOTATION, b, ClassType.ANNOTATION).reachableClasses.iterator();
            while (iterator2.hasNext()) {
                final Set<ClassInfo> set3 = iterator2.next().relatedClasses.get(relType);
                if (set3 != null) {
                    set2.addAll((Collection<?>)set3);
                }
            }
        }
        else {
            final LinkedList list = new LinkedList<ClassInfo>(set);
            while (!list.isEmpty()) {
                final Set<ClassInfo> set4 = list.removeFirst().relatedClasses.get(relType);
                if (set4 != null) {
                    for (final ClassInfo e : set4) {
                        if (set2.add(e)) {
                            list.add(e);
                        }
                    }
                }
            }
        }
        if (set2.isEmpty()) {
            return ClassInfo.NO_REACHABLE_CLASSES;
        }
        if (relType == RelType.CLASS_ANNOTATIONS || relType == RelType.METHOD_ANNOTATIONS || relType == RelType.METHOD_PARAMETER_ANNOTATIONS || relType == RelType.FIELD_ANNOTATIONS) {
            Set<?> set5 = null;
            for (final ClassInfo classInfo : set2) {
                if (classInfo.getName().startsWith("java.lang.annotation.") && !set.contains(classInfo)) {
                    if (set5 == null) {
                        set5 = new LinkedHashSet<Object>();
                    }
                    set5.add(classInfo);
                }
            }
            if (set5 != null) {
                set2.removeAll(set5);
            }
        }
        return new ReachableAndDirectlyRelatedClasses((Set)filterClassInfo((Collection<ClassInfo>)set2, this.scanResult.scanSpec, b, array), (Set)filterClassInfo((Collection<ClassInfo>)set, this.scanResult.scanSpec, b, array));
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        super.findReferencedClassInfo(map, set);
        if (this.referencedClassNames != null) {
            final Iterator<String> iterator = this.referencedClassNames.iterator();
            while (iterator.hasNext()) {
                final ClassInfo orCreateClassInfo = getOrCreateClassInfo(iterator.next(), map);
                orCreateClassInfo.setScanResult(this.scanResult);
                set.add(orCreateClassInfo);
            }
        }
        this.getMethodInfo().findReferencedClassInfo(map, set);
        this.getFieldInfo().findReferencedClassInfo(map, set);
        this.getAnnotationInfo().findReferencedClassInfo(map, set);
        if (this.annotationDefaultParamValues != null) {
            this.annotationDefaultParamValues.findReferencedClassInfo(map, set);
        }
        final ClassTypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            typeSignature.findReferencedClassInfo(map, set);
        }
    }
    
    public String getModifiersStr() {
        final StringBuilder sb = new StringBuilder();
        TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.CLASS, false, sb);
        return sb.toString();
    }
    
    void addAnnotationParamDefaultValues(final AnnotationParameterValueList annotationDefaultParamValues) {
        this.setIsAnnotation(true);
        if (this.annotationDefaultParamValues == null) {
            this.annotationDefaultParamValues = annotationDefaultParamValues;
        }
        else {
            this.annotationDefaultParamValues.addAll(annotationDefaultParamValues);
        }
    }
    
    public MethodInfoList getMethodAndConstructorInfo() {
        return this.getMethodInfo(null, true, true, false);
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
        if (this.annotationInfo != null) {
            final Iterator iterator = this.annotationInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
        if (this.fieldInfo != null) {
            final Iterator iterator2 = this.fieldInfo.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setScanResult(scanResult);
            }
        }
        if (this.methodInfo != null) {
            final Iterator iterator3 = this.methodInfo.iterator();
            while (iterator3.hasNext()) {
                iterator3.next().setScanResult(scanResult);
            }
        }
        if (this.annotationDefaultParamValues != null) {
            final Iterator iterator4 = this.annotationDefaultParamValues.iterator();
            while (iterator4.hasNext()) {
                iterator4.next().setScanResult(scanResult);
            }
        }
    }
    
    public ModuleInfo getModuleInfo() {
        return this.moduleInfo;
    }
    
    private ClassInfoList getClassesWithFieldOrMethodAnnotation(final RelType relType) {
        final boolean b = relType == RelType.CLASSES_WITH_FIELD_ANNOTATION || relType == RelType.CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION;
        if (b) {
            if (!this.scanResult.scanSpec.enableFieldInfo) {
                throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
            }
        }
        else if (!this.scanResult.scanSpec.enableMethodInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
        }
        if (this.scanResult.scanSpec.enableAnnotationInfo) {
            final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(relType, !this.isExternalClass, new ClassType[0]);
            final ReachableAndDirectlyRelatedClasses filterClassInfo2 = this.filterClassInfo(RelType.CLASSES_WITH_ANNOTATION, !this.isExternalClass, ClassType.ANNOTATION);
            if (filterClassInfo2.reachableClasses.isEmpty()) {
                return new ClassInfoList(filterClassInfo, true);
            }
            final LinkedHashSet set = new LinkedHashSet<Object>(filterClassInfo.reachableClasses);
            for (final ClassInfo classInfo : filterClassInfo2.reachableClasses) {
                set.addAll((Collection<?>)classInfo.filterClassInfo(relType, !classInfo.isExternalClass, new ClassType[0]).reachableClasses);
            }
            return new ClassInfoList((Set<ClassInfo>)set, filterClassInfo.directlyRelatedClasses, true);
        }
        throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
    }
    
    public URI getClasspathElementURI() {
        if (this.classpathElement == null) {
            throw new IllegalArgumentException("Classpath element is not known for this classpath element");
        }
        return this.classpathElement.getURI();
    }
    
    static ClassInfoList getAllImplementedInterfaceClasses(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.IMPLEMENTED_INTERFACE), true);
    }
    
    private MethodInfoList getMethodInfo(final String s, final boolean b, final boolean b2, final boolean b3) {
        if (!this.scanResult.scanSpec.enableMethodInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableMethodInfo() before #scan()");
        }
        final MethodInfoList list = new MethodInfoList();
        final HashSet<AbstractMap.SimpleEntry<String, String>> set = new HashSet<AbstractMap.SimpleEntry<String, String>>();
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            for (final MethodInfo methodInfo : iterator.next().getDeclaredMethodInfo(s, b, b2, b3)) {
                if (set.add(new AbstractMap.SimpleEntry<String, String>(methodInfo.getName(), methodInfo.getTypeDescriptor().toString()))) {
                    list.add(methodInfo);
                }
            }
        }
        return list;
    }
    
    private List<ClassInfo> getOverrideOrder(final Set<ClassInfo> set, final List<ClassInfo> list) {
        if (set.add(this)) {
            list.add(this);
            final Iterator iterator = this.getInterfaces().iterator();
            while (iterator.hasNext()) {
                iterator.next().getOverrideOrder(set, list);
            }
            final ClassInfo superclass = this.getSuperclass();
            if (superclass != null) {
                superclass.getOverrideOrder(set, list);
            }
        }
        return list;
    }
    
    public boolean isPublic() {
        return (this.modifiers & 0x1) != 0x0;
    }
    
    public boolean hasAnnotation(final String s) {
        return this.getAnnotations().containsName(s);
    }
    
    static ClassInfoList getAllAnnotationClasses(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.ANNOTATION), true);
    }
    
    public ClassInfoList getMethodParameterAnnotations() {
        return this.getFieldOrMethodAnnotations(RelType.METHOD_PARAMETER_ANNOTATIONS);
    }
    
    public boolean isInterfaceOrAnnotation() {
        return (this.modifiers & 0x200) != 0x0;
    }
    
    static ClassInfoList getAllRecords(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.RECORD), true);
    }
    
    static ClassInfoList getAllInterfacesOrAnnotationClasses(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.INTERFACE_OR_ANNOTATION), true);
    }
    
    ClassInfoList getClassesWithMethodParameterAnnotationDirectOnly() {
        return new ClassInfoList(this.filterClassInfo(RelType.CLASSES_WITH_METHOD_PARAMETER_ANNOTATION, !this.isExternalClass, new ClassType[0]), true);
    }
    
    void addMethodInfo(final MethodInfoList methodInfo, final Map<String, ClassInfo> map) {
        for (final MethodInfo methodInfo2 : methodInfo) {
            this.addFieldOrMethodAnnotationInfo(methodInfo2.annotationInfo, false, methodInfo2.getModifiers(), map);
            if (methodInfo2.parameterAnnotationInfo != null) {
                for (int i = 0; i < methodInfo2.parameterAnnotationInfo.length; ++i) {
                    final AnnotationInfo[] array = methodInfo2.parameterAnnotationInfo[i];
                    if (array != null) {
                        for (int j = 0; j < array.length; ++j) {
                            final ClassInfo orCreateClassInfo = getOrCreateClassInfo(array[j].getName(), map);
                            orCreateClassInfo.setModifiers(8192);
                            this.addRelatedClass(RelType.METHOD_PARAMETER_ANNOTATIONS, orCreateClassInfo);
                            orCreateClassInfo.addRelatedClass(RelType.CLASSES_WITH_METHOD_PARAMETER_ANNOTATION, this);
                            if (!Modifier.isPrivate(methodInfo2.getModifiers())) {
                                orCreateClassInfo.addRelatedClass(RelType.CLASSES_WITH_NONPRIVATE_METHOD_PARAMETER_ANNOTATION, this);
                            }
                        }
                    }
                }
            }
        }
        if (this.methodInfo == null) {
            this.methodInfo = methodInfo;
        }
        else {
            this.methodInfo.addAll(methodInfo);
        }
    }
    
    public AnnotationParameterValueList getAnnotationDefaultParameterValues() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        if (!this.isAnnotation()) {
            throw new IllegalArgumentException("Class is not an annotation: " + this.getName());
        }
        if (this.annotationDefaultParamValues == null) {
            return AnnotationParameterValueList.EMPTY_LIST;
        }
        if (!this.annotationDefaultParamValuesHasBeenConvertedToPrimitive) {
            this.annotationDefaultParamValues.convertWrapperArraysToPrimitiveArrays(this);
            this.annotationDefaultParamValuesHasBeenConvertedToPrimitive = true;
        }
        return this.annotationDefaultParamValues;
    }
    
    public AnnotationInfoList getAnnotationInfo() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        return AnnotationInfoList.getIndirectAnnotations(this.annotationInfo, this);
    }
    
    public Class<?> loadClass(final boolean b) {
        return super.loadClass(b);
    }
    
    public int getClassfileMinorVersion() {
        return this.classfileMinorVersion;
    }
    
    public ClassInfoList getClassesWithFieldAnnotation() {
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>(this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_FIELD_ANNOTATION));
        final Iterator iterator = this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION).iterator();
        while (iterator.hasNext()) {
            set.addAll((Collection<?>)iterator.next().getSubclasses());
        }
        return new ClassInfoList(set, new HashSet<ClassInfo>(this.getClassesWithMethodAnnotationDirectOnly()), true);
    }
    
    static String getSimpleName(final String s) {
        return s.substring(s.lastIndexOf(46) + 1);
    }
    
    public int getModifiers() {
        return this.modifiers;
    }
    
    public ClassInfoList getInterfaces() {
        final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(RelType.IMPLEMENTED_INTERFACES, false, new ClassType[0]);
        final LinkedHashSet set = new LinkedHashSet<Object>(filterClassInfo.reachableClasses);
        final Iterator<ClassInfo> iterator = this.filterClassInfo(RelType.SUPERCLASSES, false, new ClassType[0]).reachableClasses.iterator();
        while (iterator.hasNext()) {
            set.addAll((Collection<?>)iterator.next().filterClassInfo(RelType.IMPLEMENTED_INTERFACES, false, new ClassType[0]).reachableClasses);
        }
        return new ClassInfoList((Set<ClassInfo>)set, filterClassInfo.directlyRelatedClasses, true);
    }
    
    protected String toString(final boolean b) {
        final ClassTypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            return typeSignature.toString(this.name, b, this.modifiers, this.isAnnotation(), this.isInterface());
        }
        final StringBuilder sb = new StringBuilder();
        if (b) {
            sb.append(this.name);
        }
        else {
            TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.CLASS, false, sb);
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(this.isRecord() ? "record " : (this.isEnum() ? "enum " : (this.isAnnotation() ? "@interface " : (this.isInterface() ? "interface " : "class "))));
            sb.append(this.name);
            final ClassInfo superclass = this.getSuperclass();
            if (superclass != null && !superclass.getName().equals("java.lang.Object")) {
                sb.append(" extends ").append(superclass.toString(true));
            }
            final Set<ClassInfo> directlyRelatedClasses = this.filterClassInfo(RelType.IMPLEMENTED_INTERFACES, false, new ClassType[0]).directlyRelatedClasses;
            if (!directlyRelatedClasses.isEmpty()) {
                sb.append(this.isInterface() ? " extends " : " implements ");
                int n = 1;
                for (final ClassInfo classInfo : directlyRelatedClasses) {
                    if (n != 0) {
                        n = 0;
                    }
                    else {
                        sb.append(", ");
                    }
                    sb.append(classInfo.toString(true));
                }
            }
        }
        return sb.toString();
    }
    
    ClassInfoList getClassesWithAnnotationDirectOnly() {
        return new ClassInfoList(this.filterClassInfo(RelType.CLASSES_WITH_ANNOTATION, !this.isExternalClass, new ClassType[0]), true);
    }
    
    public boolean isOuterClass() {
        return !this.getInnerClasses().isEmpty();
    }
    
    ClassInfoList getClassesWithMethodAnnotationDirectOnly() {
        return new ClassInfoList(this.filterClassInfo(RelType.CLASSES_WITH_METHOD_ANNOTATION, !this.isExternalClass, new ClassType[0]), true);
    }
    
    static ClassInfoList getAllStandardClasses(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.STANDARD_CLASS), true);
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public PackageInfo getPackageInfo() {
        return this.packageInfo;
    }
    
    public <T> Class<T> loadClass(final Class<T> clazz) {
        return super.loadClass(clazz, false);
    }
    
    static ClassInfo addScannedClass(final String str, final int n, final boolean isExternalClass, final Map<String, ClassInfo> map, final ClasspathElement classpathElement, final Resource classfileResource) {
        ClassInfo classInfo = map.get(str);
        if (classInfo == null) {
            map.put(str, classInfo = new ClassInfo(str, n, classfileResource));
        }
        else {
            if (classInfo.isScannedClass) {
                throw new IllegalArgumentException("Class " + str + " should not have been encountered more than once due to classpath masking -- please report this bug at: https://github.com/classgraph/classgraph/issues");
            }
            classInfo.classfileResource = classfileResource;
            final ClassInfo classInfo2 = classInfo;
            classInfo2.modifiers |= n;
        }
        classInfo.isScannedClass = true;
        classInfo.isExternalClass = isExternalClass;
        classInfo.classpathElement = classpathElement;
        classInfo.classLoader = classpathElement.getClassLoader();
        return classInfo;
    }
    
    public boolean hasMethodAnnotation(final String s) {
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasDeclaredMethodAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    public int getClassfileMajorVersion() {
        return this.classfileMajorVersion;
    }
    
    static ClassInfoList getAllEnums(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.ENUM), true);
    }
    
    public ClassInfoList getSubclasses() {
        if (this.getName().equals("java.lang.Object")) {
            return this.scanResult.getAllClasses();
        }
        return new ClassInfoList(this.filterClassInfo(RelType.SUBCLASSES, !this.isExternalClass, new ClassType[0]), true);
    }
    
    public ClassInfoList getFieldAnnotations() {
        return this.getFieldOrMethodAnnotations(RelType.FIELD_ANNOTATIONS);
    }
    
    public boolean isStandardClass() {
        return !this.isAnnotation() && !this.isInterface();
    }
    
    public ClassInfoList getClassesWithMethodAnnotation() {
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>(this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_METHOD_ANNOTATION));
        final Iterator iterator = this.getClassesWithFieldOrMethodAnnotation(RelType.CLASSES_WITH_NONPRIVATE_METHOD_ANNOTATION).iterator();
        while (iterator.hasNext()) {
            set.addAll((Collection<?>)iterator.next().getSubclasses());
        }
        return new ClassInfoList(set, new HashSet<ClassInfo>(this.getClassesWithMethodAnnotationDirectOnly()), true);
    }
    
    static ClassInfoList getAllClasses(final Collection<ClassInfo> collection, final ScanSpec scanSpec) {
        return new ClassInfoList(filterClassInfo(collection, scanSpec, true, ClassType.ALL), true);
    }
    
    static void addClassContainment(final List<Classfile.ClassContainment> list, final Map<String, ClassInfo> map) {
        for (final Classfile.ClassContainment classContainment : list) {
            final ClassInfo orCreateClassInfo = getOrCreateClassInfo(classContainment.innerClassName, map);
            orCreateClassInfo.setModifiers(classContainment.innerClassModifierBits);
            final ClassInfo orCreateClassInfo2 = getOrCreateClassInfo(classContainment.outerClassName, map);
            orCreateClassInfo.addRelatedClass(RelType.CONTAINED_WITHIN_OUTER_CLASS, orCreateClassInfo2);
            orCreateClassInfo2.addRelatedClass(RelType.CONTAINS_INNER_CLASS, orCreateClassInfo);
        }
    }
    
    public boolean hasField(final String s) {
        final Iterator<ClassInfo> iterator = this.getOverrideOrder().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().hasDeclaredField(s)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isInnerClass() {
        return !this.getOuterClasses().isEmpty();
    }
    
    public boolean isImplementedInterface() {
        return this.relatedClasses.get(RelType.CLASSES_IMPLEMENTING) != null || this.isInterface();
    }
    
    void setReferencedClasses(final ClassInfoList referencedClasses) {
        this.referencedClasses = referencedClasses;
    }
    
    void addClassAnnotation(final AnnotationInfo annotationInfo, final Map<String, ClassInfo> map) {
        final ClassInfo orCreateClassInfo = getOrCreateClassInfo(annotationInfo.getName(), map);
        orCreateClassInfo.setModifiers(8192);
        if (this.annotationInfo == null) {
            this.annotationInfo = new AnnotationInfoList(2);
        }
        this.annotationInfo.add(annotationInfo);
        this.addRelatedClass(RelType.CLASS_ANNOTATIONS, orCreateClassInfo);
        orCreateClassInfo.addRelatedClass(RelType.CLASSES_WITH_ANNOTATION, this);
        if (annotationInfo.getName().equals(Inherited.class.getName())) {
            this.isInherited = true;
        }
    }
    
    boolean addRelatedClass(final RelType relType, final ClassInfo classInfo) {
        Set<ClassInfo> set = this.relatedClasses.get(relType);
        if (set == null) {
            this.relatedClasses.put(relType, set = new LinkedHashSet<ClassInfo>(4));
        }
        return set.add(classInfo);
    }
    
    void setIsInterface(final boolean b) {
        if (b) {
            this.modifiers |= 0x200;
        }
    }
    
    public boolean isArrayClass() {
        return this instanceof ArrayClassInfo;
    }
    
    public boolean isExternalClass() {
        return this.isExternalClass;
    }
    
    public Resource getResource() {
        return this.classfileResource;
    }
    
    public MethodInfoList getDeclaredMethodAndConstructorInfo() {
        return this.getDeclaredMethodInfo(null, true, true, false);
    }
    
    public ClassInfoList getClassesWithAnnotation() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        if (!this.isAnnotation()) {
            throw new IllegalArgumentException("Class is not an annotation: " + this.getName());
        }
        final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(RelType.CLASSES_WITH_ANNOTATION, !this.isExternalClass, new ClassType[0]);
        if (this.isInherited) {
            final LinkedHashSet set = new LinkedHashSet<Object>(filterClassInfo.reachableClasses);
            final Iterator<ClassInfo> iterator = filterClassInfo.reachableClasses.iterator();
            while (iterator.hasNext()) {
                set.addAll((Collection<?>)iterator.next().getSubclasses());
            }
            return new ClassInfoList((Set<ClassInfo>)set, filterClassInfo.directlyRelatedClasses, true);
        }
        return new ClassInfoList(filterClassInfo, true);
    }
    
    @Override
    public String toString() {
        return this.toString(false);
    }
    
    ClassInfoList getClassesWithFieldAnnotationDirectOnly() {
        return new ClassInfoList(this.filterClassInfo(RelType.CLASSES_WITH_FIELD_ANNOTATION, !this.isExternalClass, new ClassType[0]), true);
    }
    
    @Override
    public int compareTo(final ClassInfo classInfo) {
        return this.name.compareTo(classInfo.name);
    }
    
    void addFieldInfo(final FieldInfoList fieldInfo, final Map<String, ClassInfo> map) {
        for (final FieldInfo fieldInfo2 : fieldInfo) {
            this.addFieldOrMethodAnnotationInfo(fieldInfo2.annotationInfo, true, fieldInfo2.getModifiers(), map);
        }
        if (this.fieldInfo == null) {
            this.fieldInfo = fieldInfo;
        }
        else {
            this.fieldInfo.addAll(fieldInfo);
        }
    }
    
    void setTypeSignature(final String typeSignatureStr) {
        this.typeSignatureStr = typeSignatureStr;
    }
    
    public ClassInfoList getClassesImplementing() {
        if (!this.isInterface()) {
            throw new IllegalArgumentException("Class is not an interface: " + this.getName());
        }
        final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(RelType.CLASSES_IMPLEMENTING, !this.isExternalClass, new ClassType[0]);
        final LinkedHashSet set = new LinkedHashSet<Object>(filterClassInfo.reachableClasses);
        for (final ClassInfo classInfo : filterClassInfo.reachableClasses) {
            set.addAll((Collection<?>)classInfo.filterClassInfo(RelType.SUBCLASSES, !classInfo.isExternalClass, new ClassType[0]).reachableClasses);
        }
        return new ClassInfoList((Set<ClassInfo>)set, filterClassInfo.directlyRelatedClasses, true);
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }
    
    public String getFullyQualifiedDefiningMethodName() {
        return this.fullyQualifiedDefiningMethodName;
    }
    
    void setClassfileVersion(final int classfileMinorVersion, final int classfileMajorVersion) {
        this.classfileMinorVersion = classfileMinorVersion;
        this.classfileMajorVersion = classfileMajorVersion;
    }
    
    void setModifiers(final int n) {
        this.modifiers |= n;
    }
    
    public boolean hasDeclaredField(final String s) {
        return this.getDeclaredFieldInfo().containsName(s);
    }
    
    public MethodInfoList getDeclaredMethodInfo() {
        return this.getDeclaredMethodInfo(null, true, false, false);
    }
    
    private ClassInfoList getFieldOrMethodAnnotations(final RelType relType) {
        final boolean b = relType == RelType.FIELD_ANNOTATIONS;
        if (b) {
            if (!this.scanResult.scanSpec.enableFieldInfo) {
                throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
            }
        }
        else if (!this.scanResult.scanSpec.enableMethodInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
        }
        if (this.scanResult.scanSpec.enableAnnotationInfo) {
            final ReachableAndDirectlyRelatedClasses filterClassInfo = this.filterClassInfo(relType, false, ClassType.ANNOTATION);
            return new ClassInfoList(new LinkedHashSet<ClassInfo>(filterClassInfo.reachableClasses), filterClassInfo.directlyRelatedClasses, true);
        }
        throw new IllegalArgumentException("Please call ClassGraph#enable" + (b ? "Field" : "Method") + "Info() and #enableAnnotationInfo() before #scan()");
    }
    
    private MethodInfoList getDeclaredMethodInfo(final String s, final boolean b, final boolean b2, final boolean b3) {
        if (!this.scanResult.scanSpec.enableMethodInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableMethodInfo() before #scan()");
        }
        if (this.methodInfo == null) {
            return MethodInfoList.EMPTY_LIST;
        }
        if (s == null) {
            final MethodInfoList list = new MethodInfoList();
            for (final MethodInfo methodInfo : this.methodInfo) {
                final String name = methodInfo.getName();
                final boolean equals = "<init>".equals(name);
                final boolean equals2 = "<clinit>".equals(name);
                if ((equals && b2) || (equals2 && b3) || (!equals && !equals2 && b)) {
                    list.add(methodInfo);
                }
            }
            return list;
        }
        boolean b4 = false;
        final Iterator iterator2 = this.methodInfo.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().getName().equals(s)) {
                b4 = true;
                break;
            }
        }
        if (!b4) {
            return MethodInfoList.EMPTY_LIST;
        }
        final MethodInfoList list2 = new MethodInfoList();
        for (final MethodInfo methodInfo2 : this.methodInfo) {
            if (methodInfo2.getName().equals(s)) {
                list2.add(methodInfo2);
            }
        }
        return list2;
    }
    
    void addImplementedInterface(final String s, final Map<String, ClassInfo> map) {
        final ClassInfo orCreateClassInfo = getOrCreateClassInfo(s, map);
        orCreateClassInfo.setIsInterface(true);
        this.addRelatedClass(RelType.IMPLEMENTED_INTERFACES, orCreateClassInfo);
        orCreateClassInfo.addRelatedClass(RelType.CLASSES_IMPLEMENTING, this);
    }
    
    public boolean extendsSuperclass(final String s) {
        return this.getSuperclasses().containsName(s);
    }
    
    enum RelType
    {
        SUBCLASSES, 
        CLASSES_WITH_METHOD_ANNOTATION, 
        CONTAINS_INNER_CLASS, 
        CLASSES_WITH_NONPRIVATE_METHOD_ANNOTATION, 
        CLASSES_WITH_FIELD_ANNOTATION, 
        CLASSES_WITH_METHOD_PARAMETER_ANNOTATION, 
        METHOD_ANNOTATIONS, 
        FIELD_ANNOTATIONS, 
        IMPLEMENTED_INTERFACES, 
        CONTAINED_WITHIN_OUTER_CLASS, 
        CLASSES_WITH_NONPRIVATE_METHOD_PARAMETER_ANNOTATION, 
        CLASSES_WITH_ANNOTATION, 
        SUPERCLASSES, 
        CLASSES_IMPLEMENTING, 
        CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION, 
        METHOD_PARAMETER_ANNOTATIONS, 
        CLASS_ANNOTATIONS;
    }
    
    private enum ClassType
    {
        IMPLEMENTED_INTERFACE, 
        RECORD, 
        ENUM, 
        STANDARD_CLASS, 
        ANNOTATION, 
        INTERFACE_OR_ANNOTATION, 
        ALL;
    }
    
    static class ReachableAndDirectlyRelatedClasses
    {
        final /* synthetic */ Set<ClassInfo> directlyRelatedClasses;
        final /* synthetic */ Set<ClassInfo> reachableClasses;
        
        private ReachableAndDirectlyRelatedClasses(final Set<ClassInfo> reachableClasses, final Set<ClassInfo> directlyRelatedClasses) {
            this.reachableClasses = reachableClasses;
            this.directlyRelatedClasses = directlyRelatedClasses;
        }
    }
}
