package dyachenko.androidbeginnernotes;

import java.io.Serializable;

public interface NotesSourceResponse extends Serializable {
    void initialized(NotesSource notesSource);
    void initializedRemove(NotesSource notesSource, int position);
}
