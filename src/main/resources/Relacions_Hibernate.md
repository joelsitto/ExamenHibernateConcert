# Relacions en Hibernate

## Introducció

Aquest projecte demostra els diferents tipus de relacions que podem establir entre entitats en Hibernate utilitzant anotacions JPA. Entendre com funcionen aquestes relacions és fonamental per modelar correctament la nostra base de dades i treballar eficientment amb dades relacionades.

## Tipus de relacions

En bases de dades relacionals existeixen quatre tipus principals de relacions:

1. **One-to-One (1:1)**: Una entitat es relaciona amb una altra de forma única
2. **One-to-Many (1:N)**: Una entitat es relaciona amb múltiples instàncies d'una altra
3. **Many-to-One (N:1)**: L'inversa de One-to-Many
4. **Many-to-Many (N:M)**: Múltiples entitats es relacionen amb múltiples instàncies d'una altra

## Conceptes fonamentals

### Propietari de la relació (Owning Side)

En tota relació bidireccional, una entitat és la **propietària** i l'altra és la **inversa**:

- **Propietària**: Té l'anotació `@JoinColumn` i és qui físicament conté la clau forana a la base de dades
- **Inversa**: Té l'atribut `mappedBy` i fa referència al camp de l'altra entitat

**Regla important**: Només la part propietària pot gestionar la relació a nivell de base de dades.

### Cascade

L'atribut `cascade` determina quines operacions es propaguen de l'entitat pare als fills:

- `CascadeType.ALL`: Totes les operacions (persist, merge, remove, refresh, detach)
- `CascadeType.PERSIST`: Només quan guardem l'entitat
- `CascadeType.MERGE`: Només quan actualitzem
- `CascadeType.REMOVE`: Només quan esborrem

### Fetch Type

Determina com es carreguen les dades relacionades:

- **EAGER**: Carrega les dades relacionades immediatament
- **LAZY**: Carrega les dades només quan s'accedeixen (per defecte en `@OneToMany` i `@ManyToMany`)

## 1. Relació One-to-One (1:1)

Una instància de ClasseA només pot estar relacionada amb una instància de ClasseB i viceversa.

### Exemple: ClasseA ← → ClasseB

**ClasseA (Inversa)**:
```java
@OneToOne(mappedBy="classeA")
private ClasseB classeB;
```

**ClasseB (Propietària)**:
```java
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name="classeA_id")
private ClasseA classeA;
```

### Com funciona?

- `ClasseB` té una columna `classeA_id` que conté la clau forana cap a `ClasseA`
- `ClasseB` és la propietària perquè té `@JoinColumn`
- `ClasseA` utilitza `mappedBy="classeA"` per indicar que el camp `classeA` de `ClasseB` gestiona la relació

### Quan utilitzar-la?

- Usuari ← → Perfil
- Persona ← → DNI
- Empleat ← → Despatx

### Com crear la relació?

```java
ClasseA a = new ClasseA("Objecte A");
ClasseB b = new ClasseB("Objecte B");

// Establir la relació des de la part propietària
b.setClasseA(a);
a.setClasseB(b);

session.persist(b); // Amb cascade, també guarda a
```

## 2. Relació One-to-Many / Many-to-One (1:N / N:1)

Una instància de ClasseA pot estar relacionada amb múltiples instàncies de ClasseC, però cada ClasseC només pot estar relacionada amb una ClasseA.

### Exemple: ClasseA (1) ← → (N) ClasseC

**ClasseA (Inversa - One)**:
```java
@OneToMany(mappedBy="classeA", cascade=CascadeType.ALL)
private List<ClasseC> classeC;
```

**ClasseC (Propietària - Many)**:
```java
@ManyToOne
@JoinColumn(name="classeA_id")
private ClasseA classeA;	
```

### Com funciona?

- La taula de `ClasseC` té una columna `classeA_id` amb la clau forana
- La part **One** (`ClasseA`) sempre és la propietària i té `mappedBy`
- La part **Many** (`ClasseC`) és la inversa i té `@JoinColumn`

### Quan utilitzar-la?

- Categoria ← → Productes
- Client ← → Comandes
- Autor ← → Llibres
- Departament ← → Empleats

### Com crear la relació?

```java
ClasseA a = new ClasseA("Pare");
ClasseC c1 = new ClasseC("Fill 1");
ClasseC c2 = new ClasseC("Fill 2");

// Establir relació des de la part Many
c1.setClasseA(a);
c2.setClasseA(a);

// I des de la part One
List<ClasseC> fills = new ArrayList<>();
fills.add(c1);
fills.add(c2);
a.setClasseC(fills);

session.persist(a); // Amb cascade, també guarda c1 i c2, no cal fer persist dos cops
```

## 3. Relació Many-to-Many (N:M)

Múltiples instàncies de ClasseB poden estar relacionades amb múltiples instàncies de ClasseC.

### Exemple: ClasseB (N) ← → (M) ClasseC

