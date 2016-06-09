package com.example.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity
{
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.splash_screen);
		//opening transition animations
	    overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_transition);

    	new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                
				//closing transition animations
			    overridePendingTransition(R.anim.activity_open_transition,R.anim.activity_close_translate);

                // close this activity
                finishAfterTransition();
            }
        }, SPLASH_TIME_OUT);
    }

}
