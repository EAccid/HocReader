package com.eaccid.hocreader.temp.underdevelopment;

import com.eaccid.hocreader.temp.provider.db.words.WordItem;

import java.util.Random;

public class MemorizingCalculatorImpl implements MemorizingCalculator {

    public MemorizingCalculatorImpl(WordItem wordProvider) {

    }

    @Override
    public int getLevel() {
        return new Random().nextInt(2);
    }

}
