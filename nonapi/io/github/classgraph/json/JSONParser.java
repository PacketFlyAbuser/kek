// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import nonapi.io.github.classgraph.types.ParseException;
import java.util.Map;
import java.util.List;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import nonapi.io.github.classgraph.types.Parser;

final class JSONParser extends Parser
{
    private JSONObject parseJSONObject() throws ParseException {
        this.expect('{');
        this.skipWhitespace();
        if (this.peek() == '}') {
            this.next();
            return new JSONObject(Collections.emptyList());
        }
        final ArrayList<AbstractMap.SimpleEntry<String, CharSequence>> list = new ArrayList<AbstractMap.SimpleEntry<String, CharSequence>>();
        final JSONObject jsonObject = new JSONObject((List<Map.Entry<String, Object>>)list);
        int n = 1;
        while (this.peek() != '}') {
            if (n != 0) {
                n = 0;
            }
            else {
                this.expect(',');
            }
            final CharSequence string = this.parseString();
            if (string == null) {
                throw new ParseException(this, "Object keys must be strings");
            }
            if (this.peek() != ':') {
                return null;
            }
            this.expect(':');
            final Object json = this.parseJSON();
            if (string.equals("__ID")) {
                if (json == null) {
                    throw new ParseException(this, "Got null value for \"__ID\" key");
                }
                jsonObject.objectId = (CharSequence)json;
            }
            else {
                list.add(new AbstractMap.SimpleEntry<String, CharSequence>(string.toString(), json));
            }
        }
        this.expect('}');
        return jsonObject;
    }
    
    private Number parseNumber() throws ParseException {
        final int position = this.getPosition();
        if (this.peekMatches("Infinity")) {
            this.advance(8);
            return Double.POSITIVE_INFINITY;
        }
        if (this.peekMatches("-Infinity")) {
            this.advance(9);
            return Double.NEGATIVE_INFINITY;
        }
        if (this.peekMatches("NaN")) {
            this.advance(3);
            return Double.NaN;
        }
        if (this.peek() == '-') {
            this.next();
        }
        final int position2 = this.getPosition();
        while (this.hasMore()) {
            final char peek = this.peek();
            if (peek < '0') {
                break;
            }
            if (peek > '9') {
                break;
            }
            this.next();
        }
        final int position3 = this.getPosition();
        final int n = position3 - position2;
        if (n == 0) {
            throw new ParseException(this, "Expected a number");
        }
        final boolean b = this.peek() == '.';
        if (b) {
            this.next();
            while (this.hasMore()) {
                final char peek2 = this.peek();
                if (peek2 < '0') {
                    break;
                }
                if (peek2 > '9') {
                    break;
                }
                this.next();
            }
            if (this.getPosition() - (position3 + 1) == 0) {
                throw new ParseException(this, "Expected digits after decimal point");
            }
        }
        final boolean b2 = this.peek() == 'e' || this.peek() == 'E';
        if (b2) {
            this.next();
            final char peek3 = this.peek();
            if (peek3 == '-' || peek3 == '+') {
                this.next();
            }
            final int position4 = this.getPosition();
            while (this.hasMore()) {
                final char peek4 = this.peek();
                if (peek4 < '0') {
                    break;
                }
                if (peek4 > '9') {
                    break;
                }
                this.next();
            }
            if (this.getPosition() - position4 == 0) {
                throw new ParseException(this, "Expected an exponent");
            }
        }
        final String substring = this.getSubstring(position, this.getPosition());
        if (b || b2) {
            return Double.valueOf(substring);
        }
        if (n < 10) {
            return Integer.valueOf(substring);
        }
        if (n != 10) {
            return Long.valueOf(substring);
        }
        final long long1 = Long.parseLong(substring);
        if (long1 >= -2147483648L && long1 <= 2147483647L) {
            return (int)long1;
        }
        return long1;
    }
    
    private JSONParser(final String s) throws ParseException {
        super(s);
    }
    
