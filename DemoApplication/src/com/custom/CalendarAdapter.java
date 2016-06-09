package com.custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.example.controller.R;
import com.example.data.CalendarData;
import com.example.utility.ConstantUtility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CalendarAdapter extends ArrayAdapter<Date>
{
	// for view inflation
	private LayoutInflater inflater;

	Context mContext;

	// current displayed month
	private Calendar mCurrentDate;
	
	public CalendarAdapter(Context context, ArrayList<Date> days,Calendar currentDate)
	{
		super(context, R.layout.control_calendar_day, days);
		mContext=context;
		inflater = LayoutInflater.from(context);
		mCurrentDate = currentDate;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		// day in question
		Date date = getItem(position);
		int day = date.getDate();
		int month = date.getMonth();
		int year = date.getYear();

		// today
		Date today = new Date();

		// inflate item if it does not exist yet
		if (view == null)
			view = inflater.inflate(R.layout.control_calendar_day, parent, false);

		// if this day has an event, specify event image
		view.setBackgroundResource(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY && month == mCurrentDate.get(Calendar.MONTH))
		{
			view.setBackgroundResource(R.drawable.bg_event_round);
		}
		
		if (CalendarData.events != null)
		{
			for (CalendarData calObj: CalendarData.events)
			{
				if (ConstantUtility.dateCompare(calObj.getEventDate(), date))
				{
					String mColorCode = ConstantUtility.getColorCodeFromEventType(calObj.getEventType());
					// mark this day for event
					view.setBackgroundResource(R.drawable.bg_round);
					GradientDrawable bgShape = (GradientDrawable)view.getBackground();
					bgShape.setColor(Color.parseColor(mColorCode));
					// break;
				}
			}
		}

		((TextView) view).setTypeface(null, Typeface.NORMAL);
		((TextView) view).setTextColor(mContext.getResources().getColor(R.color.greyed_out));

		if (month == mCurrentDate.get(Calendar.MONTH))
		{
			// clear styling
			((TextView) view).setTypeface(null, Typeface.NORMAL);
			((TextView) view).setTextColor(Color.BLACK);
		}
		
		if (day == today.getDate() && month == today.getMonth())
		{
			// if it is today, set it to blue/bold
			((TextView) view).setTypeface(null, Typeface.BOLD);
			((TextView) view).setBackgroundResource(R.drawable.bg_rect);
//			((TextView) view).setTextColor(mContext.getResources().getColor(R.color.today));
		}

		// set text
		((TextView) view).setText(String.valueOf(date.getDate()));

		return view;
	}
}
