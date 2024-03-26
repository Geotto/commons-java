package com.zea.comm.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;

public class Pager<E> {
    /**
     * page down
     */
    public static final byte PAGE_DIRECTION_DOWN = 0;

    /**
     * page up
     */
    public static final byte PAGE_DIRECTION_UP = 1;

    /**
     * comparator
     */
    private final Comparator<E> comparator;

    /**
     * tree set that contains page result
     */
    private final TreeSet<E> set;

    /**
     * page direction
     */
    private final byte pageDirection;

    /**
     * pageSize
     */
    private final int pageSize;

    /**
     * base row value
     */
    private final E baseRowValue;

    /**
     * total count
     */
    private int totalCnt;

    public Pager(Comparator<E> comparator, byte pageDirection, int pageSize, E baseRowValue) {
        this.comparator = comparator;
        this.pageDirection = pageDirection;
        this.pageSize = pageSize;
        this.baseRowValue = baseRowValue;
        this.set = new TreeSet<>(comparator);
    }

    /**
     * add to page result.
     * this will increase total count
     * @param value value to add to page result
     */
    public void add(E value) {
        this.totalCnt++;

        if (this.baseRowValue == null) {
            this.set.add(value);
            if (this.set.size() > this.pageSize) {
                E last = this.set.last();
                this.set.remove(last);
            }
        } else if (this.pageDirection == PAGE_DIRECTION_DOWN) {
            if (this.comparator.compare(baseRowValue, value) < 0) {
                this.set.add(value);
                if (this.set.size() > this.pageSize) {
                    E last = this.set.last();
                    this.set.remove(last);
                }
            }
        } else if (this.pageDirection == PAGE_DIRECTION_UP) {
            if (this.comparator.compare(baseRowValue, value) > 0) {
                this.set.add(value);
                if (this.set.size() > this.pageSize) {
                    E first = this.set.first();
                    this.set.remove(first);
                }
            }
        }
    }

    /**
     * get total count
     * @return total count
     */
    public int getTotalCnt() {
        return this.totalCnt;
    }

    /**
     * collection page data
     * @param mapper method to mapping page data
     * @return page data as list
     * @param <K> output data type
     */
    public <K> List<K> collect(Function<E, K> mapper) {
        List<K> outputList = new ArrayList<>();
        for (E value : this.set) {
            K output = mapper.apply(value);
            outputList.add(output);
        }

        return outputList;
    }
}
