# Order Management Rest API Projesi

Bu proje, **Spring Boot** kullanılarak geliştirilmiş, ürün, sepet ve sipariş yönetimi yapan basit bir e-ticaret API'sidir. PostgreSQL veritabanı kullanılarak CRUD işlemleri gerçekleştirilmiştir.

## 🚀 Özellikler

- **Ürün Yönetimi:** Ürün ekleme, listeleme, güncelleme ve silme.
- **Müşteri Yönetimi:** Müşteri ekleme, listeleme, güncelleme ve silme.
- **Sepet Yönetimi:** Sepet oluşturma, ürün ekleme, çıkarma ve sepeti boşaltma.
- **Sipariş Yönetimi:** Sepetteki ürünlerle sipariş oluşturma, siparişleri listeleme ve silme.
- **Swagger UI:** API'yi kolayca test edebilmek için Swagger UI arayüzü.

---

## 🛠️ Teknolojiler

- Java 17
- Spring Boot
- PostgreSQL
- Lombok
- Swagger UI

---

## ⚙️ Kurulum

1. **Projeyi Klonlayın:**
   ```bash
   git clone https://github.com/gkhancobanoglu/order-management-rest-api
   cd order-managemment-rest-api
   ```
2. **Veritabanı Ayarlarınızı Yapın:**
  ```bash
  spring.datasource.url=jdbc:postgresql://localhost:5432/productdb
  spring.datasource.username=postgres
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=update
  ```
3. **Projeyi Başlatın:**

  ```bash
  mvn clean install
  mvn spring-boot:run
  ```

4. **Swagger UI üzerinden API'yi test edin:**
  ```bash
  http://localhost:8080/swagger-ui/index.html
  ```

## 🔥 API Endpointleri

### 🔄 Ürün İşlemleri
- **POST** `/api/products` - Ürün ekler.
- **GET** `/api/products` - Tüm ürünleri listeler.
- **GET** `/api/products/{id}` - Belirli bir ürünü getirir.
- **PUT** `/api/products/{id}` - Ürünü günceller.
- **DELETE** `/api/products/{id}` - Ürünü siler.

### 👥 Müşteri İşlemleri
- **POST** `/api/customers` - Müşteri ekler.
- **GET** `/api/customers` - Tüm müşterileri listeler.
- **GET** `/api/customers/{id}` - Belirli müşteriyi getirir.
- **PUT** `/api/customers/{id}` - Müşteriyi günceller.
- **DELETE** `/api/customers/{id}` - Müşteriyi siler.

### 🛒 Sepet İşlemleri
- **POST** `/api/carts` - Yeni bir sepet oluşturur.
- **POST** `/api/carts/{cartId}/add-product/{productId}?quantity=1` - Sepete ürün ekler.
- **DELETE** `/api/carts/{cartId}/remove-product/{productId}` - Sepetten ürün çıkarır.
- **DELETE** `/api/carts/{id}/empty` - Sepeti boşaltır.
- **GET** `/api/carts` - Tüm sepetleri listeler.
- **GET** `/api/carts/{id}` - Belirli sepeti getirir.
- **PUT** `/api/carts/{cartId}/update` - Sepetteki ürünlerin miktarını günceller.
- **POST** `/api/carts/{cartId}/checkout` - Sepetteki ürünlerle sipariş oluşturur.

### 📦 Sipariş İşlemleri
- **POST** `/api/orders/cart/{cartId}` - Sepetteki ürünlerle sipariş oluşturur.
- **GET** `/api/orders/{id}` - Siparişi getirir.
- **GET** `/api/orders/customer/{customerId}` - Müşterinin tüm siparişlerini getirir.
- **GET** `/api/orders/code/{orderCode}` - Siparişi kod ile getirir.
- **DELETE** `/api/orders/{id}` - Siparişi siler.



