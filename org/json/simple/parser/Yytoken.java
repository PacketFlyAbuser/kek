// 
// Decompiled by Procyon v0.5.36
// 

package org.json.simple.parser;

public class Yytoken
{
    public /* synthetic */ Object value;
    public /* synthetic */ int type;
    
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        switch (this.type) {
            case 0: {
                sb.append("VALUE(").append(this.value).append(")");
                break;
            }
            case 1: {
                sb.append("LEFT BRACE({)");
                break;
            }
            case 2: {
                sb.append("RIGHT BRACE(})");
                break;
            }
            case 3: {
                sb.append("LEFT SQUARE([)");
                break;
            }
            case 4: {
                sb.append("RIGHT SQUARE(])");
                break;
            }
            case 5: {
                sb.append("COMMA(,)");
                break;
            }
            case 6: {
                sb.append("COLON(:)");
                break;
            }
            case -1: {
                sb.append("END OF FILE");
                break;
            }
        }
        return sb.toString();
    }
    
    public Yytoken(final int type, final Object value) {
        this.type = 0;
        this.value = null;
        this.type = type;
        this.value = value;
    }
    
    static {
        TYPE_EOF = -1;
        TYPE_LEFT_SQUARE = 3;
        TYPE_COMMA = 5;
        TYPE_VALUE = 0;
        TYPE_RIGHT_BRACE = 2;
        TYPE_COLON = 6;
        TYPE_LEFT_BRACE = 1;
        TYPE_RIGHT_SQUARE = 4;
    }
}
