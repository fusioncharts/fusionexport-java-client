package com.example;

import java.io.File;
import java.lang.Class;
import java.lang.reflect.Method;

import org.apache.commons.io.FilenameUtils;

public class RunAll {
	public static File examples = new File("src/main/java/com/example/examples");
	
	public static void main (String[] args) throws Exception {
		for (File example : examples.listFiles()) {
			if (example.isFile()) {
				String className = FilenameUtils.removeExtension(example.getName());
				System.out.println("Running example: "+className+".");
				Object classInstance = Class.forName("com.example.examples."+className).newInstance();
				Class exampleClass = classInstance.getClass();
				Method exampleMainMethod = exampleClass.getDeclaredMethod("main", String[].class);
				exampleMainMethod.invoke(classInstance, new Object[1]);
				Thread.sleep(1500);
			}
		} 
	}
}