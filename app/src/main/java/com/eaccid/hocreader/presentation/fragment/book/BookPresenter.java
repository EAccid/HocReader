package com.eaccid.hocreader.presentation.fragment.book;

import android.util.Log;
import com.eaccid.hocreader.provider.file.BaseFileImpl;
import com.eaccid.hocreader.provider.file.pagesplitter.Page;
import com.eaccid.hocreader.provider.file.pagesplitter.TxtPagesFromFileProvider;
import com.eaccid.hocreader.data.local.AppDatabaseManager;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.presentation.activity.pager.PagerActivity;
import com.eaccid.hocreader.presentation.activity.pager.PagerPresenter;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BookPresenter implements BasePresenter<BookFragment> {
    private final String logTAG = "BookPresenter";
    private AppDatabaseManager dataManager;
    private BookFragment mView;
    private List<Page<String>> mPagesList;

    public BookPresenter() {
        mPagesList = new ArrayList<>();
    }

    @Override
    public void attachView(BookFragment bookFragment) {
        mView = bookFragment;
        Log.i(logTAG, "BookFragment has been attached.");
        setDataManager();
        setDataToList();
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "BookFragment has been detached.");
        mView = null;
    }

    private void setDataManager() {
        PagerPresenter pp = ((PagerActivity) mView.getActivity()).getPresenter();
        dataManager = pp.getDataManager();
    }

    private void setDataToList() {

        TxtPagesFromFileProvider txtPagesFromFileProvider = new TxtPagesFromFileProvider(mView.getActivity());
        BaseFileImpl baseFile = new BaseFileImpl(dataManager.getCurrentBookPath());
        txtPagesFromFileProvider.getPageObservable(baseFile)
                .subscribeOn(Schedulers.io()).subscribe(new Subscriber<Page<String>>() {
            @Override
            public void onCompleted() {
                mView.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                //TODO on error
            }

            @Override
            public void onNext(Page<String> page) {
                mPagesList.add(page);
            }
        });

    }

    public BookRecyclerViewAdapter createRecyclerViewAdapter() {
        return new BookRecyclerViewAdapter(mPagesList);
    }

    public void goToPageClicked(CharSequence input) {
        int currentPagePosition = dataManager.getCurrentPage() - 1;
        int fromPage = Integer.parseInt(input.toString()) - 1;
        mView.scrollToListPosition(fromPage, currentPagePosition);
        mView.showSnackbarBackToLastOpenedPage(currentPagePosition, fromPage);
    }

    }
