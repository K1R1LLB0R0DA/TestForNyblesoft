package com.example.testfornyblesoft.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testfornyblesoft.R;


public class NavigationFragment extends Fragment {
    /*   private static final String ARG_PARAM1 = "param1";
       private static final String ARG_PARAM2 = "param2";

       private String mParam1;
       private String mParam2;
   */
    private OnNavigationFragmentInteractionListener mListener;
    private TextView tvLocation;
    private Button bGetLocation;
    private Location location;

    public NavigationFragment() {
    }


    public static NavigationFragment newInstance() {
        NavigationFragment fragment = new NavigationFragment();
     /*   Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
     */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        tvLocation = view.findViewById(R.id.tvLocation);
        bGetLocation = view.findViewById(R.id.bGetLocation);
        bGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = mListener.getLocation();
                tvLocation.setText(location.getLatitude() + " , " + location.getLongitude());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationFragmentInteractionListener) {
            mListener = (OnNavigationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNavigationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
