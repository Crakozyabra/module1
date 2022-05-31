import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Автор - Голубев Иван Владимирович
 * email - mrcrakozyabra@gmail.com
 */


public class Solution extends Application{
    private static final List<Character> alphabet = Collections.unmodifiableList(getAlphabet());
    private static final String sourceFileDefault = "src"+File.separator+"sourcefile.txt";
    private static final String resultFileDefault = "src"+File.separator+"resultfile.txt";
    private static Path sourceFileDefaultPath = Path.of(sourceFileDefault).toAbsolutePath();
    private static Path resultFileDefaultPath = Path.of(resultFileDefault).toAbsolutePath();



    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }



    /**
     * GUI от JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Javarush. Module 1 (Syntax).");

        try {
            Files.createFile(sourceFileDefaultPath);
        } catch (Exception e) {}

        try {
            Files.createFile(resultFileDefaultPath);
        } catch (Exception e) {}

        // выбрать файлы для чтения и записи
        Label selectSourceAndResultFileLabel = new Label("Select source and result files:");
        selectSourceAndResultFileLabel.setStyle("-fx-font-weight: bold");

        Button selectionSourceFile = new Button("Select source file");
        Text sourceFilePath = new Text(sourceFileDefaultPath.toString());
        HBox sourceFileHorizontalBox = new HBox(selectionSourceFile,sourceFilePath);
        sourceFileHorizontalBox.setSpacing(5.0);

        Button selectionResultFile = new Button("Select result file");
        Text resultFilePath = new Text(resultFileDefaultPath.toString());
        HBox resultFileHorizontalBox = new HBox(selectionResultFile,resultFilePath);
        resultFileHorizontalBox.setSpacing(5.0);

        // выбрать режим программы, криптографические ключи и кнопка пуск
        Label sourceTextLabelGroup = new Label("Select mode, crypt keys and run:");
        sourceTextLabelGroup.setStyle("-fx-font-weight: bold");
        RadioButton radioButtonFirstMode = new RadioButton("First mode");
        RadioButton radioButtonSecondMode = new RadioButton("Second mode");

        TextField firstKeyFirstModeTextField = new TextField();
        firstKeyFirstModeTextField.setPromptText("enter one crypto key");
        TextField firstKeySecondModeTextField = new TextField();
        firstKeySecondModeTextField.setPromptText("enter crypto key1");
        firstKeySecondModeTextField.setDisable(true);
        TextField secondKeySecondModeTextField = new TextField();
        secondKeySecondModeTextField.setPromptText("enter crypto key2");
        secondKeySecondModeTextField.setDisable(true);

        HBox firstModeHorisontalBox = new HBox(radioButtonFirstMode,firstKeyFirstModeTextField);
        firstModeHorisontalBox.setSpacing(5.0);
        HBox secondModeHorisontalBox = new HBox(radioButtonSecondMode,firstKeySecondModeTextField,secondKeySecondModeTextField);
        secondModeHorisontalBox.setSpacing(5.0);
        ToggleGroup radioButtonGroup = new ToggleGroup();
        radioButtonFirstMode.setToggleGroup(radioButtonGroup);
        radioButtonSecondMode.setToggleGroup(radioButtonGroup);
        radioButtonFirstMode.setSelected(true);
        Button runModeButton = new Button("Run mode");
        Button infoButton = new Button("Show info");
        infoButton.setDisable(true);
        HBox runModeAndInfoButton = new HBox(runModeButton, infoButton);
        runModeAndInfoButton.setSpacing(5.0);

        // пользовательский ввод выравниваем по сеточке
        GridPane userInputGridPane = new GridPane();
        userInputGridPane.add(selectSourceAndResultFileLabel,0,0);
        userInputGridPane.add(sourceFileHorizontalBox,0,1);
        userInputGridPane.add(resultFileHorizontalBox,0,2);
        userInputGridPane.add(sourceTextLabelGroup,0,3);
        userInputGridPane.add(firstModeHorisontalBox,0,4);
        userInputGridPane.add(secondModeHorisontalBox,0,5);
        userInputGridPane.add(runModeAndInfoButton,0,6);
        userInputGridPane.setHgap(5);
        userInputGridPane.setVgap(5);
        userInputGridPane.setPadding(new Insets(10, 10, 10, 10));

        // исходное, результирующее поле вывода из текстовых файлов; поле вывода служебной информации
        Label sourceTextLabel = new Label("Text from source file after run mode:");
        TextArea sourceTextArea = new TextArea();
        sourceTextArea.setEditable(false);
        GridPane sourceTextGridPane = new GridPane();
        sourceTextGridPane.add(sourceTextLabel, 0,0);
        sourceTextGridPane.add(sourceTextArea, 0,1);

        Label resultTextLabel = new Label("Text from result file before run mode:");
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        GridPane resultTextGridPane = new GridPane();
        resultTextGridPane.add(resultTextLabel,0,0);
        resultTextGridPane.add(resultTextArea,0,1);

        Label serviceInformationTextLabel = new Label("Mode/runtime information:");
        TextArea serviceInformationTextArea = new TextArea();

        String startServiceInformation = "1. В первом режиме \"First mode\" программа берет текст из source file,\n" +
                "шифрует текст с ключом шифрования \"crypto key\" и записывает зашифрованный\nтекст в \"result file\".\n" +
                "2. Во втором режиме \"Second mode\" программа берет текст из \"source file\",\n" +
                "подбирает ключ из диапазона \"crypto key1\", \"crypto key2\" и записывает\nзашифрованный" +
                "текст в \"result file\".\n   П р и м е ч а н и е - Ключи могут быть больше, меньше или равными нулю.\n" +
                "\"crypto key1\" может быть больше, меньше или равен \"crypto key2\"\n" +
                "Если заданный ключ не может прочитаться из поля, он принимается равным нулю.";

        serviceInformationTextArea.setText(startServiceInformation);
        serviceInformationTextArea.setEditable(false);
        GridPane serviceInformationTextGridPane = new GridPane();
        serviceInformationTextGridPane.add(serviceInformationTextLabel,0,0);
        serviceInformationTextGridPane.add(serviceInformationTextArea,0,1);


        // все GUI выравниваем по сетке и устанавливаем промежутки между ними
        GridPane gridPaneAll = new GridPane();
        gridPaneAll.add(sourceTextGridPane, 0, 0);
        gridPaneAll.add(userInputGridPane, 1, 0);
        gridPaneAll.add(resultTextGridPane, 0, 1);
        gridPaneAll.add(serviceInformationTextGridPane, 1, 1);

        gridPaneAll.setHgap(10);
        gridPaneAll.setVgap(10);
        gridPaneAll.setPadding(new Insets(10, 10, 10, 10));


        // устанавливаем обработчики событий
        radioButtonFirstMode.setOnAction((e)->{
            firstKeyFirstModeTextField.setDisable(false);
            firstKeySecondModeTextField.setDisable(true);
            secondKeySecondModeTextField.setDisable(true);
        });
        
        
        radioButtonSecondMode.setOnAction((e)->{
            firstKeyFirstModeTextField.setDisable(true);
            firstKeySecondModeTextField.setDisable(false);
            secondKeySecondModeTextField.setDisable(false);
        });
        

        // обработчик запуска программы с выбранными параметрами
        runModeButton.setOnAction((e)->{
            infoButton.setDisable(false);
            RadioButton button = (RadioButton) radioButtonGroup.getSelectedToggle();
            String radioButtonText = button.getText();

            if(radioButtonText.equals("First mode")) {
                String sourceText = getTextFromFile(sourceFileDefaultPath);

                int key = 0;
                try {
                    key = Integer.parseInt(firstKeyFirstModeTextField.getText());
                } catch (NumberFormatException exeption) {
                    key = 0;
                }

                String resultText = cryptText(sourceText,key);
                putTextToFile(resultText,resultFileDefaultPath);
                sourceTextArea.setText(sourceText);
                resultTextArea.setText(resultText);
                serviceInformationTextArea.setText("Текст из файла "+ sourceFileDefaultPath +" зашифрован с ключом "+ key + " в файл "+ resultFileDefaultPath.toAbsolutePath() +".");
            }

            if(radioButtonText.equals("Second mode")) {
                String sourceText = getTextFromFile(sourceFileDefaultPath);
                int key1 = 0;
                int key2 = 0;
                try {
                    key1 = Integer.parseInt(firstKeySecondModeTextField.getText());
                } catch (NumberFormatException exeption) {
                    key1 = 0;
                }

                try {
                    key2 = Integer.parseInt(secondKeySecondModeTextField.getText());
                } catch (NumberFormatException exception) {
                    key2 = 0;
                }
                List<String> resultText = cryptTextWithRangeKey(sourceText,key1,key2);
                sourceTextArea.setText(sourceText);
                String result = resultText.get(0);
                if (result.equals("")) {
                    resultTextArea.setText("В файл запись не проводилась, потому что ключ не удалось подобрать в заданном диапазоне.");
                } else {
                    resultTextArea.setText(resultText.get(0));
                }
                putTextToFile(resultText.get(0), resultFileDefaultPath);
                serviceInformationTextArea.setText(resultText.get(1));
            }
        });


        // обработчик кнопки показа исходной информации в информационном поле
        infoButton.setOnAction((e)->{
            serviceInformationTextArea.setText(startServiceInformation);
        });


        // фильтр выбора и выбор файлов
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );


        selectionSourceFile.setOnAction(e -> {
            File selectedFileSource = fileChooser.showOpenDialog(primaryStage);
            if (selectedFileSource != null) {
                sourceFilePath.setText(selectedFileSource.getAbsolutePath());
                sourceFileDefaultPath = selectedFileSource.toPath();
            }
        });


        selectionResultFile.setOnAction(e -> {
            File selectedFileResult = fileChooser.showOpenDialog(primaryStage);
            if (selectedFileResult != null) {
                resultFilePath.setText(selectedFileResult.getAbsolutePath());
                resultFileDefaultPath = selectedFileResult.toPath();
            }
        });


        Scene scene = new Scene(gridPaneAll);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }





    private static int getCharPositionInAlphabetBeforeEncrypt(int startIndex, int cryptKey) {
        int size = alphabet.size();

        if ((cryptKey+startIndex) < 0){
            return size+((startIndex + cryptKey) % size);
        }

        return (startIndex + cryptKey) % size;

    }





    private static boolean hasLongWord(String text) {
        int defaultMaxLength = 30;
        StringTokenizer stringTokenizer = new StringTokenizer(text," ");

        while (stringTokenizer.hasMoreTokens()) {
            String str = stringTokenizer.nextToken();
            if (str.length()>defaultMaxLength)
                return true;
        }
        return false;
    }





    private static boolean hasComma(String text) {
        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ',') {
                return true;
            }
        }
        return false;
    }





    public static String getTextFromFile(Path path) {
        List<String> strings = new ArrayList<>();
        try {
            strings = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        for (String str:strings) {
            sb.append(str);
        }
        return sb.toString();
    }





    public static void putTextToFile(String text, Path path) {
        try {
            Files.writeString(path,text);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





    private static String cryptText(String afterEncrypt, int cryptKey) {
        StringBuilder beforeEncrypt = new StringBuilder();
        int size = alphabet.size();
        int j = 0;
        char[] chars = afterEncrypt.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (alphabet.contains(chars[i])) {
                j = getCharPositionInAlphabetBeforeEncrypt(alphabet.indexOf(chars[i]), cryptKey);
                beforeEncrypt.append(alphabet.get(j));
            }
        }
        return beforeEncrypt.toString();
    }





    private static List<String> cryptTextWithRangeKey(String afterEncrypt, int cryptKey1, int cryptKey2) {
        int max;
        int min;
        boolean keyExist = false;

        max = Math.max(cryptKey1,cryptKey2);
        min = Math.min(cryptKey1,cryptKey2);


        StringBuilder serviceInformation = new StringBuilder();
        List<String> workResult = new ArrayList<>(){{
            add("");
            add("");
        }};

        serviceInformation.append("Процесс расшифровки:\n");

        for (int i = min; i <= max; i++) {
            String cryptedText = cryptText(afterEncrypt, i);
            serviceInformation.append(i+"  "+cryptedText+"\n");
            if (!hasLongWord(cryptedText) && hasComma(cryptedText)) {
                serviceInformation.append("Подобраный ключ расшифровки: "+i);
                workResult.add(0,cryptedText);
                keyExist = true;
                break;
            }
        }
        if (!keyExist) serviceInformation.append("В заданном диапазоне ключей, ключ подобрать не удалось.");
        workResult.add(1,serviceInformation.toString());
        return workResult;
    }





    private static List<Character> getAlphabet(){
        ArrayList<Character> characters = new ArrayList<>(){{
            add(' '); //32
            add('!'); //33
            add('"'); //34
            add(','); //44
            add('-'); //45
            add('.'); //46
            add(':'); //58
            add('?'); //63
        }};

        // 1040 - 1071
        for (int i = 'А'; i <= 'Я'; i++) {
            characters.add((char) i);
        }

        // 1072 - 1103
        for (int i = 'а'; i <= 'я'; i++) {
            characters.add((char) i);
        }

        return characters;
    }

}