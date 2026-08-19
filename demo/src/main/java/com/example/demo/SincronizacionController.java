package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SincronizacionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- CLIENTES B2C (Mascotas) ---
    @GetMapping("/api/clientes-b2c")
    public List<Map<String, Object>> getB2C() {
        return jdbcTemplate.queryForList("SELECT * FROM \"ClientesB2C\"");
    }

    @PostMapping("/api/clientes-b2c")
    public String saveB2C(@RequestBody Map<String, Object> c) {
        Long id = Long.valueOf(c.get("id").toString());
        List<Map<String, Object>> existe = jdbcTemplate.queryForList("SELECT \"Id\" FROM \"ClientesB2C\" WHERE \"Id\" = ?", id);

        String mascota = c.get("mascota") != null ? c.get("mascota").toString() : "";
        String dni = c.get("dni") != null ? c.get("dni").toString() : "";
        String tel = c.get("tel") != null ? c.get("tel").toString() : "";

        if (existe.isEmpty()) {
            jdbcTemplate.update("INSERT INTO \"ClientesB2C\" (\"Id\", \"Nombre\", \"Dni\", \"Telefono\", \"Mascota\") VALUES (?, ?, ?, ?, ?)",
                    id, c.get("nombre"), dni, tel, mascota);
        } else {
            jdbcTemplate.update("UPDATE \"ClientesB2C\" SET \"Nombre\"=?, \"Dni\"=?, \"Telefono\"=?, \"Mascota\"=? WHERE \"Id\"=?",
                    c.get("nombre"), dni, tel, mascota, id);
        }
        return "{\"mensaje\": \"B2C Guardado\"}";
    }

    // --- CLIENTES B2B (Tiendas Aliadas) ---
    @GetMapping("/api/clientes-b2b")
    public List<Map<String, Object>> getB2B() {
        return jdbcTemplate.queryForList("SELECT * FROM \"ClientesB2B\"");
    }

    @PostMapping("/api/clientes-b2b")
    public String saveB2B(@RequestBody Map<String, Object> c) {
        Long id = Long.valueOf(c.get("id").toString());
        List<Map<String, Object>> existe = jdbcTemplate.queryForList("SELECT \"Id\" FROM \"ClientesB2B\" WHERE \"Id\" = ?", id);

        String ruc = c.get("ruc") != null ? c.get("ruc").toString() : "";
        String tel = c.get("tel") != null ? c.get("tel").toString() : "";
        String contacto = c.get("contacto") != null ? c.get("contacto").toString() : "";

        if (existe.isEmpty()) {
            jdbcTemplate.update("INSERT INTO \"ClientesB2B\" (\"Id\", \"RazonSocial\", \"Ruc\", \"Telefono\", \"Contacto\") VALUES (?, ?, ?, ?, ?)",
                    id, c.get("razonSocial"), ruc, tel, contacto);
        } else {
            jdbcTemplate.update("UPDATE \"ClientesB2B\" SET \"RazonSocial\"=?, \"Ruc\"=?, \"Telefono\"=?, \"Contacto\"=? WHERE \"Id\"=?",
                    c.get("razonSocial"), ruc, tel, contacto, id);
        }
        return "{\"mensaje\": \"B2B Guardado\"}";
    }

    // --- VENTAS Y TICKETS ---
    @GetMapping("/api/ventas")
    public List<Map<String, Object>> getVentas() {
        return jdbcTemplate.queryForList("SELECT * FROM \"Ventas\" ORDER BY \"Id\" ASC");
    }

    @PostMapping("/api/ventas")
    public String saveVenta(@RequestBody Map<String, Object> v) {
        try {
            Long id = Long.valueOf(v.get("id").toString());
            List<Map<String, Object>> existe = jdbcTemplate.queryForList("SELECT \"Id\" FROM \"Ventas\" WHERE \"Id\" = ?", id);

            boolean anulada = v.get("anulada") != null && (boolean) v.get("anulada");
            String itemsJson = v.get("itemsJSON") != null ? v.get("itemsJSON").toString() : "[]";

            // BLINDAJE CONTRA NULOS (Solución al Error 500)
            String fechaVenc = v.get("fechaVencimiento") != null ? v.get("fechaVencimiento").toString() : "";
            double total = v.get("total") != null ? Double.parseDouble(v.get("total").toString()) : 0.0;
            double recibido = v.get("recibido") != null ? Double.parseDouble(v.get("recibido").toString()) : 0.0;

            if (existe.isEmpty()) {
                jdbcTemplate.update("INSERT INTO \"Ventas\" (\"Id\", \"Fecha\", \"Cliente\", \"Vendedor\", \"Metodo\", \"Total\", \"Recibido\", \"Estado\", \"FechaVencimiento\", \"ItemsJSON\", \"Anulada\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        id, v.get("fecha"), v.get("cliente"), v.get("vendedor"), v.get("metodo"), total, recibido, v.get("estado"), fechaVenc, itemsJson, anulada);
            } else {
                jdbcTemplate.update("UPDATE \"Ventas\" SET \"Estado\"=?, \"Recibido\"=?, \"Anulada\"=? WHERE \"Id\"=?",
                        v.get("estado"), recibido, anulada, id);
            }
            return "{\"mensaje\": \"Venta Guardada\"}";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en Java: " + e.getMessage());
        }
    }
}