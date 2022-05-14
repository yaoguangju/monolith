package com.mochen.design.strategy.impl;

import com.mochen.design.strategy.Strategy;

public class OperationSubtract implements Strategy {
    @Override
    public int doOperation(int num1, int num2) {
        return num1 - num2;
    }
}
