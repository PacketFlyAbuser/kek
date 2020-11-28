// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import com.google.common.base.Objects;

public final class MemberInfo
{
    public final /* synthetic */ String desc;
    public final /* synthetic */ boolean matchAll;
    public final /* synthetic */ String name;
    private final /* synthetic */ boolean forceField;
    public final /* synthetic */ String owner;
    private final /* synthetic */ String unparsed;
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || o.getClass() != MemberInfo.class) {
            return false;
        }
        final MemberInfo memberInfo = (MemberInfo)o;
        return this.matchAll == memberInfo.matchAll && this.forceField == memberInfo.forceField && Objects.equal((Object)this.owner, (Object)memberInfo.owner) && Objects.equal((Object)this.name, (Object)memberInfo.name) && Objects.equal((Object)this.desc, (Object)memberInfo.desc);
    }
    
    public boolean matches(final String s, final String s2) {
        return this.matches(s, s2, 0);
    }
    
    public String toCtorType() {
        if (this.unparsed == null) {
            return null;
        }
        final String returnType = this.getReturnType();
        if (returnType != null) {
            return returnType;
        }
        if (this.owner != null) {
            return this.owner;
        }
        if (this.name != null && this.desc == null) {
            return this.name;
        }
        return (this.desc != null) ? this.desc : this.unparsed;
    }
    
    public boolean isClassInitialiser() {
        return "<clinit>".equals(this.name);
    }
    
    public String getReturnType() {
        if (this.desc == null || this.desc.indexOf(41) == -1 || this.desc.indexOf(40) != 0) {
            return null;
        }
        final String substring = this.desc.substring(this.desc.indexOf(41) + 1);
        if (substring.startsWith("L") && substring.endsWith(";")) {
            return substring.substring(1, substring.length() - 1);
        }
        return substring;
    }
    
    public boolean matches(final String anObject, final String anObject2, final String anObject3, final int n) {
        return (this.desc == null || anObject3 == null || this.desc.equals(anObject3)) && (this.name == null || anObject2 == null || this.name.equals(anObject2)) && (this.owner == null || anObject == null || this.owner.equals(anObject)) && (n == 0 || this.matchAll);
    }
    
    public MemberInfo(final String name, final String owner, final String desc, final boolean matchAll, final String unparsed) {
        if (owner != null && owner.contains(".")) {
            throw new IllegalArgumentException("Attempt to instance a MemberInfo with an invalid owner format");
        }
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.matchAll = matchAll;
        this.forceField = false;
        this.unparsed = unparsed;
    }
    
    public boolean isField() {
        return this.forceField || (this.desc != null && !this.desc.startsWith("("));
    }
    
    public static MemberInfo fromMapping(final IMapping<?> mapping) {
        return new MemberInfo(mapping);
    }
    
    public String toCtorDesc() {
        if (this.desc != null && this.desc.startsWith("(") && this.desc.indexOf(41) > -1) {
            return this.desc.substring(0, this.desc.indexOf(41) + 1) + "V";
        }
        return null;
    }
    
    public MemberInfo validate() throws InvalidMemberDescriptorException {
        if (this.owner != null) {
            if (!this.owner.matches("(?i)^[\\w\\p{Sc}/]+$")) {
                throw new InvalidMemberDescriptorException("Invalid owner: " + this.owner);
            }
            try {
                if (!this.owner.equals(Type.getType(this.owner).getDescriptor())) {
                    throw new InvalidMemberDescriptorException("Invalid owner type specified: " + this.owner);
                }
            }
            catch (Exception ex) {
                throw new InvalidMemberDescriptorException("Invalid owner type specified: " + this.owner);
            }
        }
        if (this.name != null && !this.name.matches("(?i)^<?[\\w\\p{Sc}]+>?$")) {
            throw new InvalidMemberDescriptorException("Invalid name: " + this.name);
        }
        if (this.desc != null) {
            if (!this.desc.matches("^(\\([\\w\\p{Sc}\\[/;]*\\))?\\[*[\\w\\p{Sc}/;]+$")) {
                throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
            }
            if (this.isField()) {
                if (!this.desc.equals(Type.getType(this.desc).getDescriptor())) {
                    throw new InvalidMemberDescriptorException("Invalid field type in descriptor: " + this.desc);
                }
            }
            else {
                try {
                    Type.getArgumentTypes(this.desc);
                }
                catch (Exception ex2) {
                    throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
                }
                final String substring = this.desc.substring(this.desc.indexOf(41) + 1);
                try {
                    if (!substring.equals(Type.getType(substring).getDescriptor())) {
                        throw new InvalidMemberDescriptorException("Invalid return type \"" + substring + "\" in descriptor: " + this.desc);
                    }
                }
                catch (Exception ex3) {
                    throw new InvalidMemberDescriptorException("Invalid return type \"" + substring + "\" in descriptor: " + this.desc);
                }
            }
        }
        return this;
    }
    
    public boolean isInitialiser() {
        return this.isConstructor() || this.isClassInitialiser();
    }
    
    public MemberInfo(final String s, final boolean b) {
        this(s, null, null, b);
    }
    
    public String toDescriptor() {
        if (this.desc == null) {
            return "";
        }
        return new SignaturePrinter(this).setFullyQualified(true).toDescriptor();
    }
    
    public IMapping<?> asMapping() {
        return (IMapping<?>)(this.isField() ? this.asFieldMapping() : this.asMethodMapping());
    }
    
    public MemberInfo transform(final String s) {
        if ((s == null && this.desc == null) || (s != null && s.equals(this.desc))) {
            return this;
        }
        return new MemberInfo(this.name, this.owner, s, this.matchAll);
    }
    
    @Deprecated
    public String toSrg() {
        if (!this.isFullyQualified()) {
            throw new MixinException("Cannot convert unqualified reference to SRG mapping");
        }
        if (this.desc.startsWith("(")) {
            return this.owner + "/" + this.name + " " + this.desc;
        }
        return this.owner + "/" + this.name;
    }
    
    public static MemberInfo parseAndValidate(final String s, final IMixinContext mixinContext) throws InvalidMemberDescriptorException {
        return parse(s, mixinContext.getReferenceMapper(), mixinContext.getClassRef()).validate();
    }
    
    public boolean isConstructor() {
        return "<init>".equals(this.name);
    }
    
    private static MemberInfo parse(final String s, final IReferenceMapper referenceMapper, final String s2) {
        String s3 = null;
        String s4 = null;
        String s5 = Strings.nullToEmpty(s).replaceAll("\\s", "");
        if (referenceMapper != null) {
            s5 = referenceMapper.remap(s2, s5);
        }
        final int lastIndex = s5.lastIndexOf(46);
        final int index = s5.indexOf(59);
        if (lastIndex > -1) {
            s4 = s5.substring(0, lastIndex).replace('.', '/');
            s5 = s5.substring(lastIndex + 1);
        }
        else if (index > -1 && s5.startsWith("L")) {
            s4 = s5.substring(1, index).replace('.', '/');
            s5 = s5.substring(index + 1);
        }
        final int index2 = s5.indexOf(40);
        final int index3 = s5.indexOf(58);
        if (index2 > -1) {
            s3 = s5.substring(index2);
            s5 = s5.substring(0, index2);
        }
        else if (index3 > -1) {
            s3 = s5.substring(index3 + 1);
            s5 = s5.substring(0, index3);
        }
        if ((s5.indexOf(47) > -1 || s5.indexOf(46) > -1) && s4 == null) {
            s4 = s5;
            s5 = "";
        }
        final boolean endsWith = s5.endsWith("*");
        if (endsWith) {
            s5 = s5.substring(0, s5.length() - 1);
        }
        if (s5.isEmpty()) {
            s5 = null;
        }
        return new MemberInfo(s5, s4, s3, endsWith, s);
    }
    
    public MemberInfo(final String s, final String s2, final String s3) {
        this(s, s2, s3, false);
    }
    
    public MemberInfo(final AbstractInsnNode abstractInsnNode) {
        this.matchAll = false;
        this.forceField = false;
        this.unparsed = null;
        if (abstractInsnNode instanceof MethodInsnNode) {
            final MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
            this.owner = methodInsnNode.owner;
            this.name = methodInsnNode.name;
            this.desc = methodInsnNode.desc;
        }
        else {
            if (!(abstractInsnNode instanceof FieldInsnNode)) {
                throw new IllegalArgumentException("insn must be an instance of MethodInsnNode or FieldInsnNode");
            }
            final FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
            this.owner = fieldInsnNode.owner;
            this.name = fieldInsnNode.name;
            this.desc = fieldInsnNode.desc;
        }
    }
    
    public MemberInfo(final String s, final String s2, final boolean b) {
        this(s, s2, null, b);
    }
    
    public MemberInfo move(final String s) {
        if ((s == null && this.owner == null) || (s != null && s.equals(this.owner))) {
            return this;
        }
        return new MemberInfo(this, s);
    }
    
    public MemberInfo(final IMapping<?> mapping) {
        this.owner = mapping.getOwner();
        this.name = mapping.getSimpleName();
        this.desc = mapping.getDesc();
        this.matchAll = false;
        this.forceField = (mapping.getType() == IMapping.Type.FIELD);
        this.unparsed = null;
    }
    
    public MemberInfo remapUsing(final MappingMethod mappingMethod, final boolean b) {
        return new MemberInfo(this, mappingMethod, b);
    }
    
    public boolean matches(final String s, final String s2, final String s3) {
        return this.matches(s, s2, s3, 0);
    }
    
    public MappingField asFieldMapping() {
        if (!this.isField()) {
            throw new MixinException("Cannot convert non-field reference " + this + " to FieldMapping");
        }
        return new MappingField(this.owner, this.name, this.desc);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.matchAll, this.owner, this.name, this.desc });
    }
    
    public MappingMethod asMethodMapping() {
        if (!this.isFullyQualified()) {
            throw new MixinException("Cannot convert unqualified reference " + this + " to MethodMapping");
        }
        if (this.isField()) {
            throw new MixinException("Cannot convert a non-method reference " + this + " to MethodMapping");
        }
        return new MappingMethod(this.owner, this.name, this.desc);
    }
    
    public boolean matches(final String anObject, final String s, final int n) {
        return (this.name == null || this.name.equals(anObject)) && (this.desc == null || (s != null && s.equals(this.desc))) && (n == 0 || this.matchAll);
    }
    
    public boolean isFullyQualified() {
        return this.owner != null && this.name != null && this.desc != null;
    }
    
    public MemberInfo(final String s, final String s2, final String s3, final boolean b) {
        this(s, s2, s3, b, null);
    }
    
    private MemberInfo(final MemberInfo memberInfo, final String owner) {
        this.owner = owner;
        this.name = memberInfo.name;
        this.desc = memberInfo.desc;
        this.matchAll = memberInfo.matchAll;
        this.forceField = memberInfo.forceField;
        this.unparsed = null;
    }
    
    @Override
    public String toString() {
        final String str = (this.owner != null) ? ("L" + this.owner + ";") : "";
        final String str2 = (this.name != null) ? this.name : "";
        final String str3 = this.matchAll ? "*" : "";
        final String str4 = (this.desc != null) ? this.desc : "";
        return str + str2 + str3 + (str4.startsWith("(") ? "" : ((this.desc != null) ? ":" : "")) + str4;
    }
    
    public static MemberInfo parse(final String s, final IMixinContext mixinContext) {
        return parse(s, mixinContext.getReferenceMapper(), mixinContext.getClassRef());
    }
    
    private MemberInfo(final MemberInfo memberInfo, final MappingMethod mappingMethod, final boolean b) {
        this.owner = (b ? mappingMethod.getOwner() : memberInfo.owner);
        this.name = mappingMethod.getSimpleName();
        this.desc = mappingMethod.getDesc();
        this.matchAll = memberInfo.matchAll;
        this.forceField = false;
        this.unparsed = null;
    }
    
    public static MemberInfo parse(final String s) {
        return parse(s, null, null);
    }
    
    public static MemberInfo parseAndValidate(final String s) throws InvalidMemberDescriptorException {
        return parse(s, null, null).validate();
    }
}
