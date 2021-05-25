package dyachenko.androidbeginnernotes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
            titleEditText = view.findViewById(R.id.title_edit_text);
            bodyEditText = view.findViewById(R.id.body_edit_text);
            createdTextView = view.findViewById(R.id.created_text_view);
            Button saveChangesButton = view.findViewById(R.id.save_button);
            if (noteIndex == -1) {
                note = null;
                titleEditText.setText("");
                bodyEditText.setText("");
                createdTextView.setText(Note.getCreatedString(calendar.getTime()));
                saveChangesButton.setText(R.string.action_add_note);
            } else {
                note = Notes.NOTE_STORAGE.get(noteIndex);
                titleEditText.setText(note.getTitle());
                bodyEditText.setText(note.getBody());
                createdTextView.setText(note.getCreatedString());
                calendar.setTime(note.getCreated());
            }

            view.findViewById(R.id.created_change_button).setOnClickListener(v -> changeNoteCreated());

            saveChangesButton.setOnClickListener(v -> {
                saveChanges();
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        }
    }

    private void saveChanges() {
        if (noteIndex == -1) {
            note = new Note();
        }
        note.setTitle(titleEditText.getText().toString());
        note.setBody(bodyEditText.getText().toString());
        note.setCreated(calendar.getTime());
        if (noteIndex == -1) {
            Notes.NOTE_STORAGE.add(note);
            noteIndex = Notes.NOTE_STORAGE.size() - 1;
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                Intent data = new Intent();
                data.putExtra(ARG_NOTE_INDEX, noteIndex);
                targetFragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
            }
        }
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