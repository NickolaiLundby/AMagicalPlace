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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import android.widget.Button;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;

public class AddCardFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private AddCardFragmentListener mListener;
    private static final String ARG_COID = "collectionId";
    private int collectionId;
    private TextView fragmentTitle;
    private EditText cardTitle;
    private Spinner seriesDropdown;
    private Button addButton, cancelButton, searchButton;
    private Card cardToBeAdded;

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
        fragmentTitle = v.findViewById(R.id.add_card_tv_fragmenttitle);

        cardTitle = v.findViewById(R.id.add_card_et_cardtitle);
        seriesDropdown = v.findViewById(R.id.add_card_spinner_series);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.setsCodes, R.layout.support_simple_spinner_dropdown_item);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.magicseries, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seriesDropdown.setAdapter(adapter);
        seriesDropdown.setOnItemSelectedListener(AddCardFragment.this);

        addButton = v.findViewById(R.id.add_card_button_add);
        cancelButton = v.findViewById(R.id.add_card_button_cancel);
        searchButton = v.findViewById(R.id.add_card_button_search);

        // Get arguments passed from Args Bundle
        collectionId = getArguments().getInt(ARG_COID);
        if (collectionId == -1)
            return v;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddButtonClicked();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddCardFragmentCancelInteraction();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchButtonClicked();
            }
        });

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        void onAddCardFragmentAddInteraction(Card card);
        void onAddCardFragmentCancelInteraction();
    }

    private void AddButtonClicked(){
        // TODO: A new card object should be generated based on the selection by the user.
        if (cardToBeAdded == null) {
            Toast.makeText(getActivity(), "No card selected", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mListener.onAddCardFragmentAddInteraction(cardToBeAdded);
        }
    }

    private void SearchButtonClicked(){
        Toast.makeText(getActivity(), "Search button clicked", Toast.LENGTH_SHORT).show();
    }
}
