// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.agent;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.service.IMixinService;
import java.lang.instrument.ClassDefinition;
import org.spongepowered.asm.service.MixinService;
import java.lang.instrument.ClassFileTransformer;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import java.lang.instrument.Instrumentation;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;

public class MixinAgent implements IHotSwap
{
    static /* synthetic */ Instrumentation instrumentation;
    final /* synthetic */ MixinTransformer classTransformer;
    static final /* synthetic */ Logger logger;
    static final /* synthetic */ MixinAgentClassLoader classLoader;
    private static /* synthetic */ List<MixinAgent> agents;
    public static final /* synthetic */ byte[] ERROR_BYTECODE;
    
    public static void init(final Instrumentation instrumentation) {
        MixinAgent.instrumentation = instrumentation;
        if (!MixinAgent.instrumentation.isRedefineClassesSupported()) {
            MixinAgent.logger.error("The instrumentation doesn't support re-definition of classes");
        }
        final Iterator<MixinAgent> iterator = MixinAgent.agents.iterator();
        while (iterator.hasNext()) {
            iterator.next().initTransformer();
        }
    }
    
    public MixinAgent(final MixinTransformer classTransformer) {
        this.classTransformer = classTransformer;
        MixinAgent.agents.add(this);
        if (MixinAgent.instrumentation != null) {
            this.initTransformer();
        }
    }
    
    public static void premain(final String s, final Instrumentation instrumentation) {
        System.setProperty("mixin.hotSwap", "true");
        init(instrumentation);
    }
    
    @Override
    public void registerMixinClass(final String s) {
        MixinAgent.classLoader.addMixinClass(s);
    }
    
    public static void agentmain(final String s, final Instrumentation instrumentation) {
        init(instrumentation);
    }
    
    static {
        ERROR_BYTECODE = new byte[] { 1 };
        classLoader = new MixinAgentClassLoader();
        logger = LogManager.getLogger("mixin.agent");
        MixinAgent.instrumentation = null;
        MixinAgent.agents = new ArrayList<MixinAgent>();
    }
    
    private void initTransformer() {
        MixinAgent.instrumentation.addTransformer(new Transformer(), true);
    }
    
    @Override
    public void registerTargetClass(final String s, final byte[] array) {
        MixinAgent.classLoader.addTargetClass(s, array);
    }
    
    class Transformer implements ClassFileTransformer
    {
        private boolean reApplyMixins(final List<String> list) {
            final IMixinService service = MixinService.getService();
            for (final String str : list) {
                final String replace = str.replace('/', '.');
                MixinAgent.logger.debug("Re-transforming target class {}", new Object[] { str });
                try {
                    final Class<?> class1 = service.getClassProvider().findClass(replace);
                    final byte[] originalTargetBytecode = MixinAgent.classLoader.getOriginalTargetBytecode(replace);
                    if (originalTargetBytecode == null) {
                        MixinAgent.logger.error("Target class {} bytecode is not registered", new Object[] { replace });
                        return false;
                    }
                    MixinAgent.instrumentation.redefineClasses(new ClassDefinition(class1, MixinAgent.this.classTransformer.transformClassBytes(null, replace, originalTargetBytecode)));
                }
                catch (Throwable t) {
                    MixinAgent.logger.error("Error while re-transforming target class " + str, t);
                    return false;
                }
            }
            return true;
        }
        
        private List<String> reloadMixin(final String str, final byte[] array) {
            MixinAgent.logger.info("Redefining mixin {}", new Object[] { str });
            try {
                return MixinAgent.this.classTransformer.reload(str.replace('/', '.'), array);
            }
            catch (MixinReloadException ex) {
                MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[] { ex.getMixinInfo(), ex.getMessage() });
            }
            catch (Throwable t) {
                MixinAgent.logger.error("Error while finding targets for mixin " + str, t);
            }
            return null;
        }
        
        @Override
        public byte[] transform(final ClassLoader classLoader, final String s, final Class<?> clazz, final ProtectionDomain protectionDomain, final byte[] array) throws IllegalClassFormatException {
            if (clazz == null) {
                return null;
            }
            final byte[] fakeMixinBytecode = MixinAgent.classLoader.getFakeMixinBytecode(clazz);
            if (fakeMixinBytecode != null) {
                final List<String> reloadMixin = this.reloadMixin(s, array);
                if (reloadMixin == null || !this.reApplyMixins(reloadMixin)) {
                    return MixinAgent.ERROR_BYTECODE;
                }
                return fakeMixinBytecode;
            }
            else {
                try {
                    MixinAgent.logger.info("Redefining class " + s);
                    return MixinAgent.this.classTransformer.transformClassBytes(null, s, array);
                }
                catch (Throwable t) {
                    MixinAgent.logger.error("Error while re-transforming class " + s, t);
                    return MixinAgent.ERROR_BYTECODE;
                }
            }
        }
    }
}
