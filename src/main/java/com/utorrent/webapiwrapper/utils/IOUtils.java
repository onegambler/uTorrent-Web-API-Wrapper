package com.utorrent.webapiwrapper.utils;

import java.util.Scanner;


public class IOUtils {

	public static String toString(java.io.InputStream is) {
		try (Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A")){
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
}
