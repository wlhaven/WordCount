import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Wally Haven on 2/5/2018.
 */
class WordCount {
    private static final int MAXWORD = 10;
    private WordCount() {
    }

    private Map<String, Integer> getWordCount(String fileName) {
        TreeMap<String, Integer> wordMap = new TreeMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    String strippedPunc = st.nextToken().toUpperCase().replaceAll("[^a-z\\sA-Z]", "").trim();
                    StringTokenizer newToken = new StringTokenizer(strippedPunc, " ");
                    while (newToken.hasMoreTokens()) {
                        String newWord = newToken.nextToken();
                        if (wordMap.containsKey(newWord)) {
                            wordMap.put(newWord, wordMap.get(newWord) + 1);
                        } else {
                            wordMap.put(newWord, 1);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open input file '" + fileName + "'" + "\nProgram will close.");
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return wordMap;
    }

    private List<Entry<String, Integer>> sortByValue(Map<String, Integer> wordMap) {

        Set<Entry<String, Integer>> set = wordMap.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<>(set);
        list.sort((o2, o1) -> (o1.getValue()).compareTo(o2.getValue()));
        return list;
    }

    private String getFileInfo() {
        String fileName;

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the filename to read: ");
        fileName = in.nextLine();
        return fileName;
    }

    private void findLargestElement(List<Entry<String, Integer>> list, BufferedWriter bw) {
        Entry<String, Integer> entry = list.get(0);
        String text = String.format("\nWord used the most: %s with a word count of: %s", entry.getKey(), entry.getValue());
        System.out.println(text);
        try {
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findSmallestElement(List<Entry<String, Integer>> list, BufferedWriter bw) {
        int count = list.size() - 1;
        Entry<String, Integer> entry = list.get(count);
        String text = String.format("\nWord used the least: %s with a word count of: %s ", entry.getKey(), entry.getValue());
        System.out.println(text);
        try {
            bw.append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayWordRange(List<Entry<String, Integer>> list, BufferedWriter bw, int location) {
        Entry<String, Integer> entry = list.get(location);
        String text = String.format("%-20s  %s", entry.getKey(), entry.getValue());
        System.out.println(text);
        try {
            bw.append("\n").append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void topWords(List<Entry<String, Integer>> list, BufferedWriter bw) {
        String text = "\nThe " + MAXWORD + " most used words in the file are:";
        System.out.println(text);
        try {
            bw.append("\n").append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < MAXWORD; i++) {
            displayWordRange(list, bw, i);
        }
    }

    private void bottomWords(List<Entry<String, Integer>> list, BufferedWriter bw) {
        String text = "\nThe " + MAXWORD + " least used words in the file are:";
        System.out.println(text);
        try {
            bw.append("\n").append(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int location = list.size() - 1;
        for (int i = 0; i < MAXWORD; i++) {
            displayWordRange(list, bw, location);
            location--;
        }
    }

    private void PrintData(List<Entry<String, Integer>> list) {
        String fileName;
        BufferedWriter bw = null;

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the filename to write: ");
        fileName = in.nextLine();
        try {
            bw = new BufferedWriter(new FileWriter(fileName));
            findLargestElement(list, bw);
            findSmallestElement(list, bw);
            topWords(list, bw);
            bottomWords(list, bw);
            bw.write("\n\nOutput after punctuation removed and list is sorted. ");
            System.out.println("\n\nOutput after punctuation removed and list is sorted. ");
            for (Map.Entry<String, Integer> entry : list) {
                String text = String.format("\n%-20s  %s", entry.getKey(), entry.getValue());
                bw.append(text);
                System.out.print(text);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open output file '" + fileName + "'");

        } catch (IOException ex) {
            System.out.println("Error writing file '" + fileName + "'");
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String fileName;

        WordCount wordCount = new WordCount();
        fileName = wordCount.getFileInfo();
        Map<String, Integer> wordMap = wordCount.getWordCount(fileName);
        List<Entry<String, Integer>> wordList = wordCount.sortByValue(wordMap);
        wordCount.PrintData(wordList);
    }
}
