package dyachenko.androidbeginnernotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private NotesApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        readSettings();
        initViews();
        initStartFragment(savedInstanceState);
    }

    private void initNavigation() {
        /*
         * каждый раз, когда активити пересоздается, у нее уже новый FragmentManager
         * поэтому придется обновить навигацию (по сути, его хранящую)
         */
        application = (NotesApplication) getApplication();
        application.setNavigation(new Navigation(getSupportFragmentManager()));
    }

    private void readSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        Settings.showNoteInEditor = sharedPreferences
                .getBoolean(Settings.SHOW_NOTE_IN_EDITOR, false);
    }

    private void initViews() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
    }

    private void initStartFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            application.getNavigation().addFragment(new StartFragment());
        }
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (doAction(item.getItemId())) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (doAction(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doAction(int id) {
        if (id == R.id.action_about) {
            application.getNavigation().addFragmentToBackStackOnce(new AboutFragment());
            return true;
        }
        if (id == R.id.action_settings) {
            application.getNavigation().addFragmentToBackStackOnce(new SettingsFragment());
            return true;
        }
        return false;
    }
}