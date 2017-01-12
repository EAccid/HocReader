package com.eaccid.hocreader.presentation.fragment.weditor;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eaccid.hocreader.R;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TextTranslation;
import com.eaccid.hocreader.data.remote.libtranslator.translator.TranslatorRx;
import com.eaccid.hocreader.injection.App;
import com.eaccid.hocreader.presentation.fragment.translation.semantic.ImageViewManager;
import com.eaccid.hocreader.provider.db.WordProvider;
import com.eaccid.hocreader.provider.db.WordListInteractor;
import com.eaccid.hocreader.provider.db.listprovider.ItemDataProvider;
import com.eaccid.hocreader.provider.translator.HocTranslatorProvider;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * advanced recycler view library example
 */

public class SwipeOnLongPressRecyclerViewAdapter
        extends RecyclerView.Adapter<SwipeOnLongPressRecyclerViewAdapter.WordTranslationViewHolder>
        implements SwipeableItemAdapter<SwipeOnLongPressRecyclerViewAdapter.WordTranslationViewHolder> {
    private final String LOG_TAG = "SwipeOnLongRVAdapter";
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;
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
        App.getWordListComponent().inject(this);
        mItemViewOnClickListener = this::onItemViewClick;
        mSwipeableViewContainerOnClickListener = this::onSwipeableViewContainerClick;
        setHasStableIds(true);// have to implement the getItemId() method
    }

    static class WordTranslationViewHolder extends AbstractSwipeableItemViewHolder {
        @BindView(R.id.container)
        FrameLayout container;
        @BindView(R.id.word)
        TextView word;
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

        WordTranslationViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public View getSwipeableContainerView() {
            return container;
        }
    }

    @Override
    public WordTranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.editor_word_item_fragment_1, parent, false);
        return new WordTranslationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WordTranslationViewHolder holder, int position) {
        final WordProvider item = (WordProvider) wordListInteractor.getItem(position);
        // if the item is 'pinned', click event comes to the itemView
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // if the item is 'not pinned', click event comes to the container
        holder.container.setOnClickListener(mSwipeableViewContainerOnClickListener);


        StringBuilder sb = new StringBuilder(item.getName());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        holder.word.setText(sb.toString());

        holder.translation.setText(item.getTranslation());

        //TODO: delete TEMP DATA + wrap ExpandableTextView

        holder.context.setText(item.getContext());
//        String temp = "I clasp the flask between my hands even though the warmth from the tea has long since leached into the frozen air. My muscles are clenched tight against the cold. If a pack of wild dogs were to appear at this moment, the odds of scaling a tree before they attacked are not in my favor.  I should get up, move around, and work the stiffness from my limbs. But instead I sit, as motionless as the rock beneath me, while the dawn begins to lighten the woods. I can't fight the sun. I can only watch helplessly as it drags me into a day that I've been dreading for months. By noon they will all be at my new house in the Victor's Village. ";
//        holder.context.setText(temp);
        TranslatorRx translator = (TranslatorRx) new HocTranslatorProvider();
        translator.translate(item.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TextTranslation>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        unsubscribe();
                    }

                    @Override
                    public void onNext(TextTranslation textTranslation) {
                        {
                            ImageViewManager imageViewManager = new ImageViewManager(holder.context.getContext());
                            imageViewManager.loadPictureFromUrl((ImageView) holder.wordImage, textTranslation.getPicUrl());
                        }
                    }
                })
        ;

        // set background resource (target view ID: container)
        final int swipeState = holder.getSwipeStateFlags();
        int bgResId;
        if (item.isLastAdded()) {
            bgResId = R.drawable.bg_item_session_state;
        } else {
            bgResId = R.drawable.bg_item_normal_state;
        }
        /**
         if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
         if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
         bgResId = R.drawable.bg_item_session_state;
         } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
         bgResId = R.drawable.bg_item_normal_state;
         }
         }
         */
        holder.container.setBackgroundResource(bgResId);
        holder.setSwipeItemHorizontalSlideAmount(
                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public long getItemId(int position) {
        return wordListInteractor.getItem(position).getItemId();
    }

    @Override
    public int getItemCount() {
        return wordListInteractor.getCount();
    }

    @Override
    public int onGetSwipeReactionType(WordTranslationViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_LEFT | Swipeable.REACTION_MASK_START_SWIPE_LEFT |
                Swipeable.REACTION_CAN_SWIPE_RIGHT | Swipeable.REACTION_MASK_START_SWIPE_RIGHT |
                Swipeable.REACTION_START_SWIPE_ON_LONG_PRESS;
    }

    @Override
    public void onSetSwipeBackground(WordTranslationViewHolder holder, int position, int type) {
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
    public SwipeResultAction onSwipeItem(WordTranslationViewHolder holder, final int position, int result) {
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
}
