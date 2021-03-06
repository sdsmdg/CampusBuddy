package in.co.mdg.campusBuddy.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.co.mdg.campusBuddy.R;
import in.co.mdg.campusBuddy.fragments.ContactsFragment;


public class DeptListFragment extends Fragment {

    private static final String ARG_TYPE = "type";

    private int mType;
    public ContactsRecyclerAdapter adapter;


    public DeptListFragment() {

    }

    public static DeptListFragment newInstance(int type) {
        DeptListFragment fragment = new DeptListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getBaseContext(), R.drawable.divider, metrics.density));
        adapter = new ContactsRecyclerAdapter();
        adapter.setClickListener((ContactsFragment)getParentFragment());
        adapter.setListData(mType, null);
        recyclerView.setAdapter(adapter);
        RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) view.findViewById(R.id.fastscroller);
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
        return view;
    }

    public void setLang(int lang) {
        if(adapter!=null)
        adapter.setLang(lang);
    }

    public int getLang() {
        return adapter.getLang();
    }

    @Override
    public void onDestroy() {
        adapter.closeRealm();
        super.onDestroy();
    }
}
