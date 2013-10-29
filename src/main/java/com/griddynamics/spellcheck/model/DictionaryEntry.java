package com.griddynamics.spellcheck.model;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class DictionaryEntry {

    private final String entry;
    private final int id;

    public DictionaryEntry(final String entry, final int id) {
        this.entry = entry;
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DictionaryEntry)) return false;

        final DictionaryEntry that = (DictionaryEntry) o;

        if (id != that.id) return false;
        if (entry != null ? !entry.equals(that.entry) : that.entry != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entry != null ? entry.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "DictionaryEntry{" +
                "entry='" + entry + '\'' +
                ", id=" + id +
                '}';
    }
}
