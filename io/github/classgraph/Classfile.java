// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.utils.JarUtils;
import java.util.HashSet;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.Map;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.utils.Join;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.LogNode;
import java.lang.reflect.Modifier;
import java.io.IOException;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Set;

class Classfile
{
    private /* synthetic */ FieldInfoList fieldInfoList;
    private final /* synthetic */ Set<String> acceptedClassNamesFound;
    private /* synthetic */ String superclassName;
    private final /* synthetic */ ScanSpec scanSpec;
    private /* synthetic */ int cpCount;
    private /* synthetic */ AnnotationParameterValueList annotationParamDefaultValues;
    private /* synthetic */ Set<String> refdClassNames;
    private /* synthetic */ String className;
    private final /* synthetic */ boolean isExternalClass;
    private final /* synthetic */ Resource classfileResource;
    private final /* synthetic */ ConcurrentHashMap<String, String> stringInternMap;
    private final /* synthetic */ ClasspathElement classpathElement;
    private /* synthetic */ List<ClassContainment> classContainmentEntries;
    private final /* synthetic */ Set<String> classNamesScheduledForExtendedScanning;
    private /* synthetic */ List<Scanner.ClassfileScanWorkUnit> additionalWorkUnits;
    private /* synthetic */ int[] entryTag;
    private /* synthetic */ int[] entryOffset;
    private /* synthetic */ boolean isInterface;
    private /* synthetic */ int majorVersion;
    private /* synthetic */ boolean isRecord;
    private /* synthetic */ int[] indirectStringRefs;
    private /* synthetic */ AnnotationInfoList classAnnotations;
    private final /* synthetic */ String relativePath;
    private final /* synthetic */ List<ClasspathElement> classpathOrder;
    private static final /* synthetic */ AnnotationInfo[] NO_ANNOTATIONS;
    private /* synthetic */ ClassfileReader reader;
    private /* synthetic */ String fullyQualifiedDefiningMethodName;
    private /* synthetic */ String typeSignature;
    private /* synthetic */ int classModifiers;
    private /* synthetic */ boolean isAnnotation;
    private /* synthetic */ MethodInfoList methodInfoList;
    private /* synthetic */ int minorVersion;
    private /* synthetic */ List<String> implementedInterfaces;
    
    private String getConstantPoolString(final int n, final boolean b, final boolean b2) throws ClassfileFormatException, IOException {
        final int constantPoolStringOffset = this.getConstantPoolStringOffset(n, 0);
        if (constantPoolStringOffset == 0) {
            return null;
        }
        final int unsignedShort = this.reader.readUnsignedShort(constantPoolStringOffset);
        if (unsignedShort == 0) {
            return "";
        }
        return this.intern(this.reader.readString(constantPoolStringOffset + 2L, unsignedShort, b, b2));
    }
    
