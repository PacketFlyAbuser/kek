// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import com.google.common.base.Strings;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import java.util.Iterator;
import java.util.Map;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class PrettyPrinter
{
    private /* synthetic */ boolean recalcWidth;
    private final /* synthetic */ List<Object> lines;
    protected /* synthetic */ int kvKeyWidth;
    private final /* synthetic */ HorizontalRule horizontalRule;
    protected /* synthetic */ String kvFormat;
    private /* synthetic */ Table table;
    protected /* synthetic */ int wrapWidth;
    protected /* synthetic */ int width;
    
    public PrettyPrinter table(final Object... array) {
        this.table = new Table();
        Column column = null;
        for (final Object o : array) {
            if (o instanceof String) {
                column = this.table.addColumn((String)o);
            }
            else if (o instanceof Integer && column != null) {
                final int intValue = (int)o;
                if (intValue > 0) {
                    column.setWidth(intValue);
                }
                else if (intValue < 0) {
                    column.setMaxWidth(-intValue);
                }
            }
            else if (o instanceof Alignment && column != null) {
                column.setAlignment((Alignment)o);
            }
            else if (o != null) {
                column = this.table.addColumn(o.toString());
            }
        }
        return this;
    }
    
    public PrettyPrinter() {
        this(100);
    }
    
    private List<String> getWrapped(final int fromIndex, String string, final String str) {
        final ArrayList<String> list = new ArrayList<String>();
        while (string.length() > fromIndex) {
            int lastIndex = string.lastIndexOf(32, fromIndex);
            if (lastIndex < 10) {
                lastIndex = fromIndex;
            }
            list.add(string.substring(0, lastIndex));
            string = str + string.substring(lastIndex + 1);
        }
        if (string.length() > 0) {
            list.add(string);
        }
        return list;
    }
    
    public PrettyPrinter trace(final PrintStream printStream) {
        return this.trace(printStream, getDefaultLoggerName());
    }
    
    private PrettyPrinter append(final Object o, final int i, final String s) {
        if (o instanceof String) {
            return this.add("%s%s", s, o);
        }
        if (o instanceof Iterable) {
            final Iterator<Object> iterator = ((Iterable)o).iterator();
            while (iterator.hasNext()) {
                this.append(iterator.next(), i, s);
            }
            return this;
        }
        if (o instanceof Map) {
            this.kvWidth(i);
            return this.add((Map<?, ?>)o);
        }
        if (o instanceof IPrettyPrintable) {
            return this.add((IPrettyPrintable)o);
        }
        if (o instanceof Throwable) {
            return this.add((Throwable)o, i);
        }
        if (o.getClass().isArray()) {
            return this.add((Object[])o, i + "%s");
        }
        return this.add("%s%s", s, o);
    }
    
    public PrettyPrinter th() {
        return this.th(false);
    }
    
    private static String getDefaultLoggerName() {
        final String className = new Throwable().getStackTrace()[2].getClassName();
        final int lastIndex = className.lastIndexOf(46);
        return (lastIndex == -1) ? className : className.substring(lastIndex + 1);
    }
    
    private void printString(final PrintStream printStream, final String s) {
        if (s != null) {
            printStream.printf("/* %-" + this.width + "s */\n", s);
        }
    }
    
    public PrettyPrinter trace(final Logger logger) {
        return this.trace(System.err, logger);
    }
    
    public PrettyPrinter trace() {
        return this.trace(getDefaultLoggerName());
    }
    
    private void logString(final Logger logger, final Level level, final String s) {
        if (s != null) {
            logger.log(level, String.format("/* %-" + this.width + "s */", s));
        }
    }
    
    public PrettyPrinter centre() {
        if (!this.lines.isEmpty() && this.lines.get(this.lines.size() - 1) instanceof String) {
            this.addLine(new CentredText(this.lines.remove(this.lines.size() - 1)));
        }
        return this;
    }
    
    public static void dumpStack() {
        new PrettyPrinter().add(new Exception("Stack trace")).print(System.err);
    }
    
    public PrettyPrinter print() {
        return this.print(System.err);
    }
    
    public PrettyPrinter add(final StackTraceElement[] array, final int n) {
        final String repeat = Strings.repeat(" ", n);
        for (int length = array.length, i = 0; i < length; ++i) {
            this.add("%s%s", repeat, array[i]);
        }
        return this;
    }
    
    public PrettyPrinter kvWidth(final int kvKeyWidth) {
        if (kvKeyWidth > this.kvKeyWidth) {
            this.kvKeyWidth = kvKeyWidth;
            this.kvFormat = makeKvFormat(kvKeyWidth);
        }
        this.recalcWidth = true;
        return this;
    }
    
    public PrettyPrinter addIndexed(final Object[] array) {
        final String string = "[%" + String.valueOf(array.length - 1).length() + "d] %s";
        for (int i = 0; i < array.length; ++i) {
            this.add(string, i, array[i]);
        }
        return this;
    }
    
    public PrettyPrinter print(final PrintStream printStream) {
        this.updateWidth();
        this.printSpecial(printStream, this.horizontalRule);
        for (final ISpecialEntry next : this.lines) {
            if (next instanceof ISpecialEntry) {
                this.printSpecial(printStream, next);
            }
            else {
                this.printString(printStream, next.toString());
            }
        }
        this.printSpecial(printStream, this.horizontalRule);
        return this;
    }
    
    private void updateWidth() {
        if (this.recalcWidth) {
            this.recalcWidth = false;
            for (final IVariableWidthEntry next : this.lines) {
                if (next instanceof IVariableWidthEntry) {
                    this.width = Math.min(4096, Math.max(this.width, next.getWidth()));
                }
            }
        }
    }
    
    public PrettyPrinter hr(final char c) {
        this.addLine(new HorizontalRule(new char[] { c }));
        return this;
    }
    
    public PrettyPrinter trace(final String s, final Level level) {
        return this.trace(System.err, LogManager.getLogger(s), level);
    }
    
    public PrettyPrinter trace(final PrintStream printStream, final Logger logger, final Level level) {
        this.log(logger, level);
        this.print(printStream);
        return this;
    }
    
    public PrettyPrinter add(final Object o, final int n) {
        return this.append(o, n, Strings.repeat(" ", n));
    }
    
    public PrettyPrinter add(final IPrettyPrintable prettyPrintable) {
        if (prettyPrintable != null) {
            prettyPrintable.print(this);
        }
        return this;
    }
    
    public static void print(final Throwable t) {
        new PrettyPrinter().add(t).print(System.err);
    }
    
    public PrettyPrinter trace(final PrintStream printStream, final Logger logger) {
        return this.trace(printStream, logger, Level.DEBUG);
    }
    
    public PrettyPrinter add(final Object o) {
        return this.add(o, 0);
    }
    
    public PrettyPrinter log(final Logger logger) {
        return this.log(logger, Level.INFO);
    }
    
    private void logSpecial(final Logger logger, final Level level, final ISpecialEntry specialEntry) {
        logger.log(level, "/*{}*/", new Object[] { specialEntry.toString() });
    }
    
    public PrettyPrinter add(Throwable cause, final int n) {
        while (cause != null) {
            this.add("%s: %s", cause.getClass().getName(), cause.getMessage());
            this.add(cause.getStackTrace(), n);
            cause = cause.getCause();
        }
        return this;
    }
    
    public PrettyPrinter add(final String format, final Object... args) {
        final String format2 = String.format(format, args);
        this.addLine(format2);
        this.width = Math.max(this.width, format2.length());
        return this;
    }
    
    public PrettyPrinter add() {
        this.addLine("");
        return this;
    }
    
    public PrettyPrinter add(final Map<?, ?> map) {
        for (final Map.Entry<Object, V> entry : map.entrySet()) {
            this.kv((entry.getKey() == null) ? "null" : entry.getKey().toString(), entry.getValue());
        }
        return this;
    }
    
    public PrettyPrinter trace(final PrintStream printStream, final String s, final Level level) {
        return this.trace(printStream, LogManager.getLogger(s), level);
    }
    
    public PrettyPrinter spacing(final int colSpacing) {
        if (this.table == null) {
            this.table = new Table();
        }
        this.table.setColSpacing(colSpacing);
        return this;
    }
    
    private void printSpecial(final PrintStream printStream, final ISpecialEntry specialEntry) {
        printStream.printf("/*%s*/\n", specialEntry.toString());
    }
    
    public PrettyPrinter add(final String s) {
        this.addLine(s);
        this.width = Math.max(this.width, s.length());
        return this;
    }
    
    public PrettyPrinter table() {
        this.table = new Table();
        return this;
    }
    
    public PrettyPrinter kv(final String s, final Object o) {
        this.addLine(new KeyValue(s, o));
        return this.kvWidth(s.length());
    }
    
    public PrettyPrinter addWrapped(final String s, final Object... array) {
        return this.addWrapped(this.wrapWidth, s, array);
    }
    
    public PrettyPrinter trace(final Logger logger, final Level level) {
        return this.trace(System.err, logger, level);
    }
    
    public PrettyPrinter addWrapped(final int n, final String format, final Object... args) {
        String group = "";
        final String replace = String.format(format, args).replace("\t", "    ");
        final Matcher matcher = Pattern.compile("^(\\s+)(.*)$").matcher(replace);
        if (matcher.matches()) {
            group = matcher.group(1);
        }
        try {
            final Iterator<String> iterator = this.getWrapped(n, replace, group).iterator();
            while (iterator.hasNext()) {
                this.addLine(iterator.next());
            }
        }
        catch (Exception ex) {
            this.add(replace);
        }
        return this;
    }
    
    public PrettyPrinter hr() {
        return this.hr('*');
    }
    
    public PrettyPrinter trace(final Level level) {
        return this.trace(getDefaultLoggerName(), level);
    }
    
    public int wrapTo() {
        return this.wrapWidth;
    }
    
    public PrettyPrinter add(final Object[] array) {
        return this.add(array, "%s");
    }
    
    public PrettyPrinter(final int width) {
        this.horizontalRule = new HorizontalRule(new char[] { '*' });
        this.lines = new ArrayList<Object>();
        this.recalcWidth = false;
        this.width = 100;
        this.wrapWidth = 80;
        this.kvKeyWidth = 10;
        this.kvFormat = makeKvFormat(this.kvKeyWidth);
        this.width = width;
    }
    
    private PrettyPrinter th(final boolean b) {
        if (this.table == null) {
            this.table = new Table();
        }
        if (!b || this.table.addHeader) {
            this.table.headerAdded();
            this.addLine(this.table);
        }
        return this;
    }
    
    public PrettyPrinter log(final Logger logger, final Level level) {
        this.updateWidth();
        this.logSpecial(logger, level, this.horizontalRule);
        for (final ISpecialEntry next : this.lines) {
            if (next instanceof ISpecialEntry) {
                this.logSpecial(logger, level, next);
            }
            else {
                this.logString(logger, level, next.toString());
            }
        }
        this.logSpecial(logger, level, this.horizontalRule);
        return this;
    }
    
    public PrettyPrinter wrapTo(final int wrapWidth) {
        this.wrapWidth = wrapWidth;
        return this;
    }
    
    public PrettyPrinter trace(final String s) {
        return this.trace(System.err, LogManager.getLogger(s));
    }
    
    public PrettyPrinter trace(final PrintStream printStream, final Level level) {
        return this.trace(printStream, getDefaultLoggerName(), level);
    }
    
    private static String makeKvFormat(final int i) {
        return String.format("%%%ds : %%s", i);
    }
    
    public PrettyPrinter kv(final String s, final String format, final Object... args) {
        return this.kv(s, (Object)String.format(format, args));
    }
    
    public PrettyPrinter add(final Throwable t) {
        return this.add(t, 4);
    }
    
    public PrettyPrinter add(final Object[] array, final String s) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.add(s, array[i]);
        }
        return this;
    }
    
    public PrettyPrinter trace(final PrintStream printStream, final String s) {
        return this.trace(printStream, LogManager.getLogger(s));
    }
    
    public PrettyPrinter table(final String... array) {
        this.table = new Table();
        for (int length = array.length, i = 0; i < length; ++i) {
            this.table.addColumn(array[i]);
        }
        return this;
    }
    
    public PrettyPrinter addWithIndices(final Collection<?> collection) {
        return this.addIndexed(collection.toArray());
    }
    
    public PrettyPrinter tr(final Object... array) {
        this.th(true);
        this.addLine(this.table.addRow(array));
        this.recalcWidth = true;
        return this;
    }
    
    private void addLine(final Object o) {
        if (o == null) {
            return;
        }
        this.lines.add(o);
        this.recalcWidth |= (o instanceof IVariableWidthEntry);
    }
    
    interface ISpecialEntry
    {
    }
    
    public interface IPrettyPrintable
    {
        void print(final PrettyPrinter p0);
    }
    
    class KeyValue implements IVariableWidthEntry
    {
        private final /* synthetic */ String key;
        private final /* synthetic */ Object value;
        
        @Override
        public int getWidth() {
            return this.toString().length();
        }
        
        public KeyValue(final String key, final Object value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return String.format(PrettyPrinter.this.kvFormat, this.key, this.value);
        }
    }
    
    interface IVariableWidthEntry
    {
        int getWidth();
    }
    
    static class Table implements IVariableWidthEntry
    {
        final /* synthetic */ List<Column> columns;
        /* synthetic */ String format;
        final /* synthetic */ List<Row> rows;
        /* synthetic */ int colSpacing;
        /* synthetic */ boolean addHeader;
        
        @Override
        public String toString() {
            boolean b = false;
            final String[] array = new String[this.columns.size()];
            for (int i = 0; i < this.columns.size(); ++i) {
                array[i] = this.columns.get(i).toString();
                b |= !array[i].isEmpty();
            }
            return b ? String.format(this.format, (Object[])array) : null;
        }
        
        Column add(final Column column) {
            this.columns.add(column);
            return column;
        }
        
        String getFormat() {
            return this.format;
        }
        
        Row add(final Row row) {
            this.rows.add(row);
            return row;
        }
        
        void updateFormat() {
            final String repeat = Strings.repeat(" ", this.colSpacing);
            final StringBuilder sb = new StringBuilder();
            int n = 0;
            for (final Column column : this.columns) {
                if (n != 0) {
                    sb.append(repeat);
                }
                n = 1;
                sb.append(column.getFormat());
            }
            this.format = sb.toString();
        }
        
        Row addRow(final Object... array) {
            return this.add(new Row(this, array));
        }
        
        Table grow(final int n) {
            while (this.columns.size() < n) {
                this.columns.add(new Column(this));
            }
            this.updateFormat();
            return this;
        }
        
        Column addColumn(final Alignment alignment, final int n, final String s) {
            return this.add(new Column(this, alignment, n, s));
        }
        
        Object[] getTitles() {
            final ArrayList<String> list = new ArrayList<String>();
            final Iterator<Column> iterator = this.columns.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().getTitle());
            }
            return list.toArray();
        }
        
        Column addColumn(final String s) {
            return this.add(new Column(this, s));
        }
        
        void headerAdded() {
            this.addHeader = false;
        }
        
        void setColSpacing(final int b) {
            this.colSpacing = Math.max(0, b);
            this.updateFormat();
        }
        
        Table() {
            this.columns = new ArrayList<Column>();
            this.rows = new ArrayList<Row>();
            this.format = "%s";
            this.colSpacing = 2;
            this.addHeader = true;
        }
        
        @Override
        public int getWidth() {
            final String string = this.toString();
            return (string != null) ? string.length() : 0;
        }
    }
    
    static class Row implements IVariableWidthEntry
    {
        final /* synthetic */ String[] args;
        final /* synthetic */ Table table;
        
        @Override
        public int getWidth() {
            return this.toString().length();
        }
        
        public Row(final Table table, final Object... array) {
            this.table = table.grow(array.length);
            this.args = new String[array.length];
            for (int i = 0; i < array.length; ++i) {
                this.args[i] = array[i].toString();
                this.table.columns.get(i).setMinWidth(this.args[i].length());
            }
        }
        
        @Override
        public String toString() {
            final Object[] args = new Object[this.table.columns.size()];
            for (int i = 0; i < args.length; ++i) {
                final Column column = this.table.columns.get(i);
                if (i >= this.args.length) {
                    args[i] = "";
                }
                else {
                    args[i] = ((this.args[i].length() > column.getMaxWidth()) ? this.args[i].substring(0, column.getMaxWidth()) : this.args[i]);
                }
            }
            return String.format(this.table.format, args);
        }
    }
    
    static class Column
    {
        private /* synthetic */ int maxWidth;
        private /* synthetic */ String format;
        private /* synthetic */ Alignment align;
        private /* synthetic */ int minWidth;
        private /* synthetic */ int size;
        private final /* synthetic */ Table table;
        private /* synthetic */ String title;
        
        @Override
        public String toString() {
            if (this.title.length() > this.maxWidth) {
                return this.title.substring(0, this.maxWidth);
            }
            return this.title;
        }
        
        int getMaxWidth() {
            return this.maxWidth;
        }
        
        Column(final Table table) {
            this.align = Alignment.LEFT;
            this.minWidth = 1;
            this.maxWidth = Integer.MAX_VALUE;
            this.size = 0;
            this.title = "";
            this.format = "%s";
            this.table = table;
        }
        
        private void updateFormat() {
            this.format = "%" + ((this.align == Alignment.RIGHT) ? "" : "-") + Math.min(this.maxWidth, (this.size == 0) ? this.minWidth : this.size) + "s";
            this.table.updateFormat();
        }
        
        void setTitle(final String title) {
            this.title = title;
            this.setWidth(title.length());
        }
        
        void setAlignment(final Alignment align) {
            this.align = align;
            this.updateFormat();
        }
        
        void setWidth(final int size) {
            if (size > this.size) {
                this.size = size;
                this.updateFormat();
            }
        }
        
        void setMaxWidth(final int b) {
            this.size = Math.min(this.size, this.maxWidth);
            this.maxWidth = Math.max(1, b);
            this.updateFormat();
        }
        
        Column(final Table table, final String title) {
            this(table);
            this.title = title;
            this.minWidth = title.length();
            this.updateFormat();
        }
        
        void setMinWidth(final int minWidth) {
            if (minWidth > this.minWidth) {
                this.minWidth = minWidth;
                this.updateFormat();
            }
        }
        
        Column(final Table table, final Alignment align, final int size, final String s) {
            this(table, s);
            this.align = align;
            this.size = size;
        }
        
        String getFormat() {
            return this.format;
        }
        
        String getTitle() {
            return this.title;
        }
    }
    
    public enum Alignment
    {
        LEFT, 
        RIGHT;
    }
    
    class CentredText
    {
        private final /* synthetic */ Object centred;
        
        @Override
        public String toString() {
            final String string = this.centred.toString();
            return String.format("%" + ((PrettyPrinter.this.width - string.length()) / 2 + string.length()) + "s", string);
        }
        
        public CentredText(final Object centred) {
            this.centred = centred;
        }
    }
    
    class HorizontalRule implements ISpecialEntry
    {
        private final /* synthetic */ char[] hrChars;
        
        @Override
        public String toString() {
            return Strings.repeat(new String(this.hrChars), PrettyPrinter.this.width + 2);
        }
        
        public HorizontalRule(final char... hrChars) {
            this.hrChars = hrChars;
        }
    }
}
