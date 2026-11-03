
package com.pi.rdfReader.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Service;

@Service
public class RdfService {

    private final Model model;

    // Chargement du fichier RDF
    public RdfService() {

        model = ModelFactory.createDefaultModel();

        InputStream rdf = getClass().getResourceAsStream("/musee.rdf");

        if (rdf == null) {
            throw new RuntimeException(
                    "Fichier musee.rdf introuvable dans resources"
            );
        }

        RDFDataMgr.read(model, rdf, Lang.RDFXML);

        System.out.println("Fichier musee.rdf chargé avec succès !");
        System.out.println("Nombre de triplets : " + model.size());
    }

    // 1. Toutes les œuvres
    public List<Map<String, String>> getAllOeuvres() {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX dct: <http://purl.org/dc/terms/>

            SELECT DISTINCT ?titre ?identifiant ?date
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:title ?titre .

                OPTIONAL {
                    ?oeuvre dc:identifier ?identifiant .
                }

                OPTIONAL {
                    ?oeuvre dct:created ?date .
                }

                FILTER(LANG(?titre) = "en")
            }
            LIMIT 20
            """;

        return executeQuery(sparql);
    }

    // 2. Rechercher les œuvres par artiste
    public List<Map<String, String>> getOeuvresByArtiste(String nomArtiste) {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX dct: <http://purl.org/dc/terms/>
            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

            SELECT DISTINCT ?titre ?identifiant ?date ?artiste
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:title ?titre .
                ?oeuvre dc:creator ?createur .

                ?createur skos:prefLabel ?artiste .

                OPTIONAL {
                    ?oeuvre dc:identifier ?identifiant .
                }

                OPTIONAL {
                    ?oeuvre dct:created ?date .
                }

                FILTER(LANG(?titre) = "en")

                FILTER(CONTAINS(
                    LCASE(STR(?artiste)),
                    LCASE(STR(?nom))
                ))
            }
            LIMIT 50
            """;

        Query query = QueryFactory.create(sparql);

        QuerySolutionMap binding = new QuerySolutionMap();
        binding.add("nom", model.createLiteral(nomArtiste));

