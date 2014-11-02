package arjun.wordladdersolver;

/**
 *
 * @author Arjun
 */
/**
 * This is the word class for Word Ladder Solver
 */
public class LadWord {

    /**
     * m_sData is the word (string)
     */
    public String m_sData;
    /**
     * m_iUnPopRating is that word's unpopularity rating. Lesser known words
     * have higher unpopularity ratings. If reset, everything starts out with
     * unpopularity rating of 1. Supervised Learning from the user makes the
     * unpopularity rating of rare or unknown words increase.
     */
    public int m_iUnPopRating;

    /**
     * The function LaDist provides the weight of the arc from the fromWord to
     * the toWord If these words are separated by exactly one letter then the
     * arc exists and is given the unpopularity rating of the toWord Otherwise
     * the arc does not exist and -1 is returned; think of -1 as infinite weight
     *
     * @param fromWord
     * @param toWord
     * @return the distance between fromWord and toWord
     */
    public static int LaDist(LadWord fromWord, LadWord toWord) {
        int nSize = fromWord.m_sData.length();
        if (nSize != toWord.m_sData.length()) {
            return -1;
        }

        String sFrom = fromWord.m_sData;
        String sTo = toWord.m_sData;

        int iDiff = 0;
        for (int i = 0; i < nSize; i++) {
            if (sFrom.charAt(i) != sTo.charAt(i)) {
                iDiff++;
            }
        }

        return (iDiff == 1) ? toWord.m_iUnPopRating : -1;
    }
}
