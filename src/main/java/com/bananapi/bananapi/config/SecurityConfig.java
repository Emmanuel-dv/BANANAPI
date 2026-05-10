package com.bananapi.bananapi.config;

import com.bananapi.bananapi.repository.UserRepository;
import com.bananapi.bananapi.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * UserDetailsService es un adaptador de lectura. Spring security sabe comparar contraseñas pero no sabe como llegar a obtener esos datos de la base de datos.
     * Tampoco sabe sobre los componentes que sí pueden interactuar con la base de datos.
     * <p>
     * Spring security recurre entonces a userDetailsService para que este interactúe con la base de datos y le de la información que quiere Spring Security con el formato
     * que entiende para poder operar sobre ello.
     *
     * @return
     */
    @Bean
    public UserDetailsService getUserDetailsService() {
        return username -> userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthenticator() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(getUserDetailsService());

        authProvider.setPasswordEncoder(getPasswordEncoder());

        return authProvider;
    }

    /*
        AuthenticationManager es quien valida si un intento de login es valido o no.
        No hace el trabajo sucio, delega eso a DaoAuthenticacionProvider q pasara a usar la base de datos y para sacar los datos y comparar la contra con el encoder que le pasé

        AuthenticationConfiguration ve toda la configuracion de seguridad que hemos declarado en Spring y lo guarda dentro de sí. Spring la pasa por parámetro con toda la info dentro.
     */
    @Bean
    public AuthenticationManager getAuthenticatorManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/users/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/run/**").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}
