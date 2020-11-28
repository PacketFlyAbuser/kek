// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.util.Counter;
import java.util.Map;
import java.util.List;

public class MethodMapper
{
    private static final /* synthetic */ List<String> classes;
    private static final /* synthetic */ Map<String, Counter> methods;
    private static final /* synthetic */ Logger logger;
    private final /* synthetic */ ClassInfo info;
    
    private static String getClassUID(final String s) {
        int n = MethodMapper.classes.indexOf(s);
        if (n < 0) {
            n = MethodMapper.classes.size();
            MethodMapper.classes.add(s);
        }
        return finagle(n);
    }
    
    public MethodMapper(final MixinEnvironment mixinEnvironment, final ClassInfo info) {
        this.info = info;
    }
    
    public void remapHandlerMethod(final MixinInfo mixinInfo, final MethodNode methodNode, final ClassInfo.Method method) {
        if (!(methodNode instanceof MixinInfo.MixinMethodNode) || !((MixinInfo.MixinMethodNode)methodNode).isInjector()) {
            return;
        }
        if (method.isUnique()) {
            MethodMapper.logger.warn("Redundant @Unique on injector method {} in {}. Injectors are implicitly unique", new Object[] { method, mixinInfo });
        }
        if (method.isRenamed()) {
            methodNode.name = method.getName();
            return;
        }
        methodNode.name = method.renameTo(this.getHandlerName((MixinInfo.MixinMethodNode)methodNode));
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        classes = new ArrayList<String>();
        methods = new HashMap<String, Counter>();
    }
    
    public ClassInfo getClassInfo() {
        return this.info;
    }
    
    public String getHandlerName(final MixinInfo.MixinMethodNode mixinMethodNode) {
        return String.format("%s$%s$%s%s", InjectionInfo.getInjectorPrefix(mixinMethodNode.getInjectorAnnotation()), mixinMethodNode.name, getClassUID(mixinMethodNode.getOwner().getClassRef()), getMethodUID(mixinMethodNode.name, mixinMethodNode.desc, !mixinMethodNode.isSurrogate()));
    }
    
    private static String finagle(final int i) {
        final String hexString = Integer.toHexString(i);
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < hexString.length(); ++j) {
            final char char1 = hexString.charAt(j);
            sb.append((char)(char1 + ((char1 < ':') ? 49 : 10)));
        }
        return Strings.padStart(sb.toString(), 3, 'z');
    }
    
    private static String getMethodUID(final String s, final String s2, final boolean b) {
        final String format = String.format("%s%s", s, s2);
        Counter counter = MethodMapper.methods.get(format);
        if (counter == null) {
            counter = new Counter();
            MethodMapper.methods.put(format, counter);
        }
        else if (b) {
            final Counter counter2 = counter;
            ++counter2.value;
        }
        return String.format("%03x", counter.value);
    }
}
