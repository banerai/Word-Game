package arjun.wordscramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Arjun
 */
public class Scrambler {

    private static final int OK = 0;
    private static final int DICTIONARY_IS_NOT_SORTED_ALPHABETICALLY = 1;
    private static final int DICTIONARY_FILE_OPEN_ERROR = 2;
    private static final int NOTHING_READ_FROM_DICTIONARY = 3;
    private int m_Status = OK;
    private ArrayList<String> m_WordVector = new ArrayList<>();
    private String m_OriginalWord = "";
    private String m_ScrambledWord = "";
    private String m_SubmittedWord = "";

    /**
     * This constructor opens and reads the dictionary file with filename
     * sFileName.
     *
     * This dictionary is alphabetically sorted to allow binary search.
     *
     * Any errors in opening or reading the file is recorded in status. This is
     * parsed in the calling method.
     */
    public Scrambler(String sFileName) {
        m_Status = OK;
        m_WordVector = new ArrayList<>();

        try (BufferedReader streamReader = new BufferedReader(new FileReader(sFileName))) {
            String line;
            // Read in a line
            while ((line = streamReader.readLine()) != null) {
                // Split line with delimiter comma or space into tokens
                String[] tokens = line.split("[, ]", -1);
                // Process each token
                if ((tokens.length == 0) || (tokens[0].length() == 0)) {
                    continue;
                } else {
                    String wordToAdd = tokens[0].toUpperCase().trim();
                    // Add wordToAdd word to m_WordVector
                    // Check for alphabetic sortedness of the file
                    int iWordVectorSize = m_WordVector.size();
                    if ((iWordVectorSize == 0) || (wordToAdd.compareTo(m_WordVector.get(iWordVectorSize - 1)) > 0)) {
                        m_WordVector.add(wordToAdd);
                    } else {
                        m_Status = DICTIONARY_IS_NOT_SORTED_ALPHABETICALLY;
                        break;
                    }
                }
            }
            streamReader.close();
        } catch (Exception e) {
            // File open error
            m_Status = DICTIONARY_FILE_OPEN_ERROR;
        }

        if ((m_Status == OK) && m_WordVector.isEmpty()) {
            m_Status = NOTHING_READ_FROM_DICTIONARY;
        }
    }

    public boolean isBadStatus() {
        return (m_Status != OK);
    }

    public void ProcessStatus() {
        switch (m_Status) {
            case OK:
                break;
            case DICTIONARY_IS_NOT_SORTED_ALPHABETICALLY:
                JOptionPane.showConfirmDialog(null, "Dictionary is not Sorted Alphabetically.", "Error Open File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case DICTIONARY_FILE_OPEN_ERROR:
                JOptionPane.showConfirmDialog(null, "Dictionary File Open Error.", "Error Open File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            case NOTHING_READ_FROM_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Nothing read from Dictionary.", "Error Open File", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
    }

    public String GetScrambledWord() {
        int random = (int) (Math.random() * m_WordVector.size());
        m_OriginalWord = m_WordVector.get(random);
        System.out.println("Original Word: " + m_OriginalWord);
        Scramble();
        return m_ScrambledWord;
    }

    /**
     * Finds the index number of the a given word targetWord from the list of
     * words in ladWordVector. Returns -1 if not found. Speeded up by binary
     * search because the dictionary is alphabetically sorted.
     *
     * @param targetWord
     * @return the index of word in m_WordVector
     */
    private int Find(String targetWord) {
        int low = 0;
        int high = m_WordVector.size() - 1;

        while (high >= low) {
            int mid = (high + low) / 2;
            String midWord = m_WordVector.get(mid);
            if (midWord.compareTo(targetWord) < 0) {
                low = mid + 1;
            } else if (midWord.compareTo(targetWord) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    private void Scramble() {
        char[] permArray = new char[m_OriginalWord.length()];
        for (int i = 0; i < permArray.length; i++) {
            permArray[i] = m_OriginalWord.charAt(i);
        }

        do {
            /**
             * This is the random permutation routine code
             */
            int nShrinkingSize = permArray.length;
            int currentIndex = 0;
            while (nShrinkingSize > 1) {
                // Find a random index uniformly beween 0 and nShrinkingSize - 1
                int randomIndex = (int) (Math.random() * nShrinkingSize);
                // Swap element of currentIndex with element of randomIndex
                char temp = permArray[currentIndex];
                permArray[currentIndex] = permArray[randomIndex];
                permArray[randomIndex] = temp;
                // Update the counters
                currentIndex++;
                nShrinkingSize--;
            }
            /*
             * Convert to m_ScrambledWord and check for existence in the dictionary
             */
            m_ScrambledWord = new String(permArray);
        } while (Find(m_ScrambledWord) >= 0);
        /**
         * Note that this while loop may not end if all permutations of the
         * m_OriginalWord are in the dictionary.
         */
    }

    /**
     * This method returns the histogram of the letter occurrences in testWord
     *
     * @param testWord
     * @return the histogram of the letter occurrences in testWord
     */
    private int[] GetSignature(String testWord) {
        final int nAlphabets = 'Z' - 'A' + 1;
        int[] signature = new int[nAlphabets];
        for (int i = 0; i < nAlphabets; i++) {
            signature[i] = 0;
        }
        for (int i = 0; i < testWord.length(); i++) {
            signature[testWord.charAt(i) - 'A']++;
        }
        return signature;
    }

    private boolean isSubmissionOkay() {
        // Easy Case 1
        if (m_SubmittedWord.equals(m_OriginalWord)) {
            System.out.println("Easy Case 1");
            return true;
        }
        // Easy Case 2
        if (m_SubmittedWord.length() != m_OriginalWord.length()) {
            System.out.println("Easy Case 2");
            return false;
        }
        /**
         * Hard Case: Because a permutation of the m_OriginalWord can be a valid
         * word in the dictionary (e.g. ate, eat, tea), it is necessary for a
         * 2-stage checking for the validity of the m_SubmittedWord.
         *
         * The 2-Stage Checking involves:
         *
         * STAGE 1. Check if the letters of the strings m_OriginalWord and
         * m_SubmittedWord match. In order to do this I get the histogram of the
         * letter occurrences of both words and check if those histograms are
         * the same. I call this letter occurrence histogram the signature of
         * the word.
         *
         * STAGE 2. If the signatures match, then the next step is to find if
         * the word m_SubmittedWord exists in the dictionary.
         *
         * Return true only if the m_SubmittedWord passes both tests 1 and 2
         */
        // STAGE 1
        final int nAlphabets = 'Z' - 'A' + 1;
        int[] signatureOriginal = GetSignature(m_OriginalWord);
        int[] signatureSubmitted = GetSignature(m_SubmittedWord);
        for (int i = 0; i < nAlphabets; i++) {
            if (signatureOriginal[i] != signatureSubmitted[i]) {
                return false;
            }
        }
        System.out.println("Passes Stage 1");
        // STAGE 2
        return (Find(m_SubmittedWord) >= 0);
    }

    public void ProcessSubmission(String submittedWord) {
        m_SubmittedWord = submittedWord.toUpperCase().trim();
        if (isSubmissionOkay()) {
            JOptionPane.showMessageDialog(null, "BRAVO!", "WORD SCRAMBLE Output", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Sorry the word is " + m_OriginalWord, "WORD SCRAMBLE Output", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
