package com.ofs.training;

import java.lang.reflect.Modifier;

public class MyJavaP {

    public static void main(String[] args) {

        String classPackage = args[0];
        Class clazz= loadClass(classPackage);
        printClassDetails(clazz);
    }

	private static void printClassDetails(Class clazz) {
		
		System.out.println("Compiled from " + "\"" + clazz.getSimpleName() + ".java" + "\"");
		
		//print superclass
		printSuperClass(clazz);
		
		//print Interfaces
		printInterface(clazz);
		
		//print Constructors
		printConstructor(clazz);
		
		//print Fields
		printField(clazz);
		
		//print Methods
		printMethod(clazz);
		
	}

	private static void printSuperClass(Class clazz) {

		Class<?> classSuperClass = clazz.getSuperclass();
		
			StringBuilder sb =  new StringBuilder();
				sb.append(classSuperClass);
				System.out.println("," + sb );
	}

	private static void printMethod(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	private static void printField(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	private static void printConstructor(Class clazz) {
		// TODO Auto-generated method stub
		
	}

	private static void printInterface(Class clazz) {
		
		//getModifier()
		int modifier = clazz.getModifiers();
		String classModifier = getModifier(modifier);
		System.out.println(classModifier + "class " + clazz.getName());
		
		Class<?> classInterfaces[] = clazz.getInterfaces();
		if (classInterfaces.length > 0) {
			
			System.out.println("implements");
			
			StringBuilder sb =  new StringBuilder();
			for ( Class<?> classInterface : classInterfaces) {
				sb.append(classInterface);
				System.out.println("," + sb );
			}
		}
	}

	private static String getModifier(int modifier) {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder();
		Modifier mod = new Modifier();
		if(mod.isPrivate(modifier))   { sb.append("Private"); }
		if(mod.isProtected(modifier)) { sb.append("Protected"); }
		if(mod.isPublic(modifier))    { sb.append("Public"); }
		if(mod.isFinal(modifier))     { sb.append("Final"); }
		if(mod.isStatic(modifier))    { sb.append("Static"); }
		
		return sb.toString();
	}

	private static Class loadClass(String myPackage) {

		try {
		
			Class myClassName =  Class.forName(myPackage);
			return myClassName;
		} catch (ClassNotFoundException e) {
			
			throw new RuntimeException(e);
		}
	}

}