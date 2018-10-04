package com.company;

/**
 * Created by Admin on 19.06.2018.
 */
public class Currency {
    private ArrayCurrency  typeCurrency;
    private double valueCurrency;

    private Currency(double valueCurrency, ArrayCurrency typeCurrency){
        this.valueCurrency = valueCurrency;
        this.typeCurrency = typeCurrency;
    }

    public static Currency createDollar(double valueCurrency) {
        return new Currency(valueCurrency, ArrayCurrency.DOLLAR);
    }

    public static Currency createEuro (double valueCurrency) {
        return new Currency(valueCurrency, ArrayCurrency.EURO);
    }

    public ArrayCurrency getTypeCurrency() {
        return typeCurrency;
    }

    public void setTypeCurrency(ArrayCurrency typeCurrency) {
        this.typeCurrency = typeCurrency;
    }

    public double getValueCurrency() {
        return valueCurrency;
    }

    public void setValueCurrency(double valueCurrency) {
        this.valueCurrency = valueCurrency;
    }
}
