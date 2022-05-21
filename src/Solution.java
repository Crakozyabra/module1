import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Автор - Голубев Иван Владимирович
 * email - mrcrakozyabra@gmail.com
 */

public class Solution {
    private static final List<Character> alphabet = Collections.unmodifiableList(getAlphabet()); // алфавит для шифрования
    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in)); // поток чтения из консоли
    private static final String sourceFile = "src/sourcefile.txt"; // файл из которого берется текст



    public static void main(String[] args) throws IOException {

        printMenu();

        int menuPosition = getMenuPosition();

        if (menuPosition==1)
            firstMode();

        if (menuPosition==2)
            secondMode();


    }

    
    

    /**
     * Вычисляет позицию символа в алфавите после шифрования
     * @param startIndex стартовый индекс символа в алфавите
     * @return Возвращает индекс символа в алфавите после сдвига
     */
    private static int getCharPositionInAlphabetBeforeEncrypt(int startIndex, int cryptKey) {
        int size = alphabet.size(); // размер разрешенного алфавита

        if ((cryptKey+startIndex)<0){
            return size+((startIndex + cryptKey) % size);
        }

        return (startIndex + cryptKey) % size;

    }




    /**
     * Печатает в консоль меню
     */
    public static void printMenu() {
        String str = "Программа работает в двух режимах.\n"+
                "\t1. Шифрование / расшифровка\n" +
                "\t2. Криптоанализ методом brute force\n" +
                "Выберите режим работы программы (введите с клавиатуры 1 или 2):";
        System.out.println(str);
    }




    /**
     * Читает с консоли позицию в меню
     * @return Возвращает позицию меню типа int
     */
    private static int getMenuPosition() {
        int menuPosition = 0;
        while (true) {
            menuPosition = getIntFromConsole();
            if(menuPosition==1||menuPosition==2) {
                return menuPosition;
            }
        }
    }




    /**
     * Первый режим работы программы - шифрование текста и запись зашифрованного текста в файл
     */
    private static void firstMode() {
        System.out.println("В режиме \"шифрование / расшифровка\" программа получает путь к текстовому файлу \n" +
                "с исходным текстом и на его основе создает файл с зашифрованным текстом.\n" +
                "Введите криптографический ключ (ключ может быть больше, меньше или равен 0):");
        int cryptKey = getIntFromConsole();
        String textFromFile = getTextFromFile(Path.of(sourceFile));
        String cryptedText = cryptText(textFromFile, cryptKey);
        putTextToTempFile(cryptedText);
    }




    /**
     * Второй режим работы программы - brute force (подбор ключа по условию)
     */
    private static void secondMode() {
        int key1;
        int key2;
        int max;
        int min;
        boolean keyExist = false;

        System.out.println("Программа путем перебора подбирает ключ и расшифровает текст\n" +
                "Введите диапазон подбираемых криптографических ключей (ключи №1 и №2).\n" +
                "Ключ №1:");
        key1 = getIntFromConsole();

        System.out.println("Ключ №2:");
        key2 = getIntFromConsole();

        max = Math.max(key1,key2);
        min = Math.min(key1,key2);

        String textFromFile = getTextFromFile(Path.of(sourceFile));

        System.out.println("Расшифровка подбором ключа от "+min+" до "+max+":");

        for (int i = min; i <= max; i++) {
            String cryptedText = cryptText(textFromFile, i);
            System.out.println(i+"  "+cryptedText);
            if (!hasLongWord(cryptedText) && hasComma(cryptedText)) {
                System.out.println("Подобраный ключ расшифровки: "+i);
                putTextToTempFile(cryptedText);
                keyExist = true;
                break;
            }
        }

        if (!keyExist) System.out.println("В заданном диапазоне ключей, ключ подобрать не удалось.");
    }




    /**
     * Проверяет текст на наличие слишком длинных слов
     * @param text текст для анадиза длинных слов
     */
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


    /**
     * Проверяет текст на наличие зяпятых
     * @param text текст для анадиза длинных слов
     */
    private static boolean hasComma(String text) {
        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ',') {
                return true;
            }
        }

        return false;
    }




    /**
     * Читает данные с консоли пока не введено число типа int
     * @return Возвращает число типа int
     */
    private static int getIntFromConsole() {
        String str = "";
        int parsedInt = 0;
        while (true) {
            try {
                str = bufferedReader.readLine();
                parsedInt = Integer.parseInt(str);
                break;
            } catch (Exception e) {
                continue;
            }
        }
        return parsedInt;
    }




    /**
     * Читает и возвращает текст из файла
     * @param path путь к файлу из которого функция читает текст
     * @return Возвращает текст в виде строки из файла
     */
    public static String getTextFromFile(Path path) {
        List<String> strings = new ArrayList<>();
        try {
            strings = Files.readAllLines(Path.of(sourceFile));
        } catch (IOException e) {
            System.out.println("Функция \"getTextFromFile\" не может прочитать текст из файла.");
            System.exit(1);
        }
        StringBuilder sb = new StringBuilder();
        for (String str:strings) {
            sb.append(str);
        }
        return sb.toString();
    }




    /**
     * Записывает текст во временный файл
     * @param text текст для записи в файл
     */
    public static void putTextToTempFile(String text) {
        Path path;
        try {
            path = Files.createTempFile(Paths.get(""),"result",null);
            Files.writeString(path,text);
            System.out.println("Результат записан в файл "+path+" в текущей директории");
        } catch (IOException e) {
            System.out.println("Функция \"putTextToTempFile\" не может записать текст во временный файл.");
            System.exit(1);
        }
    }




    /**
     * Шифрует текст с конкретным сдвигом
     * @param afterEncrypt текст до шифрования
     * @return Возвращает зашифрованный текст
     */
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




    /**
     * Возвращает упорядоченное разрешенное множество
     * (алфавит)
     */
    private static List<Character> getAlphabet(){
        ArrayList<Character> characters = new ArrayList<>(){{ // список, а не множество. все равно не повторяются символы
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