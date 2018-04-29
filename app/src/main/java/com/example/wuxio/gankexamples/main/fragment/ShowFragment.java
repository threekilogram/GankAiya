package com.example.wuxio.gankexamples.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuxio.gankexamples.R;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment {


    protected View         rootView;
    protected RecyclerView mRecycler;


    public static ShowFragment newInstance() {

        ShowFragment fragment = new ShowFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_show, container, false);
        initView(rootView);
        return rootView;
    }


    private void initView(View rootView) {

        mRecycler = rootView.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(new RecyclerAdapter());
    }

    //============================ recycler adapter ============================

    private class RecyclerAdapter extends RecyclerView.Adapter< RecyclerAdapter.Holder > {

        private LayoutInflater mInflater;


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }
            View view = mInflater.inflate(R.layout.item_text_view, parent, false);
            return new Holder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.bind(position);
        }


        @Override
        public int getItemCount() {

            return 50;
        }


        class Holder extends RecyclerView.ViewHolder {

            Holder(View itemView) {

                super(itemView);
            }


            void bind(int i) {

                ((TextView) itemView).setText(String.valueOf(i));
            }
        }

    }
}
