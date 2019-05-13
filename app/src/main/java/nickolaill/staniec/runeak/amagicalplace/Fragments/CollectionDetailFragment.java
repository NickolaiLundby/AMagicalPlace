package nickolaill.staniec.runeak.amagicalplace.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Services.PriceService;

public class CollectionDetailFragment extends Fragment {
    public static final String ARG_COLLECTION = "collection";
    private Collection collection;
    private CollectionDetailFragmentListener mListener;
    private Button btnUpdate, btnBack;
    private TextView tvDate, tvValue;

    public static CollectionDetailFragment newInstance(Collection collection) {
        CollectionDetailFragment fragment = new CollectionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COLLECTION, collection);
        //TODO: Any more args are put here
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_collection_detail, container, false);

        collection = getArguments().getParcelable(ARG_COLLECTION);

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
        if(collection.getLastEvaluated() == null){
            tvDate.setText("Never evaluated");
        } else{
            tvDate.setText(collection.getLastEvaluated().toString());
        }

        tvValue = v.findViewById(R.id.collection_detail_tv_value);
        if(collection.getValue() == 0){
            tvValue.setText("Never evaluated");
        } else{
            tvValue.setText(String.valueOf(collection.getValue()));
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
}
