package dyachenko.androidbeginnernotes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    public static final int INDEX_FOR_NEW_NOTE = -1;
    public static final int INDEX_NOTE_NOT_FOUND = -1;
    public static final int UNDEFINED_POSITION = -1;
    private static final String OUT_DATE_PATTERN = "dd.MM.yyyy";
    private String title;
    private String body;
    private Date created;
    private String id;

    public Note() {
    }

    public Note(String title, String body, Date created) {
        this.title = title;
        this.body = body;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        String[] strings = body.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strings) {
            stringBuilder.append(str.trim())
                    .append("\n");
        }
        this.body = stringBuilder.toString().trim();
    }

    public Date getCreated() {
        return created;
    }

    public String getCreatedString() {
        return getCreatedString(getCreated());
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public static String getCreatedString(Date created) {
        return new SimpleDateFormat(OUT_DATE_PATTERN, Locale.getDefault()).format(created);
    }
}