        return executeQuery(query, binding);
    }

    // 3. Tous les artistes
    public List<Map<String, String>> getAllArtistes() {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

            SELECT DISTINCT ?createur ?nom
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:creator ?createur .

                OPTIONAL {
                    ?createur skos:prefLabel ?nom .
                }
            }
            LIMIT 30
            """;

        return executeQuery(sparql);
    }

    // 4. Rechercher les œuvres par période
    public List<Map<String, String>> getOeuvresByPeriode(String periode) {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX dct: <http://purl.org/dc/terms/>

            SELECT DISTINCT ?titre ?identifiant ?date
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:title ?titre .
                ?oeuvre dct:created ?date .

                OPTIONAL {
                    ?oeuvre dc:identifier ?identifiant .
                }

                FILTER(LANG(?titre) = "en")

                FILTER(CONTAINS(
                    LCASE(STR(?date)),
                    LCASE(STR(?periode))
                ))
            }
            LIMIT 50
            """;

        Query query = QueryFactory.create(sparql);

        QuerySolutionMap binding = new QuerySolutionMap();
        binding.add("periode", model.createLiteral(periode));

        return executeQuery(query, binding);
    }

    // 5. Top 10 des artistes selon le nombre d'œuvres
    public List<Map<String, String>> getTopArtistes() {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

            SELECT ?createur
                   (
                     IF(
                       MAX(IF(LANG(?nom) = "en" && STRLEN(STR(?nom)) > 0,
                              STR(?nom), "")) != "",
                       MAX(IF(LANG(?nom) = "en" && STRLEN(STR(?nom)) > 0,
                              STR(?nom), "")),
                       MAX(IF(LANG(?nom) = "nl" && STRLEN(STR(?nom)) > 0,
                              STR(?nom), ""))
                     ) AS ?artiste
                   )
                   (COUNT(DISTINCT ?oeuvre) AS ?nombreOeuvres)
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:creator ?createur .

                OPTIONAL {
                    ?createur skos:prefLabel ?nom .
                }
            }
            GROUP BY ?createur
            ORDER BY DESC(?nombreOeuvres)
            LIMIT 10
            """;

        return executeQuery(sparql);
    }

    // 6. Nombre d'œuvres par siècle
    public List<Map<String, String>> getOeuvresParSiecle() {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dct: <http://purl.org/dc/terms/>
            PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>

            SELECT ?siecle
                   (COUNT(DISTINCT ?oeuvre) AS ?nombreOeuvres)
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dct:created ?date .

                FILTER(REGEX(STR(?date), "[0-9]{4}"))

                BIND(
                    xsd:integer(
                        REPLACE(
                            STR(?date),
                            "^.*?([0-9]{4}).*$",
                            "$1"
                        )
                    ) AS ?annee
                )

                BIND(
                    FLOOR((?annee - 1) / 100) + 1
                    AS ?numeroSiecle
                )

                BIND(
                    CONCAT(STR(?numeroSiecle), "e siècle")
                    AS ?siecle
                )
            }
            GROUP BY ?siecle ?numeroSiecle
            ORDER BY ?numeroSiecle
            """;

        return executeQuery(sparql);
    }

    // 7. Nombre d'œuvres par type
    public List<Map<String, String>> getOeuvresParType() {

        String sparql = """
            PREFIX edm: <http://www.europeana.eu/schemas/edm/>
            PREFIX dc: <http://purl.org/dc/elements/1.1/>
            PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

            SELECT ?type
                   (
                     IF(
                       MAX(IF(LANG(?libelle) = "en" &&
                              STRLEN(STR(?libelle)) > 0,
                              STR(?libelle), "")) != "",
                       MAX(IF(LANG(?libelle) = "en" &&
                              STRLEN(STR(?libelle)) > 0,
                              STR(?libelle), "")),
                       IF(
                         MAX(IF(LANG(?libelle) = "nl" &&
                                STRLEN(STR(?libelle)) > 0,
                                STR(?libelle), "")) != "",
                         MAX(IF(LANG(?libelle) = "nl" &&
                                STRLEN(STR(?libelle)) > 0,
                                STR(?libelle), "")),
                         STR(?type)
                       )
                     ) AS ?nomType
                   )
                   (COUNT(DISTINCT ?oeuvre) AS ?nombreOeuvres)
            WHERE {
                ?oeuvre a edm:ProvidedCHO .
                ?oeuvre dc:type ?type .

                OPTIONAL {
                    ?type skos:prefLabel ?libelle .
                }
            }
            GROUP BY ?type
            ORDER BY DESC(?nombreOeuvres)
            LIMIT 20
            """;

        return executeQuery(sparql);
    }

    // Exécution d'une requête sans paramètres
    private List<Map<String, String>> executeQuery(String sparql) {

        Query query = QueryFactory.create(sparql);

        return executeQuery(query, null);
    }

    // Exécution d'une requête avec paramètres
    private List<Map<String, String>> executeQuery(
            Query query,
            QuerySolutionMap binding) {

        List<Map<String, String>> resultat = new ArrayList<>();

        try (QueryExecution qexec =
                     QueryExecutionFactory.create(query, model)) {

            if (binding != null) {
                qexec.setInitialBinding(binding);
            }

            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {

                QuerySolution solution = results.nextSolution();

                Map<String, String> ligne = new HashMap<>();

                solution.varNames().forEachRemaining(variable -> {

                    RDFNode node = solution.get(variable);

                    if (node != null) {

                        String valeur;

                        if (node.isLiteral()) {
                            Literal literal = node.asLiteral();
                            valeur = literal.getString();
                        } else if (node.isResource()) {
                            valeur = node.asResource().getURI();

                            if (valeur == null) {
                                valeur = node.toString();
                            }
                        } else {
                            valeur = node.toString();
                        }

                        ligne.put(variable, valeur);
                    }
                });

                resultat.add(ligne);
            }
        }

        return resultat;
    }
}
