package com.thewyp.lib.java;

public class PrinterImpl implements IPrinter {
    @Override
    public void print() {
        System.out.println("打印.");
    }

    @Override
    public void addMo() {
        System.out.println("加油墨");
    }

    @Override
    public void addPagers() {
        System.out.println("加纸");
    }
}
