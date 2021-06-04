package dyachenko.androidbeginnernotes;

public abstract class Settings {
    public static final String PREFERENCE_NAME = "NotesSettings";
    public static final String USE_GOOGLE_AUTH = "USE_GOOGLE_AUTH";
    public static final String USE_YES_NO_FRAGMENT = "USE_YES_NO_FRAGMENT";
    public static final String USE_DIALOG_NOTE_FRAGMENT = "USE_DIALOG_NOTE_FRAGMENT";
    public static boolean useGoogleAuth = false;
    public static boolean useYesNoFragment = false;
    public static boolean useDialogNoteFragment = false;
}
