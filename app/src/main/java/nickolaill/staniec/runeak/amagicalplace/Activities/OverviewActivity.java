package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nickolaill.staniec.runeak.amagicalplace.Fragments.CollectionDetailFragment;
import nickolaill.staniec.runeak.amagicalplace.Fragments.OverviewFragment;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Services.PriceService;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.OverviewViewModel;

public class OverviewActivity extends AppCompatActivity implements OverviewFragment.OverviewFragmentListener, CollectionDetailFragment.CollectionDetailFragmentListener {
    private OverviewViewModel viewModel;
    private boolean mBound;
    private PriceService priceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        viewModel = ViewModelProviders.of(this).get(OverviewViewModel.class);

        if (savedInstanceState == null) {
            OverviewFragment fragment = OverviewFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.overview_container, fragment, Constants.TAG_FRAGMENT_OVERVIEW)
                    .commit();
        }
        else {
            // If there is a savedInstanceState that means there is a fragment, therefor
            // we just leave this bracket empty, as this will automatically recreate the fragment(s)
        }

        Intent priceServiceIntent = new Intent(this, PriceService.class);
        getBaseContext().startService(priceServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent priceServiceIntent = new Intent(this, PriceService.class);
        bindService(priceServiceIntent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBound = true;
            PriceService.PriceBinder mPriceBinder = (PriceService.PriceBinder)service;
            priceService = mPriceBinder.getServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            priceService = null;
        }
    };

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

    @Override
    public void onOverviewFragmentLongClickCollection(Collection collection) {
        CollectionDetailFragment fragment = CollectionDetailFragment.newInstance(collection);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.overview_container, fragment, Constants.TAG_FRAGMENT_COLLECTIONDETAIL)
                .commit();
    }

    @Override
    public void onCollectionDetailFragmentBack() {
        OverviewFragment fragment = OverviewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.overview_container, fragment, Constants.TAG_FRAGMENT_OVERVIEW)
                .commit();
    }

    @Override
    public void onCollectionDetailFragmentUpdate(Collection collection) {
        priceService.testApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
