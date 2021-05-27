package dyachenko.androidbeginnernotes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class NoteStorage {
    private static final String TAG_NOTE = "note";
    private static final List<Note> STORAGE = new ArrayList<>();

    public static void add(Note note) {
        STORAGE.add(note);
    }

    public static Note get(int index) {
        return STORAGE.get(index);
    }

    public static int size() {
        return STORAGE.size();
    }

    public static boolean isEmpty() {
        return STORAGE.isEmpty();
    }

    public static void clear() {
        STORAGE.clear();
    }

    public static void remove(int index) {
        STORAGE.remove(index);
    }

    public static int searchByPartOfTitle(String query) {
        for (int i = 0; i < STORAGE.size(); i++) {
            if (STORAGE.get(i).getTitle().toLowerCase().contains(query)) {
                return i;
            }
        }
        return -1;
    }

    public static void fillFromXml(XmlPullParser parser) {
        if (!STORAGE.isEmpty()) {
            return;
        }
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if ((parser.getEventType() == XmlPullParser.START_TAG)
                        && parser.getName().equals(TAG_NOTE)) {
                    STORAGE.add(new Note(parser.getAttributeValue(2),
                            parser.getAttributeValue(0),
                            parser.getAttributeValue(1)));
                }
                parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
