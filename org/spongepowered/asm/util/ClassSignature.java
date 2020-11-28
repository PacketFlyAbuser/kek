// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

import org.spongepowered.asm.lib.signature.SignatureWriter;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Deque;

public class ClassSignature
{
    private final /* synthetic */ Deque<String> rawInterfaces;
    private /* synthetic */ Token superClass;
    private final /* synthetic */ Map<TypeVar, TokenHandle> types;
    private final /* synthetic */ List<Token> interfaces;
    
    private String findOffsetName(final char c, final Set<String> set) {
        return this.findOffsetName(c, set, "", "");
    }
    
    public static ClassSignature ofLazy(final ClassNode classNode) {
        if (classNode.signature != null) {
            return new Lazy(classNode.signature);
        }
        return generate(classNode);
    }
    
    private ClassSignature read(final String s) {
        if (s != null) {
            try {
                new SignatureReader(s).accept(new SignatureParser());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }
    
    protected TokenHandle getType(final String s) {
        for (final TypeVar typeVar : this.types.keySet()) {
            if (typeVar.matches(s)) {
                return this.types.get(typeVar);
            }
        }
        final TokenHandle tokenHandle = new TokenHandle();
        this.types.put(new TypeVar(s), tokenHandle);
        return tokenHandle;
    }
    
    public ClassSignature wake() {
        return this;
    }
    
    private void conform(final Set<String> set) {
        for (final TypeVar typeVar : this.types.keySet()) {
            final String uniqueName = this.findUniqueName(typeVar.getOriginalName(), set);
            typeVar.rename(uniqueName);
            set.add(uniqueName);
        }
    }
    
    public static ClassSignature of(final String s) {
        return new ClassSignature().read(s);
    }
    
    private String findUniqueName(final String s, final Set<String> set) {
        if (!set.contains(s)) {
            return s;
        }
        if (s.length() == 1) {
            final String offsetName = this.findOffsetName(s.charAt(0), set);
            if (offsetName != null) {
                return offsetName;
            }
        }
        final String offsetName2 = this.findOffsetName('T', set, "", s);
        if (offsetName2 != null) {
            return offsetName2;
        }
        final String offsetName3 = this.findOffsetName('T', set, s, "");
        if (offsetName3 != null) {
            return offsetName3;
        }
        final String offsetName4 = this.findOffsetName('T', set, "T", s);
        if (offsetName4 != null) {
            return offsetName4;
        }
        final String offsetName5 = this.findOffsetName('T', set, "", s + "Type");
        if (offsetName5 != null) {
            return offsetName5;
        }
        throw new IllegalStateException("Failed to conform type var: " + s);
    }
    
    protected void addTypeVar(final TypeVar obj, final TokenHandle tokenHandle) throws IllegalArgumentException {
        if (this.types.containsKey(obj)) {
            throw new IllegalArgumentException("TypeVar " + obj + " is already present on " + this);
        }
        this.types.put(obj, tokenHandle);
    }
    
    protected String getTypeVar(final TokenHandle tokenHandle) {
        for (final Map.Entry<TypeVar, TokenHandle> entry : this.types.entrySet()) {
            final TypeVar obj = entry.getKey();
            final TokenHandle tokenHandle2 = entry.getValue();
            if (tokenHandle == tokenHandle2 || tokenHandle.asToken() == tokenHandle2.asToken()) {
                return "T" + obj + ";";
            }
        }
        return tokenHandle.token.asType();
    }
    
    protected void setSuperClass(final Token superClass) {
        this.superClass = superClass;
    }
    
    private String findOffsetName(final char c, final Set<String> set, final String s, final String s2) {
        final String format = String.format("%s%s%s", s, c, s2);
        if (!set.contains(format)) {
            return format;
        }
        if (c > '@' && c < '[') {
            for (int n = c - '@'; n + 65 != c; n = ++n % 26) {
                final String format2 = String.format("%s%s%s", s, (char)(n + 65), s2);
                if (!set.contains(format2)) {
                    return format2;
                }
            }
        }
        return null;
    }
    
    public void addInterface(final String s) {
        this.rawInterfaces.add(s);
    }
    
    public SignatureVisitor getRemapper() {
        return new SignatureRemapper();
    }
    
    protected TypeVar getTypeVar(final String s) {
        for (final TypeVar typeVar : this.types.keySet()) {
            if (typeVar.matches(s)) {
                return typeVar;
            }
        }
        return null;
    }
    
    ClassSignature() {
        this.types = new LinkedHashMap<TypeVar, TokenHandle>();
        this.superClass = new Token("java/lang/Object");
        this.interfaces = new ArrayList<Token>();
        this.rawInterfaces = new LinkedList<String>();
    }
    
    protected void addRawInterface(final String s) {
        final Token token = new Token(s);
        final String type = token.asType(true);
        final Iterator<Token> iterator = this.interfaces.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().asType(true).equals(type)) {
                return;
            }
        }
        this.interfaces.add(token);
    }
    
