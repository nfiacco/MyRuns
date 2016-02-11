package edu.dartmouth.cs.myruns;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;

import edu.dartmouth.cs.myruns.view.SlidingTabLayout;

public class MainActivity extends Activity implements StartFragment.SaveClicked, StartFragment.StartListener, HistoryFragment.HistListener, SettingsFragment.CreatedListener, UnitFragment.DismissListener{

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPagerAdapter;
    private StartFragment startFrag;
    private HistoryFragment histFrag;
    private SettingsFragment settingsFrag;
    private int mUnits = 0;

    GoogleCloudMessaging gcm;
    String regid;
    Context mContext;

    String SENDER_ID = "10622142242";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID_KEY = "registration_id";
    private static final String APP_VERSION_KEY = "appVersion";

    private IntentFilter mMessageIntentFilter;
    private BroadcastReceiver mMessageUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_string = intent.getStringExtra("id_key");
            if (id_string != null) {
                long id = Long.parseLong(id_string);
                ExerciseDataSource dataSource = new ExerciseDataSource(getParent());
                dataSource.open();
                dataSource.deleteEntryById(id);
                Log.d("", "delete entry by id");
                onSave();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, TrackingService.class));

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        mContext =getApplicationContext();

        mMessageIntentFilter = new IntentFilter();
        mMessageIntentFilter.addAction("GCM_NOTIFY");

        if (savedInstanceState != null){
            startFrag = (StartFragment) getFragmentManager().getFragment(savedInstanceState, StartFragment.class.getName());
            histFrag = (HistoryFragment) getFragmentManager().getFragment(savedInstanceState, HistoryFragment.class.getName());
            settingsFrag = (SettingsFragment) getFragmentManager().getFragment(savedInstanceState, SettingsFragment.class.getName());
            mUnits = settingsFrag.getPreferenceManager().getSharedPreferences().getInt("unit_type", 0);
        }

        if (startFrag == null){
            startFrag = new StartFragment();
        }
        if (histFrag == null){
            histFrag = new HistoryFragment();
        }
        if (settingsFrag == null){
            settingsFrag = new SettingsFragment();
        }

        // create a fragment list in order.
        fragments = new ArrayList<Fragment>();
        fragments.add(startFrag);
        fragments.add(histFrag);
        fragments.add(settingsFrag);

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles) and ViewPager (different pages of fragment) together.
        myViewPagerAdapter =new ActionTabsViewPagerAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        // make tabs the appropriate design
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        if (checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(mContext);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, StartFragment.class.getName(), startFrag);
        getFragmentManager().putFragment(outState, HistoryFragment.class.getName(), histFrag);
        getFragmentManager().putFragment(outState, SettingsFragment.class.getName(), settingsFrag);
    }

    @Override
    protected void onDestroy() {
        ExerciseDataSource datasource = new ExerciseDataSource(this);
        datasource.close();
        stopService(new Intent(MainActivity.this, TrackingService.class));
        super.onDestroy();
    }

    public void unitUpdate(){
        setUnits();
    }

    public void setUnits(){
        int units = settingsFrag.getPreferenceManager().getSharedPreferences().getInt("unit_type", 0);
        mUnits = units;
        onSave();
        startFrag.updateData();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(REG_ID_KEY, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(APP_VERSION_KEY, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        int appVersion = getAppVersion(context);
        Log.d("", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID_KEY, regId);
        editor.putInt(APP_VERSION_KEY, appVersion);
        editor.commit();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    ServerUtilities.sendRegistrationIdToBackend(mContext, regid);

                    // Persist the regID - no need to register again.
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("", "gcm register msg: " + msg);
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onResume() {
        registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onPause() {

        unregisterReceiver(mMessageUpdateReceiver);
        super.onPause();
    }

    public int getUnits(){
        return mUnits;
    }

    public int getUnits2(){
        return mUnits;
    }

    public void onSave(){
        histFrag.updateData();
    }
}