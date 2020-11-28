// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke.arg;

import java.util.HashMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.ClassWriter;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.util.asm.MethodVisitorEx;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.Type;
import com.google.common.collect.BiMap;
import java.util.Map;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

public final class ArgsClassGenerator implements IClassGenerator
{
    public static final /* synthetic */ String ARGS_NAME;
    public static final /* synthetic */ String ARGS_REF;
    private final /* synthetic */ Map<String, byte[]> classBytes;
    private /* synthetic */ int nextIndex;
    private final /* synthetic */ BiMap<String, String> classNames;
    
    public String getClassName(final String s) {
        if (!s.endsWith(")V")) {
            throw new IllegalArgumentException("Invalid @ModifyArgs method descriptor");
        }
        String format = (String)this.classNames.get((Object)s);
        if (format == null) {
            format = String.format("%s%d", "org.spongepowered.asm.synthetic.args.Args$", this.nextIndex++);
            this.classNames.put((Object)s, (Object)format);
        }
        return format;
    }
    
    public String getClassRef(final String s) {
        return this.getClassName(s).replace('.', '/');
    }
    
    private static String getSignature(final Type[] array) {
        return new SignaturePrinter("", null, array).setFullyQualified(true).getFormattedArgs();
    }
    
    private void generateGetters(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        byte i = 0;
        for (final Type type : array) {
            final MethodVisitorEx methodVisitorEx = new MethodVisitorEx(classVisitor.visitMethod(1, "$" + i, "()" + type.getDescriptor(), null, null));
            methodVisitorEx.visitCode();
            methodVisitorEx.visitVarInsn(25, 0);
            methodVisitorEx.visitFieldInsn(180, s, "values", "[Ljava/lang/Object;");
            methodVisitorEx.visitConstant(i);
            methodVisitorEx.visitInsn(50);
            unbox(methodVisitorEx, type);
            methodVisitorEx.visitInsn(type.getOpcode(172));
            methodVisitorEx.visitMaxs(2, 1);
            methodVisitorEx.visitEnd();
            ++i;
        }
    }
    
    private static void unbox(final MethodVisitor methodVisitor, final Type type) {
        final String boxingType = Bytecode.getBoxingType(type);
        if (boxingType != null) {
            final String unboxingMethod = Bytecode.getUnboxingMethod(type);
            final String string = "()" + type.getDescriptor();
            methodVisitor.visitTypeInsn(192, boxingType);
            methodVisitor.visitMethodInsn(182, boxingType, unboxingMethod, string, false);
        }
        else {
            methodVisitor.visitTypeInsn(192, type.getInternalName());
        }
    }
    
    private void generateToString(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        final MethodVisitor visitMethod = classVisitor.visitMethod(1, "toString", "()Ljava/lang/String;", null, null);
        visitMethod.visitCode();
        visitMethod.visitLdcInsn("Args" + getSignature(array));
        visitMethod.visitInsn(176);
        visitMethod.visitMaxs(1, 1);
        visitMethod.visitEnd();
    }
    
