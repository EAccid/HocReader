package com.eaccid.hocreader.underdevelopment;

import android.util.SparseBooleanArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.eaccid.hocreader.provider.NetworkAvailablenessImpl;
import com.eaccid.hocreader.provider.db.words.WordItem;
import com.eaccid.hocreader.provider.semantic.ImageViewLoader;

public class WordViewHandler {

    public void loadDataToViewFromWordItem(final WordViewElements elements, final WordItem wordItem) {
        elements.word().setText(wordItem.getWordFromText());
        elements.translation().setText(wordItem.getTranslation());
        SparseBooleanArray collapsedPositions = new SparseBooleanArray();
        collapsedPositions.put(0, true);
        if (elements.context() != null)
            elements.context().setText(wordItem.getContext(), collapsedPositions, 0);
        elements.transcription().setText("[" + wordItem.getTranscription() + "]");
        new ImageViewLoader().loadPictureFromUrl(
                elements.wordImage(),
                wordItem.getPictureUrl(), elements.defaultImageResId(), elements.defaultImageResId(),
                new NetworkAvailablenessImpl(elements.container().getContext()).isNetworkAvailable()
        );
        elements.soundPlayer().preparePlayerFromSource(wordItem.getSoundUrl());
        elements.transcriptionSpeaker().setOnClickListener(
                speaker -> {
                    showSpeaker(elements.transcriptionSpeaker(), true);
                    elements.soundPlayer().play().subscribe(completed -> {
                        showSpeaker(elements.transcriptionSpeaker(), false);
                    });
                }
        );
        handleLearningToggles(elements, wordItem);
    }

    private Learning getLearningHandler() {
        return new LearningImpl();
    }

    private IconTogglesResources getIconTogglesResources() {
        return new IconTogglesResourcesProvider();
    }

    private void showSpeaker(ImageView iv, boolean isSpeaking) {
        iv.setImageResource(
                new IconTogglesResourcesProvider().getSpeakerResId(isSpeaking)
        );
    }

    private void handleLearningToggles(WordViewElements elements, WordItem wordItem) {
        final Learning learning = getLearningHandler();
        final IconTogglesResources togglesResources = getIconTogglesResources();
        elements.alreadyLearned().setImageResource(
                togglesResources.getAlreadyLearnedWordResId(
                        new MemorizingCalculatorImpl(wordItem)
                )
        );
        elements.alreadyLearned().setOnClickListener(v -> {
            learning.isAlreadyLearned(wordItem);
            Toast.makeText(elements.container().getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
        elements.learnByHeart().setImageResource(
                togglesResources.getLearnByHeartResId(
                        learning.isLearnByHart(wordItem)
                )
        );
        elements.learnByHeart().setOnClickListener(v -> {
            boolean learn = learning.isLearnByHart(wordItem);
            learning.setToLearn(wordItem, !learn);
            elements.learnByHeart().setImageResource(
                    togglesResources.getLearnByHeartResId(!learn)
            );
            Toast.makeText(elements.container().getContext(), UnderDevelopment.TEXT, Toast.LENGTH_SHORT).show();
        });
    }

}
