# Spring Boot Hibernate @Formula POC

## Overview

This project demonstrates the use of the Hibernate `@Formula` annotation in a Spring Boot application.

The `@Formula` annotation allows developers to define **read-only, calculated entity properties** using SQL expressions without creating additional columns in the database.

This POC implements a simple **Product Management REST API** that automatically calculates the discounted price of products based on their original price and discount percentage.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- Maven
- RESTful APIs
- Relational Database
- Swagger / OpenAPI (optional)

## Key Features

- Create and retrieve product information.
- Calculate discounted product prices using Hibernate `@Formula`.
- Execute calculations directly at the database level.
- Avoid storing redundant calculated values in the database.
- Demonstrate the difference between persistent and calculated entity properties.

## Understanding Hibernate @Formula

`@Formula` is a Hibernate-specific annotation used to map an entity attribute to a SQL expression.

Unlike `@Column`, which maps an attribute to a physical database column, `@Formula` calculates its value when Hibernate retrieves the entity.

### Example

```java
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private double price;

    private int discountPercentage;

    @Formula("price - (price * discount_percentage / 100.0)")
    private double discountedPrice;
}
```

**Note:** The SQL expression uses database column names. The example assumes the discount percentage column is named `discount_percentage`.

## Formula Calculation

The discounted price is calculated using:

```text
Discounted Price = Price - (Price × Discount Percentage / 100)
```

For example:

```text
Product: Laptop
Original Price: 50000
Discount: 10%

Discounted Price = 50000 - (50000 × 10 / 100)
                 = 45000
```

The `discountedPrice` field does not require a physical database column.

## Database Structure

Example `product` table:

| Column | Data Type | Description |
|---|---|---|
| id | BIGINT | Primary key |
| name | VARCHAR | Product name |
| category | VARCHAR | Product category |
| price | DOUBLE | Original product price |
| discount_percentage | INTEGER | Discount percentage |

The calculated `discountedPrice` property is not stored in the table.

## REST API Examples

The following endpoints illustrate a typical Product Service implementation. Actual endpoint mappings depend on the controller configuration.

### Create Product

**POST** `/api/products`

Request body:

```json
{
  "name": "Laptop",
  "category": "Electronics",
  "price": 50000,
  "discountPercentage": 10
}
```

### Retrieve All Products

**GET** `/api/products`

Example response:

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "category": "Electronics",
    "price": 50000.0,
    "discountPercentage": 10,
    "discountedPrice": 45000.0
  },
  {
    "id": 2,
    "name": "Mobile",
    "category": "Electronics",
    "price": 20000.0,
    "discountPercentage": 15,
    "discountedPrice": 17000.0
  }
]
```

### Retrieve Product by ID

**GET** `/api/products/1`

Example response:

```json
{
  "id": 1,
  "name": "Laptop",
  "category": "Electronics",
  "price": 50000.0,
  "discountPercentage": 10,
  "discountedPrice": 45000.0
}
```

## Hibernate Generated SQL

When the application retrieves products using:

```java
productRepository.findAll();
```

Hibernate generates SQL conceptually similar to:

```sql
SELECT
    id,
    name,
    category,
    price,
    discount_percentage,
    price - (price * discount_percentage / 100.0)
        AS discounted_price
FROM product;
```

The database evaluates the formula and returns the calculated value to Hibernate.

## How to Run the Application

### Prerequisites

- Compatible Java JDK
- Maven
- Configured relational database
- IDE such as IntelliJ IDEA, Eclipse, or VS Code

### Clone the Repository

```bash
git clone <repository-url>
cd product-service
```

Replace `<repository-url>` with the actual Git repository URL.

### Configure the Database

Configure `src/main/resources/application.properties` according to your database.

Example using MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Create the database before starting the application:

```sql
CREATE DATABASE product_db;
```

Ensure that the required JDBC driver is included in `pom.xml`.

### Build the Project

```bash
mvn clean install
```

### Start the Application

```bash
mvn spring-boot:run
```

### Test the API

Use Postman, cURL, or Swagger UI (if configured) to test the REST endpoints.

## @Formula vs @Transient vs @Column

| Feature | @Formula | @Transient | @Column |
|---|---|---|---|
| Physical database column required | No | No | Yes |
| SQL expression supported | Yes | No | No |
| Calculated by database | Yes | No | No |
| Managed by Hibernate | Yes | No | Yes |
| Persistent write mapping | No | No | Yes |
| Standard JPA annotation | No | Yes | Yes |

## Advantages of @Formula

1. **No redundant storage:** Calculated values do not require separate database columns.
2. **Database-level computation:** Calculations execute as part of SQL queries.
3. **Simplified entity access:** Calculated values can be accessed like regular entity properties.
4. **Consistent calculation logic:** A single formula defines the calculation.
5. **SQL flexibility:** Supports arithmetic expressions, SQL functions, and suitable subqueries.

## Limitations and Considerations

- `@Formula` is Hibernate-specific and is not portable across all JPA providers.
- Formula values are read-only through Hibernate.
- Complex expressions or correlated subqueries may increase query execution cost.
- Formula expressions can depend on database-specific SQL syntax.
- Values are calculated when entities are loaded and are not automatically refreshed after underlying data changes.
- Newly persisted entities may require a refresh or reload before their formula values are available.
- For monetary calculations, `BigDecimal` is preferable to `double` to avoid floating-point precision issues.

## Learning Objectives

By completing this POC, developers will understand:

- How Hibernate `@Formula` works.
- How to define calculated entity attributes.
- How SQL expressions are integrated into Hibernate entity mappings.
- How calculated values appear in REST API responses.
- The difference between `@Formula`, `@Transient`, and `@Column`.
- When to use database-level calculations instead of Java-side calculations.

## Conclusion

This POC demonstrates how Hibernate `@Formula` simplifies calculated attribute mapping in Spring Boot applications.

Using `@Formula`, developers can retrieve dynamically calculated values without introducing redundant database columns or manually calculating the values after fetching entities.

It is particularly useful for read-only derived attributes such as discounted prices, totals, computed statuses, and other SQL-based calculations.

---

**Project Type:** Proof of Concept (POC)  
**Framework:** Spring Boot / Hibernate ORM  
**Concept:** Hibernate `@Formula` Annotation  
**Architecture:** REST API with Spring Data JPA
