package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapter;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.AddCardViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.AddCardViewModelFactory;

public class AddCardFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private AddCardFragmentListener mListener;
    private static final String ARG_COID = "collectionId";
    private int collectionId;
    private TextView fragmentTitle;
    private EditText cardTitle;
    private Spinner seriesDropdown;
    private Button addButton, cancelButton, searchButton;
    private Card cardToBeAdded;
    //
    private AddCardViewModel viewModel;
    //private MutableLiveData<List<Card>> searchResults = new MutableLiveData<>();

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

        final CardAdapter adapter = new CardAdapter();
        RecyclerView recyclerView = v.findViewById(R.id.add_card_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this, new AddCardViewModelFactory(getActivity().getApplication(), new ArrayList<Card>())).get(AddCardViewModel.class);
        viewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                adapter.submitList(cards);
            }
        });

        /* Test thingy -- Should some selection logic be implemented here? */
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Card card) {
                //cardToBeAdded = card;
                Toast.makeText(getActivity(), "Card pressed: " + cardToBeAdded.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        fragmentTitle = v.findViewById(R.id.add_card_tv_fragmenttitle);

        cardTitle = v.findViewById(R.id.add_card_et_cardtitle);
        seriesDropdown = v.findViewById(R.id.add_card_spinner_series);

        ArrayAdapter<CharSequence> stringAdapter = ArrayAdapter.createFromResource(getContext(), R.array.magicsets, R.layout.support_simple_spinner_dropdown_item);

        stringAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seriesDropdown.setAdapter(stringAdapter);
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
        // Can be deleted, right?
        String text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
        ArrayList<String> filter = new ArrayList<>();
        String cardNameString = cardTitle.getText().toString();
        if(cardNameString != null && !cardNameString.isEmpty()){
            filter.add("name="+cardNameString);
        }
        String setNameString = seriesDropdown.getSelectedItem().toString();
        if(setNameString != null && !setNameString.isEmpty()){
            filter.add("setName="+setNameString);
        }
        if(filter.size()>0){
            new APIAsyncTast().execute(filter);
        } else {
            Toast.makeText(getActivity(), R.string.no_search_terms, Toast.LENGTH_SHORT).show();
        }
    }

    private void onApiResult(List<io.magicthegathering.javasdk.resource.Card> cardList){
        if(!cardList.isEmpty()){
            List<Card> cardsRrsults = new ArrayList<>();
            for (io.magicthegathering.javasdk.resource.Card c : cardList){
                cardsRrsults.add(new Card(c));
            }
            viewModel.setAllCards(cardsRrsults);
            //Toast.makeText(getActivity(), "First card: " + cardList.get(0).getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_SHORT).show();
        }
    }

    private class APIAsyncTast extends AsyncTask< ArrayList<String>, Void, Void>{

        private List<io.magicthegathering.javasdk.resource.Card> apiResults;

        @Override
        protected Void doInBackground(ArrayList<String>... arrayLists) {
            apiResults = CardAPI.getAllCards(arrayLists[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onApiResult(apiResults);
        }
    }
}
