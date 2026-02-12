public class SimpleHashTable{
    static class Entry {
        String key;
        String value;
        Entry next;
        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] buckets;
    private int capacity = 10;
    private int size = 0;
    private final double LOAD_FACTOR_THRESHOLD = 0.75;

    public SimpleHashTable() {
        buckets = new Entry[capacity];
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(String key, String value) {
        // Check if we need to resize before adding
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = hash(key);
        Entry current = buckets[index];

        // 1. Check for duplicate keys to update the value
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // 2. No duplicate found: add new entry at the front (chaining)
        Entry newEntry = new Entry(key, value);
        newEntry.next = buckets[index];
        buckets[index] = newEntry;
        size++;
    }

    public String get(String key) {
        int index = hash(key);
        Entry current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry[] oldBuckets = buckets;
        buckets = new Entry[capacity];
        size = 0; // Reset size and re-add entries

        for (int i = 0; i < oldCapacity; i++) {
            Entry current = oldBuckets[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        System.out.println("Resized table to capacity: " + capacity);
    }

    public static void main(String[] args) {
        SimpleHashTable phoneBook = new SimpleHashTable();
        phoneBook.put("Alice", "555-0101");
        phoneBook.put("Alice", "555-9999"); // Correctly updates Alice
        phoneBook.put("Bob", "555-0102");

        System.out.println("Alice: " + phoneBook.get("Alice"));
        System.out.println("Bob: " + phoneBook.get("Bob"));
    }
}
