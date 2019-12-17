package model.core;

import controller.tsp.TSP;
import controller.tsp.TSP1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TSP1Test {

    private TSP tsp1;
    private int[][] cost;
    private int[] duration;

    @BeforeEach
    void setUp() {
        tsp1 = new TSP1();
        cost = new int[3][3];
        cost[0][1] = cost[1][2] = cost[2][0] = 10;
        cost[1][0] = cost[2][1] = cost[0][2] = 5;
        duration = new int[3];
        duration[1] = duration[2] = 1;
    }

    @Nested
    class getTimeLimitExceeded {
        @Test
        void timeLimitNotExceeded() {
            assertFalse(() -> tsp1.getTimeLimitExceeded(), "getTimeLimitExceeded should return false when initialized");
        }

        @Test
        void timeLimitExceeded() {
            tsp1.searchSolution(0, 3, cost, duration);
            assertTrue(() -> tsp1.getTimeLimitExceeded(), "getTimeLimitExceeded should return true when time limit exceeded");
        }
    }

    @Nested
    class getBestSolution {
        @Test
        void nullSolution() {
            assertNull(tsp1.getBestSolution(0), "getBestSolution should return null when initialized");
        }

        @Test
        void indexNotInRange() {
            tsp1.searchSolution(Integer.MAX_VALUE, 3, cost, duration);
            assertNull(tsp1.getBestSolution(-1), "getBestSolution should return null when i is negative");
            assertNull(tsp1.getBestSolution(3), "getBestSolution should return null when i is greater than range");
        }

        @Test
        void indexInRange() {
            tsp1.searchSolution(Integer.MAX_VALUE, 3, cost, duration);
            assertEquals(2, tsp1.getBestSolution(1), "getBestSolution should return the node visited in i-th position");
        }
    }

    @Test
    void getLowestCost() {
        tsp1.searchSolution(Integer.MAX_VALUE, 3, cost, duration);
        assertEquals(17, tsp1.getLowestCost(), "getLowestCost should return the lowest cost");
    }
}