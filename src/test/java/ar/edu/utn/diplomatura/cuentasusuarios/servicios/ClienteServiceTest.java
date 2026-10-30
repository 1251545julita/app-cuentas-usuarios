package ar.edu.utn.diplomatura.cuentasusuarios.servicios;

import ar.edu.utn.diplomatura.cuentasusuarios.componentes.ValidadorDeDni;
import ar.edu.utn.diplomatura.cuentasusuarios.entidades.Usuario;
import ar.edu.utn.diplomatura.cuentasusuarios.repositorios.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

//    @Mock
//    private ClienteRepository dummyClienteRepository;
//
//    @Mock
//    private ValidadorDeCuit validadorDeCuit;
//
//    @InjectMocks
//    private ClienteService clientService;
//
//    @Test
//    @DisplayName("ListarTodos devuelve lo que le da el repositorio y lo consulta una vez")
//    void listarTodosConsultaAlRepositorio() {
//
//        //Given
//
//        List<Cliente> clientesDeMentira = List.of(
//                new Cliente("Ana", "Gomez", "2012345678", "ana@mail.com", 1000.0),
//                new Cliente("Bruno", "Diaz", "2730123456", "bruno@mail.com", 2000.0));
//
//        when(dummyClienteRepository.findAll()).thenReturn(clientesDeMentira);
//
//        //when
//        List<Cliente> resultado = clientService.listarTodos();
//
//        //then
//
//        assertEquals(2,resultado.size());
//        assertEquals("Ana", resultado.get(0).getNombre());
//
//        verify(dummyClienteRepository).findAll();
//
//
//    }
//
//    @Test
//    void buscarPorIdDevuelveNull(){
//        when(dummyClienteRepository.findById(999L)).thenReturn(Optional.empty());
//        Optional<Cliente> resultado = clientService.buscarPorId(999L);
//        assertTrue(resultado.isEmpty());
//        assertNotNull(resultado);
//        verify(dummyClienteRepository).findById(999L);
//
//    }



}