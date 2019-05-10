package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nickolaill.staniec.runeak.amagicalplace.Fragments.OverviewFragment;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.OverviewViewModel;

public class OverviewActivity extends AppCompatActivity implements OverviewFragment.OverviewFragmentListener {
    private OverviewViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);

        if (savedInstanceState == null) {
            OverviewFragment fragment = OverviewFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.overview_container, fragment)
                    .commit();
        }
        else {
            // If there is a savedInstanceState that means there is a fragment, therefor
            // we just leave this bracket empty, as this will automatically recreate the fragment(s)
        }
    }

    @Override
    public void onOverviewFragmentEditCollectionOk(Collection collection) {
        viewModel.update(collection);
    }

    @Override
    public void onOverviewFragmentAddCollectionOk(Collection collection) {
        viewModel.insert(collection);
    }

    @Override
    public void onOverviewFragmentDeleteCollectionOk(Collection collection) {
        viewModel.delete(collection);
    }

    @Override
    public void onOverviewFragmentClickCollection(int id) {
        Intent intent = new Intent(this, CollectionActivity.class);
        intent.putExtra(Constants.COLLECTION_ID, id);

        startActivity(intent);
    }


}
