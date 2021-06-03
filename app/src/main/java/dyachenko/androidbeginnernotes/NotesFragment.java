package dyachenko.androidbeginnernotes;

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
import androidx.recyclerview.widget.RecyclerView;

public class NotesFragment extends CommonFragment {
    private NotesAdapter adapter;
    private NotesSource notesSource;
    private NotesSourceResponse notesSourceResponseRedraw;

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
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        recyclerView.setHasFixedSize(true);
        adapter = new NotesAdapter(this);

        notesSourceResponseRedraw = new NotesSourceResponse() {
            @Override
            public void initialized(NotesSource notesSource) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void initializedRemove(NotesSource notesSource, int position) {
                adapter.notifyItemRemoved(position);
            }
        };

        notesSource = application.getFirebase().init(notesSourceResponseRedraw);
        adapter.setNotesSource(notesSource);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addNote() {
        application.getNavigation().addFragmentToBackStack(NoteFragment.newInstance(-1,
                notesSourceResponseRedraw));
    }

    private void editNote(int position) {
        application.getNavigation().addFragmentToBackStack(NoteFragment.newInstance(position,
                notesSourceResponseRedraw));
    }

    private void deleteAllNotes() {
        DialogYesNoFragment.newInstance(getString(R.string.ask), getString(R.string.delete_all_notes),
                (DialogYesNoResponse) yes -> {
                    if (!notesSource.isEmpty() && yes) {
                        notesSource.clear(notesSourceResponseRedraw);
                    }
                }).show(application.getNavigation().getFragmentManager(), DialogYesNoFragment.YES_NO_TAG);
    }

    private void deleteNote(int position) {
        DialogYesNoFragment.newInstance(getString(R.string.ask), getString(R.string.delete_note),
                (DialogYesNoResponse) yes -> {
                    if (yes) {
                        notesSource.remove(position, notesSourceResponseRedraw);
                    }
                }).show(application.getNavigation().getFragmentManager(), DialogYesNoFragment.YES_NO_TAG);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notes, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return findNote(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean findNote(String query) {
        int index = notesSource.searchByPartOfTitle(query.toLowerCase());
        if (index == -1) {
            Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
        } else {
            doAction(R.id.action_edit_note, index);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (doAction(item.getItemId(), 0)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean doAction(int id, int position) {
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
            editNote(position);
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
            if (doAction(item.getItemId(), position)) {
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }
}