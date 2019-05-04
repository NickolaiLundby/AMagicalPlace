package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;

public class AddCardFragment extends Fragment {
    private AddCardFragmentListener mListener;
    private static final String ARG_COID = "collectionId";
    private int collectionId;
    private TextView cardTitle;
    private Button returnButton;

    public static AddCardFragment newInstance(int collectionId) {
        AddCardFragment fragment = new AddCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COID, collectionId);
        // TODO: Any args needed should be put here.
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_card, container, false);
        cardTitle = v.findViewById(R.id.add_card_tv_title);
        returnButton = v.findViewById(R.id.add_card_button_return);

        // Get arguments passed from Args Bundle
        collectionId = getArguments().getInt(ARG_COID);
        if (collectionId == -1)
            return v;

        // TODO: A card should be received to be displayed.
        cardTitle.setText("This text should be replaced by card title");

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: A new card object should be generated based on the selection by the user.
                Card card = new Card("Kird Ape", "Alpha", "Monkey man.");
                card.setCollectionId(collectionId);
                mListener.onAddCardFragmentInteraction(card);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddCardFragmentListener) {
            mListener = (AddCardFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "has to implement the AddCardFragmentListener interface");
        }
    }

    public interface AddCardFragmentListener {
        // TODO: onAddCardFragmentInteraction should take Card card.
        void onAddCardFragmentInteraction(Card card);
    }
}
