# Smart Parking Lot Microservices System

An enterprise-grade, event-driven microservices architecture for managing a smart parking lot. This system handles vehicle check-ins, dynamic pricing strategies, secure API access, and asynchronous notifications.

## 🏗️ Architecture & Services

This project is built using the Twelve-Factor App methodology, separating concerns across distinct microservices.

* **`config-server` (Port 8888):** Centralized configuration management for all services.
* **`discovery-service` (Port 8761):** Netflix Eureka Server for service registration and discovery.
* **`gateway-service` (Port 8000):** The main API Gateway. Handles routing to backend services and implements Resilience4j Circuit Breakers for fault tolerance. Operates as an OAuth2 Resource Server validating JWTs.
* **`bff-service` (Port 8083):** Backend-For-Frontend. Acts as the security guardian for the React client. Handles the OAuth2 Authorization Code flow with Keycloak, manages the browser session, and uses `TokenRelay` to pass the JWT to downstream services.
* **`ticket-service` (Port 8081):** Core business logic. Manages vehicle check-ins/check-outs, calculates fees using the Strategy Pattern (e.g., Car vs. Motorcycle pricing), and acts as a Kafka Producer to broadcast parking events.
* **`notification-service`:** A Kafka Consumer that listens for check-in/check-out events and processes asynchronous alerts.
* **`frontend-react` (Port 5173):** The user interface built with React and Vite.
* **Infrastructure (Docker Compose):** Runs Keycloak (Port 8080) for Identity and Access Management (IAM), Kafka/Zookeeper for event streaming, and PostgreSQL/H2 for data persistence.

## 🛠️ Tech Stack
* **Backend:** Java, Spring Boot 3, Spring Cloud (Gateway, Config, Netflix Eureka), Spring Security (WebFlux), Spring Data JPA/MongoDB.
* **Frontend:** React, Vite, Axios.
* **Messaging:** Apache Kafka.
* **Security:** Keycloak (OAuth2 / OpenID Connect).
* **Resilience:** Resilience4j (Circuit Breaker, Retry, TimeLimiter).

---

## 🛑 The Local Development Hurdle: The "One-Port" 403 Issue

During development, the architecture was configured to use a "One-Port" routing mechanism where the React frontend and all API calls were routed through the `bff-service` and `gateway-service` on `localhost`. 

While this mimics a production-ready ingress controller, it causes severe issues in a local development environment:

1.  **The Preflight (`OPTIONS`) Trap:** Browsers enforce CORS by sending hidden `OPTIONS` requests before making API calls like POST or GET. In a Double-Gateway setup (BFF -> Gateway), Spring WebFlux often blocks these preflight requests with a `403 Forbidden` because the `OPTIONS` request lacks a valid JWT, killing the call before the actual data is sent.
2.  **Localhost Cookie Drops:** The BFF relies on a `SESSION` cookie to recognize the React app's login state. Modern browsers (Chrome/Edge) strictly enforce `SameSite` and `Secure` cookie policies. On `http://localhost`, the browser frequently drops or refuses to attach the session cookie across different port hops, leading to a silent authentication failure.
3.  **JWT Validation Timeouts:** When the Gateway (8000) tries to validate the token sent by the BFF (8083), it must reach out to Keycloak (8080). Local network latency or DNS resolution issues between these local ports can cause the signature validation to fail, defaulting to a 403 Access Denied.

The codebase is structurally sound for deployment, but fighting local browser security policies wastes development time.

---

## 🟢 The "Easy Way": Local Development Setup

To bypass the complex CORS and WebFlux filter chains during local development, we decouple the React frontend from the Spring Cloud Gateway routing rules. 

**Do not copy React build files into the Spring Boot `/static` or `/public` folders.** Instead, we use Vite's built-in development proxy. This allows Vite to handle the frontend on its own port, while tricking the browser into thinking it's communicating directly with the backend, bypassing CORS completely.

### Step 1: Configure Vite Proxy
In your `frontend-react` folder, update the `vite.config.js` file:

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // 1. Proxy auth requests directly to the BFF
      '/auth': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false,
      },
      '/login': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false,
      },
      '/oauth2': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        secure: false,
      },
      // 2. Proxy API calls directly to the API Gateway
      '/api/v1': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})

### Step 2: Simplify Axios
In your React code (`api.js`), remove hardcoded `localhost:8083` base URLs. Let Axios use relative paths so the Vite proxy can intercept them.

```javascript
import axios from 'axios';

const api = axios.create({
  // Leave baseURL empty or use '/' so Vite's proxy catches it
  baseURL: '/', 
  // Still required to send the Keycloak session cookie
  withCredentials: true 
});

export default api;

### Step 3: Standard Start Sequence
1.  **Infrastructure:** Run `docker-compose up -d` (Starts Keycloak, Kafka, and Postgres).
2.  **Core Services:** Start `config-server` -> `discovery-service`.
3.  **Application Services:** Start `gateway-service` -> `bff-service` -> `ticket-service`.
4.  **Frontend:** Navigate to the `frontend-react` folder in your terminal and run `npm run dev`.
5.  **Access:** Open your browser to `http://localhost:5173`.

When you trigger a login, Vite will transparently proxy your requests to the BFF, and CORS errors will no longer block your API requests.