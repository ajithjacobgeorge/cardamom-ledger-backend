# Cardamom Ledger Backend (`cardamom-ledger-backend`)

A production-ready enterprise Spring Boot application for managing plantation parcels, cultivation expenses, harvest batches, bhatti curing yields, and auction sales realization for cardamom estates.

---

## 🏗️ Architecture & Technology Stack

- **Java**: 21 (Temurin LTS / OpenJDK 17+)
- **Framework**: Spring Boot 3.3.5
- **ORM & Data**: Spring Data JPA, Hibernate ORM 6.5
- **Validation**: Jakarta Bean Validation (`@Valid`, `@NotNull`, `@DecimalMin`, `@Digits`, etc.)
- **Database Migrations**: Flyway 10.x (Automatic PostgreSQL / H2 migrations)
- **Databases Supported**:
  - **Dev/Test**: In-Memory H2 in PostgreSQL compatibility mode
  - **Production**: PostgreSQL 12+ (Connection pool: HikariCP)
- **API Documentation**: SpringDoc OpenAPI 2.6 (Swagger UI v3)
- **Pattern**: Clean Layered Architecture (`Controller` ➔ `Service` ➔ `Repository` ➔ `Entity` + `DTOs` + `Global Exception Handling`)

---

## 📂 Project Structure

```
cardamom-ledger-backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/cardamom/ledger/
│   │   │   ├── CardamomLedgerApplication.java
│   │   │   ├── config/
│   │   │   │   ├── JpaAuditConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AnalyticsController.java
│   │   │   │   ├── BlockController.java
│   │   │   │   ├── ExpenseController.java
│   │   │   │   ├── HarvestBatchController.java
│   │   │   │   └── SaleTransactionController.java
│   │   │   ├── domain/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── BaseEntity.java
│   │   │   │   │   ├── BlockEntity.java
│   │   │   │   │   ├── ExpenseEntity.java
│   │   │   │   │   ├── HarvestBatchEntity.java
│   │   │   │   │   └── SaleTransactionEntity.java
│   │   │   │   └── enums/
│   │   │   │       ├── CardamomVariety.java      (MALABAR, VAZHUKKA, MYSORE)
│   │   │   │       ├── ExpenseCategory.java      (LABOR, FERTILIZER, PEST_CONTROL, IRRIGATION, DRYING_FUEL, TRANSPORT, MISC)
│   │   │   │       └── HarvestStatus.java        (RAW, DRYING, STORED, SOLD)
│   │   │   ├── dto/
│   │   │   │   ├── request/                      (CreateBlockRequest, CreateExpenseRequest, etc.)
│   │   │   │   └── response/                     (BlockResponse, EstateSummaryResponse, etc.)
│   │   │   ├── exception/
│   │   │   │   ├── ApiErrorResponse.java
│   │   │   │   ├── BusinessValidationException.java
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   ├── BlockRepository.java
│   │   │   │   ├── ExpenseRepository.java
│   │   │   │   ├── HarvestBatchRepository.java
│   │   │   │   └── SaleTransactionRepository.java
│   │   │   └── service/
│   │   │       ├── BlockService.java & impl/
│   │   │       ├── EstateAnalyticsService.java & impl/
│   │   │       ├── ExpenseService.java & impl/
│   │   │       ├── HarvestBatchService.java & impl/
│   │   │       └── SaleTransactionService.java & impl/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   │           ├── V1__init_schema.sql
│   │           └── V2__seed_sample_data.sql
│   └── test/
│       └── java/com/cardamom/ledger/
│           ├── CardamomLedgerApplicationTests.java
│           ├── EstateOperationsIntegrationTest.java
│           └── controller/
│               ├── BlockControllerTest.java
│               ├── ExpenseControllerTest.java
│               ├── HarvestBatchControllerTest.java
│               └── SaleTransactionControllerTest.java
```

---

## 🚀 Running the Application

### 1. Build and Run Tests
```bash
./mvnw clean test
```

### 2. Run Locally (Dev profile with H2 + Pre-seeded Estate Data)
```bash
./mvnw spring-boot:run
```
- App Server: `http://localhost:8080`
- Swagger UI Documentation: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON Spec: `http://localhost:8080/v3/api-docs`
- H2 In-Memory Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:cardamom_db`, user: `sa`, password: empty)

### 3. Run in Production (PostgreSQL Profile)
Set your environment variables and start with `prod` profile:
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=postgres-cluster.internal
export DB_PORT=5432
export DB_NAME=cardamom_db
export DB_USERNAME=cardamom_admin
export DB_PASSWORD=SecretPassword123

java -jar target/cardamom-ledger-backend-1.0.0-SNAPSHOT.jar
```

---

## 📊 Domain Business Rules & Calculations

1. **Cardamom Varieties**:
   - `MALABAR`: Suited for mid-elevations (600–1000m), prostrate panicles.
   - `VAZHUKKA`: High-yielding natural hybrid, semi-erect panicles.
   - `MYSORE`: High-altitude and cold-resistant (>1000m), erect panicles.
2. **Curing Outturn Ratio**:
   - Outturn % = `(dry_weight_kg / green_weight_kg) * 100`
   - Normal dried cardamom recovery is between 18% and 24%.
   - Validation rejects any record where dry weight exceeds green weight.
3. **Auction Inventory Guards**:
   - Sales cannot be executed if cured dry weight is not yet registered.
   - You cannot sell more kilograms than the remaining unsold balance of a batch.
   - Automatically marks batch status as `SOLD` once stock is depleted, or reverts back to `STORED` if sales are voided.
4. **Commission Accounting**:
   - `Net Realization = (total_quantity_sold_kg * price_per_kg) - commission_deduction`
   - Real-time estate KPI rollup calculates net operational profit: `Net Realization - Total Estate Expenses`.
