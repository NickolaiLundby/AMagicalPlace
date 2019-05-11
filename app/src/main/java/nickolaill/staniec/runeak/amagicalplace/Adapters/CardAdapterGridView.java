package nickolaill.staniec.runeak.amagicalplace.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.StorageUtils;

public class CardAdapterGridView extends ListAdapter<Card, CardAdapterGridView.CardHolder> {
    private OnItemClickListener listener;

    public CardAdapterGridView(){
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {
        @Override
        public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.getCaId() == newItem.getCaId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getSeries().equals(newItem.getTitle()) &&
                    oldItem.getText().equals(newItem.getTitle());
        }
    };

    public CardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_card_grid, viewGroup, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder cardHolder, int i) {
        Card card = getItem(i);
        cardHolder.textViewCardName.setText(card.getTitle());
        if(StorageUtils.isExternalStorageReadable())
            cardHolder.imageView.setImageURI(card.getImageUri());
    }

    public Card getCardAt(int position){
        return getItem(position);
    }

    class CardHolder extends RecyclerView.ViewHolder {
        private TextView textViewCardName;
        private ImageView imageView;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            textViewCardName = itemView.findViewById(R.id.text_view_card_name);
            imageView = itemView.findViewById(R.id.image_view_card);

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
        void onItemClick(Card card);
    }

    public void setOnItemClickListener(CardAdapterGridView.OnItemClickListener listener) {
        this.listener = listener;
    }
}
