// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib;

public class ClassWriter extends ClassVisitor
{
    private /* synthetic */ int name;
    /* synthetic */ int index;
    final /* synthetic */ ByteVector pool;
    private /* synthetic */ AnnotationWriter anns;
    private /* synthetic */ int signature;
    private /* synthetic */ int interfaceCount;
    /* synthetic */ MethodWriter firstMethod;
    /* synthetic */ Item[] typeTable;
    private /* synthetic */ int superName;
    private /* synthetic */ ByteVector sourceDebug;
    /* synthetic */ FieldWriter firstField;
    private /* synthetic */ short typeCount;
    /* synthetic */ FieldWriter lastField;
    /* synthetic */ String thisName;
    private /* synthetic */ int innerClassesCount;
    private /* synthetic */ int access;
    final /* synthetic */ Item key2;
    /* synthetic */ boolean hasAsmInsns;
    /* synthetic */ ClassReader cr;
    private /* synthetic */ int enclosingMethod;
    final /* synthetic */ Item key3;
    private /* synthetic */ ByteVector innerClasses;
    /* synthetic */ int bootstrapMethodsCount;
    private /* synthetic */ AnnotationWriter tanns;
    private /* synthetic */ Attribute attrs;
    /* synthetic */ ByteVector bootstrapMethods;
    /* synthetic */ int version;
    private /* synthetic */ int sourceFile;
    private /* synthetic */ int compute;
    private /* synthetic */ int enclosingMethodOwner;
    /* synthetic */ Item[] items;
    /* synthetic */ MethodWriter lastMethod;
    private /* synthetic */ int[] interfaces;
    final /* synthetic */ Item key4;
    /* synthetic */ int threshold;
    private /* synthetic */ AnnotationWriter itanns;
    private /* synthetic */ AnnotationWriter ianns;
    final /* synthetic */ Item key;
    
    public int newMethod(final String s, final String s2, final String s3, final boolean b) {
        return this.newMethodItem(s, s2, s3, b).index;
    }
    
