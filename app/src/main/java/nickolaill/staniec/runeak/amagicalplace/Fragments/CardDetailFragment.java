package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.R;

public class CardDetailFragment extends Fragment {
    private static final String ARG_CAID = "cardId";
    private TextView txtTitleCard, txtTextCard, txtPriceValue;
    private ImageView imgCard;
    private Button btnReturn;

    public static CardDetailFragment newInstance(int cardId) {
        CardDetailFragment fragment = new CardDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAID, cardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_card_detail, container, false);


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
