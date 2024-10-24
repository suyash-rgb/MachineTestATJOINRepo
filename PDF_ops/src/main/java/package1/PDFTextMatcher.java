package package1;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PDFTextMatcher {

    public static String readPDF(String filePath) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    public static Map<String, Integer> tokenizeText(String text) {
        String[] tokens = text.split("\\W+");
        Map<String, Integer> tokenCountMap = new HashMap<>();
        for (String token : tokens) {
            tokenCountMap.put(token, tokenCountMap.getOrDefault(token, 0) + 1);
        }
        return tokenCountMap;
    }

    public static int matchValues(Map<String, Integer> tokenCountMap, List<String> referenceList, Map<String, Integer> matchedWords) {
        int matches = 0;
        for (String word : referenceList) {
            if (tokenCountMap.containsKey(word)) {
                matches++;
                matchedWords.put(word, tokenCountMap.get(word));
            }
        }
        return matches;
    }

    public static double calculateMatchPercentage(int matches, int referenceListLength) {
        return ((double) matches / referenceListLength) * 100;
    }

    public static void outputResult(Map<String, Integer> matchedWords, double matchPercentage) {
        System.out.println("Matched Words: " + matchedWords.keySet());
        System.out.println("Word Occurrences: " + matchedWords);
        System.out.printf("Match Percentage: %.2f%%%n", matchPercentage);
    }

    public static void main(String[] args) {
        try {
            // Static values
            String filePath = "BrianMason_Using_Rest_API-2.pdf";
            List<String> referenceList = Arrays.asList("SOAP", "Swagger", "Java");

            Map<String, Integer> matchedWords = new HashMap<>();
            String text = readPDF(filePath);
            Map<String, Integer> tokenCountMap = tokenizeText(text);
            int matches = matchValues(tokenCountMap, referenceList, matchedWords);
            double matchPercentage = calculateMatchPercentage(matches, referenceList.size());

            outputResult(matchedWords, matchPercentage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
