package dyachenko.androidbeginnernotes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class CommonFragment extends Fragment {
    protected NotesApplication application;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        application = (NotesApplication) mainActivity.getApplication();
    }

    @Override
    public void onDetach() {
        application = null;
        super.onDetach();
    }
}
