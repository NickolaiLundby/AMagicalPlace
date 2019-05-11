package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.content.res.Configuration;
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
    private static final String ARG_MODE = "mode";
    private Button btnReturn;
    private ImageView imgCard;
    private Card card;
    private boolean mode;

    public static CardDetailFragment newInstance(Card card, boolean mode) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD, card);
        args.putBoolean(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v;

        mode = getArguments().getBoolean(ARG_MODE);
        card = getArguments().getParcelable(ARG_CARD);

        if(!mode){
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE){
                v = inflater.inflate(R.layout.fragment_card_detail_landscape, container, false);
            } else {
                v= inflater.inflate(R.layout.fragment_card_detail_portrait, container, false);
            }
        } else {
            v= inflater.inflate(R.layout.fragment_card_detail_portrait, container, false);
        }

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
