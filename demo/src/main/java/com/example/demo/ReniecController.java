package com.example.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/reniec")
@CrossOrigin(origins = "*") // Permite que tu HTML se conecte
public class ReniecController {

    @GetMapping("/dni")
    public ResponseEntity<String> consultarDni(@RequestParam String numero) {
        try {
            // La URL de Decolecta para DNI
            String url = "https://api.decolecta.com/v1/reniec/dni?numero=" + numero;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            // Tu Token de seguridad
            headers.set("Authorization", "Bearer sk_17410.SFndMpfhgcMRBVb2O9sZ3w25wQd6PT2d");
            headers.set("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Hacemos la consulta a Decolecta
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"Error al consultar DNI en la API\"}");
        }
    }
}