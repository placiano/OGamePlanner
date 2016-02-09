package com.skiwi.ogameplanner.objects;

/**
 * @author Frank van Heeswijk
 */
public interface BuildOrder extends Iterable<GameObject> {
    void saveState();

    void restoreState();

    default void add(GameObject gameObject) {
        add(size(), gameObject);
    }

    void add(int index, GameObject gameObject);

    void moveBy(int index, int delta);

    GameObject get(int index);

    int size();
}
