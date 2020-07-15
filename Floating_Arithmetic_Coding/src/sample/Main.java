package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

public class Main extends Application {

    public static ArrayList<Symbol> symbols = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Floating Arithmetic Technique");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public static ArrayList<Symbol> getRange(TreeMap<Character, Double> map){
        double low = 0, high;
        for(Character s : map.keySet()){
            high = low + map.get(s);
            DecimalFormat format = new DecimalFormat("#0.00");
            low = Double.parseDouble(format.format(low));
            high = Double.parseDouble(format.format(high));
            symbols.add(new Symbol(s, low, high));
            low = high;
        }
        return symbols;
    }


    public static ArrayList<Symbol> getRange(String sentence){
        ArrayList<String> chars = new ArrayList<>();
        chars.addAll(Arrays.asList(sentence.split("")));
        Collections.sort(chars);
        double low = 0, high;
        for (int i = 0; i < chars.size();) {
            char s = chars.get(i).charAt(0);
            double prob = (double) (Collections.frequency(chars, s+"")) / sentence.length();
            high = low + prob;
            Controller.probabilities.put(s, prob);
            DecimalFormat format = new DecimalFormat("#0.0");
            low = Double.parseDouble(format.format(low));
            high = Double.parseDouble(format.format(high));
            symbols.add(new Symbol(s, low, high));
            low = high;
            while(chars.contains(s+""))
                chars.remove(s+"");
        }
        return symbols;
    }


    public static String generateBinaryFractionalCode(double low, double high){
        String binaryCode = "";
        double code = 0;
        while( !(low <= code && code <= high)){
            if(code < low)
                binaryCode += "1";
            else{
                binaryCode = binaryCode.substring(0, binaryCode.length()-1);
                binaryCode += "01";
                code -= 1/Math.pow(2, binaryCode.length()-1);
            }
            code += 1/Math.pow(2,binaryCode.length());
        }
        return binaryCode;
    }


    public static double convertToDouble(String s){
        String withoutPeriod = s.replace(".", "");
        double value = new BigInteger(withoutPeriod, 2).doubleValue();
        String binaryDivisor = "1" + s.split("\\.")[1].replace("1", "0");
        double divisor = new BigInteger(binaryDivisor, 2).doubleValue();
        return value / divisor;
    }


    public static String Compression(String sentence){

        double lower = 0, upper = 1, range = 1;
        for (Character s : sentence.toCharArray()) {
            for (int i = 0; i < symbols.size(); i++) {
                if(s == symbols.get(i).getSymbol()){
                    upper = lower + range * symbols.get(i).getHigh_range();
                    lower = lower + range * symbols.get(i).getLow_range();
                    range = upper - lower;
                    break;
                }
            }
        }

        String code = generateBinaryFractionalCode(lower, upper);
        return code;
    }


    public static Symbol findRange (double decimalCode) {
        Symbol s = new Symbol();
        for (int i = 0; i < symbols.size(); i++) {
            if (decimalCode > symbols.get(i).getLow_range() && decimalCode < symbols.get(i).getHigh_range()){
                s = new Symbol(symbols.get(i).getSymbol(),symbols.get(i).getLow_range(),symbols.get(i).getHigh_range());
                break;
            }
            else
                s = null;
        }
        return s;
    }


    public static String decompress(String binaryCode, int length){
        double decimalCode = convertToDouble("0." + binaryCode);
        String result = "";
        double low = 0, high = 1, range;
        double code = decimalCode;
        for (int i = 0; i < length; i++) {
            Symbol symbol = findRange(decimalCode);
            if(symbol != null) {
                result = result + symbol.getSymbol();
                range = high - low;
                high = low + range * symbol.getHigh_range();
                low = low + range * symbol.getLow_range();
                decimalCode = (code - low) / (high - low);
            }
        }
        return result;
    }
}
