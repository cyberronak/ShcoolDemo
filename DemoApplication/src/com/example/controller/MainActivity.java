package com.example.controller;

import java.util.ArrayList;
import java.util.Locale;

import com.example.adapter.ContactDetailAdapter;
import com.example.data.ContactDetailVO;
import com.example.utility.ConstantUtility;
import com.example.utility.StringConst;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
	private DrawerLayout drawerLayout;
	private Boolean isFabOpen = false;
	private TextView contentTitle;
	private FloatingActionButton fab, fab1, fab2;
	private Animation fab_open, fab_close, rotate_forward, rotate_backward;
	private Toolbar toolbar;

	private SharedPreferences shpref;

	private RecyclerView mRecyclerView;
	private ContactDetailAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	private static String LOG_TAG = "CardViewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		fab = (FloatingActionButton) findViewById(R.id.fab);
		fab1 = (FloatingActionButton) findViewById(R.id.fab1);
		fab2 = (FloatingActionButton) findViewById(R.id.fab2);

		mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler_view);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new ContactDetailAdapter(getDataSet());
		mAdapter.setOnItemClickListener(onAdapterItemClickListener);
		mRecyclerView.setAdapter(mAdapter);

		shpref = getSharedPreferences(StringConst.My_PREFERENCES, MODE_PRIVATE);
		String userFn= shpref.getString(StringConst.FIRSTNAME, "");
		String userLn= shpref.getString(StringConst.LASTNAME, "");
		String title=userFn+" "+ userLn;
		contentTitle = (TextView) findViewById(R.id.content_title2);
		contentTitle.setText(ConstantUtility.toCamelCase(title));
		
		initNavigationDrawer();
		setFabButton();
	}

	private void setFabButton() {
		// TODO Auto-generated method stub
		fab_open = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fab_open);
		fab_close = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fab_close);
		rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate_forward);
		rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate_backward);
		fab.setOnClickListener(this);
		fab1.setOnClickListener(this);
		fab2.setOnClickListener(this);

	}

	public void initNavigationDrawer() {

		NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {

						int id = menuItem.getItemId();

						switch (id) {
						case R.id.home:
							Toast.makeText(getApplicationContext(), "Home",
									Toast.LENGTH_SHORT).show();
							drawerLayout.closeDrawers();
							break;
						case R.id.announcement:
							Toast.makeText(getApplicationContext(),
									"Announcement", Toast.LENGTH_SHORT).show();
							break;
						case R.id.test:
							Toast.makeText(getApplicationContext(), "Test",
									Toast.LENGTH_SHORT).show();
							break;
						case R.id.leaderboard:
							Toast.makeText(getApplicationContext(),
									"Leaderboard", Toast.LENGTH_SHORT).show();
							break;
						case R.id.settings:
							Toast.makeText(getApplicationContext(), "Settings",
									Toast.LENGTH_SHORT).show();
							break;
						case R.id.about:
							Toast.makeText(getApplicationContext(), "About",
									Toast.LENGTH_SHORT).show();
							break;
						case R.id.logout: {
							shpref = getSharedPreferences(
									StringConst.My_PREFERENCES, MODE_PRIVATE);
							shpref.edit().clear().commit();
							Intent intent = new Intent(getApplicationContext(),
									LoginActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);

							Toast.makeText(getApplicationContext(),
									"Successfully logout.", Toast.LENGTH_SHORT)
									.show();

						}
						}
						return true;
					}
				});

		// for api level 23.1.1 and higher directly access header from
		// navigation
		// View headerLayout = navigationView.getHeaderView(0);

		// Reading from SharedPreferences
		String userEmail = shpref.getString(StringConst.EMAIL, "");

		View headerLayout = navigationView
				.inflateHeaderView(R.layout.nav_header);
		TextView tv_email = (TextView) headerLayout.findViewById(R.id.tv_email);
		tv_email.setText(userEmail);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View v) {
				super.onDrawerClosed(v);
			}

			@Override
			public void onDrawerOpened(View v) {
				super.onDrawerOpened(v);
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
	}

	ContactDetailAdapter.MyClickListener onAdapterItemClickListener = new ContactDetailAdapter.MyClickListener() {

		@Override
		public void onItemClick(int position, View itemView) {

			// // 1
			// ImageView logo = (ImageView)
			// itemView.findViewById(R.id.card_logo);
			// TextView title = (TextView)
			// itemView.findViewById(R.id.card_title);
			// TextView desc = (TextView) itemView.findViewById(R.id.card_desc);
			// // 2
			// Pair<View, String> imagePair = Pair.create((View) logo,
			// "contact_logo");
			// Pair<View, String> titlePair = Pair.create((View) title,
			// "contact_title");
			// Pair<View, String> descPair = Pair.create((View) desc,
			// "contact_desc");
			// // 3
			// ActivityOptionsCompat options =
			// ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
			// imagePair, titlePair, descPair);
			// ActivityCompat.startActivity(MainActivity.this, new
			// Intent(MainActivity.this,ContactDetailsActivity.class)
			// , options.toBundle());

			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, "Clicked " + position,
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private ArrayList<ContactDetailVO> getDataSet() {
		Bitmap iconArrow = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.arrow_down);
		Bitmap iconLogo = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_action_location);
		ArrayList results = new ArrayList<ContactDetailVO>();
		ContactDetailVO obj = new ContactDetailVO(
				iconLogo,
				"Location",
				"XYZ College of Engg. & Tech.,\nNr. xyz area,\nxyzcity-364xxx.",
				iconArrow);
		results.add(obj);
		iconLogo = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_action_mail);
		obj = new ContactDetailVO(iconLogo, "Email Id", "abcd@xyz.com",
				iconArrow);
		results.add(obj);
		iconLogo = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_action_phone_start);
		obj = new ContactDetailVO(iconLogo, "Contact us", "02xx-22xxxx",
				iconArrow);
		results.add(obj);
		return results;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.fab:
			if (isFabOpen) {

				fab.startAnimation(rotate_backward);
				fab1.startAnimation(fab_close);
				fab2.startAnimation(fab_close);
				fab1.setClickable(false);
				fab2.setClickable(false);
				isFabOpen = false;
				Log.d("Raj", "close");

			} else {

				fab.startAnimation(rotate_forward);
				fab1.startAnimation(fab_open);
				fab2.startAnimation(fab_open);
				fab1.setClickable(true);
				fab2.setClickable(true);
				isFabOpen = true;
				Log.d("Raj", "open");

			}
			break;
		case R.id.fab1:

			Log.d("Raj", "Fab 1");
			break;
		case R.id.fab2:

			Log.d("Raj", "Fab 2");
			break;
		}
	}
}
