package com.custom;

import com.example.controller.R;
import com.example.data.CalendarData;
import com.example.data.CalendarData.EventType;
import com.example.utility.ConstantUtility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
public class CalendarView  extends LinearLayout {
	// for logging
	private static final String LOGTAG = "Calendar View";

	// how many days to show, defaults to six weeks, 42 days
	private static final int DAYS_COUNT = 42;

	// date format
	private String dateFormat;

	// current displayed month
	private Calendar currentDate = Calendar.getInstance();

	// event handling
	private EventHandler eventHandler = null;

	// internal components
	private LinearLayout header;
	private ImageView btnPrev;
	private ImageView btnNext;
	private TextView txtDate;
	private GridView grid;

	// seasons' rainbow
	int[] rainbow = new int[] { R.color.summer, R.color.fall, R.color.winter,
			R.color.spring };

	// month-season association (northern hemisphere, sorry australia :)
	int[] monthSeason = new int[] { 2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2 };

	private Date menuItemDate;

	public CalendarView(Context context) {
		super(context);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControl(context, attrs);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initControl(context, attrs);
	}

	/**
	 * Load control xml layout
	 */
	private void initControl(Context context, AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_calendar, this);

		loadDateFormat(attrs);
		assignUiElements();
		assignClickHandlers();

		updateCalendar(CalendarData.events);
	}

	private void loadDateFormat(AttributeSet attrs) {
		TypedArray ta = getContext().obtainStyledAttributes(attrs,
				R.styleable.CalendarView);

		try {
			// try to load provided date format, and fallback to default
			// otherwise
			dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
			if (dateFormat == null)
				dateFormat = ConstantUtility.DATE_FORMATE;
		} finally {
			ta.recycle();
		}
	}

	private void assignUiElements() {
		// layout is inflated, assign local variables to components
		header = (LinearLayout) findViewById(R.id.calendar_header);
		btnPrev = (ImageView) findViewById(R.id.calendar_prev_button);
		btnNext = (ImageView) findViewById(R.id.calendar_next_button);
		txtDate = (TextView) findViewById(R.id.calendar_date_display);
		grid = (GridView) findViewById(R.id.calendar_grid);
	}

	private void assignClickHandlers() {
		// add one month and refresh UI
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentDate.add(Calendar.MONTH, 1);
				updateCalendar(CalendarData.events);
			}
		});

		// subtract one month and refresh UI
		btnPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentDate.add(Calendar.MONTH, -1);
				updateCalendar(CalendarData.events);
			}
		});

		// click-pressing on check day having event or not
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Date dt = (Date) parent.getItemAtPosition(position);

				for (CalendarData calObj : CalendarData.events) {
					if (ConstantUtility.dateCompare(calObj.getEventDate(), dt)) {
						viewEventDialog(calObj);
						return;
					}
				}
				addEventDialog(dt);

				// for (CalendarData calObj : CalendarData.events) {
				// if (ConstantUtility.dateCompare(dt, calObj.getEventDate())) {
				// Toast.makeText(getContext(), calObj.getEventTitle(),
				// Toast.LENGTH_SHORT).show();
				// break;
				// }
				// }
			}
		});

		// long-pressing a day
		grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> view, View cell,
					int position, long id) {
				callContextMenu(getContext(),
						(Date) view.getItemAtPosition(position), cell);
				// // handle long-press
				// if (eventHandler == null)
				// return false;
				//
				//
				// eventHandler.onDayLongPress(
				// (Date) view.getItemAtPosition(position), cell);
				return true;
			}
		});
	}

	OnMenuItemClickListener menuItemClickList = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Auto-generated
			// method stub
//			if (item.getItemId() == R.id.overflow_add) {
//				addEventDialog(null);
//			} else {
				for (CalendarData calObj : CalendarData.events) {
					if (ConstantUtility.dateCompare(calObj.getEventDate(),
							menuItemDate)) {
						CalendarData.events.remove(calObj);
						break;
					}
				}
				updateCalendar(CalendarData.events);

				Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT)
						.show();
