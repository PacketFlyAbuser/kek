// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.types.TypeUtils;
import nonapi.io.github.classgraph.types.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import nonapi.io.github.classgraph.types.Parser;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

public final class TypeParameter extends HierarchicalTypeSignature
{
    final /* synthetic */ List<ReferenceTypeSignature> interfaceBounds;
    final /* synthetic */ String name;
    final /* synthetic */ ReferenceTypeSignature classBound;
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TypeParameter)) {
            return false;
        }
        final TypeParameter typeParameter = (TypeParameter)o;
        return typeParameter.name.equals(this.name) && ((typeParameter.classBound == null && this.classBound == null) || (typeParameter.classBound != null && typeParameter.classBound.equals(this.classBound))) && typeParameter.interfaceBounds.equals(this.interfaceBounds);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        String string;
        if (this.classBound == null) {
            string = null;
        }
        else {
            string = this.classBound.toString();
            if (string.equals("java.lang.Object")) {
                string = null;
            }
        }
        if (string != null || !this.interfaceBounds.isEmpty()) {
            sb.append(" extends");
        }
        if (string != null) {
            sb.append(' ');
            sb.append(string);
        }
        for (int i = 0; i < this.interfaceBounds.size(); ++i) {
            if (i > 0 || string != null) {
                sb.append(" &");
            }
            sb.append(' ');
            sb.append(this.interfaceBounds.get(i).toString());
        }
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + ((this.classBound == null) ? 0 : (this.classBound.hashCode() * 7)) + this.interfaceBounds.hashCode() * 15;
    }
    
    private TypeParameter(final String name, final ReferenceTypeSignature classBound, final List<ReferenceTypeSignature> interfaceBounds) {
        this.name = name;
        this.classBound = classBound;
        this.interfaceBounds = interfaceBounds;
    }
    
    public ReferenceTypeSignature getClassBound() {
        return this.classBound;
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.classBound != null) {
            this.classBound.setScanResult(scanResult);
        }
        if (this.interfaceBounds != null) {
            final Iterator<ReferenceTypeSignature> iterator = this.interfaceBounds.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
    }
    
    @Override
    protected String getClassName() {
        throw new IllegalArgumentException("getClassName() cannot be called here");
    }
    
    protected void findReferencedClassNames(final Set<String> set) {
        if (this.classBound != null) {
            this.classBound.findReferencedClassNames(set);
        }
        final Iterator<ReferenceTypeSignature> iterator = this.interfaceBounds.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassNames(set);
        }
    }
    
    static List<TypeParameter> parseList(final Parser parser, final String s) throws ParseException {
        if (parser.peek() != '<') {
            return Collections.emptyList();
        }
        parser.expect('<');
        final ArrayList<TypeParameter> list = new ArrayList<TypeParameter>(1);
        while (parser.peek() != '>') {
            if (!parser.hasMore()) {
                throw new ParseException(parser, "Missing '>'");
            }
            if (!TypeUtils.getIdentifierToken(parser)) {
                throw new ParseException(parser, "Could not parse identifier token");
            }
            final String currToken = parser.currToken();
            final ReferenceTypeSignature classBound = ReferenceTypeSignature.parseClassBound(parser, s);
            List<ReferenceTypeSignature> emptyList;
            if (parser.peek() == ':') {
                emptyList = new ArrayList<ReferenceTypeSignature>();
                while (parser.peek() == ':') {
                    parser.expect(':');
                    final ReferenceTypeSignature referenceTypeSignature = ReferenceTypeSignature.parseReferenceTypeSignature(parser, s);
                    if (referenceTypeSignature == null) {
                        throw new ParseException(parser, "Missing interface type signature");
                    }
                    emptyList.add(referenceTypeSignature);
                }
            }
            else {
                emptyList = Collections.emptyList();
            }
            list.add(new TypeParameter(currToken, classBound, emptyList));
        }
        parser.expect('>');
        return list;
    }
    
    public List<ReferenceTypeSignature> getInterfaceBounds() {
        return this.interfaceBounds;
    }
    
    protected ClassInfo getClassInfo() {
        throw new IllegalArgumentException("getClassInfo() cannot be called here");
    }
}
