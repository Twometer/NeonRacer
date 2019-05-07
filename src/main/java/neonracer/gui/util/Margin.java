package neonracer.gui.util;

import neonracer.gui.annotation.ParserMethod;

import java.util.Arrays;

public class Margin {

    private int left;

    private int right;

    private int top;

    private int bottom;

    public Margin() {
        this(0, 0, 0, 0);
    }

    private Margin(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @ParserMethod
    public static Margin fromString(String str) {
        int[] i = Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray();
        if (i.length == 1)
            return new Margin(i[0], i[0], i[0], i[0]);
        else if (i.length == 2)
            return new Margin(i[0], i[0], i[1], i[1]);
        else if (i.length == 4)
            return new Margin(i[0], i[1], i[2], i[3]);
        else throw new IllegalArgumentException("Invalid margin string: " + str);
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    private int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    private int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getHeight() {
        return getTop() + getBottom();
    }

    public int getWidth() {
        return getLeft() + getRight();
    }
}
