// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptionChecker
{
    private final /* synthetic */ AtomicBoolean interrupted;
    private final /* synthetic */ AtomicReference<ExecutionException> thrownExecutionException;
    
    public boolean checkAndReturn() {
        if (this.interrupted.get()) {
            this.interrupt();
            return true;
        }
        if (Thread.currentThread().isInterrupted()) {
            this.interrupted.set(true);
            return true;
        }
        return false;
    }
    
    public ExecutionException getExecutionException() {
        return this.thrownExecutionException.get();
    }
    
    public void interrupt() {
        this.interrupted.set(true);
        Thread.currentThread().interrupt();
    }
    
    public static Throwable getCause(final Throwable t) {
        Throwable cause;
        for (cause = t; cause instanceof ExecutionException; cause = cause.getCause()) {}
        return (cause != null) ? cause : new ExecutionException("ExecutionException with unknown cause", null);
    }
    
    public InterruptionChecker() {
        this.interrupted = new AtomicBoolean(false);
        this.thrownExecutionException = new AtomicReference<ExecutionException>();
    }
    
    public void setExecutionException(final ExecutionException newValue) {
        if (newValue != null && this.thrownExecutionException.get() == null) {
            this.thrownExecutionException.compareAndSet(null, newValue);
        }
    }
    
    public void check() throws InterruptedException, ExecutionException {
        final ExecutionException executionException = this.getExecutionException();
        if (executionException != null) {
            throw executionException;
        }
        if (this.checkAndReturn()) {
            throw new InterruptedException();
        }
    }
}
