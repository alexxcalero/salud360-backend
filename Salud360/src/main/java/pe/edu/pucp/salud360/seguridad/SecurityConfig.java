package pe.edu.pucp.salud360.seguridad;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.pucp.salud360.autenticacion.services.UsuarioDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .build();

        /*
        http
                .csrf().disable()  // Solo para pruebas; idealmente manejar token CSRF luego
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**",
                                "/api/medicos/**",
                                "/api/personas/**",
                                "/api/tiposDocumentos/**",
                                "/api/permisos/**",
                                "/api/roles/**",
                                "/api/notificaciones/**",
                                "/api/citas-medicas/**",
                                "/api/documentos-medicos/**",
                                "/api/locales/**",
                                "/api/servicios/**",
                                "/api/afiliaciones/**",
                                "/api/mediosDePago/**",
                                "/api/membresias/**",
                                "/api/pagos/**",
                                "/api/periodos/**",
                                "/api/solicitudes/**",
                                "/api/comentarios/**",
                                "/api/comunidades/**",
                                "/api/foros/**",
                                "/api/publicaciones/**",
                                "/api/testimonios/**",
                                "/api/reglas/**").permitAll()  // Agregar los endpoints a probar, sino dara error
                        .anyRequest().authenticated()
                )
                .httpBasic();  // Soporta autenticación básica vía Postman
        return http.build();
        */
    }

    // Para poder hashear las contrasenhas de usuario antes de guardarlas en la BD
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
