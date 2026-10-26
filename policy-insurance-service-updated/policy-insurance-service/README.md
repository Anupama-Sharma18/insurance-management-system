# Policy Insurance / Policy Issuance Microservice

Port: `8088`  
Database: `policy_issuance_db`

This is the first uploaded project, kept as the Policy Insurance/Issuance microservice. Main APIs use `/api/v1/policies`.

Important integration APIs used by the other services:
- `GET /api/v1/policies/{id}` — fetch policy details.
- `PUT /api/v1/policies/{id}/renewal-expiry?newExpiryDate=YYYY-MM-DD` — update expiry after renewal payment verification.
- `DELETE /api/v1/policies/{id}` — cancel policy after the closure service approves a cancellation.

Run MySQL first and check `src/main/resources/application.properties`. The supplied configuration uses `root/root`.
