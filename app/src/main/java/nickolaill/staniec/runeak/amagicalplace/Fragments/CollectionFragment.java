package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Activities.CardActivity;
import nickolaill.staniec.runeak.amagicalplace.Activities.CollectionActivity;
import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapter;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModelFactory;

import static android.app.Activity.RESULT_OK;

public class CollectionFragment extends Fragment {
    private CollectionFragmentListener mListener;

    private static final String ARG_COID = "collectionId";
    private int collectionId;
    private CollectionViewModel viewModel;

    public static CollectionFragment newInstance(int collectionId) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COID, collectionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        final CardAdapter adapter = new CardAdapter();

        FloatingActionButton buttonAddCard = v.findViewById(R.id.button_add_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get rid of the below, and instead call mListener.onCollectionFragmentInteraction("hest");
                // TODO: This call should be handled in the parent activity (CollectionActivity)
                Intent intent = new Intent(getActivity(), CardActivity.class);
                startActivityForResult(intent, Constants.ADD_CARD_REQUEST);
            }
        });

        // Get arguments passed from Args Bundle
        collectionId = getArguments().getInt(ARG_COID);
        if (collectionId == -1)
            return v;

        RecyclerView recyclerView = v.findViewById(R.id.collection_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this, new CollectionViewModelFactory(getActivity().getApplication(), collectionId)).get(CollectionViewModel.class);
        viewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                adapter.submitList(cards);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewModel.delete(adapter.getCardAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Card card) {
                // TODO: Get rid of the below. Instead call mListener.onCollectionFragmentEditInteraction("hest");
                // TODO: This call should be handled in the parent activity (CollectionActivity)
                Intent intent = new Intent(getActivity(), CardActivity.class);
                intent.putExtra(Constants.EDIT_EXTRA_ID, card.getCaId());
                intent.putExtra(Constants.EDIT_EXTRA_TITLE, card.getTitle());
                intent.putExtra(Constants.EDIT_EXTRA_SERIES, card.getSeries());
                intent.putExtra(Constants.EDIT_EXTRA_TEXT, card.getText());

                startActivityForResult(intent, Constants.EDIT_CARD_REQUEST);
            }
        });

        return v;
    }

    // TODO: This should all be moved out of the fragment, and into the collectionactivity, or maybe we can get rid of it altogether.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.ADD_CARD_REQUEST:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(Constants.EDIT_EXTRA_TITLE);
                    String series = data.getStringExtra(Constants.EDIT_EXTRA_SERIES);
                    String text = data.getStringExtra(Constants.EDIT_EXTRA_TEXT);

                    Card card = new Card(title, series, text);
                    Toast.makeText(getActivity(), "Card saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Card not saved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.EDIT_CARD_REQUEST:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(Constants.EDIT_EXTRA_TITLE);
                    String series = data.getStringExtra(Constants.EDIT_EXTRA_SERIES);
                    String text = data.getStringExtra(Constants.EDIT_EXTRA_TEXT);

                    int id = data.getIntExtra(Constants.EDIT_EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(getActivity(), "Data corrupted", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Card card = new Card(title, series, text);
                    card.setCaId(id);

                    viewModel.update(card);
                    Toast.makeText(getActivity(), "Returned from EDIT", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public interface CollectionFragmentListener {
        // TODO: Should take some meaningful parameter back to CollectionActivity.
        void onCollectionFragmentAddInteraction(String todoTestStr);
        void onCollectionFragmentEditInterfaction(String todoTestStr);
    }
}
