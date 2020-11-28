// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;

public class ObfuscationDataProvider implements IObfuscationDataProvider
{
    private final /* synthetic */ IMixinAnnotationProcessor ap;
    private final /* synthetic */ List<ObfuscationEnvironment> environments;
    
    @Override
    public ObfuscationData<MappingField> getObfField(final MemberInfo memberInfo) {
        return this.getObfField(memberInfo.asFieldMapping());
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final MemberInfo memberInfo, final boolean b) {
        final ObfuscationData<MappingMethod> obfuscationData = new ObfuscationData<MappingMethod>();
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MappingMethod obfMethod = obfuscationEnvironment.getObfMethod(memberInfo);
            if (obfMethod != null) {
                obfuscationData.put(obfuscationEnvironment.getType(), obfMethod);
            }
        }
        if (!obfuscationData.isEmpty() || !b) {
            return obfuscationData;
        }
        return this.remapDescriptor(obfuscationData, memberInfo);
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethodRecursive(final MemberInfo memberInfo) {
        return this.getObfEntryRecursive(memberInfo);
    }
    
    private static <T> ObfuscationData<T> applyParents(final ObfuscationData<String> obfuscationData, final ObfuscationData<T> obfuscationData2) {
        for (final ObfuscationType obfuscationType : obfuscationData2) {
            obfuscationData2.put(obfuscationType, MemberInfo.fromMapping(obfuscationData2.get(obfuscationType)).move(obfuscationData.get(obfuscationType)).asMapping());
        }
        return obfuscationData2;
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final MemberInfo memberInfo) {
        return this.getRemappedMethod(memberInfo, memberInfo.isConstructor());
    }
    
    public ObfuscationData<MappingMethod> remapDescriptor(final ObfuscationData<MappingMethod> obfuscationData, final MemberInfo memberInfo) {
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MemberInfo remapDescriptor = obfuscationEnvironment.remapDescriptor(memberInfo);
            if (remapDescriptor != null) {
                obfuscationData.put(obfuscationEnvironment.getType(), remapDescriptor.asMethodMapping());
            }
        }
        return obfuscationData;
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final String s) {
        final ObfuscationData<String> obfuscationData = new ObfuscationData<String>(s);
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final String obfClass = obfuscationEnvironment.getObfClass(s);
            if (obfClass != null) {
                obfuscationData.put(obfuscationEnvironment.getType(), obfClass);
            }
        }
        return obfuscationData;
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod mappingMethod) {
        return this.getRemappedMethod(mappingMethod, true);
    }
    
    @Override
    public ObfuscationData<MappingField> getObfFieldRecursive(final MemberInfo memberInfo) {
        return this.getObfEntryRecursive(memberInfo);
    }
    
    private <T> ObfuscationData<T> getObfEntryUsing(final MemberInfo memberInfo, final TypeHandle typeHandle) {
        return (typeHandle == null) ? new ObfuscationData<T>() : this.getObfEntry(memberInfo.move(typeHandle.getName()));
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final IMapping<T> mapping) {
        if (mapping != null) {
            if (mapping.getType() == IMapping.Type.FIELD) {
                return (ObfuscationData<T>)this.getObfField((MappingField)mapping);
            }
            if (mapping.getType() == IMapping.Type.METHOD) {
                return (ObfuscationData<T>)this.getObfMethod((MappingMethod)mapping);
            }
        }
        return new ObfuscationData<T>();
    }
    
    public ObfuscationDataProvider(final IMixinAnnotationProcessor ap, final List<ObfuscationEnvironment> environments) {
        this.ap = ap;
        this.environments = environments;
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final MemberInfo memberInfo) {
        if (memberInfo.isField()) {
            return (ObfuscationData<T>)this.getObfField(memberInfo);
        }
        return (ObfuscationData<T>)this.getObfMethod(memberInfo.asMethodMapping());
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntryRecursive(final MemberInfo memberInfo) {
        MemberInfo move = memberInfo;
        final ObfuscationData<String> obfClass = this.getObfClass(move.owner);
        ObfuscationData<Object> obfuscationData = this.getObfEntry(move);
        try {
            while (obfuscationData.isEmpty()) {
                final TypeHandle typeHandle = this.ap.getTypeProvider().getTypeHandle(move.owner);
                if (typeHandle == null) {
                    return (ObfuscationData<T>)obfuscationData;
                }
                final TypeHandle superclass = typeHandle.getSuperclass();
                obfuscationData = this.getObfEntryUsing(move, superclass);
                if (!obfuscationData.isEmpty()) {
                    return applyParents(obfClass, (ObfuscationData<T>)obfuscationData);
                }
                final Iterator<TypeHandle> iterator = typeHandle.getInterfaces().iterator();
                while (iterator.hasNext()) {
                    obfuscationData = this.getObfEntryUsing(move, iterator.next());
                    if (!obfuscationData.isEmpty()) {
                        return applyParents(obfClass, (ObfuscationData<T>)obfuscationData);
                    }
                }
                if (superclass == null) {
                    break;
                }
                move = move.move(superclass.getName());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return (ObfuscationData<T>)this.getObfEntry(memberInfo);
        }
        return (ObfuscationData<T>)obfuscationData;
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod mappingMethod, final boolean b) {
        final ObfuscationData<MappingMethod> obfuscationData = new ObfuscationData<MappingMethod>();
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MappingMethod obfMethod = obfuscationEnvironment.getObfMethod(mappingMethod);
            if (obfMethod != null) {
                obfuscationData.put(obfuscationEnvironment.getType(), obfMethod);
            }
        }
        if (!obfuscationData.isEmpty() || !b) {
            return obfuscationData;
        }
        return this.remapDescriptor(obfuscationData, new MemberInfo(mappingMethod));
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final MappingMethod mappingMethod) {
        return this.getRemappedMethod(mappingMethod, mappingMethod.isConstructor());
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final TypeHandle typeHandle) {
        return this.getObfClass(typeHandle.getName());
    }
    
    @Override
    public ObfuscationData<MappingField> getObfField(final MappingField mappingField) {
        final ObfuscationData<MappingField> obfuscationData = new ObfuscationData<MappingField>();
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            MappingField mappingField2 = obfuscationEnvironment.getObfField(mappingField);
            if (mappingField2 != null) {
                if (mappingField2.getDesc() == null && mappingField.getDesc() != null) {
                    mappingField2 = mappingField2.transform(obfuscationEnvironment.remapDescriptor(mappingField.getDesc()));
                }
                obfuscationData.put(obfuscationEnvironment.getType(), mappingField2);
            }
        }
        return obfuscationData;
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final MemberInfo memberInfo) {
        return this.getRemappedMethod(memberInfo, true);
    }
}
