#include <unordered_map>
using namespace std;

class LRUCache {
    struct Node {
        int key, value;
        Node* prev = nullptr;
        Node* next = nullptr;
        Node(int k, int v) : key(k), value(v) {}
    };

    const int capacity_;
    unordered_map<int, Node*> map_;
    Node head_{-1, -1};   // sentinel: LRU end
    Node tail_{-1, -1};   // sentinel: MRU end

    void unlink(Node* n) {
        n->prev->next = n->next;
        n->next->prev = n->prev;
    }

    void insertBeforeTail(Node* n) {
        Node* prev = tail_.prev;
        prev->next = n;
        n->prev    = prev;
        n->next    = &tail_;
        tail_.prev = n;
    }

    void moveToTail(Node* n) {
        unlink(n);
        insertBeforeTail(n);
    }

public:
    LRUCache(int capacity) : capacity_(capacity) {
        head_.next = &tail_;
        tail_.prev = &head_;
        map_.reserve(capacity);
    }

    ~LRUCache() {
        for (auto& [_, node] : map_) delete node;
    }

    int get(int key) {
        auto it = map_.find(key);
        if (it == map_.end()) return -1;
        moveToTail(it->second);
        return it->second->value;
    }

    void put(int key, int value) {
        if (auto it = map_.find(key); it != map_.end()) {
            it->second->value = value;
            moveToTail(it->second);
            return;
        }

        if (static_cast<int>(map_.size()) == capacity_) {
            Node* lru = head_.next;
            map_.erase(lru->key);
            unlink(lru);
            delete lru;
        }

        Node* node = new Node(key, value);
        insertBeforeTail(node);
        map_[key] = node;
    }
};