    private void generateCtor(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        final MethodVisitor visitMethod = classVisitor.visitMethod(2, "<init>", "([Ljava/lang/Object;)V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(25, 0);
        visitMethod.visitVarInsn(25, 1);
        visitMethod.visitMethodInsn(183, ArgsClassGenerator.ARGS_REF, "<init>", "([Ljava/lang/Object;)V", false);
        visitMethod.visitInsn(177);
        visitMethod.visitMaxs(2, 2);
        visitMethod.visitEnd();
    }
    
    private void generateFactory(final String str, final String s, final Type[] array, final ClassVisitor classVisitor) {
        final MethodVisitorEx methodVisitorEx = new MethodVisitorEx(classVisitor.visitMethod(9, "of", Bytecode.changeDescriptorReturnType(s, "L" + str + ";"), null, null));
        methodVisitorEx.visitCode();
        methodVisitorEx.visitTypeInsn(187, str);
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitConstant((byte)array.length);
        methodVisitorEx.visitTypeInsn(189, "java/lang/Object");
        byte b = 0;
        for (final Type type : array) {
            methodVisitorEx.visitInsn(89);
            methodVisitorEx.visitConstant(b);
            final MethodVisitorEx methodVisitorEx2 = methodVisitorEx;
            final int opcode = type.getOpcode(21);
            final byte b2 = b;
            ++b;
            methodVisitorEx2.visitVarInsn(opcode, b2);
            box(methodVisitorEx, type);
            methodVisitorEx.visitInsn(83);
        }
        methodVisitorEx.visitMethodInsn(183, str, "<init>", "([Ljava/lang/Object;)V", false);
        methodVisitorEx.visitInsn(176);
        methodVisitorEx.visitMaxs(6, Bytecode.getArgsSize(array));
        methodVisitorEx.visitEnd();
    }
    
    private byte[] generateClass(final String s, final String s2) {
        final String replace = s.replace('.', '/');
        final Type[] argumentTypes = Type.getArgumentTypes(s2);
        ClassVisitor classVisitor;
        final ClassWriter classWriter = (ClassWriter)(classVisitor = new ClassWriter(2));
        if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
            classVisitor = new CheckClassAdapter(classWriter);
        }
        classVisitor.visit(50, 4129, replace, null, ArgsClassGenerator.ARGS_REF, null);
        classVisitor.visitSource(s.substring(s.lastIndexOf(46) + 1) + ".java", null);
        this.generateCtor(replace, s2, argumentTypes, classVisitor);
        this.generateToString(replace, s2, argumentTypes, classVisitor);
        this.generateFactory(replace, s2, argumentTypes, classVisitor);
        this.generateSetters(replace, s2, argumentTypes, classVisitor);
        this.generateGetters(replace, s2, argumentTypes, classVisitor);
        classVisitor.visitEnd();
        return classWriter.toByteArray();
    }
    
    @Override
    public byte[] generate(final String s) {
        return this.getBytes(s);
    }
    
    private static void throwAIOOBE(final MethodVisitorEx methodVisitorEx, final int n) {
        methodVisitorEx.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException");
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitVarInsn(21, n);
        methodVisitorEx.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException", "<init>", "(I)V", false);
        methodVisitorEx.visitInsn(191);
    }
    
    private void generateSetters(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        this.generateIndexedSetter(s, s2, array, classVisitor);
        this.generateMultiSetter(s, s2, array, classVisitor);
    }
    
    static {
        SET_DESC = "(ILjava/lang/Object;)V";
        ACE_CTOR_DESC = "(IILjava/lang/String;)V";
        GETTER_PREFIX = "$";
        NPE_CTOR_DESC = "(Ljava/lang/String;)V";
        CLASS_NAME_BASE = "org.spongepowered.asm.synthetic.args.Args$";
        ACE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException";
        SETALL_DESC = "([Ljava/lang/Object;)V";
        VALUES_FIELD = "values";
        NPE = "java/lang/NullPointerException";
        AIOOBE = "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentIndexOutOfBoundsException";
        OBJECT = "java/lang/Object";
        AIOOBE_CTOR_DESC = "(I)V";
        CTOR_DESC = "([Ljava/lang/Object;)V";
        SET = "set";
        SETALL = "setAll";
        OBJECT_ARRAY = "[Ljava/lang/Object;";
        ARGS_NAME = Args.class.getName();
        ARGS_REF = ArgsClassGenerator.ARGS_NAME.replace('.', '/');
    }
    
    private static void throwNPE(final MethodVisitorEx methodVisitorEx, final String s) {
        methodVisitorEx.visitTypeInsn(187, "java/lang/NullPointerException");
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitLdcInsn(s);
        methodVisitorEx.visitMethodInsn(183, "java/lang/NullPointerException", "<init>", "(Ljava/lang/String;)V", false);
        methodVisitorEx.visitInsn(191);
    }
    
    public byte[] getBytes(final String s) {
        byte[] generateClass = this.classBytes.get(s);
        if (generateClass == null) {
            final String s2 = (String)this.classNames.inverse().get((Object)s);
            if (s2 == null) {
                return null;
            }
            generateClass = this.generateClass(s, s2);
            this.classBytes.put(s, generateClass);
        }
        return generateClass;
    }
    
    private static void box(final MethodVisitor methodVisitor, final Type type) {
        final String boxingType = Bytecode.getBoxingType(type);
        if (boxingType != null) {
            methodVisitor.visitMethodInsn(184, boxingType, "valueOf", String.format("(%s)L%s;", type.getDescriptor(), boxingType), false);
        }
    }
    
