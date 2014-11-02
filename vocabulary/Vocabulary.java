package arjun.vocabulary;

import arjun.wordscramble.Scrambler;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Arjun
 */
public class Vocabulary {

    //File based member variables
    private static String m_sFileName = "";
    private static int m_iNLinesInFile = 0;
    //Display based member variables
    private static JOptionPane m_Pane = new JOptionPane();
    //Score based member variables
    private static int nTotalWordLetters = 0;
    private static int nTotalHintLetters = 0;
    private static JFileChooser VocabFileChooser;

    /**
     * @param args the command line arguments
     */
    public static void mainApp(String[] args) throws FileNotFoundException {
        // Welcome pane
        JOptionPane.showMessageDialog(m_Pane, "Welcome to Vocabulary!\nGuess the word which matches the definition.\nPress OK to Open a Vocabulary File", "VOCABULARY Welcome", JOptionPane.PLAIN_MESSAGE);
        
        // Choose file
        VocabFileChooser = new javax.swing.JFileChooser();
        int returnVal = VocabFileChooser.showOpenDialog(m_Pane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            m_sFileName = VocabFileChooser.getSelectedFile().getAbsolutePath();
            // Get the number of lines in the file
            m_iNLinesInFile = getNumberofLinesInFile();
            //System.out.println(m_iNLinesInFile);
        }

        // Play game
        try {
            VocabularyGame();
        } catch (Exception e) {
            if (nTotalWordLetters > 0) {
                double score = 100.0 * (nTotalWordLetters - nTotalHintLetters) / nTotalWordLetters;
                DecimalFormat df = new DecimalFormat("#.##");
                JOptionPane.showMessageDialog(m_Pane, "Thanks for playing Vocabulary!\nYour Vocabulary Score is " + df.format(score) + "%", "VOCABULARY Farewell", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    public static void VocabularyGame() throws FileNotFoundException {

        while (true) {
            String line = getRandomLineFromFile().trim();
            String[] tokens = line.split("[ ]", -1);
            if (tokens.length < 2) {
                continue;
            }

            String word = tokens[0];
            String definitionText = "Definition: " + line.substring(word.length()) + "\nWhat is the word? " + word.length() + " letters\n";

            char[] hint = new char[2 * word.length()];
            for (int i = 0; i < word.length(); i++) {
                hint[2 * i] = '_';
                hint[2 * i + 1] = ' ';
            }
            String hintText = "Hint: " + (new String(hint));

            int iNumHints = 0;
            String guess = JOptionPane.showInputDialog(m_Pane, definitionText + hintText, "VOCABULARY Input", JOptionPane.PLAIN_MESSAGE);
            while (!guess.equals(word) && (iNumHints < word.length())) {
                hint[2 * iNumHints] = word.charAt(iNumHints);

                hintText = "Hint: " + (new String(hint));
                guess = JOptionPane.showInputDialog(m_Pane, "Wrong. Try Again.\n" + definitionText + hintText, "VOCABULARY Input", JOptionPane.PLAIN_MESSAGE);
                iNumHints++;
            }

            if (iNumHints < word.length()) {
                JOptionPane.showMessageDialog(m_Pane, "Correct", "VOCABULARY Output", JOptionPane.PLAIN_MESSAGE);
            }
            
            nTotalWordLetters += word.length();
            nTotalHintLetters += iNumHints;
        }
    }

    private static int getNumberofLinesInFile() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(m_sFileName))) {
            int count = 0;
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                count++;
            }
            scanner.close();
            return count;
        }
    }

    private static String getRandomLineFromFile() throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(m_sFileName))) {
            String line = "";
            int random = (int) (Math.random() * m_iNLinesInFile);
            while (random-- >= 0) {
                line = scanner.nextLine();
            }
            scanner.close();
            return line;
        }
    }
}
