package com.tallerdeapps.seguridad.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class SeguridadAppConfig extends WebSecurityConfigurerAdapter {

    // inyectar el método que hace la conexión a la BBDD
    @Autowired
    private DataSource seguridadDataSource;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        // configura la autenticación basada en JDBC en Spring Security, utilizando el DataSource llamado 
        // seguridadDataSource creado en AppConfig
        auth.jdbcAuthentication().dataSource(seguridadDataSource);
    }

    // (método para desviar el login predeterminado del ejemplo del video anterior a uno personalizado)
    // Método para configurar la seguridad de la aplicación
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Autorizar todas las peticiones entrantes                                //
        // http.authorizeRequests().anyRequest().authenticated().and().formLogin() //
        
        
        http.authorizeRequests()
                .antMatchers("/").hasRole("USUARIO")// Establecer que a la pantalla de inicio pueda entrar quien tenga rol de "usuario"
                .antMatchers("/administradores/**").hasRole("ADMNISTRADOR")// Establecer que a la pantalla de admins(y sus subdirectorios) solo entra quien tenga ese rol
                .antMatchers("/ayudante/**").hasRole("AYUDANTE")// Establecer que a la pantalla de "ayudante" solo entra ese rol 
                .and().formLogin()
                // Establecer la página de inicio de sesión personalizada como "/miFormularioLogin"
                .loginPage("/miFormularioLogin")
                // Establecer la URL de procesamiento del inicio de sesión
                // el formulario envía el login por HTTP POST a "/autenticacionUsuario" y este método de Spring Security procesa los datos ahí
                .loginProcessingUrl("/autenticacionUsuario")
                .permitAll() // Permitir que todos los usuarios intenten iniciar sesión
                // Configurar la funcionalidad de cierre de sesión, permitiendo a todos los usuarios cerrar sesión
                .and().logout().permitAll()
                .and().exceptionHandling().accessDeniedPage("/acceso_denegado");
    }

}
