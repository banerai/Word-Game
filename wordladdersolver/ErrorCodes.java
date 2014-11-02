package arjun.wordladdersolver;

import javax.swing.JOptionPane;

/**
 *
 * @author Arjun
 */
/**
 * This class contains all the error codes returned by any method in this
 * application
 */
public class ErrorCodes {

    public static final int OK = 0;
    public static final int DICTIONARY_IS_NOT_SORTED_ALPHABETICALLY = 1;
    public static final int EMPTY_WORD_IN_DICTIONARY = 2;
    public static final int INCONSISTENT_WORD_LENGTH_IN_DICTIONARY = 3;
    public static final int INTEGER_FORMAT_ERROR_IN_DICTIONARY = 4;
    public static final int BAD_DELIMITING_IN_DICTIONARY = 5;
    public static final int DICTIONARY_FILE_OPEN_ERROR = 6;
    public static final int BEGIN_WORD_NOT_IN_DICTIONARY = 7;
    public static final int END_WORD_NOT_IN_DICTIONARY = 8;
    public static final int BEGIN_WORD_EQUALS_END_WORD = 9;
    public static final int NO_LADDER_FROM_BEGIN_WORD_TO_END_WORD = 10;

    public static void ProcessReturnValue(int iReturnValue) {
        switch (iReturnValue) {
            case OK:
                break;
            case DICTIONARY_IS_NOT_SORTED_ALPHABETICALLY:
                JOptionPane.showConfirmDialog(null, "Dictionary is not Sorted Alphabetically.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case EMPTY_WORD_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Empty Word in Dictionary.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case INCONSISTENT_WORD_LENGTH_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Inconsistent Word Length in Dictionary.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case INTEGER_FORMAT_ERROR_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Integer Format Error in Dictionary.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case BAD_DELIMITING_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Bad Delimiting in Dictionary.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case DICTIONARY_FILE_OPEN_ERROR:
                JOptionPane.showConfirmDialog(null, "Dictionary File Open Error.", "Error Open Dictionary", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case BEGIN_WORD_NOT_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "Begin Word not in Dictionary.", "Error Run Solver", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case END_WORD_NOT_IN_DICTIONARY:
                JOptionPane.showConfirmDialog(null, "End Word not in Dictionary.", "Error Run Solver", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case BEGIN_WORD_EQUALS_END_WORD:
                JOptionPane.showConfirmDialog(null, "Begin Word equals End Word.", "Error Run Solver", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            case NO_LADDER_FROM_BEGIN_WORD_TO_END_WORD:
                JOptionPane.showConfirmDialog(null, "No Ladder from Begin Word to End Word.", "Error Run Solver", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
    }
}
