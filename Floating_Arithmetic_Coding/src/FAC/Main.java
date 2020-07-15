package FAC;

import java.text.DecimalFormat;
import java.util.*;

public class Main {

    public static ArrayList<Symbol> getRange(String sentence){
        ArrayList<Symbol> symbols = new ArrayList<>();
        ArrayList<String> chars = new ArrayList<>();
        chars.addAll(Arrays.asList(sentence.split("")));
        Collections.sort(chars);
        double low = 0, high;
        for (int i = 0; i < chars.size();) {
            char s = chars.get(i).charAt(0);
            high = low + (double) (Collections.frequency(chars, s+"")) / sentence.length();
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
        System.out.println("\nCompressed code: " + code);
        return binaryCode;
    }

    public static String Compression(String sentence){

        ArrayList<Symbol> symbols = getRange(sentence);
        for (Symbol symbol : symbols) {
            System.out.println(symbol);
        }
        System.out.println("\n");

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

    public static void main(String[] args) {
        String input = "BILL GATES";
        String output = Compression(input);
        System.out.println("Binary code: " + output);
    }
}
