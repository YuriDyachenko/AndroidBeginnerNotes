package dyachenko.androidbeginnernotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class StartFragment extends CommonFragment {

    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    private SignInButton signInButton;
    private MaterialButton signOutButton;
    private MaterialButton continueButton;
    private TextView emailTextView;
    private GoogleSignInAccount account;
    private GoogleSignInClient client;

    public StartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initViews(view);
        enableSign();
        updateViews();
        return view;
    }

    private void initGoogleSign() {
        GoogleSignInOptions sign = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(requireContext(), sign);
    }

    @Override
    public void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            disableSign();
            updateViews();
        }
    }

    private void updateViews() {
        if (account != null) {
            emailTextView.setText(account.getEmail());
        } else {
            emailTextView.setText(R.string.no_email);
        }
    }

    private void disableSign() {
        signInButton.setEnabled(false);
        signOutButton.setEnabled(true);
        continueButton.setEnabled(true);
    }

    private void enableSign() {
        signInButton.setEnabled(true);
        signOutButton.setEnabled(false);
        continueButton.setEnabled(false);
    }

    private void initViews(View view) {
        signInButton = view.findViewById(R.id.sign_in_button);
        signOutButton = view.findViewById(R.id.sign_out_button);
        continueButton = view.findViewById(R.id.continue_button);
        emailTextView = view.findViewById(R.id.email_text_view);

        signInButton.setOnClickListener(v -> signIn());
        signOutButton.setOnClickListener(v -> signOut());
        continueButton.setOnClickListener(v -> application.getNavigation().addFragmentToBackStack(new NotesFragment()));
    }

    private void signIn() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        client.signOut().addOnCompleteListener(task -> {
            account = null;
            enableSign();
            updateViews();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            account = task.getResult(ApiException.class);
            disableSign();
            updateViews();
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}