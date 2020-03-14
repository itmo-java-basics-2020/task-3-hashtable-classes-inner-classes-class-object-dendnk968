package ru.itmo.java;

public class HashTable {

    private double loadFactor;
    private int size;
    private Entry[] table;

    public HashTable() {
        this(25);
    }

    public HashTable(int size) {
        loadFactor = .5;
        this.size = 0;
        table = new Entry[size];
    }

    public HashTable(int size, double loadFactor) {
        this(size);
        this.loadFactor = loadFactor;
    }

    Object put(Object key, Object value) {
        if (size + 1 >= table.length * loadFactor) {
            resize();
        }
        Entry entry = new Entry(key, value);
        int ind = getInd(key.hashCode());
        while (table[ind] != null && !table[ind].key.equals(key)) {
            ind = nextInd(ind);
        }
        Entry old_value = table[ind];
        table[ind] = entry;
        if (old_value == null) {
            size++;
            return null;
        }
        return old_value.value;
    }

    private void resize() {
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
            if ((indImg <= ind && i > ind) ||
                    (indImg <= ind && (i < ind && indImg > i)) || (i > ind && indImg > i)) {
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

    private int nextInd(int ind) {
        return (ind + 1) % table.length;
    }

    private int getInd(int hash) {
        return (hash % table.length + table.length) % table.length;
    }

    private class Entry {
        Object key, value;
        int hash;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }

    }
}
