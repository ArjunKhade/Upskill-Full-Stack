# JavaScript Fundamentals

A reference guide covering core JavaScript execution concepts: synchronous vs asynchronous behavior, execution context, hoisting, closures, and functions.

## Table of Contents

- [Synchronous vs Asynchronous](#synchronous-vs-asynchronous)
- [Execution Context](#execution-context)
- [Call Stack](#call-stack)
- [Hoisting](#hoisting)
- [Closures](#closures)
- [Function Statement vs Function Expression](#function-statement-vs-function-expression)
- [Anonymous Functions](#anonymous-functions)
- [Named Function Expressions](#named-function-expressions)
- [Parameters vs Arguments](#parameters-vs-arguments)
- [First-Class Functions](#first-class-functions)
- [Higher Order Functions](#higher-order-functions)

---

## Synchronous vs Asynchronous

JavaScript is a **synchronous, single-threaded language**.

- Code is executed line by line.
- One task is completed at a time, in order.
- JavaScript waits for the current task to finish before moving to the next line.

**Synchronous** means tasks execute one after another — JavaScript waits for the current operation to complete before moving on.

**Asynchronous** means JavaScript can start a task and continue executing other code without waiting for that task to finish. Once the task completes, its result is handled later using callbacks, Promises, or `async`/`await`.

> **Example:** API calls, timers, and file operations are asynchronous in JavaScript because they take time and shouldn't block the rest of the application.

---

## Execution Context

Every time JavaScript code runs, it does so inside an **execution context**, made up of two components:

- **Memory Component (Variable Environment)** — stores variables and functions as key-value pairs.
- **Code Component (Thread of Execution)** — executes the code line by line.

---

## Call Stack

The **call stack** maintains the order of execution of execution contexts. It tracks which function is currently running and what to return to once that function completes.

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

---

## Anonymous Functions

Functions without a name. They're primarily used in contexts where functions are treated as values, such as when assigning them to a variable or passing them as an argument.

```js
const greet = function () {
  console.log("Hi there!");
};
```

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

---

## First-Class Functions

Also known as **first-class citizens**, this concept refers to the ability of functions to be treated like any other variable/value in the language.

Functions can be:

1. Assigned to variables.
2. Passed as arguments to other functions.
3. Returned from other functions.

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
console.log(radius.map(area)); pollyfill for map function in javascript


```
