package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nickolaill.staniec.runeak.amagicalplace.Fragments.CollectionFragment;
import nickolaill.staniec.runeak.amagicalplace.R;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        // TODO: Specify any args
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.overview_container, fragment)
                .commit();
    }
}
