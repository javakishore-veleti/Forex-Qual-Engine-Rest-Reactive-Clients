
# Prometheus_FXQualEngine Postman Collection

This folder contains a Postman collection designed to test and validate the **FX Qualification Engine**
and its **Prometheus metrics instrumentation**, including workflow and step-level observability.

---

## 📌 1. Files

| File | Description |
|------|-------------|
| `Prometheus_FXQualEngine.postman_collection.json` | Import into Postman to run FX Qual API tests & verify metrics |
| `Prometheus_FXQualEngine_Postman_README.md` | This documentation |

Store these under:

```
source_repo/Testing/Postman/
```

---

## 📌 2. How to Use the Collection

### Step 1 — Import into Postman  
1. Open Postman  
2. Click **Import**  
3. Select `Prometheus_FXQualEngine.postman_collection.json`

---

## 📌 3. Requests Included

### 🔹 1. Qualify FX Quote (rest_client)
```
POST http://localhost:8080/api/fx/qualify?clientType=rest_client
```

This triggers **ALL workflow and step metrics**.

Metrics verified:

| Metric | Meaning |
|--------|--------|
| `fxqual_workflow_duration_ms` | End-to-end workflow duration |
| `fxqual_step_duration_ms{step="customer"}` | Customer service latency |
| `fxqual_step_duration_ms{step="promo"}` | Promo validation latency |
| `fxqual_step_duration_ms{step="product"}` | Product catalog latency |
| `fxqual_step_duration_ms{step="fxInterest"}` | Market-data latency |
| `fxqual_method_invocations_total` | Java method call counts |
| `fxqual_quotes_requested_total` | Quote request count |
| `fxqual_quotes_qualified_total` | Successful quote count |
| `fxqual_volume_notional` | Trade notional volume |

---

### 🔹 2. Qualify FX Quote (web_client)

Used to compare performance with `rest_client`.
Prometheus labels workflow metrics by:

```
clientType=web_client
```

---

### 🔹 3. Prometheus Metrics Endpoint
```
GET http://localhost:8080/actuator/prometheus
```

Search for:

```
fxqual_
http_server_requests_
jvm_
```

---

## 📌 4. Example PromQL Queries

### Workflow latency (avg over 5 min)
```
rate(fxqual_workflow_duration_ms_sum[5m])
/
rate(fxqual_workflow_duration_ms_count[5m])
```

### Step latency comparison
```
sum by (step) (rate(fxqual_step_duration_ms_sum[1m]))
```

### Requests per second
```
rate(http_server_requests_seconds_count[1m])
```

---

## 📌 5. Recommended Folder Structure

```
source_repo/
  Testing/
    Postman/
      Prometheus_FXQualEngine.postman_collection.json
      Prometheus_FXQualEngine_Postman_README.md
```

---

## 🎉 Done!

This Postman collection + README enables:

- Manual workflow validation  
- Performance testing  
- Prometheus metrics verification  
- API regression testing  
- Latency benchmarking for multiple clientType implementations

