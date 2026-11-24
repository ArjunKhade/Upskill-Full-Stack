# JavaScript Fundamentals

A reference guide covering core JavaScript execution concepts: synchronous vs asynchronous behavior, execution context, hoisting, closures, and functions.

## Table of Contents

- [Synchronous vs Asynchronous](#synchronous-vs-asynchronous)
- [Execution Context](#execution-context)
- [Call Stack](#call-stack)
- [Hoisting](#hoisting)
- [let, const & the Temporal Dead Zone (TDZ)](#let-const--the-temporal-dead-zone-tdz)
- [undefined vs null vs not defined](#undefined-vs-null-vs-not-defined)
- [Scope, Lexical Environment & Scope Chain](#scope-lexical-environment--scope-chain)
- [Variable Shadowing & Illegal Shadowing](#variable-shadowing--illegal-shadowing)
- [Closures](#closures)
- [Function Statement vs Function Expression](#function-statement-vs-function-expression)
- [Anonymous Functions](#anonymous-functions)
- [Named Function Expressions](#named-function-expressions)
- [Parameters vs Arguments](#parameters-vs-arguments)
- [First-Class Functions](#first-class-functions)
- [Higher Order Functions](#higher-order-functions)
- [call(), apply(), and bind() Methods](#call-apply-and-bind-methods)
- [Event Loop, Microtask & Macrotask Queue](#event-loop-microtask--macrotask-queue)
- [Shallow Copy vs Deep Copy](#shallow-copy-vs-deep-copy-in-javascript)
- [ES6 Features (ECMAScript 2015)](#es6-features-ecmascript-2015)
- [Modern JavaScript Features (ES2016 – ES2024+)](#modern-javascript-features-es2016--es2024)

---

## Synchronous vs Asynchronous

JavaScript is a **synchronous, single-threaded language**.

- Code is executed line by line.
- One task is completed at a time, in order.
- JavaScript waits for the current task to finish before moving to the next line.

**Synchronous** means tasks execute one after another — JavaScript waits for the current operation to complete before moving on.

**Asynchronous** means JavaScript can start a task and continue executing other code without waiting for that task to finish. Once the task completes, its result is handled later using callbacks, Promises, or `async`/`await`.

> **Example:** API calls, timers, and file operations are asynchronous in JavaScript because they take time and shouldn't block the rest of the application.

[↑ Back to Table of Contents](#table-of-contents)

---

## Execution Context

Every time JavaScript code runs, it does so inside an **execution context**, made up of two components:

- **Memory Component (Variable Environment)** — stores variables and functions as key-value pairs.
- **Code Component (Thread of Execution)** — executes the code line by line.

[↑ Back to Table of Contents](#table-of-contents)

---

## Call Stack

The **call stack** maintains the order of execution of execution contexts. It tracks which function is currently running and what to return to once that function completes.

[↑ Back to Table of Contents](#table-of-contents)

---

## Hoisting

**Hoisting** allows variables and functions to be accessed before they are initialized or assigned a value.

```js
console.log(x); // undefined (not an error, due to hoisting)
var x = 5;

greet(); // works fine, function declarations are hoisted
function greet() {
  console.log("Hello!");
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## let, const & the Temporal Dead Zone (TDZ)

With ES6, `let` and `const` were introduced to fix issues associated with `var`. Understanding how they are hoisted and the **Temporal Dead Zone (TDZ)** is one of the most frequently asked JavaScript interview questions.

### 1. What is the Temporal Dead Zone (TDZ)?

The **Temporal Dead Zone (TDZ)** is the time window between entering a scope (when a variable is hoisted into memory) and the moment the variable is **declared and initialized** with a value.

- While a variable is inside its TDZ, attempting to access, read, or write to it will immediately throw a **`ReferenceError: Cannot access '<variable>' before initialization`**.
- The TDZ ends the exact moment the JavaScript engine executes the initialization line.

```js
{
  // ─── TDZ for 'name' starts here ───
  // console.log(name); // ReferenceError: Cannot access 'name' before initialization

  let age = 25; // age's TDZ ends here

  let name = "Arjun"; // ─── TDZ for 'name' ends here ───
  console.log(name);  // "Arjun" (Accessible now!)
}
```

---

### 2. Are `let` and `const` Hoisted? (Crucial Interview Question)

> **Answer:** **YES, `let` and `const` ARE hoisted!**

A very common myth is that only `var` and function declarations are hoisted. However, `let` and `const` are hoisted too, with one critical difference:
- **`var`** is hoisted and immediately initialized with `undefined` in the **Global / Function** memory space (accessible via `window` in browsers).
- **`let` and `const`** are hoisted into a separate, uninitialized memory space (shown as **Script** or **Block** scope in Chrome DevTools) and remain **uninitialized** in the TDZ.

#### Proof that `let` is hoisted:

```js
let x = "Global";

{
  // If 'x' was NOT hoisted inside this block, JavaScript would
  // look up the scope chain and log "Global".
  //
  // Instead, it throws: ReferenceError: Cannot access 'x' before initialization.
  // This proves that 'x' WAS hoisted to the top of this block!
  console.log(x); 

  let x = "Local"; // TDZ ends here
}
```

---

### 3. `let` vs `const`

| Feature | `let` | `const` |
|---|---|---|
| **Declaration without value** | Allowed (`let a;` default is `undefined`) | **Not allowed** (`const b;` throws `SyntaxError: Missing initializer`) |
| **Re-assignment** | Allowed (`a = 20;`) | **Not allowed** (`TypeError: Assignment to constant variable`) |
| **Re-declaration** | Not allowed in same scope | Not allowed in same scope |
| **Scope** | Block Scope `{ ... }` | Block Scope `{ ... }` |
| **Hoisted?** | Yes (in TDZ) | Yes (in TDZ) |

---

### 4. `const` with Objects and Arrays (Mutation vs Reassignment)

A common misconception is that values declared with `const` cannot change. 

- `const` only creates an immutable **variable binding (reference)** in memory. It prevents you from pointing the variable to a *new* reference.
- It does **not** freeze the internal properties or elements of an object or array!

```js
const person = { name: "Arjun", age: 25 };

// ✅ ALLOWED: Mutating internal properties
person.age = 26;
person.city = "Pune";
console.log(person); // { name: "Arjun", age: 26, city: "Pune" }

// ❌ NOT ALLOWED: Reassigning the binding
// person = { name: "Rahul" }; // TypeError: Assignment to constant variable

const numbers = [1, 2, 3];
numbers.push(4); // ✅ Allowed! (Mutating array)
// numbers = [5, 6]; // ❌ TypeError! (Reassigning reference)
```

> **Tip:** To make an object truly shallow-immutable where properties cannot be added, modified, or deleted, use **`Object.freeze(person)`**.

---

### 5. Types of Errors Associated with `var`, `let`, and `const`

| Error Type | Example Trigger |
|---|---|
| **`ReferenceError`** | Accessing a `let` or `const` variable inside its **TDZ**, or accessing an undeclared variable (`not defined`). |
| **`SyntaxError`** | Re-declaring a `let`/`const` in the same scope, or declaring `const x;` without initialization. *(Stops script before execution).* |
| **`TypeError`** | Attempting to re-assign a value to a `const` variable. |

---

### 6. Comparison Table: `var` vs `let` vs `const`

| Feature | `var` | `let` | `const` |
|---|---|---|---|
| **Scope** | Function / Global Scope | Block Scope `{ ... }` | Block Scope `{ ... }` |
| **Hoisted?** | Yes | Yes | Yes |
| **Default Initialization on Hoisting** | `undefined` | Uninitialized (in TDZ) | Uninitialized (in TDZ) |
| **Subject to TDZ?** | **No** | **Yes** | **Yes** |
| **Re-declaration (same scope)** | Allowed | ❌ SyntaxError | ❌ SyntaxError |
| **Re-assignment** | Allowed | Allowed | ❌ TypeError |
| **Attaches to `window` (Global)?** | **Yes** (`window.x`) | **No** | **No** |

[↑ Back to Table of Contents](#table-of-contents)

---

## undefined vs null vs not defined

Understanding the distinction between `undefined`, `null`, and `not defined` is a fundamental JavaScript concept and a frequent interview topic.

### 1. `undefined`

- **What it is:** A primitive value automatically assigned by JavaScript to a variable that has been declared, but not yet initialized with a value.
- **Memory Phase:** Memory is allocated for the variable during the creation phase of the Execution Context, and JavaScript assigns the placeholder value `undefined`.
- **Type:** `typeof undefined === "undefined"`

```js
var a;
console.log(a); // undefined

let b;
console.log(b); // undefined

function test() {}
console.log(test()); // undefined (functions without a return statement return undefined)
```

> **Best Practice:** Avoid manually assigning `undefined` to variables (e.g., `a = undefined;`). Use `null` instead when explicitly clearing or resetting values.

---

### 2. `null`

- **What it is:** An intentional representation of "no value" or an empty object reference. It must be assigned **explicitly** by the developer.
- **Memory Phase:** Memory is allocated and holds the explicit primitive value `null`.
- **Type:** `typeof null === "object"` (a well-known historical bug in JavaScript that cannot be fixed without breaking backward compatibility on the web).

```js
let user = null; // developer explicitly states: "there is currently no user object"
console.log(user);        // null
console.log(typeof user); // "object"
```

---

### 3. `not defined` (ReferenceError)

- **What it is:** A runtime error (`ReferenceError`) that occurs when trying to access a variable that has **never been declared** in the current or outer scopes.
- **Memory Phase:** No memory was ever allocated for the identifier in the Variable Environment of any execution context.

```js
console.log(y); // ReferenceError: y is not defined
```

> **Key Distinction:**
> - **`undefined`**: Variable **exists** in memory, but has not been assigned a value.
> - **`not defined`**: Variable **does not exist** in memory at all.

---

### Comparison Table

| Feature | `undefined` | `null` | `not defined` |
|---|---|---|---|
| **Meaning** | Declared, but no value assigned (default state) | Intentional absence of any value / empty reference | Variable was never declared anywhere |
| **Assigned By** | JavaScript Engine (automatically) | Developer (explicitly) | N/A (triggers an error) |
| **Data Type** | Primitive (`undefined`) | Primitive (`null`) | N/A |
| **`typeof`** | `"undefined"` | `"object"` (historical bug) | Throws `ReferenceError` (except `typeof undeclared` which safely returns `"undefined"`) |
| **Memory Allocated?** | Yes | Yes | No |
| **Boolean Value** | `false` (falsy) | `false` (falsy) | N/A (crashes before evaluation) |
| **Numeric Coercion** | `Number(undefined) -> NaN` | `Number(null) -> 0` | N/A |

---

### Equality & Math Operations

```js
// Loose equality (checks value with type coercion)
console.log(null == undefined);  // true

// Strict equality (checks value and type)
console.log(null === undefined); // false

// Arithmetic operations
console.log(null + 1);      // 1   (null coerces to 0)
console.log(undefined + 1); // NaN (undefined coerces to NaN)
```

---

### Quick Interview Summary

- **`undefined`**: *"I exist in memory, but no one has given me a value yet."*
- **`null`**: *"I exist in memory, and the developer explicitly set me to empty/nothing."*
- **`not defined`**: *"I was never declared and do not exist in memory."*

[↑ Back to Table of Contents](#table-of-contents)

---

## Scope, Lexical Environment & Scope Chain

### 1. Scope

**Scope** determines where variables, functions, and objects are accessible (visible) in your code during execution.

JavaScript has three main types of scope:
- **Global Scope:** Variables declared outside any function or block are accessible from anywhere in the file.
- **Function (Local) Scope:** Variables declared inside a function are accessible only within that function.
- **Block Scope (ES6):** Variables declared with `let` and `const` inside a `{}` block are accessible only within that block.

---

### 2. Lexical Environment

- **"Lexical"** means *in hierarchy* or *where physical code is written*.
- Whenever an **Execution Context** is created, a **Lexical Environment** is also created automatically.
- **Definition:**
  > **Lexical Environment = Local Memory (Variable Environment) + Reference to Lexical Environment of Parent**

An execution context has direct access to its own local memory, plus a reference to the outer environment where the code was lexically (physically) defined.

---

### 3. Scope Chain

The **Scope Chain** is the step-by-step lookup mechanism JavaScript uses to find the value of a variable:

1. JavaScript first searches the **current local memory**.
2. If the variable is not found, it traverses to the **lexical parent environment**.
3. It keeps searching upward through parent environments until reaching the **Global Lexical Environment** (whose parent reference is `null`).
4. If the variable cannot be found anywhere in the scope chain, JavaScript throws a `ReferenceError: <variable> is not defined`.

```js
function a() {
  var b = 10;
  c();

  function c() {
    console.log(b); // 10 (c resolves b through the scope chain!)
  }
}

a();
console.log(b); // ReferenceError: b is not defined (global cannot look into local scope of a)
```

**Visualizing the Scope Chain:**

```
[c() Execution Context]
  ├── Local Memory: (empty)
  └── Outer Lexical Ref ──┐
                          ▼
             [a() Execution Context]
               ├── Local Memory: { b: 10, c: function }
               └── Outer Lexical Ref ──┐
                                       ▼
                          [Global Execution Context]
                            ├── Global Memory: { a: function }
                            └── Outer Lexical Ref ──> null (End of chain)
```

> **One-Way Street:** Inner functions can access variables from outer (parent) scopes, but outer scopes can **never** access variables from inner scopes.

[↑ Back to Table of Contents](#table-of-contents)

---

## Variable Shadowing & Illegal Shadowing

### 1. What is Variable Shadowing?

**Shadowing** occurs when a variable declared in an inner scope (such as a block or a function) has the **same identifier/name** as a variable in an outer scope.

In this scenario, the inner variable **shadows (hides)** the outer variable within the boundaries of that inner scope.

```js
var a = 100;
let b = 200;

{
  var a = 10; // Shadows outer 'a' (also overwrites it in global memory because var is NOT block-scoped!)
  let b = 20; // Shadows outer 'b' inside this block (lives in separate Block scope)
  const c = 30;

  console.log(a); // 10
  console.log(b); // 20
  console.log(c); // 30
}

console.log(a); // 10 (modified globally by the block!)
console.log(b); // 200 (outer let b was NOT affected!)
```

---

### 2. Block Scope vs Function Scope Shadowing

- **Shadowing with `let` / `const`**: When you shadow an outer variable using `let` or `const` inside a block `{ ... }`, JavaScript places the new variable into a dedicated **Block Scope**. Once the block ends, the block scope is destroyed, and the outer variable is preserved.
- **Shadowing with `var`**: Since `var` is **function-scoped**, putting it inside a plain `{}` block does *not* create a new scope. It directly accesses and overwrites the variable in the Global or Function execution context.

---

### 3. Illegal Shadowing

You **cannot** shadow a variable declared with `let` or `const` using a `var` within the same or nested block scope. This is known as **Illegal Shadowing** and triggers a `SyntaxError`.

#### Why is it illegal?
A `var` variable is not bound to a block — it tries to be hoisted to the enclosing function or global scope. When it attempts to cross the block boundary, it collides with the existing `let` or `const` declaration in that same scope.

```js
// ❌ ILLEGAL SHADOWING: SyntaxError: Identifier 'a' has already been declared
let a = 20;
{
  var a = 20; // ERROR! var tries to leak out to the scope where 'let a' already exists
}
```

#### What is Legal?

1. **Shadowing `var` using `let`** is completely legal:
   ```js
   var a = 50;
   {
     let a = 100; // Legal! 'let' is strictly confined inside this block
     console.log(a); // 100
   }
   console.log(a); // 50
   ```

2. **Shadowing `let` using `var` inside a function** is legal because the `var` is contained by the function boundary and cannot leak into the outer scope:
   ```js
   let a = 20;
   function test() {
     var a = 50; // Legal! var is contained within test()'s function scope
     console.log(a); // 50
   }
   test();
   console.log(a); // 20
   ```

3. **Shadowing `let` using `let`** across different block scopes is legal:
   ```js
   let x = 10;
   {
     let x = 20; // Legal!
     console.log(x); // 20
   }
   console.log(x); // 10
   ```

---

### Shadowing Summary Matrix

| Outer Variable | Inner Variable (in Block) | Valid? | Reason |
|---|---|---|---|
| `var` | `let` | **Yes** (Legal) | `let` is confined to the block scope. |
| `var` | `var` | **Yes** (Legal) | Overwrites the outer `var` in global/function memory. |
| `let` | `let` | **Yes** (Legal) | Inner `let` lives in its own block scope. |
| `const` | `const` | **Yes** (Legal) | Inner `const` lives in its own block scope. |
| `let` | `var` | **No** (Illegal) | `var` attempts to escape the block and collides with `let`. |
| `const` | `var` | **No** (Illegal) | `var` attempts to escape the block and collides with `const`. |

[↑ Back to Table of Contents](#table-of-contents)

---

## Closures

A **function bundled together with its lexical environment** forms a closure.

- A function, along with the scope it was defined in, forms a closure.
- Even after the outer function has finished executing and its execution context is gone, the inner function still remembers a reference to the variables in its outer scope.
- It's not just the function that's returned — the entire closure (function + its lexical environment) is returned.

```js
function outer() {
  let count = 0;
  function inner() {
    count++;
    console.log(count);
  }
  return inner;
}

const counter = outer();
counter(); // 1
counter(); // 2
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Function Statement vs Function Expression

| | Function Statement (Declaration) | Function Expression |
|---|---|---|
| Definition | Defines a function with a specific name | Assigns a function to a variable |
| Hoisting | Hoisted — can be called before its definition | Not hoisted like a function; behaves like a variable (`undefined` until the assignment line runs) |

```js
// Function Statement
function add(a, b) {
  return a + b;
}

// Function Expression
const multiply = function (a, b) {
  return a * b;
};
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Anonymous Functions

Functions without a name. They're primarily used in contexts where functions are treated as values, such as when assigning them to a variable or passing them as an argument.

```js
const greet = function () {
  console.log("Hi there!");
};
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Named Function Expressions

A named function expression occurs when a named function is assigned to a variable.

> **Gotcha:** The function's name is *not* available in the outer scope — it's only accessible locally, inside the function itself.

```js
const sayHello = function hello() {
  console.log("Hello!");
};

sayHello(); // works
hello();    // ReferenceError: hello is not defined
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Parameters vs Arguments

- **Parameters** are the identifiers listed in the function definition.
- **Arguments** are the actual values passed into the function when it's called.

```js
function add(a, b) { // a, b are parameters
  return a + b;
}

add(2, 3); // 2, 3 are arguments
```

[↑ Back to Table of Contents](#table-of-contents)

---

## First-Class Functions

Also known as **first-class citizens**, this concept refers to the ability of functions to be treated like any other variable/value in the language.

Functions can be:

1. Assigned to variables.
2. Passed as arguments to other functions.
3. Returned from other functions.

[↑ Back to Table of Contents](#table-of-contents)

---

## Higher Order Functions

A **higher order function** is a function that:

- Takes another function as an argument (a **callback**), and/or
- Returns a function.

```js
example:1
function higherOrder(callback) {
  callback();
}

higherOrder(() => console.log("Called as a callback!"));

example:2

const radius = [3, 4, 5];

const area = function (radius){
  return Math.PI * radius* radius;
}

Array.prototype.calculate = function(logic){
  const output = [];
  for(let i=0; i<this.length;i++){
    output.push(logic(this[i]));
  }
  return output;
}

console.log(radius.calculate(area));
console.log(radius.map(area)); // polyfill for map function in javascript
```

[↑ Back to Table of Contents](#table-of-contents)

---

## call(), apply(), and bind() Methods

In JavaScript, the `this` keyword refers to the object executing the current function. `call`, `apply`, and `bind` are built-in methods on `Function.prototype` that allow developers to **explicitly control, set, or override the `this` context** (known as *explicit binding*) and borrow methods between objects.

```
+-----------+------------------------------------+--------------------------+---------------------------+
| Method    | Invocation                         | Arguments                | Return Value              |
+-----------+------------------------------------+--------------------------+---------------------------+
| call()    | Invokes immediately                | Comma-separated list     | Result of the function    |
+-----------+------------------------------------+--------------------------+---------------------------+
| apply()   | Invokes immediately                | Single Array / Iterable  | Result of the function    |
+-----------+------------------------------------+--------------------------+---------------------------+
| bind()    | Does NOT invoke (deferred)         | Comma-separated list     | A NEW bound function      |
+-----------+------------------------------------+--------------------------+---------------------------+
```

---

### 1. `call()` Method

- **What it does:** Invokes the function immediately with a specified `this` context and arguments passed **individually (comma-separated)**.
- **Syntax:** `fn.call(thisArg, arg1, arg2, ...)`

```js
const person1 = {
  firstName: "Arjun",
  lastName: "Khade"
};

const person2 = {
  firstName: "Rahul",
  lastName: "Sharma"
};

function printFullName(hometown, state) {
  console.log(`${this.firstName} ${this.lastName} from ${hometown}, ${state}`);
}

// Method borrowing using call:
printFullName.call(person1, "Pune", "Maharashtra");   // "Arjun Khade from Pune, Maharashtra"
printFullName.call(person2, "Mumbai", "Maharashtra"); // "Rahul Sharma from Mumbai, Maharashtra"
```

---

### 2. `apply()` Method

- **What it does:** Exactly the same as `call()`, except that function arguments are passed as a **single array** (or array-like object).
- **Syntax:** `fn.apply(thisArg, [arg1, arg2, ...])`

> **Memory Trick:**
> - **C**all $\to$ **C**omma-separated
> - **A**pply $\to$ **A**rray

```js
// Passing arguments as an array:
printFullName.apply(person1, ["Pune", "Maharashtra"]); // "Arjun Khade from Pune, Maharashtra"

// Practical Classic Use Case: Finding max/min in an array without the spread operator
const numbers = [5, 12, 8, 130, 44];
const max = Math.max.apply(null, numbers);
console.log(max); // 130
```

---

### 3. `bind()` Method

- **What it does:** Does **not** invoke the function immediately. Instead, it returns a **brand-new copy of the function** with its `this` context permanently bound to the specified object.
- **Syntax:** `const boundFn = fn.bind(thisArg, arg1, arg2, ...)`

```js
// Creates a new function permanently bound to person1:
const printArjunDetails = printFullName.bind(person1, "Pune", "Maharashtra");

// Can be executed anywhere, anytime:
printArjunDetails(); // "Arjun Khade from Pune, Maharashtra"
```

#### Key Use Cases for `bind()`:

1. **Preserving `this` in Asynchronous Callbacks & Event Listeners:**
   ```js
   const user = {
     name: "Arjun",
     greet() {
       console.log(`Hello, ${this.name}!`);
     }
   };

   // Problem: Losing 'this' in setTimeout:
   setTimeout(user.greet, 100); // "Hello, undefined!" (this points to window/timeout object)

   // Solution using bind:
   setTimeout(user.greet.bind(user), 100); // "Hello, Arjun!"
   ```

2. **Function Currying / Partial Application:**
   Pre-filling specific parameters in advance:
   ```js
   function multiply(a, b) {
     return a * b;
   }

   const multiplyByTwo = multiply.bind(null, 2);
   console.log(multiplyByTwo(5));  // 10
   console.log(multiplyByTwo(10)); // 20
   ```

---

### 4. Comparison Table: `call` vs `apply` vs `bind`

| Feature | `call()` | `apply()` | `bind()` |
|---|---|---|---|
| **Execution** | Immediate | Immediate | Deferred (executed later on demand) |
| **Argument Passing** | `fn.call(obj, a, b)` | `fn.apply(obj, [a, b])` | `fn.bind(obj, a, b)` |
| **Return Value** | Return value of function | Return value of function | Brand-new bound function |
| **Common Use Cases** | Method borrowing | Array-based arguments, math operations | Event listeners, timers, function currying |

---

### 5. Interview Classic: Polyfill for `bind()`

Interviewers frequently ask candidates to write a custom implementation of `bind` from scratch:

```js
Function.prototype.myBind = function (context, ...args) {
  const originalFunc = this; // 'this' refers to the function calling myBind

  if (typeof originalFunc !== "function") {
    throw new TypeError("myBind must be called on a function");
  }

  return function (...newArgs) {
    // Combine pre-filled arguments from myBind with newly passed arguments:
    return originalFunc.apply(context, [...args, ...newArgs]);
  };
};

// Testing the custom polyfill:
const customBound = printFullName.myBind(person1, "Pune");
customBound("Maharashtra"); // "Arjun Khade from Pune, Maharashtra"
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Event Loop, Microtask & Macrotask Queue

JavaScript is **single-threaded** — it has only one Call Stack and can do only one thing at a time. The **Event Loop** is the mechanism that allows JavaScript to perform non-blocking asynchronous operations (like timers, network requests, and user events).

### The Architecture: Key Components

1. **Call Stack:** Executes synchronous JavaScript code line by line (LIFO: Last In, First Out).
2. **Web APIs (Browser / Node.js runtime):** Handles background work outside the JS engine (e.g., `setTimeout` timer countdowns, `fetch` HTTP requests, DOM event listeners).
3. **Microtask Queue:** High-priority queue for callbacks from Promises, `async/await`, and `queueMicrotask`.
4. **Macrotask Queue (Callback / Task Queue):** Lower-priority queue for callbacks from `setTimeout`, `setInterval`, DOM event listeners, and I/O.
5. **Event Loop:** The gatekeeper. It constantly monitors:
   - *"Is the Call Stack empty?"*
   - If **yes**, it moves tasks from the queues into the Call Stack for execution.

```
       ┌────────────────────────┐
       │       Call Stack       │ ◄─── (Executes JS code)
       └───────────┬────────────┘
                   │ Hands off async tasks
                   ▼
       ┌────────────────────────┐
       │   Web APIs / Runtime   │ (Timers, Fetch, DOM Events)
       └───────────┬────────────┘
                   │ Pushes ready callbacks
         ┌─────────┴─────────┐
         ▼                   ▼
┌──────────────────┐ ┌──────────────────┐
│ Microtask Queue  │ │ Macrotask Queue  │
│ (High Priority)  │ │ (Lower Priority) │
└────────┬─────────┘ └────────┬─────────┘
         │                    │
         └─────────┬──────────┘
                   ▼
         ┌───────────────────┐
         │    Event Loop     │ ───► Pushes callback to Call Stack (only when stack is empty)
         └───────────────────┘
```

---

### The Execution Rule & Priority

When the **Call Stack is completely empty**, the Event Loop follows this strict order:

1. **Drain the Microtask Queue:** It runs **ALL** microtasks until the queue is completely empty. If a microtask adds another microtask, that new microtask runs in the same cycle!
2. **Render (in browsers):** Browser updates UI / DOM if needed.
3. **Run ONE Macrotask:** The Event Loop picks **one** task from the Macrotask Queue and pushes it to the Call Stack.
4. **Repeat:** It returns to step 1 (checking the Microtask Queue again) before picking the next macrotask.

> **Golden Rule of Priority:**
> **Synchronous Code (Call Stack)** > **Microtask Queue (ALL)** > **Macrotask Queue (ONE at a time)**

---

### Comparison: Microtask Queue vs Macrotask Queue

| Feature | Microtask Queue | Macrotask Queue (Task Queue) |
|---|---|---|
| **Priority** | **Higher** (executed first) | **Lower** (waits for microtasks) |
| **Typical APIs** | `Promise.then()` / `.catch()` / `.finally()`, `await`, `queueMicrotask()`, `MutationObserver` | `setTimeout()`, `setInterval()`, `setImmediate()` (Node.js), DOM Events, I/O |
| **Batch Behavior** | Flushes the **entire queue** until completely empty | Executes **only one task** per event loop tick |
| **Starvation Risk** | **Yes** — an infinite loop of microtasks can starve the macrotask queue and freeze the UI | **No** — browser gets control back between macrotasks |

---

### Classic Interview Code Example (Trace the Output)

```js
console.log("1. Synchronous Start");

setTimeout(() => {
  console.log("2. Macrotask: setTimeout");
}, 0);

Promise.resolve()
  .then(() => {
    console.log("3. Microtask: Promise 1");
  })
  .then(() => {
    console.log("4. Microtask: Promise 2");
  });

queueMicrotask(() => {
  console.log("5. Microtask: queueMicrotask");
});

console.log("6. Synchronous End");
```

**Output:**
```
1. Synchronous Start
6. Synchronous End
3. Microtask: Promise 1
5. Microtask: queueMicrotask
4. Microtask: Promise 2
2. Macrotask: setTimeout
```

**Step-by-Step Walkthrough:**
1. `console.log("1. Synchronous Start")` runs immediately on the Call Stack.
2. `setTimeout(..., 0)` registers a timer with the Web API. When timer expires (0ms), its callback is placed into the **Macrotask Queue**.
3. `Promise.resolve().then(...)` schedules Promise 1 callback into the **Microtask Queue**.
4. `queueMicrotask(...)` schedules its callback into the **Microtask Queue**.
5. `console.log("6. Synchronous End")` runs immediately on the Call Stack.
6. Call Stack is now empty! Event Loop checks the **Microtask Queue**:
   - Runs `Promise 1` callback → returns another promise that enqueues `Promise 2`.
   - Runs `queueMicrotask` callback.
   - Runs `Promise 2` callback.
   - Microtask Queue is now empty!
7. Event Loop moves to the **Macrotask Queue**:
   - Runs `setTimeout` callback.

[↑ Back to Table of Contents](#table-of-contents)

---

## Shallow Copy vs Deep Copy in JavaScript

## 1. Shallow Copy

A shallow copy creates a new object, but nested objects/arrays are still **shared** between the original and the copy.

```js
const person = {
  name: "Arjun",
  address: {
    city: "Pune"
  }
};

const copy = { ...person };

copy.name = "Rahul";
copy.address.city = "Mumbai";

console.log(person);
```

**Output:**

```js
{
  name: "Arjun",
  address: {
    city: "Mumbai"
  }
}
```

**Why?**

- `name` is a primitive value → copied independently.
- `address` is an object → only its **reference** is copied.

**Memory representation:**

```
person ------+
             |
             v
         { city: "Mumbai" }

copy --------+
```

### Common ways to create a shallow copy

```js
const copy1 = { ...obj };
const copy2 = Object.assign({}, obj);
const copy3 = [...arr];
```

---

## 2. Deep Copy

A deep copy creates a completely **independent** copy, including all nested objects and arrays.

```js
const person = {
  name: "Arjun",
  address: {
    city: "Pune"
  }
};

const copy = structuredClone(person);

copy.address.city = "Mumbai";

console.log(person.address.city); // Pune
console.log(copy.address.city);   // Mumbai
```

**Memory representation:**

```
person --> { city: "Pune" }

copy   --> { city: "Mumbai" }
```

Both objects are completely separate.

### Modern way (recommended)

```js
const copy = structuredClone(obj);
```

### Older approach

```js
const copy = JSON.parse(JSON.stringify(obj));
```

**Limitations of `JSON.parse(JSON.stringify(...))`:**

- ❌ Loses `functions`
- ❌ Loses `Date` objects
- ❌ Loses `undefined` values

---

## Angular Interview Answer (Short)

**Shallow Copy:** Copies only the first level of an object. Nested objects are copied by reference, so changes in nested properties affect the original object.

**Deep Copy:** Copies the entire object hierarchy. Nested objects are duplicated, so changes in the copied object do not affect the original object.

**Example:**

```js
const obj1 = { a: 1, b: { c: 2 } };

const shallow = { ...obj1 };
const deep = structuredClone(obj1);
```

> **Why it matters in Angular:** Angular change detection often relies on creating new object references using the spread operator (`...`), which performs a **shallow copy**, not a deep copy. This is why `structuredClone` is preferred when you need full isolation of nested state.

[↑ Back to Table of Contents](#table-of-contents)

---

## ES6 Features (ECMAScript 2015)

ECMAScript 2015 (ES6) was the most significant overhaul in JavaScript's history, introducing foundational features used in modern development.

### 1. `let` and `const` (Block Scoping & TDZ)

- **`var`:** Function-scoped, hoisted and initialized with `undefined`, can be re-declared.
- **`let`:** Block-scoped `{ ... }`, cannot be re-declared in the same scope, can be reassigned. Hoisted into the **Temporal Dead Zone (TDZ)** — accessing before initialization throws a `ReferenceError`.
- **`const`:** Block-scoped, must be initialized at declaration, cannot be reassigned (though internal properties of objects/arrays can still be mutated).

```js
// Block Scoping
{
  var a = 1;
  let b = 2;
  const c = 3;
}
console.log(a); // 1
// console.log(b); // ReferenceError: b is not defined

// const with objects
const user = { name: "Arjun" };
user.name = "Rahul"; // Allowed! (Mutating property)
// user = { name: "Amit" }; // TypeError: Assignment to constant variable
```

---

### 2. Arrow Functions (`=>`)

Provides a shorter syntax and **lexically binds `this`** (inherits `this` from the enclosing scope, rather than defining its own).

```js
// Concise syntax & implicit return
const add = (a, b) => a + b;
const square = x => x * x;

// Lexical `this`
function Person(name) {
  this.name = name;
  
  // Traditional function loses `this` unless bound
  setTimeout(function() {
    console.log("Traditional:", this.name); // undefined
  }, 100);

  // Arrow function retains `this` from Person
  setTimeout(() => {
    console.log("Arrow:", this.name); // "Arjun"
  }, 100);
}

new Person("Arjun");
```

> **Gotchas:** Arrow functions do not have their own `this`, `arguments`, `super`, or `new.target`, and cannot be used as constructors with `new`.

---

### 3. Template Literals

Enclosed by backticks (`` ` ``). Allows string interpolation using `${expression}` and effortless multi-line strings.

```js
const user = "Arjun";
const age = 25;

// String Interpolation
const greeting = `Hello, my name is ${user} and I am ${age} years old. Next year: ${age + 1}.`;

// Multi-line Strings without \n
const html = `
  <div>
    <h1>${user}</h1>
  </div>
`;
```

---

### 4. Destructuring Assignment (Objects & Arrays)

Unpacks values from arrays or properties from objects into distinct variables.

```js
// Object Destructuring
const developer = {
  firstName: "Arjun",
  role: "Full Stack Developer",
  country: "India"
};

// Renaming and default values
const { firstName: name, role, salary = 50000 } = developer;
console.log(name, role, salary); // "Arjun" "Full Stack Developer" 50000

// Array Destructuring & skipping elements
const rgb = [255, 140, 0];
const [red, , blue] = rgb;
console.log(red, blue); // 255 0

// Swapping variables cleanly
let x = 1, y = 2;
[x, y] = [y, x];
console.log(x, y); // 2 1
```

---

### 5. Default Parameters

Allows function parameters to be initialized with default values if no value or `undefined` is passed.

```js
function greet(name = "Guest", greeting = "Hello") {
  return `${greeting}, ${name}!`;
}

console.log(greet());                // "Hello, Guest!"
console.log(greet("Arjun"));         // "Hello, Arjun!"
console.log(greet("Arjun", "Hi"));   // "Hi, Arjun!"
```

---

### 6. Rest (`...`) and Spread (`...`) Operators

- **Rest (`...`)**: Condenses multiple elements/arguments into a single array.
- **Spread (`...`)**: Expands an iterable (array/string/object) into individual elements.

```js
// Rest Parameter (collects remaining arguments into an array)
function sum(multiplier, ...numbers) {
  return numbers.map(n => n * multiplier);
}
console.log(sum(2, 1, 2, 3)); // [2, 4, 6]

// Spread with Arrays (merging/copying)
const arr1 = [1, 2];
const arr2 = [3, 4];
const combined = [...arr1, ...arr2, 5]; // [1, 2, 3, 4, 5]

// Spread with Objects (shallow copy / property override)
const defaults = { theme: "light", fontSize: 14 };
const userSettings = { ...defaults, fontSize: 16 };
console.log(userSettings); // { theme: "light", fontSize: 16 }
```

---

### 7. Enhanced Object Literals

Cleaner ways to define object properties, methods, and dynamic keys.

```js
const name = "Arjun";
const dynamicKey = "status";

const profile = {
  // Property shorthand (same as name: name)
  name,

  // Method shorthand (same as login: function() {})
  login() {
    console.log(`${this.name} logged in`);
  },

  // Computed property keys
  [dynamicKey]: "active"
};

console.log(profile.status); // "active"
profile.login();             // "Arjun logged in"
```

---

### 8. Promises

Native abstraction for asynchronous operations that helps avoid callback hell.

```js
const fetchData = () => {
  return new Promise((resolve, reject) => {
    const success = true;
    setTimeout(() => {
      if (success) resolve({ data: "User payload" });
      else reject(new Error("Network Error"));
    }, 500);
  });
};

fetchData()
  .then(response => console.log(response.data))
  .catch(err => console.error(err.message))
  .finally(() => console.log("Operation finished."));
```

---

### 9. Classes & Inheritance

Syntactic sugar over JavaScript's existing prototype-based inheritance model.

```js
class Vehicle {
  constructor(make, model) {
    this.make = make;
    this.model = model;
  }

  getInfo() {
    return `${this.make} ${this.model}`;
  }

  static isVehicle(obj) {
    return obj instanceof Vehicle;
  }
}

class Car extends Vehicle {
  constructor(make, model, doors) {
    super(make, model); // Calls parent constructor
    this.doors = doors;
  }

  getInfo() {
    return `${super.getInfo()} with ${this.doors} doors`;
  }
}

const myCar = new Car("Tesla", "Model 3", 4);
console.log(myCar.getInfo()); // "Tesla Model 3 with 4 doors"
console.log(Vehicle.isVehicle(myCar)); // true
```

---

### 10. ES Modules (`import` / `export`)

Standardized file-level modularity.

```js
// math.js
export const add = (a, b) => a + b;
export const sub = (a, b) => a - b;
export default function multiply(a, b) { return a * b; }

// main.js
import multiply, { add, sub } from './math.js';
import * as MathOps from './math.js';
```

---

### 11. `Set` and `Map`

- **`Set`:** A collection of **unique** values of any type.
- **`Map`:** Key-value pairs where keys can be **any data type** (including objects and functions, unlike standard objects which only support string/symbol keys).

```js
// Set: Unique values (instant deduplication)
const numbers = [1, 2, 2, 3, 4, 4, 5];
const unique = [...new Set(numbers)];
console.log(unique); // [1, 2, 3, 4, 5]

// Map: Object as a key
const userObj = { id: 1 };
const roleMap = new Map();
roleMap.set(userObj, "Admin");
console.log(roleMap.get(userObj)); // "Admin"
console.log(roleMap.size);         // 1
```

[↑ Back to Table of Contents](#table-of-contents)

---

## Modern JavaScript Features (ES2016 – ES2024+)

Features introduced after ES6 that have become industry standards in modern JavaScript/TypeScript development.

### 1. `async` / `await` (ES2017)

Syntactic sugar built on top of Promises. Allows asynchronous code to be written and read like synchronous code.

```js
async function getUserProfile(userId) {
  try {
    const response = await fetch(`https://api.example.com/users/${userId}`);
    if (!response.ok) throw new Error("User not found");
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching user:", error.message);
  } finally {
    console.log("Fetch attempt completed.");
  }
}
```

---

### 2. Optional Chaining (`?.`) (ES2020)

Safely accesses deeply nested properties, methods, or array indices without having to explicitly verify each reference in the chain.

- If the value before `?.` is `null` or `undefined`, it short-circuits and returns `undefined` instead of throwing a `TypeError`.

```js
const user = {
  profile: {
    name: "Arjun"
    // address is missing
  }
};

// Safe property access
console.log(user?.profile?.name);          // "Arjun"
console.log(user?.profile?.address?.city); // undefined (No TypeError thrown!)

// Safe method call
user.profile?.getDetails?.(); // undefined (safe if function doesn't exist)

// Safe array index
const users = null;
console.log(users?.[0]); // undefined
```

---

### 3. Nullish Coalescing Operator (`??`) (ES2020)

Logical operator that returns the right-hand operand only when the left-hand operand is **`null` or `undefined`** (nullish).

> **Crucial Difference vs `||` (OR):**
> - `||` returns the fallback for **any falsy value** (`0`, `""`, `false`, `NaN`, `null`, `undefined`).
> - `??` returns the fallback **only** for `null` or `undefined`.

```js
const userConfig = {
  animationSpeed: 0,      // 0 is a valid setting!
  darkMode: false,        // false is a valid setting!
  title: ""               // empty string is a valid setting!
};

// With || (Logical OR) - BUGGY
console.log(userConfig.animationSpeed || 300); // 300 (0 is falsely overridden!)
console.log(userConfig.darkMode || true);       // true (false is falsely overridden!)

// With ?? (Nullish Coalescing) - CORRECT
console.log(userConfig.animationSpeed ?? 300); // 0 (preserves 0!)
console.log(userConfig.darkMode ?? true);       // false (preserves false!)
console.log(userConfig.unassignedKey ?? "Default"); // "Default"
```

---

### 4. Logical Assignment Operators (`&&=`, `||=`, `??=`) (ES2021)

Combines logical operators with assignment for cleaner code.

```js
let a = 0;
let b = null;
let c = "Hello";

// Nullish assignment: assigns only if currently null or undefined
b ??= "Default Value"; // b becomes "Default Value"
a ??= 100;             // a remains 0 (0 is not nullish)

// OR assignment: assigns only if currently falsy
let userCount = 0;
userCount ||= 1; // userCount becomes 1 (0 is falsy)

// AND assignment: assigns only if currently truthy
let token = "valid_token";
token &&= `Bearer ${token}`; // "Bearer valid_token"
```

---

### 5. `Object.entries()`, `Object.values()`, `Object.fromEntries()`

Utilities for transforming objects into arrays and vice versa.

```js
const scores = { math: 90, science: 85, history: 92 };

// Object.values: [90, 85, 92]
const total = Object.values(scores).reduce((acc, s) => acc + s, 0);

// Object.entries: [["math", 90], ["science", 85], ["history", 92]]
// Transforming an object using Array methods:
const scaledEntries = Object.entries(scores).map(([subject, score]) => [subject, score + 5]);

// Object.fromEntries: converts 2D key-value array back into an object
const newScores = Object.fromEntries(scaledEntries);
console.log(newScores); // { math: 95, science: 90, history: 97 }
```

---

### 6. `Array.prototype.flat()` and `flatMap()` (ES2019)

- **`.flat(depth)`**: Flattens nested array dimensions into a single array.
- **`.flatMap()`**: Maps each element, then flattens the result by 1 level.

```js
// flat()
const nested = [1, [2, [3, [4]]]];
console.log(nested.flat(1));        // [1, 2, [3, [4]]]
console.log(nested.flat(Infinity)); // [1, 2, 3, 4]

// flatMap()
const phrases = ["hello world", "web development"];
const words = phrases.flatMap(phrase => phrase.split(" "));
console.log(words); // ["hello", "world", "web", "development"]
```

---

### 7. `Array.prototype.at()` (ES2022)

Enables indexing with **positive and negative integers**. Negative integers count backwards from the end of the array.

```js
const frameworks = ["Angular", "React", "Vue", "Svelte"];

// Old way to get the last element:
console.log(frameworks[frameworks.length - 1]); // "Svelte"

// Modern way with .at():
console.log(frameworks.at(-1)); // "Svelte" (last element)
console.log(frameworks.at(-2)); // "Vue"    (second to last element)
console.log(frameworks.at(0));  // "Angular" (first element)
```

---

### 8. Immutable Array Methods: `toSorted()`, `toReversed()`, `toSpliced()`, `with()` (ES2023)

Traditional methods (`sort()`, `reverse()`, `splice()`) mutate the array in place. These modern alternatives return a **new array**, preserving the original (crucial for state immutability in React/Angular/Redux).

```js
const nums = [3, 1, 4, 1, 5];

// toSorted() (does not mutate original)
const sorted = nums.toSorted();
console.log(sorted); // [1, 1, 3, 4, 5]
console.log(nums);   // [3, 1, 4, 1, 5] (original unchanged!)

// toReversed()
const reversed = nums.toReversed();
console.log(reversed); // [5, 1, 4, 1, 3]

// with(index, newValue) - replaces one element immutably
const replaced = nums.with(0, 99);
console.log(replaced); // [99, 1, 4, 1, 5]
```

---

### 9. Modern Promise Combinators (`Promise.allSettled`, `Promise.any`)

| Method | Behavior | Resolves When... | Rejects When... |
|---|---|---|---|
| **`Promise.all`** | All-or-nothing | All promises resolve | **First** promise rejects |
| **`Promise.allSettled`** (ES2020) | Never short-circuits | **All** promises finish (resolved or rejected) | Never rejects |
| **`Promise.any`** (ES2021) | First success wins | **First** promise resolves | **All** promises reject (`AggregateError`) |
| **`Promise.race`** | First settlement wins | First promise resolves | First promise rejects |

```js
const p1 = Promise.resolve("Success 1");
const p2 = Promise.reject("Failure 2");
const p3 = Promise.resolve("Success 3");

// Promise.allSettled: gives status of every promise
Promise.allSettled([p1, p2, p3]).then(results => {
  /*
  [
    { status: "fulfilled", value: "Success 1" },
    { status: "rejected", reason: "Failure 2" },
    { status: "fulfilled", value: "Success 3" }
  ]
  */
  console.log(results);
});

// Promise.any: ignores p2 rejection, resolves with first success
Promise.any([p2, p1, p3]).then(val => {
  console.log("First successful:", val); // "Success 1"
});
```

---

### 10. Private Class Fields (`#field`) (ES2022)

True encapsulation in JavaScript classes using the `#` hash prefix. Private fields cannot be accessed or modified from outside the class instance.

```js
class BankAccount {
  #balance = 0; // Private field

  constructor(initialBalance) {
    this.#balance = initialBalance;
  }

  deposit(amount) {
    if (amount > 0) this.#balance += amount;
  }

  getBalance() {
    return this.#balance;
  }
}

const account = new BankAccount(1000);
account.deposit(500);
console.log(account.getBalance()); // 1500

// Direct access is impossible:
// console.log(account.#balance); // SyntaxError: Private field '#balance' must be declared in an enclosing class
```

---

### 11. `structuredClone()` (Built-in Deep Copy)

The modern, native standard for deep copying complex nested objects and arrays without losing types or using buggy `JSON.parse(JSON.stringify())` hacks.

```js
const original = {
  user: "Arjun",
  joined: new Date(),
  tags: ["dev", "js"],
  nested: { key: "value" }
};

const copy = structuredClone(original);
copy.nested.key = "changed";
console.log(original.nested.key); // "value" (completely independent!)
console.log(copy.joined instanceof Date); // true (retains Date object type!)
```

[↑ Back to Table of Contents](#table-of-contents)
