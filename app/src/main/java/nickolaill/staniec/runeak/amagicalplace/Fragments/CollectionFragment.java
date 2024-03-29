package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapterGridView;
import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapterListView;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModelFactory;

public class CollectionFragment extends Fragment {
    private CollectionFragmentListener mListener;

    private static final String ARG_COID = "collectionId";
    private static final String ARG_GRIDVIEW = "gridViewId";
    private int collectionId;
    private boolean gridView;
    private CollectionViewModel viewModel;

    public static CollectionFragment newInstance(int collectionId, boolean gridView) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COID, collectionId);
        args.putBoolean(ARG_GRIDVIEW, gridView);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_collection, container, false);

        FloatingActionButton buttonAddCard = v.findViewById(R.id.button_add_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCollectionFragmentAddInteraction();
            }
        });

        // Get arguments passed from Args Bundle
        collectionId = getArguments().getInt(ARG_COID);
        if (collectionId == -1)
            return v;
        gridView = getArguments().getBoolean(ARG_GRIDVIEW);

        final RecyclerView recyclerView = v.findViewById(R.id.collection_recycler_view);
        if(gridView) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int spanCount = (int)dpWidth / 180;
            if(spanCount < 3)
                spanCount = 3;
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
            recyclerView.setHasFixedSize(true);
            CardAdapterGridView adapter = new CardAdapterGridView();
            adapter.setViewModel(this.viewModel);
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new CardAdapterListView(true));
        }

        viewModel = ViewModelProviders.of(this, new CollectionViewModelFactory(getActivity().getApplication(), collectionId)).get(CollectionViewModel.class);
        viewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                if(recyclerView.getAdapter() instanceof CardAdapterListView){
                    ((CardAdapterListView) recyclerView.getAdapter()).submitList(cards);
                }
                else if(recyclerView.getAdapter() instanceof CardAdapterGridView){
                    ((CardAdapterGridView) recyclerView.getAdapter()).submitList(cards);
                }
            }
        });

        if(recyclerView.getAdapter() instanceof CardAdapterListView){
            ((CardAdapterListView) recyclerView.getAdapter()).setOnItemClickListener(new CardAdapterListView.OnItemClickListener() {
                @Override
                public void onItemClick(Card card) {
                    mListener.onCollectionFragmentDetailInteraction(card);
                }

                @Override
                public void onIncreaseItemClick(Card card) {
                    mListener.onCollectionFragmentIncreaseQuantity(card);
                }

                @Override
                public void onDecreaseItemClick(Card card) {
                    mListener.onCollectionFragmentDecreaseQuantity(card);
                }
            });
        }
        else if(recyclerView.getAdapter() instanceof CardAdapterGridView){
            ((CardAdapterGridView) recyclerView.getAdapter()).setOnItemClickListener(new CardAdapterGridView.OnItemClickListener() {
                @Override
                public void onItemClick(Card card) {
                    mListener.onCollectionFragmentDetailInteraction(card);
                }
            });
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CollectionFragmentListener) {
            mListener = (CollectionFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "has to implement the CollectionFragmentListener interface");
        }
    }

    public interface CollectionFragmentListener {
        // TODO: Should take some meaningful parameter back to CollectionActivity.
        void onCollectionFragmentAddInteraction();
        void onCollectionFragmentDetailInteraction(Card card);
        void onCollectionFragmentIncreaseQuantity(Card card);
        void onCollectionFragmentDecreaseQuantity(Card card);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_collectionactivity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CollectionFragment frg = (CollectionFragment) getFragmentManager().findFragmentByTag(Constants.TAG_FRAGMENT_COLLECTION);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        switch (item.getItemId()) {
            case R.id.menu_grid_on:
                args.putBoolean(ARG_GRIDVIEW, true);
                break;
            case R.id.menu_grid_off:
                args.putBoolean(ARG_GRIDVIEW, false);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        CollectionFragment.this.setArguments(args);
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        return super.onOptionsItemSelected(item);
    }
}
