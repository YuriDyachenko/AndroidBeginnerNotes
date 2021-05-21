package dyachenko.androidbeginnernotes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

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
        CheckBox checkBox = view.findViewById(R.id.edit_note_via_popup_checkbox);
        checkBox.setChecked(Settings.editNoteViaPopupMenu);

        view.findViewById(R.id.settings_apply_button).setOnClickListener(v -> {
            Settings.editNoteViaPopupMenu = checkBox.isChecked();
            writeSettings();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void writeSettings() {
        requireActivity().getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Settings.EDIT_NOTE_VIA_POPUP_MENU, Settings.editNoteViaPopupMenu)
                .apply();
    }
}