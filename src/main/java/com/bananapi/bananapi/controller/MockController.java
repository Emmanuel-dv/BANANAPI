package com.bananapi.bananapi.controller;

import com.bananapi.bananapi.dto.requestdto.RequestMockDTO;
import com.bananapi.bananapi.dto.responsedto.ResponseMockDTO;
import com.bananapi.bananapi.service.MockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mocks")
@RequiredArgsConstructor
public class MockController {

    private final MockService mockService;


    /**
     * Controlador que crea mocks.
     *
     * @param requestMockDTO Es el dto que se nos pasa con los datos del mock a crear (nombre del mock y el json asociado)
     * @param principal      Parametro que tiene el nombre de usuario (se coge por el jwt)
     * @return Se devuelve el dto de respuesta (contiene el nombre, la url y el json)
     */
    @PostMapping
    public ResponseMockDTO createMock(@Valid @RequestBody RequestMockDTO requestMockDTO, Principal principal) {
        return this.mockService.createApiMock(requestMockDTO, principal.getName());
    }

    /**
     * Controlador que actualiza mocks.
     *
     * @param requestMockDTO Es el objeto DTO que se nos envia desde fuera con los nuevos datos para guardar en la base de datos
     * @param url            La url a la que corresponde el mock de la api (es una UUID unica)
     * @param principal      Parametro para resolver el nombre de usuario
     * @return Objeto que entra de vuelta los datos del mock que hay en la base de datos actualizados
     */
    @PutMapping("/{url}")
    public ResponseMockDTO updateMock(@Valid @RequestBody RequestMockDTO requestMockDTO, @PathVariable String url, Principal principal) {
        return this.mockService.updateApiMock(requestMockDTO, url, principal.getName());
    }

    /**
     * Controlador para borrar un mock
     *
     * @param url       Url unica del mock
     * @param principal Parametro para resolver el nombre de usuario
     */
    @DeleteMapping("/{url}")
    public void deleteMock(@PathVariable String url, Principal principal) {
        this.mockService.deleteMock(url, principal.getName());
    }

    @GetMapping("/{url}")
    public ResponseMockDTO getSingleMock(@PathVariable String url, Principal principal) {
        return this.mockService.getSingleMock(url, principal.getName());
    }

    @GetMapping
    public List<ResponseMockDTO> getAllUserMocks(Principal principal) {
        return this.mockService.getAllUserMocks(principal.getName());
    }

}
