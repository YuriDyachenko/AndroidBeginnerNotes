package dyachenko.androidbeginnernotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DialogNoteFragment extends DialogFragment {
    public static final String DIALOG_NOTE_TAG = "DIALOG_NOTE_TAG";
    private static final String DIALOG_ARG_NOTE = "DIALOG_ARG_NOTE";
    private static final String DIALOG_ARG_CALLBACK = "DIALOG_ARG_CALLBACK";
    private Note note;
    private DialogNoteResponse response;
    private EditText titleEditText;
    private EditText bodyEditText;
    private TextView createdTextView;
    private final Calendar calendar = Calendar.getInstance();

    public DialogNoteFragment() {
    }

    public static DialogNoteFragment newInstance(Note note, DialogNoteResponse response) {
        DialogNoteFragment fragment = new DialogNoteFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIALOG_ARG_NOTE, note);
        args.putSerializable(DIALOG_ARG_CALLBACK, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
         * этот код развернет на весь экран
         */
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            note = (Note) getArguments().getSerializable(DIALOG_ARG_NOTE);
            response = (DialogNoteResponse) getArguments().getSerializable(DIALOG_ARG_CALLBACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_note, container, false);
        initViews(view);
        putDataToViews();
        return view;
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.title_edit_text);
        bodyEditText = view.findViewById(R.id.body_edit_text);
        createdTextView = view.findViewById(R.id.created_text_view);

        Button saveChangesButton = view.findViewById(R.id.save_button);
        saveChangesButton.setOnClickListener(v -> saveChanges());

        Button changeCreatedButton = view.findViewById(R.id.created_change_button);
        changeCreatedButton.setOnClickListener(v -> changeNoteCreated());
        if (note.getId() == null) {
            saveChangesButton.setText(R.string.action_add_note);
        }
    }

    private void putDataToViews() {
        titleEditText.setText(note.getTitle());
        bodyEditText.setText(note.getBody());
        createdTextView.setText(note.getCreatedString());
        calendar.setTime(note.getCreated());
    }

    private void getDataFromViews() {
        note.setTitle(titleEditText.getText().toString());
        note.setBody(bodyEditText.getText().toString());
        note.setCreated(calendar.getTime());
    }

    private void saveChanges() {
        getDataFromViews();
        response.finished(note);
        dismiss();
    }

    private void changeNoteCreated() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            createdTextView.setText(Note.getCreatedString(calendar.getTime()));
        };

        new DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}