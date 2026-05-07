# LRU Cache — Complete Reference Guide

---

## Table of Contents

1. [The Problem](#1-the-problem)
2. [The Insight — Why DLL + HashMap](#2-the-insight--why-dll--hashmap)
3. [Data Structure Anatomy](#3-data-structure-anatomy)
4. [Sentinel Nodes — The Key Trick](#4-sentinel-nodes--the-key-trick)
5. [Core Operations](#5-core-operations)
6. [Dry Run — Step by Step](#6-dry-run--step-by-step)
7. [Kotlin Solution — Line by Line](#7-kotlin-solution--line-by-line)
8. [C++ Solution — Line by Line](#8-c-solution--line-by-line)
9. [Kotlin vs C++ — Side by Side](#9-kotlin-vs-c--side-by-side)
10. [Complexity Analysis](#10-complexity-analysis)
11. [Common Mistakes](#11-common-mistakes)
12. [Decision Guide](#12-decision-guide)

---

## 1. The Problem

LeetCode 146. You need a cache with a fixed capacity that:

- **`get(key)`** — returns the value if it exists, `-1` otherwise
- **`put(key, value)`** — inserts or updates. If at capacity, **evict the least recently used** entry first

"Recently used" means: any `get` or `put` on a key counts as a use. The entry that went the longest without any access gets evicted.

**Example:**

```
LRUCache(2)        // capacity = 2

put(1, 1)          // cache: {1=1}
put(2, 2)          // cache: {1=1, 2=2}
get(1)     → 1     // cache: {2=2, 1=1}   ← 1 is now most recent
put(3, 3)          // evict 2 (LRU), cache: {1=1, 3=3}
get(2)     → -1    // 2 was evicted
```

Both operations must be **O(1)**. That constraint is the whole puzzle.

---

## 2. The Insight — Why DLL + HashMap

You need two things simultaneously:

| Need | Data Structure |
|------|---------------|
| O(1) lookup by key | HashMap |
| O(1) ordered eviction (track recency) | Doubly Linked List |

A singly linked list won't work — you can't remove a node in O(1) without the previous pointer.  
An array won't work — shifting is O(n).  
A queue alone won't work — you can't reorder an arbitrary element in O(1).

The DLL gives you O(1) insert, O(1) remove (given the node pointer), and O(1) access to both ends.  
The HashMap maps key → node pointer, so you jump straight to the right node — no traversal.

**Together:** HashMap finds the node instantly; DLL reorders it instantly. That's the entire trick.

```
HashMap:  key → Node*
DLL:      [head] ↔ [LRU] ↔ [... ] ↔ [MRU] ↔ [tail]
                   evict                 promote
```

---

## 3. Data Structure Anatomy

```
class Node:
    key    — needed so we can erase from the map when we evict
    value  — the cached value
    prev   — pointer to previous node in DLL
    next   — pointer to next node in DLL
```

Why does the node store `key`? Because when you evict the LRU node from the front of the DLL, you need to also remove it from the HashMap. You only have the node — no key anywhere else — so the node must carry its own key.

```
class LRUCache:
    capacity  — max entries
    map       — HashMap<Int, Node>
    head      — sentinel (dummy) — left end, LRU side
    tail      — sentinel (dummy) — right end, MRU side
```

Convention used throughout both solutions:

```
LEFT  = head ↔ ... ↔ LRU node       ← evict from here
RIGHT = MRU node ↔ ... ↔ tail       ← insert/promote to here
```

Most recently used always lives just before `tail`. Least recently used always lives just after `head`.

---

## 4. Sentinel Nodes — The Key Trick

Without sentinels, every DLL operation requires null checks:

```kotlin
// Without sentinels — messy
fun insertBeforeTail(node: Node) {
    if (tail.prev == null) {
        head.next = node
        node.prev = null  // ← what even is this?
    } else { ... }
}
```

With sentinels, `head` and `tail` are permanent dummy nodes that never hold real data. The list always looks like:

```
[head] ↔ [real nodes...] ↔ [tail]
```

Now every insertion is identical — there's always a `tail.prev` to attach to. Every removal is identical — every real node always has a valid `prev` and `next`. No special cases, no null checks, no edge case for empty list.

```
Initial state (empty cache):

  head ↔ tail
  
After put(1, 10):

  head ↔ Node(1,10) ↔ tail
  
After put(2, 20):

  head ↔ Node(1,10) ↔ Node(2,20) ↔ tail
```

`head.prev` and `tail.next` are never used. They exist just to complete the struct.

---

## 5. Core Operations

There are only three internal DLL operations. Everything else composes from these.

### `unlink(node)`
Remove a node from wherever it is in the list. Stitch its neighbors together.

```
Before:  ... ↔ [A] ↔ [node] ↔ [B] ↔ ...
After:   ... ↔ [A] ↔ [B] ↔ ...

node.prev.next = node.next   // A now points to B
node.next.prev = node.prev   // B now points back to A
```

The node itself still has stale `prev`/`next` pointers — that's fine, it's either about to be inserted elsewhere or deleted.

### `insertBeforeTail(node)`
Attach a node as the new MRU (just before tail).

```
Before:  ... ↔ [prev] ↔ tail
After:   ... ↔ [prev] ↔ [node] ↔ tail

prev.next = node
node.prev = prev
node.next = tail
tail.prev = node
```

Always 4 pointer assignments. Always the same shape regardless of list size.

### `moveToTail(node)`
Mark a node as most recently used.

```kotlin
unlink(node)
insertBeforeTail(node)
```

That's it. This is what happens on every cache hit (`get`) and every update (`put` on existing key).

---

## 6. Dry Run — Step by Step

```
LRUCache(capacity = 2)

Initial:  head ↔ tail     map: {}
```

---

**`put(1, 10)`**

- Key 1 not in map → no eviction needed (size 0 < 2)
- Create Node(1, 10), insertBeforeTail
- map: {1 → Node(1,10)}

```
head ↔ [1:10] ↔ tail
         LRU     MRU
```

---

**`put(2, 20)`**

- Key 2 not in map → no eviction (size 1 < 2)
- Create Node(2, 20), insertBeforeTail
- map: {1 → Node(1,10), 2 → Node(2,20)}

```
head ↔ [1:10] ↔ [2:20] ↔ tail
         LRU              MRU
```

---

**`get(1)` → returns 10**

- Key 1 found in map → Node(1,10)
- moveToTail(Node(1,10)):
  - unlink: head ↔ [2:20] ↔ tail
  - insertBeforeTail: head ↔ [2:20] ↔ [1:10] ↔ tail
- return 10

```
head ↔ [2:20] ↔ [1:10] ↔ tail
         LRU              MRU   ← 1 is now most recent
```

---

**`put(3, 30)`** — capacity hit!

- Key 3 not in map
- map.size == capacity (2 == 2) → must evict
- LRU = head.next = Node(2, 20)
- unlink Node(2,20), map.remove(2)
- Create Node(3, 30), insertBeforeTail
- map: {1 → Node(1,10), 3 → Node(3,30)}

```
head ↔ [1:10] ↔ [3:30] ↔ tail
         LRU              MRU
```

---

**`get(2)` → returns -1**

- Key 2 not in map → return -1

---

**`get(1)` → returns 10**

- Node(1,10) found, moveToTail

```
head ↔ [3:30] ↔ [1:10] ↔ tail
```

---

## 7. Kotlin Solution — Line by Line

```kotlin
class LRUCache(private val capacity: Int) {
```
The class owns capacity, the map, and the DLL directly. No wrapper, no inner class.

```kotlin
    private class Node(val key: Int, var value: Int) {
        var prev: Node? = null
        var next: Node? = null
    }
```
`key` is `val` — it never changes once a node is created.  
`value` is `var` — it gets updated on repeated `put(key, newValue)`.  
`prev`/`next` are nullable, initialized to null. Sentinels are connected in `init`.

```kotlin
    private val map = HashMap<Int, Node>(capacity)
```
Pre-sized to `capacity` — avoids rehashing since we know the map will never exceed this size.

```kotlin
    private val head = Node(-1, -1)
    private val tail = Node(-1, -1)
```
Sentinel nodes. `-1` is just a placeholder; these values are never read.  
Both are `val` — the sentinel references are fixed, but their internal pointers (`prev`/`next`) are mutated freely.

```kotlin
    init {
        head.next = tail
        tail.prev = head
    }
```
Wire up the empty list. After this, the invariant holds: the list always starts at `head` and ends at `tail`.

---

```kotlin
    fun get(key: Int): Int {
        val node = map[key] ?: return -1
```
`map[key]` returns `Node?`. The `?: return -1` is Elvis — if null, return immediately. Clean early exit.

```kotlin
        moveToTail(node)
        return node.value
    }
```
On hit: promote to MRU, return value. Two lines, two responsibilities.

---

```kotlin
    fun put(key: Int, value: Int) {
        map[key]?.let {
            it.value = value
            moveToTail(it)
            return
        }
```
`map[key]?.let { ... }` — if key exists, execute the lambda with the node as `it`.  
Update value, promote to MRU, early return. The `return` exits `put` entirely (not just the lambda — this works because `let` is an inline function in Kotlin).

```kotlin
        if (map.size == capacity) {
            val lru = head.next!!
            unlink(lru)
            map.remove(lru.key)
        }
```
New key, check capacity. `head.next!!` is safe — the sentinel invariant guarantees `head.next` is never null (worst case it's `tail`; we check `map.size == capacity` first so the list has at least one real node).  
Order matters: `unlink` first, then `map.remove`. Node's key is read during remove — unlink doesn't destroy it.

```kotlin
        val node = Node(key, value).also { insertBeforeTail(it) }
        map[key] = node
    }
```
`also` runs a side effect (`insertBeforeTail`) and returns the original object. One expression: create node, insert it, assign to `node`. Then register in map.

---

```kotlin
    private fun unlink(node: Node) {
        node.prev!!.next = node.next
        node.next!!.prev = node.prev
    }
```
`!!` is safe here by sentinel invariant — every real node always has non-null `prev` and `next`.  
Two assignments. Stitches neighbors together, leaves `node.prev`/`node.next` stale (doesn't matter).

```kotlin
    private fun insertBeforeTail(node: Node) {
        val prev = tail.prev!!
        prev.next = node
        node.prev = prev
        node.next = tail
        tail.prev = node
    }
```
`tail.prev!!` is safe — tail always has a prev (at minimum, head).  
Capture `prev` first — it's the current last real node (or `head` if empty).  
Then four pointer assignments to splice `node` in between `prev` and `tail`.

```kotlin
    private fun moveToTail(node: Node) {
        unlink(node)
        insertBeforeTail(node)
    }
```
Composed from the two primitives. Works even when the node is already at the tail — unlink removes it, insertBeforeTail puts it back at the end.

---

## 8. C++ Solution — Line by Line

```cpp
struct Node {
    int key, value;
    Node* prev = nullptr;
    Node* next = nullptr;
    Node(int k, int v) : key(k), value(v) {}
};
```
Same shape as Kotlin. In-class default initialization (`= nullptr`) means the constructor body is empty. Member initializer list (`: key(k), value(v)`) is idiomatic C++ — avoids double-assignment.

```cpp
const int capacity_;
unordered_map<int, Node*> map_;
Node head_{-1, -1};
Node tail_{-1, -1};
```
`unordered_map<int, Node*>` — map to raw pointer. The map doesn't own the nodes; the cache does.  
`head_` and `tail_` are value members (stack-allocated inside the object), not heap-allocated. Better cache locality, one fewer allocation.  
`{-1, -1}` uses brace-initialization to call `Node(-1, -1)`.

```cpp
LRUCache(int capacity) : capacity_(capacity) {
    head_.next = &tail_;
    tail_.prev = &head_;
    map_.reserve(capacity);
}
```
Member initializer list sets `capacity_` before the body runs.  
`&tail_` / `&head_` — address-of the value members. Safe as long as the `LRUCache` object isn't moved (which is fine here).  
`map_.reserve(capacity)` pre-allocates bucket array — same reasoning as Kotlin's pre-sized HashMap.

```cpp
~LRUCache() {
    for (auto& [_, node] : map_) delete node;
}
```
Structured binding (`auto& [_, node]`) ignores the key (`_`) and deletes each node pointer.  
Without this, every `Node* node = new Node(...)` leaks. In LeetCode's judge this doesn't matter, but it's correct code.  
Alternative: use `unique_ptr<Node>` in the map — but then you must `.get()` everywhere and the code gets noisier for no real gain.

```cpp
int get(int key) {
    auto it = map_.find(key);
    if (it == map_.end()) return -1;
    moveToTail(it->second);
    return it->second->value;
}
```
`map_.find(key)` returns an iterator. `map_.end()` is the "not found" sentinel.  
`it->second` is the `Node*`. Calling `moveToTail` before reading `value` — order doesn't matter here, but it's consistent with "update recency, then serve".

```cpp
void put(int key, int value) {
    if (auto it = map_.find(key); it != map_.end()) {
        it->second->value = value;
        moveToTail(it->second);
        return;
    }
```
C++17 `if` with initializer — `auto it = ...` scopes `it` to the `if` block only. Cleaner than declaring it outside.  
Same logic as Kotlin's `?.let` — find, update, promote, return early.

```cpp
    if (static_cast<int>(map_.size()) == capacity_) {
        Node* lru = head_.next;
        map_.erase(lru->key);
        unlink(lru);
        delete lru;
    }
```
`static_cast<int>` — `map_.size()` returns `size_t` (unsigned). Comparing signed/unsigned causes a warning; cast silences it correctly.  
`map_.erase` before `delete` — after `delete lru`, accessing `lru->key` is undefined behavior. Erase first.  
Note: unlike Kotlin, here we `unlink` after `erase`. Safe because `unlink` only touches the node's prev/next pointers, not the key.

```cpp
    Node* node = new Node(key, value);
    insertBeforeTail(node);
    map_[key] = node;
}
```
Three lines: allocate, link into DLL, register in map. Straightforward.

```cpp
void unlink(Node* n) {
    n->prev->next = n->next;
    n->next->prev = n->prev;
}
```
Identical logic to Kotlin. Arrow operator (`->`) dereferences pointer then accesses member.

```cpp
void insertBeforeTail(Node* n) {
    Node* prev = tail_.prev;
    prev->next = n;
    n->prev    = prev;
    n->next    = &tail_;
    tail_.prev = n;
}
```
`&tail_` — address of the stack-allocated sentinel. Same semantics as Kotlin's `tail` reference.

```cpp
void moveToTail(Node* n) {
    unlink(n);
    insertBeforeTail(n);
}
```
Identical composition to Kotlin.

---

## 9. Kotlin vs C++ — Side by Side

| Aspect | Kotlin | C++ |
|--------|--------|-----|
| Node ownership | GC handles it | Manual `new`/`delete` |
| Null safety | `!!` with sentinel guarantee | Raw pointers, same guarantee |
| Map type | `HashMap<Int, Node>` | `unordered_map<int, Node*>` |
| Early exit in `put` | `?.let { ...; return }` | `if (auto it = ...) { ...; return; }` |
| Sentinel init | `init { }` block | Constructor body |
| Memory layout | Heap objects, GC-managed | `head_`/`tail_` on stack, nodes on heap |
| Pre-allocation | `HashMap(capacity)` | `map_.reserve(capacity)` |

The algorithmic logic is 1:1 identical. The differences are purely language mechanics.

---

## 10. Complexity Analysis

| Operation | Time | Why |
|-----------|------|-----|
| `get` | O(1) | HashMap lookup + 2 DLL pointer ops |
| `put` (update) | O(1) | HashMap lookup + 2 DLL pointer ops |
| `put` (insert) | O(1) | HashMap insert + 2 DLL pointer ops |
| `put` (evict+insert) | O(1) | All pointer ops are constant |

| Resource | Space | Why |
|----------|-------|-----|
| HashMap | O(capacity) | Bounded by capacity |
| DLL | O(capacity) | At most `capacity` real nodes |
| Total | O(capacity) | |

The amortized cost of HashMap operations is O(1) assuming a good hash function (true for integer keys). Worst case is O(n) due to collisions, but this never occurs with integer keys in practice.

---

## 11. Common Mistakes

**1. Forgetting to move on `get`**
```kotlin
// WRONG — get doesn't update recency
fun get(key: Int): Int {
    return map[key]?.value ?: -1
}
```
A `get` that doesn't call `moveToTail` silently corrupts the LRU order. The entry remains wherever it was and may be evicted prematurely.

---

**2. Evicting before checking for existing key in `put`**
```kotlin
// WRONG — evicts even when key already exists (no size change needed)
fun put(key: Int, value: Int) {
    if (map.size == capacity) evictLRU()  // ← should only do this for NEW keys
    ...
}
```
If `put(existingKey, newValue)` is called at capacity, you'd evict a valid entry unnecessarily. Always handle the update path first.

---

**3. Using the wrong end as MRU**
The convention (head=LRU, tail=MRU) is arbitrary — but you must be consistent. Mixing up which end to insert at and which end to evict from produces a Most-Recently-Evicted cache, which is the opposite of what you want.

---

**4. Not storing key in Node**
```cpp
// WRONG — can't erase from map on eviction
struct Node { int value; Node* prev; Node* next; };

Node* lru = head_.next;
// map_.erase(???);  ← you have no key!
```
The node must carry its own key precisely for the eviction case.

---

**5. Deleting before erasing (C++)**
```cpp
// WRONG — UB: reading lru->key after delete
delete lru;
map_.erase(lru->key);  // ← accessing freed memory
```
Always `map_.erase(lru->key)` first, then `delete lru`.

---

## 12. Decision Guide

```
Need to implement a cache?
│
├─ Fixed capacity with eviction?
│   ├─ Evict least recently used → LRU Cache (this guide)
│   ├─ Evict least frequently used → LFU Cache (different structure)
│   └─ Evict oldest inserted → FIFO Cache (just a Queue + Map)
│
└─ Variable / unbounded?
    └─ Just use a HashMap
```

```
Choosing the DLL end convention:
│
├─ head.next = LRU (evict here)
└─ tail.prev = MRU (insert/promote here)

Choosing sentinel vs null checks:
└─ Always use sentinels — eliminates all edge cases with zero cost
```

```
When to use !! (Kotlin) / no null check (C++):
└─ When sentinel invariant guarantees non-null:
    ├─ head.next — always non-null (worst case = tail)
    ├─ tail.prev — always non-null (worst case = head)
    ├─ node.prev — always non-null for real nodes (prev = head at minimum)
    └─ node.next — always non-null for real nodes (next = tail at minimum)
```

---

*The entire solution reduces to one insight: a HashMap gives you O(1) access to a DLL node, and a DLL gives you O(1) reordering. Neither structure alone is sufficient. Together they're exactly sufficient.*
