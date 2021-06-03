package dyachenko.androidbeginnernotes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogYesNoFragment extends DialogFragment {
    public static final String YES_NO_TAG = "YES_NO_TAG";
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_BODY = "ARG_BODY";
    private static final String ARG_CALLBACK = "ARG_CALLBACK";
    private String title;
    private String body;
    DialogYesNoResponse response;

    public DialogYesNoFragment() {
    }

    public static DialogYesNoFragment newInstance(String title, String body, DialogYesNoResponse response) {
        DialogYesNoFragment fragment = new DialogYesNoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BODY, body);
        args.putSerializable(ARG_CALLBACK, response);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString(ARG_TITLE);
            body = bundle.getString(ARG_BODY);
            response = (DialogYesNoResponse) bundle.getSerializable(ARG_CALLBACK);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setMessage(body)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    dismiss();
                    response.answered(true);
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        response.answered(false);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);    //to cancel outside touch
        setCancelable(false);                       //press back button not cancel dialog
        return dialog;
    }
}
