// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CancellationException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.Iterator;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkQueue<T> implements AutoCloseable
{
    private final /* synthetic */ int numWorkers;
    private final /* synthetic */ ConcurrentLinkedQueue<Future<?>> workerFutures;
    private final /* synthetic */ WorkUnitProcessor<T> workUnitProcessor;
    private final /* synthetic */ AtomicInteger numIncompleteWorkUnits;
    private final /* synthetic */ InterruptionChecker interruptionChecker;
    private final /* synthetic */ LogNode log;
    private final /* synthetic */ BlockingQueue<WorkUnitWrapper<T>> workUnits;
    
    public void addWorkUnits(final Collection<T> collection) {
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.addWorkUnit(iterator.next());
        }
    }
    
    private void runWorkLoop() throws ExecutionException, InterruptedException {
        while (true) {
            this.interruptionChecker.check();
            final WorkUnitWrapper<T> workUnitWrapper = this.workUnits.take();
            if (workUnitWrapper.workUnit == null) {
                break;
            }
            try {
                this.workUnitProcessor.processWorkUnit(workUnitWrapper.workUnit, this, this.log);
            }
            catch (InterruptedException | OutOfMemoryError ex) {
                final Object o2;
                final Object o = o2;
                this.workUnits.clear();
                this.sendPoisonPills();
                throw o;
            }
            catch (RuntimeException cause) {
                this.workUnits.clear();
                this.sendPoisonPills();
                throw new ExecutionException("Worker thread threw unchecked exception", cause);
            }
            finally {
                if (this.numIncompleteWorkUnits.decrementAndGet() == 0) {
                    this.sendPoisonPills();
                }
            }
        }
    }
    
    private WorkQueue(final Collection<T> collection, final WorkUnitProcessor<T> workUnitProcessor, final int numWorkers, final InterruptionChecker interruptionChecker, final LogNode log) {
        this.workUnits = new LinkedBlockingQueue<WorkUnitWrapper<T>>();
        this.numIncompleteWorkUnits = new AtomicInteger();
        this.workerFutures = new ConcurrentLinkedQueue<Future<?>>();
        this.workUnitProcessor = workUnitProcessor;
        this.numWorkers = numWorkers;
        this.interruptionChecker = interruptionChecker;
        this.log = log;
        this.addWorkUnits(collection);
    }
    
    public void addWorkUnit(final T t) {
        if (t == null) {
            throw new NullPointerException("workUnit cannot be null");
        }
        this.numIncompleteWorkUnits.incrementAndGet();
        this.workUnits.add(new WorkUnitWrapper<T>(t));
    }
    
    @Override
    public void close() throws ExecutionException {
        Future<?> future;
        while ((future = this.workerFutures.poll()) != null) {
            try {
                future.get();
            }
            catch (CancellationException ex) {
                if (this.log == null) {
                    continue;
                }
                this.log.log("~", "Worker thread was cancelled");
            }
            catch (InterruptedException ex2) {
                if (this.log != null) {
                    this.log.log("~", "Worker thread was interrupted");
                }
                this.interruptionChecker.interrupt();
            }
            catch (ExecutionException executionException) {
                this.interruptionChecker.setExecutionException(executionException);
                this.interruptionChecker.interrupt();
            }
        }
    }
    
    private void startWorkers(final ExecutorService executorService, final int n) {
        for (int i = 0; i < n; ++i) {
            this.workerFutures.add(executorService.submit((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    WorkQueue.this.runWorkLoop();
                    return null;
                }
            }));
        }
    }
    
    private void sendPoisonPills() {
        for (int i = 0; i < this.numWorkers; ++i) {
            this.workUnits.add(new WorkUnitWrapper<T>(null));
        }
    }
    
    public static <U> void runWorkQueue(final Collection<U> collection, final ExecutorService executorService, final InterruptionChecker interruptionChecker, final int n, final LogNode logNode, final WorkUnitProcessor<U> workUnitProcessor) throws ExecutionException, InterruptedException {
        if (collection.isEmpty()) {
            return;
        }
        try (final WorkQueue workQueue = new WorkQueue((Collection<T>)collection, (WorkUnitProcessor<T>)workUnitProcessor, n, interruptionChecker, logNode)) {
            workQueue.startWorkers(executorService, n - 1);
            workQueue.runWorkLoop();
        }
    }
    
    public interface WorkUnitProcessor<T>
    {
        void processWorkUnit(final T p0, final WorkQueue<T> p1, final LogNode p2) throws InterruptedException;
    }
    
    private static class WorkUnitWrapper<T>
    {
        final /* synthetic */ T workUnit;
        
        public WorkUnitWrapper(final T workUnit) {
            this.workUnit = workUnit;
        }
    }
}
