package com.fabbi.util;

import java.util.Random;

public class RandomUtils {

	public static String ramdonString() {
	    int leftLimit = 97;
	    int rightLimit = 122;
	    int targetStringLength = 6;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
}
