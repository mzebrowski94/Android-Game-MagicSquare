package com.uksw.mzebrowski.magicsquare.game;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uksw.mzebrowski.magicsquare.R;
import com.uksw.mzebrowski.magicsquare.consts.SquareBlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static com.uksw.mzebrowski.magicsquare.consts.SquareBlockType.EDIT_TEXT;
import static com.uksw.mzebrowski.magicsquare.consts.SquareBlockType.TEXT_VIEW;

public class MagicSquareEngine {

    private static int CONTENT_MATRIX_SIZE = 7;
    private static int SQUARE_MATRIX_SIZE = 3;
    private LayoutInflater inflater;
    private TextView contentMatrix[][];
    private EditText squaresMatrix[][];
    private int[][] resultMatrix;
    private final int squaresAmount;
    private int squaresCreated;
    private int[] rowsSum;
    private int[] colsSum;
    private TextWatcher editFieldWatcher;

    public MagicSquareEngine(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.squaresAmount = CONTENT_MATRIX_SIZE * CONTENT_MATRIX_SIZE;
        this.contentMatrix = new TextView[CONTENT_MATRIX_SIZE][CONTENT_MATRIX_SIZE];
        this.squaresMatrix = new EditText[SQUARE_MATRIX_SIZE][SQUARE_MATRIX_SIZE];
        this.squaresCreated = 0;
        this.initTextWatcher();
    }

    public void initMagicSquare(int level) {
        this.generateResultMatrix();
        this.initResultSums(resultMatrix);
        this.initMagicSquareMatrix();
        this.solveSquareMatrixByLevel(level);
    }

    public void resetMagicSquare(int level) {
        this.squaresCreated = 0;
        initMagicSquare(level);
    }

    private void solveSquareMatrixByLevel(int level) {
        int squaresToFill = (SQUARE_MATRIX_SIZE * SQUARE_MATRIX_SIZE) - level;
        for (int i = 0; i < squaresToFill; i++) {
            solveSingleSquare();
        }
    }

    public boolean solveSingleSquare() {
        boolean squareSolved = false;
        for (int i = 0; i < SQUARE_MATRIX_SIZE * SQUARE_MATRIX_SIZE; i++) {
            int x = new Random().nextInt(SQUARE_MATRIX_SIZE);
            int y = new Random().nextInt(SQUARE_MATRIX_SIZE);
            Editable retrievedValue = squaresMatrix[x][y].getText();

            if (!isSquareValueValid(retrievedValue)) {
                squaresMatrix[x][y].setText(String.valueOf(resultMatrix[x][y]));
                squareSolved = true;
                break;
            } else if (isSquareValueValid(retrievedValue) && Integer.valueOf(retrievedValue.toString()) != resultMatrix[x][y]) {
                squaresMatrix[x][y].setText(String.valueOf(resultMatrix[x][y]));
                squareSolved = true;
                break;
            }
        }
        return squareSolved;
    }

    //
//    public void solveSingleSquare() {
//        int rangeFrom = 0;
//        int rangeTo = SQUARE_MATRIX_SIZE - 1;
//        Queue<Integer> xNumbers = generateShuffledSquaresOrder(rangeFrom, rangeTo);
//        Queue<Integer> yNumbers = generateShuffledSquaresOrder(rangeFrom, rangeTo);
//
//        for (int i = 0; i < SQUARE_MATRIX_SIZE * SQUARE_MATRIX_SIZE; i++) {
//            int x = xNumbers.remove();
//            int y = yNumbers.remove();
//            Editable retrievedValue = squaresMatrix[x][y].getText();
//
//            if (!isSquareValueValid(retrievedValue)) {
//                squaresMatrix[x][y].setText(String.valueOf(resultMatrix[x][y]));
//                break;
//            } else if (isSquareValueValid(retrievedValue) && Integer.valueOf(retrievedValue.toString()) != resultMatrix[x][y]) {
//                squaresMatrix[x][y].setText(String.valueOf(resultMatrix[x][y]));
//                break;
//            }
//        }
//    }

//    private LinkedList<Integer> generateShuffledSquaresOrder(int from, int to) {
//        ArrayList<Integer> numbers = new ArrayList<>();
//
//        for (int i = from; i < to; i++) {
//            numbers.add(i);
//        }
//
//        Collections.shuffle(numbers);
//        return new LinkedList<>(numbers);
//    }

    private void initResultSums(int[][] resultMatrix) {
        rowsSum = new int[SQUARE_MATRIX_SIZE];
        colsSum = new int[SQUARE_MATRIX_SIZE];
        for (int i = 0; i < SQUARE_MATRIX_SIZE; i++) {
            for (int j = 0; j < SQUARE_MATRIX_SIZE; j++) {
                rowsSum[i] += resultMatrix[i][j];
                colsSum[j] += resultMatrix[i][j];
            }
        }
    }

