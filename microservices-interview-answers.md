# Microservices Interview Q&A (How to Say It in an Interview)

Tip: Start with the one-line answer, then add a short example. Keep each answer under 30-40 seconds.

---

## A. Data Consistency and Transactions

### 1. Payment succeeds but the inventory service fails (Order → Payment → Inventory). What happens?
"The system ends up in an inconsistent state: the customer's money is deducted but the item is not reserved. To fix this, I would use the **Saga pattern**. If inventory fails, a compensating action runs automatically, like refunding the payment and cancelling the order. I can also add retries for temporary failures before triggering the rollback."

### 2. How does the Saga pattern undo actions automatically?
"A Saga breaks one big transaction into small local transactions, one per service. If any step fails, the system runs a **compensating action** for each step that already succeeded. For example, if payment was done and inventory fails, we trigger a refund."

### 3. Choreography vs Orchestration in Saga?
"In **Choreography**, there is no central controller. Each service listens to events and reacts. For example, Order publishes an event, Payment picks it up, processes it, and publishes the next event.

In **Orchestration**, a central orchestrator tells each service what to do step by step, and it also handles rollbacks.

Choreography is simpler for small flows. Orchestration is easier to track and debug for complex flows."

### 14. How do you handle database transactions across multiple microservices?
"I avoid distributed transactions like 2PC because they are slow and tightly coupled. I use the **Saga pattern**: each service does its own local transaction, and if one fails, compensating actions undo the earlier steps."

### 13. How do you stop duplicate payments if the Payment API is called twice?
"I use **idempotency**. The client sends a unique transaction ID (idempotency key) with each request. The server checks if it has already processed that ID. If yes, it ignores the duplicate and returns the stored result of the first request."

### 22. How do you handle concurrent updates to the same data?
"I use **optimistic locking**: each record has a version number, and if the version doesn't match during update, the update is rejected and retried. For highly conflicting data, I use **pessimistic locking**, which locks the record during the update. Idempotency also helps avoid duplicate updates."

### 43. How do you maintain data integrity if services update data out of order?
"I use versioning or optimistic locking so old updates are rejected. Or I use an event queue that preserves order, so updates are processed in the sequence they happened."

### 41. Two services show different data for the same user. How do you fix it?
"First, I check logs and timestamps to find where the data diverged, usually a missed or delayed event. Then I fix the root cause and use event-driven sync with retries so the data becomes eventually consistent."

### 45. Can a system have both Strong Consistency and High Availability?
"Not during a network partition. As per the **CAP theorem**, when a partition happens, I must choose: Consistency (accurate data but some requests may fail) or Availability (always respond, but data may be stale). Banking systems usually choose consistency, and social feeds usually choose availability."

---

## B. Database Design

### 4. Each service has its own DB. Should I call another service every time I need its data?
"No. That adds latency and creates tight coupling. Instead, I use **data replication through events**. The owning service publishes updates, and other services keep a small local copy of just the data they need."

### 5. Why should each microservice have its own database?
"To keep services **independent**. One service can change its schema, scale, or fail without affecting the others. Sharing a DB creates hidden coupling."

### 39. How do you scale databases without downtime?
"I scale gradually. I add **read replicas** for read-heavy traffic, use **sharding** for very large data, and do migrations with rolling or blue-green deployments so traffic shifts slowly."

### 40. How do you handle database schema changes?
"I always use a **backward-compatible** approach. First I add the new column, then update services to read and write both old and new, and only in a later release I remove the old column."

### 42. How do you handle cache invalidation and stale data?
"Three ways: **TTL** so cache entries expire automatically, **event-based invalidation** where cache is cleared when the DB is updated, and the **Cache-Aside pattern** where the app reads from cache first and loads from DB on a miss."

### 50. High read traffic, low write traffic. How do you design it?
"I use **CQRS**: separate the write model from the read model. Writes go to a write DB, which updates a read-optimized DB asynchronously through events. This lets me scale reads independently."

### 51. Why might CQRS show outdated data and how do you fix it?
"Because of **eventual consistency**, as there is a delay in syncing the read DB. I can reduce the delay with real-time event streaming, show a UI hint like 'updating...', or route critical reads to the write DB."

### 52. When should you NOT use CQRS?
"For simple CRUD apps, low-traffic systems, systems needing strict strong consistency like core banking, or when the team is too small to handle the extra complexity."

---

## C. API Gateway, Discovery and Routing

### 7. Why use an API Gateway instead of calling services directly?
"It gives the client a **single entry point**, so the client doesn't need to know many service URLs. It also handles common concerns in one place: authentication, logging, rate-limiting, and routing."

