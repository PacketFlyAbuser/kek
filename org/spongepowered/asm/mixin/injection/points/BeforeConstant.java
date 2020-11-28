// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.ArrayList;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Floats;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import java.util.Iterator;
import com.google.common.primitives.Ints;
import java.util.HashSet;
import org.spongepowered.asm.mixin.injection.Constant;
import java.util.List;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("CONSTANT")
public class BeforeConstant extends InjectionPoint
{
    private final /* synthetic */ String matchByType;
    private final /* synthetic */ boolean expand;
    private final /* synthetic */ Type typeValue;
    private final /* synthetic */ boolean log;
    private final /* synthetic */ int ordinal;
    private static final /* synthetic */ Logger logger;
    private final /* synthetic */ Integer intValue;
    private final /* synthetic */ int[] expandOpcodes;
    private final /* synthetic */ boolean nullValue;
    private final /* synthetic */ Double doubleValue;
    private final /* synthetic */ Long longValue;
    private final /* synthetic */ String stringValue;
    private final /* synthetic */ Float floatValue;
    
    private boolean matchesConditionalInsn(final int n, final AbstractInsnNode abstractInsnNode) {
        final int[] expandOpcodes = this.expandOpcodes;
        final int length = expandOpcodes.length;
        int i = 0;
        while (i < length) {
            final int n2 = expandOpcodes[i];
            final int opcode = abstractInsnNode.getOpcode();
            if (opcode == n2) {
                if (n == 148 || n == 149 || n == 150 || n == 151 || n == 152) {
                    this.log("  BeforeConstant is ignoring {} following {}", Bytecode.getOpcodeName(opcode), Bytecode.getOpcodeName(n));
                    return false;
                }
                this.log("  BeforeConstant found {} instruction", Bytecode.getOpcodeName(opcode));
                return true;
            }
            else {
                ++i;
            }
        }
        if (this.intValue != null && this.intValue == 0 && Bytecode.isConstant(abstractInsnNode)) {
            final Object constant = Bytecode.getConstant(abstractInsnNode);
            this.log("  BeforeConstant found INTEGER constant: value = {}", constant);
            return constant instanceof Integer && (int)constant == 0;
        }
        return false;
    }
    
    protected void log(final String s, final Object... array) {
        if (this.log) {
            BeforeConstant.logger.info(s, array);
        }
    }
    