    public static ClassSignature of(final ClassNode classNode) {
        if (classNode.signature != null) {
            return of(classNode.signature);
        }
        return generate(classNode);
    }
    
    @Override
    public String toString() {
        while (this.rawInterfaces.size() > 0) {
            this.addRawInterface(this.rawInterfaces.remove());
        }
        final StringBuilder sb = new StringBuilder();
        if (this.types.size() > 0) {
            boolean b = false;
            final StringBuilder s = new StringBuilder();
            for (final Map.Entry<TypeVar, TokenHandle> entry : this.types.entrySet()) {
                final String bound = entry.getValue().asBound();
                if (!bound.isEmpty()) {
                    s.append(entry.getKey()).append(':').append(bound);
                    b = true;
                }
            }
            if (b) {
                sb.append('<').append((CharSequence)s).append('>');
            }
        }
        sb.append(this.superClass.asType());
        final Iterator<Token> iterator2 = this.interfaces.iterator();
        while (iterator2.hasNext()) {
            sb.append(iterator2.next().asType());
        }
        return sb.toString();
    }
    
    protected void addInterface(final Token token) {
        if (!token.isRaw()) {
            final String type = token.asType(true);
            final ListIterator<Token> listIterator = this.interfaces.listIterator();
            while (listIterator.hasNext()) {
                final Token token2 = listIterator.next();
                if (token2.isRaw() && token2.asType(true).equals(type)) {
                    listIterator.set(token);
                    return;
                }
            }
        }
        this.interfaces.add(token);
    }
    
    static {
        OBJECT = "java/lang/Object";
    }
    
    private static ClassSignature generate(final ClassNode classNode) {
        final ClassSignature classSignature = new ClassSignature();
        classSignature.setSuperClass(new Token((classNode.superName != null) ? classNode.superName : "java/lang/Object"));
        final Iterator<String> iterator = classNode.interfaces.iterator();
        while (iterator.hasNext()) {
            classSignature.addInterface(new Token(iterator.next()));
        }
        return classSignature;
    }
    
    public void merge(final ClassSignature classSignature) {
        try {
            final HashSet<String> set = new HashSet<String>();
            final Iterator<TypeVar> iterator = this.types.keySet().iterator();
            while (iterator.hasNext()) {
                set.add(iterator.next().toString());
            }
            classSignature.conform(set);
        }
        catch (IllegalStateException ex) {
            ex.printStackTrace();
            return;
        }
        for (final Map.Entry<TypeVar, TokenHandle> entry : classSignature.types.entrySet()) {
            this.addTypeVar(entry.getKey(), entry.getValue());
        }
        final Iterator<Token> iterator3 = classSignature.interfaces.iterator();
        while (iterator3.hasNext()) {
            this.addInterface(iterator3.next());
        }
    }
    
    public String getSuperClass() {
        return this.superClass.asType(true);
    }
    
    class TokenHandle implements IToken
    {
        /* synthetic */ boolean array;
        /* synthetic */ char wildcard;
        final /* synthetic */ Token token;
        
        TokenHandle(final ClassSignature classSignature) {
            this(classSignature, new Token());
        }
        
        @Override
        public IToken setArray(final boolean b) {
            this.array |= b;
            return this;
        }
        
        @Override
        public String asBound() {
            return this.token.asBound();
        }
        
        public TokenHandle clone() {
            return new TokenHandle(this.token);
        }
        
        @Override
        public String toString() {
            return this.token.toString();
        }
        
        @Override
        public Token asToken() {
            return this.token;
        }
        
        @Override
        public String asType() {
            final StringBuilder sb = new StringBuilder();
            if (this.wildcard > '\0') {
                sb.append(this.wildcard);
            }
            if (this.array) {
                sb.append('[');
            }
            return sb.append(ClassSignature.this.getTypeVar(this)).toString();
        }
        
