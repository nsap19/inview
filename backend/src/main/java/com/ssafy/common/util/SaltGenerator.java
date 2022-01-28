package com.ssafy.common.util;

import java.util.StringTokenizer;

public class SaltGenerator {
	private String result;

	public SaltGenerator(String input) {
		StringTokenizer st = new StringTokenizer(input, ":|.");
		StringBuilder salt = new StringBuilder();
		while (st.hasMoreTokens()) {
			salt.append(st.nextToken());
		}
		result = salt.toString();
	}

	public String toString() {
		return result;
	}
}
