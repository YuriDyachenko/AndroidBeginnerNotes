package dyachenko.androidbeginnernotes;

import java.io.Serializable;

public interface NotesSourceResponse extends Serializable {
    void initialized(NotesSource notesSource);
}
