package edu.ucsd.dbmi.drugtarget.genomicsqs;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Design Note: this class implements AminoAcids interface: a simple fix
 * customized to amino acids, since that is all we deal with in this class
 * Supporting both DNA and Aminoacids, will require a more general design.
 */

public class SmithWaterman {

	private final double scoreThreshold = 19.9;

	/**
	 * The first input string
	 */
	private String str1;

	/**
	 * The second input String
	 */
	private String str2;

	/**
	 * The lengths of the input strings
	 */
	private int length1, length2;

	/**
	 * The score matrix. The true scores should be divided by the normalization
	 * factor.
	 */
	private double[][] score;

	/**
	 * The normalization factor. To get the true score, divide the integer score
	 * used in computation by the normalization factor.
	 */
	static final double NORM_FACTOR = 1.0;

	/**
	 * The similarity function constants. They are amplified by the
	 * normalization factor to be integers.
	 */
	static final int MATCH_SCORE = 10;
	static final int MISMATCH_SCORE = -8;
	static final int INDEL_SCORE = -9;

	/**
	 * Constants of directions. Multiple directions are stored by bits. The zero
	 * direction is the starting point.
	 */
	static final int DR_LEFT = 1; // 0001
	static final int DR_UP = 2; // 0010
	static final int DR_DIAG = 4; // 0100
	static final int DR_ZERO = 8; // 1000

	/**
	 * The directions pointing to the cells that give the maximum score at the
	 * current cell. The first index is the column index. The second index is
	 * the row index.
	 */
	private int[][] prevCells;

	public SmithWaterman(String str1, String str2) {
		this.str1 = str1;
		this.str2 = str2;
		length1 = str1.length();
		length2 = str2.length();

		score = new double[length1 + 1][length2 + 1];
		prevCells = new int[length1 + 1][length2 + 1];

		buildMatrix();
	}

	/**
	 * Compute the similarity score of substitution: use a substitution matrix
	 * if the cost model The position of the first character is 1. A position of
	 * 0 represents a gap.
	 * 
	 * @param i
	 *            Position of the character in str1
	 * @param j
	 *            Position of the character in str2
	 * @return Cost of substitution of the character in str1 by the one in str2
	 */
	private double similarity(int i, int j) {
		if (i == 0 || j == 0) {
			// it's a gap (indel)
			return INDEL_SCORE;
		}

		// return (str1.charAt(i - 1) == str2.charAt(j - 1)) ? MATCH_SCORE :
		// MISMATCH_SCORE;
		return Blosum.getDistance(str1.charAt(i - 1), str2.charAt(j - 1));
	}

	/**
	 * Build the score matrix using dynamic programming. Note: The indel scores
	 * must be negative. Otherwise, the part handling the first row and column
	 * has to be modified.
	 */
	private void buildMatrix() {
		if (INDEL_SCORE >= 0) {
			throw new Error("Indel score must be negative");
		}

		// if (isDistanceMatrixNull()) {
		// throw new Error ("Distance Matrix is NULL");
		// }

		int i; // length of prefix substring of str1
		int j; // length of prefix substring of str2

		// base case
		score[0][0] = 0;
		prevCells[0][0] = DR_ZERO; // starting point

		// the first row
		for (i = 1; i <= length1; i++) {
			score[i][0] = 0;
			prevCells[i][0] = DR_ZERO;
		}

		// the first column
		for (j = 1; j <= length2; j++) {
			score[0][j] = 0;
			prevCells[0][j] = DR_ZERO;
		}

		// the rest of the matrix
		for (i = 1; i <= length1; i++) {
			for (j = 1; j <= length2; j++) {
				double diagScore = score[i - 1][j - 1] + similarity(i, j);
				double upScore = score[i][j - 1] + similarity(0, j);
				double leftScore = score[i - 1][j] + similarity(i, 0);

				score[i][j] = Math.max(diagScore, Math.max(upScore, Math.max(leftScore, 0)));
				prevCells[i][j] = 0;

				// find the directions that give the maximum scores.
				// the bitwise OR operator is used to record multiple
				// directions.
				if (diagScore == score[i][j]) {
					prevCells[i][j] |= DR_DIAG;
				}
				if (leftScore == score[i][j]) {
					prevCells[i][j] |= DR_LEFT;
				}
				if (upScore == score[i][j]) {
					prevCells[i][j] |= DR_UP;
				}
				if (0 == score[i][j]) {
					prevCells[i][j] |= DR_ZERO;
				}
			}
		}
	}

	/**
	 * Get the maximum value in the score matrix.
	 */
	private double getMaxScore() {
		double maxScore = 0;

		// skip the first row and column
		for (int i = 1; i <= length1; i++) {
			for (int j = 1; j <= length2; j++) {
				if (score[i][j] > maxScore) {
					maxScore = score[i][j];
				}
			}
		}

		return maxScore;
	}

	/**
	 * Get the alignment score between the two input strings.
	 */
	public double getAlignmentScore() {
		return getMaxScore() / NORM_FACTOR;
	}

