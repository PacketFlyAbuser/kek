// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.types;

import nonapi.io.github.classgraph.json.JSONUtils;

public class Parser
{
    private final /* synthetic */ String string;
    private final /* synthetic */ StringBuilder token;
    private /* synthetic */ Object state;
    private /* synthetic */ int position;
    
    public Object setState(final Object state) {
        final Object state2 = this.state;
        this.state = state;
        return state2;
    }
    
    public char peek() {
        return (this.position == this.string.length()) ? '\0' : this.string.charAt(this.position);
    }
    
    public void peekExpect(final char c) throws ParseException {
        if (this.position == this.string.length()) {
            throw new ParseException(this, "Expected '" + c + "'; reached end of string");
        }
        final char char1 = this.string.charAt(this.position);
        if (char1 != c) {
            throw new ParseException(this, "Expected '" + c + "'; got '" + char1 + "'");
        }
    }
    
    public String getSubstring(final int beginIndex, final int endIndex) {
        return this.string.substring(beginIndex, endIndex);
    }
    
    public void appendToToken(final char c) {
        this.token.append(c);
    }
    
    public void next() {
        ++this.position;
    }
    
    public boolean hasMore() {
        return this.position < this.string.length();
    }
    
    public String currToken() {
        final String string = this.token.toString();
        this.token.setLength(0);
        return string;
    }
    
    public CharSequence getSubsequence(final int beginIndex, final int endIndex) {
        return this.string.subSequence(beginIndex, endIndex);
    }
    
    public void appendToToken(final String str) {
        this.token.append(str);
    }
    
    public boolean peekMatches(final String other) {
        return this.string.regionMatches(this.position, other, 0, other.length());
    }
    
    public void expect(final char c) throws ParseException {
        final char getc = this.getc();
        if (getc != c) {
            throw new ParseException(this, "Expected '" + c + "'; got '" + getc + "'");
        }
    }
    
    public Parser(final String string) throws ParseException {
        this.token = new StringBuilder();
        if (string == null) {
            throw new ParseException(null, "Cannot parse null string");
        }
        this.string = string;
    }
    
    public void skipWhitespace() {
        while (this.position < this.string.length()) {
            final char char1 = this.string.charAt(this.position);
            if (char1 != ' ' && char1 != '\n' && char1 != '\r' && char1 != '\t') {
                break;
            }
            ++this.position;
        }
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public void setPosition(final int position) {
        if (position < 0 || position >= this.string.length()) {
            throw new IllegalArgumentException("Invalid position");
        }
        this.position = position;
    }
    
    static {
        SHOW_AFTER = 80;
        SHOW_BEFORE = 80;
    }
    
    public Object getState() {
        return this.state;
    }
    
    public String getPositionInfo() {
        return "before: \"" + JSONUtils.escapeJSONString(this.string.substring(Math.max(0, this.position - 80), this.position)) + "\"; after: \"" + JSONUtils.escapeJSONString(this.string.substring(this.position, Math.min(this.string.length(), this.position + 80))) + "\"; position: " + this.position + "; token: \"" + (Object)this.token + "\"";
    }
    
    public char getc() throws ParseException {
        if (this.position >= this.string.length()) {
            throw new ParseException(this, "Ran out of input while parsing");
        }
        return this.string.charAt(this.position++);
    }
    
    public void advance(final int n) {
        if (this.position + n >= this.string.length()) {
            throw new IllegalArgumentException("Invalid skip distance");
        }
        this.position += n;
    }
    
    @Override
    public String toString() {
        return this.getPositionInfo();
    }
}
