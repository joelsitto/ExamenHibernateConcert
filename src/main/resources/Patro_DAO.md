# Patró DAO amb Hibernate

## Descripció del Projecte

Aquest projecte implementa el patró de disseny DAO (Data Access Object) utilitzant Hibernate com a framework ORM per gestionar la persistència de dades d'estudiants en una base de dades relacional. El projecte serveix com a exemple pràctic per comprendre la separació entre la lògica de negoci i la capa d'accés a dades.

## Què és el Patró DAO?

El patró DAO és un patró de disseny estructural que proporciona una interfície abstracta per accedir a dades emmagatzemades en una base de dades o altre mecanisme de persistència. Aquest patró separa la lògica de negoci de la lògica d'accés a dades.

### Avantatges del Patró DAO

- **Separació de responsabilitats**: La lògica de negoci no conté codi SQL ni detalls de persistència
- **Mantenibilitat**: Els canvis en la base de dades només afecten la capa DAO
- **Testabilitat**: Es poden crear implementacions mock del DAO per fer tests unitaris
- **Reutilització**: El mateix DAO es pot utilitzar des de diferents parts de l'aplicació
- **Flexibilitat**: Es pot canviar la implementació de persistència sense afectar la resta del codi

### Components del Patró DAO

1. **Entitat**: La classe de domini que representa les dades (Estudiant)
2. **Interfície DAO**: Defineix les operacions disponibles (IEstudiantDAO)
3. **Implementació DAO**: Conté el codi real d'accés a dades (EstudiantDAO)
4. **Client**: Utilitza el DAO per gestionar les dades (MainDAO)

## Estructura del Projecte

### Diagrama de Components

```
MainDAO (Client)
    |
    | utilitza
    v
IEstudiantDAO (Interfície)
    ^
    | implementa
    |
EstudiantDAO (Implementació)
    |
    | persisteix
    v
Estudiant (Entitat) <---> Base de Dades
```

### Classes Principals

**Estudiant.java** - Entitat de Domini
- Representa l'objecte de negoci que volem persistir
- Utilitza anotacions JPA per mapear els atributs a columnes de base de dades
- `@Entity`: Indica que és una classe persistent
- `@Table`: Especifica el nom de la taula
- `@Id` i `@GeneratedValue`: Defineixen la clau primària i la seva generació automàtica
- `@Column`: Configura les propietats de cada columna (nullable, unique, length)
- Atributs: id, nom, cognoms, email, data de naixement i nota mitjana

**IEstudiantDAO.java** - Contracte d'Accés a Dades
- Defineix les operacions CRUD que es poden realitzar sobre estudiants
- Actua com a contracte que qualsevol implementació ha de complir
- Permet canviar la implementació sense afectar el codi client
- Mètodes declarats:
  - `guardar()`: Inserir un nou estudiant
  - `actualitzar()`: Modificar un estudiant existent
  - `eliminar()`: Esborrar un estudiant per ID
  - `obtenirPerId()`: Recuperar un estudiant concret
  - `obtenirTots()`: Llistar tots els estudiants
  - `cercarPerNom()`: Cercar estudiants per nom amb coincidència parcial

**EstudiantDAO.java** - Implementació amb Hibernate
- Conté la lògica real d'accés a la base de dades
- Utilitza Hibernate per abstraure el SQL
- Gestió de transaccions:
  - Inicia una transacció abans de cada operació que modifica dades
  - Fa commit si tot va bé
  - Fa rollback si hi ha errors, mantenint la consistència de les dades
- Utilitza try-with-resources per garantir el tancament de sessions
- Implementa consultes HQL (Hibernate Query Language) per cerques complexes

**MainDAO.java** - Classe Principal i Gestió de Sessions
- Demostra l'ús pràctic de totes les operacions del DAO
- Gestiona la configuració de Hibernate mitjançant `SessionFactory`
- Patró Singleton per la SessionFactory (s'instancia una sola vegada)
- Inclou exemples de:
  - Creació d'estudiants amb dades completes
  - Llistat complet d'estudiants
  - Cerca per ID
  - Actualització de dades
  - Cerca per nom amb filtre

## Bones Pràctiques Implementades

- **Patró de disseny DAO**: Separació clara entre capes
- **Programació contra interfícies**: El client utilitza IEstudiantDAO, no EstudiantDAO directament
- **Gestió de recursos**: Try-with-resources per tancar sessions automàticament
- **Gestió de transaccions**: Commit/rollback
- **Control d'errors**: Gestió d'excepcions en tots els mètodes crítics