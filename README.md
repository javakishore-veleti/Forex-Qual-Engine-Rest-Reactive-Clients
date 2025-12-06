# Forex-Qual-Engine-Rest-Reactive-Clients

A Spring Boot 3.5.x–based Forex Qualification Engine demonstrating three HTTP client strategies:

- **RestTemplate** — legacy blocking
- **RestClient** — modern Spring 6+ blocking
- **WebClient** — reactive, non‑blocking

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
- Client Strategy means RestTemplate vs RestClient vs WebClient 
- p50, p90, p95, p99, p999 for RestTemplate / RestClient / WebClient
- Dimensions: URI, HTTP method, status code, downstream service (read more below on dimensions)

### **2. Resource Saturation Dashboard**
- Tomcat thread pool:
    - `tomcat_threads_busy`
    - `tomcat_connections_current`
- Netty (WebClient):
    - `reactor_netty_request_pending`
- JVM metrics: heap %, GC rate, allocation rate

Read more below on above dimensions

### **3. Error Budget Burn Dashboard**
From recording rules:
- Fast burn rate (5m window)
- Slow burn rate (1h window)
- Error budget %
- Alerts on burnrate thresholds

### **4. Concurrency Scaling Dashboard**
- CPU per pod
- Memory per pod
- Thread usage
- Backpressure events
- Connection pool behavior

### **5. Downstream Dependency Health**
- Promo / Customer / Product / Market latency
- Downstream error rates
- Cascading failures


Read more below on dimensions mentioned above

#### 🔵 **Dimensions Notes for Client Strategy Latency Comparison**

This document explains the **four key dimensions** used in the *Client Strategy Latency Comparison Dashboard* and how each dimension maps to **Prometheus**, **Grafana**, **Jaeger**, and the **OpenTelemetry Collector**.


#### Dimension 1. URI Dimension

##### What It Represents
The API endpoint being invoked, such as `/api/fx/qualify`, `/customer/{id}`, `/promo/{code}`.

---

##### Prometheus
Micrometer exposes URI as a low-cardinality label:

```
http_server_requests_seconds_bucket{uri="/api/fx/qualify", ...}
```

✔ Prometheus automatically normalizes URI templates  
✔ Prevents metric cardinality explosion

---

##### Grafana
Use URI as a dashboard variable:

```
label_values(http_server_requests_seconds_count, uri)
```

Recommended panels:
- Latency per URI
- Throughput per URI
- Error rate per URI

---

##### Jaeger
Span attribute:

```
http.target = "/api/fx/qualify"
```

Useful for:
- Filtering traces by endpoint
- Comparing RestTemplate vs RestClient vs WebClient behavior

---

##### OpenTelemetry Collector
Pass-through attribute:

```
attributes:
  - key: http.target
```

You may extend or rename these attributes before exporting.

---

#### Dimension 2. HTTP Method Dimension

##### What It Represents
The HTTP verb associated with the request (GET, POST, PUT, DELETE).

---

##### Prometheus
Micrometer exposes method label:

```
http_server_requests_seconds_count{method="POST"}
```

---

##### Grafana
Use as a variable:

```
label_values(http_server_requests_seconds_count, method)
```

Useful for:
- Latency by method
- Error rate by method
- Distinguishing heavy POST workloads vs light GET calls

---

##### Jeager
Span attribute:

```
http.method = "POST"
```

Allows SREs and developers to:
- Filter traces for slow POST downstream calls
- Compare blocking vs reactive performance
- Identify retry behaviors

---

##### OpenTelemetry Collector
Method forwarded automatically:

```
attributes:
  - key: http.method
```

Can be enriched or transformed for consistency.

---

#### Dimension 3. Status Code Dimension

##### What It Represents
HTTP response status code (200, 400, 500).

---

##### Prometheus
Captured in:

```
http_server_requests_seconds_count{status="200"}
```

Recommended queries:
- `rate(...{status=~"5.."}[5m])` → server errors
- `rate(...{status=~"4.."}[5m])` → client errors

---

##### Grafana
Create panels:
- Error rate over time
- Error heatmap grouped by status

Variable example:

```
label_values(http_server_requests_seconds_count, status)
```

---

##### Jaeger
Span attribute:

```
http.status_code = 500
```

