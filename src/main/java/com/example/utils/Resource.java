package com.example.utils;

import java.io.File;

public class Resource {
	public static final String HOME_DIR = "src/main/resources/examples/";

	public static String resolveResource (String file) {
		return HOME_DIR+file;
	}

	public static void resolveOutput (String dir) {
		new File(dir).mkdirs();
	}

	public static String exampleOutput (String path) {
		String outPath = "examples_result/"+path;
		Resource.resolveOutput(outPath);
		return outPath;
	}
}