    private LinkedList<Integer> generateShuffledSquaresOrder() {
        ArrayList<Integer> numbers = new ArrayList<>();

        int numbersAmount = SQUARE_MATRIX_SIZE * SQUARE_MATRIX_SIZE + 1;
        for (int i = 1; i < numbersAmount; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return new LinkedList<>(numbers);
    }

    private void generateResultMatrix() {
        int resultMatrix[][] = new int[SQUARE_MATRIX_SIZE][SQUARE_MATRIX_SIZE];
        Queue<Integer> numbersQueue;
        numbersQueue = generateShuffledSquaresOrder();

        for (int i = 0; i < SQUARE_MATRIX_SIZE; i++) {
            for (int j = 0; j < SQUARE_MATRIX_SIZE; j++) {
                resultMatrix[i][j] = numbersQueue.remove();
            }
        }
        this.resultMatrix = resultMatrix;
    }

    private void initMagicSquareMatrix() {

        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            for (int j = 0; j < CONTENT_MATRIX_SIZE; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    if (i == CONTENT_MATRIX_SIZE - 1 && j == CONTENT_MATRIX_SIZE - 1) {
                        addItem(i, j, TEXT_VIEW, "");
                    } else if (i == CONTENT_MATRIX_SIZE - 1) {
                        addItem(i, j, TEXT_VIEW, String.valueOf(colsSum[j / 2]));
                    } else if (j == CONTENT_MATRIX_SIZE - 1) {
                        addItem(i, j, TEXT_VIEW, String.valueOf(rowsSum[i / 2]));
                    } else {
                        EditText square = (EditText) addItem(i, j, EDIT_TEXT, "?");
                        squaresMatrix[i / 2][j / 2] = square;
                    }
                } else if (i % 2 == 1 && j % 2 == 1) {
                    addItem(i, j, TEXT_VIEW, "");
                } else {
                    if ((i == CONTENT_MATRIX_SIZE - 2 && j != CONTENT_MATRIX_SIZE - 1) || j == CONTENT_MATRIX_SIZE - 2 && i != CONTENT_MATRIX_SIZE - 1) {
                        addItem(i, j, TEXT_VIEW, "=");
                    } else if (i == CONTENT_MATRIX_SIZE - 1 || j == CONTENT_MATRIX_SIZE - 1) {
                        addItem(i, j, TEXT_VIEW, "");
                    } else {
                        addItem(i, j, TEXT_VIEW, "+");
                    }
                }
            }
        }
    }

    private void initTextWatcher() {
        editFieldWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkMagicSquareResolve();
            }
        };
    }

    public boolean checkMagicSquareResolve() {
        int columnCalculatedSum[] = new int[CONTENT_MATRIX_SIZE];
        int rowCalculatedSum[] = new int[CONTENT_MATRIX_SIZE];
        boolean isResolved = true;

        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            for (int j = 0; j < CONTENT_MATRIX_SIZE; j++) {
                if (i % 2 == 0 && contentMatrix[i][j] instanceof EditText
                        && isSquareValueValid(contentMatrix[i][j].getText())) {
                    int squareValue = Integer.parseInt(contentMatrix[i][j].getText().toString());
                    rowCalculatedSum[i] += squareValue;
                    columnCalculatedSum[j] += squareValue;
                }
            }
        }


        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            if (i % 2 == 0 && i != CONTENT_MATRIX_SIZE - 1) {
                if (rowsSum[i / 2] == rowCalculatedSum[i]) {
                    contentMatrix[i][CONTENT_MATRIX_SIZE - 1].setBackgroundColor(Color.parseColor("#92ff7c"));
                } else {
                    contentMatrix[i][CONTENT_MATRIX_SIZE - 1].setBackgroundColor(Color.parseColor("#e8beb7"));
                    isResolved = false;
                }

                if (colsSum[i / 2] == columnCalculatedSum[i]) {
                    contentMatrix[CONTENT_MATRIX_SIZE - 1][i].setBackgroundColor(Color.parseColor("#92ff7c"));
                } else {
                    contentMatrix[CONTENT_MATRIX_SIZE - 1][i].setBackgroundColor(Color.parseColor("#e8beb7"));
                    isResolved = false;
                }
            }
        }
        return isResolved;
    }

    private TextView addItem(int x, int y, SquareBlockType squareBlockType, String text) {
        TextView view;
        if ((squaresCreated != squaresAmount) &&
                (x >= 0 && x < CONTENT_MATRIX_SIZE) &&
                (y >= 0 && y < CONTENT_MATRIX_SIZE)) {
            view = createSquare(squareBlockType, text);
            contentMatrix[x][y] = view;
            squaresCreated++;
            return view;
        }
        return null;
    }

    private boolean isSquareValueValid(CharSequence sequence) {
        String text = sequence.toString();
        return !text.isEmpty() && android.text.TextUtils.isDigitsOnly(text);
    }

    private TextView createSquare(SquareBlockType squareBlockType, String text) {
        TextView view = null;
        View convertView;
        switch (squareBlockType) {
            case TEXT_VIEW:
                convertView = inflater.inflate(R.layout.squareblock_layout_tv, null);
                view = convertView.findViewById(R.id.squareblock_tv);
                view.setText(text);
                break;
            case EDIT_TEXT:
                convertView = inflater.inflate(R.layout.squareblock_layout_et, null);
                view = (EditText) convertView.findViewById(R.id.squareblock_et);
                view.setText(text);
                view.addTextChangedListener(editFieldWatcher);
                break;
        }
        return view;
    }

    public ArrayList<View> getAllSquares() {
        ArrayList<View> squares = new ArrayList<>();

        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            squares.addAll(Arrays.asList(contentMatrix[i]).subList(0, CONTENT_MATRIX_SIZE));
        }
        return squares;
    }

    public ArrayList<String> getSquaresMatrixContent() {
        ArrayList<String> matrixContent = new ArrayList<>();
        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            for (int j = 0; j < CONTENT_MATRIX_SIZE; j++) {
                matrixContent.add(contentMatrix[i][j].getText().toString());
            }
        }
        return matrixContent;
    }

    public void loadSquaresMatrixContent(ArrayList<String> matrixContent) {
        Queue<String> queue = new LinkedList<>(matrixContent);
        for (int i = 0; i < CONTENT_MATRIX_SIZE; i++) {
            for (int j = 0; j < CONTENT_MATRIX_SIZE; j++) {
                contentMatrix[i][j].setText(queue.remove());
            }
        }
    }
}
