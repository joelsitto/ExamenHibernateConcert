# 📚 GUÍA DEFINITIVA PARA APROBAR EL EXAMEN - HIBERNATE + SPRING BOOT

## 🎯 ÍNDICE DE CONTENIDOS

1. [Conceptos fundamentales que debes dominar](#1-conceptos-fundamentales)
2. [Estructura de proyecto y configuración](#2-estructura-y-configuracion)
3. [Anotaciones JPA/Hibernate esenciales](#3-anotaciones-jpa-hibernate)
4. [Relaciones entre entidades](#4-relaciones-entre-entidades)
5. [Patrón DAO con Hibernate](#5-patron-dao-hibernate)
6. [Spring Boot y Spring Data JPA](#6-spring-boot-spring-data-jpa)
7. [API REST con Controllers](#7-api-rest-controllers)
8. [Casos prácticos resueltos](#8-casos-practicos-resueltos)
9. [Errores comunes y cómo evitarlos](#9-errores-comunes)
10. [Checklist final del examen](#10-checklist-final)

---

## 1. CONCEPTOS FUNDAMENTALES

### 1.1 ¿Qué es ORM (Object-Relational Mapping)?

**ORM** es un puente entre el mundo de objetos (Java) y el mundo relacional (Base de datos).

```
Objeto Java (Artista) ←→ ORM (Hibernate) ←→ Tabla MySQL (artista)
```

**¿Por qué es importante?**
- ❌ Sin ORM: Escribes SQL manualmente, conviertes ResultSets a objetos
- ✅ Con ORM: Trabajas solo con objetos Java, Hibernate genera el SQL

### 1.2 Hibernate vs JPA vs Jakarta Persistence

```
Jakarta Persistence API (Especificación, interfaces)
           ↓ implementa
      Hibernate (Implementación real)
```

- **Jakarta Persistence** (antes JPA): Conjunto de **interfaces y anotaciones estándar**
- **Hibernate**: Implementación concreta que hace el trabajo real
- **Ventaja**: Si cambias de Hibernate a EclipseLink, tu código sigue funcionando

### 1.3 ¿Qué es Spring Boot?

Spring Boot es un **framework que simplifica la configuración** de aplicaciones Java.

**Sin Spring Boot:**
```java
// Tienes que hacer manualmente:
Configuration config = new Configuration().configure("hibernate.cfg.xml");
SessionFactory factory = config.buildSessionFactory();
Session session = factory.openSession();
// ... mucho código de configuración
```

**Con Spring Boot:**
```java
// Solo defines en application.properties y Spring hace todo automáticamente
@Autowired
private ArtistaRepository repository;
```

---

## 2. ESTRUCTURA Y CONFIGURACIÓN

### 2.1 Estructura del proyecto (APRÉNDELA DE MEMORIA)

```
src/main/java/
  ├── com.example/
  │   ├── Aplicacio.java                    ← Clase principal Spring Boot
  │   ├── model/                            ← Entidades (tablas)
  │   │   ├── Artista.java
  │   │   ├── Canco.java
  │   │   ├── Escenari.java
  │   │   ├── Instrument.java
  │   │   ├── Genere.java (enum)
  │   │   └── TipusEscenari.java (enum)
  │   ├── repositories/                     ← Interfaces Spring Data JPA
  │   │   ├── ArtistaRepository.java
  │   │   └── CancoRepository.java
  │   ├── services/                         ← Lógica de negocio (opcional)
  │   └── controllers/                      ← API REST (endpoints)
  │       ├── ArtistaController.java
  │       └── CancoController.java
  └── hibernateDAO/                         ← Patrón DAO tradicional
      ├── IGenericDAO.java
      ├── GenericDAO.java
      ├── ArtistaDAO.java
      └── CancoDAO.java
src/main/resources/
  ├── application.properties                ← Configuración Spring Boot
  └── hibernate.cfg.xml                     ← Configuración Hibernate tradicional
```

### 2.2 Archivos de configuración CLAVE

#### A) `pom.xml` - Dependencias Maven

**MEMORIZA estas dependencias esenciales:**

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.3</version>
</parent>

<dependencies>
    <!-- MySQL Driver -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.3.0</version>
    </dependency>
    
    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.5.2.Final</version>
    </dependency>
    
    <!-- Jakarta Persistence API -->
    <dependency>
        <groupId>jakarta.persistence</groupId>
        <artifactId>jakarta.persistence-api</artifactId>
        <version>3.1.0</version>
    </dependency>
    
    <!-- Spring Boot Web (para API REST) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

**¿Por qué cada una?**
- `mysql-connector-j`: Driver para conectar con MySQL
- `hibernate-core`: Motor ORM que mapea objetos a tablas
- `jakarta.persistence-api`: Anotaciones estándar (@Entity, @Id, etc.)
- `spring-boot-starter-web`: Para crear endpoints REST
- `spring-boot-starter-data-jpa`: Repositories automáticos

#### B) `hibernate.cfg.xml` - Configuración Hibernate tradicional

```xml
<hibernate-configuration>
    <session-factory>
        <!-- CONEXIÓN A LA BASE DE DATOS -->
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/concert_hibernate</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">super3</property>

        <!-- CONFIGURACIONES IMPORTANTES -->
        <property name="hibernate.hbm2ddl.auto">update</property>
        <!-- update: actualiza la BD / create: la recrea desde cero -->
        
        <property name="hibernate.current_session_context_class">
            org.hibernate.context.internal.ThreadLocalSessionContext
        </property>
        
        <!-- DESACTIVAR LOGS (opcional) -->
        <property name="hibernate.show_sql">false</property>
        <property name="hibernate.format_sql">false</property>
        
        <!-- MAPEAR ENTIDADES -->
        <mapping class="com.example.model.Artista" />
        <mapping class="com.example.model.Canco" />
        <mapping class="com.example.model.Escenari" />
        <mapping class="com.example.model.Instrument" />
    </session-factory>
</hibernate-configuration>
```

**IMPORTANTE:** Cada clase con `@Entity` debe estar mapeada con `<mapping class="..."/>`

#### C) `application.properties` - Configuración Spring Boot

```properties
# CONEXIÓN A LA BASE DE DATOS
spring.datasource.url=jdbc:mysql://localhost:3306/concert_hibernate
spring.datasource.username=root
spring.datasource.password=super3

# CONFIGURACIÓN HIBERNATE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# PUERTO DEL SERVIDOR
server.port=8080
```

**Diferencias clave:**
- Con Hibernate tradicional: Usas `hibernate.cfg.xml`
- Con Spring Boot: Usas `application.properties` (más simple)

#### D) Clase principal Spring Boot

```java
@SpringBootApplication  // ← ANOTACIÓN MÁGICA (combina 3 en 1)
public class Aplicacio {
    public static void main(String[] args) {
        SpringApplication.run(Aplicacio.class, args);
    }
}
```

**¿Qué hace `@SpringBootApplication`?**
1. `@Configuration`: Marca como clase de configuración
2. `@EnableAutoConfiguration`: Spring configura todo automáticamente
3. `@ComponentScan`: Busca todas las clases con `@Component`, `@Service`, `@Repository`, `@Controller`

---

## 3. ANOTACIONES JPA/HIBERNATE

### 3.1 Anotaciones de clase

#### `@Entity`
Marca una clase como entidad persistente (tabla en la BD).

```java
@Entity  // ← Esta clase será una tabla
@Table(name = "artista")  // ← Nombre de la tabla (opcional)
public class Artista {
    // ...
}
```

**REGLA:** Toda clase con `@Entity` DEBE tener:
- Un constructor sin parámetros
- Una clave primaria con `@Id`

### 3.2 Anotaciones de atributos

#### `@Id` y `@GeneratedValue`
Define la clave primaria y cómo se genera.

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "artista_id")
private Long artistaId;
```

**Estrategias de generación:**
- `IDENTITY`: La BD auto-incrementa (recomendado para MySQL)
- `AUTO`: Hibernate decide automáticamente
- `SEQUENCE`: Usa secuencias (PostgreSQL, Oracle)
- `TABLE`: Usa una tabla auxiliar

#### `@Column`
Configura la columna en la BD.

```java
@Column(name = "nom", length = 80, nullable = false, unique = true)
private String nom;
```

**Atributos importantes:**
- `name`: Nombre de la columna
- `length`: Longitud máxima (para VARCHAR)
- `nullable`: Si permite NULL (false = NOT NULL)
- `unique`: Si debe ser única

#### `@Enumerated`
Para atributos de tipo enum.

```java
@Enumerated(EnumType.STRING)  // ← SIEMPRE usa STRING, no ORDINAL
@Column(nullable = false)
private Genere genere;
```

**¿Por qué `STRING` y no `ORDINAL`?**
- `STRING`: Guarda "ROCK", "POP" (legible, seguro)
- `ORDINAL`: Guarda 0, 1, 2 (si cambias el orden del enum, ¡desastre!)

#### `@ElementCollection`
Para listas de valores simples (no entidades).

```java
@ElementCollection
@Enumerated(EnumType.STRING)
@Column(name = "generes")
private List<Genere> generes;
```

**Crea una tabla separada:** `artista_generes` con columnas `artista_id` y `generes`.

---

## 4. RELACIONES ENTRE ENTIDADES

### 4.1 Conceptos fundamentales de relaciones

#### A) Lado propietario (Owning Side) vs Lado inverso

**REGLA DE ORO:**
- **Lado propietario**: Tiene `@JoinColumn` (contiene la clave foránea en la BD)
- **Lado inverso**: Tiene `mappedBy` (solo referencia)

```java
// LADO INVERSO (no tiene la FK en la BD)
@OneToMany(mappedBy = "artista")
private List<Canco> cancons;

// LADO PROPIETARIO (tiene la FK "artista_id" en la BD)
@ManyToOne
@JoinColumn(name = "artista_id")
private Artista artista;
```

#### B) Cascade (Propagación de operaciones)

```java
@OneToMany(cascade = CascadeType.ALL)
private List<Canco> cancons;
```

**Tipos de Cascade:**
- `ALL`: Propaga todo (guardar, actualizar, eliminar)
- `PERSIST`: Solo al guardar
- `MERGE`: Solo al actualizar
- `REMOVE`: Solo al eliminar
- `REFRESH`, `DETACH`: Menos usados

**Ejemplo práctico:**
```java
Artista artista = new Artista();
Canco canco = new Canco();
artista.getCancons().add(canco);
session.persist(artista);  // Con cascade, también guarda canco automáticamente
```

#### C) Fetch (Estrategia de carga)

```java
@OneToMany(fetch = FetchType.LAZY)   // Por defecto en @OneToMany
@ManyToMany(fetch = FetchType.EAGER) // Carga inmediata
```

- **LAZY**: Carga los datos solo cuando se accede a ellos (más eficiente)
- **EAGER**: Carga los datos inmediatamente (puede causar problemas de rendimiento)

### 4.2 Relación @OneToOne (1:1)

**Ejemplo del proyecto:** `Artista ←→ Instrument` (instrumento principal)

```java
// En Artista (lado propietario)
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "instrument_principal_id")
@JsonBackReference
private Instrument instrumentPrincipal;

// En Instrument (lado inverso)
@OneToOne(mappedBy = "instrumentPrincipal")
@JsonManagedReference
private Artista artistaPrincipal;
```

**Cómo crear la relación:**
```java
Artista artista = new Artista("Rosalia", 500.0, generes);
Instrument guitarra = new Instrument("Guitarra", 8.5);

artista.setInstrumentPrincipal(guitarra);
guitarra.setArtistaPrincipal(artista);

session.persist(artista); // Con cascade, también guarda guitarra
```

**Tabla en la BD:**
```
tabla artista:
+------------+--------+---------------------------+
| artista_id | nom    | instrument_principal_id   |
+------------+--------+---------------------------+
| 1          | Rosalia| 1                         |
+------------+--------+---------------------------+
```

### 4.3 Relación @ManyToOne / @OneToMany (N:1 / 1:N)

**Ejemplo del proyecto:** `Artista (1) ←→ (N) Canco`

```java
// En Artista (lado ONE - inverso)
@OneToMany(mappedBy = "artista")
@JsonManagedReference("artista-cancons")
private List<Canco> cancons = new ArrayList<>();

// En Canco (lado MANY - propietario)
@ManyToOne
@JoinColumn(name = "artista_id")
@JsonBackReference("artista-cancons")
private Artista artista;
```

**¿Quién es el propietario?**
- Siempre el lado **MANY** (Canco)
- La tabla `canco` tiene la columna `artista_id` (FK)

**Cómo crear la relación:**
```java
Artista artista = new Artista("Rosalia", 500.0, generes);
Canco canco1 = new Canco("Malamente", Genere.POP, 4.5);
Canco canco2 = new Canco("Pienso en tu mirá", Genere.POP, 3.8);

// Establecer desde el lado MANY
canco1.setArtista(artista);
canco2.setArtista(artista);

// Y desde el lado ONE (para mantener sincronización)
artista.getCancons().add(canco1);
artista.getCancons().add(canco2);

session.persist(artista); // Con cascade, también guarda las canciones
```

**Tablas en la BD:**
```
tabla artista:
+------------+----------+
| artista_id | nom      |
+------------+----------+
| 1          | Rosalia  |
+------------+----------+

tabla canco:
+----------+----------------------+------------+
| canco_id | titol                | artista_id |
+----------+----------------------+------------+
| 1        | Malamente            | 1          |
| 2        | Pienso en tu mirá    | 1          |
+----------+----------------------+------------+
```

### 4.4 Relación @ManyToMany (N:M)

**Ejemplo del proyecto:** `Canco (N) ←→ (M) Instrument`

```java
// En Canco (lado propietario)
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "canco_instrument",                    // Tabla intermedia
    joinColumns = @JoinColumn(name = "canco_id"), // FK hacia Canco
    inverseJoinColumns = @JoinColumn(name = "instrument_id") // FK hacia Instrument
)
@JsonManagedReference("canco-instruments")
private List<Instrument> instruments = new ArrayList<>();

// En Instrument (lado inverso)
@ManyToMany(mappedBy = "instruments")
@JsonBackReference("canco-instruments")
private List<Canco> cancons;
```

**¿Qué crea en la BD?**
Una **tabla intermedia** `canco_instrument`:

```
tabla canco_instrument:
+----------+---------------+
| canco_id | instrument_id |
+----------+---------------+
| 1        | 1             |  ← Canco 1 usa Instrumento 1
| 1        | 2             |  ← Canco 1 usa Instrumento 2
| 2        | 1             |  ← Canco 2 usa Instrumento 1
+----------+---------------+
```

**Cómo crear la relación:**
```java
Canco canco = new Canco("Malamente", Genere.POP, 4.5);
Instrument guitarra = new Instrument("Guitarra", 8.5);
Instrument bateria = new Instrument("Batería", 7.0);

// Establecer desde el lado propietario
canco.getInstruments().add(guitarra);
canco.getInstruments().add(bateria);

// También desde el otro lado (sincronización)
guitarra.getCancons().add(canco);
bateria.getCancons().add(canco);

session.persist(canco);
session.persist(guitarra);
session.persist(bateria);
```

### 4.5 Anotaciones Jackson para JSON (EVITAR REFERENCIAS CIRCULARES)

**Problema:** Si tienes `Artista → Cancons` y `Canco → Artista`, al convertir a JSON:
```
Artista → Canco → Artista → Canco → Artista ... (BUCLE INFINITO)
```

**Solución:** Usar anotaciones Jackson

```java
// En el lado ONE
@JsonManagedReference("artista-cancons")  // ← "Yo gestiono"
private List<Canco> cancons;

// En el lado MANY
@JsonBackReference("artista-cancons")     // ← "Yo no me serializo"
private Artista artista;
```

**Regla simple:**
- `@JsonManagedReference`: Lado que SE MUESTRA en el JSON
- `@JsonBackReference`: Lado que NO se muestra (se ignora)

---

## 5. PATRÓN DAO HIBERNATE

### 5.1 ¿Qué es el patrón DAO?

**DAO = Data Access Object**

Separa la lógica de negocio de la lógica de acceso a datos.

```
Main/Controller → Service → DAO → Hibernate → Base de Datos
```

**Ventajas:**
- ✅ Código más limpio y mantenible
- ✅ Puedes cambiar la BD sin tocar la lógica de negocio
- ✅ Fácil de testear (mock del DAO)

### 5.2 Estructura del patrón DAO

#### A) Interfaz genérica `IGenericDAO<T>`

Define las operaciones CRUD básicas.

```java
public interface IGenericDAO<T> {
    void guardar(SessionFactory sessionFactory, T entity);
    void actualitzar(SessionFactory sessionFactory, T entity);
    void eliminar(SessionFactory sessionFactory, Long id);
    T obtenirPerId(SessionFactory sessionFactory, Long id);
    List<T> obtenirTots(SessionFactory sessionFactory);
}
```

**¿Por qué genérico?** Para reutilizar el código con cualquier entidad.

#### B) Implementación genérica `GenericDAO<T>`

```java
public class GenericDAO<T> implements IGenericDAO<T> {
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void guardar(SessionFactory sessionFactory, T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);  // ← INSERT
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void actualitzar(SessionFactory sessionFactory, T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);  // ← UPDATE
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void eliminar(SessionFactory sessionFactory, Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);  // ← DELETE
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public T obtenirPerId(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(entityClass, id);  // ← SELECT por ID
        }
    }

    @Override
    public List<T> obtenirTots(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM " + entityClass.getName(), 
                entityClass
            ).list();  // ← SELECT *
        }
    }
}
```

**Conceptos clave:**
- `session.persist()`: Para nuevos objetos (INSERT)
- `session.merge()`: Para objetos existentes (UPDATE)
- `session.remove()`: Para eliminar (DELETE)
- `session.get()`: Para obtener por ID (SELECT)
- `try-with-resources`: Cierra automáticamente la sesión

#### C) DAO específico que extiende el genérico

```java
public class ArtistaDAO extends GenericDAO<Artista> {
    
    public ArtistaDAO() {
        super(Artista.class);  // ← Le dice qué clase gestiona
    }
    
    // Métodos específicos adicionales
    public void assignarArtistaAMaxEscenaris(Artista artista, SessionFactory factory) {
        // Lógica específica para artistas
        if (artista.getConcertsDisponibles() == 0) {
            return;
        }
        
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        
        List<Escenari> escenarisDisponibles = session
            .createQuery("from Escenari where artista is null", Escenari.class)
            .getResultList();
        
        session.getTransaction().commit();
        
        // Asignar escenarios disponibles
        for (Escenari escenari : escenarisDisponibles) {
            if (artista.getConcertsDisponibles() > 0) {
                escenari.setArtista(artista);
                artista.getEscenaris().add(escenari);
                artista.setConcertsDisponibles(artista.getConcertsDisponibles() - 1);
            } else {
                break;
            }
        }
        
        actualitzar(factory, artista);
    }
}
```

### 5.3 Uso del DAO en MainHibernate

```java
public class MainHibernate {
    public static void main(String[] args) {
        // 1. Crear SessionFactory
        SessionFactory factory = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();

        try {
            // 2. Crear DAO
            ArtistaDAO artistaDAO = new ArtistaDAO();
            CancoDAO cancoDAO = new CancoDAO();

            // 3. Crear y guardar entidades
            Artista artista = new Artista("Rosalia", 500.0, generes);
            artistaDAO.guardar(factory, artista);

            // 4. Usar métodos específicos
            artistaDAO.assignarArtistaAMaxEscenaris(artista, factory);

        } finally {
            factory.close();
        }
    }
}
```

### 5.4 Diferencia entre persist() y merge()

```java
// persist() - Para objetos NUEVOS
Artista nuevo = new Artista("Rosalia", 500.0, generes);
session.persist(nuevo);  // INSERT

// merge() - Para objetos EXISTENTES
Artista existente = session.get(Artista.class, 1L);
existente.setCache(600.0);
session.merge(existente);  // UPDATE
```

---

## 6. SPRING BOOT SPRING DATA JPA

### 6.1 ¿Qué es Spring Data JPA?

Una **capa de abstracción sobre JPA** que elimina el código repetitivo.

**Sin Spring Data JPA (DAO tradicional):**
```java
public List<Artista> obtenirTots(SessionFactory sf) {
    Session session = sf.openSession();
    return session.createQuery("FROM Artista", Artista.class).list();
}
```

**Con Spring Data JPA:**
```java
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    // ¡Ya tienes findAll() automáticamente!
}
```

### 6.2 Crear un Repository

```java
@Repository  // ← Spring detecta esta interfaz
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    //                                         ↑         ↑
    //                              Tipo de entidad   Tipo de ID
    
    // Métodos automáticos que ya tienes:
    // - save(artista)
    // - findById(id)
    // - findAll()
    // - deleteById(id)
    // - count()
    // - existsById(id)
    
    // Métodos personalizados (Spring los implementa solo con el nombre)
    Artista findByNom(String nom);
    List<Artista> findByCacheGreaterThan(double cache);
}
```

**MAGIA DE SPRING DATA JPA:**
Spring genera la implementación automáticamente analizando el nombre del método.

```java
findByNom → SELECT * FROM artista WHERE nom = ?
findByCacheGreaterThan → SELECT * FROM artista WHERE cache > ?
findByNomAndCache → SELECT * FROM artista WHERE nom = ? AND cache = ?
```

### 6.3 Métodos query automáticos

| Nombre del método | SQL equivalente |
|-------------------|-----------------|
| `findByNom(String nom)` | `WHERE nom = ?` |
| `findByNomLike(String pattern)` | `WHERE nom LIKE ?` |
| `findByNomContaining(String part)` | `WHERE nom LIKE %?%` |
| `findByCacheGreaterThan(double c)` | `WHERE cache > ?` |
| `findByCacheLessThan(double c)` | `WHERE cache < ?` |
| `findByCacheBetween(double min, double max)` | `WHERE cache BETWEEN ? AND ?` |
| `findByNomOrderByCacheDesc(String nom)` | `WHERE nom = ? ORDER BY cache DESC` |

### 6.4 Queries personalizadas con @Query

```java
@Repository
public interface CancoRepository extends JpaRepository<Canco, Long> {
    
    List<Canco> findByGenere(Genere genere);
    
    // Query JPQL personalizada
    @Query("SELECT c FROM Canco c WHERE c.durada > :duradaMin")
    List<Canco> trobarCanconsLlargues(@Param("duradaMin") double duradaMin);
    
    // Query SQL nativa
    @Query(value = "SELECT * FROM canco WHERE disponible = true", nativeQuery = true)
    List<Canco> trobarCanconsDisponibles();
}
```

### 6.5 Inyección de dependencias con @Autowired

```java
@RestController
@RequestMapping("/artistes")
public class ArtistaController {
    
    @Autowired  // ← Spring inyecta automáticamente el repository
    private ArtistaRepository artistaRepository;
    
    // No necesitas crear el objeto manualmente
    // Spring lo hace por ti
}
```

**¿Qué hace `@Autowired`?**
1. Spring detecta que necesitas un `ArtistaRepository`
2. Busca una implementación (la crea automáticamente)
3. La inyecta en tu clase

---

## 7. API REST CONTROLLERS

### 7.1 ¿Qué es una API REST?

Una forma **estandarizada** de comunicación entre aplicaciones usando HTTP.

```
Cliente (Postman, navegador, app móvil)
    ↓ HTTP Request (GET, POST, PUT, DELETE)
Servidor (Spring Boot Controller)
    ↓ Procesa la petición
    ↓ Accede a la BD via Repository
    ↑ HTTP Response (JSON)
Cliente recibe los datos
```

### 7.2 Anotaciones de Controller

```java
@RestController  // ← Marca como controlador REST
@RequestMapping("/artistes")  // ← URL base: http://localhost:8080/artistes
public class ArtistaController {
    
    @Autowired
    private ArtistaRepository artistaRepository;
    
    // GET http://localhost:8080/artistes/getArtistaCancons?nom=Rosalia
    @GetMapping("/getArtistaCancons")
    public Artista obtenirArtistaCancons(@RequestParam("nom") String nom) {
        return artistaRepository.findByNom(nom);
    }
}
```

**Anotaciones HTTP:**
- `@GetMapping`: Para leer datos (no modifica nada)
- `@PostMapping`: Para crear nuevos recursos
- `@PutMapping`: Para actualizar recursos
- `@DeleteMapping`: Para eliminar recursos

### 7.3 Ejemplo completo de Controller

```java
@RestController
@RequestMapping("/cancons")
public class CancoController {

    @Autowired
    private CancoRepository cancoRepo;

    @Autowired
    private ArtistaRepository artistaRepo;

    // GET http://localhost:8080/cancons/POP
    @GetMapping("/{genere}")
    public List<Canco> getCanconsPerGenere(@PathVariable Genere genere) {
        return cancoRepo.findByGenere(genere);
    }

    // POST http://localhost:8080/cancons/nomesCanconsPopulars
    @PostMapping("/nomesCanconsPopulars")
    public Map<String, Object> eliminarCanconsNoPopulars() {
        List<Canco> cancons = cancoRepo.findAll();
        int eliminades = 0;
        List<Artista> artistesAfectats = new ArrayList<>();

        for (Canco canco : cancons) {
            double sumaPopularitat = 0;

            if (canco.getInstruments() != null && !canco.getInstruments().isEmpty()) {
                for (Instrument ins : canco.getInstruments()) {
                    sumaPopularitat += ins.getPopularitat();
                }
                sumaPopularitat /= canco.getInstruments().size();
            }

            if (sumaPopularitat < 3.0) {
                canco.setDisponible(false);
                cancoRepo.save(canco);

                Artista artista = canco.getArtista();
                if (artista != null) {
                    artista.setCache(artista.getCache() - 10.0);
                    artistaRepo.save(artista);
                    artistesAfectats.add(artista);
                }
                eliminades++;
            }
        }

        // Respuesta JSON
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("cançonsEliminades", eliminades);
        resposta.put("artistesAfectats", artistesAfectats.size());
        resposta.put("missatge", "Setlist actualitzada correctament");

        return resposta;
    }
}
```

### 7.4 Diferencia entre @RequestParam y @PathVariable

```java
// @RequestParam - Parámetros en la URL después de ?
// GET http://localhost:8080/artistes/buscar?nom=Rosalia&cache=500
@GetMapping("/buscar")
public Artista buscar(@RequestParam String nom, @RequestParam double cache) {
    // ...
}

// @PathVariable - Parte de la ruta URL
// GET http://localhost:8080/artistes/5
@GetMapping("/{id}")
public Artista obtenir(@PathVariable Long id) {
    return artistaRepository.findById(id).orElse(null);
}
```

### 7.5 Respuestas JSON

Spring Boot convierte automáticamente objetos Java a JSON.

```java
@GetMapping("/{nom}")
public Artista getArtista(@PathVariable String nom) {
    return artistaRepository.findByNom(nom);
}

// Respuesta HTTP:
// {
//   "artistaId": 1,
//   "nom": "Rosalia",
//   "cache": 500.0,
//   "generes": ["POP", "ELECTRONIC"],
//   "cancons": [
//     { "cancoId": 1, "titol": "Malamente", "genere": "POP" }
//   ]
// }
```

---

## 8. CASOS PRÁCTICOS RESUELTOS

### 8.1 Crear y guardar entidades relacionadas

```java
// 1. Crear entidades simples primero
Escenari esc1 = new Escenari("Main Stage", TipusEscenari.PRINCIPAL);
Instrument guitarra = new Instrument("Guitarra", 8.5);

Session session = factory.openSession();
session.beginTransaction();
session.persist(esc1);
session.persist(guitarra);
session.getTransaction().commit();

// 2. Crear entidades relacionadas
ArtistaDAO artistaDAO = new ArtistaDAO();
CancoDAO cancoDAO = new CancoDAO();

Canco canco = new Canco("Por una patata", Genere.POP, 4.5);
cancoDAO.guardar(factory, canco);

List<Genere> generes = Arrays.asList(Genere.POP, Genere.ELECTRONIC);
Artista artista = new Artista("Rosalia", 500.0, generes);
artista.setConcertsDisponibles(10);

// 3. Establecer relaciones
artista.getCancons().add(canco);
canco.setArtista(artista);

artistaDAO.guardar(factory, artista);
cancoDAO.actualitzar(factory, canco);
```

### 8.2 Consultas HQL personalizadas

```java
// Buscar escenarios sin artista asignado
Session session = factory.getCurrentSession();
session.beginTransaction();

List<Escenari> escenarisDisponibles = session
    .createQuery("from Escenari where artista is null", Escenari.class)
    .getResultList();

session.getTransaction().commit();
```

### 8.3 Agregar elementos a relación ManyToMany

```java
public void afegirInstrumentPerGenere(SessionFactory factory, 
                                      Genere genere, 
                                      Instrument instrument) {
    factory.getCurrentSession().beginTransaction();
    Session session = factory.getCurrentSession();

    // Buscar todas las canciones del género
    List<Canco> cancons = session
        .createQuery("from Canco where genere = :genere", Canco.class)
        .setParameter("genere", genere)
        .getResultList();
    
    session.getTransaction().commit();

    // Agregar el instrumento a cada canción
    for (Canco canco : cancons) {
        canco.getInstruments().add(instrument);
        instrument.getCancons().add(canco);
        
        session.beginTransaction();
        session.merge(instrument);
        session.getTransaction().commit();
        
        // Actualizar duración
        canco.setDurada(canco.getDurada() + (instrument.getPopularitat() * 0.5));
        actualitzar(factory, canco);
    }
}
```

### 8.4 Lógica de negocio compleja

```java
public void distribucioEscenaris(SessionFactory factory) {
    Session session = factory.openSession();
    session.beginTransaction();

    List<Artista> artistes = session
        .createQuery("FROM Artista", Artista.class)
        .getResultList();
    List<Escenari> escenaris = session
        .createQuery("FROM Escenari", Escenari.class)
        .getResultList();

    if (artistes.isEmpty() || escenaris.isEmpty()) {
        return;
    }

    // Filtrar por tipo
    List<Escenari> principals = escenaris.stream()
        .filter(e -> e.getTipus().name().equals("PRINCIPAL"))
        .toList();
    List<Escenari> secundaris = escenaris.stream()
        .filter(e -> e.getTipus().name().equals("SECUNDARI"))
        .toList();
    List<Escenari> tematics = escenaris.stream()
        .filter(e -> e.getTipus().name().equals("TEMATIC"))
        .toList();

    // Calcular proporciones
    int totalEscenaris = escenaris.size();
    int totalArtistes = artistes.size();

    int artistesPrincipal = (int) Math.round(
        (double) principals.size() / totalEscenaris * totalArtistes
    );

    int indexArtista = 0;

    // Asignar artistas a escenarios principales
    for (Escenari e : principals) {
        if (indexArtista < artistesPrincipal && indexArtista < artistes.size()) {
            e.setArtista(artistes.get(indexArtista));
            session.merge(e);
            indexArtista++;
        }
    }

    session.getTransaction().commit();
}
```

---

## 9. ERRORES COMUNES

### ❌ Error 1: No inicializar colecciones

```java
// MALO
@OneToMany(mappedBy = "artista")
private List<Canco> cancons;  // NullPointerException cuando haces add()

// BUENO
@OneToMany(mappedBy = "artista")
private List<Canco> cancons = new ArrayList<>();
```

### ❌ Error 2: mappedBy incorrecto

```java
// MALO - mappedBy debe apuntar al nombre del campo, no la clase
@OneToMany(mappedBy = "Artista")

// BUENO
@OneToMany(mappedBy = "artista")  // nombre del campo en Canco
```

### ❌ Error 3: Olvidar el constructor sin parámetros

```java
// MALO - Solo tienes constructor con parámetros
public class Artista {
    public Artista(String nom, double cache) {
        // ...
    }
}

// BUENO - JPA necesita un constructor vacío
public class Artista {
    public Artista() {}  // ← Constructor vacío obligatorio
    
    public Artista(String nom, double cache) {
        // ...
    }
}
```

### ❌ Error 4: No mapear la entidad en hibernate.cfg.xml

```xml
<!-- MALO - Falta mapear la entidad -->
<hibernate-configuration>
    <session-factory>
        <!-- ... propiedades ... -->
        <!-- ¡Falta <mapping class="com.example.model.Artista" />! -->
    </session-factory>
</hibernate-configuration>

<!-- BUENO -->
<hibernate-configuration>
    <session-factory>
        <!-- ... propiedades ... -->
        <mapping class="com.example.model.Artista" />
        <mapping class="com.example.model.Canco" />
    </session-factory>
</hibernate-configuration>
```

### ❌ Error 5: Referencias circulares en toString()

```java
// MALO - Causa StackOverflowError
@Override
public String toString() {
    return "Artista [nom=" + nom + ", cancons=" + cancons + "]";
    // cancons.toString() llama a Canco.toString()
    // que llama a artista.toString() → BUCLE INFINITO
}

// BUENO
@Override
public String toString() {
    return "Artista [nom=" + nom + ", numCancons=" + cancons.size() + "]";
}
```

### ❌ Error 6: Usar @Enumerated(EnumType.ORDINAL)

```java
// MALO - Guarda 0, 1, 2 (si cambias el orden, ¡caos!)
@Enumerated(EnumType.ORDINAL)
private Genere genere;

// BUENO - Guarda "ROCK", "POP" (legible y seguro)
@Enumerated(EnumType.STRING)
private Genere genere;
```

### ❌ Error 7: No usar transacciones

```java
// MALO - Sin transacción
Session session = factory.openSession();
session.persist(artista);  // ¡No se guarda!

// BUENO
Session session = factory.openSession();
session.beginTransaction();
session.persist(artista);
session.getTransaction().commit();
```

### ❌ Error 8: No sincronizar relaciones bidireccionales

```java
// MALO - Solo estableces un lado
artista.getCancons().add(canco);
// Si luego accedes a canco.getArtista() → NULL

// BUENO - Sincronizar ambos lados
artista.getCancons().add(canco);
canco.setArtista(artista);
```

---

## 10. CHECKLIST FINAL EXAMEN

### ✅ Antes del examen

- [ ] Sé crear un proyecto Maven con Spring Boot
- [ ] Conozco la estructura de packages (`model`, `repository`, `controller`)
- [ ] Sé configurar `pom.xml` con todas las dependencias
- [ ] Entiendo `application.properties` y `hibernate.cfg.xml`
- [ ] Sé crear la clase principal con `@SpringBootApplication`

### ✅ Entidades (Model)

- [ ] Sé usar `@Entity` y `@Table`
- [ ] Conozco `@Id` y `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- [ ] Sé configurar `@Column(name, length, nullable, unique)`
- [ ] Uso `@Enumerated(EnumType.STRING)` correctamente
- [ ] Sé cuándo usar `@ElementCollection` para listas simples
- [ ] Creo siempre un constructor vacío
- [ ] Inicializo las colecciones (`new ArrayList<>()`)
- [ ] No incluyo relaciones en `toString()`
- [ ] Implemento `equals()` y `hashCode()` correctamente

### ✅ Relaciones

- [ ] Entiendo quién es el lado propietario (tiene `@JoinColumn`)
- [ ] Entiendo el lado inverso (tiene `mappedBy`)
- [ ] Sé hacer `@OneToOne` con `@JoinColumn` y `mappedBy`
- [ ] Sé hacer `@ManyToOne` / `@OneToMany`
- [ ] Sé hacer `@ManyToMany` con `@JoinTable`
- [ ] Conozco `cascade = CascadeType.ALL`
- [ ] Conozco `fetch = FetchType.LAZY` vs `EAGER`
- [ ] Uso `@JsonManagedReference` y `@JsonBackReference` para evitar ciclos

### ✅ Patrón DAO Hibernate

- [ ] Sé crear una interfaz `IGenericDAO<T>`
- [ ] Sé implementar `GenericDAO<T>`
- [ ] Conozco `session.persist()` para INSERT
- [ ] Conozco `session.merge()` para UPDATE
- [ ] Conozco `session.remove()` para DELETE
- [ ] Conozco `session.get()` para SELECT por ID
- [ ] Sé usar `session.createQuery()` para HQL
- [ ] Uso transacciones (`beginTransaction()`, `commit()`, `rollback()`)
- [ ] Cierro sesiones correctamente (`try-with-resources`)
- [ ] Creo DAOs específicos extendiendo `GenericDAO`

### ✅ Spring Data JPA

- [ ] Sé crear un Repository con `extends JpaRepository<Entity, Long>`
- [ ] Uso `@Repository` en las interfaces
- [ ] Conozco los métodos automáticos: `save()`, `findById()`, `findAll()`, `deleteById()`
- [ ] Sé crear métodos query por nombre: `findByNom()`, `findByNomLike()`
- [ ] Sé usar `@Query` para consultas JPQL personalizadas
- [ ] Entiendo `@Param` para parámetros nombrados

### ✅ API REST Controllers

- [ ] Uso `@RestController` en la clase
- [ ] Uso `@RequestMapping("/ruta")` para la URL base
- [ ] Conozco `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- [ ] Sé usar `@PathVariable` para rutas dinámicas (`/artistes/{id}`)
- [ ] Sé usar `@RequestParam` para parámetros query (`?nom=Rosalia`)
- [ ] Uso `@Autowired` para inyectar Repositories
- [ ] Sé devolver objetos Java (se convierten a JSON automáticamente)
- [ ] Sé devolver `Map<String, Object>` para respuestas personalizadas

### ✅ Hibernate tradicional (SessionFactory)

- [ ] Sé crear un `SessionFactory` con `Configuration().configure()`
- [ ] Entiendo `hibernate.cfg.xml` y sus propiedades
- [ ] Sé mapear entidades con `<mapping class="..."/>`
- [ ] Conozco `hibernate.hbm2ddl.auto` (create, update)
- [ ] Sé abrir y cerrar sesiones
- [ ] Uso `getCurrentSession()` vs `openSession()`

### ✅ Consultas y operaciones

- [ ] Sé hacer consultas HQL básicas: `"FROM Artista"`
- [ ] Sé filtrar con WHERE: `"FROM Artista WHERE nom = :nom"`
- [ ] Sé usar `setParameter()` para evitar SQL injection
- [ ] Sé navegar por relaciones: `"FROM Artista a JOIN FETCH a.cancons"`
- [ ] Sé hacer operaciones complejas (filtros, cálculos, actualizaciones masivas)

### ✅ Debugging y testing

- [ ] Sé ejecutar el proyecto con `Run As → Spring Boot App`
- [ ] Sé probar endpoints con Postman o navegador
- [ ] Activo `spring.jpa.show-sql=true` para ver SQL
- [ ] Leo los logs de la consola para detectar errores
- [ ] Compruebo la BD con MySQL Workbench o phpMyAdmin

---

## 📝 RESUMEN ULTRA-RÁPIDO (Para repasar 5 minutos antes del examen)

### Configuración básica

1. **pom.xml**: `mysql-connector-j`, `hibernate-core`, `jakarta.persistence-api`, `spring-boot-starter-web`, `spring-boot-starter-data-jpa`
2. **application.properties**: URL, user, password, `ddl-auto=update`, `show-sql=true`
3. **Clase principal**: `@SpringBootApplication` + `SpringApplication.run()`

### Entidades

```java
@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", length = 80, nullable = false)
    private String nom;
    
    @Enumerated(EnumType.STRING)
    private Genere genere;
    
    @OneToMany(mappedBy = "artista")
    @JsonManagedReference
    private List<Canco> cancons = new ArrayList<>();
    
    // Constructor vacío + getters/setters
}
```

### Relaciones

- **1:1**: Propietario tiene `@JoinColumn`, inverso tiene `mappedBy`
- **1:N**: Lado ONE tiene `mappedBy`, lado MANY tiene `@JoinColumn`
- **N:M**: Propietario tiene `@JoinTable`, inverso tiene `mappedBy`

### DAO Hibernate

```java
public class GenericDAO<T> {
    public void guardar(SessionFactory sf, T entity) {
        try (Session s = sf.openSession()) {
            s.beginTransaction();
            s.persist(entity);
            s.getTransaction().commit();
        }
    }
}

public class ArtistaDAO extends GenericDAO<Artista> {
    public ArtistaDAO() { super(Artista.class); }
}
```

### Repository Spring

```java
@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    Artista findByNom(String nom);
}
```

### Controller REST

```java
@RestController
@RequestMapping("/artistes")
public class ArtistaController {
    @Autowired
    private ArtistaRepository repo;
    
    @GetMapping("/{id}")
    public Artista get(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
}
```

---

## 🎓 CONSEJOS FINALES

1. **Practica escribiendo código a mano** (el examen puede ser en papel)
2. **Memoriza las anotaciones clave**: `@Entity`, `@Id`, `@ManyToOne`, `@RestController`
3. **Entiende el flujo**: Controller → Repository → BD
4. **No copies y pegues en el examen** - entiende qué hace cada línea
5. **Si te atascas**: Respira, lee el enunciado de nuevo, dibuja las relaciones en papel
6. **Repasa los ejemplos del proyecto** - son casos reales de examen

---

**¿Alguna duda específica sobre algún apartado?** 
¡Dímelo y te lo explico con más detalle!

**¡MUCHA SUERTE EN EL EXAMEN! 🚀**

