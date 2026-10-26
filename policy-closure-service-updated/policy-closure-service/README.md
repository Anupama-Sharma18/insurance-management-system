# Policy Closure Microservice

Port: `8083`  
Database: `policy_closure_db`

Owns the cancellation/closure request workflow. It calls Policy Insurance at `policy.service.url` (default `http://localhost:8088`) to validate policy details and only requests policy cancellation after approval.

Workflow:
1. `POST /api/v1/cancellations/policy/{policyId}` — creates a `PENDING` request and unique cancellation request number. It does not cancel the policy.
2. `GET /api/v1/cancellations` or `GET /api/v1/cancellations/{id}` — track request.
3. `PUT /api/v1/cancellations/{id}/approve` with `{"approvedBy":1,"remarks":"Approved after review"}`.
4. Or `PUT /api/v1/cancellations/{id}/reject` with `{"approvedBy":1,"remarks":"Reason for rejection"}`.

Known integration gap: the provided BRD describes checking active claims but does not specify a Claims Service API path/contract. `TestClaimsEligibilityService` currently returns no active claims for local testing. Replace it with a real Claims Service client before production. Refund is `0.00` placeholder because refund calculation rules/payment API were not included in the supplied files. Approver ID is request-supplied for testing and must be taken from authenticated admin identity in production.
