package com.ramsy.GameCentre.GameCentreCommon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ramsy.GameCentre.R;


/**
 * This class will make the layout for tabbed activity, and allow the user to swipe between
 * tabs from the leader board of 2x2 to 10x10
 */
public class LeaderBoardActivity extends AppCompatActivity {
    private String lastScore;
    private String lastGame;
    private String gameIdentifier;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static String userUID;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        //Intent intent = getIntent();
        //userUID = intent.getExtras().getString("uniqueID");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        lastGame = getIntent().getExtras().getString("lastGame");
        lastScore = getIntent().getExtras().getString("lastScore");
        gameIdentifier = getIntent().getExtras().getString("gameIdentifier");
    }
    //create three icon bar on the top
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       //signs user out and pulls up login page when user clicks signs out from three icon bar
        if (id == R.id.action_signout) {
            LoginPage.signUserOut();
            Intent pullSignIn = new Intent (this, LoginPage.class);
            startActivity(pullSignIn);
        }
       else if (id == R.id.action_newgame) {
           Intent pullNewGame = new Intent (this, NewOrSavedGame.class);
           startActivity(pullNewGame);
       }
        return super.onOptionsItemSelected(item);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            LeaderBoardView frag = new LeaderBoardView();
            Bundle bundle = new Bundle();
            bundle.putString("lastScore",lastScore);
            bundle.putString("lastGame",lastGame);
            bundle.putString("gameIdentifier",gameIdentifier);
            bundle.putInt("position",position);
            frag.setArguments(bundle);
            return frag;
        }
        @Override
        public int getCount() {
            // Show 3 total pages.
            return 9;
        }
    }
}
