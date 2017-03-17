package com.eaccid.hocreader.underdevelopment;

import android.support.annotation.DrawableRes;

public interface IconTogglesResources {
    @DrawableRes
    int getLearnByHeartResId(boolean isSetToLearn);

    @DrawableRes
    int getAlreadyLearnedWordResId(MemorizingCalculator memorizingCalculator);

    @DrawableRes
    int getSpeakerResId(boolean isSpeaking);
}
