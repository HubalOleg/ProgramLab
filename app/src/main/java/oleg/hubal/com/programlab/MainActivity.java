package oleg.hubal.com.programlab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import oleg.hubal.com.programlab.fragment.CategoryFragment;
import oleg.hubal.com.programlab.fragment.ChannelsFragment;
import oleg.hubal.com.programlab.fragment.ProgramFragment;
import oleg.hubal.com.programlab.service.DownloadService;
import oleg.hubal.com.programlab.service.receiver.DownloadAlarmReceiver;
import oleg.hubal.com.programlab.service.receiver.DownloadResultReceiver;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ProgressDialog mDownloadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvDrawer);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        if (savedInstanceState == null) {
            launchDownloadService();
            scheduleAlarm();

            ProgramFragment programFragment = new ProgramFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.flContent, programFragment).
                    commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Fragment fragment;
                        switch(item.getItemId()) {
                            case R.id.nav_program_fragment:
                                fragment = new ProgramFragment();
                                break;
                            case R.id.nav_channel_list_fragment:
                                fragment = new ChannelsFragment();
                                break;
                            case R.id.nav_category_fragment:
                                fragment = new CategoryFragment();
                                break;
                            case R.id.nav_favorite_fragment:
                                fragment = new ChannelsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.BUNDLE_CHANNELS_FILTER, Constants.FAVORITE_CHANNELS);
                                fragment.setArguments(bundle);
                                break;
                            default:
                                fragment = new ProgramFragment();
                                break;
                        }

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                        item.setChecked(true);
                        setTitle(item.getTitle());
                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                }
        );
    }

    private void launchDownloadService() {
        if (!Utility.getDownloadPref(this)) {
            if (Utility.isNetworkConnected(this)) {
                DownloadResultReceiver resultReceiver = new DownloadResultReceiver(new Handler());
                resultReceiver.setReceiver(new DownloadResultReceiver.Receiver() {
                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {
                        switch (resultCode) {
                            case Constants.RECEIVER_SERVICE_START:
                                mDownloadDialog = new ProgressDialog(MainActivity.this);
                                mDownloadDialog.setCancelable(false);
                                mDownloadDialog.setTitle(getString(R.string.dialog_wait));
                                mDownloadDialog.setMessage(getString(R.string.dialog_downloading));
                                mDownloadDialog.show();
                                break;
                            case Constants.RECEIVER_SERVICE_FINISH:
                                mDownloadDialog.dismiss();
                                break;
                        }
                    }
                });

                Intent i = new Intent(this, DownloadService.class);
                i.putExtra(Constants.RECEIVER,resultReceiver);
                i.putExtra(Constants.SERVICE_STATUS, Constants.SERVICE_FIRST_DOWNLOAD);
                startService(i);
            } else {
                Toast.makeText(this, R.string.error_internet, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), DownloadAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this,
                DownloadAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis() + 1000 * 60 * 60 * 6;

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
    }
}
