
# 🏛️ RDF Museum API – Rijksmuseum

A semantic REST API for exploring and querying museum artworks using RDF, Apache Jena, SPARQL, and Spring Boot.

## 📖 Overview

RDF Museum API is a Semantic Web project designed to explore cultural heritage data from the Rijksmuseum dataset.

The project uses RDF (Resource Description Framework) to represent museum information as interconnected resources. Apache Jena is used to load and manipulate RDF data, while SPARQL enables semantic queries over the dataset.

A REST API developed with Spring Boot exposes this information through several endpoints, allowing users to retrieve artworks, explore artists, filter works by period, and access statistical information.

## 🎯 Project Objectives

- Understand and manipulate RDF data.
- Explore semantic relationships between artworks and artists.
- Query a cultural heritage dataset using SPARQL.
- Develop REST endpoints to expose semantic data.
- Generate statistics from RDF resources.
- Integrate Semantic Web technologies with Spring Boot.

## ✨ Features

### 🎨 Artwork Management
- Retrieve all artworks.
- Search artworks by artist name.
- Filter artworks by creation period.

### 👨‍🎨 Artist Exploration
- Retrieve artists and creators.
- Search artworks associated with a specific artist.
- Identify the most represented creators.

### 📊 Statistical Analysis
- Display the most represented creators.
- Group artworks by century.
- Group artworks by type.

### 🔍 Semantic Data Processing
- Load RDF/XML data using Apache Jena.
- Execute SPARQL queries.
- Extract RDF resources and literal values.
- Process multilingual labels and typed literals.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| Spring Boot | REST API development |
| Apache Jena | RDF data management |
| SPARQL | Semantic queries |
| RDF/XML | Data representation |
| Maven | Dependency management |
| Draw.io | RDF/RDFS schema design |

## 🏗️ Project Architecture

The application follows a layered architecture:

1. **RDF Dataset:** The `musee.rdf` file contains museum data in RDF/XML format.
2. **Service Layer:** Loads the RDF dataset, manages the Jena model, executes SPARQL queries and processes results.
3. **Controller Layer:** Exposes REST endpoints and handles HTTP requests.
4. **REST API:** Returns museum information through HTTP requests and JSON responses.

## 📁 Project Structure

```text
rdfReader/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/pi/rdfReader/
│       │       ├── Controller/
│       │       ├── Service/
│       │       └── ...
│       └── resources/
│           └── musee.rdf
├── docs/
├── .mvn/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

## 🔗 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/oeuvres` | Retrieve all artworks |
| GET | `/oeuvres/artiste?nom=...` | Search artworks by artist |
| GET | `/artistes` | Retrieve artists and creators |
| GET | `/oeuvres/periode?annee=...` | Filter artworks by period |
| GET | `/statistiques/artistes` | Get top creators |
| GET | `/statistiques/periodes` | Count artworks by century |
| GET | `/statistiques/types` | Count artworks by type |

## 🧩 RDF Data Model

The project uses several semantic vocabularies:

- **ORE:** Aggregation of digital objects.
- **Europeana EDM:** Cultural heritage objects, agents, places and web resources.
- **Dublin Core (DC):** Descriptive metadata such as title, creator and type.
- **Dublin Core Terms (DCT):** Additional metadata such as creation date and spatial information.
- **SKOS:** Concepts and multilingual labels.
- **WGS84:** Geographic coordinates.
- **RDA Group 2:** Information about agents.

Main RDF resources:

- `ore:Aggregation`
- `edm:ProvidedCHO`
- `edm:Agent`
- `edm:Place`
- `skos:Concept`
- `edm:WebResource`

## 🚀 Installation and Setup

### Prerequisites

- Java JDK compatible with the project configuration.
- Maven.
- Git.

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/RDF-Museum-API.git
cd RDF-Museum-API
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

### 4. Access the API

```text
http://localhost:8080/oeuvres
```

## 📸 Screenshots

Screenshots and project diagrams will be added here.

## 🔮 Future Improvements

- Add pagination for artwork retrieval.
- Improve search with additional metadata criteria.
- Add advanced filters.
- Integrate a data visualization dashboard.
- Add API documentation using Swagger/OpenAPI.
- Implement automated tests.

## 👩‍💻 Author

**Amira Tayssir**

Computer Engineering Student  
Specialization: Big Data & Business Intelligence

---

⭐ Feel free to explore the project!
