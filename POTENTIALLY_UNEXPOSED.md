# Exposing cljs.core Symbols in nbb/Scittle

**Date**: 2025-12-09

## Summary

Many `cljs.core` types and functions exist in the compiled JS runtime but aren't mapped in SCI config. This PR adds them with simple one-line mappings.

**Added by this PR:**
- nbb: 20 types + 9 functions = 29 total
- scittle: 27 types + 15 functions = 42 total

## Compatibility Matrix

Legend: Y = available | N = added to nbb | S = added to scittle | - = not available | n/a = N/A

### Types (for `instance?` checks)

N = 20 types added to nbb | S = 27 types added to scittle

| Type | CLJ | CLJS | nbb (before) | nbb (after) | scittle (before) | scittle (after) |
|------|-----|------|--------------|-------------|------------------|-----------------|
| `List` | Y | Y | - | N | - | S |
| `Cons` | Y | Y | - | N | - | S |
| `IndexedSeq` | n/a | Y | - | N | - | S |
| `ChunkedSeq` | Y | Y | - | N | - | S |
| `ChunkedCons` | Y | Y | - | N | - | S |
| `Subvec` | Y | Y | - | N | - | S |
| `PersistentVector` | Y | Y | Y | Y | - | S |
| `PersistentHashMap` | Y | Y | - | N | - | S |
| `PersistentHashSet` | Y | Y | - | N | - | S |
| `PersistentArrayMap` | Y | Y | - | N | - | S |
| `PersistentTreeMap` | Y | Y | - | N | - | S |
| `PersistentTreeSet` | Y | Y | - | N | - | S |
| `PersistentQueue` | Y | Y | Y | Y | - | S |
| `Repeat` | Y | Y | - | N | - | S |
| `Cycle` | Y | Y | - | N | - | S |
| `Iterate` | Y | Y | - | N | - | S |
| `IntegerRange` | n/a | Y | - | N | - | S |
| `Range` | Y | Y | - | N | - | S |
| `Atom` | Y | Y | Y | Y | - | S |
| `Volatile` | Y | Y | - | N | - | S |
| `Keyword` | Y | Y | Y | Y | - | S |
| `Symbol` | Y | Y | Y | Y | - | S |
| `UUID` | Y | Y | Y | Y | - | S |
| `MapEntry` | Y | Y | Y | Y | - | S |
| `TaggedLiteral` | Y | Y | - | N | - | S |
| `ExceptionInfo` | Y | Y | - | N | - | S |
| `Reduced` | Y | Y | - | N | - | S |

### Functions & Predicates

N = 9 functions added to nbb | S = 15 functions added to scittle

| Function | CLJ | CLJS | nbb (before) | nbb (after) | scittle (before) | scittle (after) |
|----------|-----|------|--------------|-------------|------------------|-----------------|
| `infinite?` | - | Y | - | N | Y | Y |
| `regexp?` | - | Y | - | N | - | S |
| `iterable?` | - | Y | - | N | - | S |
| `cloneable?` | - | Y | - | N | - | S |
| `reduceable?` | - | Y | - | N | - | S |
| `js-symbol?` | - | Y | - | N | - | S |
| `swap-vals!` | Y | Y | Y | Y | - | S |
| `reset-vals!` | Y | Y | Y | Y | - | S |
| `add-tap` | Y | Y | Y | Y | - | S |
| `remove-tap` | Y | Y | Y | Y | - | S |
| `tap>` | Y | Y | Y | Y | - | S |
| `demunge` | Y | Y | Y | Y | - | S |
| `Throwable->map` | Y | Y | - | N | - | S |
| `js-invoke` | - | Y | - | N | - | S |
| `js-mod` | - | Y | - | N | - | S |

### Not Available

| Symbol | CLJ | CLJS | Reason |
|--------|-----|------|--------|
| `future` | Y | - | JVM threading - use Promises |
| `future?` | Y | - | JVM threading |
| `atom?` | - | - | Never existed - use `(instance? Atom x)` |
| `reader-conditional` | Y | - | Read-time only |
| `reader-conditional?` | Y | - | Read-time only |
| `ifind?` | - | Y | Needs SCI changes |
| `js-iterable?` | - | Y | Needs SCI changes |
| `print-meta?` | - | Y | Needs SCI changes |

## Notes

- **IntegerRange vs Range**: `(range 10)` → `IntegerRange`, `(range 0.5 10 0.5)` → `Range`. Sibling types, not hierarchy.
- **Protocols**: Limited support in SCI. `satisfies?` returns `false` for most protocols. Needs more research.

## How to Add More

```clojure
;; Check if exists in runtime
(exists? js/cljs.core.some_fn)

;; Add type
'TypeName cljs.core/TypeName

;; Add function
'fn-name (sci/copy-var fn-name core-ns)
```
