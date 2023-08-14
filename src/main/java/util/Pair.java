package util;

public final class Pair<X, Y> {

    public final X x;
    public final Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Pair)) {
            return false;
        }
        final Pair<X, Y> other = (Pair<X, Y>) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.x.hashCode();
        hash = 53 * hash + this.y.hashCode();
        return hash;
//        return Objects.hash(x, y);
    }
}
