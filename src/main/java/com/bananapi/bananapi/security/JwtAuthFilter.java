package com.bananapi.bananapi.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    /**
     * Metodo para procesar JWT
     * <p>
     * Esta clase intercepta las peticiones antes que la aplicación para poder validarlas en el contexto de seguridad de Spring, es decir, que estén autenticados.
     * Cada peticion que se haga, ahora se sabrá quien la ha hecho (como resumen).
     *
     * @param request     la petición HTTP que se nos envía desde cliente con todas sus cabeceras etc.
     * @param response    la respuesta que mandamos al siguiente filtro
     * @param filterChain La interfaz que usamos para dejar dejar pasar al siguiente filtro de la aplicacion
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*
            Cada cabecera de una petición HTTP tiene un nombre y diferente información contenida por esa cabecera. Se puede investigar los nombres por convención atribuidos.
            Cogemos toda la información del Header de autenticación (que lleva el nombre de Authorization)
            Esto nos devuelve el String del token que sería: "Bearer " y a continuación el token codificado.
         */
        String authHeader = request.getHeader("Authorization");

        /*
            Verificamos que haya algo dentro de la cabecera de autentificación y que lo que haya empiece con el formato estándar de JWT
            En caso de que alguna sea errónea, detenemos la ejecución de este filtro y escalamos la petición en otro lugar en vez de cortar el flujo aquí.
            Esto haría que falle luego con un código http de 401
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Cogemos todo el string del token codificado
        String token = authHeader.substring(7);


        String nombreUsuario = null;

        /*
            Probamos que el nombre de usuario esté dentro del token, si falla porque viene null o cualquier cosa ingresa al catch y hace que pase al siguiente filtro
            Esto evita que mas adelante por el contexto vacío de un fallo 500 puesto que se corta el flujo. De este modo da un 400 algo (volver a mirar)
         */
        try {
            nombreUsuario = jwtService.getUsernameDecoded(token);
        } catch (JwtException e) {
            filterChain.doFilter(request, response);
            return;
        }

        //Creamos un contexto de seguridad vacío porque queremos asignarle nosotros uno propio
        SecurityContext contexto = SecurityContextHolder.createEmptyContext();

        //Esto es algo un pooc antiguo el nombre, pero sigue funcionando puesto que a esta peticion HTTP le asignamos un nombre, que es el del usuario
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(nombreUsuario, null, Collections.emptyList());

        //Rellenamos la caja vacía del contexto con la identidad que hemos creado.
        contexto.setAuthentication(authRequest);

        //Asignamos el contexto al hilo de ejecucion para que sepa que a una petición HTTP debe asignarle un propietario, y este será identificado mediante su nombre de usuario.
        SecurityContextHolder.setContext(contexto);

        //Pasamos a la siguiente capa
        filterChain.doFilter(request, response);

    }


}
