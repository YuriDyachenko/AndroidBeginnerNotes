package dyachenko.androidbeginnernotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private final Fragment fragment;
    private int positionForPopupMenu = Note.UNDEFINED_POSITION;
    private NotesSource notesSource;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setNotesSource(NotesSource notesSource) {
        this.notesSource = notesSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Note note = notesSource.get(position);
        holder.getTitleTextView().setText(note.getTitle());
        holder.getBodyTextView().setText(note.getBody());
        holder.getCreatedTextView().setText(note.getCreatedString());
    }

    @Override
    public int getItemCount() {
        return notesSource.size();
    }

    public int getPositionForPopupMenu() {
        return positionForPopupMenu;
    }

    public void clearPositionForPopupMenu() {
        positionForPopupMenu = Note.UNDEFINED_POSITION;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView bodyTextView;
        private final TextView createdTextView;

        public TextView getBodyTextView() {
            return bodyTextView;
        }

        public TextView getCreatedTextView() {
            return createdTextView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.item_title_text_view);
            bodyTextView = itemView.findViewById(R.id.item_body_text_view);
            createdTextView = itemView.findViewById(R.id.item_created_text_view);

            registerContextMenu(itemView);
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnClickListener(v -> {
                    positionForPopupMenu = getLayoutPosition();
                    itemView.showContextMenu();
                });
                fragment.registerForContextMenu(itemView);
            }
        }
    }
}
