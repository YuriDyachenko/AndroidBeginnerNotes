package dyachenko.androidbeginnernotes;

import java.util.ArrayList;
import java.util.List;

public abstract class Notes {
    public static final List<Note> NOTE_STORAGE = new ArrayList<>();

    public static int searchByPartOfTitle(String query) {
        for (int i = 0; i < NOTE_STORAGE.size(); i++) {
            if (NOTE_STORAGE.get(i).getTitle().toLowerCase().contains(query)) {
                return i;
            }
        }
        return -1;
    }
}
