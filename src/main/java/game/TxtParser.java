package game;


import util.PairInt;
import util.UtilFile;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.IntStream;

public class TxtParser {

    public static final int MAX_DIGITS_FOR_WIDTH_OR_HEIGHT = 8;
    public static final int RADIX = 10;

    public static void main(String[] args) throws IOException, UnexpectedCharException, URISyntaxException {
        Blueprint b = blueprintFromTxt("ZZZtestIn.txt");

        bluePrintToTxt(b, "ZZZtestOut.txt");
    }

    public static void bluePrintToTxt(Blueprint blueprint, String filename) throws IOException, URISyntaxException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        writeInt(out, blueprint.WIDTH);
        out.write(',');
        writeInt(out, blueprint.HEIGHT);
        out.write(';');

        out.write('\r');
        out.write('\n');

        for (PairInt p : blueprint.ALIVE_CELLS_RELATIVE) {
            out.write('[');
            writeInt(out, p.x);
            out.write(',');
            writeInt(out, p.y);
            out.write(']');
        }
        out.write(';');

        out.close();
        UtilFile.writeUserDataFile(out.toByteArray(), filename);
    }

    public static Blueprint blueprintFromTxt(String filename) throws IOException, UnexpectedCharException {

        /*
        Files are assumed to be in this format:

        width,height;
        [x0,y0][x1,y1][x2,y2]...;

        first row: dimensions of blueprint, width or height must have digits less than or equal to MAX_DIGITS_FOR_WIDTH_OR_HEIGHT.
        second row: pairs of active cells relative to the blueprint, x cannot be greater than width-1 and y cannot be greater than height-1.
         */

        InputStream inputStream = UtilFile.readUserDataFile(filename);
        InputStreamReader reader = new InputStreamReader(inputStream);

        char c;

        // width and height
        int width = parseInt(reader, MAX_DIGITS_FOR_WIDTH_OR_HEIGHT, ',');
        int height = parseInt(reader, MAX_DIGITS_FOR_WIDTH_OR_HEIGHT, ';');

        // new line
        c = (char) reader.read();
        if (c != '\r') {
            throw new UnexpectedCharException('\r', c);
        }
        c = (char) reader.read();
        if (c != '\n') {
            throw new UnexpectedCharException('\n', c);
        }


        // alive cells
        LinkedHashSet<PairInt> aliveCellsRelative = new LinkedHashSet<>();
        while (true) {
            int x;
            int y;

            c = (char) reader.read();
            if (c != '[') {
                if (c == ';') {
                    break; // semicolon found, end
                } else {
                    throw new UnexpectedCharException('[', c);
                }
            }

            x = parseInt(reader, MAX_DIGITS_FOR_WIDTH_OR_HEIGHT, ',');
            y = parseInt(reader, MAX_DIGITS_FOR_WIDTH_OR_HEIGHT, ']');

            if (x > width - 1 || y > height - 1) {
                throw new IOException("x and y of a active cell cannot be greater than width-1 and height-1 respectively");
            }
            aliveCellsRelative.add(new PairInt(x, y));
        }

        System.out.println("width: " + width + ", height: " + height + ", aliveCellsRelative" + aliveCellsRelative);
        reader.close();
        return new Blueprint(width, height, aliveCellsRelative);
    }

    private static int parseInt(InputStreamReader reader, int maxDigits, char stopChar) throws UnexpectedCharException, IOException {
        ArrayList<Integer> digits = new ArrayList<>();
        int output = 0;
        char c;

        for (int i = 0; i < maxDigits; i++) {
            c = (char) reader.read();
            if (!Character.isDigit(c)) {
                if (c == stopChar) {
                    break; // semicolon found, end
                }
                throw new UnexpectedCharException(stopChar, c);
            }
            digits.add(Character.getNumericValue(c));
        }
        for (int i = 0; i < digits.size(); i++) {
            output += digits.get(digits.size() - 1 - i) * Math.pow(10, i);
        }
        return output;
    }

    private static void writeInt(ByteArrayOutputStream out, int i) {
        String number = String.valueOf(i);
        IntStream chars = number.chars();
        chars.forEach(out::write);
    }
}
