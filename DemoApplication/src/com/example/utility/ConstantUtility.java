package com.example.utility;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.data.CalendarData.EventType;

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
	
	public static final String DATE_FORMATE = "dd/MM/yyyy";

	// Compare two date equal or not
	@SuppressWarnings("deprecation")
	public static boolean dateCompare(Date date1,Date date2) {
		if(date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear())
			return true;
		
		return false;
	}
	

	public static String getColorCodeFromEventType(EventType eventType) {
		// TODO Auto-generated method stub
		if (eventType == EventType.Event) {
			return "#AB47BC";
		} else if (eventType == EventType.Exam) {
			return "#26A69A";
		} else {
			return "#42A5F5";
		}
	}

	
	// Change image(bitmap) color
	public static Bitmap changeImageColor(Drawable sourceDrawable, String color) {
		//Convert drawable in to bitmap
        Bitmap sourceBitmap = convertDrawableToBitmap(sourceDrawable);
        
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,  
        sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);    
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(Color.parseColor(color), 1);
        p.setColorFilter(filter);
 
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }
 
 
 	// Convert drawable to bitmap
    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
 
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), 
        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
 
        return bitmap;
    }
}
