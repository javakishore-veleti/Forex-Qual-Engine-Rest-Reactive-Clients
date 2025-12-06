# Forex-Qual-Engine-Rest-Reactive-Clients

A Spring Boot 3.5.x–based Forex Qualification Engine demonstrating three HTTP client strategies:

- **RestTemplate** — legacy blocking
- **RestClient** — modern Spring 6+ blocking
- **WebClient** — reactive, non-blocking

Built for **production-grade benchmarking**, **observability**, **SRE-driven analysis**, and **high‑concurrency performance comparisons** across blocking and non‑blocking paradigms.

---

# 1. Business Workflow — Forex Qualification Pipeline

The API:

```
POST /api/fx/qualify?clientType=rest_client|rest_template|web_client
```

Triggers a multi-step workflow:

1. Customer validation
2. Promo validation
3. Product books retrieval
4. Market interest rate retrieval
5. Response assembly via `FxQualResp`

These are orchestrated via a clean **strategy pattern**:

```
FxQualRestTemplateImpl  → Blocking
FxQualRestClientImpl    → Blocking (new client)
FxQualWebClientImpl     → Reactive (Netty)
```

The workflow engine:

```
GenericQualWFExecImpl
```

executes the steps consistently across all three implementations.

---

# 2. Expert SREs — What Matters

This repo is designed to showcase the KPIs, SLIs, SLOs, and saturation metrics an **SRE cares about in real production environments**, especially at scale (50+ pods, 100k concurrent users).

## Critical Golden Signals

| Signal | What It Tells You | Metric Source |
|--------|--------------------|---------------|
| **Latency** | Client strategy comparison, degradation under load | `http_server_requests_seconds_*` |
| **Traffic (RPS)** | Load level & request distribution | `http_server_requests_seconds_count` |
| **Errors** | SLO adherence, downstream reliability | 4xx/5xx request counts |
| **Saturation** | Thread pools, connection pools, memory, CPU | JVM metrics, Tomcat metrics |

---

## Detailed SRE Dimensions & Dashboards

### **1. Client Strategy Latency Comparison Dashboard**
- p50, p90, p95, p99, p999 for:
    - RestTemplate
    - RestClient
    - WebClient
- Dimensions:
    - URI
    - HTTP method
    - status code
    - downstream service

### **2. Resource Saturation Dashboard**
- Tomcat thread pool:
    - `tomcat_threads_busy`
    - `tomcat_connections_current`
- Netty event-loop pressure (WebClient):
    - `reactor_netty_request_pending`
- JVM:
    - Heap usage %
    - Allocation rate
    - GC pause p99

### **3. Error Budget Burn Dashboard**
Based on the recording rules:
- Fast burn rate (5m window)
- Slow burn rate (1h window)
- Error budget consumption %
- SLO alerts (e.g., burnrate_fast > 1)

### **4. Concurrency Scaling Dashboard**
Monitor impacts of scaling pods:
- CPU saturation per pod
- Memory usage per pod
- Live vs peak threads
- Backpressure in WebClient
- Connection pool exhaustion

### **5. Downstream Dependency Health**
- promo / customer / market latency
- per-client type downstream error rate
- cascading failure indicators

---

## Recording Rules
Full Prometheus rules are maintained here:

```
DevOps/Local/compose/oltp-stack/Prometheus/recording_rules.yml
```

These include:
- SLO burn
- latency histograms
- client-type comparisons
- JVM + GC metrics
- business KPIs
- saturation metrics

---

# 3. Expert Java Developers — Deep Technical Insights

This repo is also built for senior/principal Java engineers who care about concurrency, thread models, and JVM behavior.

---

## Threading Models

### **RestTemplate & RestClient (Blocking)**
- Thread-per-request model (Tomcat)
- Uses servlet worker threads
- Best for straightforward synchronous workflows
- Latency directly increases thread occupancy
- Connection pool saturation is key bottleneck

### **WebClient (Non-Blocking)**
- Event‑loop model via Netty
- Allows thousands of requests with few threads
- Requires careful handling:
    - backpressure
    - timeouts
    - concurrency operators
- Suitable for high fan-out or async scenarios (5% of workloads)

---

## JVM & Concurrency Considerations

### **1. Allocation Hotspots**
High GC pressure is expected when running N concurrent requests:
- Reactive chains produce small temporary objects
- Blocking IO holds memory longer

### **2. CPU Behavior**
- RestTemplate/RestClient → predictable CPU usage
- WebClient → more context switching, but better at scale

### **3. Thread Pool Design**
- RestTemplate/RestClient → Apache HttpClient or JDK HttpClient pools
- WebClient → event loop + connection pool interplay

### **4. Deadlock/Backpressure Safety**
WebClient requires:
- `.timeout()`
- bounded concurrency (`flatMap(concurrency=X)`)
- controlled schedulers

### **5. Error Propagation Differences**
- Blocking → exceptions thrown immediately
- Reactive → errors travel Mono/Flux chain

---

## Performance Expectations Under Load

| Aspect | RestTemplate | RestClient | WebClient |
|--------|--------------|------------|-----------|
| Latency | Medium | Medium | Lowest |
| Throughput | Medium | High | Very High |
| Thread Usage | High | High | Very Low |
| Backpressure | None | None | Yes |
| Complexity | Low | Low | High |

---

# Running the Engine

Start WireMock:

```bash
docker compose -f DevOps/Local/compose/wiremock/docker-compose.yml up
```

Run application:

```bash
./mvnw spring-boot:run
```

Call API:

```bash
curl -X POST "http://localhost:8080/api/fx/qualify?clientType=web_client"   -H "Content-Type: application/json"   -d '{ "customerId": "123", "pair": "EURUSD", "promoCode": "WELCOME" }'
```

---

# Summary

This repository is a **complete production blueprint** for:
- comparing blocking vs reactive strategies
- understanding JVM, threads, GC and connection pools
- evaluating microservice efficiency at scale
- observing SRE-grade metrics and golden signals
- running deterministic downstream mocks
- enabling deep analysis for senior Java engineers  