    private int getAndParseHexChar() throws ParseException {
        final char getc = this.getc();
        if (getc >= '0' && getc <= '9') {
            return getc - '0';
        }
        if (getc >= 'a' && getc <= 'f') {
            return getc - 'a' + 10;
        }
        if (getc >= 'A' && getc <= 'F') {
            return getc - 'A' + 10;
        }
        throw new ParseException(this, "Invalid character in Unicode escape sequence: " + getc);
    }
    
    static Object parseJSON(final String s) throws ParseException {
        return new JSONParser(s).parseJSON();
    }
    
    private CharSequence parseString() throws ParseException {
        this.skipWhitespace();
        if (this.peek() != '\"') {
            return null;
        }
        this.next();
        final int position = this.getPosition();
        boolean b = false;
        while (this.hasMore()) {
            final char getc = this.getc();
            if (getc == '\\') {
                switch (this.getc()) {
                    case '\"':
                    case '\'':
                    case '/':
                    case '\\':
                    case 'b':
                    case 'f':
                    case 'n':
                    case 'r':
                    case 't': {
                        b = true;
                        continue;
                    }
                    case 'u': {
                        b = true;
                        this.advance(4);
                        continue;
                    }
                    default: {
                        throw new ParseException(this, "Invalid escape sequence: \\" + getc);
                    }
                }
            }
            else {
                if (getc == '\"') {
                    break;
                }
                continue;
            }
        }
        final int n = this.getPosition() - 1;
        if (!b) {
            return this.getSubsequence(position, n);
        }
        this.setPosition(position);
        final StringBuilder sb = new StringBuilder();
        while (this.hasMore()) {
            final char getc2 = this.getc();
            if (getc2 == '\\') {
                final char getc3 = this.getc();
                switch (getc3) {
                    case 98: {
                        sb.append('\b');
                        continue;
                    }
                    case 102: {
                        sb.append('\f');
                        continue;
                    }
                    case 110: {
                        sb.append('\n');
                        continue;
                    }
                    case 114: {
                        sb.append('\r');
                        continue;
                    }
                    case 116: {
                        sb.append('\t');
                        continue;
                    }
                    case 34:
                    case 39:
                    case 47:
                    case 92: {
                        sb.append(getc3);
                        continue;
                    }
                    case 117: {
                        sb.append((char)(this.getAndParseHexChar() << 12 | this.getAndParseHexChar() << 8 | this.getAndParseHexChar() << 4 | this.getAndParseHexChar()));
                        continue;
                    }
                    default: {
                        throw new ParseException(this, "Invalid escape sequence: \\" + getc2);
                    }
                }
            }
            else {
                if (getc2 == '\"') {
                    break;
                }
                sb.append(getc2);
            }
        }
        this.skipWhitespace();
        return sb.toString();
    }
    
    private Object parseJSON() throws ParseException {
        this.skipWhitespace();
        try {
            final char peek = this.peek();
            if (peek == '{') {
                return this.parseJSONObject();
            }
            if (peek == '[') {
                return this.parseJSONArray();
            }
            if (peek == '\"') {
                final CharSequence string = this.parseString();
                if (string == null) {
                    throw new ParseException(this, "Invalid string");
                }
                return string;
            }
            else {
                if (this.peekMatches("true")) {
                    this.advance(4);
                    return Boolean.TRUE;
                }
                if (this.peekMatches("false")) {
                    this.advance(5);
                    return Boolean.FALSE;
                }
                if (this.peekMatches("null")) {
                    this.advance(4);
                    return null;
                }
                return this.parseNumber();
            }
        }
        finally {
            this.skipWhitespace();
        }
    }
    
    private JSONArray parseJSONArray() throws ParseException {
        this.expect('[');
        this.skipWhitespace();
        if (this.peek() == ']') {
            this.next();
            return new JSONArray(Collections.emptyList());
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        int n = 1;
        while (this.peek() != ']') {
            if (n != 0) {
                n = 0;
            }
            else {
                this.expect(',');
            }
            list.add(this.parseJSON());
        }
        this.expect(']');
        return new JSONArray(list);
    }
}
