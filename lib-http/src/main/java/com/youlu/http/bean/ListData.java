package com.youlu.http.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 18721
 */
public class ListData<T> {

    public List<T> list = new ArrayList<>();
    private Integer total;

    public int getTotal() {
        if (total != null) {
            return total;
        } else if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public boolean isEmpty() {
        return getTotal() <= 0;
    }

    public void assertEmpty() throws RuntimeException {
        if (getTotal() <= 0) {
            throw new RuntimeException("ListData is Empty");
        }
    }
}
