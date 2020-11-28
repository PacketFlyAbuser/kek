// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.struct;

import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.annotation.processing.Messager;

public class InjectorRemap
{
    private final /* synthetic */ boolean remap;
    private /* synthetic */ Message message;
    private /* synthetic */ int remappedCount;
    
    public void notifyRemapped() {
        ++this.remappedCount;
        this.clearMessage();
    }
    
    public void dispatchPendingMessages(final Messager messager) {
        if (this.remappedCount == 0 && this.message != null) {
            this.message.sendTo(messager);
        }
    }
    
    public void addMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationHandle annotationHandle) {
        this.message = new Message(kind, charSequence, element, annotationHandle);
    }
    
    public InjectorRemap(final boolean remap) {
        this.remap = remap;
    }
    
    public boolean shouldRemap() {
        return this.remap;
    }
    
    public void clearMessage() {
        this.message = null;
    }
}
