# Angular Fundamentals & Architecture Reference Guide

A comprehensive, production-grade reference guide covering core Angular architecture, modern control flow, reactive forms, dependency injection, change detection, NgRx state management, PWA, and RxJS operators.

## Table of Contents

- [Custom Directives (Attribute & Structural)](#1-custom-directives)
- [Custom Pipes (Pure vs Impure)](#2-custom-pipes)
- [Route Guards & Navigation Control](#3-route-guards)
- [Route Resolvers](#4-route-resolvers)
- [Component Lifecycle & Modern Cleanup](#5-component-lifecycle--modern-cleanup)
- [Dependency Injection (DI) Architecture & How DI Works](#6-dependency-injection-di-architecture--how-di-works)
- [Change Detection & Angular Performance Optimization](#7-change-detection--performance-optimization)
- [Content Projection & Templates (ng-content, ng-template, ng-container, ngTemplateOutlet)](#8-content-projection--templates)
- [Modern Control Flow (@if, @for, @switch, @defer)](#9-modern-control-flow-angular-17)
- [Modern Angular Features (v14 – v18+) & Angular Signals](#10-modern-angular-features-v14--v18--angular-signals)
- [Angular Forms (Template-Driven, Reactive, FormArray & Validators)](#11-angular-forms)
- [Observables, Subjects & State Streams](#12-observables-subjects--state-streams)
- [RxJS Operators Guide](#13-rxjs-operators-guide)
- [NgRx & State Management](#14-ngrx--state-management)
- [Progressive Web Apps (PWA) in Angular](#15-progressive-web-apps-pwa-in-angular)

---

## 1. Custom Directives

Directives attach custom behavior or manipulate the DOM of elements in your Angular applications.

### Types of Directives

| Type | Purpose | Example |
|---|---|---|
| **Component** | Directive with an inline or external template | `<app-user-card>` |
| **Structural** | Adds, removes, or manipulates DOM elements | `*ngIf`, `*ngFor`, custom `*appIf` |
| **Attribute** | Changes the appearance or behavior of an existing element | `[ngClass]`, `[appHighlight]` |

---

### Attribute Directive (Class-based vs Modern Standalone)

#### Standalone Attribute Directive (Angular 15+ Recommended)

```typescript
import { Directive, ElementRef, HostListener, Input, inject } from '@angular/core';

@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {
  private el = inject(ElementRef);

  @Input('appHighlight') highlightColor: string = 'yellow';
  @Input() defaultColor: string = 'transparent';

  @HostListener('mouseenter') onMouseEnter() {
    this.setColor(this.highlightColor || 'yellow');
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.setColor(this.defaultColor);
  }

  private setColor(color: string) {
    this.el.nativeElement.style.backgroundColor = color;
  }
}
```

**Usage in HTML:**
```html
<p [appHighlight]="'lightblue'" [defaultColor]="'#f0f0f0'">Hover over this text!</p>
```

---

### Structural Directive (Custom `*appIf`)

Structural directives interact with `TemplateRef` and `ViewContainerRef` to conditionally render content.

```typescript
import { Directive, Input, TemplateRef, ViewContainerRef, inject } from '@angular/core';

@Directive({
  selector: '[appIf]',
  standalone: true
})
export class AppIfDirective {
  private templateRef = inject(TemplateRef);
  private vcr = inject(ViewContainerRef);
  private hasView = false;

  @Input() set appIf(condition: boolean) {
    if (condition && !this.hasView) {
      this.vcr.createEmbeddedView(this.templateRef);
      this.hasView = true;
    } else if (!condition && this.hasView) {
      this.vcr.clear();
      this.hasView = false;
    }
  }
}
```

**Usage:**
```html
<div *appIf="isLoggedIn">Welcome back, User!</div>
```

---

### Common HostListener Patterns

```typescript
// Restrict input to digits only
@HostListener('input', ['$event'])
onInput(event: Event) {
  const input = event.target as HTMLInputElement;
  input.value = input.value.replace(/[^0-9]/g, '');
}

// Click outside detection for dropdowns/modals
@HostListener('document:click', ['$event.target'])
onClickOutside(target: HTMLElement) {
  if (!this.el.nativeElement.contains(target)) {
    this.closeMenu.emit();
  }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 2. Custom Pipes

Pipes transform display values directly inside your templates without mutating the source data.

### Pure vs Impure Pipes

| Feature | Pure Pipe (`pure: true`, Default) | Impure Pipe (`pure: false`) |
|---|---|---|
| **Execution Trigger** | Runs **only** when primitive input value changes or object/array reference changes | Runs on **every** Change Detection cycle (keyboard, mouse move, timer) |
| **Performance** | High performance & memoized | Potentially costly; must be lightweight |
| **Use Case** | Formatting dates, currency, text transforms, filtering immutable arrays | Filtering mutable arrays (e.g., array mutations via `.push()`) |

---

### Custom Pipe Example (Truncate with Options)

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate',
  standalone: true
})
export class TruncatePipe implements PipeTransform {
  transform(value: string | null | undefined, limit: number = 30, trail: string = '...'): string {
    if (!value) return '';
    return value.length > limit ? value.substring(0, limit) + trail : value;
  }
}
```

**Usage:**
```html
<p>{{ product.description | truncate:50:'… Read More' }}</p>
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 3. Route Guards

Route guards control navigation flow: authenticating access, prompting users about unsaved form data, or checking feature flags.

### Guard Types Summary

| Guard | Interface / Type | Purpose |
|---|---|---|
| **CanActivate** | `CanActivateFn` | Blocks navigation to a route (e.g. Auth check) |
| **CanActivateChild** | `CanActivateChildFn` | Protects child sub-routes |
| **CanDeactivate** | `CanDeactivateFn<T>` | Prompts user before leaving with unsaved state |
| **CanMatch** | `CanMatchFn` | Replaces `CanLoad`; controls if a route pattern matches (feature toggles) |

---

### Modern Functional Guards (Angular 15+)

#### 1. `canActivate` (Authentication Guard)

```typescript
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated()) {
    return true;
  }

  // Redirect to login with returnUrl
  return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
};
```

#### 2. `canDeactivate` (Unsaved Changes Guard)

```typescript
import { CanDeactivateFn } from '@angular/router';

export interface HasUnsavedChanges {
  hasUnsavedChanges(): boolean;
}

export const unsavedChangesGuard: CanDeactivateFn<HasUnsavedChanges> = (component) => {
  if (component.hasUnsavedChanges && component.hasUnsavedChanges()) {
    return confirm('You have unsaved changes. Are you sure you want to discard them?');
  }
  return true;
};
```

#### 3. Route Configuration

```typescript
export const routes: Routes = [
  {
    path: 'admin',
    loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent),
    canActivate: [authGuard]
  },
  {
    path: 'editor',
    loadComponent: () => import('./editor/editor.component').then(m => m.EditorComponent),
    canDeactivate: [unsavedChangesGuard]
  }
];
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 4. Route Resolvers

Resolvers fetch required API data **before** the destination route activates, eliminating blank screens and in-component loading spinners.

```typescript
import { inject } from '@angular/core';
import { ResolveFn, ActivatedRouteSnapshot } from '@angular/router';
import { UserService, UserProfile } from '../services/user.service';

export const userProfileResolver: ResolveFn<UserProfile> = (route: ActivatedRouteSnapshot) => {
  const userId = route.paramMap.get('id')!;
  return inject(UserService).getUserById(userId);
};
```

**Route Registration & Component Access:**

```typescript
// Route:
{
  path: 'user/:id',
  component: UserProfileComponent,
  resolve: { profile: userProfileResolver }
}

// In UserProfileComponent:
export class UserProfileComponent implements OnInit {
  private route = inject(ActivatedRoute);
  userProfile!: UserProfile;

  ngOnInit() {
    this.userProfile = this.route.snapshot.data['profile'];
    
    // Or reactively to respond to parameter changes:
    this.route.data.subscribe(data => {
      this.userProfile = data['profile'];
    });
  }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 5. Component Lifecycle & Modern Cleanup

Angular manages the lifecycle of components from creation, change detection, view rendering, through destruction.

```
       ┌────────────────────────┐
       │      constructor       │ (Dependency injection only; no DOM / API calls)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │      ngOnChanges       │ (Called whenever @Input() bindings change)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │        ngOnInit        │ (Component initialized; prime location for HTTP requests)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │       ngDoCheck        │ (Custom change detection check)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │   ngAfterContentInit   │ (Projected content <ng-content> initialized)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │   ngAfterViewInit      │ (Component template and child views initialized)
       └───────────┬────────────┘
                   ▼
       ┌────────────────────────┐
       │      ngOnDestroy       │ (Cleanup: unsubscribe observables, stop timers)
       └────────────────────────┘
```

### Modern Subscription Cleanup (`DestroyRef` & `takeUntilDestroyed`)

Instead of writing boilerplate `ngOnDestroy` with `Subject` and `takeUntil`, Angular 16+ provides `takeUntilDestroyed`:

```typescript
import { Component, OnInit, inject, DestroyRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { interval } from 'rxjs';

@Component({
  selector: 'app-timer',
  standalone: true,
  template: `<p>Seconds: {{ seconds }}</p>`
})
export class TimerComponent implements OnInit {
  seconds = 0;
  private destroyRef = inject(DestroyRef); // Injectable anywhere!

  ngOnInit() {
    // Automatically unsubscribes when this component is destroyed:
    interval(1000)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(val => (this.seconds = val));

    // Register custom teardown logic
    this.destroyRef.onDestroy(() => {
      console.log('TimerComponent cleaned up successfully!');
    });
  }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 6. Dependency Injection (DI) Architecture & How DI Works

Dependency Injection (DI) is a core design pattern in Angular where a class requests dependencies from external sources rather than creating them directly with `new`.

### 1. How DI Works Under the Hood

Angular's DI system consists of 4 foundational building blocks:

```
┌──────────────┐         1. Requests Token         ┌──────────────┐
│   Consumer   │ ────────────────────────────────► │   Injector   │
│ (Component / │                                   └──────┬───────┘
│   Service)   │ ◄────────────────────────────────────────┘
└──────────────┘         4. Returns Instance
                                  ▲
                                  │ 2. Finds Provider Recipe
                                  ▼
                         ┌─────────────────┐
                         │    Provider     │ ──► 3. Instantiates Dependency
                         │ (Recipe / Value)│
                         └─────────────────┘
```

1. **Consumer:** The component, directive, pipe, or service that requires a dependency.
2. **Token:** The unique lookup key (usually a TypeScript class type like `UserService` or an `InjectionToken<T>`).
3. **Provider:** A registration that tells the Injector *how* to instantiate and configure the dependency.
4. **Injector:** The container holding the token-to-instance registry that resolves and caches instances.

---

### 2. The Injector Hierarchy & Lookup Resolution Algorithm

Angular organizes injectors into a **two-tiered hierarchical tree**:
- **Environment/Module Injector Tree:** Root (`providedIn: 'root'`) -> Platform -> NullInjector.
- **Element/Component Injector Tree:** Built implicitly for each DOM element with directives/components (`providers: [...]` or `viewProviders: [...]`).

#### Lookup Algorithm (Bubble-Up Resolution):

When a component requests a dependency:
1. Angular starts searching at the **Element Injector** of the current component.
2. If not found, it traverses upward through the **parent Element Injectors**.
3. If not found at the root component element, it switches to the **Environment Injector** (Route/Module scope).
4. Next, it checks the **Root Injector** (`providedIn: 'root'`).
5. Then the **Platform Injector** (shared across applications, e.g. `DomSanitizer`).
6. If still not found, it reaches the **NullInjector**, which throws:  
   `NullInjectorError: No provider for Token!`.

```
[Child Component Element Injector]
  │ (Not found? Bubble up)
  ▼
[Parent Component Element Injector]
  │ (Not found? Bubble up)
  ▼
[Root Component Element Injector]
  │ (Switch to Environment Hierarchy)
  ▼
[Environment / Route Injector]
  │
  ▼
[Root Injector (providedIn: 'root')] ──► (Singleton across app)
  │
  ▼
[Platform Injector]
  │
  ▼
[NullInjector] ──► Throws NullInjectorError!
```

---

### 3. Provider Recipes (`provide: ...`)

You can customize how Angular constructs any token using provider configuration objects:

```typescript
export const APP_PROVIDERS = [
  // 1. useClass: Creates an instance of a specified class
  { provide: LoggerService, useClass: BetterLoggerService },

  // 2. useValue: Injects a static value, constant, or configuration object
  { provide: API_URL_TOKEN, useValue: 'https://api.myapp.com/v1' },

  // 3. useExisting: Creates an alias pointing to an existing singleton
  { provide: OldLogger, useExisting: LoggerService },

  // 4. useFactory: Dynamic creation with custom initialization logic and dependencies
  {
    provide: AuthService,
    useFactory: (http: HttpClient, config: AppConfig) => {
      return config.isMockAuth ? new MockAuthService() : new RealAuthService(http);
    },
    deps: [HttpClient, AppConfigToken]
  }
];
```

---

### 4. `InjectionToken<T>` for Non-Class Dependencies

TypeScript interfaces and primitives do not exist at JavaScript runtime, so they cannot be used as injection tokens. Use `InjectionToken<T>`:

```typescript
import { InjectionToken, inject } from '@angular/core';

export interface AppConfig {
  apiUrl: string;
  retryAttempts: number;
}

// 1. Create the token
export const APP_CONFIG = new InjectionToken<AppConfig>('app.config', {
  providedIn: 'root',
  factory: () => ({ apiUrl: 'https://api.example.com', retryAttempts: 3 })
});

// 2. Inject using modern inject() or @Inject()
@Component({...})
export class UserComponent {
  private config = inject(APP_CONFIG);
}
```

---

### 5. Resolution Modifiers (`@Optional`, `@Self`, `@SkipSelf`, `@Host`)

Modifiers control where the injector starts and stops its lookup:

```typescript
constructor(
  // @Optional: Does not throw error if missing; injects null instead
  @Optional() private logger?: LoggerService,

  // @Self: Only searches current element's injector; does NOT bubble up
  @Self() private validator?: CustomValidatorDirective,

  // @SkipSelf: Skips current element and starts searching from the parent element
  @SkipSelf() private parentService?: ParentService,

  // @Host: Stops searching when reaching the host component that rendered the template
  @Host() private formGroup?: FormGroupDirective
) {}
```

---

### 6. Constructor Injection vs Modern `inject()` Function

In Angular 14+, the `inject()` function replaces constructor boilerplate and works in functions, factory providers, and functional route guards:

```typescript
@Component({...})
export class ProductListComponent {
  // Modern inject(): Clean, typed, works inside functional guards and initialization logic
  private cartService = inject(CartService);
  private router = inject(Router);
  private config = inject(APP_CONFIG);
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 7. Change Detection & Angular Performance Optimization

Ensuring peak performance and 60 FPS in Angular applications requires understanding how change detection works and implementing modern optimization patterns.

### 1. Change Detection: Default vs OnPush

| Feature | `ChangeDetectionStrategy.Default` | `ChangeDetectionStrategy.OnPush` |
|---|---|---|
| **Tree Traversal** | Checks **all** components in the tree from root to leaf on every async event (click, timer, HTTP) | Skips component sub-tree unless explicitly marked dirty |
| **When it runs** | Any Zone.js event | 1. `@Input()` reference changes (`prev !== curr`)<br>2. Event originating from component/children<br>3. `async` pipe emits<br>4. Signal updates (v16+)<br>5. Manual `ChangeDetectorRef` call |
| **Performance** | Slower in large applications | Extremely fast, predictable & optimal |

#### OnPush in Action with Immutable Data:

```typescript
@Component({
  selector: 'app-user-card',
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<div>{{ user.name }} ({{ user.role }})</div>`
})
export class UserCardComponent {
  @Input({ required: true }) user!: { id: string; name: string; role: string };
}
```

> **Rule for OnPush:** Always pass **new object references** (immutable updates using spread `{ ...user, role: 'Admin' }`) to trigger OnPush input detection! Mutating a property directly (`user.role = 'Admin'`) will **not** trigger change detection.

---

### 2. `trackBy` in `*ngFor` vs Mandatory `track` in `@for`

#### The Problem:
Without tracking, whenever an array changes (items added, reordered, or refreshed from API), Angular destroys and recreates **all** DOM elements in the list! This causes UI flickering, loss of input focus, scroll jumps, and heavy rendering overhead.

#### Legacy `*ngFor` with `trackBy`:
```typescript
@Component({
  template: `
    <li *ngFor="let item of items; trackBy: trackById">
      {{ item.name }}
    </li>
  `
})
export class ListComponent {
  items = [{ id: 1, name: 'Laptop' }, { id: 2, name: 'Phone' }];

  trackById(index: number, item: { id: number; name: string }): number {
    return item.id;
  }
}
```

#### Modern Angular 17+ `@for` (Mandatory `track` Expression):
Angular 17 made tracking **mandatory**, eliminating `trackBy` boilerplate and performing up to **90% faster**:

```html
<ul>
  @for (item of items; track item.id; let idx = $index) {
    <li>#{{ idx + 1 }}: {{ item.name }}</li>
  } @empty {
    <li>No items available.</li>
  }
</ul>
```

---

### 3. Template Performance: Pure Pipes vs Calling Methods in Templates

- ❌ **Anti-Pattern (Method in template):**
  ```html
  <!-- BAD: calculateDiscount() runs on EVERY SINGLE Change Detection tick! -->
  <p>{{ calculateDiscount(product.price) }}</p>
  ```
- ✅ **Best Practice (Pure Pipe):** Pure pipes are memoized; they only re-execute when their input primitive or reference changes:
  ```html
  <!-- GOOD: Only runs when product.price changes -->
  <p>{{ product.price | discount }}</p>
  ```

---

### 4. Running Outside Angular (`NgZone.runOutsideAngular`)

For high-frequency events (mouse move, window scroll, canvas rendering, WebSockets, D3 animations), Zone.js triggers change detection on every event. Run them outside Angular:

```typescript
import { Component, ElementRef, OnInit, inject, NgZone } from '@angular/core';

@Component({
  selector: 'app-canvas-animation',
  standalone: true,
  template: `<canvas #canvas></canvas>`
})
export class CanvasAnimationComponent implements OnInit {
  private ngZone = inject(NgZone);
  private el = inject(ElementRef);

  ngOnInit() {
    // Run high-frequency loop completely outside Angular's zone:
    this.ngZone.runOutsideAngular(() => {
      this.el.nativeElement.addEventListener('mousemove', (e: MouseEvent) => {
        // Does NOT trigger Angular change detection!
        this.renderParticles(e.clientX, e.clientY);
      });
    });
  }

  private renderParticles(x: number, y: number) { /* Heavy canvas calculations */ }
}
```

---

### 5. Image Optimization with `NgOptimizedImage`

The `NgOptimizedImage` directive enforces web performance best practices (LCP optimization, automatic `srcset`, layout shift prevention):

```html
<!-- Automatically lazy-loaded, responsive, prevents Cumulative Layout Shift (CLS) -->
<img [ngSrc]="product.imageUrl" width="400" height="300" alt="Product" />

<!-- For Largest Contentful Paint (Hero/Banner): Set priority to preload immediately -->
<img [ngSrc]="heroBanner" width="1200" height="600" priority alt="Hero Banner" />
```

---

### 6. Comprehensive Performance Optimization Checklist

| Technique | How it Improves Performance |
|---|---|
| **`OnPush` Strategy** | Skips change detection for entire component sub-trees unless inputs change |
| **`track` in `@for`** | Diffing lists by unique ID prevents recreating unaffected DOM nodes |
| **Pure Pipes** | Memoizes calculation results; prevents function execution on every CD cycle |
| **`@defer` Blocks** | Lazy-loads JS chunks and template markup only when scrolled into view |
| **Route-level Lazy Loading** | `loadComponent: () => import(...)` splits code into small on-demand bundles |
| **`NgOptimizedImage`** | Preloads LCP images, lazy-loads off-screen images, enforces dimensions |
| **`NgZone.runOutsideAngular`** | Bypasses Zone.js change detection for rapid events (scroll, mousemove) |
| **`takeUntilDestroyed`** | Prevents memory leaks by automatically tearing down unneeded subscriptions |

[↑ Back to Table of Contents](#table-of-contents)

---

## 8. Content Projection & Templates

Techniques for building reusable, flexible components.

### 1. `ng-content` (Multi-Slot Content Projection)

Allows consumer components to pass HTML content into specific slots inside child components.

```html
<!-- Card Component Template (card.component.html) -->
<div class="card">
  <div class="card-header">
    <ng-content select="[card-title]"></ng-content>
  </div>
  <div class="card-body">
    <ng-content select="[card-body]"></ng-content>
  </div>
  <div class="card-footer">
    <ng-content select="[card-footer]"></ng-content>
  </div>
</div>
```

**Consumer Usage:**
```html
<app-card>
  <h2 card-title>User Profile</h2>
  <p card-body>Arjun Khade - Full Stack Developer</p>
  <button card-footer>Save</button>
</app-card>
```

---

### 2. `ng-container`

An invisible grouping tag. Applies structural directives without adding unnecessary wrapper elements (like extra `<div>`s) to the DOM.

```html
<ng-container *ngIf="user">
  <p>Name: {{ user.name }}</p>
  <p>Role: {{ user.role }}</p>
</ng-container>
```

---

### 3. `ng-template` & `ngTemplateOutlet`

`ng-template` holds markup that is never rendered by default. `ngTemplateOutlet` instantiates and dynamically injects templates with custom contexts.

```html
<!-- Define the template with context variables -->
<ng-template #customItem let-item="item" let-i="index">
  <li class="item">{{ i + 1 }}. {{ item.name }} ({{ item.price | currency }})</li>
</ng-template>

<!-- Render template dynamically using ngTemplateOutlet -->
<ul>
  @for (prod of products; track prod.id; let idx = $index) {
    <ng-container 
      *ngTemplateOutlet="customItem; context: { item: prod, index: idx }">
    </ng-container>
  }
</ul>
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 9. Modern Control Flow (Angular 17+)

Angular 17 introduced declarative built-in control flow syntax replacing legacy `*ngIf`, `*ngFor`, and `*ngSwitch`.

### 1. `@if` / `@else if` / `@else`

```html
@if (user.role === 'admin') {
  <app-admin-panel />
} @else if (user.role === 'editor') {
  <app-editor-panel />
} @else {
  <p>Standard Viewer Access</p>
}
```

---

### 2. `@for` with required `track` and `@empty`

The `track` expression is mandatory and provides performant list diffing without requiring `trackBy` boilerplate.

```html
<ul>
  @for (user of users; track user.id; let idx = $index, count = $count) {
    <li>#{{ idx + 1 }}/{{ count }}: {{ user.name }}</li>
  } @empty {
    <li class="no-records">No users found.</li>
  }
</ul>
```

---

### 3. `@switch` / `@case` / `@default`

```html
@switch (notification.type) {
  @case ('SUCCESS') { <span class="badge green">Success!</span> }
  @case ('WARNING') { <span class="badge yellow">Warning</span> }
  @case ('ERROR')   { <span class="badge red">Critical Error</span> }
  @default          { <span class="badge gray">Info</span> }
}
```

---

### 4. `@defer` (Deferred / Lazy Loading Blocks)

Enables lazy-loading parts of the template (and their JS chunks) on demand based on runtime triggers.

```html
@defer (on viewport; prefetch on idle) {
  <!-- This component chunk only downloads when scrolled into view! -->
  <app-heavy-analytics-chart [data]="chartData" />
} @placeholder (minimum 300ms) {
  <div class="skeleton-placeholder">Scroll down to view chart...</div>
} @loading (minimum 500ms) {
  <div class="spinner">Loading chart package...</div>
} @error {
  <p class="error">Failed to load chart component.</p>
}
```

**Common Triggers:** `on viewport`, `on idle`, `on hover(triggerBtn)`, `on interaction(btn)`, `when isLoaded`.

[↑ Back to Table of Contents](#table-of-contents)

---

## 10. Modern Angular Features (v14 – v18+) & Angular Signals

Angular has undergone a complete renaissance from Angular 14 through Angular 18+, transitioning to a lightweight, standalone-first, and reactivity-driven framework.

### 1. Complete Catalog of Modern Angular Features

| Feature | Introduced | What it Replaced / Solved |
|---|---|---|
| **Standalone Components** | Angular 14/15 | Eliminated `NgModule` boilerplate; components directly import dependencies |
| **`inject()` Function** | Angular 14 | Replaces constructor injection; usable in functions, factory providers, and guards |
| **Functional Guards & Resolvers** | Angular 15 | Replaced class-based `CanActivate` and `Resolve` interfaces with lightweight functions |
| **Directive Composition API** | Angular 15 | Allows components to inherit host behaviors from other directives without class inheritance |
| **`DestroyRef` & `takeUntilDestroyed`** | Angular 16 | Replaces repetitive `ngOnDestroy` + `Subject` unsubscribe boilerplate |
| **Angular Signals** | Angular 16+ | Introduced granular, fine-grained reactivity with zero Zone.js overhead |
| **Non-Destructive Hydration** | Angular 16/17 | Reuses server-rendered DOM instead of destroying and re-rendering on client load |
| **Built-in Control Flow (`@if`, `@for`, `@switch`)** | Angular 17 | Replaces `*ngIf`, `*ngFor`, `*ngSwitch` with performant compiler syntax |
| **Deferrable Views (`@defer`)** | Angular 17 | Zero-boilerplate lazy loading of template sections based on triggers |
| **Vite / esbuild Application Builder** | Angular 17 | Up to 80% faster build times using esbuild/Vite instead of Webpack |
| **Signal Inputs, Outputs & Models** | Angular 17.1+ / 18 | `input()`, `output()`, and `model()` replacing `@Input()`, `@Output()`, two-way binding |
| **Signal Queries (`viewChild`, `contentChild`)** | Angular 17.2+ / 18 | Signals-based DOM/view queries replacing `@ViewChild`, `@ContentChild` |
| **Experimental Zoneless Support** | Angular 18 | `provideExperimentalZonelessChangeDetection()` removes `Zone.js` completely! |

---

### 2. Angular Signals: Fine-Grained Reactivity

A **Signal** is a reactive wrapper around a value that notifies consumers whenever that value changes. Unlike RxJS (which is push-based over time) or Zone.js (which dirties entire trees), Signals enable **fine-grained reactivity**: Angular knows *precisely* where in the template a value changed and updates only that specific DOM node.

#### Reading a Signal:
A Signal is accessed by calling it as a function: `count()`.

---

### 3. Types of Signals

#### 1. Writable Signals (`signal()`)
A signal whose value can be read and updated directly:

```typescript
import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-counter',
  standalone: true,
  template: `
    <p>Count: {{ count() }}</p>
    <button (click)="increment()">+1</button>
    <button (click)="reset()">Reset</button>
  `
})
export class CounterComponent {
  // 1. Declare writable signal with initial value
  count = signal<number>(0);

  increment() {
    // .update(): derives next value from previous value
    this.count.update(prev => prev + 1);
  }

  reset() {
    // .set(): explicitly sets a new value
    this.count.set(0);
  }

  // Expose as readonly to external consumers:
  readonlyCount = this.count.asReadonly();
}
```

#### 2. Computed Signals (`computed()`)
A read-only signal derived from other signals.
- **Memoized:** Recalculates only when one of its source signals changes.
- **Lazy:** Calculation is deferred until the computed value is actually read.

```typescript
import { Component, signal, computed } from '@angular/core';

@Component({
  selector: 'app-cart-summary',
  standalone: true,
  template: `
    <p>Subtotal: {{ subtotal() | currency }}</p>
    <p>Tax (10%): {{ tax() | currency }}</p>
    <h3>Total: {{ total() | currency }}</h3>
  `
})
export class CartSummaryComponent {
  price = signal(100);
  quantity = signal(2);

  // Computed signals:
  subtotal = computed(() => this.price() * this.quantity());
  tax = computed(() => this.subtotal() * 0.10);
  total = computed(() => this.subtotal() + this.tax());
}
```

#### 3. Effects (`effect()`)
An operation that runs automatically whenever one or more tracked signals change. Used for side-effects:
- Logging / Analytics
- Syncing state to `localStorage`
- Custom Canvas or DOM mutations

```typescript
import { Component, signal, effect } from '@angular/core';

@Component({ ... })
export class SettingsComponent {
  theme = signal<'light' | 'dark'>('light');

  constructor() {
    // Effect automatically re-runs whenever theme() changes:
    effect(() => {
      console.log(`Theme updated to: ${this.theme()}`);
      localStorage.setItem('user-theme', this.theme());
      document.body.className = this.theme();
    });
  }
}
```

> **Rule:** Do **not** set other signals inside an `effect()` unless explicitly necessary (requires `{ allowSignalWrites: true }`). Use `computed()` for derived state instead.

---

### 4. Modern Signal-Based Component APIs (Angular 17+ / 18)

Replace legacy `@Input()`, `@Output()`, and `@ViewChild()` decorators with functional Signal equivalents:

```typescript
import { Component, input, output, model, viewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  template: `
    <input #searchBox placeholder="Search..." />
    <p>User: {{ username() }}</p>
    <button (click)="toggleActive()">Toggle Active</button>
  `
})
export class UserProfileComponent {
  // 1. Signal Inputs (Replaces @Input)
  username = input.required<string>(); // Mandatory input
  role = input<string>('Guest');       // Optional input with default

  // 2. Signal Model (Two-way binding [(active)]="isActive")
  active = model<boolean>(false);

  // 3. Signal Output (Replaces @Output EventEmitter)
  roleChanged = output<string>();

  // 4. Signal Queries (Replaces @ViewChild)
  searchBox = viewChild<ElementRef<HTMLInputElement>>('searchBox');

  toggleActive() {
    this.active.update(val => !val); // Automatically updates parent binding!
    this.roleChanged.emit(this.role());
  }

  focusSearch() {
    this.searchBox()?.nativeElement.focus();
  }
}
```

---

### 5. RxJS Interoperability (`toSignal` & `toObservable`)

Angular seamlessly bridges RxJS Observables and Signals using `@angular/core/rxjs-interop`:

```typescript
import { Component, inject, signal } from '@angular/core';
import { toSignal, toObservable } from '@angular/core/rxjs-interop';
import { HttpClient } from '@angular/common/http';
import { switchMap } from 'rxjs/operators';

export interface Product { id: number; name: string; }

@Component({
  selector: 'app-products',
  standalone: true,
  template: `
    <ul>
      @for (prod of products(); track prod.id) {
        <li>{{ prod.name }}</li>
      }
    </ul>
  `
})
export class ProductComponent {
  private http = inject(HttpClient);

  // 1. toSignal: Converts an Observable into a Signal (auto-subscribes and auto-unsubscribes!)
  products = toSignal(
    this.http.get<Product[]>('/api/products'),
    { initialValue: [] }
  );

  // 2. toObservable: Converts a Signal into an Observable to use RxJS operators:
  searchQuery = signal('laptop');
  searchQuery$ = toObservable(this.searchQuery);

  searchResults$ = this.searchQuery$.pipe(
    switchMap(query => this.http.get<Product[]>(`/api/search?q=${query}`))
  );
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 11. Angular Forms

Angular provides two distinct paradigms for capturing user input: **Template-Driven** and **Reactive Forms**.

### Template-Driven vs Reactive Forms

| Feature | Template-Driven Forms | Reactive Forms (Preferred) |
|---|---|---|
| **Setup** | `FormsModule` | `ReactiveFormsModule` |
| **Source of Truth** | HTML Template (`[(ngModel)]`, `ngForm`) | Component TypeScript model (`FormGroup`, `FormControl`) |
| **Immutability** | Mutable state | Immutable structured data streams |
| **Validation** | HTML directives (`required`, `minlength`) | Pure TypeScript functions (`Validators.required`) |
| **Scalability** | Small, simple forms | Complex, dynamic, enterprise-scale forms |

---

### Reactive Forms with `FormArray` & Nested Groups

```typescript
import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-form.component.html'
})
export class UserFormComponent {
  private fb = inject(FormBuilder);

  userForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    // FormArray for dynamic list of phone numbers:
    phoneNumbers: this.fb.array([
      this.fb.control('', [Validators.required, Validators.pattern(/^[0-9]{10}$/)])
    ])
  });

  get phoneNumbers(): FormArray {
    return this.userForm.get('phoneNumbers') as FormArray;
  }

  addPhone() {
    this.phoneNumbers.push(this.fb.control('', [Validators.required]));
  }

  removePhone(index: number) {
    this.phoneNumbers.removeAt(index);
  }

  onSubmit() {
    if (this.userForm.valid) {
      console.log('Submitted Data:', this.userForm.value);
    }
  }
}
```

---

### Custom Validators: Synchronous vs Asynchronous

#### 1. Custom Synchronous Validator (No Forbidden Names)

```typescript
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function forbiddenNameValidator(forbiddenName: RegExp): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const isForbidden = forbiddenName.test(control.value);
    return isForbidden ? { forbiddenName: { value: control.value } } : null;
  };
}
```

#### 2. Custom Asynchronous Validator (Check Username Availability via API)

Async validators return `Observable<ValidationErrors | null>` or `Promise<ValidationErrors | null>`.

```typescript
import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Observable, of, timer } from 'rxjs';
import { map, switchMap, catchError } from 'rxjs/operators';
import { UserService } from '../services/user.service';

export function usernameAvailableValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) return of(null);

    // Debounce user keystrokes by 400ms before calling backend API:
    return timer(400).pipe(
      switchMap(() => userService.checkUsernameExists(control.value)),
      map(isTaken => (isTaken ? { usernameTaken: true } : null)),
      catchError(() => of(null))
    );
  };
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 12. Observables, Subjects & State Streams

RxJS powers reactive programming in Angular. Understanding the difference between stream types is critical.

### Observable vs Subject vs BehaviorSubject

| Feature | Observable | Subject | BehaviorSubject | ReplaySubject |
|---|---|---|---|---|
| **Stream Type** | Cold (executes per subscriber) | Hot (multicast) | Hot (multicast) | Hot (multicast) |
| **Initial Value** | None | No default value | **Requires** default value | None |
| **Late Subscriber Value** | Runs producer from start | Misses past emissions | Receives **latest** emitted value immediately | Receives *n* past buffered values |
| **Read Value Directly?** | No | No | Yes via `.getValue()` | No |
| **Primary Use Case** | HTTP calls, timer events | Event bus, notifying actions | Store/State management, auth status | Caching history of items |

---

### Real-World Service Store using `BehaviorSubject`

```typescript
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface User {
  id: string;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class UserStateService {
  // 1. Private BehaviorSubject with default initial value
  private userSubject = new BehaviorSubject<User | null>(null);

  // 2. Public readonly Observable stream exposed to components
  user$: Observable<User | null> = this.userSubject.asObservable();

  // 3. Derived state using RxJS map
  isLoggedIn$: Observable<boolean> = this.user$.pipe(map(user => !!user));

  // 4. State updates
  setUser(user: User) {
    this.userSubject.next(user);
  }

  logout() {
    this.userSubject.next(null);
  }

  // 5. Snapshot read when needed synchronously
  getCurrentUser(): User | null {
    return this.userSubject.getValue();
  }
}
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 13. RxJS Operators Guide

RxJS operators allow you to compose, transform, filter, coordinate, and clean up asynchronous data streams.

---

### 1. High-Order Flattening Operators

Flattening operators map an outer observable emission to an inner observable (e.g. mapping an input event stream to an HTTP request).

#### 1. `switchMap`
- **Definition:** Cancels the previous inner request when a new emission arrives and only keeps the latest one.
- **Angular Use Case:** Search input autocomplete, live filtering, and route parameter changes (`paramMap.pipe(switchMap(...))`).

```typescript
this.searchControl.valueChanges.pipe(
  debounceTime(300),
  distinctUntilChanged(),
  switchMap(searchTerm => this.productService.search(searchTerm))
).subscribe(results => (this.products = results));
```

#### 2. `mergeMap`
- **Definition:** Runs multiple requests concurrently in parallel without cancellation. Preserves all emissions.
- **Angular Use Case:** Bulk file uploads, concurrent non-blocking API writes.

```typescript
from(fileList).pipe(
  mergeMap(file => this.uploadService.upload(file), 4) // max 4 concurrent requests
).subscribe(progress => console.log('File upload completed:', progress));
```

> **🔥 Critical Gotcha: What happens if an API call fails in `mergeMap`?**
> By default, an unhandled error inside an inner Observable **bubbles up and terminates the entire outer stream immediately**. Any remaining parallel requests or future emissions are killed.
>
> **The Fix (Inner `catchError`):** Always place `catchError` **inside the inner Observable** pipeline:
> ```typescript
> from(fileList).pipe(
>   mergeMap(file =>
>     this.uploadService.upload(file).pipe(
>       retry({ count: 2, delay: 1000 }),
>       catchError(error => {
>         console.error(`Upload failed for ${file.name}:`, error);
>         return of({ file: file.name, status: 'FAILED', error: error.message });
>       })
>     )
>   )
> ).subscribe(result => console.log('Processed:', result));
> ```

#### 3. `concatMap`
- **Definition:** Executes requests one after another in strict sequential order. Waits for the previous inner request to complete before beginning the next.
- **Angular Use Case:** Multi-step form submissions, sequential order processing, queueing financial transactions.

```typescript
from(stepPayloads).pipe(
  concatMap(step => this.wizardService.saveStep(step))
).subscribe(response => console.log('Step saved:', response));
```

#### 4. `exhaustMap`
- **Definition:** Ignores new incoming requests while the current inner request is still executing.
- **Angular Use Case:** Login button clicks, payment checkout submissions (prevents duplicate requests caused by accidental double-clicks).

```typescript
fromEvent(this.loginButton.nativeElement, 'click').pipe(
  exhaustMap(() => this.authService.login(this.loginForm.value))
).subscribe(user => this.router.navigate(['/dashboard']));
```

---

### 2. Combination Operators

#### 5. `forkJoin`
- **Definition:** Combines results from multiple observables, waits for all to complete, and emits the final value of each as an array or dictionary.
- **Angular Use Case:** Admin dashboard loading user profile, role permissions, and configuration simultaneously on initial load.

```typescript
forkJoin({
  profile: this.userService.getProfile(),
  permissions: this.authService.getPermissions(),
  settings: this.configService.getAppSettings()
}).subscribe(({ profile, permissions, settings }) => {
  console.log('All 3 APIs completed successfully:', profile, permissions, settings);
});
```

#### 6. `combineLatest`
- **Definition:** Combines the latest values from multiple observables and emits whenever *any* source emits (after all sources have emitted at least once).
- **Angular Use Case:** Dynamic multi-filter bar (search query + category dropdown + sort order).

```typescript
combineLatest([
  this.searchQuery$,
  this.selectedCategory$,
  this.sortBy$
]).subscribe(([query, category, sort]) => {
  this.applyFilters(query, category, sort);
});
```

---

### 3. Creation Operators

#### 7. `of`
- **Definition:** Creates an Observable from given values and emits them immediately, then completes.
- **Angular Use Case:** Returning mock/fallback data, interceptor bypasses, unit tests.

```typescript
of([1, 2, 3]).subscribe(console.log); // Emits the array immediately and completes
of('admin', 'editor', 'viewer').subscribe(console.log); // Emits strings one by one
```

#### 8. `from`
- **Definition:** Converts arrays, promises, or iterables into an Observable and emits items one by one.
- **Angular Use Case:** Converting native Promises (`fetch()`, Geolocation) or arrays into reactive streams.

```typescript
// Converts array to stream of individual numbers:
from([10, 20, 30]).subscribe(val => console.log(val)); // 10, then 20, then 30

// Converts a Promise to an Observable:
from(fetch('/api/status')).subscribe(res => console.log('Promise resolved to observable'));
```

#### 9. `interval`
- **Definition:** Emits values continuously (0, 1, 2...) at a specified time interval (in milliseconds).
- **Angular Use Case:** Polling an API every 30 seconds, auto-refresh tokens, countdown clocks.

```typescript
// Polls backend every 30 seconds:
interval(30000).pipe(
  switchMap(() => this.notificationService.checkNewNotifications())
).subscribe(notifications => (this.notifications = notifications));
```

#### 10. `timer`
- **Definition:** Emits a value after an initial delay, optionally continuing at fixed intervals.
- **Angular Use Case:** Delayed execution (e.g. showing a banner after 3 seconds, delayed retry, periodic polling with initial delay).

```typescript
// Emits after 3 seconds, then completes:
timer(3000).subscribe(() => (this.showWelcomeBanner = true));

// Waits 1 second, then emits every 5 seconds:
timer(1000, 5000).subscribe(val => console.log('Timer tick:', val));
```

---

### 4. Transformation & Filtering Operators

#### 11. `map`
- **Definition:** Used to transform emitted values into another format.
- **Angular Use Case:** Extracting specific properties from API responses or formatting presentation values.

```typescript
this.userService.getUser(id).pipe(
  map(user => `${user.firstName} ${user.lastName}`) // Transforms User object to full name string
).subscribe(fullName => console.log(fullName));
```

#### 12. `filter`
- **Definition:** Allows only values that satisfy a boolean condition to pass through the stream.
- **Angular Use Case:** Filtering out null/undefined values, ignoring specific route change events.

```typescript
this.router.events.pipe(
  filter(event => event instanceof NavigationEnd)
).subscribe(event => this.trackPageView(event.url));
```

#### 13. `debounceTime`
- **Definition:** Waits for a pause in emissions before emitting the latest value.
- **Angular Use Case:** Search input typing to avoid hitting the backend API on every keystroke.

```typescript
this.searchField.valueChanges.pipe(
  debounceTime(400) // Waits 400ms after user stops typing
).subscribe(value => this.searchApi(value));
```

#### 14. `take`
- **Definition:** Emits only the first *n* values from an Observable and then automatically completes.
- **Angular Use Case:** Listening to an observable only once (e.g., initial auth status check, first button click) without manual unsubscribe.

```typescript
this.authService.currentUser$.pipe(
  take(1) // Takes only the first emission and automatically unsubscribes
).subscribe(user => console.log('Initial user check:', user));
```

---

### 5. Utility, Multicasting & Cleanup Operators

#### 15. `tap`
- **Definition:** Used for side effects such as logging, analytics tracking, showing/hiding loaders, or debugging. It does not modify the emitted value.
- **Angular Use Case:** Managing loading spinners, logging debug data.

```typescript
this.orderService.placeOrder(order).pipe(
  tap(() => (this.isLoading = true)),
  tap({
    next: res => console.log('Order created:', res),
    error: err => console.error('Order failed:', err),
    finalize: () => (this.isLoading = false)
  })
).subscribe();
```

#### 16. `takeUntil`
- **Definition:** Automatically unsubscribes from an Observable when another notifier Observable emits a value.
- **Angular Use Case:** Cleaning up subscriptions in `ngOnDestroy` to prevent memory leaks in Angular components.

```typescript
export class ProfileComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.userService.getUpdates().pipe(
      takeUntil(this.destroy$) // Auto-unsubscribes when destroy$ emits!
    ).subscribe(data => (this.profileData = data));
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

> **Modern Alternative (Angular 16+):** Use `takeUntilDestroyed()` to eliminate `destroy$` boilerplate entirely.

#### 17. `shareReplay`
- **Definition:** Shares a single Observable execution among multiple subscribers and caches the latest emitted value.
- **Angular Use Case:** Caching HTTP GET requests so multiple components or multiple template `async` pipes don't trigger duplicate network calls.

```typescript
@Injectable({ providedIn: 'root' })
export class ConfigService {
  private http = inject(HttpClient);

  // Executes only 1 HTTP request, even if 10 components subscribe:
  readonly appConfig$ = this.http.get<AppConfig>('/api/config').pipe(
    shareReplay({ bufferSize: 1, refCount: true })
  );
}
```

---

### RxJS Operators Master Cheat Sheet

| Operator | Category | Primary Purpose | Ideal Real-World Use Case |
|---|---|---|---|
| **`forkJoin`** | Combination | Combines multiple streams, waits for all to complete | Admin dashboard loading multiple endpoints |
| **`switchMap`** | Flattening | Cancels previous inner request, keeps latest | Search autocomplete, route param changes |
| **`mergeMap`** | Flattening | Runs multiple inner requests in parallel | Multi-file uploads, concurrent writes |
| **`concatMap`** | Flattening | Runs inner requests sequentially in order | Multi-step form submissions, queued actions |
| **`exhaustMap`** | Flattening | Ignores new emissions while current is running | Login / checkout submit button clicks |
| **`of`** | Creation | Emits values synchronously and completes | Mock data, testing, fallback values |
| **`from`** | Creation | Converts arrays/promises/iterables to stream | Converting `fetch()` Promises or array items |
| **`interval`** | Creation | Emits numbers indefinitely at time intervals | Polling backend APIs, periodic refresh |
| **`timer`** | Creation | Emits after initial delay, optionally intervals | Delayed notifications, scheduled triggers |
| **`tap`** | Utility | Executes side effects without modifying stream | Loading indicators, console logging |
| **`map`** | Transformation | Transforms emitted value into another format | Data extraction, formatting response fields |
| **`filter`** | Filtering | Emits only values matching a condition | Ignoring nulls, listening to specific events |
| **`take`** | Filtering | Emits first *n* items and completes | Single-shot reads without manual unsubscribe |
| **`takeUntil`** | Cleanup | Unsubscribes when notifier observable emits | Preventing memory leaks in `ngOnDestroy` |
| **`shareReplay`** | Multicasting | Multicasts stream and caches latest emission | Caching HTTP calls across multiple `async` pipes |
| **`debounceTime`** | Filtering | Waits for pause in emissions before emitting | Search bar typing pause |
| **`combineLatest`** | Combination | Combines latest values when *any* source emits | Multi-faceted filter bars (search + tags) |

[↑ Back to Table of Contents](#table-of-contents)

---

## 14. NgRx & State Management

NgRx is a Redux-inspired reactive state management architecture for enterprise Angular applications.

### The 5 Pillars of NgRx

```
      ┌─────────────────────────────┐
      │          Component          │
      └──────────────┬──────────────┘
                     │ Dispatches
                     ▼
      ┌─────────────────────────────┐
      │           Action            │
      └──────┬───────────────┬──────┘
             │ Handled by    │ Intercepted by
             ▼               ▼
      ┌─────────────┐ ┌─────────────┐
      │   Reducer   │ │   Effect    │ ───► HTTP / External APIs
      └──────┬──────┘ └──────┬──────┘
             │ Updates       │ Dispatches new Action
             ▼               │
      ┌─────────────────────────────┐
      │            Store            │ ◄┘
      └──────────────┬──────────────┘
                     │ Selects (memoized)
                     ▼
      ┌─────────────────────────────┐
      │          Selector           │
      └─────────────────────────────┘
```

### Complete NgRx Example

#### 1. Actions (`todo.actions.ts`)

```typescript
import { createActionGroup, emptyProps, props } from '@ngrx/store';

export interface Todo {
  id: number;
  title: string;
}

export const TodoActions = createActionGroup({
  source: 'Todo Page',
  events: {
    'Load Todos': emptyProps(),
    'Load Todos Success': props<{ todos: Todo[] }>(),
    'Load Todos Failure': props<{ error: string }>()
  }
});
```

#### 2. Reducer (`todo.reducer.ts`)

```typescript
import { createReducer, on } from '@ngrx/store';
import { TodoActions, Todo } from './todo.actions';

export interface TodoState {
  todos: Todo[];
  loading: boolean;
  error: string | null;
}

export const initialTodoState: TodoState = {
  todos: [],
  loading: false,
  error: null
};

export const todoReducer = createReducer(
  initialTodoState,
  on(TodoActions.loadTodos, state => ({ ...state, loading: true, error: null })),
  on(TodoActions.loadTodosSuccess, (state, { todos }) => ({ ...state, todos, loading: false })),
  on(TodoActions.loadTodosFailure, (state, { error }) => ({ ...state, error, loading: false }))
);
```

#### 3. Selectors (`todo.selectors.ts`)

```typescript
import { createFeatureSelector, createSelector } from '@ngrx/store';
import { TodoState } from './todo.reducer';

export const selectTodoState = createFeatureSelector<TodoState>('todos');

export const selectAllTodos = createSelector(
  selectTodoState,
  state => state.todos
);

export const selectTodosLoading = createSelector(
  selectTodoState,
  state => state.loading
);
```

#### 4. Effects (`todo.effects.ts`)

```typescript
import { inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { TodoService } from '../services/todo.service';
import { TodoActions } from './todo.actions';
import { catchError, map, mergeMap, of } from 'rxjs';

export const loadTodosEffect = createEffect(
  (actions$ = inject(Actions), todoService = inject(TodoService)) => {
    return actions$.pipe(
      ofType(TodoActions.loadTodos),
      mergeMap(() =>
        todoService.getTodos().pipe(
          map(todos => TodoActions.loadTodosSuccess({ todos })),
          catchError(err => of(TodoActions.loadTodosFailure({ error: err.message })))
        )
      )
    );
  },
  { functional: true }
);
```

[↑ Back to Table of Contents](#table-of-contents)

---

## 15. Progressive Web Apps (PWA) in Angular

A Progressive Web App (PWA) delivers native-app-like user experiences on mobile and desktop: offline availability, fast caching, background synchronization, and installation prompts.

### 1. Adding PWA to an Angular Project

```bash
ng add @angular/pwa
```

This automates:
- Adding `@angular/service-worker` to `package.json`.
- Creating `src/manifest.webmanifest` (app identity, theme colors, home screen icons).
- Creating `ngsw-config.json` (caching rules and asset groups).
- Registering `provideServiceWorker('ngsw-worker.js', ...)` in `app.config.ts`.

---

### 2. Service Worker Configuration (`ngsw-config.json`)

```json
{
  "$schema": "./node_modules/@angular/service-worker/config/schema.json",
  "index": "/index.html",
  "assetGroups": [
    {
      "name": "app",
      "installMode": "prefetch",
      "resources": {
        "files": ["/favicon.ico", "/index.html", "/manifest.webmanifest", "/*.css", "/*.js"]
      }
    },
    {
      "name": "assets",
      "installMode": "lazy",
      "updateMode": "prefetch",
      "resources": {
        "files": ["/assets/**", "/*.(svg|cur|jpg|jpeg|png|apng|webp|avif|gif|otf|ttf|woff|woff2)"]
      }
    }
  ],
  "dataGroups": [
    {
      "name": "api-freshness",
      "urls": ["/api/todos/**"],
      "cacheConfig": {
        "maxSize": 100,
        "maxAge": "1h",
        "timeout": "5s",
        "strategy": "freshness"
      }
    }
  ]
}
```

> **DataGroup Strategies:**
> - **`freshness` (Network-First):** Tries network first; if offline or times out (e.g. 5s), serves from cache. Ideal for live data.
> - **`performance` (Cache-First):** Serves immediately from cache; fetches update in the background. Ideal for static/infrequently changing assets.

---

### 3. Detecting & Prompting App Updates with `SwUpdate`

When a new version of the app is published, prompt the user to reload:

```typescript
import { Component, OnInit, inject } from '@angular/core';
import { SwUpdate, VersionReadyEvent } from '@angular/service-worker';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `<router-outlet />`
})
export class AppComponent implements OnInit {
  private swUpdate = inject(SwUpdate);

  ngOnInit() {
    if (this.swUpdate.isEnabled) {
      // Listen for version ready events
      this.swUpdate.versionUpdates
        .pipe(filter((evt): evt is VersionReadyEvent => evt.type === 'VERSION_READY'))
        .subscribe(() => {
          if (confirm('A new version of the application is available. Load update?')) {
            window.location.reload();
          }
        });
    }
  }
}
```

[↑ Back to Table of Contents](#table-of-contents)
