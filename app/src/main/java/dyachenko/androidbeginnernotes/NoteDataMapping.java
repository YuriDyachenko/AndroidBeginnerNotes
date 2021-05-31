package dyachenko.androidbeginnernotes;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {

    public static class Fields {
        public static final String TITLE = "title";
        public static final String BODY = "body";
        public static final String CREATED = "created";
    }

    public static Note toNote(String id, Map<String, Object> doc) {
        String title = (String) doc.get(Fields.TITLE);
        String body = (String) doc.get(Fields.BODY);
        Timestamp created = (Timestamp) doc.get(Fields.CREATED);
        Note note = new Note(title, body, created.toDate());
        note.setId(id);
        return note;
    }

    public static Map<String, Object> toDoc(Note note) {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.TITLE, note.getTitle());
        map.put(Fields.BODY, note.getBody());
        map.put(Fields.CREATED, note.getCreated());
        return map;
    }
}
