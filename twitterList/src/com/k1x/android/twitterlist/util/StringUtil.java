package com.k1x.android.twitterlist.util;

public class StringUtil {
	
	public static String removeTags(String str) {
		if (str == null) {
			throw new IllegalArgumentException("Str must not be null.");
		}
		//
		StringBuffer out = new StringBuffer();
		char cs[] = str.toCharArray();
		boolean tagFound = false;
		int i1 = 0;
		int l = 0;
		//
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '<' && !tagFound) {
				out.append(cs, i1, l);
				//
				i1 = i;
				l = 0;
				tagFound = true;
				l++;
			} else if (cs[i] == '>' && tagFound) {
				i1 = i +1;
				l = 0;
				tagFound = false;
			} else {
				l++;
			}
		}
		if (l > 0) {
			out.append(cs, i1, l);
		}
		//
		return out.toString().trim();
	}
}
