package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
                EditCollectionDialogBuilder(collection);
            }
        });

        return v;
    }

    // TODO: Rewrite this method to use a LayoutInflater
    // TODO: Design the dialogbox in .xml and use that to inflate the layout
    private void EditCollectionDialogBuilder(final Collection collection){
        LayoutInflater mLayout = LayoutInflater.from(getContext());
        final View dialogView = mLayout.inflate(R.layout.edit_collection_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        //Edit collection dialog set/get title
        final EditText editTitle = dialogView.findViewById(R.id.editText_editCollectionDialogTitle);
        editTitle.setText(collection.getTitle());

        //Edit collection dialog set/get description
        final EditText editDescription = dialogView.findViewById(R.id.editText_editCollectionDialogDescription);
        editDescription.setText(collection.getDescription());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Collection col = new Collection(editTitle.getText().toString(), editDescription.getText().toString());
                col.setCoId(collection.getCoId());
                viewModel.update(col);
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
}
