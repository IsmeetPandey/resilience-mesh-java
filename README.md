# ☕ resilience-mesh-java

<div align="center">

[![Java CI](https://github.com/IsmeetPandey/resilience-mesh-java/actions/workflows/ci.yml/badge.svg)](https://github.com/IsmeetPandey/resilience-mesh-java/actions/workflows/ci.yml)
[![Language: Java 21](https://img.shields.io/badge/Language-Java%2021-ED8B00.svg)](https://openjdk.org/projects/jdk/21/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Zero-dependency fault-tolerance and circuit breaker library for modern Java 21+ with Virtual Threads support.**

</div>

---

## ⚡ Architecture Highlights

- **Zero External Dependencies**: Eliminates dependency conflicts in enterprise production deployments.
- **Lock-Free State Transitions**: High-concurrency state transitions managed via atomic references.
- **Java 21 Virtual Threads Ready**: Non-blocking semantics engineered to operate alongside Project Loom virtual threads.

---

## 🚀 Quick Start

```java
CircuitBreaker breaker = new CircuitBreaker("orders-api", 3, Duration.ofSeconds(5));

String response = breaker.execute(() -> {
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
});
```

---

## 📄 License

MIT License © 2026 [Ismeet Pandey](https://github.com/IsmeetPandey).
