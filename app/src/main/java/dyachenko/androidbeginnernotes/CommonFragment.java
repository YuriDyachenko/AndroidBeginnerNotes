package dyachenko.androidbeginnernotes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CommonFragment extends Fragment {
    protected Navigation navigation;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        navigation = mainActivity.getNavigation();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
         * при повороте экрана сначала происходит onAttach, а потом уже "создание"
         * MainActivity, так что навигация там еще нулл, вот эту ситуацию обработаем
         * здесь. если нулл, то получим навигацию заново
         */
        if (navigation == null) {
            Activity activity = getActivity();
            if (activity != null) {
                MainActivity mainActivity = (MainActivity) activity;
                navigation = mainActivity.getNavigation();
            }
        }
    }

    @Override
    public void onDetach() {
        navigation = null;
        super.onDetach();
    }
}
