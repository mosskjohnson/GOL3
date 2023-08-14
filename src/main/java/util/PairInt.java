package util;

public final class PairInt {

    public final int x;
    public final int y;

    public PairInt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PairInt)) {
            return false;
        }
        final PairInt other = (PairInt) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
//        return Objects.hash(x, y);
    }
}