	/**
	 * Output the local alignments ending in the (i, j) cell. aligned1 and
	 * aligned2 are suffixes of final aligned strings found in backtracking
	 * before calling this function. Note: the strings are replicated at each
	 * recursive call. Use buffers or stacks to improve efficiency.
	 */
	private void printAlignments(int i, int j, String aligned1, String aligned2) {
		// we've reached the starting point, so print the alignments

		if ((prevCells[i][j] & DR_ZERO) > 0) {
			System.out.println(aligned1);
			System.out.println(aligned2);
			System.out.println("");

			// Note: we could check other directions for longer alignments
			// with the same score. we don't do it here.
			return;
		}

		// find out which directions to backtrack
		if ((prevCells[i][j] & DR_LEFT) > 0) {
			printAlignments(i - 1, j, str1.charAt(i - 1) + aligned1, "_" + aligned2);
		}
		if ((prevCells[i][j] & DR_UP) > 0) {
			printAlignments(i, j - 1, "_" + aligned1, str2.charAt(j - 1) + aligned2);
		}
		if ((prevCells[i][j] & DR_DIAG) > 0) {
			printAlignments(i - 1, j - 1, str1.charAt(i - 1) + aligned1, str2.charAt(j - 1) + aligned2);
		}
	}

	/**
	 * given the bottom right corner point trace back the top left conrner. at
	 * entry: i, j hold bottom right (end of Aligment coords) at return: hold
	 * top left (start of Alignment coords)
	 */
	private int[] traceback(int i, int j) {

		// find out which directions to backtrack
		while (true) {
			if ((prevCells[i][j] & DR_LEFT) > 0) {
				if (score[i - 1][j] > 0)
					i--;
				else
					break;
			}
			if ((prevCells[i][j] & DR_UP) > 0) {
				// return traceback(i, j-1);
				if (score[i][j - 1] > 0)
					j--;
				else
					break;
			}
			if ((prevCells[i][j] & DR_DIAG) > 0) {
				// return traceback(i-1, j-1);
				if (score[i - 1][j - 1] > 0) {
					i--;
					j--;
				} else
					break;
			}
		}
		int[] m = { i, j };
		return m;
	}

	/**
	 * Output the local alignments with the maximum score.
	 */
	public void printAlignments() {
		// find the cell with the maximum score
		double maxScore = getMaxScore();

		/*
		 * for (int i = 0; i < matches.length; i++) { System.out.println(
		 * "Match #" + i + ":" + matches.get(i)); }
		 */

		// skip the first row and column
		for (int i = 1; i <= length1; i++) {
			for (int j = 1; j <= length2; j++) {
				if (score[i][j] == maxScore) {
					printAlignments(i, j, "", "");
				}
			}
		}
		// Note: empty alignments are not printed.
	}

	/**
	 * print the dynmaic programming matrix
	 */
	public void printDPMatrix() {
		System.out.print("   ");
		for (int j = 1; j <= length2; j++)
			System.out.print("   " + str2.charAt(j - 1));
		System.out.println();
		for (int i = 0; i <= length1; i++) {
			if (i > 0)
				System.out.print(str1.charAt(i - 1) + " ");
			else
				System.out.print("  ");
			for (int j = 0; j <= length2; j++) {
				System.out.print(score[i][j] / NORM_FACTOR + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Return a set of Matches idenfied in Dynamic programming matrix. A match
	 * is a pair of subsequences whose score is higher than the preset
	 * scoreThreshold
	 **/
	public List getMatches() {
		ArrayList matchList = new ArrayList();
		int fA = 0, fB = 0;
		// skip the first row and column, find the next maxScore after
		// prevmaxScore
		for (int i = 1; i <= length1; i++) {
			for (int j = 1; j <= length2; j++) {
				if (score[i][j] > scoreThreshold && score[i][j] > score[i - 1][j - 1] && score[i][j] > score[i - 1][j]
						&& score[i][j] > score[i][j - 1]) {
					if (i == length1 || j == length2 || score[i][j] > score[i + 1][j + 1]) {
						// should be lesser than prev maxScore
						fA = i;
						fB = j;
						int[] f = traceback(fA, fB); // sets the x, y to
														// startAlignment
														// coordinates
						matchList.add(new SimpleChaining.Match(f[0], i, f[1], j, score[i][j] / NORM_FACTOR));
					}
				}
			}
		}
		return matchList; // could be empty if no HSP scores are >
							// scoreThreshold
	}

	public void test(String protein1, String protein2) {
		System.out.println("Compute the local alignments between two strings \n ");

		// accept two strings from the standard input

		String str1 = (new FastaSequence(protein1)).getSequence();
		String str2 = (new FastaSequence(protein2)).getSequence();

		// compute and output the score and alignments
		SmithWaterman sw = new SmithWaterman(str1, str2);

		System.out.println("\nThe maximum alignment score is: " + sw.getAlignmentScore());

		System.out.println("\nThe alignments with the maximum score are: \n");
		sw.printAlignments();
		// System.out.println("The dynamic programming distance matrix is");
		// sw.printDPMatrix();
		List ms = sw.getMatches();
		System.out.println("Chaining demo: ");
		SimpleChaining.chaining(ms, true);
	}

	public static void main(String[] args) throws IOException {

			
	}
}
