# Cache

## References:
https://course.ccs.neu.edu/com3200/parent/NOTES/cache-basics.html
https://csillustrated.berkeley.edu/PDFs/handouts/cache-3-associativity-handout.pdf

## Requirements:
- 8 words per block
- 16 lines , Fully associative
- FIFO Eviction Strategy
- Write Through Cache
- Display Cache UI

UI Example:
- Tag is a reference to the address
```text
 Tag | Lines -->
 075   000750 000751 000752 000753 000754 000755 000756 000757
```

Code Sketch:
```java
// The CPU Cache is as follows:
Queue<CacheLine> = new LinkedList<>();

// Each CacheLine is an entry in a linked list using the following structure:
Pair<Integer, int[]> CacheLine
{
    Integer Key,  // Represents the Tag   (Memory address offset)
    int[8] Value  // Represents the Line  (Array of 8 32-bit integers, or 8 addresses from memory)
}
// As each element of the cache is accessed, the cache is updated.
// Stale elements are removed over time, oldest first.
```