// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.struct;

import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.AnnotationMirror;
import javax.tools.Diagnostic;
import javax.lang.model.element.AnnotationValue;

public class Message
{
    private final /* synthetic */ AnnotationValue value;
    private /* synthetic */ Diagnostic.Kind kind;
    private /* synthetic */ CharSequence msg;
    private final /* synthetic */ AnnotationMirror annotation;
    private final /* synthetic */ Element element;
    
    public Message(final Diagnostic.Kind kind, final CharSequence charSequence) {
        this(kind, charSequence, null, (AnnotationMirror)null, null);
    }
    
    public Message setKind(final Diagnostic.Kind kind) {
        this.kind = kind;
        return this;
    }
    
    public AnnotationValue getValue() {
        return this.value;
    }
    
    public Message sendTo(final Messager messager) {
        if (this.value != null) {
            messager.printMessage(this.kind, this.msg, this.element, this.annotation, this.value);
        }
        else if (this.annotation != null) {
            messager.printMessage(this.kind, this.msg, this.element, this.annotation);
        }
        else if (this.element != null) {
            messager.printMessage(this.kind, this.msg, this.element);
        }
        else {
            messager.printMessage(this.kind, this.msg);
        }
        return this;
    }
    
    public AnnotationMirror getAnnotation() {
        return this.annotation;
    }
    
    public Message(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element) {
        this(kind, charSequence, element, (AnnotationMirror)null, null);
    }
    
    public CharSequence getMsg() {
        return this.msg;
    }
    
    public Message(final Diagnostic.Kind kind, final CharSequence msg, final Element element, final AnnotationMirror annotation, final AnnotationValue value) {
        this.kind = kind;
        this.msg = msg;
        this.element = element;
        this.annotation = annotation;
        this.value = value;
    }
    
    public Message(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationHandle annotationHandle) {
        this(kind, charSequence, element, annotationHandle.asMirror(), null);
    }
    
    public Message(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationHandle annotationHandle, final AnnotationValue annotationValue) {
        this(kind, charSequence, element, annotationHandle.asMirror(), annotationValue);
    }
    
    public Diagnostic.Kind getKind() {
        return this.kind;
    }
    
    public Message setMsg(final CharSequence msg) {
        this.msg = msg;
        return this;
    }
    
    public Message(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationMirror annotationMirror) {
        this(kind, charSequence, element, annotationMirror, null);
    }
    
    public Element getElement() {
        return this.element;
    }
}
