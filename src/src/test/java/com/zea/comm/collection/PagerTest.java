package com.zea.comm.collection;

import junit.framework.TestCase;

import java.util.*;

public class PagerTest extends TestCase {
    private int size = 0;

    private int pageSize = 0;

    /**
     * double ratio test times
     */
    private int times;

    @Override
    public void setUp() {
        this.size = 100000;
        this.pageSize = 100;
        this.times = 5;
    }

    public void testPageDown() {
        for (int i = 0; i < times; i++) {
            int cnt = size * (1 << i);

            // generating data set
            List<Integer> integerList = getIntegerList(cnt);

            // paging
            int baseRowValue = cnt / 2;
            Pager<Integer> pager = new Pager<>(Integer::compareTo, Pager.PAGE_DIRECTION_DOWN, pageSize, baseRowValue);

            long startTime = System.currentTimeMillis();
            for (int value : integerList) {
                pager.add(value);
            }
            List<Integer> result = pager.collect(o -> o);
            long endTime = System.currentTimeMillis();

            assertEquals(pageSize, result.size());
            int prevValue = -1;
            for (int value : result) {
                assertTrue(value > baseRowValue);
                if (prevValue != -1) {
                    assertEquals(prevValue + 1, value);
                }

                prevValue = value;
            }

            assertEquals(cnt, pager.getTotalCnt());
            System.out.printf("testPageDown, cnt: %d, time: %dms%n", cnt, endTime - startTime);
        }
    }

    public void testPageUp() {
        for (int i = 0; i < times; i++) {
            int cnt = size * (1 << i);

            // generating data set
            List<Integer> integerList = getIntegerList(cnt);

            // paging
            int baseRowValue = cnt / 2;
            Pager<Integer> pager = new Pager<>(Integer::compareTo, Pager.PAGE_DIRECTION_UP, pageSize, baseRowValue);

            long startTime = System.currentTimeMillis();
            for (int value : integerList) {
                pager.add(value);
            }
            List<Integer> result = pager.collect(o -> o);
            long endTime = System.currentTimeMillis();

            assertEquals(pageSize, result.size());
            int prevValue = -1;
            for (int value : result) {
                assertTrue(value < baseRowValue);
                if (prevValue != -1) {
                    assertEquals(prevValue + 1, value);
                }

                prevValue = value;
            }

            assertEquals(cnt, pager.getTotalCnt());
            System.out.printf("testPageUp, cnt: %d, time: %dms%n", cnt, endTime - startTime);
        }
    }

    public void testPageDownWhenBaseRowValueIsNull() {
        for (int i = 0; i < times; i++) {
            int cnt = size * (1 << i);

            // generating data set
            List<Integer> integerList = getIntegerList(cnt);

            // paging
            Pager<Integer> pager = new Pager<>(Integer::compareTo, Pager.PAGE_DIRECTION_DOWN, pageSize, null);

            long startTime = System.currentTimeMillis();
            for (int value : integerList) {
                pager.add(value);
            }
            List<Integer> result = pager.collect(o -> o);
            long endTime = System.currentTimeMillis();

            assertEquals(pageSize, result.size());
            int prevValue = -1;
            for (int value : result) {
                assertTrue(value >= 0);
                if (prevValue != -1) {
                    assertEquals(prevValue + 1, value);
                }

                prevValue = value;
            }

            assertEquals(cnt, pager.getTotalCnt());
            System.out.printf("testPageDownWhenBaseValueIsNull, cnt: %d, time: %dms%n", cnt, endTime - startTime);
        }
    }

    public void testPageUpWhenBaseRowValueIsNull() {
        for (int i = 0; i < times; i++) {
            int cnt = size * (1 << i);

            // generating data set
            List<Integer> integerList = getIntegerList(cnt);

            // paging
            Pager<Integer> pager = new Pager<>(Integer::compareTo, Pager.PAGE_DIRECTION_UP, pageSize, null);

            long startTime = System.currentTimeMillis();
            for (int value : integerList) {
                pager.add(value);
            }
            List<Integer> result = pager.collect(o -> o);
            long endTime = System.currentTimeMillis();

            assertEquals(pageSize, result.size());
            int prevValue = -1;
            for (int value : result) {
                assertTrue(value >= 0);
                if (prevValue != -1) {
                    assertEquals(prevValue + 1, value);
                }

                prevValue = value;
            }

            assertEquals(cnt, pager.getTotalCnt());
            System.out.printf("testPageUpWhenBaseValueIsNull, cnt: %d, time: %dms%n", cnt, endTime - startTime);
        }
    }

    public void testUseTreeSet() {
        for (int i = 0; i < times; i++) {
            int cnt = size * (1 << i);

            List<Integer> dataList = getIntegerList(cnt);
            TreeSet<Integer> set = new TreeSet<>(Integer::compareTo);

            long startTime = System.currentTimeMillis();
            set.addAll(dataList);
            int baseRowValue = cnt / 2;
            NavigableSet<Integer> tailSet = set.tailSet(baseRowValue, false);
            Iterator<Integer> it = tailSet.iterator();
            List<Integer> result = new ArrayList<>();
            for (int j = 0; j < pageSize; j++) {
                if (it.hasNext()) {
                    result.add(it.next());
                } else {
                    break;
                }
            }
            long endTime = System.currentTimeMillis();

            assertEquals(pageSize, result.size());
            int prevValue = -1;
            for (int value : result) {
                assertTrue(value > baseRowValue);
                if (prevValue != -1) {
                    assertEquals(prevValue + 1, value);
                }

                prevValue = value;
            }

            assertEquals(cnt, set.size());
            System.out.printf("testUseTreeSet, cnt: %d, time: %dms%n", cnt, endTime - startTime);
        }
    }

    private List<Integer> getIntegerList(int size) {
        // generating data set
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            integerList.add(i);
        }

        // make data set random
        Random rand = new Random();
        for (int i = 0; i < size / 2; i++) {
            int index1 = rand.nextInt(integerList.size());
            int index2 = rand.nextInt(integerList.size());
            if (index1 != index2) {
                int tmp = integerList.get(index1);
                integerList.set(index1, integerList.get(index2));
                integerList.set(index2, tmp);
            }
        }

        return integerList;
    }

    @Override
    public void tearDown() {}
}