        @Override
        public IToken setWildcard(final char c) {
            if ("+-".indexOf(c) > -1) {
                this.wildcard = c;
            }
            return this;
        }
        
        TokenHandle(final Token token) {
            this.token = token;
        }
    }
    
    interface IToken
    {
        IToken setArray(final boolean p0);
        
        Token asToken();
        
        String asType();
        
        IToken setWildcard(final char p0);
        
        String asBound();
    }
    
    static class Token implements IToken
    {
        private /* synthetic */ boolean array;
        private /* synthetic */ String type;
        private /* synthetic */ List<IToken> signature;
        private /* synthetic */ Token tail;
        private /* synthetic */ List<Token> classBound;
        private final /* synthetic */ boolean inner;
        private /* synthetic */ List<Token> ifaceBound;
        private /* synthetic */ char symbol;
        private /* synthetic */ List<IToken> suffix;
        
        IToken addTypeArgument(final char c) {
            if (this.tail != null) {
                return this.tail.addTypeArgument(c);
            }
            final Token token = new Token(c);
            this.getSignature().add(token);
            return token;
        }
        
        @Override
        public String asBound() {
            final StringBuilder sb = new StringBuilder();
            if (this.type != null) {
                sb.append(this.type);
            }
            if (this.classBound != null) {
                final Iterator<Token> iterator = this.classBound.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().asType());
                }
            }
            if (this.ifaceBound != null) {
                final Iterator<Token> iterator2 = this.ifaceBound.iterator();
                while (iterator2.hasNext()) {
                    sb.append(':').append(iterator2.next().asType());
                }
            }
            return sb.toString();
        }
        
        @Override
        public String asType() {
            return this.asType(false);
        }
        
        private List<IToken> getSignature() {
            if (this.signature == null) {
                this.signature = new ArrayList<IToken>();
            }
            return this.signature;
        }
        
        boolean isRaw() {
            return this.signature == null;
        }
        
        Token setSymbol(final char c) {
            if (this.symbol == '\0' && "+-*".indexOf(c) > -1) {
                this.symbol = c;
            }
            return this;
        }
        
        IToken addTypeArgument(final String s) {
            if (this.tail != null) {
                return this.tail.addTypeArgument(s);
            }
            final Token token = new Token(s);
            this.getSignature().add(token);
            return token;
        }
        
        Token(final char symbol) {
            this();
            this.symbol = symbol;
        }
        
        public String asType(final boolean b) {
            final StringBuilder sb = new StringBuilder();
            if (this.array) {
                sb.append('[');
            }
            if (this.symbol != '\0') {
                sb.append(this.symbol);
            }
            if (this.type == null) {
                return sb.toString();
            }
            if (!this.inner) {
                sb.append('L');
            }
            sb.append(this.type);
            if (!b) {
                if (this.signature != null) {
                    sb.append('<');
                    final Iterator<IToken> iterator = this.signature.iterator();
                    while (iterator.hasNext()) {
                        sb.append(iterator.next().asType());
                    }
                    sb.append('>');
                }
                if (this.suffix != null) {
                    final Iterator<IToken> iterator2 = this.suffix.iterator();
                    while (iterator2.hasNext()) {
                        sb.append('.').append(iterator2.next().asType());
                    }
                }
            }
            if (!this.inner) {
                sb.append(';');
            }
            return sb.toString();
        }
        
        String getClassType() {
            return (this.type != null) ? this.type : "java/lang/Object";
        }
        
        Token addClassBound(final String s) {
            final Token token = new Token(s);
            this.getClassBound().add(token);
            return token;
        }
        
        private List<Token> getIfaceBound() {
            if (this.ifaceBound == null) {
                this.ifaceBound = new ArrayList<Token>();
            }
            return this.ifaceBound;
        }
        
        @Override
        public IToken setArray(final boolean b) {
            this.array |= b;
            return this;
        }
        
        Token addInterfaceBound(final String s) {
            final Token token = new Token(s);
            this.getIfaceBound().add(token);
            return token;
        }
        
        boolean hasInterfaceBound() {
            return this.ifaceBound != null;
        }
        
        IToken addTypeArgument(final Token token) {
            if (this.tail != null) {
                return this.tail.addTypeArgument(token);
            }
            this.getSignature().add(token);
            return token;
        }
        
        @Override
        public Token asToken() {
            return this;
        }
        
        Token(final String type, final boolean inner) {
            this.symbol = '\0';
            this.inner = inner;
            this.type = type;
        }
        
        static {
            SYMBOLS = "+-*";
        }
        
        Token() {
            this(false);
        }
        
        IToken addTypeArgument(final TokenHandle tokenHandle) {
            if (this.tail != null) {
                return this.tail.addTypeArgument(tokenHandle);
            }
            final TokenHandle clone = tokenHandle.clone();
            this.getSignature().add(clone);
            return clone;
        }
        
        private List<Token> getClassBound() {
            if (this.classBound == null) {
                this.classBound = new ArrayList<Token>();
            }
            return this.classBound;
        }
        
        Token setType(final String type) {
            if (this.type == null) {
                this.type = type;
            }
            return this;
        }
        
        Token addBound(final String s, final boolean b) {
            if (b) {
                return this.addClassBound(s);
            }
            return this.addInterfaceBound(s);
        }
        
        @Override
        public String toString() {
            return this.asType();
        }
        
        boolean hasClassBound() {
            return this.classBound != null;
        }
        
        Token(final String s) {
            this(s, false);
        }
        
        Token(final boolean b) {
            this(null, b);
        }
        
        private List<IToken> getSuffix() {
            if (this.suffix == null) {
                this.suffix = new ArrayList<IToken>();
            }
            return this.suffix;
        }
        
        @Override
        public IToken setWildcard(final char c) {
            if ("+-".indexOf(c) == -1) {
                return this;
            }
            return this.setSymbol(c);
        }
        
        Token addInnerClass(final String s) {
            this.tail = new Token(s, true);
            this.getSuffix().add(this.tail);
            return this.tail;
        }
    }
    
    static class Lazy extends ClassSignature
    {
        private /* synthetic */ ClassSignature generated;
        private final /* synthetic */ String sig;
        
        Lazy(final String sig) {
            this.sig = sig;
        }
        
        @Override
        public ClassSignature wake() {
            if (this.generated == null) {
                this.generated = ClassSignature.of(this.sig);
            }
            return this.generated;
        }
    }
    
    class SignatureParser extends SignatureVisitor
    {
        final /* synthetic */ ClassSignature this$0;
        private /* synthetic */ FormalParamElement param;
        
        @Override
        public SignatureVisitor visitSuperclass() {
            return new SuperClassElement();
        }
        
        @Override
        public SignatureVisitor visitInterface() {
            return new InterfaceElement();
        }
        
        SignatureParser() {
            super(327680);
        }
        
        @Override
        public SignatureVisitor visitClassBound() {
            return this.param.visitClassBound();
        }
        
        @Override
        public SignatureVisitor visitInterfaceBound() {
            return this.param.visitInterfaceBound();
        }
        
        @Override
        public void visitFormalTypeParameter(final String s) {
            this.param = new FormalParamElement(s);
        }
        
        class BoundElement extends TokenElement
        {
            private final /* synthetic */ TokenElement type;
            private final /* synthetic */ boolean classBound;
            
            BoundElement(final TokenElement type, final boolean classBound) {
                this.type = type;
                this.classBound = classBound;
            }
            
            @Override
            public SignatureVisitor visitTypeArgument(final char c) {
                return new TypeArgElement(this, c);
            }
            
            @Override
            public void visitClassType(final String s) {
                this.token = this.type.token.addBound(s, this.classBound);
            }
            
            @Override
            public void visitTypeArgument() {
                this.token.addTypeArgument('*');
            }
        }
        
        abstract class TokenElement extends SignatureElement
        {
            private /* synthetic */ boolean array;
            protected /* synthetic */ Token token;
            
            @Override
            public SignatureVisitor visitTypeArgument(final char c) {
                return new TypeArgElement(this, c);
            }
            
            private boolean getArray() {
                final boolean array = this.array;
                this.array = false;
                return array;
            }
            
            IToken addTypeArgument(final char c) {
                return this.token.addTypeArgument(c).setArray(this.getArray());
            }
            
            IToken addTypeArgument(final Token token) {
                return this.token.addTypeArgument(token).setArray(this.getArray());
            }
            
            public Token getToken() {
                if (this.token == null) {
                    this.token = new Token();
                }
                return this.token;
            }
            
            @Override
            public SignatureVisitor visitArrayType() {
                this.setArray();
                return this;
            }
            
            protected void setArray() {
                this.array = true;
            }
            
            @Override
            public SignatureVisitor visitInterfaceBound() {
                this.getToken();
                return new BoundElement(this, false);
            }
            
            IToken addTypeArgument(final TokenHandle tokenHandle) {
                return this.token.addTypeArgument(tokenHandle).setArray(this.getArray());
            }
            
            Token addTypeArgument() {
                return this.token.addTypeArgument('*').asToken();
            }
            
            @Override
            public void visitInnerClassType(final String s) {
                this.token.addInnerClass(s);
            }
            
            @Override
            public SignatureVisitor visitClassBound() {
                this.getToken();
                return new BoundElement(this, true);
            }
            
            @Override
            public void visitClassType(final String type) {
                this.getToken().setType(type);
            }
            
            IToken addTypeArgument(final String s) {
                return this.token.addTypeArgument(s).setArray(this.getArray());
            }
        }
        
        abstract class SignatureElement extends SignatureVisitor
        {
            public SignatureElement() {
                super(327680);
            }
        }
        
        class TypeArgElement extends TokenElement
        {
            private final /* synthetic */ TokenElement type;
            private final /* synthetic */ char wildcard;
            
            @Override
            public void visitBaseType(final char c) {
                this.token = this.type.addTypeArgument(c).asToken();
            }
            
            @Override
            public SignatureVisitor visitArrayType() {
                this.type.setArray();
                return this;
            }
            
            @Override
            public void visitEnd() {
            }
            
            @Override
            public void visitClassType(final String s) {
                this.token = this.type.addTypeArgument(s).setWildcard(this.wildcard).asToken();
            }
            
            @Override
            public void visitTypeVariable(final String s) {
                this.token = this.type.addTypeArgument(ClassSignature.this.getType(s)).setWildcard(this.wildcard).asToken();
            }
            
            @Override
            public SignatureVisitor visitTypeArgument(final char c) {
                return new TypeArgElement(this, c);
            }
            
            @Override
            public void visitTypeArgument() {
                this.token.addTypeArgument('*');
            }
            
            TypeArgElement(final TokenElement type, final char wildcard) {
                this.type = type;
                this.wildcard = wildcard;
            }
        }
        
        class SuperClassElement extends TokenElement
        {
            @Override
            public void visitEnd() {
                ClassSignature.this.setSuperClass(this.token);
            }
        }
        
        class InterfaceElement extends TokenElement
        {
            @Override
            public void visitEnd() {
                ClassSignature.this.addInterface(this.token);
            }
        }
        
        class FormalParamElement extends TokenElement
        {
            private final /* synthetic */ TokenHandle handle;
            
            FormalParamElement(final String s) {
                this.handle = SignatureParser.this.this$0.getType(s);
                this.token = this.handle.asToken();
            }
        }
    }
    
    static class TypeVar implements Comparable<TypeVar>
    {
        private final /* synthetic */ String originalName;
        private /* synthetic */ String currentName;
        
        @Override
        public int hashCode() {
            return this.currentName.hashCode();
        }
        
        void rename(final String currentName) {
            this.currentName = currentName;
        }
        
        @Override
        public boolean equals(final Object anObject) {
            return this.currentName.equals(anObject);
        }
        
        String getOriginalName() {
            return this.originalName;
        }
        
        @Override
        public String toString() {
            return this.currentName;
        }
        
        public boolean matches(final String anObject) {
            return this.originalName.equals(anObject);
        }
        
        @Override
        public int compareTo(final TypeVar typeVar) {
            return this.currentName.compareTo(typeVar.currentName);
        }
        
        TypeVar(final String s) {
            this.originalName = s;
            this.currentName = s;
        }
    }
    
    class SignatureRemapper extends SignatureWriter
    {
        private final /* synthetic */ Set<String> localTypeVars;
        
        SignatureRemapper() {
            this.localTypeVars = new HashSet<String>();
        }
        
        @Override
        public void visitTypeVariable(final String s) {
            if (!this.localTypeVars.contains(s)) {
                final TypeVar typeVar = ClassSignature.this.getTypeVar(s);
                if (typeVar != null) {
                    super.visitTypeVariable(typeVar.toString());
                    return;
                }
            }
            super.visitTypeVariable(s);
        }
        
        @Override
        public void visitFormalTypeParameter(final String s) {
            this.localTypeVars.add(s);
            super.visitFormalTypeParameter(s);
        }
    }
}
