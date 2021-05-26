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
    private int positionForPopupMenu = -1;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
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
        holder.getTitleTextView().setText(Notes.get(position).getNumberedTitle(position));
        holder.getBodyTextView().setText(Notes.get(position).getBody());
        holder.getCreatedTextView().setText(Notes.get(position).getCreatedString());
    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }

    public int getPositionForPopupMenu() {
        return positionForPopupMenu;
    }

    public void clearPositionForPopupMenu() {
        positionForPopupMenu = -1;
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
