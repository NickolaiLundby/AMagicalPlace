package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Activities.CollectionActivity;
import nickolaill.staniec.runeak.amagicalplace.Adapters.CollectionAdapter;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.OverviewViewModel;


public class OverviewFragment extends Fragment {
    private OverviewFragmentListener mListener;
    private OverviewViewModel viewModel;

    public static OverviewFragment newInstance(){
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        // TODO: Specify args
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        final CollectionAdapter adapter = new CollectionAdapter();

        FloatingActionButton buttonAddCard = v.findViewById(R.id.button_add_collection);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverviewDialogBuilder(null);
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.overview_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);
        viewModel.getAllCollections().observe(this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(@Nullable List<Collection> collections) {
                adapter.submitList(collections);
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
                viewModel.delete(adapter.getCollectionAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Collection collection) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                intent.putExtra(Constants.COLLECTION_ID, collection.getCoId());

                startActivity(intent);
            }

            @Override
            public void onButtonItemClick(Collection collection) {
                OverviewDialogBuilder(collection);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OverviewFragmentListener) {
            mListener = (OverviewFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "has to implement the OverviewFragmentListener interface");
        }
    }

    // TODO: Rewrite this be a fragment, so that is may persist on screen orientation
    private void OverviewDialogBuilder(final Collection collection){
        LayoutInflater mLayout = LayoutInflater.from(getContext());
        final View dialogView = mLayout.inflate(R.layout.overview_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        // Find objects
        final TextView tvTop = dialogView.findViewById(R.id.overview_dialog_tv_top);
        final EditText collectionTitle = dialogView.findViewById(R.id.overview_dialog_et_title);
        final EditText collectionDescription = dialogView.findViewById(R.id.overview_dialog_et_description);

        // Set objects
        if(collection == null) {
            // Means we're adding a collection
            tvTop.setText(getResources().getText(R.string.add_collection));
        } else {
            // Means we're editing a collection
            tvTop.setText(getResources().getText(R.string.edit_collection));
            collectionTitle.setText(collection.getTitle());
            collectionDescription.setText(collection.getDescription());
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(collection == null) {
                    // Means we're adding a collection
                    if(collectionTitle.getText().toString().isEmpty() ||
                            collectionDescription.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(), "Fill in both title and description!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Collection collection = new Collection(collectionTitle.getText().toString(), collectionDescription.getText().toString());
                    mListener.onOverviewFragmentAddCollectionOk(collection);
                } else {
                    // Means we're editing a collection
                    Collection col = new Collection(collectionTitle.getText().toString(), collectionDescription.getText().toString());
                    col.setCoId(collection.getCoId());
                    mListener.onOverviewFragmentEditCollectionOk(col);
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public interface OverviewFragmentListener {
        void onOverviewFragmentEditCollectionOk(Collection collection);
        void onOverviewFragmentAddCollectionOk(Collection collection);
    }
}
