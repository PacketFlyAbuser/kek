// 
// Decompiled by Procyon v0.5.36
// 

package org.json.simple.parser;

import java.io.IOException;

public interface ContentHandler
{
    void startJSON() throws IOException, ParseException;
    
    void endJSON() throws IOException, ParseException;
    
    boolean startObject() throws ParseException, IOException;
    
    boolean primitive(final Object p0) throws ParseException, IOException;
    
    boolean endArray() throws IOException, ParseException;
    
    boolean endObject() throws IOException, ParseException;
    
    boolean startArray() throws IOException, ParseException;
    
    boolean startObjectEntry(final String p0) throws IOException, ParseException;
    
    boolean endObjectEntry() throws IOException, ParseException;
}
