// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
import java.util.ArrayList;
import java.util.List;

public class InjectorGroupInfo
{
    private /* synthetic */ int maxCallbackCount;
    private final /* synthetic */ String name;
    private final /* synthetic */ List<InjectionInfo> members;
    private /* synthetic */ int minCallbackCount;
    private final /* synthetic */ boolean isDefault;
    
    @Override
    public String toString() {
        return String.format("@Group(name=%s, min=%d, max=%d)", this.getName(), this.getMinRequired(), this.getMaxAllowed());
    }
    
    InjectorGroupInfo(final String name, final boolean isDefault) {
        this.members = new ArrayList<InjectionInfo>();
        this.minCallbackCount = -1;
        this.maxCallbackCount = Integer.MAX_VALUE;
        this.name = name;
        this.isDefault = isDefault;
    }
    
    public int getMaxAllowed() {
        return Math.min(this.maxCallbackCount, Integer.MAX_VALUE);
    }
    
    public InjectorGroupInfo validate() throws InjectionValidationException {
        if (this.members.size() == 0) {
            return this;
        }
        int n = 0;
        final Iterator<InjectionInfo> iterator = this.members.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().getInjectedCallbackCount();
        }
        final int minRequired = this.getMinRequired();
        final int maxAllowed = this.getMaxAllowed();
        if (n < minRequired) {
            throw new InjectionValidationException(this, String.format("expected %d invocation(s) but only %d succeeded", minRequired, n));
        }
        if (n > maxAllowed) {
            throw new InjectionValidationException(this, String.format("maximum of %d invocation(s) allowed but %d succeeded", maxAllowed, n));
        }
        return this;
    }
    
    public void setMaxAllowed(final int b) {
        if (b < 1) {
            throw new IllegalArgumentException("Cannot set zero or negative value for injector group max count. Attempted to set max=" + b + " on " + this);
        }
        if (this.maxCallbackCount < Integer.MAX_VALUE && this.maxCallbackCount != b) {
            LogManager.getLogger("mixin").warn("Conflicting max value '{}' on @Group({}), previously specified {}", new Object[] { b, this.name, this.maxCallbackCount });
        }
        this.maxCallbackCount = Math.min(this.maxCallbackCount, b);
    }
    
    public String getName() {
        return this.name;
    }
    
    public Collection<InjectionInfo> getMembers() {
        return Collections.unmodifiableCollection((Collection<? extends InjectionInfo>)this.members);
    }
    
    public void setMinRequired(final int b) {
        if (b < 1) {
            throw new IllegalArgumentException("Cannot set zero or negative value for injector group min count. Attempted to set min=" + b + " on " + this);
        }
        if (this.minCallbackCount > 0 && this.minCallbackCount != b) {
            LogManager.getLogger("mixin").warn("Conflicting min value '{}' on @Group({}), previously specified {}", new Object[] { b, this.name, this.minCallbackCount });
        }
        this.minCallbackCount = Math.max(this.minCallbackCount, b);
    }
    
    public InjectorGroupInfo(final String s) {
        this(s, false);
    }
    
    public int getMinRequired() {
        return Math.max(this.minCallbackCount, 1);
    }
    
    public boolean isDefault() {
        return this.isDefault;
    }
    
    public InjectorGroupInfo add(final InjectionInfo injectionInfo) {
        this.members.add(injectionInfo);
        return this;
    }
    
    public static final class Map extends HashMap<String, InjectorGroupInfo>
    {
        private static final /* synthetic */ InjectorGroupInfo NO_GROUP;
        
        static {
            NO_GROUP = new InjectorGroupInfo("NONE", true);
        }
        
        public InjectorGroupInfo parseGroup(final AnnotationNode annotationNode, final String s) {
            if (annotationNode == null) {
                return Map.NO_GROUP;
            }
            String s2 = Annotations.getValue(annotationNode, "name");
            if (s2 == null || s2.isEmpty()) {
                s2 = s;
            }
            final InjectorGroupInfo forName = this.forName(s2);
            final Integer n = Annotations.getValue(annotationNode, "min");
            if (n != null && n != -1) {
                forName.setMinRequired(n);
            }
            final Integer n2 = Annotations.getValue(annotationNode, "max");
            if (n2 != null && n2 != -1) {
                forName.setMaxAllowed(n2);
            }
            return forName;
        }
        
        public void validateAll() throws InjectionValidationException {
            final Iterator<InjectorGroupInfo> iterator = ((HashMap<K, InjectorGroupInfo>)this).values().iterator();
            while (iterator.hasNext()) {
                iterator.next().validate();
            }
        }
        
        public InjectorGroupInfo forName(final String s) {
            InjectorGroupInfo value = super.get(s);
            if (value == null) {
                value = new InjectorGroupInfo(s);
                this.put(s, value);
            }
            return value;
        }
        
        public InjectorGroupInfo parseGroup(final MethodNode methodNode, final String s) {
            return this.parseGroup(Annotations.getInvisible(methodNode, Group.class), s);
        }
        
        @Override
        public InjectorGroupInfo get(final Object o) {
            return this.forName(o.toString());
        }
    }
}
