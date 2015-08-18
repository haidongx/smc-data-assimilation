package edu.gsu.hxue.smc;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public interface GlobalConstants {
    Random RAND = new Random(12345);
    double DOUBLE_COMPARISON_THRESHOLD = 1e-7;
    MathContext BIG_DECIMAL_MATHCONTEXT = new MathContext(200, RoundingMode.HALF_UP);
}