    @Override
    public final AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (b) {
            annotationWriter.next = this.anns;
            this.anns = annotationWriter;
        }
        else {
            annotationWriter.next = this.ianns;
            this.ianns = annotationWriter;
        }
        return annotationWriter;
    }
    
    Item newInteger(final int n) {
        this.key.set(n);
        Item value = this.get(this.key);
        if (value == null) {
            this.pool.putByte(3).putInt(n);
            value = new Item(this.index++, this.key);
            this.put(value);
        }
        return value;
    }
    
    Item newNameTypeItem(final String s, final String s2) {
        this.key2.set(12, s, s2, null);
        Item value = this.get(this.key2);
        if (value == null) {
            this.put122(12, this.newUTF8(s), this.newUTF8(s2));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    protected String getCommonSuperClass(final String s, final String s2) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        Class<?> clazz;
        Class<?> forName;
        try {
            clazz = Class.forName(s.replace('/', '.'), false, classLoader);
            forName = Class.forName(s2.replace('/', '.'), false, classLoader);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
        if (clazz.isAssignableFrom(forName)) {
            return s;
        }
        if (forName.isAssignableFrom(clazz)) {
            return s2;
        }
        if (clazz.isInterface() || forName.isInterface()) {
            return "java/lang/Object";
        }
        do {
            clazz = clazz.getSuperclass();
        } while (!clazz.isAssignableFrom(forName));
        return clazz.getName().replace('.', '/');
    }
    
    @Override
    public final void visitAttribute(final Attribute attrs) {
        attrs.next = this.attrs;
        this.attrs = attrs;
    }
    
    int getMergedType(final int n, final int n2) {
        this.key2.type = 32;
        this.key2.longVal = ((long)n | (long)n2 << 32);
        this.key2.hashCode = (Integer.MAX_VALUE & 32 + n + n2);
        Item value = this.get(this.key2);
        if (value == null) {
            this.key2.intVal = this.addType(this.getCommonSuperClass(this.typeTable[n].strVal1, this.typeTable[n2].strVal1));
            value = new Item(0, this.key2);
            this.put(value);
        }
        return value.intVal;
    }
    
    public int newUTF8(final String s) {
        this.key.set(1, s, null, null);
        Item value = this.get(this.key);
        if (value == null) {
            this.pool.putByte(1).putUTF8(s);
            value = new Item(this.index++, this.key);
            this.put(value);
        }
        return value.index;
    }
    
    Item newHandleItem(final int n, final String s, final String s2, final String s3, final boolean b) {
        this.key4.set(20 + n, s, s2, s3);
        Item value = this.get(this.key4);
        if (value == null) {
            if (n <= 4) {
                this.put112(15, n, this.newField(s, s2, s3));
            }
            else {
                this.put112(15, n, this.newMethod(s, s2, s3, b));
            }
            value = new Item(this.index++, this.key4);
            this.put(value);
        }
        return value;
    }
    
    Item newFieldItem(final String s, final String s2, final String s3) {
        this.key3.set(9, s, s2, s3);
        Item value = this.get(this.key3);
        if (value == null) {
            this.put122(9, this.newClass(s), this.newNameType(s2, s3));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    @Override
    public final FieldVisitor visitField(final int n, final String s, final String s2, final String s3, final Object o) {
        return new FieldWriter(this, n, s, s2, s3, o);
    }
    
    @Override
    public final MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        return new MethodWriter(this, n, s, s2, s3, array, this.compute);
    }
    
    static {
        LABELW_INSN = 10;
        HANDLE = 15;
        MANA_INSN = 16;
        FIELD = 9;
        IINC_INSN = 13;
        IMETH = 11;
        CLASS = 7;
        LDC_INSN = 11;
        COMPUTE_FRAMES = 2;
        IMPLVAR_INSN = 4;
        VAR_INSN = 3;
        TABL_INSN = 14;
        SBYTE_INSN = 1;
        UTF8 = 1;
        BSM = 33;
        TYPE_MERGED = 32;
        ACC_SYNTHETIC_ATTRIBUTE = 262144;
        HANDLE_BASE = 20;
        SHORT_INSN = 2;
        WIDE_INSN = 17;
        TYPE_INSN = 5;
        ASM_LABEL_INSN = 18;
        LONG = 5;
        MTYPE = 16;
        LABEL_INSN = 9;
        FLOAT = 4;
        INDYMETH_INSN = 8;
        TYPE_NORMAL = 30;
        FIELDORMETH_INSN = 6;
        INDY = 18;
        NOARG_INSN = 0;
        ITFMETH_INSN = 7;
        NAME_TYPE = 12;
        LOOK_INSN = 15;
        COMPUTE_MAXS = 1;
        TO_ACC_SYNTHETIC = 64;
        LDCW_INSN = 12;
        METH = 10;
        STR = 8;
        INT = 3;
        F_INSERT = 256;
        DOUBLE = 6;
        TYPE_UNINIT = 31;
        final byte[] type = new byte[220];
        final String s = "AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKSSSSSSSSSSSSSSSSSS";
        for (int i = 0; i < type.length; ++i) {
            type[i] = (byte)(s.charAt(i) - 'A');
        }
        TYPE = type;
    }
    
    Item newLong(final long n) {
        this.key.set(n);
        Item value = this.get(this.key);
        if (value == null) {
            this.pool.putByte(5).putLong(n);
            value = new Item(this.index, this.key);
            this.index += 2;
            this.put(value);
        }
        return value;
    }
    
    @Override
    public final void visit(final int version, final int access, final String thisName, final String s, final String s2, final String[] array) {
        this.version = version;
        this.access = access;
        this.name = this.newClass(thisName);
        this.thisName = thisName;
        if (s != null) {
            this.signature = this.newUTF8(s);
        }
        this.superName = ((s2 == null) ? 0 : this.newClass(s2));
        if (array != null && array.length > 0) {
            this.interfaceCount = array.length;
            this.interfaces = new int[this.interfaceCount];
            for (int i = 0; i < this.interfaceCount; ++i) {
                this.interfaces[i] = this.newClass(array[i]);
            }
        }
    }
    
    int addType(final String s) {
        this.key.set(30, s, null, null);
        Item item = this.get(this.key);
        if (item == null) {
            item = this.addType(this.key);
        }
        return item.index;
    }
    
    public int newField(final String s, final String s2, final String s3) {
        return this.newFieldItem(s, s2, s3).index;
    }
    
    int addUninitializedType(final String strVal1, final int intVal) {
        this.key.type = 31;
        this.key.intVal = intVal;
        this.key.strVal1 = strVal1;
        this.key.hashCode = (Integer.MAX_VALUE & 31 + strVal1.hashCode() + intVal);
        Item item = this.get(this.key);
        if (item == null) {
            item = this.addType(this.key);
        }
        return item.index;
    }
    
    private Item newString(final String s) {
        this.key2.set(8, s, null, null);
        Item value = this.get(this.key2);
        if (value == null) {
            this.pool.put12(8, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    public int newInvokeDynamic(final String s, final String s2, final Handle handle, final Object... array) {
        return this.newInvokeDynamicItem(s, s2, handle, array).index;
    }
    
    Item newMethodTypeItem(final String s) {
        this.key2.set(16, s, null, null);
        Item value = this.get(this.key2);
        if (value == null) {
            this.pool.put12(16, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    Item newClassItem(final String s) {
        this.key2.set(7, s, null, null);
        Item value = this.get(this.key2);
        if (value == null) {
            this.pool.put12(7, this.newUTF8(s));
            value = new Item(this.index++, this.key2);
            this.put(value);
        }
        return value;
    }
    
    @Override
    public final void visitEnd() {
    }
    
    public byte[] toByteArray() {
        if (this.index > 65535) {
            throw new RuntimeException("Class file too large!");
        }
        int n = 24 + 2 * this.interfaceCount;
        int n2 = 0;
        for (FieldWriter firstField = this.firstField; firstField != null; firstField = (FieldWriter)firstField.fv) {
            ++n2;
            n += firstField.getSize();
        }
        int n3 = 0;
        for (MethodWriter firstMethod = this.firstMethod; firstMethod != null; firstMethod = (MethodWriter)firstMethod.mv) {
            ++n3;
            n += firstMethod.getSize();
        }
        int n4 = 0;
        if (this.bootstrapMethods != null) {
            ++n4;
            n += 8 + this.bootstrapMethods.length;
            this.newUTF8("BootstrapMethods");
        }
        if (this.signature != 0) {
            ++n4;
            n += 8;
            this.newUTF8("Signature");
        }
        if (this.sourceFile != 0) {
            ++n4;
            n += 8;
            this.newUTF8("SourceFile");
        }
        if (this.sourceDebug != null) {
            ++n4;
            n += this.sourceDebug.length + 6;
            this.newUTF8("SourceDebugExtension");
        }
        if (this.enclosingMethodOwner != 0) {
            ++n4;
            n += 10;
            this.newUTF8("EnclosingMethod");
        }
        if ((this.access & 0x20000) != 0x0) {
            ++n4;
            n += 6;
            this.newUTF8("Deprecated");
        }
        if ((this.access & 0x1000) != 0x0 && ((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            ++n4;
            n += 6;
            this.newUTF8("Synthetic");
        }
        if (this.innerClasses != null) {
            ++n4;
            n += 8 + this.innerClasses.length;
            this.newUTF8("InnerClasses");
        }
        if (this.anns != null) {
            ++n4;
            n += 8 + this.anns.getSize();
            this.newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.ianns != null) {
            ++n4;
            n += 8 + this.ianns.getSize();
            this.newUTF8("RuntimeInvisibleAnnotations");
        }
        if (this.tanns != null) {
            ++n4;
            n += 8 + this.tanns.getSize();
            this.newUTF8("RuntimeVisibleTypeAnnotations");
        }
        if (this.itanns != null) {
            ++n4;
            n += 8 + this.itanns.getSize();
            this.newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        if (this.attrs != null) {
            n4 += this.attrs.getCount();
            n += this.attrs.getSize(this, null, 0, -1, -1);
        }
        final ByteVector byteVector = new ByteVector(n + this.pool.length);
        byteVector.putInt(-889275714).putInt(this.version);
        byteVector.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
        byteVector.putShort(this.access & ~(0x60000 | (this.access & 0x40000) / 64)).putShort(this.name).putShort(this.superName);
        byteVector.putShort(this.interfaceCount);
        for (int i = 0; i < this.interfaceCount; ++i) {
            byteVector.putShort(this.interfaces[i]);
        }
        byteVector.putShort(n2);
        for (FieldWriter firstField2 = this.firstField; firstField2 != null; firstField2 = (FieldWriter)firstField2.fv) {
            firstField2.put(byteVector);
        }
        byteVector.putShort(n3);
        for (MethodWriter firstMethod2 = this.firstMethod; firstMethod2 != null; firstMethod2 = (MethodWriter)firstMethod2.mv) {
            firstMethod2.put(byteVector);
        }
        byteVector.putShort(n4);
        if (this.bootstrapMethods != null) {
            byteVector.putShort(this.newUTF8("BootstrapMethods"));
            byteVector.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
            byteVector.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
        }
        if (this.signature != 0) {
            byteVector.putShort(this.newUTF8("Signature")).putInt(2).putShort(this.signature);
        }
        if (this.sourceFile != 0) {
            byteVector.putShort(this.newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
        }
        if (this.sourceDebug != null) {
            final int length = this.sourceDebug.length;
            byteVector.putShort(this.newUTF8("SourceDebugExtension")).putInt(length);
            byteVector.putByteArray(this.sourceDebug.data, 0, length);
        }
        if (this.enclosingMethodOwner != 0) {
            byteVector.putShort(this.newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
        }
        if ((this.access & 0x20000) != 0x0) {
            byteVector.putShort(this.newUTF8("Deprecated")).putInt(0);
        }
        if ((this.access & 0x1000) != 0x0 && ((this.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            byteVector.putShort(this.newUTF8("Synthetic")).putInt(0);
        }
        if (this.innerClasses != null) {
            byteVector.putShort(this.newUTF8("InnerClasses"));
            byteVector.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
            byteVector.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        if (this.anns != null) {
            byteVector.putShort(this.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(this.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(this.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(this.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this, null, 0, -1, -1, byteVector);
        }
        if (this.hasAsmInsns) {
            this.anns = null;
            this.ianns = null;
            this.attrs = null;
            this.innerClassesCount = 0;
            this.innerClasses = null;
            this.firstField = null;
            this.lastField = null;
            this.firstMethod = null;
            this.lastMethod = null;
            this.compute = 1;
            this.hasAsmInsns = false;
            new ClassReader(byteVector.data).accept(this, 264);
            return this.toByteArray();
        }
        return byteVector.data;
    }
    
    private void put122(final int n, final int n2, final int n3) {
        this.pool.put12(n, n2).putShort(n3);
    }
    
    private Item get(final Item item) {
        Item next;
        for (next = this.items[item.hashCode % this.items.length]; next != null && (next.type != item.type || !item.isEqualTo(next)); next = next.next) {}
        return next;
    }
    
    public int newHandle(final int n, final String s, final String s2, final String s3, final boolean b) {
        return this.newHandleItem(n, s, s2, s3, b).index;
    }
    
    private Item addType(final Item item) {
        ++this.typeCount;
        final Item item2 = new Item(this.typeCount, this.key);
        this.put(item2);
        if (this.typeTable == null) {
            this.typeTable = new Item[16];
        }
        if (this.typeCount == this.typeTable.length) {
            final Item[] typeTable = new Item[2 * this.typeTable.length];
            System.arraycopy(this.typeTable, 0, typeTable, 0, this.typeTable.length);
            this.typeTable = typeTable;
        }
        return this.typeTable[this.typeCount] = item2;
    }
    
    Item newConstItem(final Object obj) {
        if (obj instanceof Integer) {
            return this.newInteger((int)obj);
        }
        if (obj instanceof Byte) {
            return this.newInteger((int)obj);
        }
        if (obj instanceof Character) {
            return this.newInteger((char)obj);
        }
        if (obj instanceof Short) {
            return this.newInteger((int)obj);
        }
        if (obj instanceof Boolean) {
            return this.newInteger(((boolean)obj) ? 1 : 0);
        }
        if (obj instanceof Float) {
            return this.newFloat((float)obj);
        }
        if (obj instanceof Long) {
            return this.newLong((long)obj);
        }
        if (obj instanceof Double) {
            return this.newDouble((double)obj);
        }
        if (obj instanceof String) {
            return this.newString((String)obj);
        }
        if (obj instanceof Type) {
            final Type type = (Type)obj;
            final int sort = type.getSort();
            if (sort == 10) {
                return this.newClassItem(type.getInternalName());
            }
            if (sort == 11) {
                return this.newMethodTypeItem(type.getDescriptor());
            }
            return this.newClassItem(type.getDescriptor());
        }
        else {
            if (obj instanceof Handle) {
                final Handle handle = (Handle)obj;
                return this.newHandleItem(handle.tag, handle.owner, handle.name, handle.desc, handle.itf);
            }
            throw new IllegalArgumentException("value " + obj);
        }
    }
    
    private void put112(final int n, final int n2, final int n3) {
        this.pool.put11(n, n2).putShort(n3);
    }
    
    @Override
    public final void visitOuterClass(final String s, final String s2, final String s3) {
        this.enclosingMethodOwner = this.newClass(s);
        if (s2 != null && s3 != null) {
            this.enclosingMethod = this.newNameType(s2, s3);
        }
    }
    
    public ClassWriter(final ClassReader cr, final int n) {
        this(n);
        cr.copyPool(this);
        this.cr = cr;
    }
    
    public int newConst(final Object o) {
        return this.newConstItem(o).index;
    }
    
    public int newMethodType(final String s) {
        return this.newMethodTypeItem(s).index;
    }
    
    @Override
    public final AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(n, typePath, byteVector);
        byteVector.putShort(this.newUTF8(s)).putShort(0);
        final AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, byteVector.length - 2);
        if (b) {
            annotationWriter.next = this.tanns;
            this.tanns = annotationWriter;
        }
        else {
            annotationWriter.next = this.itanns;
            this.itanns = annotationWriter;
        }
        return annotationWriter;
    }
    
    Item newFloat(final float n) {
        this.key.set(n);
        Item value = this.get(this.key);
        if (value == null) {
            this.pool.putByte(4).putInt(this.key.intVal);
            value = new Item(this.index++, this.key);
            this.put(value);
        }
        return value;
    }
    
    @Deprecated
    public int newHandle(final int n, final String s, final String s2, final String s3) {
        return this.newHandle(n, s, s2, s3, n == 9);
    }
    
    public int newNameType(final String s, final String s2) {
        return this.newNameTypeItem(s, s2).index;
    }
    
    @Override
    public final void visitInnerClass(final String s, final String s2, final String s3, final int n) {
        if (this.innerClasses == null) {
            this.innerClasses = new ByteVector();
        }
        final Item classItem = this.newClassItem(s);
        if (classItem.intVal == 0) {
            ++this.innerClassesCount;
            this.innerClasses.putShort(classItem.index);
            this.innerClasses.putShort((s2 == null) ? 0 : this.newClass(s2));
            this.innerClasses.putShort((s3 == null) ? 0 : this.newUTF8(s3));
            this.innerClasses.putShort(n);
            classItem.intVal = this.innerClassesCount;
        }
    }
    
    Item newDouble(final double n) {
        this.key.set(n);
        Item value = this.get(this.key);
        if (value == null) {
            this.pool.putByte(6).putLong(this.key.longVal);
            value = new Item(this.index, this.key);
            this.index += 2;
            this.put(value);
        }
        return value;
    }
    
    Item newMethodItem(final String s, final String s2, final String s3, final boolean b) {
        final int n = b ? 11 : 10;
        this.key3.set(n, s, s2, s3);
        Item value = this.get(this.key3);
        if (value == null) {
            this.put122(n, this.newClass(s), this.newNameType(s2, s3));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    private void put(final Item item) {
        if (this.index + this.typeCount > this.threshold) {
            final int length = this.items.length;
            final int n = length * 2 + 1;
            final Item[] items = new Item[n];
            for (int i = length - 1; i >= 0; --i) {
                Item next;
                for (Item item2 = this.items[i]; item2 != null; item2 = next) {
                    final int n2 = item2.hashCode % items.length;
                    next = item2.next;
                    item2.next = items[n2];
                    items[n2] = item2;
                }
            }
            this.items = items;
            this.threshold = (int)(n * 0.75);
        }
        final int n3 = item.hashCode % this.items.length;
        item.next = this.items[n3];
        this.items[n3] = item;
    }
    
    public ClassWriter(final int n) {
        super(327680);
        this.index = 1;
        this.pool = new ByteVector();
        this.items = new Item[256];
        this.threshold = (int)(0.75 * this.items.length);
        this.key = new Item();
        this.key2 = new Item();
        this.key3 = new Item();
        this.key4 = new Item();
        this.compute = (((n & 0x2) != 0x0) ? 0 : (((n & 0x1) != 0x0) ? 2 : 3));
    }
    
    @Override
    public final void visitSource(final String s, final String s2) {
        if (s != null) {
            this.sourceFile = this.newUTF8(s);
        }
        if (s2 != null) {
            this.sourceDebug = new ByteVector().encodeUTF8(s2, 0, Integer.MAX_VALUE);
        }
    }
    
    Item newInvokeDynamicItem(final String s, final String s2, final Handle handle, final Object... array) {
        ByteVector bootstrapMethods = this.bootstrapMethods;
        if (bootstrapMethods == null) {
            final ByteVector bootstrapMethods2 = new ByteVector();
            this.bootstrapMethods = bootstrapMethods2;
            bootstrapMethods = bootstrapMethods2;
        }
        final int length = bootstrapMethods.length;
        int hashCode = handle.hashCode();
        bootstrapMethods.putShort(this.newHandle(handle.tag, handle.owner, handle.name, handle.desc, handle.isInterface()));
        final int length2 = array.length;
        bootstrapMethods.putShort(length2);
        for (final Object o : array) {
            hashCode ^= o.hashCode();
            bootstrapMethods.putShort(this.newConst(o));
        }
        final byte[] data = bootstrapMethods.data;
        final int n = 2 + length2 << 1;
        final int n2 = hashCode & Integer.MAX_VALUE;
        Item item = this.items[n2 % this.items.length];
    Label_0163:
        while (item != null) {
            if (item.type == 33 && item.hashCode == n2) {
                final int intVal = item.intVal;
                for (int j = 0; j < n; ++j) {
                    if (data[length + j] != data[intVal + j]) {
                        item = item.next;
                        continue Label_0163;
                    }
                }
                break;
            }
            item = item.next;
        }
        int index;
        if (item != null) {
            index = item.index;
            bootstrapMethods.length = length;
        }
        else {
            index = this.bootstrapMethodsCount++;
            final Item item2 = new Item(index);
            item2.set(length, n2);
            this.put(item2);
        }
        this.key3.set(s, s2, index);
        Item value = this.get(this.key3);
        if (value == null) {
            this.put122(18, index, this.newNameType(s, s2));
            value = new Item(this.index++, this.key3);
            this.put(value);
        }
        return value;
    }
    
    public int newClass(final String s) {
        return this.newClassItem(s).index;
    }
}
