import java.io.Serializable;

public class Pair<F, S> implements Serializable{
    private final F first;
    private final S second;

    Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getKey() {
        return first;
    }

    public S getValue() {
        return second;
    }
}