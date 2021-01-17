
package com.example.uasa11201811347;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class Adapter extends FirebaseRecyclerAdapter<Item,Adapter.ViewHolder> {

    private OnItemClickListener listener;
    public Adapter(@NonNull FirebaseRecyclerOptions<Item> options)
    {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Item model) {

        holder.txtf1.setText(model.getUsername());
        holder.txtf2.setText(model.getPass());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtf1;
        private final TextView txtf2;
        private LinearLayout ly;
        public ViewHolder(View itemView) {
            super(itemView);
            ly= itemView.findViewById(R.id.an_item);
            txtf1= itemView.findViewById(R.id.out_user);
            txtf2= itemView.findViewById(R.id.out_pass);
            ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DataSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
