// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.List;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.BitSet;

final class GraphvizDotfileGenerator
{
    private static final /* synthetic */ BitSet IS_UNICODE_WHITESPACE;
    
    private static boolean isUnicodeWhitespace(final char bitIndex) {
        return GraphvizDotfileGenerator.IS_UNICODE_WHITESPACE.get(bitIndex);
    }
    
    static String generateGraphVizDotFileFromInterClassDependencies(final ClassInfoList c, final float f, final float f2, final boolean b) {
        final StringBuilder sb = new StringBuilder(1048576);
        sb.append("digraph {\n");
        sb.append("size=\"").append(f).append(',').append(f2).append("\";\n");
        sb.append("layout=dot;\n");
        sb.append("rankdir=\"BT\";\n");
        sb.append("overlap=false;\n");
        sb.append("splines=true;\n");
        sb.append("pack=true;\n");
        sb.append("graph [fontname = \"Courier, Regular\"]\n");
        sb.append("node [fontname = \"Courier, Regular\"]\n");
        sb.append("edge [fontname = \"Courier, Regular\"]\n");
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>(c);
        if (b) {
            final Iterator iterator = c.iterator();
            while (iterator.hasNext()) {
                set.addAll((Collection<?>)iterator.next().getClassDependencies());
            }
        }
        for (final ClassInfo classInfo : set) {
            sb.append('\"').append(classInfo.getName()).append('\"');
            sb.append("[shape=").append(classInfo.isAnnotation() ? "oval" : (classInfo.isInterface() ? "diamond" : "box")).append(",style=filled,fillcolor=\"#").append(classInfo.isAnnotation() ? "f3c9ff" : (classInfo.isInterface() ? "b6e7ff" : "fff2b6")).append("\",label=");
            sb.append('<');
            sb.append("<table border='0' cellborder='0' cellspacing='1'>");
            sb.append("<tr><td><font point-size='12'>").append(classInfo.getModifiersStr()).append(' ').append(classInfo.isEnum() ? "enum" : (classInfo.isAnnotation() ? "@interface" : (classInfo.isInterface() ? "interface" : "class"))).append("</font></td></tr>");
            if (classInfo.getName().contains(".")) {
                sb.append("<tr><td><font point-size='14'><b>");
                htmlEncode(classInfo.getPackageName(), sb);
                sb.append("</b></font></td></tr>");
            }
            sb.append("<tr><td><font point-size='20'><b>");
            htmlEncode(classInfo.getSimpleName(), sb);
            sb.append("</b></font></td></tr>");
            sb.append("</table>");
            sb.append(">];\n");
        }
        sb.append('\n');
        for (final ClassInfo classInfo2 : c) {
            for (final ClassInfo classInfo3 : classInfo2.getClassDependencies()) {
                if (b || set.contains(classInfo3)) {
                    sb.append("  \"").append(classInfo2.getName()).append("\" -> \"").append(classInfo3.getName()).append("\" [arrowsize=2.5]\n");
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    static {
        STANDARD_CLASS_COLOR = "fff2b6";
        PARAM_WRAP_WIDTH = 40;
        INTERFACE_COLOR = "b6e7ff";
        ANNOTATION_COLOR = "f3c9ff";
        IS_UNICODE_WHITESPACE = new BitSet(65536);
        for (int i = 0; i < " \t\n\u000b\f\r\u0085 \u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u2028\u2029\u202f\u205f\u3000".length(); ++i) {
            GraphvizDotfileGenerator.IS_UNICODE_WHITESPACE.set(" \t\n\u000b\f\r\u0085 \u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u2028\u2029\u202f\u205f\u3000".charAt(i));
        }
    }
    
    private static void htmlEncode(final CharSequence charSequence, final boolean b, final StringBuilder sb) {
        for (int i = 0; i < charSequence.length(); ++i) {
            final char char1 = charSequence.charAt(i);
            switch (char1) {
                case 38: {
                    sb.append("&amp;");
                    break;
                }
                case 60: {
                    sb.append("&lt;");
                    break;
                }
                case 62: {
                    sb.append("&gt;");
                    break;
                }
                case 34: {
                    sb.append("&quot;");
                    break;
                }
                case 39: {
                    sb.append("&#x27;");
                    break;
                }
                case 92: {
                    sb.append("&lsol;");
                    break;
                }
                case 47: {
                    sb.append("&#x2F;");
                    break;
                }
                case 8212: {
                    sb.append("&mdash;");
                    break;
                }
                case 8211: {
                    sb.append("&ndash;");
                    break;
                }
                case 8220: {
                    sb.append("&ldquo;");
                    break;
                }
                case 8221: {
                    sb.append("&rdquo;");
                    break;
                }
                case 8216: {
                    sb.append("&lsquo;");
                    break;
                }
                case 8217: {
                    sb.append("&rsquo;");
                    break;
                }
                case 171: {
                    sb.append("&laquo;");
                    break;
                }
                case 187: {
                    sb.append("&raquo;");
                    break;
                }
                case 163: {
                    sb.append("&pound;");
                    break;
                }
                case 169: {
                    sb.append("&copy;");
                    break;
                }
                case 174: {
                    sb.append("&reg;");
                    break;
                }
                case 160: {
                    sb.append("&nbsp;");
                    break;
                }
                case 10: {
                    if (b) {
                        sb.append("<br>");
                        break;
                    }
                    sb.append(' ');
                    break;
                }
                default: {
                    if (char1 <= ' ' || isUnicodeWhitespace(char1)) {
                        sb.append(' ');
                        break;
                    }
                    sb.append(char1);
                    break;
                }
            }
        }
    }
    
    private static void htmlEncode(final CharSequence charSequence, final StringBuilder sb) {
        htmlEncode(charSequence, false, sb);
    }
    
    static String generateGraphVizDotFile(final ClassInfoList list, final float f, final float f2, final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6, final ScanSpec scanSpec) {
        final StringBuilder sb = new StringBuilder(1048576);
        sb.append("digraph {\n");
        sb.append("size=\"").append(f).append(',').append(f2).append("\";\n");
        sb.append("layout=dot;\n");
        sb.append("rankdir=\"BT\";\n");
        sb.append("overlap=false;\n");
        sb.append("splines=true;\n");
        sb.append("pack=true;\n");
        sb.append("graph [fontname = \"Courier, Regular\"]\n");
        sb.append("node [fontname = \"Courier, Regular\"]\n");
        sb.append("edge [fontname = \"Courier, Regular\"]\n");
        final ClassInfoList standardClasses = list.getStandardClasses();
        final ClassInfoList interfaces = list.getInterfaces();
        final ClassInfoList annotations = list.getAnnotations();
        for (final ClassInfo classInfo : standardClasses) {
            sb.append('\"').append(classInfo.getName()).append('\"');
            labelClassNodeHTML(classInfo, "box", "fff2b6", b, b3, b6, scanSpec, sb);
            sb.append(";\n");
        }
        for (final ClassInfo classInfo2 : interfaces) {
            sb.append('\"').append(classInfo2.getName()).append('\"');
            labelClassNodeHTML(classInfo2, "diamond", "b6e7ff", b, b3, b6, scanSpec, sb);
            sb.append(";\n");
        }
        for (final ClassInfo classInfo3 : annotations) {
            sb.append('\"').append(classInfo3.getName()).append('\"');
            labelClassNodeHTML(classInfo3, "oval", "f3c9ff", b, b3, b6, scanSpec, sb);
            sb.append(";\n");
        }
        final HashSet set = new HashSet();
        set.addAll(standardClasses.getNames());
        set.addAll(interfaces.getNames());
        set.addAll(annotations.getNames());
        sb.append('\n');
        for (final ClassInfo classInfo4 : standardClasses) {
            for (final ClassInfo classInfo5 : classInfo4.getSuperclasses().directOnly()) {
                if (classInfo5 != null && set.contains(classInfo5.getName()) && !classInfo5.getName().equals("java.lang.Object")) {
                    sb.append("  \"").append(classInfo4.getName()).append("\" -> \"").append(classInfo5.getName()).append("\" [arrowsize=2.5]\n");
                }
            }
            for (final ClassInfo classInfo6 : classInfo4.getInterfaces().directOnly()) {
                if (set.contains(classInfo6.getName())) {
                    sb.append("  \"").append(classInfo4.getName()).append("\" -> \"").append(classInfo6.getName()).append("\" [arrowhead=diamond, arrowsize=2.5]\n");
                }
            }
            if (b2 && classInfo4.fieldInfo != null) {
                final Iterator iterator7 = classInfo4.fieldInfo.iterator();
                while (iterator7.hasNext()) {
                    for (final ClassInfo classInfo7 : iterator7.next().findReferencedClassInfo()) {
                        if (set.contains(classInfo7.getName())) {
                            sb.append("  \"").append(classInfo7.getName()).append("\" -> \"").append(classInfo4.getName()).append("\" [arrowtail=obox, arrowsize=2.5, dir=back]\n");
                        }
                    }
                }
            }
            if (b4 && classInfo4.methodInfo != null) {
                final Iterator iterator9 = classInfo4.methodInfo.iterator();
                while (iterator9.hasNext()) {
                    for (final ClassInfo classInfo8 : iterator9.next().findReferencedClassInfo()) {
                        if (set.contains(classInfo8.getName())) {
                            sb.append("  \"").append(classInfo8.getName()).append("\" -> \"").append(classInfo4.getName()).append("\" [arrowtail=box, arrowsize=2.5, dir=back]\n");
                        }
                    }
                }
            }
        }
        for (final ClassInfo classInfo9 : interfaces) {
            for (final ClassInfo classInfo10 : classInfo9.getInterfaces().directOnly()) {
                if (set.contains(classInfo10.getName())) {
                    sb.append("  \"").append(classInfo9.getName()).append("\" -> \"").append(classInfo10.getName()).append("\" [arrowhead=diamond, arrowsize=2.5]\n");
                }
            }
        }
        if (b5) {
            for (final ClassInfo classInfo11 : annotations) {
                for (final ClassInfo classInfo12 : classInfo11.getClassesWithAnnotationDirectOnly()) {
                    if (set.contains(classInfo12.getName())) {
                        sb.append("  \"").append(classInfo12.getName()).append("\" -> \"").append(classInfo11.getName()).append("\" [arrowhead=dot, arrowsize=2.5]\n");
                    }
                }
                for (final ClassInfo classInfo13 : classInfo11.getClassesWithMethodAnnotationDirectOnly()) {
                    if (set.contains(classInfo13.getName())) {
                        sb.append("  \"").append(classInfo13.getName()).append("\" -> \"").append(classInfo11.getName()).append("\" [arrowhead=odot, arrowsize=2.5]\n");
                    }
                }
                for (final ClassInfo classInfo14 : classInfo11.getClassesWithFieldAnnotationDirectOnly()) {
                    if (set.contains(classInfo14.getName())) {
                        sb.append("  \"").append(classInfo14.getName()).append("\" -> \"").append(classInfo11.getName()).append("\" [arrowhead=odot, arrowsize=2.5]\n");
                    }
                }
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    private static void labelClassNodeHTML(final ClassInfo classInfo, final String str, final String str2, final boolean b, final boolean b2, final boolean b3, final ScanSpec scanSpec, final StringBuilder sb) {
        sb.append("[shape=").append(str).append(",style=filled,fillcolor=\"#").append(str2).append("\",label=");
        sb.append('<');
        sb.append("<table border='0' cellborder='0' cellspacing='1'>");
        sb.append("<tr><td><font point-size='12'>").append(classInfo.getModifiersStr()).append(' ').append(classInfo.isEnum() ? "enum" : (classInfo.isAnnotation() ? "@interface" : (classInfo.isInterface() ? "interface" : "class"))).append("</font></td></tr>");
        if (classInfo.getName().contains(".")) {
            sb.append("<tr><td><font point-size='14'><b>");
            htmlEncode(classInfo.getPackageName() + ".", sb);
            sb.append("</b></font></td></tr>");
        }
        sb.append("<tr><td><font point-size='20'><b>");
        htmlEncode(classInfo.getSimpleName(), sb);
        sb.append("</b></font></td></tr>");
        final int n = (int)(Integer.parseInt(str2.substring(0, 2), 16) * 0.8f);
        final int n2 = (int)(Integer.parseInt(str2.substring(2, 4), 16) * 0.8f);
        final int n3 = (int)(Integer.parseInt(str2.substring(4, 6), 16) * 0.8f);
        final String format = String.format("#%s%s%s%s%s%s", Integer.toString(n >> 4, 16), Integer.toString(n & 0xF, 16), Integer.toString(n2 >> 4, 16), Integer.toString(n2 & 0xF, 16), Integer.toString(n3 >> 4, 16), Integer.toString(n3 & 0xF, 16));
        final AnnotationInfoList annotationInfo = classInfo.annotationInfo;
        if (annotationInfo != null && !annotationInfo.isEmpty()) {
            sb.append("<tr><td colspan='3' bgcolor='").append(format).append("'><font point-size='12'><b>ANNOTATIONS</b></font></td></tr>");
            final AnnotationInfoList list = new AnnotationInfoList(annotationInfo);
            CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
            for (final AnnotationInfo annotationInfo2 : list) {
                if (!annotationInfo2.getName().startsWith("java.lang.annotation.")) {
                    sb.append("<tr>");
                    sb.append("<td align='center' valign='top'>");
                    htmlEncode(annotationInfo2.toString(), sb);
                    sb.append("</td></tr>");
                }
            }
        }
        final FieldInfoList fieldInfo = classInfo.fieldInfo;
        if (b && fieldInfo != null && !fieldInfo.isEmpty()) {
            final FieldInfoList list2 = new FieldInfoList(fieldInfo);
            CollectionUtils.sortIfNotEmpty((List<Comparable>)list2);
            for (int i = list2.size() - 1; i >= 0; --i) {
                if (list2.get(i).getName().equals("serialVersionUID")) {
                    ((PotentiallyUnmodifiableList<Object>)list2).remove(i);
                }
            }
            if (!list2.isEmpty()) {
                sb.append("<tr><td colspan='3' bgcolor='").append(format).append("'><font point-size='12'><b>").append(scanSpec.ignoreFieldVisibility ? "" : "PUBLIC ").append("FIELDS</b></font></td></tr>");
                sb.append("<tr><td cellpadding='0'>");
                sb.append("<table border='0' cellborder='0'>");
                for (final FieldInfo fieldInfo2 : list2) {
                    sb.append("<tr>");
                    sb.append("<td align='right' valign='top'>");
                    final AnnotationInfoList annotationInfo3 = fieldInfo2.annotationInfo;
                    if (annotationInfo3 != null) {
                        for (final AnnotationInfo annotationInfo4 : annotationInfo3) {
                            if (sb.charAt(sb.length() - 1) != ' ') {
                                sb.append(' ');
                            }
                            htmlEncode(annotationInfo4.toString(), sb);
                        }
                    }
                    if (scanSpec.ignoreFieldVisibility) {
                        if (sb.charAt(sb.length() - 1) != ' ') {
                            sb.append(' ');
                        }
                        sb.append(fieldInfo2.getModifierStr());
                    }
                    if (sb.charAt(sb.length() - 1) != ' ') {
                        sb.append(' ');
                    }
                    final TypeSignature typeSignatureOrTypeDescriptor = fieldInfo2.getTypeSignatureOrTypeDescriptor();
                    htmlEncode(b3 ? typeSignatureOrTypeDescriptor.toStringWithSimpleNames() : typeSignatureOrTypeDescriptor.toString(), sb);
                    sb.append("</td>");
                    sb.append("<td align='left' valign='top'><b>");
                    htmlEncode(fieldInfo2.getName(), sb);
                    sb.append("</b></td></tr>");
                }
                sb.append("</table>");
                sb.append("</td></tr>");
            }
        }
        final MethodInfoList methodInfo = classInfo.methodInfo;
        if (b2 && methodInfo != null) {
            final MethodInfoList list3 = new MethodInfoList(methodInfo);
            CollectionUtils.sortIfNotEmpty((List<Comparable>)list3);
            for (int j = list3.size() - 1; j >= 0; --j) {
                final MethodInfo methodInfo2 = list3.get(j);
                final String name = methodInfo2.getName();
                final int length = methodInfo2.getParameterInfo().length;
                if (name.equals("<clinit>") || (name.equals("hashCode") && length == 0) || (name.equals("toString") && length == 0) || (name.equals("equals") && length == 1 && methodInfo2.getTypeDescriptor().toString().equals("boolean (java.lang.Object)"))) {
                    ((PotentiallyUnmodifiableList<Object>)list3).remove(j);
                }
            }
            if (!list3.isEmpty()) {
                sb.append("<tr><td cellpadding='0'>");
                sb.append("<table border='0' cellborder='0'>");
                sb.append("<tr><td colspan='3' bgcolor='").append(format).append("'><font point-size='12'><b>").append(scanSpec.ignoreMethodVisibility ? "" : "PUBLIC ").append("METHODS</b></font></td></tr>");
                for (final MethodInfo methodInfo3 : list3) {
                    sb.append("<tr>");
                    sb.append("<td align='right' valign='top'>");
                    final AnnotationInfoList annotationInfo5 = methodInfo3.annotationInfo;
                    if (annotationInfo5 != null) {
                        for (final AnnotationInfo annotationInfo6 : annotationInfo5) {
                            if (sb.charAt(sb.length() - 1) != ' ') {
                                sb.append(' ');
                            }
                            htmlEncode(annotationInfo6.toString(), sb);
                        }
                    }
                    if (scanSpec.ignoreMethodVisibility) {
                        if (sb.charAt(sb.length() - 1) != ' ') {
                            sb.append(' ');
                        }
                        sb.append(methodInfo3.getModifiersStr());
                    }
                    if (sb.charAt(sb.length() - 1) != ' ') {
                        sb.append(' ');
                    }
                    if (!methodInfo3.getName().equals("<init>")) {
                        final TypeSignature resultType = methodInfo3.getTypeSignatureOrTypeDescriptor().getResultType();
                        htmlEncode(b3 ? resultType.toStringWithSimpleNames() : resultType.toString(), sb);
                    }
                    else {
                        sb.append("<b>&lt;constructor&gt;</b>");
                    }
                    sb.append("</td>");
                    sb.append("<td align='left' valign='top'>");
                    sb.append("<b>");
                    if (methodInfo3.getName().equals("<init>")) {
                        htmlEncode(classInfo.getSimpleName(), sb);
                    }
                    else {
                        htmlEncode(methodInfo3.getName(), sb);
                    }
                    sb.append("</b>&nbsp;");
                    sb.append("</td>");
                    sb.append("<td align='left' valign='top'>");
                    sb.append('(');
                    final MethodParameterInfo[] parameterInfo = methodInfo3.getParameterInfo();
                    if (parameterInfo.length != 0) {
                        int k = 0;
                        int n4 = 0;
                        while (k < parameterInfo.length) {
                            if (k > 0) {
                                sb.append(", ");
                                n4 += 2;
                            }
                            if (n4 > 40) {
                                sb.append("</td></tr><tr><td></td><td></td><td align='left' valign='top'>");
                                n4 = 0;
                            }
                            final AnnotationInfo[] annotationInfo7 = parameterInfo[k].annotationInfo;
                            if (annotationInfo7 != null) {
                                final AnnotationInfo[] array = annotationInfo7;
                                for (int length2 = array.length, l = 0; l < length2; ++l) {
                                    final String string = array[l].toString();
                                    if (!string.isEmpty()) {
                                        if (sb.charAt(sb.length() - 1) != ' ') {
                                            sb.append(' ');
                                        }
                                        htmlEncode(string, sb);
                                        n4 += 1 + string.length();
                                        if (n4 > 40) {
                                            sb.append("</td></tr><tr><td></td><td></td><td align='left' valign='top'>");
                                            n4 = 0;
                                        }
                                    }
                                }
                            }
                            final TypeSignature typeSignatureOrTypeDescriptor2 = parameterInfo[k].getTypeSignatureOrTypeDescriptor();
                            final String s = b3 ? typeSignatureOrTypeDescriptor2.toStringWithSimpleNames() : typeSignatureOrTypeDescriptor2.toString();
                            htmlEncode(s, sb);
                            n4 += s.length();
                            final String name2 = parameterInfo[k].getName();
                            if (name2 != null) {
                                sb.append(" <B>");
                                htmlEncode(name2, sb);
                                n4 += 1 + name2.length();
                                sb.append("</B>");
                            }
                            ++k;
                        }
                    }
                    sb.append(')');
                    sb.append("</td></tr>");
                }
                sb.append("</table>");
                sb.append("</td></tr>");
            }
        }
        sb.append("</table>");
        sb.append(">]");
    }
    
    private GraphvizDotfileGenerator() {
    }
}
