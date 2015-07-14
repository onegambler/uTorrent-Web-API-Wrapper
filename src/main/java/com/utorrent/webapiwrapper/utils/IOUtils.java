package com.utorrent.webapiwrapper.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class IOUtils {

	public static String toString(java.io.InputStream is) {
		try (Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A")){
			return scanner.hasNext() ? scanner.next() : "";
		}
	}

	public static String readFileFully(InputStream stream) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();

		try (Scanner scanner = new Scanner(stream)) {
			while(scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
		}

		return stringBuilder.toString();
	}
}
