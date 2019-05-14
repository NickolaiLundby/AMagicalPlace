package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapterListView;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.Utilities.InternetUtils;
import nickolaill.staniec.runeak.amagicalplace.Utilities.StorageUtils;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.AddCardViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.AddCardViewModelFactory;

public class AddCardFragment extends Fragment{
    private AddCardFragmentListener mListener;
    private static final String ARG_COID = "collectionId";
    private static final String ARG_MODE = "mode";
    private int collectionId;
    private boolean mode;
    private EditText cardTitle;
    private Spinner seriesDropdown;
    private Card cardToBeAdded;
    private AddCardViewModel viewModel;
    private RelativeLayout loadingPanel;

    public static AddCardFragment newInstance(int collectionId, boolean mode) {
        AddCardFragment fragment = new AddCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COID, collectionId);
        args.putBoolean(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_add_card, container, false);

        final CardAdapterListView adapter = new CardAdapterListView(false);
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

        adapter.setOnItemClickListener(new CardAdapterListView.OnItemClickListener() {
            @Override
            public void onItemClick(Card card) {
                cardToBeAdded = card;
                cardToBeAdded.setCollectionId(collectionId);
                cardToBeAdded.setQuantity(1);

            }

            @Override
            public void onIncreaseItemClick(Card card) {
                // Since quantity should always be 0, the visibility is in the adapter set to gone
                // and this button will never show in this fragment. Hence empty implementation
            }

            @Override
            public void onDecreaseItemClick(Card card) {
                // Since quantity should always be 0, the visibility is in the adapter set to gone
                // and this button will never show in this fragment. Hence empty implementation
            }
        });

        cardTitle = v.findViewById(R.id.add_card_et_cardtitle);
        seriesDropdown = v.findViewById(R.id.add_card_spinner_series);

        ArrayAdapter<CharSequence> stringAdapter = ArrayAdapter.createFromResource(getContext(), R.array.magicsets, R.layout.support_simple_spinner_dropdown_item);

        stringAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        seriesDropdown.setAdapter(stringAdapter);

        Button addButton = v.findViewById(R.id.add_card_button_add);
        Button cancelButton = v.findViewById(R.id.add_card_button_cancel);
        Button searchButton = v.findViewById(R.id.add_card_button_search);

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

        mode = getArguments().getBoolean(ARG_MODE);
        if(!mode){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddCardFragmentCancelInteraction();
                }
            });
        } else {
            cancelButton.setVisibility(View.INVISIBLE);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchButtonClicked();
            }
        });

        //https://stackoverflow.com/questions/5442183/using-the-animated-circle-in-an-imageview-while-loading-stuff
        loadingPanel = v.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

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
        void onAddCardFragmentAddInteraction(Card card);
        void onAddCardFragmentCancelInteraction();
    }

    private void AddButtonClicked(){
        // TODO: A new card object should be generated based on the selection by the user.
        if (cardToBeAdded == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_card_selected), Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if(StorageUtils.isExternalStorageWritable())
                mListener.onAddCardFragmentAddInteraction(cardToBeAdded);
            else
                Toast.makeText(getActivity(), getResources().getString(R.string.external_storage_unwritable), Toast.LENGTH_SHORT).show();
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
            loadingPanel.setVisibility(View.VISIBLE);
            List<Card> noCards = new ArrayList<>();
            viewModel.setAllCards(noCards);
            new APIAsyncTask().execute(filter);
        } else {
            Toast.makeText(getActivity(), R.string.no_search_terms, Toast.LENGTH_SHORT).show();
        }
    }

    private void onApiResult(List<io.magicthegathering.javasdk.resource.Card> cardList){
        loadingPanel.setVisibility(View.GONE);
        if(cardList != null){
            if(!cardList.isEmpty()){
                List<Card> cardsRrsults = new ArrayList<>();
                for (io.magicthegathering.javasdk.resource.Card c : cardList){
                    cardsRrsults.add(new Card(c));
                }
                viewModel.setAllCards(cardsRrsults);
            } else {
                //TODO: better way of showing there is no result
                Toast.makeText(getActivity(), R.string.no_result, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e(Constants.LOG_TAG_API_ERROR, "APIAsyncTask return null");
        }

    }

    private void onNoInternetConnection(){
        //TODO: better way of showing if connected
        Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    private class APIAsyncTask extends AsyncTask< ArrayList<String>, Void, Boolean>{

        private List<io.magicthegathering.javasdk.resource.Card> apiResults;

        protected Boolean doInBackground(ArrayList<String>... arrayLists) {

            if(InternetUtils.isOnline()){
                try {
                    apiResults = CardAPI.getAllCards(arrayLists[0]);
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG_API_ERROR, e.getMessage());
                    e.printStackTrace();
                }
                return true;
            } else {
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean isOnline) {
            super.onPostExecute(isOnline);
            if(isOnline)
                onApiResult(apiResults);
            else
                onNoInternetConnection();
        }
    }

}
