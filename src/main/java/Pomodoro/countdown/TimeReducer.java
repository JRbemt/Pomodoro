package Pomodoro.countdown;

import javafx.util.Duration;

public class TimeReducer extends Reducer{
    private final static int TIME_STEPS = TimeUnit.values().length;
    private int[] left = new int[TIME_STEPS];
    private int[] reduced = new int[TIME_STEPS];

    private final static int[] factor_to_secs = new int[TIME_STEPS];
    private final static int[] factor_to_lower_unit = new int[TIME_STEPS];
    {
        TimeUnit[] vals = TimeUnit.values();
        for (int i = 0; i < TIME_STEPS-1; i++){
            factor_to_secs[i] = vals[i].getFactorToSeconds();
            factor_to_lower_unit[i] = factor_to_secs[i];
            if (i > 0){
                factor_to_lower_unit[i-1] /= factor_to_lower_unit[i];
            }
        }
        factor_to_lower_unit[TIME_STEPS-1] = factor_to_secs[TIME_STEPS-1] = 1;
    }

    public void from(Duration duration){
        super.from((int) duration.toSeconds());
    }

    public TimeReducer reduce(TimeUnit unit){
        ticks += unit.getFactorToSeconds();
        update();
        return this;
    }

    public int[] getTime(){
        return reduced;
    }

    public String format(String delimiter, boolean ignore_zero){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < TIME_STEPS; i++) {
            if ((TIME_STEPS-i) > 2 && ignore_zero && reduced[i] == 0)
                continue;
            ignore_zero = false;
            builder.append(String.format("%02d", reduced[i]));
            if (i != TIME_STEPS-1)
                builder.append(delimiter);
        }
        return builder.toString();
    }

    @Override
    public void update() {
        super.update();
        for (int i = 0; i < TIME_STEPS; i++){
            left[i] = getDifference() / factor_to_secs[i];
            reduced[i] = i > 0 ? left[i] - left[i-1] * factor_to_lower_unit[i-1]
                    : left[i];
        }
    }

    public enum TimeUnit {
        HOURS(60*60),
        MINUTES(60),
        SECONDS(1);

        private final int factor_to_seconds;

        TimeUnit(int factor_to_seconds){
            this.factor_to_seconds = factor_to_seconds;
        }

        public int getFactorToSeconds() {
            return factor_to_seconds;
        }
    }
}
