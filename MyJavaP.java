package com.objectfrontier.training.demo;

import java.lang.reflect.Modifier;

public class MyJavaP {

    private void run(String[] args) throws Exception {
        // get classname
        String className = args[0];

        // load class
        Class clazz = loadClass(className);

        // print class details
        printClassDetails(clazz);
    }

    private void printClassDetails(Class clazz) {
        // print compilation source
        // Compiled from "String.java"
        log("Compiled from \"%s.java", clazz.getSimpleName());

        // print class header
        printClassHeader(clazz);

        // print fields
        // print constructors
        // print methods
        // print inner classes
        // print class footer
    }

    private void printClassHeader(Class clazz) {
        // public final class java.lang.String implements java.io.Serializable, java.lang.Comparable<java.lang.String>, java.lang.Ch
        int modifier = clazz.getModifiers();
        String modifierPart = parseModifier(modifier);
        String  qualifiedClassName = clazz.getName();
        String inheritanceDetails = getInheritanceDetails(clazz);
        log("%s class %s %s", modifierPart, qualifiedClassName , inheritanceDetails );
    }

    private String getInheritanceDetails(Class clazz) {

        StringBuilder sb = new StringBuilder();

        // if available, add implemented interfaces
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) { sb.append("implements "); }
        for (Class intf : interfaces) {
            sb.append(intf.getName()).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), "");

        // if available, add extended Class
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            if (interfaces.length > 0) { sb.append(" "); }
            sb.append("extebds ").append(superClass.getName());
        }

        return sb.toString();
    }

    private String parseModifier(int modifier) {

        StringBuilder sb = new StringBuilder();

        if (Modifier.isPrivate(modifier))   {  sb.append("private"); }
        if (Modifier.isPublic(modifier))    {  sb.append("public"); }
        if (Modifier.isProtected(modifier)) {  sb.append("protected"); }

        if (Modifier.isFinal(modifier)) {  sb.append(" final"); }
        if (Modifier.isStatic(modifier)) {  sb.append(" static"); }
        if (Modifier.isAbstract(modifier)) {  sb.append(" abstract"); }
        if (Modifier.isSynchronized(modifier)) {  sb.append(" synchronized"); }
        if (Modifier.isTransient(modifier)) {  sb.append(" transient"); }

        return sb.toString();
    }

    private Class loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load class : " + className);
        }
    }

    public static void main(String[] args) {
        try {
            MyJavaP o = new MyJavaP();
            o.run(args);
        } catch (Throwable t) {
            log(t);
        }
    }

    private static void log(Throwable t) {
        t.printStackTrace(System.err);
    }

    private static void log(String format, String... vals) {
        System.out.format(format, vals);
    }
}
