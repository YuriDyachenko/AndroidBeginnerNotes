package dyachenko.androidbeginnernotes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import static dyachenko.androidbeginnernotes.NoteFragment.ARG_NOTE_INDEX;

public class EditNoteFragment extends Fragment {
    private Note note;
    private int noteIndex;
    private EditText titleEditText;
    private EditText bodyEditText;
    private TextView createdTextView;
    private final Calendar calendar = Calendar.getInstance();

    public EditNoteFragment() {
    }

    public static EditNoteFragment newInstance(int noteIndex) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_INDEX, noteIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            noteIndex = bundle.getInt(ARG_NOTE_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        if (noteIndex < Notes.NOTE_STORAGE.size()) {
            note = Notes.NOTE_STORAGE.get(noteIndex);

            titleEditText = view.findViewById(R.id.title_edit_text);
            titleEditText.setText(note.getTitle());

            bodyEditText = view.findViewById(R.id.body_edit_text);
            bodyEditText.setText(note.getBody());

            createdTextView = view.findViewById(R.id.created_text_view);
            createdTextView.setText(note.getCreatedString());
            calendar.setTime(note.getCreated());

            view.findViewById(R.id.created_change_button).setOnClickListener(v -> changeNoteCreated());

            view.findViewById(R.id.save_button).setOnClickListener(v -> {
                saveChanges();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }
    }

    private void saveChanges() {
        note.setTitle(titleEditText.getText().toString());
        note.setBody(bodyEditText.getText().toString());
        note.setCreated(calendar.getTime());
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