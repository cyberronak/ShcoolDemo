package com.example.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantUtility {
	public static String getURL(String url) {
		if (url.equals(StringConst.SIGN_IN))
			return StringConst.URL_SIGN_IN;
		else if (url.equals(StringConst.SIGN_UP))
			return StringConst.URL_SIGN_UP;
		else if (url.equals(StringConst.FORGOT_PW))
			return StringConst.URL_SIGN_UP;
		else
			return "";
	}

	public static boolean emailValidator(final String mailAddress) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(StringConst.EMAIL_EXP);
		matcher = pattern.matcher(mailAddress);
		return matcher.matches();
	}

	public static boolean notEmpty(String value) {
		if (value != null && value.length() > 0)
			return true;
		return false;
	}

	public static String toCamelCase(final String init) {
	    if (init==null)
	        return null;

	    final StringBuilder ret = new StringBuilder(init.length());

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) {
	            ret.append(word.substring(0, 1).toUpperCase());
	            ret.append(word.substring(1).toLowerCase());
	        }
	        if (!(ret.length()==init.length()))
	            ret.append(" ");
	    }

	    return ret.toString();
	}
}
