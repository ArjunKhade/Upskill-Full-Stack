# Java Tricky Interview Q&A (How to Say It in an Interview)

Tip: Say **Yes/No first**, then give the reason in one line.

---

## 1. main() Method

**Can we run a Java program without `public static void`?**
Yes, from Java 25. It supports instance main methods, so the main method no longer needs to be `public static void`.

**Can we write a Java file with no class?**
Yes, from Java 25 using compact source files. The compiler wraps the code in an implicit class for us.

**Can we override the main method?**
No. `main` is static, and static methods are not overridden, they are hidden.

**Can the JVM call our overloaded main?**
No. The JVM only looks for the exact signature `main(String[] args)`. Other overloads are just normal methods.

**Can we print something without writing a main method?**
Until Java 6, yes, using a static block. From Java 7 onwards, no, because the JVM checks for the main method first.

---

## 2. Classes, Inheritance and Interfaces

**Can a class extend itself?**
No. It gives a cyclic inheritance compile error.

**Can a class extend two classes?**
No. Java avoids the diamond problem by not allowing multiple class inheritance.

**Can an interface extend multiple interfaces?**
Yes. Traditionally interfaces had no state or implementation, so there was nothing to conflict.

**Can two interfaces have the same default method?**
Yes, but the implementing class won't compile until it overrides the method and resolves the conflict. It can call a specific one using `InterfaceName.super.method()`.

**Can an interface have private methods?**
Yes, from Java 9. They are used only to reuse code between default methods.

**Can an interface have a constructor?**
No. Interfaces have no state to initialize and can't be instantiated directly.

**Can an abstract class have a constructor?**
Yes. It is used to initialize the fields that child classes inherit.

**Can we create an object of an abstract class?**
Not directly. We can create an object of a concrete child class or an anonymous subclass.

**Can we override a static method?**
No. It is called method hiding, and it is resolved at compile time, not runtime.

**Can we override a private method?**
No. Private methods are not visible to the child class, so a method with the same name in the child is just a new, separate method.

**Can a top-level class be private or protected?**
No. It can only be `public` or default (package-private). A private top-level class would be inaccessible to everyone.

---

## 3. Constructors

**Can a constructor be final, static, or abstract?**
No to all three. Constructors are not inherited (so no final or abstract needed), and they need an instance (so not static).

**Can a constructor be private?**
Yes. It is commonly used in singletons and utility classes to stop outside instantiation.

**Can a constructor call another constructor?**
Yes, using `this()`. This is called constructor chaining.

**Can we call `this()` and `super()` in the same constructor?**
No. Both must be the first statement, so only one can be used.

**Can a constructor throw an exception?**
Yes, both checked and unchecked exceptions.

---

## 4. Enums

**Can an enum have a constructor?**
Yes, but it is implicitly private so no one can create enum objects from outside.

**Can an enum extend a class?**
No. It already extends `java.lang.Enum` implicitly, and Java has no multiple inheritance.

**Can an enum implement an interface?**
Yes. Each enum constant can even provide its own implementation of the interface methods.

---

## 5. String and Immutability

**Can we extend the String class?**
No. `String` is final for security, predictable behavior, and reliability as a key in maps.

**Can String ever be made mutable?**
Technically yes, using Reflection to change its internal byte array, but it is dangerous and strongly discouraged.

**Can two Strings with the same content have different references?**
Yes. One may be in the string pool and the other created with `new String()` on the heap.

**Can `==` be true for Strings created with `new`?**
Yes, if we call `.intern()`, which returns the pooled reference.

**Can `String.hashCode()` return zero?**
Yes, for an empty string and for some specific non-empty strings.

**Can we create our own immutable class?**
Yes:
- make the class `final`
- make fields `private final`
- no setters
- initialize through the constructor
- use defensive copies for mutable inputs and outputs

**Can an immutable class have a mutable field?**
Yes, but returning it directly breaks immutability. We must use defensive copying or unmodifiable views.

