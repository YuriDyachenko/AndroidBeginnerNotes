package dyachenko.androidbeginnernotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import static dyachenko.androidbeginnernotes.NoteFragment.ARG_NOTE_INDEX;

public class NotesFragment extends CommonFragment {
    private static final int ADD_NOTE_REQUEST_CODE = 1;
    private static final int EDIT_NOTE_REQUEST_CODE = 2;
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
        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        moveToPosition();
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

    private void addNote() {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(-1);
        editNoteFragment.setTargetFragment(this, ADD_NOTE_REQUEST_CODE);
        navigation.addFragmentToBackStack(editNoteFragment);
    }

    private void editNote(int position, boolean forceEdit) {
        Fragment fragment = (forceEdit || Settings.showNoteInEditor)
                ? EditNoteFragment.newInstance(position)
                : NoteFragment.newInstance(position);
        fragment.setTargetFragment(this, EDIT_NOTE_REQUEST_CODE);
        navigation.addFragmentToBackStack(fragment);
    }

    private void deleteAllNotes() {
        if (!NoteStorage.isEmpty()) {
            NoteStorage.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteNote(int position) {
        NoteStorage.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode != ADD_NOTE_REQUEST_CODE)
                && (requestCode != EDIT_NOTE_REQUEST_CODE)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if ((resultCode == Activity.RESULT_OK) && (data != null)) {
            positionToMove = data.getIntExtra(ARG_NOTE_INDEX, 0);

            /*
             * при повороте экрана, если мы внутри добавления заметки, здесь адаптер почему-то
             * получается null, добавлю проверку пока
             */
            if (adapter != null) {
                if (requestCode == ADD_NOTE_REQUEST_CODE) {
                    adapter.notifyItemInserted(positionToMove);
                } else {
                    adapter.notifyItemChanged(positionToMove);
                }
            }
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
                int index = NoteStorage.searchByPartOfTitle(query.toLowerCase());
                if (index == -1) {
                    Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                } else {
                    doAction(R.id.action_edit_note, index, false);
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
        if (doAction(item.getItemId(), 0, true)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doAction(int id, int position, boolean forceEdit) {
        if (id == R.id.action_add_note) {
            addNote();
            return true;
        }
        if (id == R.id.action_clear) {
            deleteAllNotes();
            return true;
        }
        if (id == R.id.action_delete_note) {
            deleteNote(position);
            return true;
        }
        if (id == R.id.action_edit_note) {
            editNote(position, forceEdit);
            return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_note_popup, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getPositionForPopupMenu();
        if (position != -1) {
            adapter.clearPositionForPopupMenu();
            if (doAction(item.getItemId(), position, true)) {
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }
}