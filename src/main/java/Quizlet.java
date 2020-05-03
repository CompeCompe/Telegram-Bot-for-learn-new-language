
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quizlet {
    private static Map<String,String> words= new HashMap<String,String>();

    public static Map<String, String> getWords() {
        return words;
    }

    private static List<String> keys = new ArrayList<>();

    public static List<String> getKeys() {
        return keys;
    }

    static int randomKey;

        public static String showWords(){
            StringBuilder sb = new StringBuilder();
            for(int i = 0;i < keys.size(); i++){
                sb.append(keys.get(i) + " - ");
                sb.append(words.get(keys.get(i)) + "\n" );
            }

            return sb.toString();
        }
        public static String createWordsList(String w) throws IOException {
            String[] wordsAndTranslate = w.split(" - ");
            words.put(wordsAndTranslate[0],wordsAndTranslate[1]);
            keys.add(wordsAndTranslate[0]);
            return "Записано";
        }
        public static String messageEquals(String message){
            if(words.containsKey(message)){
                return message;
            }else {
                return "";
            }
        }
        public static String randomWord() {
            randomKey = (int) (Math.random() * keys.size());
            return words.get(keys.get(randomKey));
        }

        public static boolean checkWords(String word) {
            String check = words.get(keys.get(randomKey));
            if(words.containsKey(word) && check.equals(words.get(word))){
                return true;
            }
            return false;
        }
}
