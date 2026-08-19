package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 1. LEER (Cargar el catálogo)
    @GetMapping("/api/productos")
    public List<Map<String, Object>> obtenerProductos() {
        return jdbcTemplate.queryForList("SELECT * FROM \"Productos\" ORDER BY \"Id\" ASC");
    }

    // 2. CREAR / ACTUALIZAR (Inteligente: Detecta si viene con ID para actualizar o insertar)
    @PostMapping("/api/productos")
    public String guardarProducto(@RequestBody Map<String, Object> producto) {
        // Extraer ID si viene en el mapa (maneja tanto 'id' como 'Id')
        Object idObj = producto.get("id");
        if (idObj == null) {
            idObj = producto.get("Id");
        }

        // Capturar los campos contemplando variaciones de nombres que envía el frontend
        Object nombre = producto.get("nombre") != null ? producto.get("nombre") : producto.get("Nombre");
        Object precio = producto.get("precio") != null ? producto.get("precio") : (producto.get("precioPublico") != null ? producto.get("precioPublico") : producto.get("PrecioPublico"));
        Object precioMayor = producto.get("precioMayor") != null ? producto.get("precioMayor") : (producto.get("precioMayorista") != null ? producto.get("precioMayorista") : producto.get("PrecioMayorista"));
        Object stock = producto.get("stock") != null ? producto.get("stock") : (producto.get("Stock") != null ? producto.get("Stock") : producto.get("cantidad"));
        Object cat = producto.get("cat") != null ? producto.get("cat") : producto.get("categoria");
        Object talla = producto.get("talla") != null ? producto.get("talla") : producto.get("Talla");
        Object icon = producto.get("icon") != null ? producto.get("icon") : producto.get("icono");
        Object img = producto.get("imagenURL") != null ? producto.get("imagenURL") : (producto.get("imagenUrl") != null ? producto.get("imagenUrl") : producto.get("ImagenURL"));
        Object desc = producto.get("desc") != null ? producto.get("desc") : producto.get("Descripcion");

        if (idObj != null && !idObj.toString().isEmpty()) {
            // SI TIENE ID: Actualiza el registro existente en lugar de duplicarlo
            int id = Integer.parseInt(idObj.toString());
            String sql = "UPDATE \"Productos\" SET \"Nombre\"=?, \"PrecioPublico\"=?, \"PrecioMayorista\"=?, \"Stock\"=?, \"Categoria\"=?, \"Talla\"=?, \"Icono\"=?, \"ImagenURL\"=?, \"Descripcion\"=? WHERE \"Id\"=?";
            jdbcTemplate.update(sql, nombre, precio, precioMayor, stock, cat, talla, icon, img, desc, id);
            return "{\"mensaje\": \"Actualizado exitosamente\"}";
        } else {
            // SI NO TIENE ID: Crea un producto completamente nuevo
            String sql = "INSERT INTO \"Productos\" (\"Nombre\", \"PrecioPublico\", \"PrecioMayorista\", \"Stock\", \"Categoria\", \"Talla\", \"Icono\", \"ImagenURL\", \"Descripcion\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, nombre, precio, precioMayor, stock, cat, talla, icon, img, desc);
            return "{\"mensaje\": \"Guardado exitosamente\"}";
        }
    }

    // 3. EDITAR / CAMBIAR (Por si se usa PUT explícitamente)
    @PutMapping("/api/productos/{id}")
    public String actualizarProducto(@PathVariable int id, @RequestBody Map<String, Object> producto) {
        String sql = "UPDATE \"Productos\" SET \"Nombre\"=?, \"PrecioPublico\"=?, \"PrecioMayorista\"=?, \"Stock\"=?, \"Categoria\"=?, \"Talla\"=?, \"Icono\"=?, \"ImagenURL\"=?, \"Descripcion\"=? WHERE \"Id\"=?";
        jdbcTemplate.update(sql,
                producto.get("nombre"), producto.get("precio"), producto.get("precioMayor"),
                producto.get("stock"), producto.get("cat"), producto.get("talla"),
                producto.get("icon"), producto.get("img"), producto.get("desc"),
                id
        );
        return "{\"mensaje\": \"Actualizado exitosamente\"}";
    }

    // 4. ELIMINAR (Botón de Basurero)
    @DeleteMapping("/api/productos/{id}")
    public String eliminarProducto(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM \"Productos\" WHERE \"Id\"=?", id);
        return "{\"mensaje\": \"Eliminado exitosamente\"}";
    }
}