### 6. API Gateway suddenly gets heavy traffic. What do you do?
"I scale the gateway **horizontally** with more instances behind a load balancer. I add **rate-limiting** and **caching**, and for heavy operations I use a message queue like Kafka or RabbitMQ so requests don't fail immediately."

### 53. What if the API Gateway goes down, and how do you prevent it?
"The whole system becomes unreachable, so the gateway is a single point of failure. I prevent it with multiple gateway instances behind a load balancer, multi-zone deployment, and auto-scaling."

### 54. Different data for Mobile vs Web clients?
"I use the **BFF (Backend for Frontend)** pattern. I create a separate gateway or composition layer per client type, so each gets only the data it needs and there is no over-fetching."

### 56. How do services discover each other dynamically?
"Through a **Service Registry** like Eureka or Consul. Each service registers itself at startup, and other services ask the registry for active instances instead of hardcoding IPs."

### 55. Traffic still goes to an instance that is down. What went wrong?
"Health checks or service discovery failed. The load balancer didn't detect the dead instance. I would fix the **liveness and readiness probes** so failures are detected quickly."

### 57. Autoscaling adds instances but they get no traffic. Why?
"Most likely the new instances are failing health or readiness checks, the service registry hasn't updated yet, or the load balancer hasn't refreshed its routing table."

### 28. How do you fix high latency from too many inter-service calls?
"I use an **API Composition layer** to fetch data in one go, add **caching** to avoid repeated calls, and use **asynchronous** communication where a response isn't needed immediately."

### 36. Is it good design if one user request triggers 50+ internal calls?
"No. I would reduce it using an API composition layer or BFF, cache frequently used data, and keep local read-copies of data using event-driven replication."

---

## D. Communication and Resilience

### 8. Synchronous vs Asynchronous communication?
"**Synchronous** when I need an immediate response, like login or payment confirmation. **Asynchronous** for background work like emails, analytics, and report updates, or to reduce load during high traffic."

### 9. An app keeps calling a service that is down. What do you do?
"I use a **Circuit Breaker** to stop calling it for some time. Along with that: retries with delay, timeouts, and a **fallback response** like 'please try again later'."

### 12. A service fails because of temporary network issues?
"I add a **retry mechanism** with a delay and a strict limit, like 3 retries. If it still fails, the circuit breaker opens, and I return a fallback such as cached data."

### 18. Service A calls Service B and B is down?
"A's request will fail or hang. So I set **timeouts**, add **retries** and a **circuit breaker**, and return a fallback response so the failure doesn't spread through the whole system."

### 46. What if retries increase system load?
"I use **exponential backoff** (increase the wait between retries), add **jitter** (random delay) so all clients don't retry together, limit the max retries, and use a circuit breaker to stop retrying a dead service."

### 35. Third-party API fails randomly?
"I check the provider's status page and my logs. To protect my system, I use strict timeouts, limited retries, a circuit breaker, and cached fallback responses."

### 44. Half of the services go down due to network failure?
"The system should **degrade gracefully** and not crash. Circuit breakers, timeouts, and fallbacks (cached or partial data) keep core features working, while alerts trigger auto-recovery."

### 23. How do you stop a buggy service from spreading bad event data?
"I enforce a strict **event contract/schema**, make consumers validate incoming data, use idempotency, and use a circuit breaker to temporarily stop consuming from the faulty service."

---

## E. Messaging and Queues

### 25. How do you fix a message queue backlog?
"I remove or skip **poison-pill** (bad) messages, restart crashed workers, **increase the number of consumers**, optimize slow APIs they call, and rate-limit incoming tasks."

### 48. Queue produces duplicate messages?
"I make the consumer **idempotent**. Every message has a unique ID, and I store processed IDs in a DB or cache so a duplicate is ignored."

### 49. How do you ensure messages are processed in order?
"I use **partitioning**: messages with the same key (like userId or orderId) go to the same partition, so they are consumed in order. Alternatively I add sequence numbers and reorder at the consumer."

### 34. How do you implement real-time updates (chat, notifications)?
"I use **WebSockets** for a persistent connection. When an event happens, it goes to a message queue, a notification service picks it up and pushes it to the client. I also store messages in a DB so offline users get them later."

---

## F. Scaling and Performance

### 15. Scaling a single service under heavy load?
"Scale **horizontally**: add more instances behind a load balancer, enable auto-scaling, optimize the code, and add caching."

### 19. One slow microservice is affecting the whole system?
"First I use monitoring and logs to find the root cause. Then I optimize slow DB queries or code, add caching, scale the service, and use async calls or timeouts to isolate the impact."

### 32. Design for millions of requests per second?
"Keep services **stateless**, scale horizontally behind load balancers, use heavy caching, async processing, **database sharding**, read replicas, and auto-scaling."

### 30. Global latency for users in different regions?
"Deploy in **multiple regions**, use location-based routing to send users to the nearest server, use a **CDN** and caching, and use multi-region read replicas for the database."

