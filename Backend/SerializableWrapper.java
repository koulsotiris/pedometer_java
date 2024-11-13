import java.io.Serializable;

public class SerializableWrapper<T> implements Serializable {
    private T object;

    public SerializableWrapper(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}