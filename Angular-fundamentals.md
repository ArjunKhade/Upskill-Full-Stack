# Angular Fundamentals — Short Notes
> Custom Directives · Pipes · Route Guards · Resolvers

---

## 1. Custom Directives

### Types of Directives

| Type | Purpose | Example |
|---|---|---|
| **Component** | Directive with a template | `<app-user>` |
| **Structural** | Adds/removes DOM elements | `*ngIf`, `*ngFor` |
| **Attribute** | Changes appearance/behavior | `[ngClass]`, `[appHighlight]` |

---

### Attribute Directive (Class-based)

```typescript
import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective {
  @Input('appHighlight') color: string = 'yellow';

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter') onMouseEnter() {
    this.el.nativeElement.style.backgroundColor = this.color;
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.el.nativeElement.style.backgroundColor = '';
  }
}
```

**Usage:**
```html
<p [appHighlight]="'lightblue'">Hover me!</p>
```

**Use Case:** Dynamically style elements, add tooltip behavior, restrict input, etc.

---

### Attribute Directive (Function-based / Standalone — Angular 15+)

```typescript
import { Directive, ElementRef, HostListener, inject } from '@angular/core';

@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {
  private el = inject(ElementRef);

  @HostListener('mouseenter') onMouseEnter() {
    this.el.nativeElement.style.backgroundColor = 'yellow';
  }
}
```

> No need to declare in `NgModule`. Import directly into `imports[]` of a standalone component.

---

### Structural Directive (Class-based)

```typescript
import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

@Directive({ selector: '[appIf]' })
export class AppIfDirective {
  constructor(
    private templateRef: TemplateRef<any>,
    private vcr: ViewContainerRef
  ) {}

  @Input() set appIf(condition: boolean) {
    this.vcr.clear();
    if (condition) {
      this.vcr.createEmbeddedView(this.templateRef);
    }
  }
}
```

**Usage:**
```html
<div *appIf="isLoggedIn">Welcome!</div>
```

**Use Case:** Custom `*ngIf`-like logic, permission-based rendering (`*hasPermission`).

---

### Common HostListener Patterns

```typescript
// Restrict input to numbers only
@HostListener('input', ['$event'])
onInput(event: Event) {
  const input = event.target as HTMLInputElement;
  input.value = input.value.replace(/[^0-9]/g, '');
}

// Click outside to close dropdown
@HostListener('document:click', ['$event.target'])
onClick(target: HTMLElement) {
  if (!this.el.nativeElement.contains(target)) {
    this.close.emit();
  }
}
```

**Generate directive:** `ng generate directive directives/highlight`

---

## 2. Custom Pipes

### Pure vs Impure

| | Pure | Impure |
|---|---|---|
| Runs when | Input reference changes | Every CD cycle |
| Performance | Efficient | Costly |
| Default | `pure: true` | `pure: false` |

> **Use impure** only when the pipe depends on mutable state (e.g., array push without new ref).

---

### Custom Pipe (Class-based)

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'reverseStr' })
export class ReverseStrPipe implements PipeTransform {
  transform(value: string): string {
    return value.split('').reverse().join('');
  }
}
```

**Usage:**
```html
{{ 'Angular' | reverseStr }}  <!-- ralugnA -->
```

---

### Pipe with Parameters

```typescript
@Pipe({ name: 'truncate' })
export class TruncatePipe implements PipeTransform {
  transform(value: string, limit: number = 50, ellipsis: string = '...'): string {
    return value.length > limit ? value.substring(0, limit) + ellipsis : value;
  }
}
```

```html
{{ description | truncate:100:'…' }}
```

---

### Standalone Pipe (Function-based style — Angular 17+)

```typescript
@Pipe({
  name: 'reverseStr',
  standalone: true
})
export class ReverseStrPipe implements PipeTransform {
  transform(value: string): string {
    return value.split('').reverse().join('');
  }
}
```

> Import directly into standalone component's `imports[]`.

---

### Chaining Pipes

```html
{{ name | uppercase | truncate:20 }}
```

**Generate pipe:** `ng generate pipe pipes/truncate`

---

## 3. Route Guards

### Guard Types

| Guard | Purpose |
|---|---|
| `CanActivate` | Block route activation (e.g. auth check) |
| `CanActivateChild` | Block child route activation |
| `CanDeactivate` | Prevent leaving a route (e.g. unsaved form) |
| `CanLoad` | Prevent loading lazy modules (deprecated in v17, use `CanMatch`) |
| `CanMatch` | Control whether a route is matched at all |
| `Resolve` | Pre-fetch data before route activates |

---

### CanActivate — Class-based

```typescript
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.auth.isLoggedIn()) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
```

```typescript
// Route config
{ path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }
```

---

### CanActivate — Function-based (Angular 15+)

```typescript
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isLoggedIn()) return true;
  return router.parseUrl('/login');
};
```

```typescript
{ path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] }
```

> **Preferred in Angular 15+.** No class, no `@Injectable`.

---

### CanDeactivate — Class-based

```typescript
export interface CanComponentDeactivate {
  canDeactivate(): boolean;
}

