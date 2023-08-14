package game;

import util.PairInt;

import java.util.LinkedHashSet;

public class Blueprint {

    public final int WIDTH;
    public final int HEIGHT;
    public final LinkedHashSet<PairInt> ALIVE_CELLS_RELATIVE;

    public Blueprint(int width, int height, LinkedHashSet<PairInt> aliveCellsRelative) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be greater than zero");
        }
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ALIVE_CELLS_RELATIVE = aliveCellsRelative;
    }
}
