package dyachenko.androidbeginnernotes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SettingsFragment extends CommonFragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        CheckBox useGoogleAuthCheckBox = view.findViewById(R.id.use_google_auth_checkbox);
        useGoogleAuthCheckBox.setChecked(Settings.useGoogleAuth);
        CheckBox useYesNoFragmentCheckBox = view.findViewById(R.id.use_yes_no_fragment);
        useYesNoFragmentCheckBox.setChecked(Settings.useYesNoFragment);

        view.findViewById(R.id.settings_apply_button).setOnClickListener(v -> {
            Settings.useGoogleAuth = useGoogleAuthCheckBox.isChecked();
            Settings.useYesNoFragment = useYesNoFragmentCheckBox.isChecked();
            writeSettings();
            application.getNavigation().popBackStack();
        });
    }

    private void writeSettings() {
        requireActivity().getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Settings.USE_GOOGLE_AUTH, Settings.useGoogleAuth)
                .putBoolean(Settings.USE_YES_NO_FRAGMENT, Settings.useYesNoFragment)
                .apply();
    }
}