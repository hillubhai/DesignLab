package com.techsquad;

public class Medicine {
    private int id;
    private int qty;
    private String name;

    public Medicine(int id, int qty, String name) {
        this.id = id;
        this.qty = qty;
        this.name = name;
    }

    public Medicine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
