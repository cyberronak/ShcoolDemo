package com.custom;

import com.example.controller.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
public class CalendarActivity extends ActionBarActivity 
{
	private CalendarView cv;
	private Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_attendence);

		//opening transition animations
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_transition);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		// set the toolbar title
		getSupportActionBar().setTitle(R.string.title_attendence);
        
		cv = ((CalendarView) findViewById(R.id.calendar_view));
	}
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		//closing transition animations
	    overridePendingTransition(R.anim.activity_open_transition,R.anim.activity_close_translate);
	    finishAfterTransition();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);	}
}
