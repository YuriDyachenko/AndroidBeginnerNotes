package dyachenko.androidbeginnernotes;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note implements Serializable {
    private String title;
    private String body;
    private Date created;

    public Note() {
    }

    public Note(String title, String body, String created) {
        setTitle(title);
        setBody(body);
        setCreatedFromString(created);
    }

    public String getTitle() {
        return title;
    }

    public String getNumberedTitle(int index) {
        return (index + 1) + ". " + getTitle();
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
            this.created = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public static String getCreatedString(Date created) {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(created);
    }
}
