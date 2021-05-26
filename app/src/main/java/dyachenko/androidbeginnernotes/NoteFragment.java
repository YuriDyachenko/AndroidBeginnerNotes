package dyachenko.androidbeginnernotes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class NoteFragment extends Fragment {
    public static final String ARG_NOTE_INDEX = "ARG_NOTE_INDEX";
    private int noteIndex;
    private TextView createdTextView;
    private final Calendar calendar = Calendar.getInstance();

    public NoteFragment() {
    }

    public static NoteFragment newInstance(int noteIndex) {
        NoteFragment noteFragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NOTE_INDEX, noteIndex);
        noteFragment.setArguments(bundle);
        return noteFragment;
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
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        if (noteIndex < Notes.size()) {
            Note note = Notes.get(noteIndex);

            TextView titleTextView = view.findViewById(R.id.title_text_view);
            titleTextView.setText(note.getTitle());

            TextView bodyTextView = view.findViewById(R.id.body_text_view);
            bodyTextView.setText(note.getBody());

            createdTextView = view.findViewById(R.id.created_text_view);
            createdTextView.setText(note.getCreatedString());

            view.findViewById(R.id.created_change_button)
                    .setOnClickListener(v -> changeNoteCreated());
        }
    }

    private void changeNoteCreated() {
        Note note = Notes.get(noteIndex);

        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            note.setCreated(calendar.getTime());
            createdTextView.setText(note.getCreatedString());
        };

        calendar.setTime(note.getCreated());
        new DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}