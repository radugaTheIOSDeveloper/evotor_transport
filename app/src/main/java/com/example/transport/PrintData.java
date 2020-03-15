package com.example.transport;

import ru.evotor.devices.commons.printer.printable.PrintableBarcode;

class PrintData {
    enum PrintType {TEXT, BARCODE, IMAGE}

    private PrintableBarcode.BarcodeType barType;
    private PrintType printType;
    private String data;

    public PrintData(PrintableBarcode printableBarcode, PrintType barcode, String data) {
    }

    public PrintData(PrintableBarcode.BarcodeType barType, PrintType printType, String data) {
        this.barType = barType;
        this.printType = printType;
        this.data = data;
    }

    public PrintableBarcode.BarcodeType getBarType() {
        return barType;
    }

    public void setBarType(PrintableBarcode.BarcodeType barType) {
        this.barType = barType;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
