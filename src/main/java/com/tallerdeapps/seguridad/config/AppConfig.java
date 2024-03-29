package com.tallerdeapps.seguridad.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Clase de configuración hecha a mano desde el curso 1-Anotación para indicar
 * que es una configuración 2-Anotación para habilitar MVC 3-Anotación para
 * indicar donde buscará Spring todas las clases de componentes 4-La cuarta
 * anotación indica el archivo de configuración de la BBDD de
 * usuarios(seguridad)
 */
//método que se encarga de resolver las vistas de los recursos internos, directorio web y extensión de archivos
@Configuration
@EnableWebMvc
@ComponentScan("com.tallerdeapps.seguridad")
@PropertySource("classpath:persistencia-mysql.properties")
public class AppConfig {

    // Anotación para inyección de dependencias. En este contexto, Spring crea automáticamente una instancia de
    // la clase Environment sin necesidad de instanciarla.
    @Autowired
    // Environment es una interfaz en Spring que proporciona acceso a las propiedades del entorno de la aplicación,
    // como variables de entorno, propiedades del sistema y propiedades definidas en archivos de propiedades 
    // cargados con @PropertySource.
    private Environment env;

    // sistema de log para revisar
    private Logger miLogger = Logger.getLogger(getClass().getName());

    // Definir un Bean para seguridad
    @Bean
    public DataSource seguridadDataSource() {

        // Crear un objeto ComboPooledDataSource para manejar la conexión a la base de datos
        ComboPooledDataSource seguridadDataSource = new ComboPooledDataSource();
        try {
            // Configurar el controlador JDBC que se utilizará para la conexión
            seguridadDataSource.setDriverClass(env.getProperty("jdbc.driver"));

            // Registrar información sobre la URL y el usuario de la base de datos en el log (netbeans->Output->Apache Tomcat or TomEE
            miLogger.info(">>>>> URL de la BBDD: " + env.getProperty("jdbc.url"));
            miLogger.info(">>>>> Usuario de la BBDD: " + env.getProperty("jdbc.username"));

            // Establecer la URL de la base de datos, el usuario y la contraseña
            seguridadDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
            seguridadDataSource.setUser(env.getProperty("jdbc.user"));
            seguridadDataSource.setPassword(env.getProperty("jdbc.password"));
        } catch (PropertyVetoException ex) {
            // Manejar una excepción si ocurre al establecer la clase del controlador JDBC
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

        // pool de conexiones
        // Tamaño inicial del grupo de conexiones
        int initialPoolSize = Integer.parseInt(env.getProperty("connection.pool.initialPoolSize"));
        // Tamaño mínimo del grupo de conexiones
        int minPoolSize = Integer.parseInt(env.getProperty("connection.pool.minPoolSize"));
        // Tamaño máximo del grupo de conexiones
        int maxPoolSize = Integer.parseInt(env.getProperty("connection.pool.maxPoolSize"));
        // Tiempo máximo de inactividad para las conexiones (milisegundos)
        int maxIdleTime = Integer.parseInt(env.getProperty("connection.pool.maxIdleTime"));
        // Devolver el objeto ComboPooledDataSource configurado como un Bean DataSource
        return seguridadDataSource;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/view/"); //donde están las vistas
        viewResolver.setSuffix(".jsp"); // extensión

        return viewResolver;
    }
}
