
# FX Qual Engine — Workflow & Step Metrics (Prometheus)

This document explains the workflow-level and step-level observability added to the
**Forex Qualification Engine**, enabling SREs, engineers, and managers to understand:

- How long the full FX qualification workflow takes
- How much time is spent calling customer, promo, product, and market-data services
- Which downstream dependency slows down the pipeline
- How often workflow components are invoked

Metrics follow Micrometer + Prometheus conventions and appear automatically under:

`/actuator/prometheus`

---

# 1. Workflow-Level Metrics

The **entire FX qualification process** (all downstream API calls) is measured using:

### **`fxqual_workflow_total_ms`**

| Label | Description |
|-------|-------------|
| `clientImpl` | Which HTTP client strategy was used (`rest_template`, `rest_client`, `web_client`) |

### Example Prometheus Output

```
fxqual_workflow_total_ms_count{clientImpl="rest_client"} 18
fxqual_workflow_total_ms_sum{clientImpl="rest_client"} 1420.50
fxqual_workflow_total_ms_max{clientImpl="rest_client"} 180.13
```

---

# 2. Step-Level Metrics

Each downstream call is measured separately using:

### **`fxqual_step_duration_ms`**

| Step | Label |
|------|--------|
| Customer validation | `customer` |
| Promo validation | `promo` |
| Product lookup | `product` |
| FX Market interest | `fxInterest` |

---

# 3. Method Invocation Metrics

### **`fxqual_method_invocations_total`**

Tracks how frequently workflow components execute.

---

# 4. Developer-Friendly Example

After one quote run:

Workflow:
```
fxqual_workflow_total_ms_sum{clientImpl="rest_client"} 152
```

Steps:
```
fxqual_step_duration_ms_sum{step="customer"} 41
fxqual_step_duration_ms_sum{step="promo"} 22
fxqual_step_duration_ms_sum{step="product"} 56
fxqual_step_duration_ms_sum{step="fxInterest"} 34
```

---

# 5. Viewing Metrics in Prometheus

Visit:
```
http://localhost:8080/actuator/prometheus
```

Search for:
- `fxqual_workflow_total_ms`
- `fxqual_step_duration_ms`
- `fxqual_method_invocations_total`

---

# 6. Summary Table

| Metric | Purpose | Level |
|--------|----------|--------|
| `fxqual_workflow_total_ms` | End‑to‑end workflow duration | Workflow |
| `fxqual_step_duration_ms` | Downstream service timing | Step |
| `fxqual_method_invocations_total` | Method execution frequency | Method |

---

