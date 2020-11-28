// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import java.util.Iterator;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionPointException;
import java.util.regex.Matcher;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import com.google.common.base.Joiner;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Map;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class InjectionPointData
{
    private final /* synthetic */ IMixinContext context;
    private final /* synthetic */ InjectionPoint.Selector selector;
    private final /* synthetic */ String at;
    private final /* synthetic */ String id;
    private final /* synthetic */ String slice;
    private final /* synthetic */ String target;
    private final /* synthetic */ AnnotationNode parent;
    private final /* synthetic */ Map<String, String> args;
    private final /* synthetic */ int ordinal;
    private final /* synthetic */ MethodNode method;
    private final /* synthetic */ int opcode;
    private static final /* synthetic */ Pattern AT_PATTERN;
    private final /* synthetic */ String type;
    
    public AnnotationNode getParent() {
        return this.parent;
    }
    
    public MethodNode getMethod() {
        return this.method;
    }
    
    private static Pattern createPattern() {
        return Pattern.compile(String.format("^([^:]+):?(%s)?$", Joiner.on('|').join((Object[])InjectionPoint.Selector.values())));
    }
    
    public LocalVariableDiscriminator getLocalVariableDiscriminator() {
        return LocalVariableDiscriminator.parse(this.parent);
    }
    
    public int getOrdinal() {
        return this.ordinal;
    }
    
    public InjectionPointData(final IMixinContext context, final MethodNode method, final AnnotationNode parent, final String s, final List<String> list, final String target, final String s2, final int n, final int n2, final String id) {
        this.args = new HashMap<String, String>();
        this.context = context;
        this.method = method;
        this.parent = parent;
        this.at = s;
        this.target = target;
        this.slice = Strings.nullToEmpty(s2);
        this.ordinal = Math.max(-1, n);
        this.opcode = n2;
        this.id = id;
        this.parseArgs(list);
        this.args.put("target", target);
        this.args.put("ordinal", String.valueOf(n));
        this.args.put("opcode", String.valueOf(n2));
        final Matcher matcher = InjectionPointData.AT_PATTERN.matcher(s);
        this.type = parseType(matcher, s);
        this.selector = parseSelector(matcher);
    }
    
    public static String parseType(final String input) {
        return parseType(InjectionPointData.AT_PATTERN.matcher(input), input);
    }
    
    public int get(final String s, final int i) {
        return parseInt(this.get(s, String.valueOf(i)), i);
    }
    
    public String getSlice() {
        return this.slice;
    }
    
    private static InjectionPoint.Selector parseSelector(final Matcher matcher) {
        return (matcher.matches() && matcher.group(2) != null) ? InjectionPoint.Selector.valueOf(matcher.group(2)) : InjectionPoint.Selector.DEFAULT;
    }
    
    private static String parseType(final Matcher matcher, final String s) {
        return matcher.matches() ? matcher.group(1) : s;
    }
    
    public boolean get(final String s, final boolean b) {
        return parseBoolean(this.get(s, String.valueOf(b)), b);
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public String getAt() {
        return this.at;
    }
    
    public MemberInfo get(final String s) {
        try {
            return MemberInfo.parseAndValidate(this.get(s, ""), this.context);
        }
        catch (InvalidMemberDescriptorException ex) {
            throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\").%s descriptor \"%s\" on %s", new Object[] { this.at, s, this.target, InjectionInfo.describeInjector(this.context, this.parent, this.method) });
        }
    }
    
    public MemberInfo getTarget() {
        try {
            return MemberInfo.parseAndValidate(this.target, this.context);
        }
        catch (InvalidMemberDescriptorException ex) {
            throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\") descriptor \"%s\" on %s", new Object[] { this.at, this.target, InjectionInfo.describeInjector(this.context, this.parent, this.method) });
        }
    }
    
    public int getOpcode(final int n) {
        return (this.opcode > 0) ? this.opcode : n;
    }
    
    private static boolean parseBoolean(final String s, final boolean b) {
        try {
            return Boolean.parseBoolean(s);
        }
        catch (Exception ex) {
            return b;
        }
    }
    
    private static int parseInt(final String s, final int n) {
        try {
            return Integer.parseInt(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    @Override
    public String toString() {
        return this.type;
    }
    
    public InjectionPoint.Selector getSelector() {
        return this.selector;
    }
    
    public Type getMethodReturnType() {
        return Type.getReturnType(this.method.desc);
    }
    
    public String getType() {
        return this.type;
    }
    
    public String get(final String s, final String s2) {
        final String s3 = this.args.get(s);
        return (s3 != null) ? s3 : s2;
    }
    
    private void parseArgs(final List<String> list) {
        if (list == null) {
            return;
        }
        for (final String s : list) {
            if (s != null) {
                final int index = s.indexOf(61);
                if (index > -1) {
                    this.args.put(s.substring(0, index), s.substring(index + 1));
                }
                else {
                    this.args.put(s, "");
                }
            }
        }
    }
    
    public int getOpcode(final int n, final int... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (this.opcode == array[i]) {
                return this.opcode;
            }
        }
        return n;
    }
    
    public IMixinContext getContext() {
        return this.context;
    }
    
    static {
        AT_PATTERN = createPattern();
    }
}
