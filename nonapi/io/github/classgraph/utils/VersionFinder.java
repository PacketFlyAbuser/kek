// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.util.Locale;
import java.util.ArrayList;
import org.w3c.dom.Document;
import java.io.InputStream;
import java.nio.file.Path;
import java.net.URL;
import java.util.Properties;
import java.io.IOException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import io.github.classgraph.ClassGraph;

public final class VersionFinder
{
    public static final /* synthetic */ String JAVA_VERSION;
    
    public static String getProperty(final String key) {
        try {
            return System.getProperty(key);
        }
        catch (SecurityException ex) {
            return null;
        }
    }
    
    public static synchronized String getVersion() {
        final Class<ClassGraph> clazz = ClassGraph.class;
        try {
            final String name = clazz.getName();
            final URL resource = clazz.getResource("/" + JarUtils.classNameToClassfilePath(name));
            if (resource != null) {
                final Path parent = Paths.get(resource.toURI()).getParent();
                final int n = name.length() - name.replace(".", "").length();
                Path path = parent;
                for (int n2 = 0; n2 < n && path != null; path = path.getParent(), ++n2) {}
                for (int n3 = 0; n3 < 3 && path != null; ++n3, path = path.getParent()) {
                    final Path resolve = path.resolve("pom.xml");
                    try (final InputStream inputStream = Files.newInputStream(resolve, new OpenOption[0])) {
                        final Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
                        parse.getDocumentElement().normalize();
                        final String s = (String)XPathFactory.newInstance().newXPath().compile("/project/version").evaluate(parse, XPathConstants.STRING);
                        if (s != null) {
                            final String trim = s.trim();
                            if (!trim.isEmpty()) {
                                return trim;
                            }
                        }
                    }
                    catch (IOException ex) {}
                }
            }
        }
        catch (Exception ex2) {}
        try (final InputStream resourceAsStream = clazz.getResourceAsStream("/META-INF/maven/io.github.classgraph/classgraph/pom.properties")) {
            if (resourceAsStream != null) {
                final Properties properties = new Properties();
                properties.load(resourceAsStream);
                final String trim2 = properties.getProperty("version", "").trim();
                if (!trim2.isEmpty()) {
                    return trim2;
                }
            }
        }
        catch (IOException ex3) {}
        final Package package1 = clazz.getPackage();
        if (package1 != null) {
            String implementationVersion = package1.getImplementationVersion();
            if (implementationVersion == null) {
                implementationVersion = "";
            }
            String s2 = implementationVersion.trim();
            if (s2.isEmpty()) {
                String specificationVersion = package1.getSpecificationVersion();
                if (specificationVersion == null) {
                    specificationVersion = "";
                }
                s2 = specificationVersion.trim();
            }
            if (!s2.isEmpty()) {
                return s2;
            }
        }
        return "unknown";
    }
    
    static {
        MAVEN_ARTIFACT = "classgraph";
        MAVEN_PACKAGE = "io.github.classgraph";
        JAVA_VERSION = getProperty("java.version");
        int intValue = 0;
        int intValue2 = 0;
        int intValue3 = 0;
        final ArrayList<Integer> list = new ArrayList<Integer>();
        if (VersionFinder.JAVA_VERSION != null) {
            for (final String s : VersionFinder.JAVA_VERSION.split("[^0-9]+")) {
                try {
                    list.add(Integer.parseInt(s));
                }
                catch (NumberFormatException ex) {}
            }
            if (!list.isEmpty() && (int)list.get(0) == 1) {
                list.remove(0);
            }
            if (list.isEmpty()) {
                throw new RuntimeException("Could not determine Java version: " + VersionFinder.JAVA_VERSION);
            }
            intValue = (int)list.get(0);
            if (list.size() > 1) {
                intValue2 = (int)list.get(1);
            }
            if (list.size() > 2) {
                intValue3 = (int)list.get(2);
            }
        }
        JAVA_MAJOR_VERSION = intValue;
        JAVA_MINOR_VERSION = intValue2;
        JAVA_SUB_VERSION = intValue3;
        JAVA_IS_EA_VERSION = (VersionFinder.JAVA_VERSION != null && VersionFinder.JAVA_VERSION.endsWith("-ea"));
        final String lowerCase = getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH);
        if (lowerCase == null) {
            OS = OperatingSystem.Unknown;
        }
        else if (lowerCase.contains("mac") || lowerCase.contains("darwin")) {
            OS = OperatingSystem.MacOSX;
        }
        else if (lowerCase.contains("win")) {
            OS = OperatingSystem.Windows;
        }
        else if (lowerCase.contains("nux")) {
            OS = OperatingSystem.Linux;
        }
        else if (lowerCase.contains("sunos") || lowerCase.contains("solaris")) {
            OS = OperatingSystem.Solaris;
        }
        else if (lowerCase.contains("bsd")) {
            OS = OperatingSystem.Unix;
        }
        else if (lowerCase.contains("nix") || lowerCase.contains("aix")) {
            OS = OperatingSystem.Unix;
        }
        else {
            OS = OperatingSystem.Unknown;
        }
    }
    
    public static String getProperty(final String key, final String def) {
        try {
            return System.getProperty(key, def);
        }
        catch (SecurityException ex) {
            return null;
        }
    }
    
    private VersionFinder() {
    }
    
    public enum OperatingSystem
    {
        MacOSX, 
        Solaris, 
        Unix, 
        Windows, 
        Unknown, 
        Linux, 
        BSD;
    }
}
