package com.sbowling.warmachine.war.batting;

import com.sbowling.warmachine.war.batting.impl.BattingAverage;
import com.sbowling.warmachine.war.batting.impl.OnBasePercentage;
import com.sbowling.warmachine.war.batting.impl.SluggingPercentage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BattingMetricRegistryTest {
    private BattingMetricRegistry registry = new BattingMetricRegistry(List.of(new BattingAverage(), new OnBasePercentage(), new SluggingPercentage()));

    @Test
    void findsBattingAverage() {
        BattingMetric metric = registry.getMetric("AVG");

        assertInstanceOf(BattingAverage.class, metric);
    }

    @Test
    void findsOnBasePercentage() {
        BattingMetric metric = registry.getMetric("OBP");

        assertInstanceOf(OnBasePercentage.class, metric);
    }

    @Test
    void findsSluggingPercentage() {
        BattingMetric metric = registry.getMetric("SLG");

        assertInstanceOf(SluggingPercentage.class, metric);
    }

    @Test
    void throwsForUnknownMetric() {
        assertThrows(
                IllegalArgumentException.class,
                () -> registry.getMetric("FAKE")
        );
    }
}
