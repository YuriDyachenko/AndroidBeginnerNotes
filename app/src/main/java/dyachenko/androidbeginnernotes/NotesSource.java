package dyachenko.androidbeginnernotes;

public interface NotesSource {
    NotesSource init(NotesSourceResponse notesSourceResponse);
    void add(Note note, NotesSourceResponse notesSourceResponse);
    Note get(int index);
    int size();
    boolean isEmpty();
    void clear(NotesSourceResponse notesSourceResponse);
    void remove(int index, NotesSourceResponse notesSourceResponse);
    void update(int index, Note note, NotesSourceResponse notesSourceResponse);
    int searchByPartOfTitle(String query);
}
