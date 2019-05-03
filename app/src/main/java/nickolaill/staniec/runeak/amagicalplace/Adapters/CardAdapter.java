package nickolaill.staniec.runeak.amagicalplace.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;

public class CardAdapter extends ListAdapter<Card, CardAdapter.CardHolder> {
    private OnItemClickListener listener;

    public CardAdapter(){
        super(DIFF_CALLBACK);
    }

    @Override
    public void submitList(@Nullable List<Card> list) {
        super.submitList(list);
        notifyDataSetChanged();
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
                .inflate(R.layout.card_item, viewGroup, false);
        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder cardHolder, int i) {
        Card card = getItem(i);
        cardHolder.textViewTitle.setText(card.getTitle());
        cardHolder.textViewSeries.setText(card.getSeries());
        cardHolder.textViewText.setText(card.getText());
    }

    public Card getCardAt(int position){
        return getItem(position);
    }

    class CardHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, textViewSeries, textViewText;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewSeries = itemView.findViewById(R.id.text_view_series);
            textViewText = itemView.findViewById(R.id.text_view_text);

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
