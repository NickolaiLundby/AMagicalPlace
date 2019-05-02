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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit your collection");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView titleInputText = new TextView(getContext());
        titleInputText.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInputText.setText("Title:");
        layout.addView(titleInputText);

        final EditText titleInput = new EditText(getContext());
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setText(collection.getTitle());
        layout.addView(titleInput);

        final TextView descriptionInputText = new TextView(getContext());
        descriptionInputText.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptionInputText.setText("Description:");
        layout.addView(descriptionInputText);

        final EditText descriptionInput = new EditText(getContext());
        descriptionInput.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptionInput.setText(collection.getDescription());
        layout.addView(descriptionInput);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                collection.setTitle(titleInput.getText().toString());
                collection.setDescription(descriptionInput.getText().toString());
                viewModel.update(collection);
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
