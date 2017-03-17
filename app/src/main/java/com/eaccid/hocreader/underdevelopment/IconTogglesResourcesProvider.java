package com.eaccid.hocreader.underdevelopment;

import android.support.annotation.DrawableRes;

import com.eaccid.hocreader.R;

public class IconTogglesResourcesProvider implements IconTogglesResources {

    @Override
    @DrawableRes
    public int getLearnByHeartResId(boolean isSetToLearn) {
        if (isSetToLearn) {
            return R.drawable.ic_learn_by_heart_green_24px;
        } else {
            return R.drawable.ic_learn_by_heart_border_gray_24px;
        }
    }

    @Override
    @DrawableRes
    public int getAlreadyLearnedWordResId(MemorizingCalculator memorizingCalculator) {
        int result = MemorizingCalculator.LOW;
        switch (memorizingCalculator.getLevel()) {
            case MemorizingCalculator.LOW:
                result = R.drawable.ic_star_border_yellow_24px;
                break;
            case MemorizingCalculator.MEDIUM:
                result = R.drawable.ic_star_half_yellow_24px;
                break;
            case MemorizingCalculator.HIGH:
                result = R.drawable.ic_star_yellow_24px;
                break;
        }
        return result;
    }

    @Override
    @DrawableRes
    public int getSpeakerResId(boolean isSpeaking) {
        if (isSpeaking) {
            return R.drawable.ic_volume_up_accent_24px;
        } else {
            return R.drawable.ic_volume_up_gray_24px;
        }
    }

}
