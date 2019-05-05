package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import nickolaill.staniec.runeak.amagicalplace.Fragments.AddCardFragment;
import nickolaill.staniec.runeak.amagicalplace.Fragments.CollectionFragment;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModelFactory;

public class CollectionActivity extends AppCompatActivity implements AddCardFragment.AddCardFragmentListener, CollectionFragment.CollectionFragmentListener {
    private CollectionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        viewModel = ViewModelProviders.of(this, new CollectionViewModelFactory(getApplication(), getIntent().getIntExtra(Constants.COLLECTION_ID, -1))).get(CollectionViewModel.class);

        if (savedInstanceState == null) {
            CollectionFragment fragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, fragment)
                    .commit();
        }
        else {
            // If there is a savedInstanceState that means there is a fragment, therefor
            // we just leave this bracket empty, as this will automatically recreate the fragment(s)
        }
    }

    @Override
    public void onAddCardFragmentAddInteraction(Card card) {
        // TODO: Handle actions from the AddCardFragment here
        viewModel.insert(card);

        // TODO: Then show the collection fragment again
        CollectionFragment fragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.collection_container, fragment)
                .commit();
    }

    @Override
    public void onAddCardFragmentCancelInteraction() {
        // TODO: Handle actions from the AddCardFragment here

        // TODO: Then show the collection fragment again
        CollectionFragment fragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.collection_container, fragment)
                .commit();
    }

    @Override
    public void onCollectionFragmentAddInteraction(String todoTestStr) {
        // TODO: Handle actions from the CollectionFragment here
        // TODO: Etc, fire up the add fragment.
        AddCardFragment fragment = AddCardFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.collection_container, fragment)
                .commit();
    }

    @Override
    public void onCollectionFragmentEditInterfaction(String todoTestStr) {
        // TODO: Handle actions from the CollectionFragment here
        // TODO: Etc, fire up the edit fragment.
    }
}
