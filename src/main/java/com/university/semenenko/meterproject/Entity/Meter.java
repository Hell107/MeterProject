package com.university.semenenko.meterproject.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Meter {
    private int number;

    private int valueBefore;
    private int valueAfter;
}
