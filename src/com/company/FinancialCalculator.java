package com.company;

import com.sun.deploy.util.StringUtils;


import java.io.*;
import java.util.Properties;

public class FinancialCalculator {

    public static void main(String[] args) throws IOException, Exception {
        //TODO: сейчас принимаются данные только конкретного формата. Переделать, чтобы принимал любого формата
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String currency = reader.readLine();
        String[] enterCurrency = currency.split("[( | )]");

        String metod1 = enterCurrency[0];
        String metod2 = enterCurrency[1];
        String currency1 = enterCurrency[2];
        String action = enterCurrency[3].substring(0,1);
        String currency2 = enterCurrency[3].substring(1);

        Properties prop = new Properties();
        OutputStream output = null;
        InputStream input = null;

        double coefDollarToEuro = 0;
        double coefEuroToDollar = 0;

        try {

            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("DollarCoef", "0.8");
            prop.setProperty("EuroCoef", "1.5");

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value

             coefDollarToEuro = Double.parseDouble(prop.getProperty("DollarCoef"));
             coefEuroToDollar = Double.parseDouble(prop.getProperty("EuroCoef"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        output.close();
        input.close();

        Currency valuta1 = detectCurrency(currency1);
        Currency valuta2 = detectCurrency(currency2);

        Currency currencyConvert = detectMetod(metod2, valuta1, coefDollarToEuro, coefEuroToDollar);
        detectAction(action, currencyConvert, valuta2);
        Currency currencyResult = detectMetod(metod1, currencyConvert, coefDollarToEuro, coefEuroToDollar);
        String currencyResultFinal;
        if(currencyResult.getTypeCurrency()==ArrayCurrency.DOLLAR){
            if(currencyResult.getValueCurrency()<0){
                currencyResultFinal = "-$" + Math.abs(currencyResult.getValueCurrency());
            }
            else {
                currencyResultFinal = "$" + currencyResult.getValueCurrency();
            }
        }
        else {
            currencyResultFinal = currencyResult.getValueCurrency() + "eur";
        }
        System.out.println("Результат (если за 1 доллар дают " + coefDollarToEuro + " евро, а за 1 евро " + coefEuroToDollar + " доллара):\n" + currencyResultFinal);


    }

    public static void detectAction(String action, Currency a, Currency b) throws Exception{

        if(a.getTypeCurrency()==b.getTypeCurrency()){
            if(action.equals("+")){
              a.setValueCurrency(a.getValueCurrency() + b.getValueCurrency());
            }
            else {
                a.setValueCurrency(a.getValueCurrency() - b.getValueCurrency());
            }
        }
        else {
            System.out.println("Разные типы валют!");
            throw new Exception();
        }

    }

    public static Currency detectMetod (String metod, Currency a, double coefDollarToEuro, double coefEuroToDollar) throws Exception{

        Currency newCurrency;
        double currencyConvert = 0;
        if(metod.substring(2,3).equals("E")){
            if(!(a.getTypeCurrency()==ArrayCurrency.EURO)){
                currencyConvert = toEuro(a.getValueCurrency(), coefDollarToEuro);
            }
            else {
                System.out.println("нельзя конвертировать валюту в саму себя!");
                throw new Exception();
            }
            newCurrency = Currency.createEuro(currencyConvert);
        }
        else {
            if(!(a.getTypeCurrency()==ArrayCurrency.DOLLAR)){
                currencyConvert = toDollar(a.getValueCurrency(), coefEuroToDollar);
            }
            else {
                System.out.println("нельзя конвертировать валюту в саму себя!");
                throw new Exception();
            }
            newCurrency = Currency.createDollar(currencyConvert);
        }
        return newCurrency;
    }


    public static double toDollar(double euro, double coef){
        return euro*coef;
      //  coef - козффициент конвертации

    }

    public static double toEuro(double dollar, double coef){
        return dollar*coef;
       // coef - козффициент конвертации

    }

    public static Currency detectCurrency(String cur){
        Currency n;
        if(cur.substring(0,1).equals("-")){
            if(cur.substring(1,2).equals("$")){
                double dollar = Double.parseDouble(cur.substring(0,1) + cur.substring(2));
                n = Currency.createDollar(dollar);
            }
            else {
                double euro = Double.parseDouble(cur.substring(0,cur.length()-3));
                n = Currency.createEuro(euro);
            }
        }
        else {
            if(cur.substring(0,1).equals("$")){
                double dollar = Double.parseDouble(cur.substring(1));
                n = Currency.createDollar(dollar);
            }
            else {
                double euro = Double.parseDouble(cur.substring(0,cur.length()-3));
                n = Currency.createEuro(euro);
            }
        }

        return n;
    }
}
