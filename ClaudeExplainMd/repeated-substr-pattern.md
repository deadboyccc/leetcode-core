# Repeated Substring Pattern — Deep Dive

---

## What is a Rotation?

A **rotation** means taking some characters from the front and moving them to the back.

```
s = "abcd"
rotate by 1 → "bcda"   (took 'a' from front, appended to back)
rotate by 2 → "cdab"
rotate by 3 → "dabc"
rotate by 4 → "abcd"   ← trivial, back to itself
```

**Trivial rotation** = rotating by 0 or by full length `n`. You get the original string back, but it proves nothing — every string trivially rotates to itself.

**Non-trivial rotation** = any rotation by `k` where `0 < k < n`. This is what we care about.

---

## What does "s is a substring of ss" mean?

Every possible rotation of `s` lives inside `s+s`.

```
s  = "abcd"
ss = "abcdabcd"
       ^^^^       rotation by 0 (trivial)
        ^^^^      rotation by 1 → "bcda"
         ^^^^     rotation by 2 → "cdab"
          ^^^^    rotation by 3 → "dabc"
           ^^^^   rotation by 4 (trivial)
```

So `ss` is literally a **rotation machine** — every window of size `n` inside `ss` is one rotation of `s`. That's why we use it.

---

## Why chop the first and last character?

We chop to **kill the two trivial rotations** (index 0 and index n), leaving only the non-trivial ones available to match.

```
ss        = "abcdabcd"   length 8
chopped   =  "bcdabc"    indices 1..6
```

Now if `s` appears in `chopped`, it can only be at a non-trivial rotation offset.

---

## The Core Claim

> A string has a repeated substring pattern **if and only if** it has a non-trivial rotation that equals itself.

**Concrete proof with `s = "abcabc"` (repeating unit = `"abc"`):**

```
ss      = "abcabcabcabc"
chopped =  "bcabcabcab"

does "abcabc" appear in "bcabcabcab"?
          ^^^^^^  → yes, at index 2
```

Rotating `"abcabc"` by 3 (one unit length) gives `"abcabc"` — the same string. That's the non-trivial rotation that proves it.

**Why does repeating imply a self-rotation?**

If `s` is made of `k` copies of unit `u`:
```
s = "abc" + "abc" + "abc"    (k=3, u="abc", len=3)
```
Rotating by `len(u) = 3` just shifts one copy from front to back:
```
"abc|abc|abc" → "abc|abc|abc"  ← identical!
```
The string maps to itself under rotation by unit length. That's the non-trivial self-rotation.

---

## Why does NO repeated pattern mean NO self-rotation?

Take `s = "abcd"` (no repeating unit):

```
ss      = "abcdabcd"
chopped =  "bcdabc"

does "abcd" appear in "bcdabc"? No.
```

Every rotation produces something *different* from `s`. There is no shift that maps `s` back to itself, so no repeated unit exists.

---

## Full picture in one diagram

```
s   =  a b c a b c
ss  =  a b c a b c | a b c a b c
chop → b c a b c a b c a b c a b   (remove first 'a' and last 'c')

sliding window of size 6:
  [b c a b c a]  → "bcabca" ≠ s
  [c a b c a b]  → "cabcab" ≠ s
  [a b c a b c]  → "abcabc" = s ✓  ← found it
```

The window that matched is exactly **rotation by 3** — one unit length — proving the repeated pattern.

---

## Summary

| Term | Meaning |
|---|---|
| Rotation by k | Move first k chars to the back |
| Trivial rotation | k=0 or k=n, always equals s |
| Non-trivial rotation | 0 < k < n |
| ss trick | ss contains ALL rotations as windows |
| Chop | Removes trivial rotations from consideration |
| Self-rotation | A non-trivial rotation that still equals s — only possible if s has a repeated unit |