    @Override
    public boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> collection) {
        boolean b = false;
        this.log("BeforeConstant is searching for constants in method with descriptor {}", s);
        final ListIterator<AbstractInsnNode> iterator = list.iterator();
        int i = 0;
        int opcode = 0;
        while (iterator.hasNext()) {
            final AbstractInsnNode abstractInsnNode = iterator.next();
            if (this.expand ? this.matchesConditionalInsn(opcode, abstractInsnNode) : this.matchesConstantInsn(abstractInsnNode)) {
                this.log("    BeforeConstant found a matching constant{} at ordinal {}", (this.matchByType != null) ? " TYPE" : " value", i);
                if (this.ordinal == -1 || this.ordinal == i) {
                    this.log("      BeforeConstant found {}", Bytecode.describeNode(abstractInsnNode).trim());
                    collection.add(abstractInsnNode);
                    b = true;
                }
                ++i;
            }
            if (!(abstractInsnNode instanceof LabelNode) && !(abstractInsnNode instanceof FrameNode)) {
                opcode = abstractInsnNode.getOpcode();
            }
        }
        return b;
    }
    
    private int[] parseExpandOpcodes(final List<Constant.Condition> list) {
        final HashSet<Integer> set = new HashSet<Integer>();
        final Iterator<Constant.Condition> iterator = list.iterator();
        while (iterator.hasNext()) {
            final int[] opcodes = iterator.next().getEquivalentCondition().getOpcodes();
            for (int length = opcodes.length, i = 0; i < length; ++i) {
                set.add(opcodes[i]);
            }
        }
        return Ints.toArray((Collection)set);
    }
    
    private String validateDiscriminator(final IMixinContext obj, String s, final Boolean b, final String str) {
        final int count = count(b, this.intValue, this.floatValue, this.longValue, this.doubleValue, this.stringValue, this.typeValue);
        if (count == 1) {
            s = null;
        }
        else if (count > 1) {
            throw new InvalidInjectionException(obj, "Conflicting constant discriminators specified " + str + " for " + obj);
        }
        return s;
    }
    
    private static int count(final Object... array) {
        int n = 0;
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] != null) {
                ++n;
            }
        }
        return n;
    }
    
    private boolean matchesConstantInsn(final AbstractInsnNode abstractInsnNode) {
        if (!Bytecode.isConstant(abstractInsnNode)) {
            return false;
        }
        final Object constant = Bytecode.getConstant(abstractInsnNode);
        if (constant == null) {
            this.log("  BeforeConstant found NULL constant: nullValue = {}", this.nullValue);
            return this.nullValue || "Ljava/lang/Object;".equals(this.matchByType);
        }
        if (constant instanceof Integer) {
            this.log("  BeforeConstant found INTEGER constant: value = {}, intValue = {}", constant, this.intValue);
            return constant.equals(this.intValue) || "I".equals(this.matchByType);
        }
        if (constant instanceof Float) {
            this.log("  BeforeConstant found FLOAT constant: value = {}, floatValue = {}", constant, this.floatValue);
            return constant.equals(this.floatValue) || "F".equals(this.matchByType);
        }
        if (constant instanceof Long) {
            this.log("  BeforeConstant found LONG constant: value = {}, longValue = {}", constant, this.longValue);
            return constant.equals(this.longValue) || "J".equals(this.matchByType);
        }
        if (constant instanceof Double) {
            this.log("  BeforeConstant found DOUBLE constant: value = {}, doubleValue = {}", constant, this.doubleValue);
            return constant.equals(this.doubleValue) || "D".equals(this.matchByType);
        }
        if (constant instanceof String) {
            this.log("  BeforeConstant found STRING constant: value = {}, stringValue = {}", constant, this.stringValue);
            return constant.equals(this.stringValue) || "Ljava/lang/String;".equals(this.matchByType);
        }
        if (constant instanceof Type) {
            this.log("  BeforeConstant found CLASS constant: value = {}, typeValue = {}", constant, this.typeValue);
            return constant.equals(this.typeValue) || "Ljava/lang/Class;".equals(this.matchByType);
        }
        return false;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    public BeforeConstant(final InjectionPointData injectionPointData) {
        super(injectionPointData);
        final String value = injectionPointData.get("nullValue", null);
        final Boolean b = (value != null) ? Boolean.valueOf(Boolean.parseBoolean(value)) : null;
        this.ordinal = injectionPointData.getOrdinal();
        this.nullValue = (b != null && b);
        this.intValue = Ints.tryParse(injectionPointData.get("intValue", ""));
        this.floatValue = Floats.tryParse(injectionPointData.get("floatValue", ""));
        this.longValue = Longs.tryParse(injectionPointData.get("longValue", ""));
        this.doubleValue = Doubles.tryParse(injectionPointData.get("doubleValue", ""));
        this.stringValue = injectionPointData.get("stringValue", null);
        final String value2 = injectionPointData.get("classValue", null);
        this.typeValue = ((value2 != null) ? Type.getObjectType(value2.replace('.', '/')) : null);
        this.matchByType = this.validateDiscriminator(injectionPointData.getContext(), "V", b, "in @At(\"CONSTANT\") args");
        if ("V".equals(this.matchByType)) {
            throw new InvalidInjectionException(injectionPointData.getContext(), "No constant discriminator could be parsed in @At(\"CONSTANT\") args");
        }
        final ArrayList<Constant.Condition> list = new ArrayList<Constant.Condition>();
        final String lowerCase = injectionPointData.get("expandZeroConditions", "").toLowerCase();
        for (final Constant.Condition condition : Constant.Condition.values()) {
            if (lowerCase.contains(condition.name().toLowerCase())) {
                list.add(condition);
            }
        }
        this.expandOpcodes = this.parseExpandOpcodes(list);
        this.expand = (this.expandOpcodes.length > 0);
        this.log = injectionPointData.get("log", false);
    }
    
    public BeforeConstant(final IMixinContext mixinContext, final AnnotationNode annotationNode, final String s) {
        super(Annotations.getValue(annotationNode, "slice", ""), Selector.DEFAULT, null);
        final Boolean b = Annotations.getValue(annotationNode, "nullValue", (Boolean)null);
        this.ordinal = Annotations.getValue(annotationNode, "ordinal", -1);
        this.nullValue = (b != null && b);
        this.intValue = Annotations.getValue(annotationNode, "intValue", (Integer)null);
        this.floatValue = Annotations.getValue(annotationNode, "floatValue", (Float)null);
        this.longValue = Annotations.getValue(annotationNode, "longValue", (Long)null);
        this.doubleValue = Annotations.getValue(annotationNode, "doubleValue", (Double)null);
        this.stringValue = Annotations.getValue(annotationNode, "stringValue", (String)null);
        this.typeValue = Annotations.getValue(annotationNode, "classValue", (Type)null);
        this.matchByType = this.validateDiscriminator(mixinContext, s, b, "on @Constant annotation");
        this.expandOpcodes = this.parseExpandOpcodes(Annotations.getValue(annotationNode, "expandZeroConditions", true, Constant.Condition.class));
        this.expand = (this.expandOpcodes.length > 0);
        this.log = Annotations.getValue(annotationNode, "log", Boolean.FALSE);
    }
}
