package com.eaccid.hocreader.presentation.weditor.adapter;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.App;
import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;
import com.eaccid.hocreader.provider.semantic.MediaPlayerManager;
import com.eaccid.hocreader.provider.db.words.WordItemImpl;
import com.eaccid.hocreader.provider.db.words.WordListInteractor;
import com.eaccid.hocreader.provider.db.words.listprovider.ItemDataProvider;
import com.eaccid.hocreader.underdevelopment.MemorizingCalculatorImpl;
import com.eaccid.hocreader.exceptions.ReaderExceptionHandlerImpl;
import com.eaccid.hocreader.presentation.weditor.IconTogglesResourcesProvider;
import com.eaccid.hocreader.underdevelopment.UnderDevelopment;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class SwipeOnLongPressRecyclerViewAdapter
        extends RecyclerView.Adapter<SwipeOnLongPressRecyclerViewAdapter.WordsEditorViewHolder>
        implements SwipeableItemAdapter<SwipeOnLongPressRecyclerViewAdapter.WordsEditorViewHolder> {
    private final String LOG_TAG = "OnLongPressRVAdapter";
    private EventListener mEventListener;
    private final View.OnClickListener mItemViewOnClickListener;
    private final View.OnClickListener mSwipeableViewContainerOnClickListener;
    private SparseBooleanArray mSelectedItemsIds;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    WordListInteractor wordListInteractor;

    private interface Swipeable extends SwipeableItemConstants {
    }

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);
    }

    public SwipeOnLongPressRecyclerViewAdapter() {
        mItemViewOnClickListener = this::onItemViewClick;
        mSwipeableViewContainerOnClickListener = this::onSwipeableViewContainerClick;
        setHasStableIds(true);// have to implement the getItemId() method
        mSelectedItemsIds = new SparseBooleanArray();
    }

    /**
     * Work with view holder
     */

    static class WordsEditorViewHolder extends AbstractSwipeableItemViewHolder {
        @BindView(R.id.container)
        FrameLayout container;
        @BindView(R.id.word)
        TextView word;
        @BindView(R.id.word_transcription)
        TextView transcription;
        @BindView(R.id.translation)
        TextView translation;
        @BindView(R.id.expand_text_view)
        ExpandableTextView context;
        @BindView(R.id.word_image)
        ImageView wordImage;
        @BindView(R.id.show_in_page)
        ImageView showInPage;
        @BindView(R.id.learn_by_heart_false)
        ImageView learnByHeart;
        @BindView(R.id.already_learned)
        ImageView alreadyLearned;
        @BindView(R.id.transcription_speaker)
        ImageView transcriptionSpeaker;
        MediaPlayer mediaPlayer;
        boolean isSetToLearn;
        Subscription subscription;

        WordsEditorViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        App.get(recyclerView.getContext())
                .getWordListComponent()
                .inject(this);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        compositeSubscription.unsubscribe();
    }

    @Override
    public WordsEditorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.editor_word_item_fragment_1, parent, false);
        return new WordsEditorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordsEditorViewHolder holder, int position) {
        setDataToViewFromItem(holder, position);
        setListenersToViewFromItem(holder, position);
        setViewHoldersContainerBackGround(holder, position);
        holder.setSwipeItemHorizontalSlideAmount(
                getWordListItemProvider(position).isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0
        );
    }

    private void setDataToViewFromItem(WordsEditorViewHolder holder, int position) {
        if (holder.subscription != null && !holder.subscription.isUnsubscribed())
            compositeSubscription.remove(holder.subscription);
        holder.subscription = wordListInteractor
                .getWordItem(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordItem -> {
                    holder.word.setText(wordItem.getWordFromText());
                    holder.translation.setText(wordItem.getTranslation());
                    SparseBooleanArray collapsedPositions = new SparseBooleanArray();
                    collapsedPositions.put(0, true);
                    holder.context.setText(wordItem.getContext(), collapsedPositions, 0);
                    new ImageViewLoader().loadPictureFromUrl(
                            holder.wordImage,
                            wordItem.getPictureUrl(),
                            R.drawable.empty_picture_background,
                            R.drawable.empty_picture_background,
                            new NetworkAvailablenessImpl(holder.itemView.getContext()).isNetworkAvailable()
                    );
                    if (holder.mediaPlayer != null) //delete, after todo release method in MediaPlayerManager
                        holder.mediaPlayer.release();
                    holder.mediaPlayer = new MediaPlayerManager().createAndPreparePlayerFromURL(wordItem.getSoundUrl());
                    holder.transcription.setText("[" + wordItem.getTranscription() + "]");
                    holder.alreadyLearned.setImageResource(
                            new IconTogglesResourcesProvider().getAlreadyLearnedWordResId(
                                    new MemorizingCalculatorImpl(wordItem)
                            )
                    );
                    holder.learnByHeart.setImageResource(
                            new IconTogglesResourcesProvider().getLearnByHeartResId(
                                    getWordListItemProvider(position).isSetToLearn()
                            )
                    );
                    //Temp:
                    holder.isSetToLearn = getWordListItemProvider(position).isSetToLearn();
                }, e -> {
                    new ReaderExceptionHandlerImpl().handleError(e);
                });
        compositeSubscription.add(holder.subscription);
        Log.i(LOG_TAG, "Setting data to view from item: position " + position);
    }

    private void setListenersToViewFromItem(WordsEditorViewHolder holder, int position) {
        // if the item is 'pinned', click event comes to the itemView
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // if the item is 'not pinned', click event comes to the container
        holder.container.setOnClickListener(mSwipeableViewContainerOnClickListener);
        holder.transcriptionSpeaker.setOnClickListener(
                speaker -> {
                    if (holder.mediaPlayer == null) return;
                    showSpeaker(holder.transcriptionSpeaker, true);
                    holder.mediaPlayer.setOnCompletionListener(mp ->
                            showSpeaker(holder.transcriptionSpeaker, false));
                    new MediaPlayerManager().play(holder.mediaPlayer);
                }
        );
        holder.showInPage.setOnClickListener(
                new OnShowWordInBookPageClickListener(getWordListItemProvider(position))
        );
        holder.learnByHeart.setOnClickListener(v -> {
            holder.isSetToLearn = !holder.isSetToLearn;
            holder.learnByHeart.setImageResource(
                    new IconTogglesResourcesProvider().getLearnByHeartResId(
                            holder.isSetToLearn //getWordListItemProvider(position).isSetToLearn()
                    )
            );
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
        holder.alreadyLearned.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }

    private void setViewHoldersContainerBackGround(WordsEditorViewHolder holder, int position) {
        final int swipeState = holder.getSwipeStateFlags();
        int bgResId;
        if (mSelectedItemsIds.get(position)) {
            bgResId = R.drawable.bg_item_selected_state;
        } else if (getWordListItemProvider(position).isLastAdded()) {
            bgResId = R.drawable.bg_item_session_state;
        } else {
            bgResId = R.drawable.bg_item_normal_state;
        }
        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
            if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            }
        }
        holder.container.setBackgroundResource(bgResId);
    }

    private WordItemImpl getWordListItemProvider(final int position) {
        return (WordItemImpl) wordListInteractor.getItem(position);
    }

    /**
     * by advanced recycler view library example
     */

    @Override
    public long getItemId(int position) {
        return wordListInteractor.getItem(position).getItemId();
    }

    @Override
    public int getItemCount() {
        return wordListInteractor.getCount();
    }

    @Override
    public int onGetSwipeReactionType(WordsEditorViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_LEFT | Swipeable.REACTION_MASK_START_SWIPE_LEFT |
                Swipeable.REACTION_CAN_SWIPE_RIGHT | Swipeable.REACTION_MASK_START_SWIPE_RIGHT |
                Swipeable.REACTION_START_SWIPE_ON_LONG_PRESS;
    }

    @Override
    public void onSetSwipeBackground(WordsEditorViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(WordsEditorViewHolder holder, final int position, int result) {
        Log.d(LOG_TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

        switch (result) {
            case Swipeable.RESULT_SWIPED_RIGHT:
                if (wordListInteractor.getItem(position).isPinned()) {
                    return new UnpinResultAction(this, position);
                } else {
                    return new SwipeRightResultAction(this, position);
                }
            case Swipeable.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true); //true -> isPinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false -> isPinned
        }
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private SwipeOnLongPressRecyclerViewAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

        SwipeLeftResultAction(SwipeOnLongPressRecyclerViewAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            ItemDataProvider item = mAdapter.wordListInteractor.getItem(mPosition);
            if (!item.isPinned()) {
                item.setPinned(true);
                mAdapter.notifyItemChanged(mPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemPinned(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class SwipeRightResultAction extends SwipeResultActionRemoveItem {
        private SwipeOnLongPressRecyclerViewAdapter mAdapter;
        private final int mPosition;

        SwipeRightResultAction(SwipeOnLongPressRecyclerViewAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            mAdapter.wordListInteractor.removeItem(mPosition);
            mAdapter.notifyItemRemoved(mPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemRemoved(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private SwipeOnLongPressRecyclerViewAdapter mAdapter;
        private final int mPosition;

        UnpinResultAction(SwipeOnLongPressRecyclerViewAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            ItemDataProvider item = mAdapter.wordListInteractor.getItem(mPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    /***
     * Methods required for do selections
     */

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyItemChanged(position);
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
