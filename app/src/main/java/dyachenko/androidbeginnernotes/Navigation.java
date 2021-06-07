package dyachenko.androidbeginnernotes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void popBackStack() {
        fragmentManager.popBackStack();
    }

    public void addFragment(Fragment fragment, Boolean useBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.notes_fragment_container, fragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, false);
    }

    public void addFragmentToBackStack(Fragment fragment) {
        addFragment(fragment, true);
    }

    public void addFragmentToBackStackOnce(Fragment fragment) {
        Fragment visibleFragment = getVisibleFragment();
        if (visibleFragment != null && visibleFragment.getClass() == fragment.getClass()) {
            return;
        }
        addFragmentToBackStack(fragment);
    }

    private Fragment getVisibleFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}
