package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.StorageUtils;

public class CardDetailFragment extends Fragment {
    private CardDetailFragmentListener mListener;
    private static final String ARG_CARD = "card";
    private TextView txtTitleCard, txtTextCard, txtPriceValue;
    private Button btnReturn;
    private ImageView imgCard;
    private Card card;

    public static CardDetailFragment newInstance(Card card) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_card_detail, container, false);

        card = getArguments().getParcelable(ARG_CARD);

        txtTitleCard = v.findViewById(R.id.txtTitleCard);
        txtTitleCard.setText(card.getTitle());

        txtTextCard = v.findViewById(R.id.txtTextCard);
        txtTextCard.setText(card.getText());

        txtPriceValue = v.findViewById(R.id.txtPriceValue);
        txtPriceValue.setText("");

        btnReturn = v.findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCardDetailFragmentCancelInteraction();
            }
        });

        imgCard = v.findViewById(R.id.imgCard);
        if(StorageUtils.isExternalStorageReadable())
            imgCard.setImageURI(card.getImageUri());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CardDetailFragmentListener) {
            mListener = (CardDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "has to implement the CardDetailFragmentListener interface");
        }
    }

    public interface CardDetailFragmentListener {
        void onCardDetailFragmentCancelInteraction();
    }
}
