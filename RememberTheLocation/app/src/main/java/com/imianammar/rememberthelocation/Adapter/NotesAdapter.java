package com.imianammar.rememberthelocation.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imianammar.rememberthelocation.Model.Note;
import com.imianammar.rememberthelocation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.textViewLocationName.setText(note.getLocationName());
        holder.textViewDescription.setText(note.getDescription());

        // Load image (if available)
        if (note.getImagePath() != null && !note.getImagePath().isEmpty()) {
            // Set default image
            // You can use your own default image resource
            holder.imageViewNoteImage.setImageResource(R.drawable.img);

            // Load actual image using Picasso
            Picasso.get().load(note.getImagePath()).into(holder.imageViewNoteImage);
        }
        else {
            holder.imageViewNoteImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLocationName;
        TextView textViewDescription;
        ImageView imageViewNoteImage;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocationName = itemView.findViewById(R.id.textViewLocationName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewNoteImage = itemView.findViewById(R.id.imageViewNoteImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
