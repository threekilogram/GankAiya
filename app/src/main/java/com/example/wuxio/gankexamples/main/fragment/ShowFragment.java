package com.example.wuxio.gankexamples.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.constraintlayout.Constraint;
import com.example.constraintlayout.ConstraintLayout;
import com.example.constraintlayout.adapter.BaseConstraintAdapter;
import com.example.objectbus.message.Messengers;
import com.example.objectbus.message.OnMessageReceiveListener;
import com.example.wuxio.gankexamples.R;
import com.example.wuxio.gankexamples.model.GankCategoryBean;

import java.util.List;

/**
 * @author wuxio 2018-04-29:9:23
 */
public class ShowFragment extends Fragment implements OnMessageReceiveListener {

    private static final String TAG = "ShowFragment";

    protected View               rootView;
    protected RecyclerView       mRecycler;
    protected SwipeRefreshLayout mSwipeRefresh;


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

        mSwipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        mRecycler = rootView.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Messengers.send(11, 1000, ShowFragment.this);
            }
        });

    }

    //============================ message receive ============================


    @Override
    public void onReceive(int what) {

        final int flagStopRefresh = 11;

        if (what == flagStopRefresh) {
            mSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onDestroy() {

        Messengers.remove(11, this);
        super.onDestroy();
    }

    //============================ load data ============================


    public void loadData(String category) {

        ShowFragmentManager instance = ShowFragmentManager.getInstance();
        instance.register(this);
        instance.loadData(category);
    }


    public void dataReady(List< GankCategoryBean > categoryBeans) {

        mSwipeRefresh.setRefreshing(false);

        if (categoryBeans != null) {
            mRecycler.setAdapter(new RecyclerAdapter(categoryBeans));
        }
    }

    //============================ recycler adapter ============================

    private class RecyclerAdapter extends RecyclerView.Adapter< RecyclerAdapter.Holder > {

        private LayoutInflater mInflater;
        List< GankCategoryBean > mCategoryBeans;
        private ShowConstraintAdapter mAdapter;


        public RecyclerAdapter(List< GankCategoryBean > categoryBeans) {

            this.mCategoryBeans = categoryBeans;
        }


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (mInflater == null) {
                mInflater = LayoutInflater.from(parent.getContext());
            }

            View view = mInflater.inflate(
                    R.layout.item_show_pager_recycler,
                    parent,
                    false
            );

            return new Holder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.bind(position);
        }


        @Override
        public int getItemCount() {

            return mCategoryBeans.size();
        }


        class Holder extends RecyclerView.ViewHolder {

            ConstraintLayout mConstraintLayout;


            Holder(View itemView) {

                super(itemView);

                mConstraintLayout = itemView.findViewById(R.id.constraintLayout);
            }


            void bind(int position) {

                if (mAdapter == null) {
                    mAdapter = new ShowConstraintAdapter();
                }
                mAdapter.setCategoryBean(mCategoryBeans.get(position));
                mAdapter.setConstraintLayout(mConstraintLayout);
                mConstraintLayout.setAdapter(mAdapter);
            }
        }
    }

    //============================ recycler item adapter ============================

    private class ShowConstraintAdapter extends BaseConstraintAdapter {

        private GankCategoryBean mCategoryBean;
        private ConstraintLayout mConstraintLayout;


        public void setCategoryBean(GankCategoryBean categoryBean) {

            mCategoryBean = categoryBean;
        }


        public void setConstraintLayout(ConstraintLayout constraintLayout) {

            mConstraintLayout = constraintLayout;
        }


        @Override
        public View generateViewTo(int i) {

            if (i == 0) {
                TextView textView = new TextView(getContext());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView.setTextColor(getResources().getColor(R.color.category_text));
                if (mCategoryBean != null) {
                    textView.setText(mCategoryBean.desc);
                }
                return textView;
            }

            if (i <= 3) {
                TextView textView = new TextView(getContext());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                textView.setTextColor(getResources().getColor(R.color.category_text2));

                if (mCategoryBean != null) {

                    switch (i) {
                        case 1:
                            textView.setText(mCategoryBean.who);
                            break;
                        case 2:
                            textView.setText(mCategoryBean.publishedAt.substring(0, 10));
                            break;
                        case 3:
                            textView.setText(mCategoryBean.type);
                            break;
                        default:
                            break;
                    }
                }

                return textView;
            }

            if (i < getChildCount()) {
                return new ImageView(getContext());
            }

            return null;
        }


        @Override
        public ConstraintLayout.LayoutParams generateLayoutParamsTo(int position) {

            if (position == 0) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            if (position <= 3) {
                return new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }

            return super.generateLayoutParamsTo(position);
        }


        @Override
        public Constraint generateConstraintTo(int i, Constraint constraint) {

            if (i == 0) {
                constraint.leftToLeftOfParent(20)
                        .topToTopOfParent(20)
                        .rightToRightOfParent(-20);
                return constraint;
            }

            if (i == 1) {

                int imagesSize = getImagesSize();
                if (imagesSize > 0) {
                    int size = constraint.getWeightWidth(4, 1, 20 + 20 + 20 + 20);
                    int j = imagesSize / 3 + 1;
                    constraint.leftToLeftOfParent(20)
                            .topToBottomOfView(i - 1, 20 + j * size + 20);
                } else {
                    constraint.leftToLeftOfParent(20)
                            .topToBottomOfView(i - 1, 20);
                }
                return constraint;
            } else if (i == 2) {
                constraint.rightToRightOfParent(-20)
                        .topToTopOfView(i - 1, 0);
                return constraint;
            } else if (i == 3) {

                constraint.rightToLeftOfView(i - 1, -20)
                        .topToTopOfView(i - 1, 0);

                int imagesSize = getImagesSize();
                if (imagesSize == 0) {
                    int count = mConstraintLayout.getChildCount();
                    for (int j = 4; j < count; j++) {
                        View child = mConstraintLayout.getChildAt(j);
                        if (child != null) {
                            child.setVisibility(View.GONE);
                            ((ImageView) child).setImageBitmap(null);
                        }
                    }
                }

                return constraint;
            }

            int j = i - 4;
            if (j == 0) {
                int size = constraint.getWeightWidth(4, 1, 20 + 20 + 20 + 20);
                constraint.leftToLeftOfParent(20, size).topToBottomOfView(0, 20, size);

            } else if (j <= 2) {
                constraint.copyFrom(i - 1).translateX(constraint.getViewWidth(i - 1) + 20);

            } else {

                constraint.copyFrom(i - 3).translateY(constraint.getViewHeight(i - 3) + 20);
            }

            View view = mConstraintLayout.getChildAt(i);
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }

            return constraint;
        }


        @Override
        public void afterMeasure(int position, View view) {

            int j = position - 4;
            if (j >= 0) {
                String url = mCategoryBean.images.get(j);
                ImageView imageView = (ImageView) view;
                imageView.setImageResource(R.drawable.music);
                view.setTag(R.id.show_item_image, url);
            }
        }


        @Override
        public int getChildCount() {

            return 4 + getImagesSize();
        }


        public int getImagesSize() {

            return mCategoryBean.images == null ? 0 : mCategoryBean.images.size();
        }
    }
}
