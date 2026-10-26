# Policy Servicing Microservice

Port: `8081`  
Database: `policy_servicing_db`

Contains service requests, request history, nominee details, and endorsements. Renewal and cancellation classes have been removed to avoid duplicate ownership.

Policy lookup now calls the Policy Insurance service at `policy.service.url` (default `http://localhost:8088`).

Key APIs:
- `POST/GET /api/v1/service-requests`
- `GET /api/v1/service-request-history`
- `POST /api/v1/nominees`
- `POST /api/v1/endorsements`

The policy service must be running and have the requested policy before policy-dependent requests can succeed.
