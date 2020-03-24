package ru.itmo.java;

public class HashTable {

    private static final int DEFAULT_SIZE = 25;
    private static final double DEFAULT_LOAD_FACTOR = .5;
    private double loadFactor;
    private int size;
    private Entry[] table;

    public HashTable() {
        this(DEFAULT_SIZE);
    }

    public HashTable(int size) {
        loadFactor = DEFAULT_LOAD_FACTOR;
        this.size = 0;
        table = new Entry[size];
    }

    public HashTable(int size, double loadFactor) {
        this(size);
        this.loadFactor = loadFactor;
    }

    Object put(Object key, Object value) {
        resizeIfNeeded();
        Entry entry = new Entry(key, value);
        int ind = getInd(key.hashCode());
        while (table[ind] != null && !table[ind].key.equals(key)) {
            ind = nextInd(ind);
        }
        Entry oldValue = table[ind];
        table[ind] = entry;
        if (oldValue == null) {
            size++;
            return null;
        }
        return oldValue.value;
    }


    Object get(Object key) {
        int ind = getInd(key.hashCode());
        while (table[ind] != null) {
            if (table[ind].key.equals(key))
                return table[ind].value;
            ind = nextInd(ind);
        }
        if (table[ind] == null) return null;
        return table[ind].value;
    }


    Object remove(Object key) {
        int ind = getInd(key.hashCode());
        while (table[ind] != null && !table[ind].key.equals(key)) {
            ind = nextInd(ind);
        }
        Entry oldValue = table[ind];
        if (oldValue == null)
            return null;
        table[ind] = null;
        int i = nextInd(ind);
        while (table[i] != null) {
            int indImg = getInd(table[i].hash);
            if (checkNeedMoveElement(ind, i, indImg)) {
                table[ind] = table[i];
                table[i] = null;
                ind = i;
            }
            i = nextInd(i);
        }
        size--;
        return oldValue.value;
    }

    int size() {
        return size;
    }

    private boolean checkNeedMoveElement(int ind, int i, int indImg) {
        return (indImg <= ind && i > ind) || (indImg <= ind && (i < ind && indImg > i)) || (i > ind && indImg > i);
    }

    private int nextInd(int ind) {
        return (ind + 1) % table.length;
    }

    private int getInd(int hash) {
        int tableLength = table.length;
        return (hash % tableLength + tableLength) % tableLength;
    }

    private void resizeIfNeeded() {
        if (size + 1 < table.length * loadFactor) {
            return;
        }
        size = 0;
        Entry[] save = table;
        table = new Entry[table.length * 2];
        for (Entry entry : save) {
            if (entry == null) {
                continue;
            }
            put(entry.key, entry.value);
        }
    }

    private class Entry {
        final Object key;
        Object value;
        int hash;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }

    }
}
