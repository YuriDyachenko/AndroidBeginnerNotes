package dyachenko.androidbeginnernotes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class NoteFragment extends CommonFragment {
    private static final String ARG_NOTE_INDEX = "ARG_NOTE_INDEX";
    private static final String ARG_NOTE_CALLBACK = "ARG_NOTE_CALLBACK";
    private Note note;
    private int noteIndex;
    private EditText titleEditText;
    private EditText bodyEditText;
    private TextView createdTextView;
    private final Calendar calendar = Calendar.getInstance();
    private NotesSourceResponse notesSourceResponse;

    public NoteFragment() {
    }

    public static NoteFragment newInstance(int noteIndex, NotesSourceResponse notesSourceResponse) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_INDEX, noteIndex);
        args.putSerializable(ARG_NOTE_CALLBACK, notesSourceResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            noteIndex = bundle.getInt(ARG_NOTE_INDEX);
            notesSourceResponse = (NotesSourceResponse) bundle.getSerializable(ARG_NOTE_CALLBACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
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
        if (noteIndex == Note.INDEX_FOR_NEW_NOTE) {
            saveChangesButton.setText(R.string.action_add_note);
        }
    }

    private void putDataToViews() {
        if (noteIndex == Note.INDEX_FOR_NEW_NOTE) {
            note = null;
            titleEditText.setText("");
            bodyEditText.setText("");
            createdTextView.setText(Note.getCreatedString(calendar.getTime()));
        } else {
            note = application.getFirebase().get(noteIndex);
            titleEditText.setText(note.getTitle());
            bodyEditText.setText(note.getBody());
            createdTextView.setText(note.getCreatedString());
            calendar.setTime(note.getCreated());
        }
    }

    private void getDataFromViews() {
        note.setTitle(titleEditText.getText().toString());
        note.setBody(bodyEditText.getText().toString());
        note.setCreated(calendar.getTime());
    }

    private void saveChanges() {
        boolean isAdding = (noteIndex == Note.INDEX_FOR_NEW_NOTE);
        if (isAdding) {
            note = new Note();
        }
        getDataFromViews();
        if (isAdding) {
            application.getFirebase().add(note, notesSourceResponse);
        } else {
            application.getFirebase().update(noteIndex, note, notesSourceResponse);
        }
        application.getNavigation().popBackStack();
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