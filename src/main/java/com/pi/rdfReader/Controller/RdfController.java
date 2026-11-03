
package com.pi.rdfReader.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pi.rdfReader.Service.RdfService;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class RdfController {

    private final RdfService rdfService;

    public RdfController(RdfService rdfService) {
        this.rdfService = rdfService;
    }

    // 1. Toutes les œuvres
    @GetMapping("/oeuvres")
    public List<Map<String, String>> getAllOeuvres() {
        return rdfService.getAllOeuvres();
    }

    // 2. Œuvres par artiste
    @GetMapping("/oeuvres/artiste")
    public List<Map<String, String>> getOeuvresByArtiste(
            @RequestParam String nom) {

        return rdfService.getOeuvresByArtiste(nom);
    }

    // 3. Tous les artistes
    @GetMapping("/artistes")
    public List<Map<String, String>> getAllArtistes() {
        return rdfService.getAllArtistes();
    }

    // 4. Œuvres par période
    @GetMapping("/oeuvres/periode")
    public List<Map<String, String>> getOeuvresByPeriode(
            @RequestParam String annee) {

        return rdfService.getOeuvresByPeriode(annee);
    }

    // 5. Top 10 artistes
    @GetMapping("/statistiques/artistes")
    public List<Map<String, String>> getTopArtistes() {
        return rdfService.getTopArtistes();
    }

    // 6. Nombre d'œuvres par siècle
    @GetMapping("/statistiques/periodes")
    public List<Map<String, String>> getOeuvresParSiecle() {
        return rdfService.getOeuvresParSiecle();
    }

    // 7. Nombre d'œuvres par type
    @GetMapping("/statistiques/types")
    public List<Map<String, String>> getOeuvresParType() {
        return rdfService.getOeuvresParType();
    }
}
