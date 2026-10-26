# Policy Renewal Microservice

Port: `8082`  
Database: `policy_renewal_db`

Owns renewal records and the renewal workflow. It calls Policy Insurance (`policy.service.url`, default `http://localhost:8088`) to fetch policy details and update the expiry date after payment verification.

Workflow:
1. `POST /api/v1/renewals`
2. `POST /api/v1/renewals/{id}/reminder`
3. `POST /api/v1/renewals/{id}/confirm`
4. `POST /api/v1/renewals/{id}/verify-payment` with `{"paymentSuccessful":true}`

Rules retained from the supplied source include active-policy validation, expiry-date validation, reminder before customer confirmation, and customer confirmation before successful payment verification.

The payment endpoint currently accepts a Boolean supplied by the caller; connect the actual Payment service before production.
