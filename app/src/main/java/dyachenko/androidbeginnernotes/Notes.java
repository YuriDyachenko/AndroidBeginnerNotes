package dyachenko.androidbeginnernotes;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Notes {
    private static final String tagNote = "note";
    public static final List<Note> NOTE_STORAGE = new ArrayList<>();

    public static int searchByPartOfTitle(String query) {
        for (int i = 0; i < NOTE_STORAGE.size(); i++) {
            if (NOTE_STORAGE.get(i).getTitle().toLowerCase().contains(query)) {
                return i;
            }
        }
        return -1;
    }

    public static void fillFromXml(XmlPullParser parser) {
        if (!NOTE_STORAGE.isEmpty()) {
            return;
        }
        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(tagNote)) {
                    NOTE_STORAGE.add(new Note(parser.getAttributeValue(2),
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
