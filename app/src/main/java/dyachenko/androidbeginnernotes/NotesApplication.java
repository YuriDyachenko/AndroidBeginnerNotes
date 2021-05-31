package dyachenko.androidbeginnernotes;

import android.app.Application;

public class NotesApplication extends Application {
    private NotesSourceFirebase firebase;
    private Navigation navigation;

    @Override
    public void onCreate() {
        super.onCreate();
        firebase = new NotesSourceFirebase();
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public NotesSourceFirebase getFirebase() {
        return firebase;
    }
}
