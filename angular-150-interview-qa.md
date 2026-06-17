# Angular Interview Questions & Answers

---

## Table of Contents

1. [Angular Fundamentals](#1-angular-fundamentals)
2. [Components](#2-components)
3. [Directives & Pipes](#3-directives--pipes)
4. [Data Binding & Forms](#4-data-binding--forms)
5. [Services & Dependency Injection](#5-services--dependency-injection)
6. [Routing & Navigation](#6-routing--navigation)

---

## 1. Angular Fundamentals

### Q1. What is Angular, and why is it used for web development?

Angular is a TypeScript-based front-end framework developed by Google. It is used to build scalable, maintainable single-page applications (SPAs).

**Why it's used:**

- **Component-based architecture** – Promotes reusability and clean separation of concerns.
- **Two-way data binding** – Simplifies DOM and model synchronization.
- **Dependency injection** – Enhances testability and modular design.
- **Routing** – Supports navigation, lazy loading, and guards.
- **Built-in tooling (CLI)** – Speeds up development and enforces best practices.
- **AOT compilation** – Improves performance by compiling ahead of time.
- **RxJS integration** – Enables reactive, asynchronous data handling.

It is especially strong in enterprise-level applications due to its structure and long-term support.

---

### Q2. How does Angular differ from AngularJS?

Angular (2+) is a complete rewrite of AngularJS. Key differences:

| Feature | AngularJS | Angular (2+) |
|---|---|---|
| Language | JavaScript | TypeScript |
| Architecture | MVC-based | Component-based |
| Performance | Digest cycle; can be slow in large apps | Faster with AOT and improved change detection |
| Mobile Support | Not optimized | Mobile-first design |
| Dependency Injection | Basic DI | Hierarchical, more powerful DI |
| Tooling | Manual setup | CLI, RxJS, TypeScript, Webpack |
| Routing & Modularity | Basic routing, limited modularity | Advanced routing with lazy loading and feature modules |

---

### Q3. What is TypeScript, and why is it used in Angular?

TypeScript is a superset of JavaScript developed by Microsoft. It adds static typing, interfaces, generics, and modern ES features.

**Why Angular uses TypeScript:**

1. **Static Typing** – Catches errors at compile time, improving code quality.
2. **IntelliSense Support** – Better tooling and autocompletion in IDEs.
3. **OOP Features** – Supports classes, interfaces, and access modifiers.
4. **Maintainability** – Helps manage large codebases effectively.
5. **Early Error Detection** – Improves developer productivity and reduces runtime issues.
6. **Better Refactoring** – Safer and easier code refactors in large projects.

---

### Q4. Explain the component-based architecture of Angular.

Angular follows a component-based architecture where the UI is built using independent, reusable components.

**Key Points:**

1. Component = Template + Class + Metadata (via `@Component` decorator).
2. Each component controls a section of the UI (called a view).
3. Components are organized in a tree structure — `AppComponent` is the root.
4. Promotes reusability, testability, and separation of concerns.
5. Encourages modular design — each feature can be built as a self-contained module.
6. Components communicate using:
   - `@Input()` – to receive data from a parent.
   - `@Output()` – to send events to a parent.
7. Supports encapsulation via `ViewEncapsulation` and scoped styles.

---

### Q5. What is the purpose of the `angular.json` file in an Angular project?

`angular.json` is the workspace configuration file for Angular CLI projects.

**Key purposes:**

1. Defines project structure and settings.
2. Configures build, serve, test, and lint options.
3. Specifies file paths for source files (`main.ts`, styles, polyfills, assets, etc.).
4. Manages configurations for different environments (development, production).
5. Allows customization of output directories and optimization settings.
6. Supports multiple projects (apps and libraries) in a single workspace.

---

### Q6. How does an Angular application bootstrap?

Bootstrapping is the process of initializing the Angular application.

**Steps:**

1. Angular looks for `main.ts` as the entry point.
2. `main.ts` calls `platformBrowserDynamic().bootstrapModule(AppModule)` to bootstrap the root module.
3. `AppModule` is the root module, defined with `@NgModule`.
4. `AppModule` declares `AppComponent` in the `bootstrap` array.
5. Angular creates and inserts `AppComponent` into `index.html` using the selector (e.g., `<app-root>`).

---

### Q7. What is the role of the `main.ts` file in Angular?

`main.ts` is the **entry point** of an Angular application.

**Key roles:**

1. Boots up the Angular application by calling `platformBrowserDynamic().bootstrapModule(AppModule)`.
2. Tells Angular which root module (`AppModule`) to load.
3. Handles environment-specific setups, such as enabling production mode.
4. Responsible for starting the Angular runtime and rendering the app in the browser.

---

### Q8. What are NgModules, and why are they used?

NgModules are the core organizational units in Angular that group related components, directives, pipes, and services.

1. They help break down large apps into smaller, manageable, and reusable feature modules.
2. Each NgModule, defined with `@NgModule`, declares what it contains, what it imports, and what it exports.
3. They control service scope through providers, enabling lazy loading and efficient dependency injection.
4. The root module bootstraps the app by specifying the root component.
5. NgModules support lazy loading, allowing parts of the app to load only when needed.
6. They encapsulate features, promoting separation of concerns and better team collaboration.

---

### Q9. What is the difference between a module and a component in Angular?

A **module** is a container that groups related components, directives, pipes, and services. A **component** is a building block of the UI that controls a part of the screen.

- Modules use `@NgModule`; components use `@Component`.
- Components must be declared in a module to work.

---

### Q10. What is the purpose of the `@NgModule` decorator?

`@NgModule` defines a module's metadata. It tells Angular how to compile and run the module by declaring components, importing other modules, providing services, and specifying the bootstrap component.

---

### Q11. How does Angular handle dependency injection?

Angular has a built-in dependency injection (DI) system. It automatically provides class instances (like services) to components or other services via constructors. Providers are registered in modules, components, or services using the `providers` array.

---

### Q12. What is the difference between a provider and an injectable in Angular?

- `@Injectable` marks a class as available for dependency injection. The `providedIn` property (commonly set to `'root'`) tells Angular where to register the service.
- A **provider** defines how and where the service instance should be created.

In short: `@Injectable` makes a class injectable; a provider controls its instantiation.

---

### Q13. Explain the concept of Single Page Applications (SPAs) in Angular.

SPAs load a single HTML page and dynamically update the content without reloading the page. Angular uses its router to switch views and update the URL while keeping the app state in memory. This results in faster navigation and a smoother user experience.

---

### Q14. What is the Angular CLI, and what are its common commands?

Angular CLI is a command-line tool to create, build, test, and manage Angular projects.

| Command | Purpose |
|---|---|
| `ng new` | Create a new Angular project |
| `ng serve` | Run the app locally |
| `ng build` | Build the app for production |
| `ng generate` | Generate components, services, etc. |
| `ng test` | Run unit tests |
| `ng lint` | Run code linter |

---

### Q15. What is the difference between JIT and AOT compilation in Angular?

- **JIT (Just-in-Time):** Compiles the app in the browser at runtime. Slower; typically used during development.
- **AOT (Ahead-of-Time):** Compiles the app during build time. Produces faster startup and smaller bundles; used for production.

---

### Q16. Why is AOT compilation preferred for production builds?

AOT compiles the app before runtime, resulting in:
- Faster rendering
- Smaller bundle sizes
- Better security by detecting template errors early
- Improved overall performance

---

### Q17. How does Angular differ from React or Vue in terms of architecture?

- **Angular** is a full-featured framework with built-in routing, dependency injection, and opinionated structure (TypeScript required).
- **React** and **Vue** are primarily UI libraries focused on the view layer, requiring additional libraries for routing and state management.
- React and Vue are more flexible and lightweight; Angular is more structured, which suits large enterprise apps.

---

### Q18. What is the use of the `environment.ts` file?

`environment.ts` stores environment-specific settings like API URLs or feature flags. Angular uses different versions (e.g., `environment.prod.ts`) to configure the app for development, production, or testing during the build process.

---

## 2. Components

### Q19. What is a component in Angular, and how is it defined?

A component controls a part of the UI. It is defined using the `@Component` decorator, which specifies the selector, template, and styles. The component class contains the logic and data binding for the view.

---

### Q20. What is the `@Component` decorator, and what are its key properties?

`@Component` marks a class as an Angular component and provides metadata.

| Property | Purpose |
|---|---|
| `selector` | The HTML tag used to render the component |
| `templateUrl` / `template` | The HTML view (external file or inline) |
| `styleUrls` / `styles` | Component-specific CSS |
| `providers` | Services scoped to the component |

---

### Q21. Explain the role of templates in Angular components.

Templates define the HTML view of a component. They determine what gets rendered on the screen and bind the component's data and events to the UI using Angular's template syntax.

---

### Q22. What is the difference between `template` and `templateUrl` in a component?

- `template`: Defines the HTML inline inside the decorator.
- `templateUrl`: Points to an external HTML file for the component's template.

---

### Q23. How does Angular handle component lifecycle hooks?

Angular provides lifecycle hook methods that run at specific stages of a component's life.

| Hook | When it runs |
|---|---|
| `ngOnInit()` | After component initialization |
| `ngOnChanges()` | When input properties change |
| `ngOnDestroy()` | Before the component is removed |

---

### Q24. List all Angular lifecycle hooks and their purposes.

| Hook | Purpose |
|---|---|
| `ngOnChanges()` | Called when input properties change |
| `ngOnInit()` | Called once after first `ngOnChanges`; used for initialization logic |
| `ngDoCheck()` | Custom change detection |
| `ngAfterContentInit()` | After content projection (`ng-content`) is complete |
| `ngAfterContentChecked()` | After projected content is checked |
| `ngAfterViewInit()` | After the component's view is initialized |
| `ngAfterViewChecked()` | After the view is checked by change detection |
| `ngOnDestroy()` | Before the component is destroyed; used for cleanup |

---

### Q25. What is the difference between `ngOnInit` and `constructor` in a component?

- **Constructor:** Used to inject dependencies and set up basic class initialization. Angular injects services here.
- **`ngOnInit`:** A lifecycle hook called after the component is initialized; used for logic like data fetching.

Avoid placing heavy logic inside the constructor.

---

### Q26. How can you pass data to a component using `@Input`?

Use the `@Input()` decorator to define a property in the child component, then bind a value to it from the parent using property binding.

```typescript
// Child
@Input() title: string;

// Parent template
<app-child [title]="parentTitle"></app-child>
```

---

### Q27. What is the purpose of `@Output` and `EventEmitter` in Angular?

`@Output` allows a child component to send data to its parent using `EventEmitter` to emit custom events.

```typescript
// Child
@Output() clicked = new EventEmitter<string>();
this.clicked.emit('Hello');

// Parent template
<app-child (clicked)="handleEvent($event)"></app-child>
```

---

### Q28. How do you create a dynamic component in Angular?

Use `ViewContainerRef` with `createComponent()` (Angular 13+):

```typescript
@ViewChild('container', { read: ViewContainerRef }) container: ViewContainerRef;

// Angular 13+
const compRef = this.container.createComponent(YourComponent);
```

---

### Q29. What is `ViewEncapsulation` in Angular, and what are its types?

`ViewEncapsulation` controls how a component's styles affect the DOM.

| Type | Description |
|---|---|
| `Emulated` (default) | Scopes styles using attribute selectors |
| `None` | Styles are global and not scoped |
| `ShadowDom` | Uses native Shadow DOM for strict encapsulation |

---

### Q30. How does Angular handle component communication?

- **Parent → Child:** `@Input()`
- **Child → Parent:** `@Output()` with `EventEmitter`
- **Siblings / distant components:** Shared service with RxJS `Subject` / `BehaviorSubject`
- **Direct interaction:** Template references or `ViewChild` / `ViewChildren`

---

### Q31. What is the difference between `ViewChild` and `ViewChildren`?

- `@ViewChild` gets a reference to a **single** child element or component.
- `@ViewChildren` gets references to **multiple** child elements or components as a `QueryList`.

---

### Q32. How do you access a DOM element in an Angular component?

Use `@ViewChild` with a template reference variable:

```typescript
// Template
<div #myDiv></div>

// Component
@ViewChild('myDiv') myDivElement: ElementRef;

// Access native element
this.myDivElement.nativeElement
```

---

### Q33. What is Content Projection in Angular, and how is it implemented?

Content Projection lets you insert external content into a component's template using `<ng-content>`.

```html
<!-- Parent -->
<app-card>
  <p>Projected content here</p>
</app-card>

<!-- Card component template -->
<div class="card">
  <ng-content></ng-content>
</div>
```

---

### Q34. Explain the use of `ng-content` in Angular components.

`<ng-content>` acts as a placeholder in a component's template where external content is inserted. It enables content projection, allowing parent components to pass HTML or child components into a component's template.

---

### Q35. How can you style a component in Angular?

- Adding CSS in the component's `styleUrls` or `styles` metadata.
- Using inline styles inside the template.
- Using global styles in `styles.css` (affects the whole app).
- `ViewEncapsulation` controls the style scope.

---

### Q36–Q52. Scenario-Based Component Questions

**Q36. Conditional rendering:** Use `*ngIf` to conditionally include or exclude elements from the DOM.

```html
<div *ngIf="isVisible">Content shown only if isVisible is true</div>
```

**Q37. Purpose of `*ngIf`:** Conditionally adds or removes elements from the DOM, improving performance by not rendering unnecessary elements.

**Q38. Outdated data from async updates:** Use the `async` pipe with Observables, or call `ChangeDetectorRef.detectChanges()` manually. Ensure updates happen inside Angular zones (`NgZone`).

**Q39. Large dataset rendering (10,000 rows):**
- Use Angular CDK's virtual scrolling (`*cdkVirtualFor`).
- Paginate the data.
- Use `trackBy` with `*ngFor`.
- Apply `OnPush` change detection strategy.

**Q40. Prevent unnecessary re-renders with frequent `@Input` changes:** Use `ChangeDetectionStrategy.OnPush` in the child component and pass immutable data.

**Q41. Slow complex form:** Profile with Chrome DevTools + Angular DevTools. Apply `OnPush`, break into smaller components, debounce `valueChanges`, use `trackBy`.

**Q42. Dynamic layout switching:**

```html
<ng-container [ngSwitch]="layoutType">
  <app-grid-layout *ngSwitchCase="'grid'"></app-grid-layout>
  <app-list-layout *ngSwitchCase="'list'"></app-list-layout>
</ng-container>
```

**Q43. `ViewChild` undefined during `ngOnInit`:** Access `@ViewChild` references in `ngAfterViewInit`, not `ngOnInit`. Use `{ static: false }` when the element is inside `*ngIf`.

**Q44. Reusable modal component:** Use `<ng-content>` or `ViewContainerRef`. Use a shared service to open/close the modal and pass data via `@Input()` or `<ng-template>`.

**Q45. Runtime error from missing property:** Use optional chaining (`user?.name`), enable Angular strict mode, and guard template rendering with `*ngIf`.

**Q46. Sharing state between sibling components:** Use a shared singleton service with `BehaviorSubject` or `ReplaySubject`. Both siblings subscribe to the observable to receive and push updates.

**Q47. Lifecycle hook called unexpectedly multiple times:** Check for component re-creation due to routing or structural directives. Use `OnPush` change detection and Angular DevTools to trace lifecycle calls.

**Q48. Drag-and-drop interface:** Use Angular CDK `DragDropModule` with `cdkDrag` and `cdkDropList` directives.

**Q49. Deeply nested template performance:** Break into smaller child components with `OnPush`, flatten `*ngIf`/`*ngFor` structures, move complex logic to the component class, and use lazy loading.

**Q50. Light/Dark theme support:**

```typescript
document.body.classList.toggle('dark-theme', isDarkTheme);
```

Store the user preference in `localStorage` for persistence.

**Q51. UI not updating after state change:** Check if `OnPush` strategy is in use and whether input references changed. Confirm async operations are inside Angular zone and use `ChangeDetectorRef.detectChanges()` if needed.

**Q52. Permission-based rendering:** Use a permission service, guard elements with `*ngIf`, or create a custom structural directive like `*hasPermission`. Use route guards for route-level protection.

---

## 3. Directives & Pipes

### Q53. What are directives in Angular, and how are they used?

Directives are classes that add behavior to elements in Angular.

| Type | Description | Example |
|---|---|---|
| Component | A directive with a template | `<app-user>` |
| Structural | Changes DOM layout | `*ngIf`, `*ngFor` |
| Attribute | Changes appearance/behavior | `[ngClass]`, custom highlight |

---

### Q54. What is the difference between a component directive and an attribute directive?

- **Component directive:** Has a template and defines a view (e.g., `<app-user></app-user>`).
- **Attribute directive:** Alters the behavior or appearance of an existing element (e.g., `[ngClass]`).

---

### Q55. Name some built-in Angular directives.

**Structural:** `*ngIf`, `*ngFor`, `*ngSwitch`, `*ngSwitchCase`, `*ngSwitchDefault`

**Attribute:** `[ngClass]`, `[ngStyle]`, `[ngModel]`

---

### Q56. How do you create a custom directive in Angular?

```typescript
@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective {
  constructor(el: ElementRef) {
    el.nativeElement.style.backgroundColor = 'yellow';
  }
}
```

Generate with: `ng generate directive myDirective`

---

### Q57. What is the purpose of the `*ngFor` directive?

`*ngFor` is a structural directive used to loop over a collection and render a template for each item.

```html
<li *ngFor="let item of items">{{ item }}</li>
```

---

### Q58. Explain the difference between `*ngFor` and `ng-repeat`.

| Feature | `*ngFor` (Angular 2+) | `ng-repeat` (AngularJS) |
|---|---|---|
| Syntax | `*ngFor="let item of items"` | `ng-repeat="item in items"` |
| Performance | Supports `trackBy` | No `trackBy` equivalent |
| Change detection | Zone.js-based | Digest cycle |

---

### Q59. How does the `ngClass` directive work?

`ngClass` dynamically adds or removes CSS classes.

```html
<div [ngClass]="'active'"></div>
<div [ngClass]="{ 'active': isActive, 'disabled': isDisabled }"></div>
<div [ngClass]="['class1', 'class2']"></div>
```

---

### Q60. What is the purpose of the `ngStyle` directive?

`ngStyle` sets inline CSS styles dynamically.

```html
<div [ngStyle]="{ 'color': isActive ? 'green' : 'red', 'font-size': '16px' }">
  Styled Text
</div>
```

---

### Q61. How do you create a structural directive in Angular?

```typescript
@Directive({ selector: '[appIf]' })
export class AppIfDirective {
  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef
  ) {}

  @Input() set appIf(condition: boolean) {
    this.viewContainer.clear();
    if (condition) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    }
  }
}
```

---

### Q62. What are pipes in Angular, and how are they used?

Pipes transform data in templates.

```html
{{ value | pipeName }}
{{ today | date:'short' }}
{{ name | uppercase }}
{{ value | pipe1 | pipe2 }}  <!-- chaining -->
```

---

### Q63. Name some built-in Angular pipes.

`date`, `uppercase`, `lowercase`, `currency`, `number`, `percent`, `json`, `slice`, `async`

---

### Q64. How do you create a custom pipe in Angular?

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'reverseStr' })
export class ReverseStrPipe implements PipeTransform {
  transform(value: string): string {
    return value.split('').reverse().join('');
  }
}
```

Generate with: `ng generate pipe myPipe`

---

### Q65. What is the difference between pure and impure pipes?

| | Pure | Impure |
|---|---|---|
| Runs when | Input reference changes | Every change detection cycle |
| Performance | Efficient | Can be costly |
| Default | Yes (`pure: true`) | `pure: false` |

---

### Q66. How does the `async` pipe work in Angular?

The `async` pipe subscribes to Observables or Promises automatically, unwraps emitted values, and unsubscribes on component destruction to prevent memory leaks.

```html
{{ data$ | async }}
```

---

### Q67. How can you chain multiple pipes in Angular?

```html
{{ value | pipe1 | pipe2 | pipe3 }}
```

Output of `pipe1` becomes input for `pipe2`, and so on.

---

### Q68. What is the purpose of the `slice` pipe?

Extracts a subset of elements from an array or characters from a string.

```html
{{ 'Angular' | slice:0:3 }}   <!-- 'Ang' -->
{{ [1,2,3,4] | slice:1:3 }}  <!-- [2,3] -->
```

---

### Q69. How do you pass parameters to a custom pipe?

```html
{{ value | myPipe:param1:param2 }}
```

```typescript
transform(value: any, param1: any, param2: any) { ... }
```

---

### Q70–Q80. Scenario-Based Directive & Pipe Questions

**Q70. Debugging a custom directive or pipe:** Use `console.log`, Angular DevTools, IDE breakpoints, lifecycle hooks, and unit tests.

**Q71. Highlight table rows based on value thresholds:**

```typescript
@Directive({ selector: '[appHighlightRow]' })
export class HighlightRowDirective implements OnChanges {
  @Input('appHighlightRow') value: number = 0;

  constructor(private el: ElementRef) {}

  ngOnChanges() {
    const bg = this.value > 100 ? 'lightgreen' : this.value < 0 ? 'lightcoral' : 'white';
    this.el.nativeElement.style.backgroundColor = bg;
  }
}
```

**Q72. Custom directive not applying changes:** Check selector, `@Input` values, lifecycle hooks, DOM inspection, module declaration, and trace with breakpoints.

**Q73. Restrict input to numbers only:**

```typescript
@HostListener('input', ['$event']) onInputChange(event: Event) {
  const input = event.target as HTMLInputElement;
  input.value = input.value.replace(/[^0-9]/g, '');
}
```

**Q74. Currency conversion pipe with locale:** Create a pipe that accepts a currency code and locale, and delegates to Angular's built-in `CurrencyPipe` internally.

**Q75. Refactoring impure to pure pipe:** Make the pipe stateless, avoid modifying inputs, and use immutable operations (e.g., spread operator for arrays).

**Q76. `*ngFor` rendering incorrect data due to array mutation:** Avoid `push`/`splice`; use immutable operations and assign a new array reference. Use `trackBy` to optimize rendering.

**Q77. Dynamic tooltips directive:** Use `HostListener` to detect `mouseenter`/`mouseleave` and set/remove the `title` attribute with `Renderer2`.

**Q78. Custom pipe not updating on input changes:** If the pipe is pure (default), mutating the input won't trigger re-evaluation. Assign a new object/array reference, or mark the pipe as `pure: false` if mutation is unavoidable.

**Q79. Click-outside directive:**

```typescript
@HostListener('document:click', ['$event.target'])
onClick(target: HTMLElement) {
  if (!this.el.nativeElement.contains(target)) {
    this.appClickOutside.emit();
  }
}
```

**Q80. Overuse of pipes in templates:** Move complex logic to the component class and bind results to variables. Cache results and avoid chaining multiple expensive pipes in templates.

---

## 4. Data Binding & Forms

### Q81. What is data binding in Angular, and what are its types?

Data binding connects component logic to the UI.

| Type | Syntax | Direction |
|---|---|---|
| Interpolation | `{{ expression }}` | Component → View |
| Property binding | `[property]="expression"` | Component → View |
| Event binding | `(event)="handler()"` | View → Component |
| Two-way binding | `[(ngModel)]="property"` | Both directions |

---

### Q82–Q115. Forms Reference

**Q82. One-way data binding:** Data flows one direction — either component → view (interpolation, property binding) or view → component (event binding).

**Q83. Two-way data binding:** Uses `[(ngModel)]` to sync data between component and view in both directions. Requires `FormsModule`.

**Q84. Interpolation:** Syntax `{{ expression }}` binds component data to the template as text.

**Q85. Property binding vs interpolation:** `[property]="expression"` binds to HTML element properties and supports non-string values. Interpolation is limited to text content.

**Q86. Event binding:**

```html
<button (click)="onClick()">Click</button>
```

**Q87. `ngModel` two-way binding:** `[(ngModel)]` combines `[value]` (property binding) and `(input)` (event binding). Requires `FormsModule` to be imported.

**Q88. Reactive Forms:** Forms driven by TypeScript code using `FormGroup`, `FormControl`, and `FormBuilder`. Preferred for dynamic and complex forms.

**Q89. Template-driven vs Reactive Forms:**

| | Template-driven | Reactive |
|---|---|---|
| Definition | HTML template | TypeScript class |
| Complexity | Simpler | Better for complex logic |
| Scalability | Limited | Highly scalable |
| Testing | Harder | Easier |

**Q90. Creating a form control:**

```typescript
this.name = new FormControl('');
// or
this.form = this.fb.group({ name: [''] });
```

**Q91. `FormGroup` and `FormControl`:**
- `FormGroup`: A collection of `FormControl` instances.
- `FormControl`: Tracks the value and validation status of a single field.

**Q92. Form validation:** Use built-in or custom validators attached to `FormControl` or `FormBuilder`.

**Q93. Common built-in validators:** `Validators.required`, `Validators.minLength()`, `Validators.maxLength()`, `Validators.email`, `Validators.pattern()`.

**Q94. Custom validator:**

```typescript
function customValidator(control: AbstractControl) {
  return control.value === 'admin' ? { invalidName: true } : null;
}
```

**Q95. `FormBuilder`:** Simplifies creation of `FormGroup`/`FormControl` with less boilerplate code.

**Q96. Form submission:**

```html
<form [formGroup]="form" (ngSubmit)="onSubmit()">
```

```typescript
onSubmit() { console.log(this.form.value); }
```

**Q97. `dirty` vs `pristine`:**
- `dirty`: The form/control value has changed since it was loaded.
- `pristine`: The form/control value has not changed since it was loaded.

**Q98. Resetting a form:** Call `form.reset()` to clear values and reset state back to `pristine`.

**Q99. Purpose of `ngForm`:** Creates a top-level `FormGroup` instance in template-driven forms and tracks form state (validity, controls, submission status).

**Q100. Cross-field validation:**

```typescript
const form = this.fb.group({
  password: [''],
  confirmPassword: ['']
}, { validators: this.passwordMatchValidator });

passwordMatchValidator(group: FormGroup) {
  const pass = group.get('password')?.value;
  const confirm = group.get('confirmPassword')?.value;
  return pass === confirm ? null : { passwordMismatch: true };
}
```

**Q101. Preventing infinite loops with `ngModel`:** Separate update logic from binding using `ngModelChange`:

```html
<input [ngModel]="value" (ngModelChange)="onValueChange($event)">
```

```typescript
onValueChange(newValue: string) {
  if (this.value !== newValue) { this.value = newValue; }
}
```

**Q102. Reactive form validation not triggering:** Verify validators are attached and controls are bound with `formControlName`. Call `updateValueAndValidity()` for dynamically updated controls. Use `markAllAsTouched()` on submission.

**Q103. Tracking unexpected control value changes:** Subscribe to `valueChanges`, check template bindings, verify no unintended external updates, and use `{ emitEvent: false }` when programmatically setting values.

**Q104. Dynamic form with `FormArray`:** Use `FormArray` inside a `FormGroup`. Push or remove `FormControl` instances on user action. Bind in the template using `formArrayName`.

**Q105. Handling server errors on submission:** Catch the error in `catchError`, show a user-friendly error message (toast/snackbar/inline alert), and keep the form editable for resubmission.

**Q106. Sync form data across browser tabs:** Save form data to `localStorage` on every change. Listen to the browser's `storage` event to detect updates from other tabs.

**Q107. Large reactive form performance:** Break into nested `FormGroup` sections, use `OnPush` change detection, debounce `valueChanges`, and conditionally render sections with `*ngIf`.

**Q108. Template-driven form not binding correctly:** Ensure `FormsModule` is imported. Verify correct use of `[(ngModel)]` with `name` attributes and that component properties are declared and initialized.

**Q109. Custom form control with `ControlValueAccessor`:** Implement `writeValue()`, `registerOnChange()`, `registerOnTouched()`, and `setDisabledState()`. Provide `NG_VALUE_ACCESSOR` in the component's `providers`.

**Q110. Autosave functionality:** Subscribe to `valueChanges` with `debounceTime()`, then trigger a save to the backend or `localStorage`.

**Q111. Slow validators:** Use async validators for expensive checks and debounce triggers. Avoid heavy synchronous computations in validators.

**Q112. Sync and async validation together:** Combine synchronous validators in the `validators` array and pass async validators in `asyncValidators` on `FormControl` or `FormGroup`.

**Q113. Displaying field-specific error messages:** Check each `FormControl`'s `invalid` and `touched`/`dirty` state and use `*ngIf` with specific error keys (`required`, `minlength`, etc.).

**Q114. Undo/redo for form inputs:** Maintain a history stack of form state snapshots. On undo, revert to the previous state; on redo, move forward. Use deep copies to prevent mutation issues.

**Q115. Throttling `valueChanges`:** Apply `debounceTime()` or `throttleTime()` on the `valueChanges` observable to reduce processing frequency.

---

## 5. Services & Dependency Injection

### Q116. What is a service in Angular, and how is it created?

A service is a class used to share data or logic across components. Created with `ng generate service service-name` and decorated with `@Injectable()`.

---

### Q117. How do you inject a service into a component?

```typescript
constructor(private myService: MyService) {}
```

---

### Q118. What is the role of the `@Injectable` decorator?

It marks a class as available for dependency injection and allows Angular to inject dependencies into that class.

---

### Q119. Explain hierarchical dependency injection in Angular.

Angular creates injectors in a tree structure. A service provided at a **component** level creates a new instance scoped to that component subtree. A service provided at **root** is shared globally as a singleton.

---

### Q120. What is the difference between `providedIn: 'root'` and module-level providers?

- `providedIn: 'root'`: Singleton across the entire application.
- Module-level providers: Instances scoped to that module's component tree.

---

### Q121. How do you create a singleton service in Angular?

Use `providedIn: 'root'` in the `@Injectable()` decorator:

```typescript
@Injectable({ providedIn: 'root' })
export class MyService { }
```

---

### Q122. What is the purpose of the `Injector` in Angular?

The `Injector` is Angular's DI system that resolves and provides dependencies to components, directives, and services at runtime.

---

### Q123. How do you pass data between services in Angular?

Use a shared service with class properties or `Subject`/`BehaviorSubject` for reactive, observable-based updates.

---

### Q124. What is the difference between a service and a component?

- A **component** controls a view (HTML + logic) and handles user interaction.
- A **service** contains reusable business logic or shared data and has no UI. Components consume services to separate concerns.

---

### Q125. How do you handle HTTP requests in an Angular service?

```typescript
@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>('/api/users');
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>('/api/users', user);
  }
}
```

---

### Q126. What is the `HttpClient` module, and how is it used?

`HttpClientModule` (from `@angular/common/http`) provides the `HttpClient` service. Import it in `AppModule`, then inject `HttpClient` in services to make GET, POST, PUT, DELETE requests.

---

### Q127. How do you handle errors in HTTP requests?

Use the `catchError` RxJS operator inside the service method:

```typescript
getData(): Observable<Data> {
  return this.http.get<Data>('/api/data').pipe(
    catchError((error: HttpErrorResponse) => {
      console.error('Error:', error.message);
      return throwError(() => new Error('Failed to load data'));
    })
  );
}
```

---

### Q128. What is the purpose of `HttpInterceptor` in Angular?

`HttpInterceptor` intercepts and modifies outgoing requests and incoming responses globally. Common uses: adding auth tokens, logging, global error handling, and response transformation.

```typescript
intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  const authReq = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${token}`)
  });
  return next.handle(authReq);
}
```

---

### Q129. How do you mock a service for testing in Angular?

```typescript
class MockUserService {
  getUser() { return of({ name: 'Test User' }); }
}

TestBed.configureTestingModule({
  providers: [{ provide: UserService, useClass: MockUserService }]
});
```

Or use Jasmine spies:

```typescript
const mockService = jasmine.createSpyObj('UserService', ['getUser']);
mockService.getUser.and.returnValue(of({ name: 'Test' }));
```

---

### Q130. Best practices for structuring Angular services.

- Single responsibility: one service per concern.
- Use `providedIn: 'root'` for singletons.
- Avoid accessing the DOM directly in services.
- Use Observables for async operations.
- Organize services into feature folders.
- Encapsulate HTTP logic in services, not components.

---

### Q131–Q140. Scenario-Based Service Questions

**Q131. Intermittent HTTP failures:** Use browser DevTools network logs, add detailed error logging, apply the RxJS `retry()` operator, implement exponential backoff, and handle errors gracefully with user-friendly messages.

**Q132. Caching mechanism to reduce HTTP calls:**

```typescript
private cache = new Map<string, any>();

getData(url: string): Observable<any> {
  if (this.cache.has(url)) return of(this.cache.get(url));
  return this.http.get(url).pipe(
    tap(data => this.cache.set(url, data)),
    shareReplay(1)
  );
}
```

**Q133. Ensuring a service remains a singleton:** Use `providedIn: 'root'`. Remove the service from any module/component `providers` array. Lazy-loaded modules with their own providers will create a new instance — move the service to root to avoid this.

**Q134. Managing circular dependencies:** Extract shared logic into a separate utility service. Use `Injector` to lazily resolve dependencies:

```typescript
someMethod() {
  const serviceB = this.injector.get(ServiceB);
}
```

**Q135. Real-time updates via WebSockets:**

```typescript
@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private socket$!: WebSocketSubject<any>;

  connect(url: string): void {
    if (!this.socket$ || this.socket$.closed) {
      this.socket$ = webSocket(url);
    }
  }

  onMessage(): Observable<any> { return this.socket$.asObservable(); }
  sendMessage(msg: any): void { this.socket$.next(msg); }
  close(): void { this.socket$.complete(); }
}
```

**Q136. Service data not updating components:** Verify data is exposed as an Observable and subscribed to. Check the service is a singleton. In `OnPush` components, ensure change detection is triggered or use the `async` pipe.

**Q137. File uploads with progress tracking:** Use `HttpClient` with `reportProgress: true` and `observe: 'events'`. Handle `HttpEventType.UploadProgress` to compute percentage and `HttpEventType.Response` for completion.

**Q138. Sharing data across unrelated components:** Create a singleton service with `BehaviorSubject`. Both components subscribe to the observable to receive updates and call `next()` to push changes.

**Q139. Refactoring a monolithic service:** Identify logical boundaries (API, state, utilities) and extract them into feature-specific services. Inject them where needed.

**Q140. DI causing performance issues:** Ensure services are not recreated unnecessarily due to incorrect provider scope. Move services to `providedIn: 'root'` and lazy-load heavy services only where needed.

---

## 6. Routing & Navigation

### Q141. What is routing in Angular, and how is it implemented?

Angular routing enables navigation between views based on the URL, enabling SPA behaviour.

```typescript
const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
```

Add `<router-outlet></router-outlet>` in the template as a placeholder for routed components.

---

### Q142. How do you configure routes in an Angular application?

Define an array of route objects with `path` and `component` properties in `app-routing.module.ts`, then pass it to `RouterModule.forRoot(routes)`.

---

### Q143. What is `RouterModule` in Angular?

`RouterModule` is a built-in Angular module that provides router directives and services. It enables navigation between components and manages route configuration, guards, parameters, and lazy loading.

---

### Q144. What is the difference between `RouterLink` and `Router.navigate`?

| | `RouterLink` | `Router.navigate` |
|---|---|---|
| Usage | In templates (declarative) | In TypeScript (programmatic) |
| Example | `<a [routerLink]="['/home']">` | `this.router.navigate(['/home'])` |

---

### Q145. What is a route guard, and what are its types?

Route guards control access to routes.

| Guard | Purpose |
|---|---|
| `CanActivate` | Checks access before activating a route |
| `CanDeactivate` | Checks if the user can leave a route |
| `Resolve` | Pre-fetches data before activating a route |
| `CanLoad` | Prevents loading of lazy-loaded modules |
| `CanActivateChild` | Checks access to child routes |

---

### Q146. How do you implement a `CanActivate` guard?

```typescript
@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService) {}

  canActivate(): boolean {
    return this.auth.isLoggedIn();
  }
}

// Route config
{ path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }
```

---

### Q147. What is the purpose of the `CanDeactivate` guard?

`CanDeactivate` prevents users from accidentally leaving a route (e.g., a form with unsaved changes).

```typescript
@Injectable()
export class CanDeactivateGuard implements CanDeactivate<CanComponentDeactivate> {
  canDeactivate(component: CanComponentDeactivate): boolean {
    return component.canDeactivate();
  }
}
```

---

### Q148. How do you handle route parameters in Angular?

```typescript
constructor(private route: ActivatedRoute) {}

ngOnInit() {
  const id = this.route.snapshot.paramMap.get('id');
  // or subscribe for reactive updates:
  this.route.paramMap.subscribe(params => { ... });
}
```

---

### Q149. What is the difference between query parameters and route parameters?

| | Route Parameters | Query Parameters |
|---|---|---|
| Definition | Part of the route path | Appended after `?` |
| Required | Yes | Optional |
| Example URL | `/user/42` | `/search?query=angular` |
| Access | `paramMap.get('id')` | `queryParamMap.get('query')` |

---

*End of document — 149 questions covering Angular Fundamentals, Components, Directives, Pipes, Data Binding, Forms, Services, and Routing.*
