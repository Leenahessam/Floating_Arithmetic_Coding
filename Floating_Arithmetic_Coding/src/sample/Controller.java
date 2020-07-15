package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Controller {

    public static TreeMap<Character, Double> probabilities = new TreeMap<>();
    private double sum = 0;

    @FXML
    private TextField inputText;
    @FXML
    private TextField outputText;
    @FXML
    private TextField character;
    @FXML
    private TextField probability;
    @FXML
    private TextField length;
    @FXML
    private TextField inputFile;
    @FXML
    private TextField outputFile;
    @FXML
    private Label labelIn;
    @FXML
    private Label labelOut;
    @FXML
    private Button compress;
    @FXML
    private Button decompress;
    @FXML
    private Button files;
    @FXML
    private Button addChar;


    public void clear(){
        inputText.clear();
        outputText.clear();
        character.clear();
        probability.clear();
        length.clear();
        inputFile.clear();
        outputFile.clear();

        inputFile.setVisible(false);
        outputFile.setVisible(false);
        labelOut.setVisible(false);
        labelIn.setVisible(false);

        addChar.setDisable(true);
        compress.setDisable(true);
        decompress.setDisable(true);

        probabilities.clear();
    }


    public void enableAdd(){
        if(character.getText().isEmpty() || probability.getText().isEmpty())
            addChar.setDisable(true);
        else
            addChar.setDisable(false);
    }


    public void enableCompress(){
        if(inputText.getText().isEmpty() || (sum != 0 && sum !=1))
            compress.setDisable(true);
        else
            compress.setDisable(false);
    }


    public void enableDecompress(){
        if(inputText.getText().matches("[01]+") && !length.getText().isEmpty() && sum == 1)
            decompress.setDisable(false);
        else
            decompress.setDisable(true);
    }


    public void addChar(){
        Character symbol = character.getText().charAt(0);
        double prob = Double.parseDouble(probability.getText());
        probabilities.put(symbol, prob);
        sum = 0;
        for(Character s : probabilities.keySet())
            sum += probabilities.get(s);
        character.clear();
        probability.clear();
        enableCompress();
        enableDecompress();
    }


    public void Compress(){
        if(!inputFile.getText().isEmpty()){
            File inFile = new File(inputFile.getText());
            if(inFile.exists()){
                Scanner scanner;
                try {
                    scanner = new Scanner(inFile);
                    String sentence = scanner.nextLine();
                    inputText.setText(sentence);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                outputText.setText("Input file doesn't exist..");
                return;
            }
        }

        if(probabilities.size() == 0)
            Main.symbols = Main.getRange(inputText.getText());
        else
            Main.symbols = Main.getRange(probabilities);

        String result = Main.Compression(inputText.getText());
        outputText.setText("Binary code: " + result);

        if(!outputFile.getText().isEmpty()){
            File outFile = new File(outputFile.getText());
            try {
                FileOutputStream fos = new FileOutputStream(outFile, false);
                fos.write((result + "\n").getBytes());
                fos.write((inputText.getText().length() + "\n").getBytes());
                int counter = 0;
                for(Character s : probabilities.keySet()){
                    if(counter == probabilities.size()-1)
                        fos.write((s + "|" + probabilities.get(s).toString()).getBytes());
                    else
                        fos.write((s + "|" + probabilities.get(s).toString() + "|").getBytes());
                    counter++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Main.symbols.clear();
        probabilities.clear();
    }


    public void Decompress(){

        if(!inputFile.getText().isEmpty()){
            File inFile = new File(inputFile.getText());
            ArrayList<String> lines = new ArrayList<>();
            if(inFile.exists()){
                Scanner scanner;
                try {
                    scanner = new Scanner(inFile);
                    while (scanner.hasNextLine())
                        lines.add(scanner.nextLine());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                outputText.setText("Input file doesn't exist..");
                return;
            }

            inputText.setText(lines.get(0));
            length.setText(lines.get(1));
            String[] s = lines.get(2).split("\\|");
            for (int i = 0; i < s.length; i+=2)
                probabilities.put(s[i].charAt(0), Double.parseDouble(s[i+1]));
        }


        Main.symbols = Main.getRange(probabilities);
        String result = Main.decompress(inputText.getText(), Integer.parseInt(length.getText()));
        outputText.setText("Original text: " + result);

        if(!outputFile.getText().isEmpty()){
            File outFile = new File(outputFile.getText());
            try {
                FileOutputStream fos = new FileOutputStream(outFile, false);
                fos.write((result).getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Main.symbols.clear();
        probabilities.clear();
        decompress.setDisable(true);
    }


    public void Files(){
        labelIn.setVisible(true);
        labelOut.setVisible(true);
        inputFile.setVisible(true);
        outputFile.setVisible(true);
        compress.setDisable(false);
        decompress.setDisable(false);
    }
}
