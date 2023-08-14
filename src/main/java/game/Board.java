package game;

import util.PairInt;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Board {

    private LinkedHashSet<PairInt> aliveCells;

    public Board() {
        aliveCells = new LinkedHashSet<>();
    }

    public LinkedHashSet<PairInt> getAliveCells() {
        return aliveCells;
    }

    public void clearAliveCells() {
        aliveCells.clear();
    }

    public boolean isAlive(int x, int y) {
        return aliveCells.contains(new PairInt(x, y));
    }

    public void addAliveCell(int x, int y) {
        aliveCells.add(new PairInt(x, y));
    }

    public void removeAliveCell(int x, int y) {
        aliveCells.remove(new PairInt(x, y));
    }

    public void toggleAliveCell(int x, int y) {
        PairInt cell = new PairInt(x, y);
        if (aliveCells.contains(cell)) {
            aliveCells.remove(cell);
        } else {
            aliveCells.add(cell);
        }
    }

    public void applyBlueprint(Blueprint b, int x, int y) {
        for (PairInt p : b.ALIVE_CELLS_RELATIVE) {
            addAliveCell(x + p.x, y + p.y);
        }
    }

    public Blueprint createBlueprint(int sx, int sy, int width, int height) {
        LinkedHashSet<PairInt> aliveCellsRelative = new LinkedHashSet<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isAlive(x + sx, y + sy)) {
                    aliveCellsRelative.add(new PairInt(x, y));
                }
            }
        }
        return new Blueprint(width, height, aliveCellsRelative);
    }

    public void step() {

        ArrayList<PairInt> births = new ArrayList<>();
        ArrayList<PairInt> deaths = new ArrayList<>();

        LinkedHashSet<PairInt> deadCellsThatBorderAliveCells = new LinkedHashSet<>();

        for (PairInt aliveCell : aliveCells) {
            int numberOfAliveNeighbors = calculateNumberOfAliveNeighbors(aliveCell.x, aliveCell.y);
            if (numberOfAliveNeighbors != 2 && numberOfAliveNeighbors != 3) {
                deaths.add(new PairInt(aliveCell.x, aliveCell.y));
            }
            deadCellsThatBorderAliveCells = findDeadCellsAndAddThemToSet(aliveCell, deadCellsThatBorderAliveCells);
        }
        for (PairInt deadCell : deadCellsThatBorderAliveCells) {
            int numberOfAliveNeighbors = calculateNumberOfAliveNeighbors(deadCell.x, deadCell.y);
            if (numberOfAliveNeighbors == 3) {
                births.add(deadCell);
            }
        }
        applyBirthsAndDeaths(births, deaths);
    }

    private int calculateNumberOfAliveNeighbors(int x, int y) {
        int sum = 0;
        for (int nx = x-1; nx < x+2; nx++) {
            for (int ny = y-1; ny < y+2; ny++) {
                if (nx == x && ny == y) {
                    continue;
                }
                if (aliveCells.contains(new PairInt(nx, ny))) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private LinkedHashSet<PairInt> findDeadCellsAndAddThemToSet(PairInt aliveCell, LinkedHashSet<PairInt> deadCellsThatBorderAliveCells) {
        for (int nx = aliveCell.x-1; nx < aliveCell.x+2; nx++) {
            for (int ny = aliveCell.y-1; ny < aliveCell.y+2; ny++) {
                if (nx == aliveCell.x && ny == aliveCell.y) {
                    continue;
                }
                PairInt cell = new PairInt(nx, ny);
                if (!aliveCells.contains(cell)) {
                    deadCellsThatBorderAliveCells.add(cell);
                }
            }
        }
        return deadCellsThatBorderAliveCells;
    }

    private void applyBirthsAndDeaths(ArrayList<PairInt> births, ArrayList<PairInt> deaths) {
//        System.out.println(births.size() + " | "+ deaths.size());
        for (PairInt birth : births) {
            aliveCells.add(birth);
        }
        for (PairInt death : deaths) {
            aliveCells.remove(death);
        }
    }
}
