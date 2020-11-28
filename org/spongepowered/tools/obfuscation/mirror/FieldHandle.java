// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import javax.lang.model.element.Element;
import com.google.common.base.Strings;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;

public class FieldHandle extends MemberHandle<MappingField>
{
    private final /* synthetic */ boolean rawType;
    private final /* synthetic */ VariableElement element;
    
    public FieldHandle(final TypeElement typeElement, final VariableElement variableElement, final boolean b) {
        this(TypeUtils.getInternalName(typeElement), variableElement, b);
    }
    
    public FieldHandle(final String s, final VariableElement variableElement, final boolean b) {
        this(s, variableElement, b, TypeUtils.getName(variableElement), TypeUtils.getInternalName(variableElement));
    }
    
    @Override
    public String toString() {
        return String.format("%s%s:%s", (this.getOwner() != null) ? ("L" + this.getOwner() + ";") : "", Strings.nullToEmpty(this.getName()), Strings.nullToEmpty(this.getDesc()));
    }
    
    public VariableElement getElement() {
        return this.element;
    }
    
    public FieldHandle(final String s, final VariableElement variableElement) {
        this(s, variableElement, false);
    }
    
    public boolean isRawType() {
        return this.rawType;
    }
    
    @Override
    public MappingField asMapping(final boolean b) {
        return new MappingField(b ? this.getOwner() : null, this.getName(), this.getDesc());
    }
    
    public boolean isImaginary() {
        return this.element == null;
    }
    
    public FieldHandle(final TypeElement typeElement, final VariableElement variableElement) {
        this(TypeUtils.getInternalName(typeElement), variableElement);
    }
    
    private FieldHandle(final String s, final VariableElement element, final boolean rawType, final String s2, final String s3) {
        super(s, s2, s3);
        this.element = element;
        this.rawType = rawType;
    }
    
    @Override
    public Visibility getVisibility() {
        return TypeUtils.getVisibility(this.element);
    }
    
    public FieldHandle(final String s, final String s2, final String s3) {
        this(s, null, false, s2, s3);
    }
}
