package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import nickolaill.staniec.runeak.amagicalplace.Fragments.AddCardFragment;
import nickolaill.staniec.runeak.amagicalplace.Fragments.CardDetailFragment;
import nickolaill.staniec.runeak.amagicalplace.Fragments.CollectionFragment;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.Utilities.CustomVolleyRequest;
import nickolaill.staniec.runeak.amagicalplace.Utilities.StorageUtils;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModel;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.CollectionViewModelFactory;

public class CollectionActivity extends AppCompatActivity implements AddCardFragment.AddCardFragmentListener, CollectionFragment.CollectionFragmentListener, CardDetailFragment.CardDetailFragmentListener {
    private CollectionViewModel viewModel;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        viewModel = ViewModelProviders.of(this, new CollectionViewModelFactory(getApplication(), getIntent().getIntExtra(Constants.COLLECTION_ID, -1))).get(CollectionViewModel.class);

        if(findViewById(R.id.wide_collection_container) != null){
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        if (mTwoPane) {
            twoPaneCreation(savedInstanceState);
        } else {
            singlePaneCreation(savedInstanceState);
        }
    }

    private void singlePaneCreation(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            CollectionFragment collectionFragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1), false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, collectionFragment, Constants.TAG_FRAGMENT_COLLECTION)
                    .commit();
        }
        else {
            // If there is a savedInstanceState that means there is a fragment, therefor
            // we just leave this bracket empty, as this will automatically recreate the fragment(s)
        }
    }

    private void twoPaneCreation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            CollectionFragment collectionFragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1), false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, collectionFragment, Constants.TAG_FRAGMENT_COLLECTION)
                    .commit();
            AddCardFragment addCardFragment = AddCardFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wide_card_fragment_container, addCardFragment, Constants.TAG_FRAGMENT_ADDCARD)
                    .commit();
        }
        else {
            // If there is a savedInstanceState that means there is a fragment, therefor
            // we just leave this bracket empty, as this will automatically recreate the fragment(s
        }
    }

    @Override
    public void onAddCardFragmentAddInteraction(Card card) {
        // TODO: Handle actions from the AddCardFragment here
        viewModel.insert(card);

        // TODO: Then show the collection fragment again
        if (mTwoPane) {
            // In twoPane we should always be showing the collection on the left side.
        }
        else {
            CollectionFragment collectionFragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1), false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, collectionFragment, Constants.TAG_FRAGMENT_COLLECTION)
                    .commit();
        }

    }

    @Override
    public void onAddCardFragmentCancelInteraction() {
        // TODO: Handle actions from the AddCardFragment here

        // TODO: Then show the collection fragment again
        if (mTwoPane) {
            // In twoPane we should always be showing the collection on the left side.
        }
        else {
            CollectionFragment collectionFragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1), false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, collectionFragment, Constants.TAG_FRAGMENT_COLLECTION)
                    .commit();
        }
    }

    @Override
    public void onCollectionFragmentAddInteraction() {
        // TODO: Handle actions from the CollectionFragment here

        // TODO: Etc, fire up the add fragment.
        if (mTwoPane) {
            AddCardFragment addCardFragment = AddCardFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wide_card_fragment_container, addCardFragment, Constants.TAG_FRAGMENT_ADDCARD)
                    .commit();
        }
        else {
            AddCardFragment addCardFragment = AddCardFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, addCardFragment, Constants.TAG_FRAGMENT_ADDCARD)
                    .commit();
        }
    }

    @Override
    public void onCollectionFragmentDetailInteraction(Card card) {
        // TODO: Handle actions from the CollectionFragment here -> The received paramenter should be Card instead.

        // TODO: Get rid of the below, and fire up the DetailCardFragment instead.

        if (mTwoPane) {
            CardDetailFragment fragment = CardDetailFragment.newInstance(card, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wide_card_fragment_container, fragment)
                    .commit();
        }
        else {
            //Toast.makeText(this, "DetailCardFragment needs implementation in singlePaneView", Toast.LENGTH_SHORT).show();

            CardDetailFragment fragment = CardDetailFragment.newInstance(card, false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onCollectionFragmentIncreaseQuantity(Card card) {
        card.setQuantity(card.getQuantity() + 1);
        viewModel.update(card);
    }

    @Override
    public void onCollectionFragmentDecreaseQuantity(Card card) {
        if(card.getQuantity() == 1){
            viewModel.delete(card);
        }
        else{
            card.setQuantity(card.getQuantity() - 1);
            viewModel.update(card);
        }
    }

    @Override
    public void onCardDetailFragmentCancelInteraction() {
        // TODO: Handle actions from the CardDetailFragment here

        // TODO: Then show the collection fragment again
        if (mTwoPane) {
            // In twoPane we should always be showing the collection on the left side.
        }
        else {
            CollectionFragment collectionFragment = CollectionFragment.newInstance(getIntent().getIntExtra(Constants.COLLECTION_ID, -1), false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.collection_container, collectionFragment, Constants.TAG_FRAGMENT_COLLECTION)
                    .commit();
        }
    }

}
