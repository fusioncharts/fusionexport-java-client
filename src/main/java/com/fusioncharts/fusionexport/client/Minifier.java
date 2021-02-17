package com.fusioncharts.fusionexport.client;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.io.FilenameUtils;
import com.fusioncharts.fusionexport.client.Utils;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class Minifier {
	private static final HtmlCompressor compressor = new HtmlCompressor();
	private static Document doc = null;
	static {
	    compressor.setPreserveLineBreaks(false);
	    compressor.setCompressCss(true);
	    compressor.setCompressJavaScript(true);
	}

	public static String minify (String type, String payload) {
		String temp = "", result = "";
		temp = type=="resource" || type=="html" ?Minifier.readFile(payload) :payload;
		if (type=="html") {
			doc = Jsoup.parse(temp);
        	Elements scripts = doc.select("[src]");
        	Elements styles = doc.select("[href]");
        	Minifier.updateHtml("src", scripts, Utils.getDirectory(payload));
        	Minifier.updateHtml("href", styles, Utils.getDirectory(payload));
	        return Minifier.writeFile(payload, compressor.compress(doc.toString()));
		}
		result = compressor.compress(temp);
		if (type=="resource") {
			return Minifier.writeFile(payload, result);
		} else {
			return result;
		}
	}

	public static String getNewName (String path) {
		String dirName = "", fileName = "", fileExtension = "", newFileName = "";
		
		File file = new File(path);
		dirName = file.getAbsoluteFile().getParentFile().getAbsolutePath();
		newFileName = dirName + "/" + Minifier.getNewFilename(path);

		return newFileName;
	}

	public static String getNewFilename (String path) {
		File file = new File(path);
		String fileName = FilenameUtils.removeExtension(file.getName());
		String fileExtension = FilenameUtils.getExtension(file.getName());
		return fileName + ".min.fusionexport." +fileExtension;
	}

	public static String writeFile (String path, String payload) {
		String newFileName = Minifier.getNewName(path);
		try {	
			//Create file
			new File(newFileName).createNewFile();
			//Write to file
			FileWriter writer = new FileWriter(newFileName);
			writer.write(payload);
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
      		e.printStackTrace();
		}
		
		return newFileName;
	}

	public static void updateHtml (String attr, Elements media, String basePath) {
		for(Element elm : media){
			if (Minifier.validToMinify(elm.attr(attr))) {
	    		String minified = "";
	            if (Paths.get(elm.attr(attr)).isAbsolute()) {
	            	minified = Minifier.getNewName(elm.attr(attr));
	            } else {
	            	String parent = Utils.getDirectory(elm.attr(attr));
	            	minified = Utils.resolvePath(basePath+"/"+(parent!=null && !parent.equals(".") ?parent :"")+Minifier.getNewFilename(elm.attr(attr)));
	            }
	            elm.attr(attr, minified);
	        }
        }
	}

	public static String readFile (String path) {
		String temp = "";
		try {
	      File file = new File(path);
	      Scanner myReader = new Scanner(file);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        temp += data;
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	    return temp;
	}

	public static Boolean validToMinify (String path) {
		String ext = FilenameUtils.getExtension(path);
		return ext.equals("html") || ext.equals("css") || ext.equals("js");
	}
} 