    private Object getFieldConstantPoolValue(final int i, final char c, final int n) throws IOException, ClassfileFormatException {
        switch (i) {
            case 1:
            case 7:
            case 8: {
                return this.getConstantPoolString(n);
            }
            case 3: {
                final int cpReadInt = this.cpReadInt(n);
                switch (c) {
                    case 'I': {
                        return cpReadInt;
                    }
                    case 'S': {
                        return (short)cpReadInt;
                    }
                    case 'C': {
                        return (char)cpReadInt;
                    }
                    case 'B': {
                        return (byte)cpReadInt;
                    }
                    case 'Z': {
                        return cpReadInt != 0;
                    }
                    default: {
                        throw new ClassfileFormatException("Unknown Constant_INTEGER type " + c + ", cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
                    }
                }
                break;
            }
            case 4: {
                return Float.intBitsToFloat(this.cpReadInt(n));
            }
            case 5: {
                return this.cpReadLong(n);
            }
            case 6: {
                return Double.longBitsToDouble(this.cpReadLong(n));
            }
            default: {
                throw new ClassfileFormatException("Unknown constant pool tag " + i + ", cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
        }
    }
    
    private void readBasicClassInfo() throws ClassfileFormatException, SkipClassException, IOException {
        this.classModifiers = this.reader.readUnsignedShort();
        this.isInterface = ((this.classModifiers & 0x200) != 0x0);
        this.isAnnotation = ((this.classModifiers & 0x2000) != 0x0);
        final String constantPoolString = this.getConstantPoolString(this.reader.readUnsignedShort());
        if (constantPoolString == null) {
            throw new ClassfileFormatException("Class name is null");
        }
        this.className = constantPoolString.replace('/', '.');
        if ("java.lang.Object".equals(this.className)) {
            throw new SkipClassException("No need to scan java.lang.Object");
        }
        final boolean b = (this.classModifiers & 0x8000) != 0x0;
        final boolean regionMatches = this.relativePath.regionMatches(this.relativePath.lastIndexOf(47) + 1, "package-info.class", 0, 18);
        if (!this.scanSpec.ignoreClassVisibility && !Modifier.isPublic(this.classModifiers) && !b && !regionMatches) {
            throw new SkipClassException("Class is not public, and ignoreClassVisibility() was not called");
        }
        if (!this.relativePath.endsWith(".class")) {
            throw new SkipClassException("Classfile filename " + this.relativePath + " does not end in \".class\"");
        }
        final int length = constantPoolString.length();
        if (this.relativePath.length() != length + 6 || !constantPoolString.regionMatches(0, this.relativePath, 0, length)) {
            throw new SkipClassException("Relative path " + this.relativePath + " does not match class name " + this.className);
        }
        final int unsignedShort = this.reader.readUnsignedShort();
        if (unsignedShort > 0) {
            this.superclassName = this.getConstantPoolClassName(unsignedShort);
        }
    }
    
    private void extendScanningUpwards(final LogNode logNode) {
        if (this.superclassName != null) {
            this.scheduleScanningIfExternalClass(this.superclassName, "superclass", logNode);
        }
        if (this.implementedInterfaces != null) {
            final Iterator<String> iterator = this.implementedInterfaces.iterator();
            while (iterator.hasNext()) {
                this.scheduleScanningIfExternalClass(iterator.next(), "interface", logNode);
            }
        }
        if (this.classAnnotations != null) {
            for (final AnnotationInfo annotationInfo : this.classAnnotations) {
                this.scheduleScanningIfExternalClass(annotationInfo.getName(), "class annotation", logNode);
                this.extendScanningUpwardsFromAnnotationParameterValues(annotationInfo, logNode);
            }
        }
        if (this.annotationParamDefaultValues != null) {
            final Iterator iterator3 = this.annotationParamDefaultValues.iterator();
            while (iterator3.hasNext()) {
                this.extendScanningUpwardsFromAnnotationParameterValues(iterator3.next().getValue(), logNode);
            }
        }
        if (this.methodInfoList != null) {
            for (final MethodInfo methodInfo : this.methodInfoList) {
                if (methodInfo.annotationInfo != null) {
                    for (final AnnotationInfo annotationInfo2 : methodInfo.annotationInfo) {
                        this.scheduleScanningIfExternalClass(annotationInfo2.getName(), "method annotation", logNode);
                        this.extendScanningUpwardsFromAnnotationParameterValues(annotationInfo2, logNode);
                    }
                    if (methodInfo.parameterAnnotationInfo == null || methodInfo.parameterAnnotationInfo.length <= 0) {
                        continue;
                    }
                    for (final AnnotationInfo[] array : methodInfo.parameterAnnotationInfo) {
                        if (array != null && array.length > 0) {
                            for (final AnnotationInfo annotationInfo3 : array) {
                                this.scheduleScanningIfExternalClass(annotationInfo3.getName(), "method parameter annotation", logNode);
                                this.extendScanningUpwardsFromAnnotationParameterValues(annotationInfo3, logNode);
                            }
                        }
                    }
                }
            }
        }
        if (this.fieldInfoList != null) {
            for (final FieldInfo fieldInfo : this.fieldInfoList) {
                if (fieldInfo.annotationInfo != null) {
                    for (final AnnotationInfo annotationInfo4 : fieldInfo.annotationInfo) {
                        this.scheduleScanningIfExternalClass(annotationInfo4.getName(), "field annotation", logNode);
                        this.extendScanningUpwardsFromAnnotationParameterValues(annotationInfo4, logNode);
                    }
                }
            }
        }
        if (this.classContainmentEntries != null) {
            for (final ClassContainment classContainment : this.classContainmentEntries) {
                if (classContainment.innerClassName.equals(this.className)) {
                    this.scheduleScanningIfExternalClass(classContainment.outerClassName, "outer class", logNode);
                }
            }
        }
    }
    
    Classfile(final ClasspathElement classpathElement, final List<ClasspathElement> classpathOrder, final Set<String> acceptedClassNamesFound, final Set<String> classNamesScheduledForExtendedScanning, final String relativePath, final Resource classfileResource, final boolean isExternalClass, final ConcurrentHashMap<String, String> stringInternMap, final WorkQueue<Scanner.ClassfileScanWorkUnit> workQueue, final ScanSpec scanSpec, final LogNode logNode) throws SkipClassException, IOException, ClassfileFormatException {
        this.classpathElement = classpathElement;
        this.classpathOrder = classpathOrder;
        this.relativePath = relativePath;
        this.acceptedClassNamesFound = acceptedClassNamesFound;
        this.classNamesScheduledForExtendedScanning = classNamesScheduledForExtendedScanning;
        this.classfileResource = classfileResource;
        this.isExternalClass = isExternalClass;
        this.stringInternMap = stringInternMap;
        this.scanSpec = scanSpec;
        try {
            this.reader = classfileResource.openClassfile();
            if (this.reader.readInt() != -889275714) {
                throw new ClassfileFormatException("Classfile does not have correct magic number");
            }
            this.minorVersion = this.reader.readUnsignedShort();
            this.majorVersion = this.reader.readUnsignedShort();
            this.readConstantPoolEntries();
            this.readBasicClassInfo();
            this.readInterfaces();
            this.readFields();
            this.readMethods();
            this.readClassAttributes();
        }
        finally {
            classfileResource.close();
            this.reader = null;
        }
        final LogNode logNode2 = (logNode == null) ? null : logNode.log("Found " + (this.isAnnotation ? "annotation class" : (this.isInterface ? "interface class" : "class")) + " " + this.className);
        if (logNode2 != null) {
            if (this.superclassName != null) {
                logNode2.log("Super" + ((this.isInterface && !this.isAnnotation) ? "interface" : "class") + ": " + this.superclassName);
            }
            if (this.implementedInterfaces != null) {
                logNode2.log("Interfaces: " + Join.join(", ", this.implementedInterfaces));
            }
            if (this.classAnnotations != null) {
                logNode2.log("Class annotations: " + Join.join(", ", this.classAnnotations));
            }
            if (this.annotationParamDefaultValues != null) {
                final Iterator iterator = this.annotationParamDefaultValues.iterator();
                while (iterator.hasNext()) {
                    logNode2.log("Annotation default param value: " + iterator.next());
                }
            }
            if (this.fieldInfoList != null) {
                final Iterator iterator2 = this.fieldInfoList.iterator();
                while (iterator2.hasNext()) {
                    logNode2.log("Field: " + iterator2.next());
                }
            }
            if (this.methodInfoList != null) {
                final Iterator iterator3 = this.methodInfoList.iterator();
                while (iterator3.hasNext()) {
                    logNode2.log("Method: " + iterator3.next());
                }
            }
            if (this.typeSignature != null) {
                ClassTypeSignature parse = null;
                try {
                    parse = ClassTypeSignature.parse(this.typeSignature, null);
                    if (this.refdClassNames != null) {
                        parse.findReferencedClassNames(this.refdClassNames);
                    }
                }
                catch (ParseException ex) {}
                logNode2.log("Class type signature: " + ((parse == null) ? this.typeSignature : parse.toString(this.className, false, this.classModifiers, this.isAnnotation, this.isInterface)));
            }
            if (this.refdClassNames != null) {
                final ArrayList<Comparable> list = new ArrayList<Comparable>(this.refdClassNames);
                CollectionUtils.sortIfNotEmpty(list);
                logNode2.log("Referenced class names: " + Join.join(", ", list));
            }
        }
        if (scanSpec.extendScanningUpwardsToExternalClasses) {
            this.extendScanningUpwards(logNode2);
            if (this.additionalWorkUnits != null) {
                workQueue.addWorkUnits(this.additionalWorkUnits);
            }
        }
    }
    
    private int getConstantPoolStringOffset(final int i, final int j) throws ClassfileFormatException {
        if (i < 1 || i >= this.cpCount) {
            throw new ClassfileFormatException("Constant pool index " + i + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        final int n = this.entryTag[i];
        if ((n != 12 && j != 0) || (n == 12 && j != 0 && j != 1)) {
            throw new ClassfileFormatException("Bad subfield index " + j + " for tag " + n + ", cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        if (n == 0) {
            return 0;
        }
        int n2;
        if (n == 1) {
            n2 = i;
        }
        else if (n == 7 || n == 8 || n == 19) {
            final int n3 = this.indirectStringRefs[i];
            if (n3 == -1) {
                throw new ClassfileFormatException("Bad string indirection index, cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
            if (n3 == 0) {
                return 0;
            }
            n2 = n3;
        }
        else {
            if (n != 12) {
                throw new ClassfileFormatException("Wrong tag number " + n + " at constant pool index " + i + ", cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
            final int n4 = this.indirectStringRefs[i];
            if (n4 == -1) {
                throw new ClassfileFormatException("Bad string indirection index, cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
            final int n5 = ((j == 0) ? (n4 >> 16) : n4) & 0xFFFF;
            if (n5 == 0) {
                throw new ClassfileFormatException("Bad string indirection index, cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
            n2 = n5;
        }
        if (n2 < 1 || n2 >= this.cpCount) {
            throw new ClassfileFormatException("Constant pool index " + i + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        return this.entryOffset[n2];
    }
    
    private String getConstantPoolClassDescriptor(final int n) throws IOException, ClassfileFormatException {
        return this.getConstantPoolString(n, true, true);
    }
    
    void link(final Map<String, ClassInfo> map, final Map<String, PackageInfo> map2, final Map<String, ModuleInfo> map3) {
        boolean b = false;
        boolean b2 = false;
        ClassInfo addScannedClass = null;
        if (this.className.equals("module-info")) {
            b = true;
        }
        else if (this.className.equals("package-info") || this.className.endsWith(".package-info")) {
            b2 = true;
        }
        else {
            addScannedClass = ClassInfo.addScannedClass(this.className, this.classModifiers, this.isExternalClass, map, this.classpathElement, this.classfileResource);
            addScannedClass.setClassfileVersion(this.minorVersion, this.majorVersion);
            addScannedClass.setModifiers(this.classModifiers);
            addScannedClass.setIsInterface(this.isInterface);
            addScannedClass.setIsAnnotation(this.isAnnotation);
            addScannedClass.setIsRecord(this.isRecord);
            if (this.superclassName != null) {
                addScannedClass.addSuperclass(this.superclassName, map);
            }
            if (this.implementedInterfaces != null) {
                final Iterator<String> iterator = this.implementedInterfaces.iterator();
                while (iterator.hasNext()) {
                    addScannedClass.addImplementedInterface(iterator.next(), map);
                }
            }
            if (this.classAnnotations != null) {
                final Iterator iterator2 = this.classAnnotations.iterator();
                while (iterator2.hasNext()) {
                    addScannedClass.addClassAnnotation(iterator2.next(), map);
                }
            }
            if (this.classContainmentEntries != null) {
                ClassInfo.addClassContainment(this.classContainmentEntries, map);
            }
            if (this.annotationParamDefaultValues != null) {
                addScannedClass.addAnnotationParamDefaultValues(this.annotationParamDefaultValues);
            }
            if (this.fullyQualifiedDefiningMethodName != null) {
                addScannedClass.addFullyQualifiedDefiningMethodName(this.fullyQualifiedDefiningMethodName);
            }
            if (this.fieldInfoList != null) {
                addScannedClass.addFieldInfo(this.fieldInfoList, map);
            }
            if (this.methodInfoList != null) {
                addScannedClass.addMethodInfo(this.methodInfoList, map);
            }
            if (this.typeSignature != null) {
                addScannedClass.setTypeSignature(this.typeSignature);
            }
            if (this.refdClassNames != null) {
                addScannedClass.addReferencedClassNames(this.refdClassNames);
            }
        }
        PackageInfo orCreatePackage = null;
        if (!b) {
            orCreatePackage = PackageInfo.getOrCreatePackage(PackageInfo.getParentPackageName(this.className), map2);
            if (b2) {
                orCreatePackage.addAnnotations(this.classAnnotations);
            }
            else if (addScannedClass != null) {
                orCreatePackage.addClassInfo(addScannedClass);
                addScannedClass.packageInfo = orCreatePackage;
            }
        }
        final String moduleName = this.classpathElement.getModuleName();
        if (moduleName != null) {
            ModuleInfo moduleInfo = map3.get(moduleName);
            if (moduleInfo == null) {
                map3.put(moduleName, moduleInfo = new ModuleInfo(this.classfileResource.getModuleRef(), this.classpathElement));
            }
            if (b) {
                moduleInfo.addAnnotations(this.classAnnotations);
            }
            if (addScannedClass != null) {
                moduleInfo.addClassInfo(addScannedClass);
                addScannedClass.moduleInfo = moduleInfo;
            }
            if (orCreatePackage != null) {
                moduleInfo.addPackageInfo(orCreatePackage);
            }
        }
    }
    
    private AnnotationInfo readAnnotation() throws IOException {
        final String constantPoolClassDescriptor = this.getConstantPoolClassDescriptor(this.reader.readUnsignedShort());
        final int unsignedShort = this.reader.readUnsignedShort();
        AnnotationParameterValueList list = null;
        if (unsignedShort > 0) {
            list = new AnnotationParameterValueList(unsignedShort);
            for (int i = 0; i < unsignedShort; ++i) {
                list.add(new AnnotationParameterValue(this.getConstantPoolString(this.reader.readUnsignedShort()), this.readAnnotationElementValue()));
            }
        }
        return new AnnotationInfo(constantPoolClassDescriptor, list);
    }
    
    private void readInterfaces() throws IOException {
        for (int unsignedShort = this.reader.readUnsignedShort(), i = 0; i < unsignedShort; ++i) {
            final String constantPoolClassName = this.getConstantPoolClassName(this.reader.readUnsignedShort());
            if (this.implementedInterfaces == null) {
                this.implementedInterfaces = new ArrayList<String>();
            }
            this.implementedInterfaces.add(constantPoolClassName);
        }
    }
    
    private String getConstantPoolString(final int n) throws IOException, ClassfileFormatException {
        return this.getConstantPoolString(n, 0);
    }
    
    private String getConstantPoolClassName(final int n) throws ClassfileFormatException, IOException {
        return this.getConstantPoolString(n, true, false);
    }
    
    private boolean constantPoolStringEquals(final int n, final String s) throws IOException, ClassfileFormatException {
        final int constantPoolStringOffset = this.getConstantPoolStringOffset(n, 0);
        if (constantPoolStringOffset == 0) {
            return s == null;
        }
        if (s == null) {
            return false;
        }
        final int unsignedShort = this.reader.readUnsignedShort(constantPoolStringOffset);
        if (unsignedShort != s.length()) {
            return false;
        }
        final int n2 = constantPoolStringOffset + 2;
        this.reader.bufferTo(n2 + unsignedShort);
        final byte[] buf = this.reader.buf();
        for (int i = 0; i < unsignedShort; ++i) {
            if ((char)(buf[n2 + i] & 0xFF) != s.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    private void extendScanningUpwardsFromAnnotationParameterValues(final Object o, final LogNode logNode) {
        if (o != null) {
            if (o instanceof AnnotationInfo) {
                final AnnotationInfo annotationInfo = (AnnotationInfo)o;
                this.scheduleScanningIfExternalClass(annotationInfo.getClassName(), "annotation class", logNode);
                final Iterator iterator = annotationInfo.getParameterValues().iterator();
                while (iterator.hasNext()) {
                    this.extendScanningUpwardsFromAnnotationParameterValues(iterator.next().getValue(), logNode);
                }
            }
            else if (o instanceof AnnotationEnumValue) {
                this.scheduleScanningIfExternalClass(((AnnotationEnumValue)o).getClassName(), "enum class", logNode);
            }
            else if (o instanceof AnnotationClassRef) {
                this.scheduleScanningIfExternalClass(((AnnotationClassRef)o).getClassName(), "class ref", logNode);
            }
            else if (o.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(o); ++i) {
                    this.extendScanningUpwardsFromAnnotationParameterValues(Array.get(o, i), logNode);
                }
            }
        }
    }
    
    private int cpReadUnsignedShort(final int i) throws IOException {
        if (i < 1 || i >= this.cpCount) {
            throw new ClassfileFormatException("Constant pool index " + i + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        return this.reader.readUnsignedShort(this.entryOffset[i]);
    }
    
    private String intern(final String s) {
        if (s == null) {
            return null;
        }
        final String s2 = this.stringInternMap.putIfAbsent(s, s);
        if (s2 != null) {
            return s2;
        }
        return s;
    }
    
    private Object readAnnotationElementValue() throws IOException {
        final char c = (char)this.reader.readUnsignedByte();
        switch (c) {
            case 66: {
                return (byte)this.cpReadInt(this.reader.readUnsignedShort());
            }
            case 67: {
                return (char)this.cpReadInt(this.reader.readUnsignedShort());
            }
            case 68: {
                return Double.longBitsToDouble(this.cpReadLong(this.reader.readUnsignedShort()));
            }
            case 70: {
                return Float.intBitsToFloat(this.cpReadInt(this.reader.readUnsignedShort()));
            }
            case 73: {
                return this.cpReadInt(this.reader.readUnsignedShort());
            }
            case 74: {
                return this.cpReadLong(this.reader.readUnsignedShort());
            }
            case 83: {
                return (short)this.cpReadUnsignedShort(this.reader.readUnsignedShort());
            }
            case 90: {
                return this.cpReadInt(this.reader.readUnsignedShort()) != 0;
            }
            case 115: {
                return this.getConstantPoolString(this.reader.readUnsignedShort());
            }
            case 101: {
                return new AnnotationEnumValue(this.getConstantPoolClassDescriptor(this.reader.readUnsignedShort()), this.getConstantPoolString(this.reader.readUnsignedShort()));
            }
            case 99: {
                return new AnnotationClassRef(this.getConstantPoolString(this.reader.readUnsignedShort()));
            }
            case 64: {
                return this.readAnnotation();
            }
            case 91: {
                final int unsignedShort = this.reader.readUnsignedShort();
                final Object[] array = new Object[unsignedShort];
                for (int i = 0; i < unsignedShort; ++i) {
                    array[i] = this.readAnnotationElementValue();
                }
                return array;
            }
            default: {
                throw new ClassfileFormatException("Class " + this.className + " has unknown annotation element type tag '" + c + "': element size unknown, cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
            }
        }
    }
    
    static {
        NO_ANNOTATIONS = new AnnotationInfo[0];
    }
    
    private String getConstantPoolString(final int n, final int n2) throws ClassfileFormatException, IOException {
        final int constantPoolStringOffset = this.getConstantPoolStringOffset(n, n2);
        if (constantPoolStringOffset == 0) {
            return null;
        }
        final int unsignedShort = this.reader.readUnsignedShort(constantPoolStringOffset);
        if (unsignedShort == 0) {
            return "";
        }
        return this.intern(this.reader.readString(constantPoolStringOffset + 2L, unsignedShort, false, false));
    }
    
    private int cpReadInt(final int i) throws IOException {
        if (i < 1 || i >= this.cpCount) {
            throw new ClassfileFormatException("Constant pool index " + i + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        return this.reader.readInt(this.entryOffset[i]);
    }
    
    private void readConstantPoolEntries() throws IOException {
        List<Integer> list = null;
        List<Integer> list2 = null;
        if (this.scanSpec.enableInterClassDependencies) {
            list = new ArrayList<Integer>();
            list2 = new ArrayList<Integer>();
        }
        this.cpCount = this.reader.readUnsignedShort();
        this.entryOffset = new int[this.cpCount];
        this.entryTag = new int[this.cpCount];
        this.indirectStringRefs = new int[this.cpCount];
        Arrays.fill(this.indirectStringRefs, 0, this.cpCount, -1);
        int i = 1;
        int n = 0;
        while (i < this.cpCount) {
            if (n == 1) {
                n = 0;
            }
            else {
                this.entryTag[i] = this.reader.readUnsignedByte();
                this.entryOffset[i] = this.reader.currPos();
                switch (this.entryTag[i]) {
                    case 0: {
                        throw new ClassfileFormatException("Unknown constant pool tag 0 in classfile " + this.relativePath + " (possible buffer underflow issue). Please report this at https://github.com/classgraph/classgraph/issues");
                    }
                    case 1: {
                        this.reader.skip(this.reader.readUnsignedShort());
                        break;
                    }
                    case 3:
                    case 4: {
                        this.reader.skip(4);
                        break;
                    }
                    case 5:
                    case 6: {
                        this.reader.skip(8);
                        n = 1;
                        break;
                    }
                    case 7: {
                        this.indirectStringRefs[i] = this.reader.readUnsignedShort();
                        if (list != null) {
                            list.add(this.indirectStringRefs[i]);
                            break;
                        }
                        break;
                    }
                    case 8: {
                        this.indirectStringRefs[i] = this.reader.readUnsignedShort();
                        break;
                    }
                    case 9: {
                        this.reader.skip(4);
                        break;
                    }
                    case 10: {
                        this.reader.skip(4);
                        break;
                    }
                    case 11: {
                        this.reader.skip(4);
                        break;
                    }
                    case 12: {
                        final int unsignedShort = this.reader.readUnsignedShort();
                        final int unsignedShort2 = this.reader.readUnsignedShort();
                        if (list2 != null) {
                            list2.add(unsignedShort2);
                        }
                        this.indirectStringRefs[i] = (unsignedShort << 16 | unsignedShort2);
                        break;
                    }
                    case 15: {
                        this.reader.skip(3);
                        break;
                    }
                    case 16: {
                        this.reader.skip(2);
                        break;
                    }
                    case 18: {
                        this.reader.skip(4);
                        break;
                    }
                    case 19: {
                        this.indirectStringRefs[i] = this.reader.readUnsignedShort();
                        break;
                    }
                    case 20: {
                        this.reader.skip(2);
                        break;
                    }
                    default: {
                        throw new ClassfileFormatException("Unknown constant pool tag " + this.entryTag[i] + " (element size unknown, cannot continue reading class). Please report this at https://github.com/classgraph/classgraph/issues");
                    }
                }
            }
            ++i;
        }
        if (list != null) {
            this.refdClassNames = new HashSet<String>();
            final Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                final String constantPoolString = this.getConstantPoolString(iterator.next(), true, false);
                if (constantPoolString != null) {
                    if (constantPoolString.startsWith("[")) {
                        try {
                            TypeSignature.parse(constantPoolString.replace('.', '/'), null).findReferencedClassNames(this.refdClassNames);
                            continue;
                        }
                        catch (ParseException ex) {
                            throw new ClassfileFormatException("Could not parse class name: " + constantPoolString, ex);
                        }
                    }
                    this.refdClassNames.add(constantPoolString);
                }
            }
        }
        if (list2 != null) {
            final Iterator<Integer> iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                final String constantPoolString2 = this.getConstantPoolString(iterator2.next());
                if (constantPoolString2 != null) {
                    try {
                        if (constantPoolString2.indexOf(40) >= 0 || "<init>".equals(constantPoolString2)) {
                            MethodTypeSignature.parse(constantPoolString2, null).findReferencedClassNames(this.refdClassNames);
                        }
                        else {
                            TypeSignature.parse(constantPoolString2, null).findReferencedClassNames(this.refdClassNames);
                        }
                    }
                    catch (ParseException ex2) {
                        throw new ClassfileFormatException("Could not parse type signature: " + constantPoolString2, ex2);
                    }
                }
            }
        }
    }
    
    private void readClassAttributes() throws ClassfileFormatException, IOException {
        for (int unsignedShort = this.reader.readUnsignedShort(), i = 0; i < unsignedShort; ++i) {
            final int unsignedShort2 = this.reader.readUnsignedShort();
            final int int1 = this.reader.readInt();
            if (this.scanSpec.enableAnnotationInfo && (this.constantPoolStringEquals(unsignedShort2, "RuntimeVisibleAnnotations") || (!this.scanSpec.disableRuntimeInvisibleAnnotations && this.constantPoolStringEquals(unsignedShort2, "RuntimeInvisibleAnnotations")))) {
                final int unsignedShort3 = this.reader.readUnsignedShort();
                if (unsignedShort3 > 0) {
                    if (this.classAnnotations == null) {
                        this.classAnnotations = new AnnotationInfoList();
                    }
                    for (int j = 0; j < unsignedShort3; ++j) {
                        this.classAnnotations.add(this.readAnnotation());
                    }
                }
            }
            else if (this.constantPoolStringEquals(unsignedShort2, "Record")) {
                this.isRecord = true;
                this.reader.skip(int1);
            }
            else if (this.constantPoolStringEquals(unsignedShort2, "InnerClasses")) {
                for (int unsignedShort4 = this.reader.readUnsignedShort(), k = 0; k < unsignedShort4; ++k) {
                    final int unsignedShort5 = this.reader.readUnsignedShort();
                    final int unsignedShort6 = this.reader.readUnsignedShort();
                    this.reader.skip(2);
                    final int unsignedShort7 = this.reader.readUnsignedShort();
                    if (unsignedShort5 != 0 && unsignedShort6 != 0) {
                        final String constantPoolClassName = this.getConstantPoolClassName(unsignedShort5);
                        final String constantPoolClassName2 = this.getConstantPoolClassName(unsignedShort6);
                        if (constantPoolClassName == null || constantPoolClassName2 == null) {
                            throw new ClassfileFormatException("Inner and/or outer class name is null");
                        }
                        if (constantPoolClassName.equals(constantPoolClassName2)) {
                            throw new ClassfileFormatException("Inner and outer class name cannot be the same");
                        }
                        if (!"java.lang.invoke.MethodHandles$Lookup".equals(constantPoolClassName) || !"java.lang.invoke.MethodHandles".equals(constantPoolClassName2)) {
                            if (this.classContainmentEntries == null) {
                                this.classContainmentEntries = new ArrayList<ClassContainment>();
                            }
                            this.classContainmentEntries.add(new ClassContainment(constantPoolClassName, unsignedShort7, constantPoolClassName2));
                        }
                    }
                }
            }
            else if (this.constantPoolStringEquals(unsignedShort2, "Signature")) {
                this.typeSignature = this.getConstantPoolString(this.reader.readUnsignedShort());
            }
            else if (this.constantPoolStringEquals(unsignedShort2, "EnclosingMethod")) {
                final String constantPoolClassName3 = this.getConstantPoolClassName(this.reader.readUnsignedShort());
                final int unsignedShort8 = this.reader.readUnsignedShort();
                String constantPoolString;
                if (unsignedShort8 == 0) {
                    constantPoolString = "<clinit>";
                }
                else {
                    constantPoolString = this.getConstantPoolString(unsignedShort8, 0);
                }
                if (this.classContainmentEntries == null) {
                    this.classContainmentEntries = new ArrayList<ClassContainment>();
                }
                this.classContainmentEntries.add(new ClassContainment(this.className, this.classModifiers, constantPoolClassName3));
                this.fullyQualifiedDefiningMethodName = constantPoolClassName3 + "." + constantPoolString;
            }
            else if (this.constantPoolStringEquals(unsignedShort2, "Module")) {
                this.classpathElement.moduleNameFromModuleDescriptor = this.getConstantPoolString(this.reader.readUnsignedShort());
                this.reader.skip(int1 - 2);
            }
            else {
                this.reader.skip(int1);
            }
        }
    }
    
    private byte getConstantPoolStringFirstByte(final int n) throws IOException, ClassfileFormatException {
        final int constantPoolStringOffset = this.getConstantPoolStringOffset(n, 0);
        if (constantPoolStringOffset == 0) {
            return 0;
        }
        if (this.reader.readUnsignedShort(constantPoolStringOffset) == 0) {
            return 0;
        }
        return this.reader.readByte(constantPoolStringOffset + 2L);
    }
    
    private void readMethods() throws ClassfileFormatException, IOException {
        for (int unsignedShort = this.reader.readUnsignedShort(), i = 0; i < unsignedShort; ++i) {
            final int unsignedShort2 = this.reader.readUnsignedShort();
            final boolean b = (unsignedShort2 & 0x1) == 0x1 || this.scanSpec.ignoreMethodVisibility;
            String constantPoolString = null;
            String constantPoolString2 = null;
            String constantPoolString3 = null;
            final boolean b2 = this.scanSpec.enableMethodInfo || this.isAnnotation;
            if (b2 || this.isAnnotation) {
                constantPoolString = this.getConstantPoolString(this.reader.readUnsignedShort());
                constantPoolString2 = this.getConstantPoolString(this.reader.readUnsignedShort());
            }
            else {
                this.reader.skip(4);
            }
            final int unsignedShort3 = this.reader.readUnsignedShort();
            String[] array = null;
            int[] array2 = null;
            AnnotationInfo[][] array3 = null;
            AnnotationInfoList list = null;
            boolean b3 = false;
            if (!b || (!b2 && !this.isAnnotation)) {
                for (int j = 0; j < unsignedShort3; ++j) {
                    this.reader.skip(2);
                    this.reader.skip(this.reader.readInt());
                }
            }
            else {
                for (int k = 0; k < unsignedShort3; ++k) {
                    final int unsignedShort4 = this.reader.readUnsignedShort();
                    final int int1 = this.reader.readInt();
                    if (this.scanSpec.enableAnnotationInfo && (this.constantPoolStringEquals(unsignedShort4, "RuntimeVisibleAnnotations") || (!this.scanSpec.disableRuntimeInvisibleAnnotations && this.constantPoolStringEquals(unsignedShort4, "RuntimeInvisibleAnnotations")))) {
                        final int unsignedShort5 = this.reader.readUnsignedShort();
                        if (unsignedShort5 > 0) {
                            if (list == null) {
                                list = new AnnotationInfoList(1);
                            }
                            for (int l = 0; l < unsignedShort5; ++l) {
                                list.add(this.readAnnotation());
                            }
                        }
                    }
                    else if (this.scanSpec.enableAnnotationInfo && (this.constantPoolStringEquals(unsignedShort4, "RuntimeVisibleParameterAnnotations") || (!this.scanSpec.disableRuntimeInvisibleAnnotations && this.constantPoolStringEquals(unsignedShort4, "RuntimeInvisibleParameterAnnotations")))) {
                        final int unsignedByte = this.reader.readUnsignedByte();
                        if (array3 == null) {
                            array3 = new AnnotationInfo[unsignedByte][];
                        }
                        else if (array3.length != unsignedByte) {
                            throw new ClassfileFormatException("Mismatch in number of parameters between RuntimeVisibleParameterAnnotations and RuntimeInvisibleParameterAnnotations");
                        }
                        for (int n = 0; n < unsignedByte; ++n) {
                            final int unsignedShort6 = this.reader.readUnsignedShort();
                            if (unsignedShort6 > 0) {
                                int length = 0;
                                if (array3[n] != null) {
                                    length = array3[n].length;
                                    array3[n] = Arrays.copyOf(array3[n], length + unsignedShort6);
                                }
                                else {
                                    array3[n] = new AnnotationInfo[unsignedShort6];
                                }
                                for (int n2 = 0; n2 < unsignedShort6; ++n2) {
                                    array3[n][length + n2] = this.readAnnotation();
                                }
                            }
                            else if (array3[n] == null) {
                                array3[n] = Classfile.NO_ANNOTATIONS;
                            }
                        }
                    }
                    else if (this.constantPoolStringEquals(unsignedShort4, "MethodParameters")) {
                        final int unsignedByte2 = this.reader.readUnsignedByte();
                        array = new String[unsignedByte2];
                        array2 = new int[unsignedByte2];
                        for (int n3 = 0; n3 < unsignedByte2; ++n3) {
                            final int unsignedShort7 = this.reader.readUnsignedShort();
                            array[n3] = ((unsignedShort7 == 0) ? null : this.getConstantPoolString(unsignedShort7));
                            array2[n3] = this.reader.readUnsignedShort();
                        }
                    }
                    else if (this.constantPoolStringEquals(unsignedShort4, "Signature")) {
                        constantPoolString3 = this.getConstantPoolString(this.reader.readUnsignedShort());
                    }
                    else if (this.constantPoolStringEquals(unsignedShort4, "AnnotationDefault")) {
                        if (this.annotationParamDefaultValues == null) {
                            this.annotationParamDefaultValues = new AnnotationParameterValueList();
                        }
                        this.annotationParamDefaultValues.add(new AnnotationParameterValue(constantPoolString, this.readAnnotationElementValue()));
                    }
                    else if (this.constantPoolStringEquals(unsignedShort4, "Code")) {
                        b3 = true;
                        this.reader.skip(int1);
                    }
                    else {
                        this.reader.skip(int1);
                    }
                }
                if (b2) {
                    if (this.methodInfoList == null) {
                        this.methodInfoList = new MethodInfoList();
                    }
                    this.methodInfoList.add(new MethodInfo(this.className, constantPoolString, list, unsignedShort2, constantPoolString2, constantPoolString3, array, array2, array3, b3));
                }
            }
        }
    }
    
    private void scheduleScanningIfExternalClass(final String str, final String str2, final LogNode logNode) {
        if (str != null && !str.equals("java.lang.Object") && !this.acceptedClassNamesFound.contains(str) && this.classNamesScheduledForExtendedScanning.add(str)) {
            if (this.scanSpec.classAcceptReject.isRejected(str)) {
                if (logNode != null) {
                    logNode.log("Cannot extend scanning upwards to external " + str2 + " " + str + ", since it is rejected");
                }
            }
            else {
                final String classNameToClassfilePath = JarUtils.classNameToClassfilePath(str);
                Resource resource = this.classpathElement.getResource(classNameToClassfilePath);
                ClasspathElement classpathElement = null;
                if (resource != null) {
                    classpathElement = this.classpathElement;
                }
                else {
                    for (final ClasspathElement classpathElement2 : this.classpathOrder) {
                        if (classpathElement2 != this.classpathElement) {
                            resource = classpathElement2.getResource(classNameToClassfilePath);
                            if (resource != null) {
                                classpathElement = classpathElement2;
                                break;
                            }
                            continue;
                        }
                    }
                }
                if (resource != null) {
                    if (logNode != null) {
                        resource.scanLog = logNode.log("Extending scanning to external " + str2 + ((classpathElement == this.classpathElement) ? " in same classpath element" : (" in classpath element " + classpathElement)) + ": " + str);
                    }
                    if (this.additionalWorkUnits == null) {
                        this.additionalWorkUnits = new ArrayList<Scanner.ClassfileScanWorkUnit>();
                    }
                    this.additionalWorkUnits.add(new Scanner.ClassfileScanWorkUnit(classpathElement, resource, true));
                }
                else if (logNode != null) {
                    logNode.log("External " + str2 + " " + str + " was not found in non-rejected packages -- cannot extend scanning to this class");
                }
            }
        }
    }
    
    private void readFields() throws ClassfileFormatException, IOException {
        for (int unsignedShort = this.reader.readUnsignedShort(), i = 0; i < unsignedShort; ++i) {
            final int unsignedShort2 = this.reader.readUnsignedShort();
            final boolean b = (unsignedShort2 & 0x1) == 0x1 || this.scanSpec.ignoreFieldVisibility;
            final boolean b2 = this.scanSpec.enableStaticFinalFieldConstantInitializerValues && b;
            if (!b || (!this.scanSpec.enableFieldInfo && !b2)) {
                this.reader.readUnsignedShort();
                this.reader.readUnsignedShort();
                for (int unsignedShort3 = this.reader.readUnsignedShort(), j = 0; j < unsignedShort3; ++j) {
                    this.reader.readUnsignedShort();
                    this.reader.skip(this.reader.readInt());
                }
            }
            else {
                final String constantPoolString = this.getConstantPoolString(this.reader.readUnsignedShort());
                final int unsignedShort4 = this.reader.readUnsignedShort();
                final char c = (char)this.getConstantPoolStringFirstByte(unsignedShort4);
                String constantPoolString2 = null;
                final String constantPoolString3 = this.getConstantPoolString(unsignedShort4);
                Object fieldConstantPoolValue = null;
                AnnotationInfoList list = null;
                for (int unsignedShort5 = this.reader.readUnsignedShort(), k = 0; k < unsignedShort5; ++k) {
                    final int unsignedShort6 = this.reader.readUnsignedShort();
                    final int int1 = this.reader.readInt();
                    if (b2 && this.constantPoolStringEquals(unsignedShort6, "ConstantValue")) {
                        final int unsignedShort7 = this.reader.readUnsignedShort();
                        if (unsignedShort7 < 1 || unsignedShort7 >= this.cpCount) {
                            throw new ClassfileFormatException("Constant pool index " + unsignedShort7 + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
                        }
                        fieldConstantPoolValue = this.getFieldConstantPoolValue(this.entryTag[unsignedShort7], c, unsignedShort7);
                    }
                    else if (b && this.constantPoolStringEquals(unsignedShort6, "Signature")) {
                        constantPoolString2 = this.getConstantPoolString(this.reader.readUnsignedShort());
                    }
                    else if (this.scanSpec.enableAnnotationInfo && (this.constantPoolStringEquals(unsignedShort6, "RuntimeVisibleAnnotations") || (!this.scanSpec.disableRuntimeInvisibleAnnotations && this.constantPoolStringEquals(unsignedShort6, "RuntimeInvisibleAnnotations")))) {
                        final int unsignedShort8 = this.reader.readUnsignedShort();
                        if (unsignedShort8 > 0) {
                            if (list == null) {
                                list = new AnnotationInfoList(1);
                            }
                            for (int l = 0; l < unsignedShort8; ++l) {
                                list.add(this.readAnnotation());
                            }
                        }
                    }
                    else {
                        this.reader.skip(int1);
                    }
                }
                if (this.scanSpec.enableFieldInfo && b) {
                    if (this.fieldInfoList == null) {
                        this.fieldInfoList = new FieldInfoList();
                    }
                    this.fieldInfoList.add(new FieldInfo(this.className, constantPoolString, unsignedShort2, constantPoolString3, constantPoolString2, fieldConstantPoolValue, list));
                }
            }
        }
    }
    
    private long cpReadLong(final int i) throws IOException {
        if (i < 1 || i >= this.cpCount) {
            throw new ClassfileFormatException("Constant pool index " + i + ", should be in range [1, " + (this.cpCount - 1) + "] -- cannot continue reading class. Please report this at https://github.com/classgraph/classgraph/issues");
        }
        return this.reader.readLong(this.entryOffset[i]);
    }
    
    static class ClassfileFormatException extends IOException
    {
        public ClassfileFormatException(final String message) {
            super(message);
        }
        
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
        
        public ClassfileFormatException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
    
    static class SkipClassException extends IOException
    {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
        
        public SkipClassException(final String message) {
            super(message);
        }
    }
    
    static class ClassContainment
    {
        public final /* synthetic */ int innerClassModifierBits;
        public final /* synthetic */ String outerClassName;
        public final /* synthetic */ String innerClassName;
        
        public ClassContainment(final String innerClassName, final int innerClassModifierBits, final String outerClassName) {
            this.innerClassName = innerClassName;
            this.innerClassModifierBits = innerClassModifierBits;
            this.outerClassName = outerClassName;
        }
    }
}
