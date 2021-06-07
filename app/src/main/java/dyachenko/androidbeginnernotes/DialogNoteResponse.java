package dyachenko.androidbeginnernotes;

import java.io.Serializable;

public interface DialogNoteResponse extends Serializable {
    void finished(Note note);
}
