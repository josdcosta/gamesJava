package br.com.sudoku.model;

public class Space {
    private boolean fixed;
    private Integer value;
    private int expected;

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if(this.fixed) return;
        this.value = value;
    }

    public void clearSpace(){
        this.value = null;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(Integer expected) {
        this.expected = expected;
    }
}
