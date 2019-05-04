package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import nickolaill.staniec.runeak.amagicalplace.Fragments.AddCardFragment;
import nickolaill.staniec.runeak.amagicalplace.Fragments.CollectionFragment;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;

public class CollectionActivity extends AppCompatActivity implements AddCardFragment.AddCardFragmentListener, CollectionFragment.CollectionFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        CollectionFragment fragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.collection_container, fragment)
                .commit();
    }

    @Override
    public void onAddCardFragmentInteraction(String todoTestStr) {
        // TODO: Handle actions from the AddCardFragment here
    }

    @Override
    public void onCollectionFragmentAddInteraction(String todoTestStr) {
        // TODO: Handle actions from the CollectionFragment here
        // TODO: Etc, fire up the add fragment.
        Toast.makeText(this, todoTestStr, Toast.LENGTH_SHORT).show();
        AddCardFragment fragment = AddCardFragment.newInstance();
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
