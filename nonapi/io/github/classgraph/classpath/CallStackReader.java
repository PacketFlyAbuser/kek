// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.lang.reflect.Proxy;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.security.AccessController;
import java.security.PrivilegedAction;
import nonapi.io.github.classgraph.utils.VersionFinder;
import nonapi.io.github.classgraph.utils.LogNode;

class CallStackReader
{
    private static /* synthetic */ Class<?>[] callStack;
    
    static Class<?>[] getClassContext(final LogNode logNode) {
        if (CallStackReader.callStack == null) {
            if ((VersionFinder.JAVA_MAJOR_VERSION == 11 && (VersionFinder.JAVA_MINOR_VERSION >= 1 || VersionFinder.JAVA_SUB_VERSION >= 4) && !VersionFinder.JAVA_IS_EA_VERSION) || (VersionFinder.JAVA_MAJOR_VERSION == 12 && (VersionFinder.JAVA_MINOR_VERSION >= 1 || VersionFinder.JAVA_SUB_VERSION >= 2) && !VersionFinder.JAVA_IS_EA_VERSION) || (VersionFinder.JAVA_MAJOR_VERSION == 13 && !VersionFinder.JAVA_IS_EA_VERSION) || VersionFinder.JAVA_MAJOR_VERSION > 13) {
                CallStackReader.callStack = (Class<?>[])AccessController.doPrivileged((PrivilegedAction<Class[]>)new PrivilegedAction<Class<?>[]>() {
                    @Override
                    public Class<?>[] run() {
                        return getCallStackViaStackWalker();
                    }
                });
            }
            if (CallStackReader.callStack == null || CallStackReader.callStack.length == 0) {
                CallStackReader.callStack = (Class<?>[])AccessController.doPrivileged((PrivilegedAction<Class[]>)new PrivilegedAction<Class<?>[]>() {
                    @Override
                    public Class<?>[] run() {
                        return getCallStackViaSecurityManager(logNode);
                    }
                });
            }
            if (CallStackReader.callStack == null || CallStackReader.callStack.length == 0) {
                StackTraceElement[] array = Thread.currentThread().getStackTrace();
                Label_0173: {
                    if (array != null) {
                        if (array.length != 0) {
                            break Label_0173;
                        }
                    }
                    try {
                        throw new Exception();
                    }
                    catch (Exception ex) {
                        array = ex.getStackTrace();
                    }
                }
                final ArrayList<Class<?>> list = new ArrayList<Class<?>>();
                for (final StackTraceElement stackTraceElement : array) {
                    try {
                        list.add(Class.forName(stackTraceElement.getClassName()));
                    }
                    catch (ClassNotFoundException ex2) {}
                    catch (LinkageError linkageError) {}
                }
                if (!list.isEmpty()) {
                    CallStackReader.callStack = list.toArray(new Class[0]);
                }
                else {
                    CallStackReader.callStack = (Class<?>[])new Class[] { CallStackReader.class };
                }
            }
        }
        return CallStackReader.callStack;
    }
    
    private static Class<?>[] getCallStackViaStackWalker() {
        try {
            final Class<?> forName = Class.forName("java.util.function.Consumer");
            final ArrayList list = new ArrayList();
            final Class<?> forName2 = Class.forName("java.lang.StackWalker$Option");
            final Object invoke = Class.forName("java.lang.Enum").getMethod("valueOf", Class.class, String.class).invoke(null, forName2, "RETAIN_CLASS_REFERENCE");
            final Class<?> forName3 = Class.forName("java.lang.StackWalker");
            forName3.getMethod("forEach", forName).invoke(forName3.getMethod("getInstance", forName2).invoke(null, invoke), Proxy.newProxyInstance(forName.getClassLoader(), new Class[] { forName }, new InvocationHandler() {
                final /* synthetic */ Method val$stackFrameGetDeclaringClassMethod = Class.forName("java.lang.StackWalker$StackFrame").getMethod("getDeclaringClass", (Class<?>[])new Class[0]);
                
                @Override
                public Object invoke(final Object o, final Method method, final Object[] array) throws Throwable {
                    list.add(this.val$stackFrameGetDeclaringClassMethod.invoke(array[0], new Object[0]));
                    return null;
                }
            }));
            return (Class<?>[])list.toArray(new Class[0]);
        }
        catch (Exception | LinkageError ex) {
            return null;
        }
    }
    
    private static Class<?>[] getCallStackViaSecurityManager(final LogNode logNode) {
        try {
            return new CallerResolver().getClassContext();
        }
        catch (SecurityException ex) {
            if (logNode != null) {
                logNode.log("Exception while trying to obtain call stack via SecurityManager", ex);
            }
            return null;
        }
    }
    
    private CallStackReader() {
    }
    
    private static final class CallerResolver extends SecurityManager
    {
        @Override
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }
}
