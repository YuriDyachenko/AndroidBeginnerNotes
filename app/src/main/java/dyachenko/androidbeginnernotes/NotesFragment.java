package dyachenko.androidbeginnernotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import static dyachenko.androidbeginnernotes.NoteFragment.ARG_NOTE_INDEX;

public class NotesFragment extends CommonFragment {
    private static final String EXTRA_POSITION_KEY = "EXTRA_POSITION";
    private static final int ADD_NOTE_REQUEST_CODE = 1;
    private int position;
    private int positionToMove = -1;
    private RecyclerView recyclerView;
    private NotesAdapter adapter;

    public NotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        recyclerView.setHasFixedSize(true);
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);

        moveToPosition();

        adapter.setOnItemClickListener((view1, index) -> {
            position = index;
            showNoteDetails();
        });
    }

    private void moveToPosition() {
        if (positionToMove != -1) {
            recyclerView.smoothScrollToPosition(positionToMove);
            positionToMove = -1;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_POSITION_KEY, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(EXTRA_POSITION_KEY);
        }
    }

    private void showNoteDetails() {
        navigation.addFragmentToBackStack(Settings.editNoteViaEditor
                ? EditNoteFragment.newInstance(position)
                : NoteFragment.newInstance(position));
    }

    private void addNote() {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(-1);
        editNoteFragment.setTargetFragment(this, ADD_NOTE_REQUEST_CODE);
        navigation.addFragmentToBackStack(editNoteFragment);
    }

    private void deleteAllNotes() {
        if (!Notes.isEmpty()) {
            Notes.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != ADD_NOTE_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (resultCode == Activity.RESULT_OK && data != null) {
            positionToMove = data.getIntExtra(ARG_NOTE_INDEX, 0);
            adapter.notifyItemInserted(positionToMove);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.menu_notes, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int index = Notes.searchByPartOfTitle(query.toLowerCase());
                if (index == -1) {
                    Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                } else {
                    position = index;
                    showNoteDetails();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (doAction(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doAction(int id) {
        if (id == R.id.action_add_note) {
            addNote();
            return true;
        }
        if (id == R.id.action_clear) {
            deleteAllNotes();
            return true;
        }
        return false;
    }
}