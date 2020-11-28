// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.concurrency;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import io.github.classgraph.ClassGraphException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public class AutoCloseableExecutorService extends ThreadPoolExecutor implements AutoCloseable
{
    public final /* synthetic */ InterruptionChecker interruptionChecker;
    
    @Override
    public void close() {
        try {
            this.shutdown();
        }
        catch (SecurityException ex2) {}
        boolean awaitTermination = false;
        try {
            awaitTermination = this.awaitTermination(2500L, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ex3) {
            this.interruptionChecker.interrupt();
        }
        if (!awaitTermination) {
            try {
                this.shutdownNow();
            }
            catch (SecurityException ex) {
                throw ClassGraphException.newClassGraphException("Could not shut down ExecutorService -- need java.lang.RuntimePermission(\"modifyThread\"), or the security manager's checkAccess method denies access", ex);
            }
        }
    }
    
    public AutoCloseableExecutorService(final int n) {
        super(n, n, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new SimpleThreadFactory("ClassGraph-worker-", true));
        this.interruptionChecker = new InterruptionChecker();
    }
    
    public void afterExecute(final Runnable r, final Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            this.interruptionChecker.setExecutionException(new ExecutionException("Uncaught exception", t));
            this.interruptionChecker.interrupt();
        }
        else if (r instanceof Future) {
            try {
                ((Future)r).get();
            }
            catch (CancellationException | InterruptedException ex) {
                this.interruptionChecker.interrupt();
            }
            catch (ExecutionException executionException) {
                this.interruptionChecker.setExecutionException(executionException);
                this.interruptionChecker.interrupt();
            }
        }
    }
}
