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

Recording rules live here:

```
DevOps/Local/compose/oltp-stack/Prometheus/recording_rules.yml
```

Rules include:
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
- Predictable but limited concurrency
- Latency directly increases thread occupancy
- Connection pool saturation is the main bottleneck

### **WebClient (Non-Blocking)**
- Event‑loop model via Netty
- Allows thousands of concurrent requests with a few threads
- Requires proper:
    - backpressure
    - timeouts
    - concurrency limits
    - scheduler control

---

## JVM & Concurrency Considerations

### **1. Allocation Hotspots**
Reactive chains generate short-lived allocations.  
Blocking IO holds memory longer and increases GC load.

### **2. CPU Behavior**
- RestTemplate/RestClient → steady CPU usage
- WebClient → more context switching but higher throughput

### **3. Thread Pool Design**
- Blocking clients → Apache HttpClient or JDK HttpClient pools
- WebClient → event loop + reactor connection pool

### **4. Backpressure Safety**
WebClient requires:
- `.timeout(...)`
- bounded concurrency (`flatMap(concurrency=X)`)
- controlled schedulers

### **5. Error Propagation**
- Blocking → exceptions bubble directly
- Reactive → errors propagate through the reactive chain

---

## 4. How to Achieve Non‑Blocking Behavior While Using Tomcat (Servlet Stack)

Even though **Tomcat is the primary web server**, your application still achieves non‑blocking IO **whenever WebClient is used**.

### ✔ Tomcat handles inbound requests (blocking)
- Uses servlet worker threads (`http-nio-8080`)
- Each incoming HTTP request occupies exactly one Tomcat thread

### ✔ WebClient uses Netty under the hood
Provided by this dependency:

```
spring-boot-starter-webflux
```

→ Internally uses *reactor-netty* regardless of the main web server.

### ✔ What becomes non-blocking?

Only **outbound HTTP calls** become non-blocking:

```
WebClient → Netty → event-loop threads → async downstream calls
```

Tomcat worker thread is only blocked *until* the reactive chain completes (unless you isolate it — optional).

### ✔ How to make the entire request path fully non-blocking?

You would need to:
1. Remove `spring-boot-starter-web`
2. Use only `spring-boot-starter-webflux`
3. Switch from Tomcat → Netty
4. Rewrite controllers to return `Mono<FxQualResp>`

### ✔ What you have today (best for 99% of enterprises)

**Hybrid model:**

| Part | Technology | Blocking? |
|------|------------|-----------|
| Inbound Web Server | Tomcat | Yes |
| Outbound HTTP (WebClient) | Netty | No |
| Business Logic | Yours | Depends |
| DB (R2DBC) | Yes (non-blocking) | No |

This is **realistic and extremely common**.  
It matches **Fortune 100** microservice patterns — most teams do NOT fully switch to Netty.

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
- understanding JVM, threads, GC, pools
- evaluating microservice efficiency at scale
- observing SRE-grade Golden Signals
- deterministic testing using WireMock
- deep performance analysis for senior Java engineers  

