package com.handicrafts.dto;

import java.util.List;

public class DatatableDTO<T> {
    private List<T> items;
    private int recordsTotal;
    private int recordsFiltered;
    private int draw;

    public DatatableDTO() {
    }

    public DatatableDTO(List<T> items, int recordsTotal, int recordsFiltered, int draw) {
        this.items = items;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.draw = draw;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
}
