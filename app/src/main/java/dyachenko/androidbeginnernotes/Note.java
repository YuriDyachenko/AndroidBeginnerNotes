package dyachenko.androidbeginnernotes;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    private static final String IN_DATE_PATTERN = "yyyy-MM-dd";
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

    public Note(String title, String body, String created) {
        setTitle(title);
        setBody(body);
        setCreatedFromString(created);
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

    public void setCreatedFromString(String stringDate) {
        try {
            this.created = new SimpleDateFormat(IN_DATE_PATTERN, Locale.getDefault()).parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public static String getCreatedString(Date created) {
        return new SimpleDateFormat(OUT_DATE_PATTERN, Locale.getDefault()).format(created);
    }
}
