// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.HashMap;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import java.util.Map;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;

class Mappings implements IMappingConsumer
{
    private final /* synthetic */ Map<ObfuscationType, MappingSet<MappingField>> fieldMappings;
    private final /* synthetic */ Map<ObfuscationType, MappingSet<MappingMethod>> methodMappings;
    private /* synthetic */ UniqueMappings unique;
    
    public IMappingConsumer asUnique() {
        if (this.unique == null) {
            this.unique = new UniqueMappings(this);
        }
        return this.unique;
    }
    
    private void init() {
        for (final ObfuscationType obfuscationType : ObfuscationType.types()) {
            this.fieldMappings.put(obfuscationType, new MappingSet<MappingField>());
            this.methodMappings.put(obfuscationType, new MappingSet<MappingMethod>());
        }
    }
    
    @Override
    public MappingSet<MappingMethod> getMethodMappings(final ObfuscationType obfuscationType) {
        final MappingSet<MappingMethod> set = this.methodMappings.get(obfuscationType);
        return (set != null) ? set : new MappingSet<MappingMethod>();
    }
    
    @Override
    public void clear() {
        this.fieldMappings.clear();
        this.methodMappings.clear();
        if (this.unique != null) {
            this.unique.clearMaps();
        }
        this.init();
    }
    
    @Override
    public MappingSet<MappingField> getFieldMappings(final ObfuscationType obfuscationType) {
        final MappingSet<MappingField> set = this.fieldMappings.get(obfuscationType);
        return (set != null) ? set : new MappingSet<MappingField>();
    }
    
    @Override
    public void addFieldMapping(final ObfuscationType obfuscationType, final MappingField mappingField, final MappingField mappingField2) {
        MappingSet<MappingField> set = this.fieldMappings.get(obfuscationType);
        if (set == null) {
            set = new MappingSet<MappingField>();
            this.fieldMappings.put(obfuscationType, set);
        }
        set.add(new MappingSet.Pair<MappingField>(mappingField, mappingField2));
    }
    
    public Mappings() {
        this.fieldMappings = new HashMap<ObfuscationType, MappingSet<MappingField>>();
        this.methodMappings = new HashMap<ObfuscationType, MappingSet<MappingMethod>>();
        this.init();
    }
    
    @Override
    public void addMethodMapping(final ObfuscationType obfuscationType, final MappingMethod mappingMethod, final MappingMethod mappingMethod2) {
        MappingSet<MappingMethod> set = this.methodMappings.get(obfuscationType);
        if (set == null) {
            set = new MappingSet<MappingMethod>();
            this.methodMappings.put(obfuscationType, set);
        }
        set.add(new MappingSet.Pair<MappingMethod>(mappingMethod, mappingMethod2));
    }
    
    public static class MappingConflictException extends RuntimeException
    {
        private final /* synthetic */ IMapping<?> oldMapping;
        private final /* synthetic */ IMapping<?> newMapping;
        
        public IMapping<?> getNew() {
            return this.newMapping;
        }
        
        public MappingConflictException(final IMapping<?> oldMapping, final IMapping<?> newMapping) {
            this.oldMapping = oldMapping;
            this.newMapping = newMapping;
        }
        
        public IMapping<?> getOld() {
            return this.oldMapping;
        }
    }
    
    static class UniqueMappings implements IMappingConsumer
    {
        private final /* synthetic */ Map<ObfuscationType, Map<MappingField, MappingField>> fields;
        private final /* synthetic */ IMappingConsumer mappings;
        private final /* synthetic */ Map<ObfuscationType, Map<MappingMethod, MappingMethod>> methods;
        
        @Override
        public MappingSet<MappingMethod> getMethodMappings(final ObfuscationType obfuscationType) {
            return this.mappings.getMethodMappings(obfuscationType);
        }
        
        @Override
        public MappingSet<MappingField> getFieldMappings(final ObfuscationType obfuscationType) {
            return this.mappings.getFieldMappings(obfuscationType);
        }
        
        @Override
        public void addMethodMapping(final ObfuscationType obfuscationType, final MappingMethod mappingMethod, final MappingMethod mappingMethod2) {
            if (!this.checkForExistingMapping(obfuscationType, mappingMethod, mappingMethod2, this.methods)) {
                this.mappings.addMethodMapping(obfuscationType, mappingMethod, mappingMethod2);
            }
        }
        
        private <TMapping extends IMapping<TMapping>> boolean checkForExistingMapping(final ObfuscationType obfuscationType, final TMapping tMapping, final TMapping obj, final Map<ObfuscationType, Map<TMapping, TMapping>> map) throws MappingConflictException {
            Map<TMapping, TMapping> map2 = map.get(obfuscationType);
            if (map2 == null) {
                map2 = new HashMap<TMapping, TMapping>();
                map.put(obfuscationType, map2);
            }
            final IMapping<TMapping> mapping = map2.get(tMapping);
            if (mapping == null) {
                map2.put(tMapping, obj);
                return false;
            }
            if (mapping.equals(obj)) {
                return true;
            }
            throw new MappingConflictException(mapping, obj);
        }
        
        @Override
        public void clear() {
            this.clearMaps();
            this.mappings.clear();
        }
        
        public UniqueMappings(final IMappingConsumer mappings) {
            this.fields = new HashMap<ObfuscationType, Map<MappingField, MappingField>>();
            this.methods = new HashMap<ObfuscationType, Map<MappingMethod, MappingMethod>>();
            this.mappings = mappings;
        }
        
        @Override
        public void addFieldMapping(final ObfuscationType obfuscationType, final MappingField mappingField, final MappingField mappingField2) {
            if (!this.checkForExistingMapping(obfuscationType, mappingField, mappingField2, this.fields)) {
                this.mappings.addFieldMapping(obfuscationType, mappingField, mappingField2);
            }
        }
        
        protected void clearMaps() {
            this.fields.clear();
            this.methods.clear();
        }
    }
}
