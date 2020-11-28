// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class SimpleThreadFactory implements ThreadFactory
{
    private final /* synthetic */ String threadNamePrefix;
    private static final /* synthetic */ AtomicInteger threadIdx;
    private final /* synthetic */ boolean daemon;
    
    @Override
    public Thread newThread(final Runnable target) {
        final SecurityManager securityManager = System.getSecurityManager();
        final Thread thread = new Thread((securityManager != null) ? securityManager.getThreadGroup() : new ThreadGroup("ClassGraph-thread-group"), target, this.threadNamePrefix + SimpleThreadFactory.threadIdx.getAndIncrement());
        thread.setDaemon(this.daemon);
        return thread;
    }
    
    static {
        threadIdx = new AtomicInteger();
    }
    
    SimpleThreadFactory(final String threadNamePrefix, final boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }
}
