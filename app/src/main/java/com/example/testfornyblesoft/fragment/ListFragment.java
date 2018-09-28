package com.example.testfornyblesoft.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.activity.DescriptionActivity;
import com.example.testfornyblesoft.adapter.OnItemClickListener;
import com.example.testfornyblesoft.adapter.RecyclerAdapter;
import com.example.testfornyblesoft.adapter.SavedData;
import com.example.testfornyblesoft.preference.Preferences;

import java.util.List;

public class ListFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private List<SavedData> data;
    private RecyclerView rvHistory;

    public ListFragment() {
    }


    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rvHistory = view.findViewById(R.id.rvHistory);
        initRecyclerView();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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

    private void initRecyclerView() {
        loadData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvHistory.setLayoutManager(linearLayoutManager);
        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(getContext(), DescriptionActivity.class);
                intent.putExtra("position", pos);
                startActivity(intent);
                getActivity().finish();
            }
        };
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(data, onItemClickListener);
        rvHistory.setAdapter(recyclerAdapter);

    }

    private void loadData() {
        Preferences preferences = Preferences.getPreferences(getContext());
        data = preferences.loadFile();
    }

}