**ClasseC (Propietària)**:
```java
@ManyToMany
@JoinTable(
    name="classeB_classeC",
    joinColumns=@JoinColumn(name="classeC_id"),
    inverseJoinColumns=@JoinColumn(name="classeB_id")
)
private List<ClasseB> classeB = new ArrayList<>();
```

**ClasseB (Inversa)**:
```java
@ManyToMany(mappedBy="classeB")
private List<ClasseC> classeC = new ArrayList<>();
```

### Com funciona?

- Es crea una **taula intermèdia** (`classeB_classeC`) que conté les claus foranes de les dues taules
- La part propietària té `@JoinTable` i defineix el nom de la taula intermèdia i les columnes
- `joinColumns`: Columna que apunta a l'entitat propietària (ClasseC)
- `inverseJoinColumns`: Columna que apunta a l'altra entitat (ClasseB)

### Quan utilitzar-la?

- Estudiants ← → Assignatures
- Autors ← → Llibres (quan un llibre pot tenir múltiples autors)
- Actors ← → Pel·lícules
- Productes ← → Categories (si un producte pot estar en múltiples categories)

### Com crear la relació?

```java
ClasseB b1 = new ClasseB("B1");
ClasseB b2 = new ClasseB("B2");
ClasseC c1 = new ClasseC("C1");

// Establir relació des de la part propietària
c1.getClasseB().add(b1);
c1.getClasseB().add(b2);

// També des de l'altra part per mantenir sincronització
b1.getClasseC().add(c1);
b2.getClasseC().add(c1);

session.persist(c1);
session.persist(b1);
session.persist(b2);
```

## Taula resum de relacions

| Relació | Part propietària | Part inversa | Taula intermèdia |
|---------|------------------|--------------|------------------|
| **One-to-One** | Té `@JoinColumn` | Té `mappedBy` | No |
| **One-to-Many** | La part Many té `@JoinColumn` | La part One té `mappedBy` | No |
| **Many-to-One** | Té `@JoinColumn` | Té `mappedBy` | No |
| **Many-to-Many** | Té `@JoinTable` | Té `mappedBy` | Sí |

## Bones pràctiques

### 1. Sincronització bidireccional

Quan establim relacions bidireccionals, cal mantenir ambdues parts sincronitzades:

```java
public void setClasseB(ClasseB b) {
    this.classeB = b;
    if (b != null && b.getClasseA() != this) {
        b.setClasseA(this);
    }
}
```

### 2. Evitar referències circulars en toString()

**MAI** incloure relacions bidireccionals al `toString()` perquè causa `StackOverflowError`:

```java
// ❌ MALAMENT
@Override
public String toString() {
    return "ClasseA [id=" + id + ", classeB=" + classeB + "]";
}

// ✅ CORRECTE
@Override
public String toString() {
    return "ClasseA [id=" + id + ", nom=" + nom + "]";
}
```

### 3. Inicialitzar col·leccions

Sempre inicialitzar llistes per evitar `NullPointerException`:

```java
@OneToMany(mappedBy="classeA")
private List<ClasseC> classeC = new ArrayList<>();
```

### 4. Escollir el cascade adequat

- Utilitza `CascadeType.ALL` amb precaució
- Per relacions One-to-Many on el fill no pot existir sense el pare, utilitzar `CascadeType.ALL`
- Per relacions Many-to-Many, evitar cascade en ambdues parts


## Errors comuns

### ❌ Error 1: mappedBy apuntant al lloc incorrecte

```java
// MALAMENT - mappedBy ha d'apuntar al nom del camp, no de la classe
@OneToOne(mappedBy="ClasseB")

// CORRECTE
@OneToOne(mappedBy="classeB")
```

### ❌ Error 2: Ambdues parts amb @JoinColumn

```java
// MALAMENT - només la propietària ha de tenir @JoinColumn
@OneToOne
@JoinColumn(name="classeB_id")
private ClasseB classeB;

// A ClasseB
@OneToOne
@JoinColumn(name="classeA_id")  // ❌ Duplicat!
private ClasseA classeA;
```

### ❌ Error 3: No sincronitzar relacions bidireccionals

```java
// Establir només un costat
a.setClasseB(b);
// Si no fem b.setClasseA(a), la relació pot no funcionar correctament
```

### ❌ Error 4: Utilitzar tipus incorrecte en relacions

```java
// MALAMENT - @ManyToOne amb List
@ManyToOne
private List<ClasseA> classeA;

// CORRECTE - @ManyToOne amb un sol objecte
@ManyToOne
private ClasseA classeA;
```


## Diagrama de relacions del projecte

```
ClasseA (1) ←────→ (1) ClasseB
   │                      │
   │                      │
   │ (1)              (N) │
   ↓                      ↓
ClasseC ←────────────→ ClasseB
        (N)        (M)
```

- **ClasseA** té relació 1:1 amb **ClasseB**
- **ClasseA** té relació 1:N amb **ClasseC**
- **ClasseB** té relació N:M amb **ClasseC**