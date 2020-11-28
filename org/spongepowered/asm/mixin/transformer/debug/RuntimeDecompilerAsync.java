// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.debug;

import java.util.concurrent.LinkedBlockingQueue;
import java.io.File;
import java.util.concurrent.BlockingQueue;

public class RuntimeDecompilerAsync extends RuntimeDecompiler implements Runnable, Thread.UncaughtExceptionHandler
{
    private final /* synthetic */ BlockingQueue<File> queue;
    private final /* synthetic */ Thread thread;
    private /* synthetic */ boolean run;
    
    private void flush() {
        this.run = false;
        File file;
        while ((file = this.queue.poll()) != null) {
            this.decompile(file);
        }
    }
    
    @Override
    public void decompile(final File file) {
        if (this.run) {
            this.queue.offer(file);
        }
        else {
            super.decompile(file);
        }
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        this.logger.error("Async decompiler encountered an error and will terminate. Further decompile requests will be handled synchronously. {} {}", new Object[] { t.getClass().getName(), t.getMessage() });
        this.flush();
    }
    
    public RuntimeDecompilerAsync(final File file) {
        super(file);
        this.queue = new LinkedBlockingQueue<File>();
        this.run = true;
        this.thread = new Thread(this, "Decompiler thread");
        this.thread.setDaemon(true);
        this.thread.setPriority(1);
        this.thread.setUncaughtExceptionHandler(this);
        this.thread.start();
    }
    
    @Override
    public void run() {
        while (this.run) {
            try {
                super.decompile(this.queue.take());
            }
            catch (InterruptedException ex2) {
                this.run = false;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
