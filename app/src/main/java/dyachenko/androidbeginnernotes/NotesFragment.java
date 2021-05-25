package dyachenko.androidbeginnernotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NotesFragment extends Fragment {

    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private int position;

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
        NotesAdapter adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view1, index) -> {
            position = index;
            showNoteDetails();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_POSITION, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(EXTRA_POSITION);
        }
    }

    private void showNoteDetails() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.notes_fragment_container, Settings.editNoteViaEditor
                        ? EditNoteFragment.newInstance(position)
                        : NoteFragment.newInstance(position))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.menu_search, menu);

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
}