package nickolaill.staniec.runeak.amagicalplace.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;

public class CardAdapterListView extends ListAdapter<Card, CardAdapterListView.CardHolder> {
    private OnItemClickListener listener;
    private int selectedPosition = -1;
    private boolean collectionFragment = false;

    public CardAdapterListView(boolean collectionFragment){
        super(DIFF_CALLBACK);
        this.collectionFragment = collectionFragment;
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
                .inflate(R.layout.item_card_list, viewGroup, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardHolder cardHolder, int i) {
        Card card = getItem(i);
        cardHolder.textViewCardName.setText(card.getTitle());
        cardHolder.textViewSeries.setText(card.getSeries());
        cardHolder.textViewCardType.setText(card.getType());

        if(selectedPosition == i)
            cardHolder.itemView.setBackgroundColor(Color.parseColor("#808080"));
        else
            cardHolder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        cardHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = cardHolder.getAdapterPosition();
                listener.onItemClick(getItem(cardHolder.getAdapterPosition()));
                // TODO: The below line hurts performance. Is there a better way?
                notifyDataSetChanged();
            }
        });

        if(collectionFragment) {
            cardHolder.textViewQuantity.setText(String.valueOf(card.getQuantity()));
        }
        else{
            cardHolder.buttonIncreaseQuantity.setVisibility(View.GONE);
            cardHolder.buttonDecreaseQuantity.setVisibility(View.GONE);
        }
    }

    public Card getCardAt(int position){
        return getItem(position);
    }

    class CardHolder extends RecyclerView.ViewHolder {
        private TextView textViewCardName, textViewSeries, textViewCardType, textViewQuantity;
        private ImageButton buttonIncreaseQuantity, buttonDecreaseQuantity;

        private CardHolder(@NonNull final View itemView) {
            super(itemView);
            textViewCardName = itemView.findViewById(R.id.text_view_card_name);
            textViewSeries = itemView.findViewById(R.id.text_view_series);
            textViewCardType = itemView.findViewById(R.id.text_view_card_type);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(getAdapterPosition()));
                    }
                }
            });

            buttonIncreaseQuantity = itemView.findViewById(R.id.button_increase_quantity);
            buttonDecreaseQuantity = itemView.findViewById(R.id.button_decrease_quantity);
            buttonIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onIncreaseItemClick(getItem(getAdapterPosition()));
                    }
                }
            });
            buttonDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onDecreaseItemClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Card card);
        void onIncreaseItemClick(Card card);
        void onDecreaseItemClick(Card card);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