Useful for:
- Tracing root cause of 4xx/5xx
- Error correlation with downstream calls
- Latency vs status comparisons

---

##### OpenTelemetry Collector
Forwarded as-is:

```
attributes:
  - key: http.status_code
```

Useful for routing:
- send 5xx traces to separate pipeline
- sample errors aggressively

---

#### Dimension 4. Downstream Service Dimension

##### What It Represents
The external system called in the workflow, e.g.:

- `customer-service`
- `promo-service`
- `product-service`
- `market-service`

---

##### Prometheus
Captured using custom metrics or via WebClient instrumentation:

```
http_client_requests_seconds_bucket{clientName="promo-service"}
```

You can also add:
```
downstream="customer-service"
```

---

##### Grafana
Recommended dashboards:
- Latency per downstream
- Error ratio per downstream
- Comparison of RestTemplate vs RestClient vs WebClient for each dependency

Query variable:

```
label_values(http_client_requests_seconds_count, clientName)
```

---

##### Jaeger
Span attributes:

```
peer.service = "promo-service"
net.peer.name = "customer-service"
```

Provides:
- Full dependency map
- Latency per downstream hop
- Comparing blocking vs non-blocking downstream behavior

---

##### OpenTelemetry Collector
Dependency metadata is preserved:

```
attributes:
  - key: peer.service
```

Can be used for:
- routing traces per downstream
- exporting specific services to specialized backends

---

#### Summary

This document provides a production-ready breakdown of the **four primary dimensions** used in latency and performance analysis:

- **URI**
- **HTTP Method**
- **Status Code**
- **Downstream Service**

Each dimension is mapped clearly to:
- Prometheus labels
- Grafana dashboards
- Jaeger tracing fields
- OTel Collector attributes

This enables deep observability and expert-level SRE diagnostics.


---

Read more below on dimensions mentioned above on "Resource Saturation Dashboard"

#### 🔵 **Resource Saturation Dashboard — Human-Friendly Deep Explanation**

Saturation is one of the **Four Golden Signals of SRE** (Latency, Traffic, Errors, Saturation).  
This dashboard helps teams understand **how close the service is to overload**, which directly affects latency, failures, and error budgets.

This section explains each metric in simple, practical language — suitable for developers, senior engineers, SREs, and technical managers.

---

##### Dimension 1. Tomcat Thread Pool Saturation

Tomcat is the inbound web server (for all REST controller requests).  
It uses a **thread-per-request** model, which means:

- Each request occupies one thread.
- Too many requests → thread pool fills → requests queue → latency spikes → timeouts happen.

### **🔹 `tomcat_threads_busy`**

**What it measures:**  
The number of Tomcat worker threads currently serving requests.

**Why it matters:**  
High thread usage means your service is approaching overload.

**Interpreting the value:**

| Usage Level | Meaning |
|-------------|---------|
| **< 70%** | Healthy |
| **70–90%** | Warning — load increasing |
| **> 90%** | Critical — risk of timeouts & 5xx errors |

---

##### Dimension 2. Tomcat Connection Pool Saturation

### **🔹 `tomcat_connections_current`**

**What it measures:**  
The number of current open HTTP connections to Tomcat.

**Why it matters:**  
A rapid increase usually indicates:

- Traffic spikes
- Slow downstream dependencies
- Clients retrying aggressively
- Load-test or real production surge

**Signs of problems:**

- Increasing linearly → healthy scaling
- Sudden vertical spikes → bottleneck forming
- Flat at max → connection exhaustion

---

##### Dimension 3. Netty (WebClient) Event-Loop Saturation

The Spring WebClient uses **Reactor Netty** internally, which is **non-blocking** and uses a small number of event-loop threads.

### **🔹 `reactor_netty_request_pending`**

**What it measures:**  
Number of **in-flight WebClient requests waiting to be processed**.

**Why it matters:**  
This shows if your outbound HTTP traffic is overwhelming Netty’s event loops.

**How to interpret:**

| Value | Meaning |
|-------|---------|
| **Low & steady** | Healthy reactive system |
| **Gradually rising** | Downstream slow / backpressure building |
| **Spiking rapidly** | Netty overwhelmed → expect timeouts soon |
| **Stuck high** | Hard saturation → failure cascade risk |

---

##### Dimension 4. JVM Resource Saturation

Saturation inside the JVM tells you whether memory or GC slowdowns are limiting throughput.

