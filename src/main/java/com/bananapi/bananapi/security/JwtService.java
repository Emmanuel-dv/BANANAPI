package com.bananapi.bananapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
/**
 * Clase encargada de la lógica de los tokens para la aplicación.
 * La clase sirve para tener los métodos y la lógica pesada para que las otras clases la usen.
 */
public class JwtService {


    //Esta es la clave secreta que usaremos para crear nuestra secret key que usaremos para firmar los tokens
    private static final String CLAVE_SECRETA = "YmFuYW5hcGktc3VwZXItc2VjcmV0LWtleS1kZXZlbG9wbWVudC0xMjM0NTY3ODkw";


    //Este metodo es el que transforma nuestra cadena de caracteres en un objeto SecretKey
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(CLAVE_SECRETA);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Metodo para fabricar el token
     * <p>
     * Cogemos el nombre de usuario y generamos el token metiendole la info del username, la fecha de creacion,la de caducidad y lo firmamos con nuestra clave secreta.
     * <p>
     * El .compact() sirve para construir el JWT y entregarlo.
     *
     * @param username Nombre de usuario
     * @return Devuelve un String que deberemos pasar al formato deseado del token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSigningKey())
                .compact();
    }


    /**
     * Método para obtener el nombre de usuario a partir del token
     *
     * @param token que se nos pasa desde fuera
     * @return String con el nombre de usuario dentro del token
     */
    public String getUsernameDecoded(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Método para verificar un token (si el nombre de usuario esperado concuerda con el del token y si la fecha de caducidad es posterior a la fecha actual)
     *
     *  Recoge los claims del token con un método privado y almacena tanto el nombre de usuario dentro del Subject como la fecha de caducidad dentro de Expiration
     *
     *  Evalua los datos
     *
     * @param token que se nos pasa desde fuera
     * @param expectedUsername Nombre de usuario que esperamos recibir dentro del token codificado
     * @return el valor de la evaluación. True si tanto el nombre esperado coincide con el que hay en el token y si la fecha de caducidad es posterior a la fecha actual. False en cualquier otro caso.
     */
    public boolean verifyToken(String token, String expectedUsername) {

        Claims tokenClaims = getClaims(token);


        String tokenUsername = tokenClaims.getSubject();

        Date tokenDate = tokenClaims.getExpiration();

        return tokenUsername.equals(expectedUsername) && tokenDate.after(new Date());
    }


    /**
     * Método que decodifica un token que recibe como String y devuelve los Claims (payload)
     *
     * Coge el token que recibe por parámetros, lo decodifica y devuelve el payload
     *
     * .parser() -> sirve para empezar a decodificar el token, se usa junto a verifyWith( y la clave secreta que usemos) para verificar la veracidad del token (si es nuestro) mas adelante.
     *
     * .build() -> Construye la instancia del analizador (JwtParser) con la configuracion proporcionada previamente (la clave secreta para que la use para verificar en parseSignedClaims)
     *             Usamos .build porque este entrega el objeto parseador. Antes de poner este método, el parseador está en modo de configuración y este método finaliza ese modo.
     *             Para este ejemplo, nuestra configuración consta de pasarle la clave secreta.
     *
     *
     * .parseSignedClaims(token) -> Separa el token en sus 3 partes. Valida la firma criptográfica y la fecha de expiración.
     *                              Toma las 3 partes del token Header, Payload y Signature.
     *                              Recalcula la firma usando la clave secreta proporcionada y la contrasta con la firma original del token para ver si el token ha sido modificado o no.
     *                              Si el token fue modificado lanza una excepción, y si pasa la validación, devuelve el objeto parseado (Jws<claims>).
     *
     *
     * .getPayload() -> sirve para obtener el payload del token en forma de objeto Claims.
     *                  Este objeto contiene la info en un mapa de Clave valor del payload que se le dio al momento de crear el token
     *
     * .getSubject() -> para obtener el subject, que es el identificador principal del token.
     *                  Se puede modificar y poner otras cosas, no sólo el nombre de usuario.
     *
     * @param token token que recibe el método
     * @return Claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
