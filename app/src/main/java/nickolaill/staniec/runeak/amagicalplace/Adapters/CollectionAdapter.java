package nickolaill.staniec.runeak.amagicalplace.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;

public class CollectionAdapter extends ListAdapter<Collection, CollectionAdapter.CollectionHolder> {
    private OnItemClickListener listener;

    public CollectionAdapter(){
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Collection> DIFF_CALLBACK = new DiffUtil.ItemCallback<Collection>() {
        @Override
        public boolean areItemsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
            return oldItem.getCoId() == newItem.getCoId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Collection oldItem, @NonNull Collection newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.collection_item, viewGroup, false);
        return new CollectionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionHolder collectionHolder, int i) {
        Collection collection = getItem(i);
        collectionHolder.textViewTitle.setText(collection.getTitle());
        collectionHolder.textViewDescription.setText(collection.getDescription());
    }

    public Collection getCollectionAt(int position){
        return getItem(position);
    }

    class CollectionHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, textViewDescription;

        public CollectionHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_collection_title);
            textViewDescription = itemView.findViewById(R.id.tv_collection_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Collection collection);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
