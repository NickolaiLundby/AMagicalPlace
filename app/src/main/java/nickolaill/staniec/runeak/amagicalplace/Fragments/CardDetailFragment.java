package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.CustomVolleyRequest;
import nickolaill.staniec.runeak.amagicalplace.Utilities.InternetUtils;
import nickolaill.staniec.runeak.amagicalplace.Utilities.StorageUtils;

public class CardDetailFragment extends Fragment {
    private CardDetailFragmentListener mListener;
    private static final String ARG_CARD = "card";
    private static final String ARG_MODE = "mode";
    private Button btnReturn;
    private ImageView imgCard;
    private TextView txtPrice, txtLastEval;
    private Card card;
    private boolean mode;
    private RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(getActivity().getBaseContext());

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

        txtPrice = v.findViewById(R.id.txtPrice);
        Double price = card.getPrice();
        if(price != null && price > 0){
            String p = price + " USD";
            txtPrice.setText(p);
        }


        txtLastEval = v.findViewById(R.id.txtLastEval);
        Date date = card.getLastEvaluated();
        if(date != null)
            txtLastEval.setText(String.valueOf(card.getLastEvaluated()));

        btnReturn = v.findViewById(R.id.btnReturn);
        if(!mode){
            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCardDetailFragmentCancelInteraction();
                }
            });
        } else {
            btnReturn.setVisibility(View.INVISIBLE);
        }

        imgCard = v.findViewById(R.id.imgCard);
        if(StorageUtils.isUriValid(card.getImageUri())){
            imgCard.setImageURI(card.getImageUri());
        } else if(InternetUtils.isOnline()) {
            new AddImageAsyncTask().execute(card);
        } else {
            imgCard.requestLayout();
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) imgCard.getLayoutParams();
            lp.matchConstraintPercentWidth = (float)0.2;
            lp.matchConstraintPercentHeight = (float)0.2;
            imgCard.setLayoutParams(lp);
            imgCard.setImageDrawable(getResources().getDrawable(R.drawable.no_signal));
        }

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

    private class AddImageAsyncTask extends AsyncTask<Card, Void, Void> {

        @Override
        protected Void doInBackground(Card... cards) {
            final Card card = cards[0];
            CustomVolleyRequest volleyRequest = new CustomVolleyRequest(convertHttpToHttps(card.getImageUrl()), new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    InputStream is = new ByteArrayInputStream(response.data);
                    Bitmap b = BitmapFactory.decodeStream(is);
                    imgCard.setImageBitmap(b);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(volleyRequest);

            return null;
        }
    }

    private String convertHttpToHttps(String uri){
        return uri.replace("http://", "https://");
    }
}
