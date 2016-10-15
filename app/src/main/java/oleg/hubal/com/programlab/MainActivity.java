package oleg.hubal.com.programlab;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import oleg.hubal.com.programlab.fragment.CategoryFragment;
import oleg.hubal.com.programlab.fragment.ChannelFragment;
import oleg.hubal.com.programlab.fragment.FavoriteFragment;
import oleg.hubal.com.programlab.fragment.ProgramFragment;
import oleg.hubal.com.programlab.service.DownloadService;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setNavigationDrawer();
        if (savedInstanceState == null) {
            launchDownloadService();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.drawer_open, R.string.drawer_close);
    }


    private void setNavigationDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(drawerToggle);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvDrawer);
        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_program_fragment:
                fragmentClass = ProgramFragment.class;
                break;
            case R.id.nav_channel_list_fragment:
                fragmentClass = ChannelFragment.class;
                break;
            case R.id.nav_category_fragment:
                fragmentClass = CategoryFragment.class;
                break;
            case R.id.nav_favorite_fragment:
                fragmentClass = FavoriteFragment.class;
                break;
            default:
                fragmentClass = ProgramFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void launchDownloadService() {
        if (!Utility.getFirstDownloadPref(this)) {
            if (Utility.isNetworkConnected(this)) {
                Intent i = new Intent(this, DownloadService.class);
                i.putExtra(Constants.SERVICE_STATUS, Constants.SERVICE_FIRST_DOWNLOAD);
                startService(i);
            } else {
                Toast.makeText(this, R.string.error_internet, Toast.LENGTH_LONG).show();
            }
        }
    }
}