//			}
			return false;
		}
	};

	private void viewEventDialog(CalendarData calData) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calData.getEventDate());

		String mColorCode = ConstantUtility.getColorCodeFromEventType(calData
				.getEventType());

		// Get the image to be changed from the drawable, drawable-xhdpi,
		// drawable-hdpi,etc folder.
		Drawable titalDrawable = getResources().getDrawable(
				R.drawable.ic_action_ticket);
		Drawable descDrawable = getResources().getDrawable(
				R.drawable.ic_action_document);

		Bitmap titalIcon = ConstantUtility.changeImageColor(titalDrawable,
				mColorCode);
		Bitmap descIcon = ConstantUtility.changeImageColor(descDrawable,
				mColorCode);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);

		LayoutInflater inflater = LayoutInflater.from(getContext());

		View dialogView = inflater.inflate(R.layout.view_event_dialog, null);
		builder.setView(dialogView);

		// Change color off upper date view layout
		LinearLayout dateLayout = (LinearLayout) dialogView
				.findViewById(R.id.date_layout);
		dateLayout.setBackgroundColor(Color.parseColor(mColorCode));

		// Display year
		ImageView imgViewTitle = (ImageView) dialogView
				.findViewById(R.id.imageView1);
		imgViewTitle.setImageBitmap(titalIcon);
		TextView txtYear = (TextView) dialogView.findViewById(R.id.txt_year);
		txtYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

		// Display date with month
		ImageView imgViewDesc = (ImageView) dialogView
				.findViewById(R.id.imageView2);
		imgViewDesc.setImageBitmap(descIcon);
		TextView txtDate = (TextView) dialogView.findViewById(R.id.txt_date);
		txtDate.setText(String.valueOf(sdf.format(calendar.getTime())));

		// Title/Description drawable and it's text
		TextView title = (TextView) dialogView.findViewById(R.id.txt_title);
		title.setText(calData.getEventTitle());
		TextView desc = (TextView) dialogView.findViewById(R.id.txt_desc);
		desc.setText(calData.getEventDesc());

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// positive button logic

			}
		});

		AlertDialog dialog = builder.create();
		// display dialog
		dialog.show();
	}

	private void addEventDialog(final Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);

		LayoutInflater inflater = LayoutInflater.from(getContext());

		final View dialogView = inflater.inflate(R.layout.add_event_dialog, null);
		builder.setView(dialogView);
		TextView txtYear = (TextView) dialogView.findViewById(R.id.txt_year);
		txtYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
		TextView txtDate = (TextView) dialogView.findViewById(R.id.txt_date);
		txtDate.setText(String.valueOf(sdf.format(calendar.getTime())));
		final RadioGroup rbgroup = (RadioGroup) dialogView
				.findViewById(R.id.rbGroup);
		// final RadioButton btn1 = (RadioButton) dialogView
		// .findViewById(R.id.rbType1);
		// final RadioButton btn2 = (RadioButton) dialogView
		// .findViewById(R.id.rbType2);
		// final RadioButton btn3 = (RadioButton) dialogView
		// .findViewById(R.id.rbType3);
		//
		// final TextInputLayout inputTitle = (TextInputLayout) dialogView
		// .findViewById(R.id.input_event_title);
		// final TextInputLayout inputDesc = (TextInputLayout) dialogView
		// .findViewById(R.id.input_event_desc);
		final EditText title = (EditText) dialogView
				.findViewById(R.id.event_title);
		final EditText desc = (EditText) dialogView
				.findViewById(R.id.event_desc);
		builder.setCancelable(false);
		builder.setPositiveButton("Add-Event",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// positive button logic
						
					}
				});


		builder.setNegativeButton("Cancel-Event",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// negative button logic
					}
				});

		final AlertDialog dialog = builder.create();
		// display dialog
		dialog.show();
		

		// Overriding the handler immediately after show is probably a better
		// approach than OnShowListener as described below
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Do stuff, possibly set wantToCloseDialog to true
						// then...
						if (title.getText().toString().trim().isEmpty()) {
							title.setError(getResources().getString(
									R.string.error_event_title));
							return;
						} else {
							title.setError(null);
						}

						if (desc.getText().toString().trim().isEmpty()) {
							desc.setError(getResources().getString(
									R.string.error_event_desc));
							return;
						} else {
							desc.setError(null);
						}

						int selectedId = rbgroup.getCheckedRadioButtonId();

						CalendarData cal = new CalendarData();
						cal.setEventTitle(title.getText().toString());

						if (selectedId == R.id.rbType2)
							cal.setEventType(EventType.Exam);
						else if (selectedId == R.id.rbType3)
							cal.setEventType(EventType.Holiday);
						else
							cal.setEventType(EventType.Event);

						cal.setEventDesc(desc.getText().toString());
						cal.setEventDate(dt);

						CalendarData.events.add(cal);
						updateCalendar(CalendarData.events);

						Toast.makeText(getContext(),
								"event added on " + dt.toString(),
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
	}

	private void requestFocus(View view) {
		if (view.requestFocus()) {
			((Activity) getContext()).getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	protected void callContextMenu(Context mContext, Date date, View v) {
		// TODO Auto-generated method stub
		// This is an android.support.v7.widget.PopupMenu;
		PopupMenu popupMenu = new PopupMenu(mContext, v);
		popupMenu.inflate(R.menu.overflow_menu);
		menuItemDate = date;
		popupMenu.getMenu().removeItem(R.id.overflow_delete);
		for (CalendarData calObj : CalendarData.events) {
			if (ConstantUtility.dateCompare(calObj.getEventDate(), date)) {
				popupMenu.getMenu().add(1, R.id.overflow_delete, 1, "remove");
				break;
			}
		}
		// Force icons to show
		Object menuHelper;
		Class[] argTypes;
		try {
			Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
			fMenuHelper.setAccessible(true);
			menuHelper = fMenuHelper.get(popupMenu);
			argTypes = new Class[] { boolean.class };
			menuHelper.getClass()
					.getDeclaredMethod("setForceShowIcon", argTypes)
					.invoke(menuHelper, true);
		} catch (Exception e) {

			popupMenu.show();
			return;
		}
		popupMenu.setOnMenuItemClickListener(menuItemClickList);
		popupMenu.show();
	}

	/**
	 * Display dates correctly in grid
	 */
	public void updateCalendar(List<CalendarData> events) {
		ArrayList<Date> cells = new ArrayList<Date>();
		Calendar calendar = (Calendar) currentDate.clone();

		// determine the cell for current month's beginning
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		// move calendar backwards to the beginning of the week
		calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

		// fill cells
		while (cells.size() < DAYS_COUNT) {
			cells.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// update grid
		grid.setAdapter(new CalendarAdapter(getContext(), cells, currentDate));

		// update title
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		txtDate.setText(sdf.format(currentDate.getTime()));

		// set header color according to current season
		int month = currentDate.get(Calendar.MONTH);
		int season = monthSeason[month];
		int color = rainbow[season];
//		System.out.println(month+" "+season+" "+color);
		header.setBackgroundColor(getResources().getColor(color));
	}

	/**
	 * Assign event handler to be passed needed events
	 */
	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	/**
	 * This interface defines what events to be reported to the outside world
	 */
	public interface EventHandler {
		void onDayLongPress(Date date, View view);
	}
}