**Can an immutable object still break in multi-threading?**
No. If it is truly immutable, the `final` field guarantee ensures other threads see it fully constructed.

---

## 6. Exception Handling

**Can we write `try` without `catch`?**
Yes, with `try-finally` or `try-with-resources`.

**Can the `finally` block ever not execute?**
Yes, if `System.exit()` is called, the JVM crashes, or there is an infinite loop in the `try` block.

**Can `finally` change the value returned by `try`?**
Yes. A `return` inside `finally` overrides the `return` in `try`. (It's bad practice.)

**Can we throw an exception from `finally`?**
Yes, but it replaces and hides the original exception from `try`.

**Can we catch an `Error`?**
Yes, because it extends `Throwable`, but we shouldn't. It usually means a serious JVM problem we can't recover from.

**Can a static block throw an exception?**
Only unchecked ones. It causes `ExceptionInInitializerError`, and after that the class becomes unusable (`NoClassDefFoundError` on later use).

---

## 7. Collections

**Can a HashMap store a null key?**
Yes, exactly one null key. It goes to bucket zero.

**Can ConcurrentHashMap store a null key or value?**
No. It throws `NullPointerException` to avoid ambiguity in multi-threaded code (you can't tell if a key is missing or mapped to null).

**Can two keys with the same hash code both be stored in a HashMap?**
Yes. They go in the same bucket as a linked list or tree. This is a hash collision.

**Can we use a mutable object as a HashMap key?**
Yes, but if its fields change, its hash code changes and we can't find it in the map anymore.

**Can a HashSet contain duplicates?**
Yes, if we override `equals()` but not `hashCode()`.

**Can we override `equals()` without `hashCode()`?**
The compiler allows it, but it breaks the contract and causes bugs in hash-based collections.

**Can we modify a list while iterating over it?**
Not in a normal for-each loop, because it throws `ConcurrentModificationException`. Use `Iterator.remove()`, `removeIf()`, or `CopyOnWriteArrayList`.

**Can a TreeMap store null keys?**
No. It throws `NullPointerException` because it must compare keys to sort them.

**Can a HashMap cause an infinite loop?**
Yes, before Java 8. Concurrent resizing by multiple threads could create a circular linked list.

**Can `Collections.sort()` throw an exception on valid data?**
Yes, if the Comparator logic is inconsistent (for example, it breaks the comparison contract).

**Can we make a HashMap thread-safe without ConcurrentHashMap?**
Yes, by wrapping it with `Collections.synchronizedMap()`.

---

## 8. Generics

**Can we create an array of a generic type?**
No. Arrays check their type at runtime, but generics are erased at compile time (type erasure).

**Can we use primitives with generics?**
No. After type erasure everything becomes `Object`, and primitives are not objects. We use wrappers like `Integer`.

**Can a `List<String>` be assigned to a `List<Object>`?**
No. Generics are invariant, which keeps them type-safe.

---

## 9. Multithreading

**Can we start a thread twice?**
No. Calling `start()` again throws `IllegalThreadStateException`.

**Can we call `run()` instead of `start()`?**
Yes, but it works like a normal method call on the current thread. No new thread is created.

**Can a dead thread be restarted?**
No. Once a thread is dead, it stays dead.

**Can `volatile` make `count++` atomic?**
No. `volatile` gives visibility only, not atomicity. Use `AtomicInteger` or synchronization.

**Can two threads enter the same synchronized method at once?**
Yes, if they call it on two different objects, because the lock is per object.

**Can a thread acquire the same lock twice?**
Yes. Java locks are re-entrant.

**Can a constructor be synchronized?**
No, the compiler doesn't allow it. It's also unnecessary because the object isn't visible to other threads while it's being constructed.

**Can `sleep()` release the lock?**
No. `sleep()` keeps all locks. `wait()` releases the lock.

**Can `Thread.interrupt()` stop a thread?**
No. It only sets an interrupt flag. The thread must check it and decide to stop.

**Can a deadlock happen with only one lock?**
Yes, if a thread holding the lock calls `wait()` and the notification never comes.

---

## 10. JVM and Garbage Collection

**Can a Java app run without the JRE installed?**
Yes. Using `jlink` (Java 9+), we can bundle a minimal custom runtime with the application.

**Can we force garbage collection?**
No. `System.gc()` is only a request, and the JVM can ignore it.

**Can two objects that reference each other be garbage collected?**
Yes, if neither is reachable from GC roots.

**Can an object come back to life after becoming eligible for GC?**
Yes, this is called resurrection. It can happen in `finalize()` by assigning `this` to a live reference, and only once per object. (`finalize()` is deprecated now.)

**Can a memory leak happen even with automatic GC?**
Yes, if unused objects are still strongly referenced, like growing static lists or forgotten listeners.

**Can we disable garbage collection completely?**
Yes, using **Epsilon GC**. It allocates memory but never reclaims it, so you get `OutOfMemoryError` when memory is full. It is mostly used for testing and benchmarking.

---

## 11. Java 8+ Features

**Can a functional interface have more than one method?**
Yes, but only one **abstract** method. Default methods, static methods, and `Object` class methods don't count.

**Can a lambda use a non-final local variable?**
Only if it is **effectively final**, meaning it is never changed after initialization.

**Can we reuse a stream?**
No. Once a terminal operation is called, the stream is consumed and closed.

**Can a stream run without a terminal operation?**
No. Intermediate operations are lazy and don't run until a terminal operation triggers them.

**Can a parallel stream be slower than a sequential one?**
Yes, for small data, light work per element, or blocking I/O, because thread management overhead costs more than the gain.

**Can an Optional itself be null?**
Technically yes, but returning null from a method that returns `Optional` defeats its purpose. Never do it.

**Can a record be mutable?**
The record's fields are final, so the record itself is shallowly immutable. But if it holds a mutable object like a `List`, the contents of that list can still change.

**Can `var` be used as a field type?**
No. `var` is only for local variables.

**Can a parallel stream starve our whole application?**
Yes. Parallel streams share the common ForkJoinPool, so one slow or blocking stream can delay all the others in the JVM.

---

## 12. Design Patterns, Serialization and Class Loaders

**Can a singleton have more than one instance?**
Yes, if broken by Reflection, Deserialization, Cloning, or multiple ClassLoaders. An enum singleton avoids most of these.

**Can a transient field still be serialized?**
No, it gets the default value (null or 0) after deserialization, unless we write custom `writeObject` and `readObject` methods.

**Can two classes with the same fully qualified name exist in one JVM?**
Yes, if they are loaded by different class loaders.

**Can static fields be shared across class loaders?**
No. Each class loader has its own copy of the class and its own static fields.

**Can ThreadLocal cause a memory leak?**
Yes, in thread pools. The thread stays alive, and if we don't call `remove()`, the value stays referenced.

---

## Quick Revision: One-Line Memory Hooks

| Topic | Remember |
|---|---|
| Static methods | Hidden, not overridden |
| Private methods | Not inherited, so no override |
| Multiple inheritance | Classes: no. Interfaces: yes |
| String | Final + immutable + pool |
| `finally` | Always runs except `System.exit()`, crash, infinite loop |
| HashMap | One null key, uses equals + hashCode |
| ConcurrentHashMap | No null key or value |
| `volatile` | Visibility only, not atomic |
| `sleep()` vs `wait()` | sleep keeps the lock, wait releases it |
| `interrupt()` | Only sets a flag |
| `System.gc()` | Just a request |
| Streams | Lazy, single use |
| Parallel stream | Shared ForkJoinPool |
| Lambda variables | Must be effectively final |
| Records | Shallow immutable |
