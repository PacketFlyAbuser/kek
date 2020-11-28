// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.HashSet;
import org.spongepowered.asm.mixin.Unique;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.Type;
import java.util.Set;

public final class InterfaceInfo
{
    private final /* synthetic */ String prefix;
    private /* synthetic */ Set<String> methods;
    private final /* synthetic */ MixinInfo mixin;
    private final /* synthetic */ boolean unique;
    private final /* synthetic */ Type iface;
    
    public String getPrefix() {
        return this.prefix;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final InterfaceInfo interfaceInfo = (InterfaceInfo)o;
        return this.mixin.equals(interfaceInfo.mixin) && this.prefix.equals(interfaceInfo.prefix) && this.iface.equals(interfaceInfo.iface);
    }
    
    public String getName() {
        return this.iface.getClassName();
    }
    
    private void readInterface(final String s) {
        final ClassInfo forName = ClassInfo.forName(s);
        final Iterator<ClassInfo.Method> iterator = forName.getMethods().iterator();
        while (iterator.hasNext()) {
            this.methods.add(iterator.next().toString());
        }
        final Iterator<String> iterator2 = forName.getInterfaces().iterator();
        while (iterator2.hasNext()) {
            this.readInterface(iterator2.next());
        }
    }
    
    public boolean renameMethod(final MethodNode methodNode) {
        if (this.methods == null) {
            this.initMethods();
        }
        if (!methodNode.name.startsWith(this.prefix)) {
            if (this.methods.contains(methodNode.name + methodNode.desc)) {
                this.decorateUniqueMethod(methodNode);
            }
            return false;
        }
        final String substring = methodNode.name.substring(this.prefix.length());
        if (!this.methods.contains(substring + methodNode.desc)) {
            throw new InvalidMixinException(this.mixin, String.format("%s does not exist in target interface %s", substring, this.getName()));
        }
        if ((methodNode.access & 0x1) == 0x0) {
            throw new InvalidMixinException(this.mixin, String.format("%s cannot implement %s because it is not visible", substring, this.getName()));
        }
        Annotations.setVisible(methodNode, MixinRenamed.class, "originalName", methodNode.name, "isInterfaceMember", true);
        this.decorateUniqueMethod(methodNode);
        methodNode.name = substring;
        return true;
    }
    
    @Override
    public int hashCode() {
        return 31 * (31 * this.mixin.hashCode() + this.prefix.hashCode()) + this.iface.hashCode();
    }
    
    private InterfaceInfo(final MixinInfo mixin, final String prefix, final Type iface, final boolean unique) {
        if (prefix == null || prefix.length() < 2 || !prefix.endsWith("$")) {
            throw new InvalidMixinException(mixin, String.format("Prefix %s for iface %s is not valid", prefix, iface.toString()));
        }
        this.mixin = mixin;
        this.prefix = prefix;
        this.iface = iface;
        this.unique = unique;
    }
    
    public Type getIface() {
        return this.iface;
    }
    
    private void decorateUniqueMethod(final MethodNode methodNode) {
        if (!this.unique) {
            return;
        }
        if (Annotations.getVisible(methodNode, Unique.class) == null) {
            Annotations.setVisible(methodNode, Unique.class, new Object[0]);
            this.mixin.getClassInfo().findMethod(methodNode).setUnique(true);
        }
    }
    
    public boolean isUnique() {
        return this.unique;
    }
    
    private void initMethods() {
        this.methods = new HashSet<String>();
        this.readInterface(this.iface.getInternalName());
    }
    
    static InterfaceInfo fromAnnotation(final MixinInfo mixinInfo, final AnnotationNode annotationNode) {
        final String s = Annotations.getValue(annotationNode, "prefix");
        final Type type = Annotations.getValue(annotationNode, "iface");
        final Boolean b = Annotations.getValue(annotationNode, "unique");
        if (s == null || type == null) {
            throw new InvalidMixinException(mixinInfo, String.format("@Interface annotation on %s is missing a required parameter", mixinInfo));
        }
        return new InterfaceInfo(mixinInfo, s, type, b != null && b);
    }
    
    public String getInternalName() {
        return this.iface.getInternalName();
    }
}
