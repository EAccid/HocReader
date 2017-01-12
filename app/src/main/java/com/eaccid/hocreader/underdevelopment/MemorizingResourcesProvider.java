package com.eaccid.hocreader.underdevelopment;

import android.support.annotation.DrawableRes;

import com.eaccid.hocreader.R;

public class MemorizingResourcesProvider {

    public
    @DrawableRes
    int getLearnByHeartResId(boolean isSetToLearn) {
        if (isSetToLearn) {
            return R.drawable.ic_learn_by_heart_border_gray_18px;
        } else {
            return R.drawable.ic_learn_by_heart_gray_18px;
        }
    }

    public
    @DrawableRes
    int getAlreadyLearnedWordResId(MemorizingCalculator memorizingCalculator) {
        int result = MemorizingCalculator.LOW;
        switch (memorizingCalculator.getLevel()) {
            case MemorizingCalculator.LOW:
                result = R.drawable.ic_star_border_black_18px;
                break;
            case MemorizingCalculator.MEDIUM:
                result = R.drawable.ic_star_half_black_18px;
                break;
            case MemorizingCalculator.HIGH:
                result = R.drawable.ic_star_black_18px;
                break;
        }

        return result;
    }
}
