package com.ndamelio.mutant.helper;

public class DNAEvaluator {
    private char[][] dnaSequence;
    private int sequenceCount = 0;
    private char lastCharacter;

    private static final String HORIZONTAL = "HORIZONTAL";
    private static final String VERTICAL = "VERTICAL";
    private static final String LEFT = "LEFT";
    private static final String RIGHT = "RIGHT";
    private static final String BOTTOM_DIAGONAL = "BOTTOM_DIAGONAL";
    private static final String TOP_DIAGONAL = "TOP_DIAGONAL";

    private int sequenceToFound;

    private int minLengthSequenceDna;

    public DNAEvaluator(int sequenceToFound, int minLengthSequenceDna) {
        this.sequenceToFound = sequenceToFound;
        this.minLengthSequenceDna = minLengthSequenceDna;
    }

    /**
     * Este metodo lee las filas de la matriz que forma la secuencia de ADN
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean horizontalCheck(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int row = 0; row < dnaSequence.length; row++) {
            lastCharacter = dnaSequence[row][0];
            if (checkHorizontalOrVertical(HORIZONTAL, lastCharacter, row)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo lee las columnas de la matriz que forma la secuencia de ADN
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean verticalCheck(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int col = 0; col < dnaSequence.length; col++) {
            lastCharacter = dnaSequence[0][col];
            if (checkHorizontalOrVertical(VERTICAL, lastCharacter, col)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo lee de izquierda a derecha todos las diagonales de la parte inferior, sin la diagonal principal.
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean bottomDiagonalsFromLeftCheckWithoutMainDiagonal(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int row = 1; row < dnaSequence.length; row++) {
            lastCharacter = dnaSequence[row][0];
            if (checkDiagonals(LEFT, BOTTOM_DIAGONAL, row, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo lee de derecha a izquierda todos las diagonales de la parte inferior, sin la diagonal principal.
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean bottomDiagonalsFromRightCheckWithoutMainDiagonal(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int row = 1; row < dnaSequence.length; row++) {
            lastCharacter = dnaSequence[row][dnaSequence.length - 1];
            if (checkDiagonals(RIGHT, BOTTOM_DIAGONAL, row, dnaSequence.length - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo lee de izquierda a derecha todos las diagonales de la parte superior, incluyendo diagonal principal.
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean topDiagonalsFromLeftCheckWithMainDiagonal(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int col = 0; col < dnaSequence.length; col++) {
            lastCharacter = dnaSequence[0][col];
            if (checkDiagonals(LEFT, TOP_DIAGONAL, 0, col)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo lee de derecha a izquierda todos las diagonales de la parte superior, incluyendo diagonal principal.
     *
     * @return boolean, que determina si es o no Mutante
     */
    public boolean topDiagonalsFromRightCheckWithMainDiagonal(char[][] dnaSequence) {
        this.dnaSequence = dnaSequence;
        for (int col = 1; col < dnaSequence.length; col++) {
            lastCharacter = dnaSequence[0][dnaSequence.length - col];
            if (checkDiagonals(RIGHT, TOP_DIAGONAL, 0, dnaSequence.length - col)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo es usado para encontrar secuencias en una unica fila o columna.
     *
     * @param direction        Determina si buscar en la fila o columna.
     * @param initialCharacter El primer caracter de la secuencia.
     * @param index            Iterador para saber que fila o columna revisar.
     * @return boolean, que determina si es o no Mutante
     */
    private boolean checkHorizontalOrVertical(String direction, char initialCharacter, int index) {
        int sameCharactersCount = 1;
        char lastCharacter = initialCharacter;
        char currentCharacter;

        for (int subindex = 1; dnaSequence.length - subindex + sameCharactersCount >= minLengthSequenceDna && subindex < dnaSequence.length; subindex++) {
            currentCharacter = (HORIZONTAL.equals(direction) ? dnaSequence[index][subindex] : dnaSequence[subindex][index]);
            if (lastCharacter == currentCharacter) {
                sameCharactersCount++;
                if (sameCharactersCount == minLengthSequenceDna) {
                    sequenceCount++;
                    sameCharactersCount = 0;
                    if (sequenceCount >= sequenceToFound) {
                        return true;
                    }
                }
            } else {
                lastCharacter = currentCharacter;
                sameCharactersCount = 1;
            }
        }
        return false;
    }

    /**
     * Este metodo es usado para encontrar secuencias en una diagonal, dependiendo del punto de partida, direccion y si es la parte inferior o superior de la matriz.
     *
     * @param leftOrRight Direccion a buscar.
     * @param bottomOrTop Si se lee parte inferior o superior de la matriz.
     * @param baseRow fila inicial
     * @param baseColumn columna inicial
     * @return boolean, que determina si es o no Mutante
     */
    private boolean checkDiagonals(String leftOrRight, String bottomOrTop, int baseRow, int baseColumn) {
        int offset = 1;

        int sameCharactersCount = 1;

        char lastCharacter = dnaSequence[baseRow][baseColumn];
        char currentCharacter;

        boolean bottomCheckCondition = baseRow + offset < dnaSequence.length;

        boolean topCheckCondition = (leftOrRight.equals(LEFT) && baseColumn + offset < dnaSequence.length ||
                leftOrRight.equals(RIGHT) && baseColumn - offset >= 0);

        while ((bottomOrTop.equals(TOP_DIAGONAL) && topCheckCondition) ||
                (bottomOrTop.equals(BOTTOM_DIAGONAL) && bottomCheckCondition)) {

            currentCharacter = (leftOrRight.equals(LEFT)) ? dnaSequence[baseRow + offset][baseColumn + offset] :
                    dnaSequence[baseRow + offset][baseColumn - offset];
            if (lastCharacter == currentCharacter) {
                sameCharactersCount++;
                if (sameCharactersCount == minLengthSequenceDna) {
                    sequenceCount++;
                    sameCharactersCount = 0;
                    if (sequenceCount >= sequenceToFound) {
                        // If a find the minimum sequences to determine if it is Mutant, I finish the search.
                        return true;
                    }
                }
            } else {
                lastCharacter = currentCharacter;
                sameCharactersCount = 1;
            }

            offset++;

            bottomCheckCondition = baseRow + offset < dnaSequence.length;

            topCheckCondition = (leftOrRight.equals(LEFT) && baseColumn + offset < dnaSequence.length ||
                    leftOrRight.equals(RIGHT) && baseColumn - offset >= 0);
        }
        return false;
    }

}
