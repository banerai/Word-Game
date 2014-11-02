package arjun.wordladdersolver;

import java.util.ArrayList;

/**
 *
 * @author Arjun
 */
/**
 * This is the class that houses the shortest path algorithm to determine the
 * shortest weighted path from one word to another with unpopularity numbers
 * providing the weights.
 *
 * The weight W(s,t) of a directed arc going from node word s to node word t is
 * equal to the unpopularity rating of the end node t.
 */
public class Puzzle {

    /**
     * m_nNodes: Number of nodes of the underlying graph
     */
    private int m_nNodes = 0;
    /**
     * m_pOutNeighs[i] is a list of the out-neighbors of node i Equivalent to a
     * jagged array created at Setup
     */
    private ArrayList<Integer>[] m_pOutNeighs = null;
    /**
     * Used during Run m_pCosts records the shortest distance from the starting
     * node to any other node
     */
    private int[] m_pCosts = null;
    /**
     * Used during Run m_pVisited record whether a node has been visited or not
     */
    private int[] m_pVisited = null;
    /**
     * Used during Run to retrace the shortest path m_pParent keeps the record
     * of the parent in the current shortest path
     */
    private int[] m_pParent = null;

    /**
     * Method Setup() sets up the necessary data structures for the Dijkstra's
     * shortest path algorithm. In particular, the all-important data structure
     * m_pOutNeighs is created.
     *
     * @param ladWordVector
     */
    public final void Setup(ArrayList<LadWord> ladWordVector) {
        if (ladWordVector == null) {
            throw new RuntimeException("ladWordVector is null");
        }

        m_nNodes = ladWordVector.size();
        allocate();

        int iLaDist;
        for (int i = 0; i < m_nNodes; i++) {
            m_pOutNeighs[i] = new ArrayList<>();
            for (int j = 0; j < m_nNodes; j++) {
                if (j != i) {
                    iLaDist = LadWord.LaDist(ladWordVector.get(i), ladWordVector.get(j));
                    if (iLaDist > 0) {
                        m_pOutNeighs[i].add(j);
                    }
                }
            }
        }
    }

    /**
     * All memory allocations are handled here in method allocate()
     */
    @SuppressWarnings("unchecked")
    private void allocate() {
        m_pOutNeighs = new ArrayList[m_nNodes];
        m_pCosts = new int[m_nNodes];
        m_pVisited = new int[m_nNodes];
        m_pParent = new int[m_nNodes];
    }

    /**
     * The execution of Dijkstra's shortest path algorithm is performed in
     * method Run()
     *
     * @param ladWordVector
     * @param beginWord
     * @param endWord
     * @param wordIndexList
     * @return the indices of the words in ladWordVector along the shortest path
     * from beginWord to endWord
     */
    public final ArrayList<Integer> Run(ArrayList<LadWord> ladWordVector, String beginWord, String endWord, int[] status) {
        status[0] = ErrorCodes.OK; // okay

        for (int i = 0; i < m_nNodes; i++) {
            m_pCosts[i] = -1;
            m_pVisited[i] = 0;
        }

        /**
         * Because the final word list appears in the reverse order we change
         * the order of iBegin and iEnd here
         */
        int iEnd = find(ladWordVector, beginWord);
        if (iEnd < 0) // Not in dictionary
        {
            status[0] = ErrorCodes.BEGIN_WORD_NOT_IN_DICTIONARY;
            return null;
        }

        int iBegin = find(ladWordVector, endWord);
        if (iBegin < 0) // Not in dictionary
        {
            status[0] = ErrorCodes.END_WORD_NOT_IN_DICTIONARY;
            return null;
        }

        if (iBegin == iEnd) // Same word
        {
            status[0] = ErrorCodes.BEGIN_WORD_EQUALS_END_WORD;
            return null;
        }

        /**
         * Initialization before main loop
         */
        m_pVisited[iBegin] = 1;
        for (int j = 0; j < m_pOutNeighs[iBegin].size(); j++) {
            int i = m_pOutNeighs[iBegin].get(j);
            m_pCosts[i] = ladWordVector.get(i).m_iUnPopRating;
            m_pParent[i] = iBegin;
        }

        /**
         * Main Loop of Dijkstra's Shortest Path Algorithm. Exits on finding (or
         * not finding) the end word. Not finding the end word implies node is
         * unreachable.
         */
        while (true) {
            int iVNode = -1;
            int iVValue = Integer.MAX_VALUE;
            for (int i = 0; i < m_nNodes; i++) {
                if ((m_pVisited[i] == 0) && (m_pCosts[i] >= 0)) {
                    if (m_pCosts[i] < iVValue) {
                        iVValue = m_pCosts[i];
                        iVNode = i;
                    }
                }
            }

            if (iVNode == -1) // Unreachable
            {
                status[0] = ErrorCodes.NO_LADDER_FROM_BEGIN_WORD_TO_END_WORD;
                return null;
            }

            m_pVisited[iVNode] = 1;
            if (iVNode == iEnd) {
                break;
            }

            int iWNode;
            int iWValue;
            for (int j = 0; j < m_pOutNeighs[iVNode].size(); j++) {
                iWNode = m_pOutNeighs[iVNode].get(j);
                if (m_pVisited[iWNode] == 0) {
                    iWValue = m_pCosts[iVNode] + ladWordVector.get(iWNode).m_iUnPopRating;
                    if ((m_pCosts[iWNode] < 0) || (m_pCosts[iWNode] > iWValue)) {
                        m_pCosts[iWNode] = iWValue;
                        m_pParent[iWNode] = iVNode;
                    }
                }
            }
        }

        /**
         * Retraces Shortest Path by going back from parent to parent until
         * beginning node is found.
         */
        ArrayList<Integer> wordIndexList = new ArrayList<>();
        int iXNode = iEnd;
        while (iXNode != iBegin) {
            wordIndexList.add(iXNode);
            iXNode = m_pParent[iXNode];
        }
        wordIndexList.add(iXNode);

        return wordIndexList;
    }

    /**
     * Finds the index number of the a given word targetWord from the list of
     * words in ladWordVector. Returns -1 if not found. Speeded up by binary
     * search because the dictionary is alphabetically sorted.
     *
     * @param ladWordVector
     * @param targetWord
     * @return the index of word in ladWordVector
     */
    private int find(ArrayList<LadWord> ladWordVector, String targetWord) {
        int low = 0;
        int high = ladWordVector.size() - 1;

        while (high >= low) {
            int mid = (high + low) / 2;
            String midWord = ladWordVector.get(mid).m_sData;
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
}
