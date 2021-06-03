package dyachenko.androidbeginnernotes;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesSourceFirebase implements NotesSource {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NotesSourceFirebase]";

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = firebaseFirestore.collection(NOTES_COLLECTION);
    private final List<Note> storage = new ArrayList<>();

    @Override
    public NotesSource init(NotesSourceResponse notesSourceResponse) {
        collectionReference.orderBy(NoteDataMapping.Fields.CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storage.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                Map<String, Object> map = doc.getData();
                                String id = doc.getId();
                                storage.add(NoteDataMapping.toNote(id, map));
                            }
                        }
                        Log.d(TAG, "success: " + storage.size());
                        notesSourceResponse.initialized(this);
                    } else {
                        Log.d(TAG, "get failed with  " + task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "get failed with  " + e));
        return this;
    }

    @Override
    public void add(Note note, NotesSourceResponse notesSourceResponse) {
        collectionReference.add(NoteDataMapping.toDoc(note))
                .addOnSuccessListener(documentReference -> {
                    note.setId(documentReference.getId());
                    notesSourceResponse.initialized(this);
                });
    }

    @Override
    public Note get(int index) {
        return storage.get(index);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public void clear(NotesSourceResponse notesSourceResponse) {
        for (Note note : storage) {
            final String id = note.getId();
            collectionReference.document(id).delete()
                    .addOnSuccessListener(unused -> {
                        removeFromStorageById(id);
                        if (storage.size() == 0) {
                            notesSourceResponse.initialized(this);
                        }
                    });
        }
    }

    private int findById(String id) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private void removeFromStorageById(String id) {
        int index = findById(id);
        if (index != -1) {
            storage.remove(index);
        }
    }

    @Override
    public void remove(int index, NotesSourceResponse notesSourceResponse) {
        final String id = storage.get(index).getId();
        collectionReference.document(id)
                .delete()
                .addOnSuccessListener(unused -> {
                    removeFromStorageById(id);
                    notesSourceResponse.initializedRemove(this, index);
                });
    }

    @Override
    public void update(int index, Note note, NotesSourceResponse notesSourceResponse) {
        collectionReference.document(note.getId())
                .set(NoteDataMapping.toDoc(note))
                .addOnSuccessListener(unused -> {
                    storage.set(index, note);
                    notesSourceResponse.initialized(this);
                });
    }

    @Override
    public int searchByPartOfTitle(String query) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getTitle().toLowerCase().contains(query)) {
                return i;
            }
        }
        return -1;
    }
}
