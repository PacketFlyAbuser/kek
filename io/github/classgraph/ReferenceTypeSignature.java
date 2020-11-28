// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.types.ParseException;
import nonapi.io.github.classgraph.types.Parser;

public abstract class ReferenceTypeSignature extends TypeSignature
{
    static ReferenceTypeSignature parseReferenceTypeSignature(final Parser parser, final String s) throws ParseException {
        final ClassRefTypeSignature parse = ClassRefTypeSignature.parse(parser, s);
        if (parse != null) {
            return parse;
        }
        final TypeVariableSignature parse2 = TypeVariableSignature.parse(parser, s);
        if (parse2 != null) {
            return parse2;
        }
        final ArrayTypeSignature parse3 = ArrayTypeSignature.parse(parser, s);
        if (parse3 != null) {
            return parse3;
        }
        return null;
    }
    
    static ReferenceTypeSignature parseClassBound(final Parser parser, final String s) throws ParseException {
        parser.expect(':');
        return parseReferenceTypeSignature(parser, s);
    }
    
    protected ReferenceTypeSignature() {
    }
}
