package com.thewyp.lib.java;

import com.thewyp.lib.java.my.MyPrinterProxy;

public class MyTest {

    public static void main(String[] args) {

        PrinterProxy printerProxy = new PrinterProxy();
        IPrinter printer = (IPrinter) printerProxy.newProxyInstance(new PrinterImpl());
        printer.addMo();
        printer.addPagers();
        printer.print();

        MyPrinterProxy myPrinterProxy = new MyPrinterProxy();
        IPrinter myPrinter = (IPrinter) myPrinterProxy.newProxyInstance(new PrinterImpl());
        myPrinter.addMo();
        myPrinter.addPagers();
        myPrinter.print();

    }
}