@Injectable({ providedIn: 'root' })
export class UnsavedChangesGuard implements CanDeactivate<CanComponentDeactivate> {
  canDeactivate(component: CanComponentDeactivate): boolean {
    return component.canDeactivate()
      ? true
      : confirm('You have unsaved changes. Leave?');
  }
}
```

```typescript
// In the component:
canDeactivate(): boolean {
  return !this.form.dirty;
}
```

**Use Case:** Form with unsaved changes — prompt user before navigating away.

---

### CanDeactivate — Function-based

```typescript
export const unsavedChangesGuard: CanDeactivateFn<FormComponent> = (component) => {
  return component.form.dirty
    ? confirm('Unsaved changes. Leave?')
    : true;
};
```

---

### CanActivateChild — Class-based

```typescript
@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivateChild {
  constructor(private auth: AuthService) {}

  canActivateChild(): boolean {
    return this.auth.hasRole('admin');
  }
}
```

```typescript
{
  path: 'admin',
  canActivateChild: [RoleGuard],
  children: [
    { path: 'users', component: UsersComponent }
  ]
}
```

**Use Case:** Protect all child routes of an admin section.

---

### CanMatch — Function-based (Angular 15+, replaces CanLoad)

```typescript
export const featureFlagGuard: CanMatchFn = () => {
  const featureService = inject(FeatureFlagService);
  return featureService.isEnabled('newDashboard');
};
```

```typescript
{ path: 'dashboard', canMatch: [featureFlagGuard], loadChildren: () => import('./dashboard/dashboard.module') }
```

**Use Case:** Feature flags, A/B testing — match a route only under certain conditions.

---

## 4. Resolver

### What is a Resolver?

A resolver pre-fetches data **before** the route activates. The component receives ready data via `ActivatedRoute`, avoiding loading spinners inside the component.

---

### Resolver — Class-based

```typescript
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserResolver implements Resolve<User> {
  constructor(private userService: UserService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<User> {
    const id = route.paramMap.get('id')!;
    return this.userService.getUserById(id);
  }
}
```

```typescript
// Route config
{ path: 'user/:id', component: UserDetailComponent, resolve: { user: UserResolver } }
```

```typescript
// Component — consume resolved data
constructor(private route: ActivatedRoute) {}

ngOnInit() {
  this.user = this.route.snapshot.data['user'];
  // or reactively:
  this.route.data.subscribe(data => this.user = data['user']);
}
```

---

### Resolver — Function-based (Angular 14+)

```typescript
import { inject } from '@angular/core';
import { ResolveFn, ActivatedRouteSnapshot } from '@angular/router';

export const userResolver: ResolveFn<User> = (route: ActivatedRouteSnapshot) => {
  const id = route.paramMap.get('id')!;
  return inject(UserService).getUserById(id);
};
```

```typescript
{ path: 'user/:id', component: UserDetailComponent, resolve: { user: userResolver } }
```

**Use Case:** Pre-load user profile, product details, or config data before a page renders.

---

## Quick Reference — Class vs Function

| Feature | Class-based | Function-based |
|---|---|---|
| Angular version | All versions | Angular 14–15+ |
| Boilerplate | More (class + `@Injectable`) | Minimal |
| DI | Constructor injection | `inject()` |
| Recommended (modern) | Legacy support | ✅ Preferred |

---

## Use Case Summary

| Feature | When to Use |
|---|---|
| **Attribute Directive** | DOM manipulation, styling, events on elements |
| **Structural Directive** | Conditionally add/remove DOM elements |
| **Custom Pipe** | Transform display values in templates |
| **CanActivate** | Authentication / authorization check before entering route |
| **CanActivateChild** | Protect all child routes in a section |
| **CanDeactivate** | Prevent leaving a route with unsaved changes |
| **CanMatch** | Feature flags, lazy load control |
| **Resolver** | Pre-fetch data before component initializes |
