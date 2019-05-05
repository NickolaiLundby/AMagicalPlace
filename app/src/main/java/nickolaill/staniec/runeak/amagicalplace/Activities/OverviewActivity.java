package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nickolaill.staniec.runeak.amagicalplace.Fragments.OverviewFragment;
import nickolaill.staniec.runeak.amagicalplace.R;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

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
}
