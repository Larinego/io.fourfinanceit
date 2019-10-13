package io.fourfinanceit.util;

import java.math.BigDecimal;

public class ApplicationConstants {
    public final static Integer COUNT_OF_IP_LIMIT = 3;
    public final static Long HOURS_PERIOD_IP_LIMIT = 24l;
    public final static int START_TIME_RISKS = 0;
    public final static int END_TIME_RISKS = 8;
    public final static String STATUS_ACCEPTED = "ACCEPTED";
    public final static String STATUS_REJECTED = "REJECTED";
    public final static double INTEREST_RATE = 12.;
    public final static double EXTEND_INTEREST_RATE = 1.5;
    public final static BigDecimal MAX_POSSIBLE_AMOUNT = new BigDecimal(30);
}
