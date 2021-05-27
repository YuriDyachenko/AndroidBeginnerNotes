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
        CheckBox checkBox = view.findViewById(R.id.show_note_in_editor_checkbox);
        checkBox.setChecked(Settings.showNoteInEditor);

        view.findViewById(R.id.settings_apply_button).setOnClickListener(v -> {
            Settings.showNoteInEditor = checkBox.isChecked();
            writeSettings();
            navigation.popBackStack();
        });
    }

    private void writeSettings() {
        requireActivity().getSharedPreferences(Settings.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(Settings.SHOW_NOTE_IN_EDITOR, Settings.showNoteInEditor)
                .apply();
    }
}