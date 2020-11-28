// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.util.Calendar;
import nonapi.io.github.classgraph.classpath.SystemJarFinder;
import java.util.Iterator;
import java.util.Collection;
import java.util.Locale;
import io.github.classgraph.ClassGraph;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentSkipListMap;
import java.text.DecimalFormat;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class LogNode
{
    private static /* synthetic */ boolean logInRealtime;
    private static final /* synthetic */ Logger log;
    private final /* synthetic */ long timeStampMillis;
    private /* synthetic */ long elapsedTimeNanos;
    private final /* synthetic */ String msg;
    private final /* synthetic */ String sortKeyPrefix;
    private static /* synthetic */ AtomicInteger sortKeyUniqueSuffix;
    private static final /* synthetic */ SimpleDateFormat dateTimeFormatter;
    private final /* synthetic */ long timeStampNano;
    private final /* synthetic */ Map<String, LogNode> children;
    private static final /* synthetic */ DecimalFormat nanoFormatter;
    private /* synthetic */ LogNode parent;
    private /* synthetic */ String stackTrace;
    
    public LogNode log(final String s, final Throwable t) {
        return this.addChild("", s, -1L).addChild(t);
    }
    
    public LogNode log(final Throwable t) {
        return this.log("Exception thrown").addChild(t);
    }
    
    public LogNode log(final String s) {
        return this.addChild("", s, -1L);
    }
    
    private LogNode addChild(final Throwable t) {
        return this.addChild("", "", -1L, t);
    }
    
    private LogNode(final String sortKeyPrefix, final String msg, final long elapsedTimeNanos, final Throwable t) {
        this.timeStampNano = System.nanoTime();
        this.timeStampMillis = System.currentTimeMillis();
        this.children = new ConcurrentSkipListMap<String, LogNode>();
        this.sortKeyPrefix = sortKeyPrefix;
        this.msg = msg;
        this.elapsedTimeNanos = elapsedTimeNanos;
        if (t != null) {
            final StringWriter out = new StringWriter();
            t.printStackTrace(new PrintWriter(out));
            this.stackTrace = out.toString();
        }
        else {
            this.stackTrace = null;
        }
        if (LogNode.logInRealtime) {
            LogNode.log.info(this.toString());
        }
    }
    
    public LogNode log(final String s, final String s2, final Throwable t) {
        return this.addChild(s, s2, -1L).addChild(t);
    }
    
    public LogNode log(final String s, final long n, final Throwable t) {
        return this.addChild("", s, n).addChild(t);
    }
    
    public LogNode() {
        this("", "", -1L, null);
        this.log("ClassGraph version " + VersionFinder.getVersion());
        this.logJavaInfo();
    }
    
    public LogNode log(final String s, final String s2, final long n, final Throwable t) {
        return this.addChild(s, s2, n).addChild(t);
    }
    
    public static void logInRealtime(final boolean logInRealtime) {
        LogNode.logInRealtime = logInRealtime;
    }
    
    static {
        log = Logger.getLogger(ClassGraph.class.getName());
        LogNode.sortKeyUniqueSuffix = new AtomicInteger(0);
        dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ", Locale.US);
        nanoFormatter = new DecimalFormat("0.000000");
    }
    
    public LogNode log(final String s, final String s2) {
        return this.addChild(s, s2, -1L);
    }
    
    public LogNode log(final String s, final String s2, final long n) {
        return this.addChild(s, s2, n);
    }
    
    public LogNode log(final String s, final long n) {
        return this.addChild("", s, n);
    }
    
    private LogNode addChild(final String s, final String s2, final long n) {
        return this.addChild(s, s2, n, null);
    }
    
    public LogNode log(final Collection<String> collection) {
        LogNode log = null;
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            log = this.log(iterator.next());
        }
        return log;
    }
    
    public void addElapsedTime() {
        this.elapsedTimeNanos = System.nanoTime() - this.timeStampNano;
    }
    
    private void logJavaInfo() {
        this.log("Operating system: " + VersionFinder.getProperty("os.name") + " " + VersionFinder.getProperty("os.version") + " " + VersionFinder.getProperty("os.arch"));
        this.log("Java version: " + VersionFinder.getProperty("java.version") + " / " + VersionFinder.getProperty("java.runtime.version") + " (" + VersionFinder.getProperty("java.vendor") + ")");
        this.log("Java home: " + VersionFinder.getProperty("java.home"));
        final String jreRtJarPath = SystemJarFinder.getJreRtJarPath();
        if (jreRtJarPath != null) {
            this.log("JRE rt.jar:").log(jreRtJarPath);
        }
    }
    
    private void toString(final int n, final StringBuilder sb) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(this.timeStampMillis);
        final String format;
        synchronized (LogNode.dateTimeFormatter) {
            format = LogNode.dateTimeFormatter.format(instance.getTime());
        }
        if (this.msg != null && !this.msg.isEmpty()) {
            this.appendLine(format, n, (this.elapsedTimeNanos > 0L) ? (this.msg + " (took " + LogNode.nanoFormatter.format(this.elapsedTimeNanos * 1.0E-9) + " sec)") : this.msg, sb);
        }
        if (this.stackTrace != null && !this.stackTrace.isEmpty()) {
            final String[] split = this.stackTrace.split("\n");
            for (int length = split.length, i = 0; i < length; ++i) {
                this.appendLine(format, n, split[i], sb);
            }
        }
        final Iterator<Map.Entry<String, LogNode>> iterator = this.children.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().getValue().toString(n + 1, sb);
        }
    }
    
    private void appendLine(final String str, final int n, final String str2, final StringBuilder sb) {
        sb.append(str);
        sb.append('\t');
        sb.append(ClassGraph.class.getSimpleName());
        sb.append('\t');
        final int n2 = 2 * (n - 1);
        for (int i = 0; i < n2; ++i) {
            sb.append('-');
        }
        if (n2 > 0) {
            sb.append(' ');
        }
        sb.append(str2);
        sb.append('\n');
    }
    
    private LogNode addChild(final String s, final String s2, final long n, final Throwable t) {
        final String string = this.sortKeyPrefix + "\t" + ((s == null) ? "" : s) + "\t" + String.format("%09d", LogNode.sortKeyUniqueSuffix.getAndIncrement());
        final LogNode logNode = new LogNode(string, s2, n, t);
        logNode.parent = this;
        this.children.put(string, logNode);
        return logNode;
    }
    
    public void flush() {
        if (this.parent != null) {
            throw new IllegalArgumentException("Only flush the toplevel LogNode");
        }
        if (!this.children.isEmpty()) {
            final String string = this.toString();
            this.children.clear();
            LogNode.log.info(string);
        }
    }
    
    @Override
    public String toString() {
        synchronized (LogNode.dateTimeFormatter) {
            final StringBuilder sb = new StringBuilder();
            this.toString(0, sb);
            return sb.toString();
        }
    }
}
