// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
import com.google.common.base.Strings;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public class MethodHandle extends MemberHandle<MappingMethod>
{
    private final /* synthetic */ TypeHandle ownerHandle;
    private final /* synthetic */ ExecutableElement element;
    
    public MethodHandle(final TypeHandle typeHandle, final ExecutableElement executableElement) {
        this(typeHandle, executableElement, TypeUtils.getName(executableElement), TypeUtils.getDescriptor(executableElement));
    }
    
    public boolean isImaginary() {
        return this.element == null;
    }
    
    @Override
    public Visibility getVisibility() {
        return TypeUtils.getVisibility(this.element);
    }
    
    private MethodHandle(final TypeHandle ownerHandle, final ExecutableElement element, final String s, final String s2) {
        super((ownerHandle != null) ? ownerHandle.getName() : null, s, s2);
        this.element = element;
        this.ownerHandle = ownerHandle;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s%s", (this.getOwner() != null) ? ("L" + this.getOwner() + ";") : "", Strings.nullToEmpty(this.getName()), Strings.nullToEmpty(this.getDesc()));
    }
    
    @Override
    public MappingMethod asMapping(final boolean b) {
        if (!b) {
            return new MappingMethod(null, this.getName(), this.getDesc());
        }
        if (this.ownerHandle != null) {
            return new ResolvableMappingMethod(this.ownerHandle, this.getName(), this.getDesc());
        }
        return new MappingMethod(this.getOwner(), this.getName(), this.getDesc());
    }
    
    public MethodHandle(final TypeHandle typeHandle, final String s, final String s2) {
        this(typeHandle, null, s, s2);
    }
    
    public ExecutableElement getElement() {
        return this.element;
    }
}
