// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.io.IOException;
import java.io.File;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.Overwrite;
import javax.annotation.processing.RoundEnvironment;
import com.google.common.collect.ImmutableList;
import org.spongepowered.tools.obfuscation.validation.TargetValidator;
import org.spongepowered.tools.obfuscation.validation.ParentValidator;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import org.spongepowered.tools.obfuscation.mirror.TypeHandleSimulated;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.io.InputStream;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.HashMap;
import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.mixin.gen.Invoker;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import java.util.Properties;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import java.util.List;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Map;
import org.spongepowered.tools.obfuscation.interfaces.IJavadocProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;

final class AnnotatedMixins implements ITypeHandleProvider, ITokenProvider, IMixinAnnotationProcessor, IJavadocProvider
{
    private final /* synthetic */ Map<String, Integer> tokenCache;
    private static /* synthetic */ Map<ProcessingEnvironment, AnnotatedMixins> instances;
    private final /* synthetic */ TargetMap targets;
    private final /* synthetic */ List<IMixinValidator> validators;
    private final /* synthetic */ List<AnnotatedMixin> mixinsForPass;
    private final /* synthetic */ CompilerEnvironment env;
    private final /* synthetic */ ProcessingEnvironment processingEnv;
    private final /* synthetic */ Map<String, AnnotatedMixin> mixins;
    private /* synthetic */ Properties properties;
    private final /* synthetic */ IObfuscationManager obf;
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationMirror annotationMirror, final AnnotationValue annotationValue) {
        this.processingEnv.getMessager().printMessage(kind, charSequence, element, annotationMirror, annotationValue);
    }
    
    public void registerInvoker(final TypeElement typeElement, final ExecutableElement executableElement) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", executableElement);
            return;
        }
        final AnnotationHandle of = AnnotationHandle.of(executableElement, Invoker.class);
        mixin.registerInvoker(executableElement, of, this.shouldRemap(mixin, of));
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence charSequence) {
        if (this.env == CompilerEnvironment.JAVAC || kind != Diagnostic.Kind.NOTE) {
            this.processingEnv.getMessager().printMessage(kind, charSequence);
        }
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeMirror typeMirror) {
        return this.getMixinsTargeting((TypeElement)((DeclaredType)typeMirror).asElement());
    }
    
    @Override
    public ITokenProvider getTokenProvider() {
        return this;
    }
    
    static {
        MAPID_SYSTEM_PROPERTY = "mixin.target.mapid";
        AnnotatedMixins.instances = new HashMap<ProcessingEnvironment, AnnotatedMixins>();
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationMirror annotationMirror) {
        this.processingEnv.getMessager().printMessage(kind, charSequence, element, annotationMirror);
    }
    
    @Override
    public IObfuscationManager getObfuscationManager() {
        return this.obf;
    }
    
    @Override
    public IJavadocProvider getJavadocProvider() {
        return this;
    }
    
    public void registerMixin(final TypeElement typeElement) {
        final String string = typeElement.getQualifiedName().toString();
        if (!this.mixins.containsKey(string)) {
            final AnnotatedMixin annotatedMixin = new AnnotatedMixin(this, typeElement);
            this.targets.registerTargets(annotatedMixin);
            annotatedMixin.runValidators(IMixinValidator.ValidationPass.EARLY, this.validators);
            this.mixins.put(string, annotatedMixin);
            this.mixinsForPass.add(annotatedMixin);
        }
    }
    
    @Override
    public TypeHandle getTypeHandle(String replace) {
        replace = replace.replace('/', '.');
        final Elements elementUtils = this.processingEnv.getElementUtils();
        final TypeElement typeElement = elementUtils.getTypeElement(replace);
        if (typeElement != null) {
            try {
                return new TypeHandle(typeElement);
            }
            catch (NullPointerException ex) {}
        }
        final int lastIndex = replace.lastIndexOf(46);
        if (lastIndex > -1) {
            final PackageElement packageElement = elementUtils.getPackageElement(replace.substring(0, lastIndex));
            if (packageElement != null) {
                return new TypeHandle(packageElement, replace);
            }
        }
        return null;
    }
    
    @Override
    public String getJavadoc(final Element element) {
        return this.processingEnv.getElementUtils().getDocComment(element);
    }
    
    public Properties getProperties() {
        if (this.properties == null) {
            this.properties = new Properties();
            try {
                final FileObject resource = this.processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, "", "mixin.properties");
                if (resource != null) {
                    final InputStream openInputStream = resource.openInputStream();
                    this.properties.load(openInputStream);
                    openInputStream.close();
                }
            }
            catch (Exception ex) {}
        }
        return this.properties;
    }
    
    public void registerSoftImplements(final TypeElement typeElement, final AnnotationHandle annotationHandle) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Implements annotation on a non-mixin class");
            return;
        }
        mixin.registerSoftImplements(annotationHandle);
    }
    
    private CompilerEnvironment detectEnvironment(final ProcessingEnvironment processingEnvironment) {
        if (processingEnvironment.getClass().getName().contains("jdt")) {
            return CompilerEnvironment.JDT;
        }
        return CompilerEnvironment.JAVAC;
    }
    
    @Override
    public CompilerEnvironment getCompilerEnvironment() {
        return this.env;
    }
    
    public void writeReferences() {
        this.obf.writeReferences();
    }
    
    public void registerAccessor(final TypeElement typeElement, final ExecutableElement executableElement) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Accessor annotation on a non-mixin method", executableElement);
            return;
        }
        final AnnotationHandle of = AnnotationHandle.of(executableElement, Accessor.class);
        mixin.registerAccessor(executableElement, of, this.shouldRemap(mixin, of));
    }
    
    @Override
    public TypeHandle getSimulatedHandle(String replace, final TypeMirror typeMirror) {
        replace = replace.replace('/', '.');
        final int lastIndex = replace.lastIndexOf(46);
        if (lastIndex > -1) {
            final PackageElement packageElement = this.processingEnv.getElementUtils().getPackageElement(replace.substring(0, lastIndex));
            if (packageElement != null) {
                return new TypeHandleSimulated(packageElement, replace, typeMirror);
            }
        }
        return new TypeHandleSimulated(replace, typeMirror);
    }
    
    public void registerInjector(final TypeElement typeElement, final ExecutableElement executableElement, final AnnotationHandle obj) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found " + obj + " annotation on a non-mixin method", executableElement);
            return;
        }
        final InjectorRemap injectorRemap = new InjectorRemap(this.shouldRemap(mixin, obj));
        mixin.registerInjector(executableElement, obj, injectorRemap);
        injectorRemap.dispatchPendingMessages(this);
    }
    
    @Override
    public Integer getToken(final String s) {
        if (this.tokenCache.containsKey(s)) {
            return this.tokenCache.get(s);
        }
        final String option = this.getOption(s);
        Integer value = null;
        try {
            value = Integer.parseInt(option);
        }
        catch (Exception ex) {}
        this.tokenCache.put(s, value);
        return value;
    }
    
    public AnnotatedMixin getMixin(final TypeElement typeElement) {
        return this.getMixin(typeElement.getQualifiedName().toString());
    }
    
    @Override
    public ITypeHandleProvider getTypeProvider() {
        return this;
    }
    
    private boolean shouldRemap(final AnnotatedMixin annotatedMixin, final AnnotationHandle annotationHandle) {
        return annotationHandle.getBoolean("remap", annotatedMixin.remap());
    }
    
    private void initTokenCache(final String s) {
        if (s != null) {
            final Pattern compile = Pattern.compile("^([A-Z0-9\\-_\\.]+)=([0-9]+)$");
            final String[] split = s.replaceAll("\\s", "").toUpperCase().split("[;,]");
            for (int length = split.length, i = 0; i < length; ++i) {
                final Matcher matcher = compile.matcher(split[i]);
                if (matcher.matches()) {
                    this.tokenCache.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
                }
            }
        }
    }
    
    @Override
    public ProcessingEnvironment getProcessingEnvironment() {
        return this.processingEnv;
    }
    
    public void writeMappings() {
        this.obf.writeMappings();
    }
    
    public void clear() {
        this.mixins.clear();
    }
    
    @Override
    public void printMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element) {
        this.processingEnv.getMessager().printMessage(kind, charSequence, element);
    }
    
    public static AnnotatedMixins getMixinsForEnvironment(final ProcessingEnvironment processingEnvironment) {
        AnnotatedMixins annotatedMixins = AnnotatedMixins.instances.get(processingEnvironment);
        if (annotatedMixins == null) {
            annotatedMixins = new AnnotatedMixins(processingEnvironment);
            AnnotatedMixins.instances.put(processingEnvironment, annotatedMixins);
        }
        return annotatedMixins;
    }
    
    public Collection<TypeMirror> getMixinsTargeting(final TypeElement typeElement) {
        final ArrayList<TypeMirror> list = new ArrayList<TypeMirror>();
        final Iterator<TypeReference> iterator = this.targets.getMixinsTargeting(typeElement).iterator();
        while (iterator.hasNext()) {
            final TypeHandle handle = iterator.next().getHandle(this.processingEnv);
            if (handle != null) {
                list.add(handle.getType());
            }
        }
        return list;
    }
    
    public AnnotatedMixin getMixin(final String s) {
        return this.mixins.get(s);
    }
    
    private AnnotatedMixins(final ProcessingEnvironment processingEnv) {
        this.mixins = new HashMap<String, AnnotatedMixin>();
        this.mixinsForPass = new ArrayList<AnnotatedMixin>();
        this.tokenCache = new HashMap<String, Integer>();
        this.env = this.detectEnvironment(processingEnv);
        this.processingEnv = processingEnv;
        this.printMessage(Diagnostic.Kind.NOTE, "SpongePowered MIXIN Annotation Processor Version=0.7.4");
        this.targets = this.initTargetMap();
        this.obf = new ObfuscationManager(this);
        this.obf.init();
        this.validators = (List<IMixinValidator>)ImmutableList.of((Object)new ParentValidator(this), (Object)new TargetValidator(this));
        this.initTokenCache(this.getOption("tokens"));
    }
    
    public void registerShadow(final TypeElement typeElement, final ExecutableElement executableElement, final AnnotationHandle annotationHandle) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin method", executableElement);
            return;
        }
        mixin.registerShadow(executableElement, annotationHandle, this.shouldRemap(mixin, annotationHandle));
    }
    
    @Override
    public String getOption(final String key) {
        if (key == null) {
            return null;
        }
        final String s = this.processingEnv.getOptions().get(key);
        if (s != null) {
            return s;
        }
        return this.getProperties().getProperty(key);
    }
    
    public void onPassCompleted(final RoundEnvironment roundEnvironment) {
        if (!"true".equalsIgnoreCase(this.getOption("disableTargetExport"))) {
            this.targets.write(true);
        }
        final Iterator<AnnotatedMixin> iterator = (roundEnvironment.processingOver() ? this.mixins.values() : this.mixinsForPass).iterator();
        while (iterator.hasNext()) {
            iterator.next().runValidators(roundEnvironment.processingOver() ? IMixinValidator.ValidationPass.FINAL : IMixinValidator.ValidationPass.LATE, this.validators);
        }
    }
    
    public void registerOverwrite(final TypeElement typeElement, final ExecutableElement executableElement) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Overwrite annotation on a non-mixin method", executableElement);
            return;
        }
        final AnnotationHandle of = AnnotationHandle.of(executableElement, Overwrite.class);
        mixin.registerOverwrite(executableElement, of, this.shouldRemap(mixin, of));
    }
    
    public void onPassStarted() {
        this.mixinsForPass.clear();
    }
    
    public void registerShadow(final TypeElement typeElement, final VariableElement variableElement, final AnnotationHandle annotationHandle) {
        final AnnotatedMixin mixin = this.getMixin(typeElement);
        if (mixin == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Found @Shadow annotation on a non-mixin field", variableElement);
            return;
        }
        mixin.registerShadow(variableElement, annotationHandle, this.shouldRemap(mixin, annotationHandle));
    }
    
    protected TargetMap initTargetMap() {
        final TargetMap create = TargetMap.create(System.getProperty("mixin.target.mapid"));
        System.setProperty("mixin.target.mapid", create.getSessionId());
        final String option = this.getOption("dependencyTargetsFile");
        if (option != null) {
            try {
                create.readImports(new File(option));
            }
            catch (IOException ex) {
                this.printMessage(Diagnostic.Kind.WARNING, "Could not read from specified imports file: " + option);
            }
        }
        return create;
    }
}
