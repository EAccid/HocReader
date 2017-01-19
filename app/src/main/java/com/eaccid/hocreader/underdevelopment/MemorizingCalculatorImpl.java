package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.provider.db.WordItem;

import java.util.Random;

public class MemorizingCalculatorImpl implements MemorizingCalculator {

    public MemorizingCalculatorImpl(WordItem wordProvider) {

    }

    @Override
    public int getLevel() {
        return new Random().nextInt(2);
    }

}
