package dyachenko.androidbeginnernotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.getTitleTextView().setText(Notes.NOTE_STORAGE.get(position).getNumberedTitle(position));
        holder.getBodyTextView().setText(Notes.NOTE_STORAGE.get(position).getBody());
        holder.getCreatedTextView().setText(Notes.NOTE_STORAGE.get(position).getCreatedString());
    }

    @Override
    public int getItemCount() {
        return Notes.NOTE_STORAGE.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;

        public TextView getBodyTextView() {
            return bodyTextView;
        }

        public TextView getCreatedTextView() {
            return createdTextView;
        }

        private final TextView bodyTextView;
        private final TextView createdTextView;

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title_text_view);
            bodyTextView = itemView.findViewById(R.id.item_body_text_view);
            createdTextView = itemView.findViewById(R.id.item_created_text_view);

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
