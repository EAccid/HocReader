package com.eaccid.hocreader.presentation.book;

import android.util.Log;

import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.provider.db.books.BookInteractor;
import com.eaccid.hocreader.provider.db.words.BookOnReadProvider;
import com.eaccid.hocreader.provider.file.BaseFileImpl;
import com.eaccid.hocreader.provider.file.pagesplitter.CharactersDefinerForFullScreenTextView;
import com.eaccid.hocreader.provider.file.pagesplitter.Page;
import com.eaccid.hocreader.provider.file.pagesplitter.TxtPagesFromFileProvider;
import com.eaccid.hocreader.presentation.BasePresenter;
import com.eaccid.hocreader.underdevelopment.ReaderExceptionHandlerImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BookPresenter implements BasePresenter<BookFragment> {
    private final String logTAG = "BookPresenter";
    private BookFragment mView;
    private final List<Page<String>> mPagesList;

    @Inject
    BookInteractor bookInteractor;

    public BookPresenter() {
        mPagesList = new ArrayList<>();
        App.getAppComponent().inject(this);
    }

    @Override
    public void attachView(BookFragment bookFragment) {
        mView = bookFragment;
        Log.i(logTAG, "BookFragment has been attached.");
    }

    @Override
    public void detachView() {
        Log.i(logTAG, "BookFragment has been detached.");
        new BookOnReadProvider().storeCurrentBooksPage(mView.getCurrentPosition());
        mView = null;
    }

    public void onViewCreated() {
        setDataToList();
        mView.setSelectableText(mView.isSelectableMode());
        int position = new BookOnReadProvider().loadCurrentBooksPage();
        mView.scrollToListPosition(position, 0);
    }

    private void setDataToList() {
        TxtPagesFromFileProvider txtPagesFromFileProvider = new TxtPagesFromFileProvider(
                new CharactersDefinerForFullScreenTextView(mView.getActivity())
        );
        BaseFileImpl baseFile = new BaseFileImpl(bookInteractor.getCurrentBookPath());
        txtPagesFromFileProvider.getPageObservable(baseFile)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<String>>() {
                    @Override
                    public void onCompleted() {
                        mView.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        new ReaderExceptionHandlerImpl().handleError(e);
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
        int currentPagePosition = bookInteractor.getCurrentPage() - 1;
        int fromPage = Integer.parseInt(input.toString()) - 1;
        mView.scrollToListPosition(fromPage, currentPagePosition);
        mView.showSnackbarBackToPage(currentPagePosition, fromPage);
    }

    public void onSelectTextMenuClicked() {
        mView.setSelectableText(true);
    }

    public void onMoreMenuClicked() {
        if (mView.isSelectableMode()) {
            mView.setSelectableText(false);
            return;
        }
        mView.showMoreMenu();
    }

    public void onGoToPageClicked() {
        mView.showGoToPage();
    }


    public void onUndoClicked(int currentPage, int previousPage) {
        mView.scrollToListPosition(currentPage, previousPage);
    }

}
