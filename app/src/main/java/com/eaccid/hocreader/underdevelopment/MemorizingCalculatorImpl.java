package com.eaccid.hocreader.underdevelopment;

import com.eaccid.hocreader.provider.db.WordProviderImpl;

import java.util.Random;

public class MemorizingCalculatorImpl implements MemorizingCalculator {

    public MemorizingCalculatorImpl(WordProviderImpl wordProvider) {

    }

    @Override
    public int getLevel() {
        return new Random().nextInt(3);
    }

}
