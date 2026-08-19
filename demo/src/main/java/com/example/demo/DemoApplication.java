package com.example.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Primary
	@Bean(name = "neonDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.neon")
	public DataSource neonDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	// Le pasamos AMBAS bases de datos para probarlas al mismo tiempo
	@Bean
	CommandLineRunner probarConexion(
			@Qualifier("neonDataSource") DataSource neonDataSource,
			@Qualifier("mysqlDataSource") DataSource mysqlDataSource) {

		return args -> {
			System.out.println("\n==================================================");

			// 1. PRUEBA MYSQL (LOCAL)
			try {
				System.out.println("🖥️ CONECTANDO AL MYSQL LOCAL...");
				JdbcTemplate mysqlJdbc = new JdbcTemplate(mysqlDataSource);

				// Le ordenamos a MySQL que cree una tablita de prueba
				mysqlJdbc.execute("CREATE TABLE IF NOT EXISTS usuarios_prueba (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50))");
				System.out.println("✅ ¡MYSQL LOCAL FUNCIONA Y CREÓ LA TABLA!");
			} catch (Exception e) {
				System.out.println("❌ ERROR EN MYSQL: " + e.getMessage());
			}

			System.out.println("--------------------------------------------------");

			// 2. PRUEBA NEON (NUBE)
			try {
				System.out.println("☁️ CONECTANDO A LA NUBE (NEON)...");
				JdbcTemplate neonJdbc = new JdbcTemplate(neonDataSource);
				neonJdbc.execute("CREATE TABLE IF NOT EXISTS \"Productos\" (\"Id\" SERIAL PRIMARY KEY, \"Nombre\" VARCHAR(100), \"PrecioPublico\" DECIMAL(10,2), \"PrecioMayorista\" DECIMAL(10,2), \"Stock\" INT, \"Categoria\" VARCHAR(50), \"Talla\" VARCHAR(20), \"Icono\" VARCHAR(50), \"ImagenURL\" VARCHAR(255), \"Descripcion\" TEXT)");

				Integer count = neonJdbc.queryForObject("SELECT COUNT(*) FROM \"Productos\"", Integer.class);
				System.out.println("✅ ¡CONEXIÓN A NEON EXITOSA! Tienes " + count + " productos listos.");
			} catch (Exception e) {
				System.out.println("❌ ERROR EN NEON: " + e.getMessage());
			}

			System.out.println("==================================================\n");
		};
	}
}