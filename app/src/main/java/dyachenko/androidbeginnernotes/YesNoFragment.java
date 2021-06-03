package dyachenko.androidbeginnernotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class YesNoFragment extends CommonFragment implements View.OnClickListener {
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_BODY = "ARG_BODY";
    private static final String ARG_CALLBACK = "ARG_CALLBACK";
    private String title;
    private String body;
    DialogYesNoResponse response;

    public YesNoFragment() {
    }

    public static YesNoFragment newInstance(String title, String body, DialogYesNoResponse response) {
        YesNoFragment fragment = new YesNoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BODY, body);
        args.putSerializable(ARG_CALLBACK, response);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString(ARG_TITLE);
            body = bundle.getString(ARG_BODY);
            response = (DialogYesNoResponse) bundle.getSerializable(ARG_CALLBACK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yes_no, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        ((TextView) view.findViewById(R.id.ask_title_text_view)).setText(title);
        ((TextView) view.findViewById(R.id.ask_body_text_view)).setText(body);
        view.findViewById(R.id.ask_yes_button).setOnClickListener(this);
        view.findViewById(R.id.ask_no_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        response.answered(v.getId() == R.id.ask_yes_button);
        application.getNavigation().popBackStack();
    }
}