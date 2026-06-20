# Interview Preparation Notes — Arjun Khade

---

## Tell Me About Yourself

### Frontend Role

My name is Arjun Khade. I am a Full Stack Developer with 3.5 years of experience, mainly working with Angular, TypeScript, Java, and Spring Boot.

Currently, I am working at Aloha Technology, where I develop and maintain enterprise web applications. My work includes developing reusable UI components, building reactive forms, integrating REST APIs, implementing responsive designs, and improving application performance.

I have experience with Angular features like lazy loading, route guards, reactive forms, RxJS, and performance optimization techniques such as image optimization, code splitting, and OnPush change detection.

I also work closely with backend teams, participate in code reviews, bug fixing, and production support activities. I am now looking for an opportunity where I can work on larger-scale projects and continue growing my Angular and full-stack development skills.

---

### Full Stack Role

My name is Arjun Khade. I am a Full Stack Developer with 3.5 years of experience in developing web applications using Angular, TypeScript, Java, and Spring Boot.

Currently, I am working at Aloha Technology, where I develop and maintain enterprise applications. On the frontend, I work with Angular, TypeScript, RxJS, state management, reactive forms, reusable components, and REST API integration.

On the backend, I work with Java and Spring Boot to develop RESTful APIs, implement business logic, and interact with databases.

I have experience in application performance optimization using techniques such as lazy loading, code splitting, image optimization, and efficient API handling. I also work on authentication, authorization, debugging, bug fixing, production support, and code reviews.

I collaborate closely with frontend, backend, QA, and DevOps teams in an Agile environment to deliver high-quality solutions. Over the years, I have gained strong experience across the full software development lifecycle, from requirement analysis to deployment and support.

I am currently looking for a new opportunity where I can contribute to challenging projects, enhance my full-stack development skills, and continue growing as a Java and Angular developer.

---

## Project Architecture

### Tell Me About Your Project Architecture (Frontend + Backend)

I work on a Healthcare Solution with an Angular frontend and a Java Spring Boot backend.

**Frontend:** We follow a feature-based architecture. The application is organized into Core, Shared, and Feature modules. Key modules include Attendance, Data Collection, Medical Event, Behaviour Tracking, Diagnosis & Medication. We use Reactive Forms, Dependency Injection, service-based API communication, lazy loading, and standalone components to build a scalable, maintainable, and high-performance application.

**Backend:** We follow a layered architecture consisting of Controller, Service, Repository, and Database layers. Controllers expose REST APIs, Services contain business logic, and Repositories handle database operations using Spring Data JPA. We also use Spring Security with JWT authentication, DTOs for data transfer, global exception handling, validation, and centralized logging. This architecture provides scalability, maintainability, security, and clear separation of concerns.

**Database:** MySQL is used as the database, and Hibernate/JPA manages entity-to-table mapping and relationships.

---

## Responsibilities

### Frontend

- Developing Angular components and reactive forms
- Building reusable components and shared services
- Creating dynamic forms with more than 100 fields using FormGroup and FormArray
- Integrating REST APIs with RxJS
- Implementing route guards and security
- Optimizing application performance using lazy loading and image optimization
- Fixing production issues and participating in code reviews
- Collaborating with backend developers to ensure smooth API integration

### Full Stack

- Developed scalable healthcare application features using Angular, Spring Boot, and MySQL
- Created reusable UI components and complex Reactive Forms with dynamic validations
- Built and maintained REST APIs, business logic, and database integrations
- Implemented secure authentication and authorization using JWT and Spring Security
- Integrated frontend and backend modules while ensuring efficient API communication
- Performed performance optimization, bug fixing, code reviews, and production support

---

## CI/CD

### How Is Your Project Deployed?

We use Bitbucket CI/CD pipelines for deployment. When code is merged into the main branch, the pipeline automatically starts. It installs dependencies, runs builds and tests, performs code quality checks, and generates production artifacts. After successful validation, the application is deployed to the required environment such as Dev, QA, or Production. This automation helps us maintain release quality and reduces manual deployment effort.

### What Is Your Role in CI/CD?

My role is to monitor pipeline execution, fix build issues, resolve compiler warnings, ensure successful Angular builds, review deployment logs, and coordinate with the DevOps team whenever deployment-related issues occur.

I primarily worked on the application development side and monitored CI/CD pipelines. The deployment was automated through Bitbucket Pipelines. After code was merged, the pipeline built and validated the application and deployed it to the target environment. The infrastructure was managed by the DevOps team, so I was not directly involved in configuring the hosting environment.

---

## Design Principles & Patterns

### SOLID Principles

1. **Single Responsibility Principle** — A class or component should only do one thing, making it easier to understand and test.
2. **Open/Closed Principle** — Design classes so you can add new features by extending them, not by changing existing code.
3. **Liskov Substitution Principle** — Subclasses should be replaceable for their parent class, so components or services behave consistently.
4. **Interface Segregation Principle** — Create small, focused interfaces rather than one large one, making it easier to implement exactly what you need.
5. **Dependency Inversion Principle** — High-level modules shouldn't depend on low-level details; both should depend on abstractions.

By applying these principles, both Angular and Java code stays flexible, easier to maintain, and more scalable as the project grows.

---

### Design Patterns

1. **Singleton Pattern** — Angular services are singletons by default, ensuring a single instance is shared across the app.
2. **Factory Pattern** — Used to create services or components dynamically based on input.
3. **Container/Presentational Pattern** — Containers handle the logic, while presentational components focus only on the view.
4. **Observer Pattern** — Used with RxJS; components subscribe to data streams so they react to changes automatically.

These patterns help keep the code organized, reusable, and easier to scale.
