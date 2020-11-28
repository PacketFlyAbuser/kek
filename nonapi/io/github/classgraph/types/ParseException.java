// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.types;

public class ParseException extends Exception
{
    public ParseException(final Parser parser, final String str) {
        super((parser == null) ? str : (str + " (" + parser.getPositionInfo() + ")"));
    }
}
