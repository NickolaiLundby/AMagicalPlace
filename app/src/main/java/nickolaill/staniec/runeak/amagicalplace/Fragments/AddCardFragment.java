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
    private TextView cardTitle;
    private Button returnButton;

    public static AddCardFragment newInstance() {
        AddCardFragment fragment = new AddCardFragment();
        Bundle args = new Bundle();
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

        // TODO: A card should be received to be displayed.
        cardTitle.setText("This text should be replaced by card title");

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddCardFragmentInteraction("Returning from AddCardFragment");
            }
        });

        return v;
    }

    public interface AddCardFragmentListener {
        // TODO: onAddCardFragmentInteraction should take Card card.
        void onAddCardFragmentInteraction(String todoTestStr);
    }
}