### 31. High reliability for critical financial data?
"Run multiple instances, use DB replication and backups, add retries and failover, and strictly enforce transactions and validation checks."

### 33. Sudden CPU spike after a deployment?
"First I check recent code changes for bugs like infinite loops, and **roll back** if found. If code is fine, I check for traffic surges and scale up, or look for unoptimized queries and heavy APIs."

### 47. Service works correctly but is slow?
"I check CPU and memory metrics, use **distributed tracing** to find the bottleneck like a slow DB query or external call, check thread pools, then optimize queries or add caching."

### 37. If network latency increases, are microservices still better than a monolith?
"Not necessarily. A monolith runs in one process with no network hop, so if inter-service latency is very high, a monolith can be much faster."

---

## G. Debugging and Observability

### 10. Request fails across five microservices. How do you debug?
"I generate a unique **Trace ID / Request ID** at the entry point and pass it in headers to every service. Then I use distributed tracing (like Zipkin) and centralized logging to follow that ID and find exactly where it failed."

### 24. How do you trace a request in centralized logging?
"Create a **Correlation ID** when the request enters the system, pass it via headers, log it in every service, and then filter the logs by that ID."

### 17. Works in staging but fails in production?
"I compare environment configs and variables, check logs, verify external dependencies, look for data differences, and try to reproduce the same state in staging."

---

## H. Deployment and Configuration

### 11. Deploy a new version without downtime?
"I use zero-downtime strategies:
- **Blue-Green**: run old and new side by side, then switch traffic.
- **Rolling**: update instances one by one.
- **Canary**: release to a small group of users first."

### 16. Manage configs for 10 microservices across environments?
"Use **centralized configuration** like Spring Cloud Config. All configs are stored in one place and services fetch them dynamically for their environment."

### 27. Roll out a feature to only 10% of users?
"I take a stable identifier like userId, hash it, and use modulo 100 to put users into buckets 0-99. The feature is enabled for buckets 0-9. Since the same user always lands in the same bucket, the experience stays consistent."

### 38. Main strategies for testing microservices?
"- **Unit tests**: test logic in isolation by mocking dependencies.
- **Integration tests**: test DB and API interactions.
- **End-to-end tests**: test full user flows across the system."

(Bonus: mention **contract testing** like Pact to check service-to-service agreements.)

---

## I. Security

### 20. How do you handle security in microservices?
"I secure APIs with **JWT/OAuth** tokens and HTTPS, validate all inputs, use the API Gateway for centralized security, enforce **service-to-service authentication**, and apply **RBAC** for permissions."

### 26. Internal service accidentally exposed to outside traffic?
"Keep internal services behind the API Gateway, restrict communication using network policies, use service-to-service authentication, and apply rate limiting."

### 58. An internal service is compromised. How do you limit damage?
"I follow **Zero Trust**: strict service authentication (mTLS/JWT), tight network policies, rate limiting to stop DoS spreading, and I immediately revoke access when suspicious activity is found."

---

## J. Architecture Decisions

### 21. When should you NOT use microservices?
"For small or simple applications, small or inexperienced teams, or when you need fast delivery with tight coordination. A **monolith** is better in those cases."

### 29. How do you break down tightly coupled modules when migrating from a monolith?
*(The original answer in the source was wrong. It talked about latency. Here is the correct one.)*

"I don't rewrite everything at once. I follow these steps:
1. Find natural boundaries using **Domain-Driven Design (bounded contexts)**, like Orders, Payments, Users.
2. Use the **Strangler Fig pattern**: put a gateway in front, and move one module at a time into a new microservice while the rest stays in the monolith.
3. Break shared database dependencies by giving each new service its own data, and use events to keep data in sync.
4. Replace direct method calls between modules with APIs or events.
5. Start with the least risky or most independent module first, and keep rollback possible."

---

## Quick Cheat Sheet (Revise Before Interview)

| Problem | Go-to Solution |
|---|---|
| Distributed transaction | Saga (choreography / orchestration) |
| Duplicate requests/messages | Idempotency key |
| Service down | Timeout + Retry + Circuit Breaker + Fallback |
| Retry storms | Exponential backoff + Jitter |
| Heavy traffic | Horizontal scaling + Cache + Queue + Rate limit |
| Tracing across services | Trace/Correlation ID + Zipkin + central logs |
| Zero-downtime deploy | Blue-Green / Rolling / Canary |
| Read-heavy system | CQRS + read replicas + cache |
| Message ordering | Partition by key |
| Config management | Spring Cloud Config |
| Service discovery | Eureka / Consul |
| Mobile vs Web responses | BFF |
| Security | JWT/OAuth + mTLS + RBAC + Gateway |