### **🔹 Heap Usage (%)**

**What it measures:**  
How much of your application memory is currently being used.

**Why it matters:**  
High heap = more GC = more application pause time.

| Heap % | Meaning |
|--------|---------|
| **< 70%** | Optimal |
| **70–85%** | Heavy memory load |
| **> 85%** | Critical — risk of GC thrashing / OOM |

---

### **🔹 GC Rate / GC Pause**

**What it measures:**  
Frequency and duration of garbage collection pauses.

**Why it matters:**  
Frequent GC pauses directly increase:

- API latency
- tail latencies (p95/p99)
- thread blocking

If GC pause spikes → your latency spikes.

---

### **🔹 Allocation Rate**

**What it measures:**  
How fast your application is allocating new objects.

**Why it matters:**  
High allocation = more GC work = reduced throughput.

**Expected behavior:**

- Blocking clients (RestTemplate/RestClient): moderate allocations
- WebClient/Reactive: higher short-lived allocations

---

#### Saturation Dimensions Summary Overview Table

| Area | Metric | What It Indicates | Why It Matters |
|------|--------|-------------------|----------------|
| **Tomcat** | `tomcat_threads_busy` | Thread pool load | Predicts overload & timeouts |
| **Tomcat** | `tomcat_connections_current` | Incoming connection pressure | Finds spikes & retry storms |
| **Netty** | `reactor_netty_request_pending` | Event-loop & backpressure | Detects cascading failures early |
| **JVM** | Heap % | Memory pressure | Predicts GC bottlenecks |
| **JVM** | GC rate/pause | GC health | Impacts p95+ latency |
| **JVM** | Allocation rate | Object churn | Silent performance killer |

---

This dashboard provides a **complete saturation picture**, allowing teams to catch:

- Overload conditions
- Thread starvation
- Connection exhaustion
- GC thrashing
- Backpressure buildup

*all before they become outages.*

#### Error Budget Burn, Concurrency Scaling, and Downstream Dependency Dashboards

##### Human-Friendly Deep Explanations for Developers, SREs, and Engineering Leaders

This document expands three critical observability dashboards used to evaluate system reliability, scalability, and dependency behavior in production microservices.

---

