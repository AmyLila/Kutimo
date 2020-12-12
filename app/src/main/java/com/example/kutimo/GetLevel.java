package com.example.kutimo;

public class GetLevel {
    /**
     *
     * @param FaithPoints pass in total faith points
     * @return index based on Constants.LEVELS
     */
    private static int getLevelIndex(float FaithPoints) {
        int[] levels = Constants.LEVELS;

        int next_level_index = 0;
        for (int each : levels)
            if (FaithPoints > each)
                next_level_index++;

        // returns index and if out of range, return last index
        return next_level_index > levels.length ? levels.length - 1 : next_level_index;
    }

    /**
     * Needs a zero in Constants.LEVELS at first index, otherwise you must add 1 to it.
     * @param faith_points pass in total faith_points
     * @return currentLevelIndex by total faith_points based on Constants.LEVELS
     */
    public static int getCurrentLevel(float faith_points) {
        return getLevelIndex(faith_points);
    }

    /**
     * Returns a next max levelUpPoints based on where faith_points lies in Constants.LEVELS
     * @param faith_points pass in total faith points
     * @return levelUpPoints value from Constants.LEVELS
     */
    public static float getLevelUpPoints(float faith_points) {
        return Constants.LEVELS[getLevelIndex(faith_points)];
    }

    /**
     * The faith_points is changed into percentage in between previous level index (0%) and current
     * level index (100%) from Constants.LEVELS
     * @param faith_points pass in total faith points
     * @return the percentage between previous level number and current level number
     */
    public static float getPercent(float faith_points) {
        int index = getLevelIndex(faith_points);
        float number = Constants.LEVELS[index];
        float previous_number = Constants.LEVELS[Math.max(index - 1, 0)];

        float divisor = number - previous_number == 0 ? 1 : number - previous_number;
        return (faith_points - previous_number) * 100 / divisor;
    }
}