    private void generateIndexedSetter(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        final MethodVisitorEx methodVisitorEx = new MethodVisitorEx(classVisitor.visitMethod(1, "set", "(ILjava/lang/Object;)V", null, null));
        methodVisitorEx.visitCode();
        final Label label = new Label();
        final Label label2 = new Label();
        final Label[] array2 = new Label[array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = new Label();
        }
        methodVisitorEx.visitVarInsn(25, 0);
        methodVisitorEx.visitFieldInsn(180, s, "values", "[Ljava/lang/Object;");
        for (byte b = 0; b < array.length; ++b) {
            methodVisitorEx.visitVarInsn(21, 1);
            methodVisitorEx.visitConstant(b);
            methodVisitorEx.visitJumpInsn(159, array2[b]);
        }
        throwAIOOBE(methodVisitorEx, 1);
        for (int j = 0; j < array.length; ++j) {
            final String boxingType = Bytecode.getBoxingType(array[j]);
            methodVisitorEx.visitLabel(array2[j]);
            methodVisitorEx.visitVarInsn(21, 1);
            methodVisitorEx.visitVarInsn(25, 2);
            methodVisitorEx.visitTypeInsn(192, (boxingType != null) ? boxingType : array[j].getInternalName());
            methodVisitorEx.visitJumpInsn(167, (boxingType != null) ? label2 : label);
        }
        methodVisitorEx.visitLabel(label2);
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitJumpInsn(199, label);
        throwNPE(methodVisitorEx, "Argument with primitive type cannot be set to NULL");
        methodVisitorEx.visitLabel(label);
        methodVisitorEx.visitInsn(83);
        methodVisitorEx.visitInsn(177);
        methodVisitorEx.visitMaxs(6, 3);
        methodVisitorEx.visitEnd();
    }
    
    private void generateMultiSetter(final String s, final String s2, final Type[] array, final ClassVisitor classVisitor) {
        final MethodVisitorEx methodVisitorEx = new MethodVisitorEx(classVisitor.visitMethod(1, "setAll", "([Ljava/lang/Object;)V", null, null));
        methodVisitorEx.visitCode();
        final Label label = new Label();
        final Label label2 = new Label();
        int n = 6;
        methodVisitorEx.visitVarInsn(25, 1);
        methodVisitorEx.visitInsn(190);
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitConstant((byte)array.length);
        methodVisitorEx.visitJumpInsn(159, label);
        methodVisitorEx.visitTypeInsn(187, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException");
        methodVisitorEx.visitInsn(89);
        methodVisitorEx.visitInsn(93);
        methodVisitorEx.visitInsn(88);
        methodVisitorEx.visitConstant((byte)array.length);
        methodVisitorEx.visitLdcInsn(getSignature(array));
        methodVisitorEx.visitMethodInsn(183, "org/spongepowered/asm/mixin/injection/invoke/arg/ArgumentCountException", "<init>", "(IILjava/lang/String;)V", false);
        methodVisitorEx.visitInsn(191);
        methodVisitorEx.visitLabel(label);
        methodVisitorEx.visitInsn(87);
        methodVisitorEx.visitVarInsn(25, 0);
        methodVisitorEx.visitFieldInsn(180, s, "values", "[Ljava/lang/Object;");
        for (byte b = 0; b < array.length; ++b) {
            methodVisitorEx.visitInsn(89);
            methodVisitorEx.visitConstant(b);
            methodVisitorEx.visitVarInsn(25, 1);
            methodVisitorEx.visitConstant(b);
            methodVisitorEx.visitInsn(50);
            final String boxingType = Bytecode.getBoxingType(array[b]);
            methodVisitorEx.visitTypeInsn(192, (boxingType != null) ? boxingType : array[b].getInternalName());
            if (boxingType != null) {
                methodVisitorEx.visitInsn(89);
                methodVisitorEx.visitJumpInsn(198, label2);
                n = 7;
            }
            methodVisitorEx.visitInsn(83);
        }
        methodVisitorEx.visitInsn(177);
        methodVisitorEx.visitLabel(label2);
        throwNPE(methodVisitorEx, "Argument with primitive type cannot be set to NULL");
        methodVisitorEx.visitInsn(177);
        methodVisitorEx.visitMaxs(n, 2);
        methodVisitorEx.visitEnd();
    }
    
    public ArgsClassGenerator() {
        this.nextIndex = 1;
        this.classNames = (BiMap<String, String>)HashBiMap.create();
        this.classBytes = new HashMap<String, byte[]>();
    }
}
