// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.refmap;

import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import com.google.common.io.Files;
import java.io.IOException;
import com.google.common.io.LineProcessor;
import com.google.common.base.Charsets;
import java.util.HashMap;
import java.io.File;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public final class RemappingReferenceMapper implements IReferenceMapper
{
    private final /* synthetic */ Map<String, String> mappings;
    private static final /* synthetic */ Logger logger;
    private static final /* synthetic */ Map<String, Map<String, String>> srgs;
    private final /* synthetic */ IReferenceMapper refMap;
    private final /* synthetic */ Map<String, Map<String, String>> cache;
    
    private static String getMappingEnv(final MixinEnvironment mixinEnvironment) {
        final String optionValue = mixinEnvironment.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_SOURCE_ENV);
        return Strings.isNullOrEmpty(optionValue) ? "searge" : optionValue;
    }
    
    private static boolean hasData(final MixinEnvironment mixinEnvironment) {
        final String resource = getResource(mixinEnvironment);
        return resource != null && new File(resource).exists();
    }
    
    @Override
    public String remapWithContext(final String s, final String s2, final String s3) {
        return this.refMap.remapWithContext(s, s2, s3);
    }
    
    private static Map<String, String> loadSrgs(final String pathname) {
        if (RemappingReferenceMapper.srgs.containsKey(pathname)) {
            return RemappingReferenceMapper.srgs.get(pathname);
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        RemappingReferenceMapper.srgs.put(pathname, hashMap);
        final File file = new File(pathname);
        if (!file.isFile()) {
            return hashMap;
        }
        try {
            Files.readLines(file, Charsets.UTF_8, (LineProcessor)new LineProcessor<Object>() {
                public Object getResult() {
                    return null;
                }
                
                public boolean processLine(final String s) throws IOException {
                    if (Strings.isNullOrEmpty(s) || s.startsWith("#")) {
                        return true;
                    }
                    final int n = 0;
                    int n3;
                    final int n2 = s.startsWith("MD: ") ? (n3 = 2) : (s.startsWith("FD: ") ? (n3 = 1) : (n3 = 0));
                    final int n4 = n3;
                    if (n2 > 0) {
                        final String[] split = s.substring(4).split(" ", 4);
                        hashMap.put(split[n].substring(split[n].lastIndexOf(47) + 1), split[n4].substring(split[n4].lastIndexOf(47) + 1));
                    }
                    return true;
                }
            });
        }
        catch (IOException ex) {
            RemappingReferenceMapper.logger.warn("Could not read input SRG file: {}", new Object[] { pathname });
            RemappingReferenceMapper.logger.catching((Throwable)ex);
        }
        return hashMap;
    }
    
    private Map<String, String> getCache(final String s) {
        Map<String, String> map = this.cache.get(s);
        if (map == null) {
            map = new HashMap<String, String>();
            this.cache.put(s, map);
        }
        return map;
    }
    
    static {
        DEFAULT_RESOURCE_PATH_PROPERTY = "net.minecraftforge.gradle.GradleStart.srg.srg-mcp";
        DEFAULT_MAPPING_ENV = "searge";
        logger = LogManager.getLogger("mixin");
        srgs = new HashMap<String, Map<String, String>>();
    }
    
    @Override
    public String getStatus() {
        return this.refMap.getStatus();
    }
    
    private static String getResource(final MixinEnvironment mixinEnvironment) {
        final String optionValue = mixinEnvironment.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_RESOURCE);
        return Strings.isNullOrEmpty(optionValue) ? System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") : optionValue;
    }
    
    @Override
    public boolean isDefault() {
        return this.refMap.isDefault();
    }
    
    private RemappingReferenceMapper(final MixinEnvironment mixinEnvironment, final IReferenceMapper refMap) {
        this.cache = new HashMap<String, Map<String, String>>();
        this.refMap = refMap;
        this.refMap.setContext(getMappingEnv(mixinEnvironment));
        final String resource = getResource(mixinEnvironment);
        this.mappings = loadSrgs(resource);
        RemappingReferenceMapper.logger.info("Remapping refMap {} using {}", new Object[] { refMap.getResourceName(), resource });
    }
    
    @Override
    public String getContext() {
        return this.refMap.getContext();
    }
    
    @Override
    public String remap(final String s, final String s2) {
        final Map<String, String> cache = this.getCache(s);
        String s3 = cache.get(s2);
        if (s3 == null) {
            s3 = this.refMap.remap(s, s2);
            for (final Map.Entry<String, String> entry : this.mappings.entrySet()) {
                s3 = s3.replace(entry.getKey(), entry.getValue());
            }
            cache.put(s2, s3);
        }
        return s3;
    }
    
    @Override
    public String getResourceName() {
        return this.refMap.getResourceName();
    }
    
    public static IReferenceMapper of(final MixinEnvironment mixinEnvironment, final IReferenceMapper referenceMapper) {
        if (!referenceMapper.isDefault() && hasData(mixinEnvironment)) {
            return new RemappingReferenceMapper(mixinEnvironment, referenceMapper);
        }
        return referenceMapper;
    }
    
    @Override
    public void setContext(final String s) {
    }
}
