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
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readSettings();
        initViews();
        initStartFragment(savedInstanceState);
    }

    private void readSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        Settings.editNoteViaPopupMenu = sharedPreferences
                .getBoolean(Settings.EDIT_NOTE_VIA_POPUP_MENU, false);
    }

    private void initViews() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
    }

    private void initStartFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.notes_fragment_container, new NotesFragment())
                    .commit();
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
            addFragmentToBackStack(new AboutFragment());
            return true;
        }
        if (id == R.id.action_settings) {
            addFragmentToBackStack(new SettingsFragment());
            return true;
        }
        return false;
    }

    private Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    private void addFragmentToBackStack(Fragment fragment) {
        Fragment visibleFragment = getVisibleFragment();
        if (visibleFragment != null && visibleFragment.getClass() == fragment.getClass()) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.notes_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}