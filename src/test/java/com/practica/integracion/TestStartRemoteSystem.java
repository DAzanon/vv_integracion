package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;


@ExtendWith(MockitoExtension.class)
public class TestStartRemoteSystem {
    @Mock private static AuthDAO mockAuthDao;
    @Mock private static GenericDAO mockGenericDao;

    @Test
    public void testStartRemoteSystemWithValidUserAndSystem() throws Exception {
        User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
        Mockito.when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);

        String validId = "12345"; // id valido de sistema
        ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
        Mockito.when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);
        // primero debe ejecutarse la llamada al dao de autenticación
        // despues el de  acceso a datos del sistema (la validaciones del orden en cada prueba)
        InOrder ordered = Mockito.inOrder(mockAuthDao, mockGenericDao);
        // instanciamos el manager con los mock creados
        SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
        // llamada al api a probar
        Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);
        assertEquals(retorno.toString(), "[uno, dos]");
        // vemos si se ejecutan las llamadas a los dao, y en el orden correcto
        ordered.verify(mockAuthDao).getAuthData(validUser.getId());
        ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
    }




}