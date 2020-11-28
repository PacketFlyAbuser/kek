// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.types;

public final class TypeUtils
{
    public static boolean getIdentifierToken(final Parser parser) throws ParseException {
        return getIdentifierToken(parser, '\0', '\0');
    }
    
    public static boolean getIdentifierToken(final Parser parser, final char c, final char c2) {
        boolean b = false;
        while (parser.hasMore()) {
            final char peek = parser.peek();
            if (peek == c) {
                parser.appendToToken(c2);
                parser.next();
                b = true;
            }
            else {
                if (peek == ';' || peek == '[' || peek == '<' || peek == '>' || peek == ':' || peek == '/' || peek == '.') {
                    break;
                }
                parser.appendToToken(peek);
                parser.next();
                b = true;
            }
        }
        return b;
    }
    
    private TypeUtils() {
    }
    
    public static void modifiersToString(final int n, final ModifierType modifierType, final boolean b, final StringBuilder sb) {
        if ((n & 0x1) != 0x0) {
            appendModifierKeyword(sb, "public");
        }
        else if ((n & 0x2) != 0x0) {
            appendModifierKeyword(sb, "private");
        }
        else if ((n & 0x4) != 0x0) {
            appendModifierKeyword(sb, "protected");
        }
        if (modifierType != ModifierType.FIELD && (n & 0x400) != 0x0) {
            appendModifierKeyword(sb, "abstract");
        }
        if ((n & 0x8) != 0x0) {
            appendModifierKeyword(sb, "static");
        }
        if (modifierType == ModifierType.FIELD) {
            if ((n & 0x40) != 0x0) {
                appendModifierKeyword(sb, "volatile");
            }
            if ((n & 0x80) != 0x0) {
                appendModifierKeyword(sb, "transient");
            }
        }
        if ((n & 0x10) != 0x0) {
            appendModifierKeyword(sb, "final");
        }
        if (modifierType == ModifierType.METHOD) {
            if ((n & 0x20) != 0x0) {
                appendModifierKeyword(sb, "synchronized");
            }
            if (b) {
                appendModifierKeyword(sb, "default");
            }
        }
        if ((n & 0x1000) != 0x0) {
            appendModifierKeyword(sb, "synthetic");
        }
        if (modifierType != ModifierType.FIELD && (n & 0x40) != 0x0) {
            appendModifierKeyword(sb, "bridge");
        }
        if (modifierType == ModifierType.METHOD && (n & 0x100) != 0x0) {
            appendModifierKeyword(sb, "native");
        }
        if (modifierType != ModifierType.FIELD && (n & 0x800) != 0x0) {
            appendModifierKeyword(sb, "strictfp");
        }
    }
    
    private static void appendModifierKeyword(final StringBuilder sb, final String str) {
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
            sb.append(' ');
        }
        sb.append(str);
    }
    
    public enum ModifierType
    {
        FIELD, 
        CLASS, 
        METHOD;
    }
}