Read more on the [Error Budget Burn Dashboard](#error-budget-burn-dashboard) below.

#### 3. Error Budget Burn Dashboard — Deep Explanation

The **error budget** represents how much failure is acceptable before an SLO violation occurs.  
This dashboard shows **how quickly your service is consuming its allowed failures**.

It uses two burn windows:

---

###### 🔹 Fast Burn Rate (5-minute window)

**What it measures:**  
Short-term spike in error rate.

**Why it matters:**  
Detects **acute failures** such as:

- sudden downstream outage
- connection pool exhaustion
- thread pool starvation
- cascading failures

**Interpretation:**

| Fast Burn Rate | Meaning |
|----------------|---------|
| **< 1.0** | Within error budget |
| **1.0 – 2.0** | Approaching SLO breach |
| **> 2.0** | Immediate alert — budget burning too quickly |

---

##### 🔹 Slow Burn Rate (1-hour window)

**What it measures:**  
Sustained error patterns over a longer period.

**Why it matters:**  
Catches **chronic reliability issues**, including:

- slow degradation
- intermittent downstream instability
- insufficient resources
- retry storms or networking issues

**Interpretation:**

| Slow Burn Rate | Meaning |
|----------------|---------|
| **< 0.5** | Healthy |
| **0.5 – 1.0** | At-risk |
| **> 1.0** | SLO violation if trend continues |

---

##### 🔹 Error Budget %

This shows how much of your monthly/weekly budget has been consumed.

- **0–25%** → Safe
- **25–75%** → Monitor
- **> 75%** → Reliability risk
- **100%** → SLO violated; freeze deployments recommended

---

##### 🔹 Alerts on Burnrate Thresholds
Alerts are triggered when:

- `burnrate_fast > 2.0`
- `burnrate_slow > 1.0`
- sustained 4xx/5xx exceed error budget

**Why it matters:**  
This ensures you detect failures *before customers feel them.*

---

Read more on the [Concurrency Scaling Dashboard](#concurrency-scaling-dashboard) below.

#### 4. Concurrency Scaling Dashboard — Deep Explanation

This dashboard helps engineering teams understand how the service behaves as **traffic and pod counts scale**, especially under high concurrency.

---

##### 🔹 CPU Usage Per Pod

**What it measures:**  
Compute pressure across pods.

**Why it matters:**  
Shows whether increased load is causing CPU throttling or hotspots.

**Interpretation:**

| CPU Level | Meaning |
|-----------|---------|
| **< 60%** | Healthy |
| **60–80%** | Near scaling threshold |
| **> 80%** | Scaling required / throttling risk |

---

##### 🔹 Memory Usage Per Pod

**What it measures:**  
How efficiently the JVM uses available memory.

High memory usage → GC pressure → latency increase.

---

##### 🔹 Thread Usage

Includes:

- `jvm_threads_live_threads`
- `tomcat_threads_busy`
- Netty event-loop thread utilization (indirect via pending requests)

**Why it matters:**

| Signal | Problem |
|--------|---------|
| High Tomcat threads | Blocking call bottlenecks |
| High Netty pending requests | Backpressure building |
| High peak threads | Thread leaks or misconfiguration |

---

##### 🔹 Backpressure Events (WebClient)

Observed through:

- `reactor_netty_request_pending`
- timeouts
- increased retries

**Why it matters:**  
Shows when downstream systems cannot absorb the traffic your service is generating.

---

##### 🔹 Connection Pool Behavior

Metrics include:

- `http_client_pool_connections_active`
- `tomcat_connections_current`
- `reactor_netty_connection_provider_active_connections`

**Symptoms of trouble:**

| Behavior | Meaning |
|----------|---------|
| Sustained high active connections | Downstream slowness |
| Idle stays zero | Exhausted pool |
| Spikes in pending requests | Backpressure or overload |

---

##### 5. Downstream Dependency Health — Deep Explanation

Your Forex Qualification Engine calls:

- Customer Service
- Promo Service
- Product Books Service
- Market Rates Service

This dashboard answers:  
**“Are we slow, or is our dependency slow?”**

---

##### 🔹 Promo / Customer / Product / Market Latency

Each dependency has its own latency bucket (p50/p95/p99).  
This identifies which external call contributes most to your end-to-end latency.

Example interpretations:

| Observation | Root Cause |
|-------------|------------|
| High p99 Promo latency | Promo API inconsistent / overloaded |
| All dependencies slow | Network or DNS issue |
| Only Market Rates API slow | Specific downstream bottleneck |

---

##### 🔹 Downstream Error Rates

Uses metrics like:

- `http_client_requests_seconds_count{status!~"2.."}`
- retry counts
- timeout counts

Helps distinguish:

| Pattern | Meaning |
|---------|---------|
| High 5xx | Downstream service failing |
| High 4xx | Upstream request issue (bad input) |
| High timeouts | Saturated dependency or network issues |
| Spikes in retries | Retry storm → cascading failure risk |

---

##### 🔹 Cascading Failures

These occur when one dependency becomes slow or unavailable, causing:

- thread pool starvation
- connection pool exhaustion
- increased fast-burn rate
- latency explosion across all endpoints

This section visualizes early indicators:

| Signal | Cascade Indicator |
|--------|-------------------|
| Rising `tomcat_threads_busy` | Waiting on slow downstream |
| High Netty pending requests | Reactive backpressure |
| Error rate spikes | Downstream failure spreading |
| GC pauses increase | Memory pressure from blocked threads |

---

##### Summary Table

| Dashboard | Key Metrics | What It Explains | Who Uses It |
|-----------|-------------|------------------|--------------|
| **Error Budget Burn** | burnrate_fast, burnrate_slow, error % | Reliability & SLO health | SRE, Platform |
| **Concurrency Scaling** | CPU, memory, threads, backpressure | Scaling efficiency | Backend, SRE |
| **Downstream Health** | dependency latency & error rates | If failures originate upstream or downstream | All engineering teams |

---

This documentation converts complex reliability concepts into clear, actionable insights that any engineer or technical leader can understand and apply.

# 3. Recording Rules

Stored in:

```
DevOps/Local/compose/oltp-stack/Prometheus/recording_rules.yml
```

Includes:
- SLO burn rates
- Latency histograms
- Client-type comparisons
- JVM + GC metrics
- Business KPIs
- Saturation metrics

---

# 4. Expert Java Developers — Deep Technical Insights

For senior/principal Java engineers understanding concurrency, threading, and JVM mechanics.

---

## Threading Models

### **RestTemplate & RestClient (Blocking)**
- Thread-per-request model (Tomcat)
- Predictable and synchronous
- Higher thread usage under load
- Connection pool saturation is bottleneck

### **WebClient (Non-Blocking)**
- Event-loop system (Netty)
- Thousands of concurrent requests
- Requires:
    - Backpressure
    - Timeouts
    - Bounded concurrency
    - Scheduler control

---

## JVM & Concurrency Details

### Allocation & GC
- Reactive chains → many short-lived allocations
- Blocking IO → longer-lived objects + higher GC pressure

### CPU Behavior
- RestTemplate / RestClient → stable CPU
- WebClient → higher throughput but more context switching

### Thread Pools
- Blocking → Apache HttpClient/JDK HttpClient pools
- Reactive → event-loop workers + reactor connection pool

### Backpressure
- Must explicitly manage with WebClient
- Avoid unbounded concurrency

### Error Propagation
- Blocking → standard exceptions
- Reactive → error flows inside operator chain

---

# 5. Achieving Non‑Blocking Behavior While Using Tomcat

Even though the application uses **Tomcat as the main HTTP server**, WebClient remains entirely **non-blocking for outbound calls** via Reactor Netty.

## Breakdown

| Layer | Technology | Blocking? |
|-------|-----------|-----------|
| Inbound server | Tomcat | Yes |
| Outbound WebClient | Reactor Netty | No |
| DB operations (R2DBC) | Reactive | No |
| Business logic | App code | Mixed |

This is the **realistic production architecture** used by **Fortune 100 companies**.

---

# 6. Tomcat vs Netty Distinction

| Topic | Tomcat | Netty |
|-------|--------|--------|
| Role in this project | Inbound HTTP Server | Outbound HTTP Client (WebClient) |
| Model | Thread-per-request | Event-loop |
| Blocking? | Yes | No |
| Best for | Traditional REST APIs | High concurrency downstream calls |
| Used for request handling? | Yes | No |
| Used for WebClient? | No | Yes |

### Can you run both?

Yes — your project **already does**:

- `spring-boot-starter-web` → Tomcat server
- `spring-boot-starter-webflux` → Reactor Netty for WebClient

### Should you switch Tomcat → Netty for everything?

Only if:
- You want *full reactive end-to-end*
- You remove the servlet stack
- You rewrite controllers to return `Mono<>`

99% of enterprises keep **Tomcat + WebClient hybrid**.

---

# 7. 🔥 Extended KPIs, SLIs, SLOs (15 each)

## 7.1 KPIs (Table)

| KPI | Description | Prometheus Metric | Grafana Panel | Jaeger | OTel Collector |
|-----|-------------|------------------|---------------|--------|----------------|
| Qualification latency | End‑to‑end latency | http_server_requests_seconds | Latency graph | Trace timing | Export via OTLP |
| Customer API latency | Downstream latency | http_client_duration_seconds | Downstream panel | Span child | OTLP metrics |
| Promo API latency | Same | Same | Same | Same | Same |
| Product API latency | Same | Same | Same | Same | Same |
| Market API latency | Same | Same | Same | Same | Same |
| Workflow latency | Total pipeline | custom fxqual_workflow_seconds | Workflow dashboard | Composite span | Custom OTLP |
| RPS | Throughput | http_server_requests_seconds_count | RPS panel | n/a | Metrics pipeline |
| Success rate | 2xx requests | http_server_requests_seconds_count | Success gauge | n/a | OTLP |
| Error rate | 4xx/5xx | http_server_requests_seconds_count | Error % | Error spans | OTLP |
| Heap usage | JVM memory | jvm_memory_used_bytes | JVM dashboard | n/a | JVM exporter |
| GC pauses | GC time | jvm_gc_pause_seconds | GC heatmap | n/a | JMX → OTLP |
| Tomcat threads | Busy threads | tomcat_threads_busy | Thread panel | n/a | Micrometer |
| Netty connection usage | Pending ops | reactor_netty_request_pending | Netty panel | n/a | Micrometer |
| CPU per pod | Pod CPU | container_cpu_usage_seconds_total | K8s panel | n/a | K8s receiver |
| P99 stability | Latency variance | Histogram quantiles | Percentile panel | Trace analytics | OTLP |

---

## 7.2 SLIs (Table)

| SLI | Definition | Prometheus | Grafana | Jaeger | OTel Collector |
|-----|------------|------------|---------|--------|----------------|
| Availability | Success / total | success / total | Uptime panel | n/a | Metrics |
| p90 latency | 90th percentile | histogram_quantile | Percentile | Span timing | OTLP |
| p95 latency | 95th percentile | Same | Same | Same | Same |
| p99 latency | 99th percentile | Same | Same | Same | Same |
| Request error ratio | errors/req | error counts | Error ratio panel | Error spans | OTLP |
| Downstream error ratio | same but client | client_error_total | Downstream errors | Spans | OTLP |
| Workflow success ratio | success workflows | custom metric | Workflow panel | Trace success | OTLP |
| DB latency | DB op duration | db_query_seconds | DB panel | DB spans | OTLP |
| Thread saturation | busy/max threads | tomcat_threads_busy | Thread heatmap | n/a | Micrometer |
| Connection saturation | conn used/max | http_client_pool_* | Pool panel | n/a | OTLP |
| Backpressure | reactor backpressure events | reactor_* | Reactive panel | n/a | Micrometer |
| Timeouts | timeout occurrences | http_client_timeout_total | Timeout graph | Timeout span | OTLP |
| Retries | retry count | resilience4j_retry_calls | Retry panel | Span tags | OTLP |
| GC frequency | GC events/min | jvm_gc_pause_seconds_count | GC panel | n/a | JVM exporter |
| Pod restarts | restart count | kube_pod_container_status_restarts | K8s panel | n/a | K8s receiver |

---

## 7.3 SLOs (Table)

| SLO | Target | Prometheus Rule | Grafana | Jaeger | OTel Collector |
|-----|--------|------------------|---------|--------|----------------|
| Availability | 99.9% | error_budget_burn | SLO panel | n/a | OTLP |
| p95 < 150ms | Latency SLO | quantile alert | Latency SLO | Traces | OTLP |
| p99 < 350ms | High percentile | Same | Same | Same | Same |
| Error < 0.5% | Error SLO | error_rate > .005 | Error SLO panel | Error spans | OTLP |
| Downstream errors < 1% | Per client | downstream_error > .01 | Downstream SLO | Spans | OTLP |
| CPU < 70% | Pod CPU | cpu_usage > 70 | CPU panel | n/a | K8s receiver |
| Memory < 80% | Pod memory | mem_usage > 80 | Memory panel | n/a | K8s |
| GC pauses < 200ms | GC pause limit | gc_pause > .2 | GC panel | n/a | JVM |
| Tomcat busy < 85% | Thread SLO | threads_busy > .85 | Thread panel | n/a | Micrometer |
| Netty pending < 50 | Pending ops | netty_pending > 50 | Netty panel | n/a | Micrometer |
| Timeout rate < 1/min | Timeout SLO | timeout > 1 | Timeout panel | Timeout spans | OTLP |
| Retry rate < 5/min | Reliability | retry > 5 | Retry graph | Span tags | OTLP |
| DB latency < 20ms | DB SLO | db_latency >20 | DB panel | DB spans | OTLP |
| Workflow success > 99% | End-to-end | workflow_success < 99 | SLO panel | Trace root | OTLP |
| Pod restarts < 1/day | Stability | restart > 1 | K8s panel | n/a | K8s |

---

# 8. Running the Engine

Start WireMock:

```
docker compose -f DevOps/Local/compose/wiremock/docker-compose.yml up
```

Run the app:

```
./mvnw spring-boot:run
```

Call API:

```
curl -X POST "http://localhost:8080/api/fx/qualify?clientType=web_client"   -H "Content-Type: application/json"   -d '{ "customerId": "123", "pair": "EURUSD", "promoCode": "WELCOME" }'
```

---

# Summary

This repository is a **complete production blueprint** for:

- Comparing blocking vs reactive HTTP clients
- Understanding JVM, GC, threads, & connection pools
- Running SRE-grade observability with Prometheus/Grafana/Otel
- Demonstrating Fortune‑100 hybrid Tomcat + WebClient patterns
- Delivering high‑concurrency workflow orchestration  
