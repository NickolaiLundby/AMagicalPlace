package nickolaill.staniec.runeak.amagicalplace.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;

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
            boolean theSame = oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
            Log.d(Constants.LOG_TAG_COLLECTION_ADAPTER, "Title old: " +oldItem.getTitle() + " Description old: " + oldItem.getDescription()
            + " Title new: " + newItem.getTitle() + " New Description: " + newItem.getDescription());
            Log.d(Constants.LOG_TAG_COLLECTION_ADAPTER, "areContentsTheSame: " +theSame);
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_collection, viewGroup, false);
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
        private Button buttonEdit;

        public CollectionHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_collection_title);
            textViewDescription = itemView.findViewById(R.id.tv_collection_description);
            buttonEdit = itemView.findViewById(R.id.button_collection_edit);

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onButtonItemClick(getItem(getAdapterPosition()));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(getAdapterPosition()));
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onLongItemClick(getItem(getAdapterPosition()));
                        return true;
                    }
                    else
                        return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Collection collection);
        void onLongItemClick(Collection collection);
        void onButtonItemClick(Collection collection);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
