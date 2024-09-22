package org.bitcoin.bll;

import org.bitcoin.bll.model.Transformer;
import org.bitcoin.storage.entity.PriceEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PriceMergeUtils {
    public static List<PriceEntity> mergePrices(List<PriceEntity> inputEntities, PriceRangeType rangeType) {
        // If rangeType is DAY, nothing to do
        if (PriceRangeType.DAY == rangeType) {
            return inputEntities;
        } else if (rangeType == null) {
            throw new IllegalArgumentException("PriceRangeType cannot be null");
        }

        if (inputEntities == null) {
            throw new IllegalArgumentException("PriceEntity list cannot be null");
        }

        // Copy the list so that we can modify it
        inputEntities = new ArrayList<>(inputEntities);

        List<PriceEntity> resultEntities = new ArrayList<>();
        if (inputEntities.isEmpty()) {
            return resultEntities;
        }

        PriceEntity groupEntity = initializeGroup(inputEntities.remove(0), rangeType);
        for (PriceEntity inputEntity : inputEntities) {
            if (isSameGroup(groupEntity, inputEntity, rangeType)) {
                // Combine these price entries
                groupEntity = combineEntities(groupEntity, inputEntity);
            } else {
                // Add the previous group and start a new one
                resultEntities.add(groupEntity);
                groupEntity = initializeGroup(inputEntity, rangeType);
            }
        }

        // Add the last group
        resultEntities.add(groupEntity);

        return resultEntities;
    }

    private static PriceEntity initializeGroup(PriceEntity entity, PriceRangeType rangeType) {
        LocalDate originalDate = entity.getDate();

        // Preserve the year
        int year = originalDate.getYear();

        // For year groupings, set the month to January
        // Otherwise, preserve the month
        Month month = originalDate.getMonth();
        if (PriceRangeType.YEAR == rangeType) {
            month = Month.JANUARY;
        }

        // Always set the day to 1
        int day = 1;

        // Change the data so that it starts on the first day of the group range
        LocalDate groupDate = LocalDate.of(year, month, day);
        String groupDateStr = Transformer.dateToStr(groupDate);

        return new PriceEntity(groupDateStr, groupDate, entity.getOpen(), entity.getHigh(), entity.getLow(), entity.getClose());
    }

    private static boolean isSameGroup(PriceEntity entity1, PriceEntity entity2, PriceRangeType rangeType) {
        LocalDate date1 = entity1.getDate();
        LocalDate date2 = entity2.getDate();

        boolean sameMonth = date1.getMonth() == date2.getMonth();
        boolean sameYear = date1.getYear() == date2.getYear();

        if (PriceRangeType.MONTH == rangeType) {
            return sameMonth && sameYear;
        } else if (PriceRangeType.YEAR == rangeType) {
            return sameYear;
        }

        throw new IllegalArgumentException("Unsupported PriceRangeType " + rangeType);
    }

    private static PriceEntity combineEntities(PriceEntity groupEntity, PriceEntity otherEntity) {
        String maxHighStr = getMaxHigh(groupEntity.getHigh(), otherEntity.getHigh());
        String minLowStr = getMinLow(groupEntity.getLow(), otherEntity.getLow());

        return new PriceEntity(
                // Preserve the dates from the first entity
                groupEntity.getDateStr(),
                groupEntity.getDate(),
                // Preserve the open from the first entity
                groupEntity.getOpen(),
                // Use the largest value for high
                maxHighStr,
                // Use the smallest value for low
                minLowStr,
                // Always use the last close
                otherEntity.getClose()
        );
    }

    private static String getMinLow(String low1Str, String low2Str) {
        // Convert to doubles
        double low1 = Double.parseDouble(low1Str);
        double low2 = Double.parseDouble(low2Str);

        // Return the min
        return String.valueOf(Math.min(low1, low2));
    }

    private static String getMaxHigh(String high1Str, String high2Str) {
        // Convert to doubles
        double high1 = Double.parseDouble(high1Str);
        double high2 = Double.parseDouble(high2Str);

        // Return the max
        return String.valueOf(Math.max(high1, high2));
    }
}
