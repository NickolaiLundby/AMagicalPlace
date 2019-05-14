package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;

public class CollectionDetailFragment extends Fragment {
    public static final String ARG_COLLECTION = "collection";
    private boolean twoPaneCreation;
    private Collection collection;
    private CollectionDetailFragmentListener mListener;
    private Button btnUpdate, btnBack;
    private TextView tvDate, tvValue;

    public static CollectionDetailFragment newInstance(Collection collection) {
        CollectionDetailFragment fragment = new CollectionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COLLECTION, collection);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_collection_detail, container, false);
        registerMyReceiver();

        collection = getArguments().getParcelable(ARG_COLLECTION);
        if(collection == null)
            twoPaneCreation = true;

        btnUpdate = v.findViewById(R.id.collection_detail_btn_update);
        btnBack = v.findViewById(R.id.collection_detail_btn_back);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCollectionDetailFragmentUpdate(collection);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCollectionDetailFragmentBack();
            }
        });

        tvDate = v.findViewById(R.id.collection_detail_tv_date);
        tvValue = v.findViewById(R.id.collection_detail_tv_value);

        if(twoPaneCreation){
            tvDate.setText(getResources().getString(R.string.no_collection_selected));
            tvValue.setText(getResources().getString(R.string.no_collection_selected));
        } else{
            if(collection.getLastEvaluated() == null){
                tvDate.setText(getResources().getString(R.string.never_evaluated));
            } else{
                tvDate.setText(collection.getLastEvaluated().toString());
            }

            if(collection.getValue() == 0){
                tvValue.setText(getResources().getString(R.string.never_evaluated));
            } else{
                tvValue.setText(String.valueOf(collection.getValue()));
            }
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CollectionDetailFragmentListener) {
            mListener = (CollectionDetailFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "has to implement the CollectionDetailFragmentListener interface");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface CollectionDetailFragmentListener {
        void onCollectionDetailFragmentBack();
        void onCollectionDetailFragmentUpdate(Collection collection);
    }

    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.BROADCAST_DATABASE_UPDATED);
            getActivity().registerReceiver(receiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Collection collection = intent.getParcelableExtra(Constants.COLLECTION_TAG);
            Bundle args = new Bundle();
            args.putParcelable(ARG_COLLECTION, collection);
            getFragmentManager()
                    .beginTransaction()
                    .detach(CollectionDetailFragment.this)
                    .attach(CollectionDetailFragment.this)
                    .commit();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null)
            getActivity().unregisterReceiver(receiver);
    }
}
