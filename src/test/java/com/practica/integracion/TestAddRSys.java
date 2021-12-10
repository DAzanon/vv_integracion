package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TestAddRSys {
    @Mock private static AuthDAO authDAO;
    @Mock private static GenericDAO genericDAO;

    @Test
    public void Test_invalidUser_invalidObject () throws Exception{
        genericDAO = mock(GenericDAO.class);
        authDAO = mock(AuthDAO.class);

        User invalidUser = new User("","","","",new ArrayList<Object>());
        Mockito.when(authDAO.getAuthData(invalidUser.getId())).thenReturn(null);

        Object invalidObject = null;
        Mockito.when(genericDAO.updateSomeData(invalidUser, invalidObject)).thenReturn(false);

        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);

        SystemManager systemManager = new SystemManager(authDAO, genericDAO);
        assertThrows(SystemManagerException.class, () -> systemManager.addRemoteSystem(invalidUser.getId(),invalidObject));

        ordered.verify(authDAO, times(1)).getAuthData(invalidUser.getId());
        ordered.verify(genericDAO, times(1)).updateSomeData(invalidUser,invalidObject);
    }

    @Test
    public void Test_validUser_validObject_noPermission () throws Exception{
        genericDAO = mock(GenericDAO.class);
        authDAO = mock(AuthDAO.class);

        User validUser = new User("1","Usuario","Ejemplo","ejemplo",new ArrayList<Object>());
        when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);

        Object validObject = 3;
        when(genericDAO.updateSomeData(validUser, validObject)).thenThrow(javax.naming.OperationNotSupportedException.class);

        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);

        SystemManager systemManager = new SystemManager(authDAO, genericDAO);
        assertThrows(SystemManagerException.class, () -> systemManager.addRemoteSystem(validUser.getId(),validObject));

        ordered.verify(authDAO, times(1)).getAuthData(validUser.getId());
        ordered.verify(genericDAO, times(1)).updateSomeData(validUser, validObject);
    }

    @Test
    public void Test_validUser_validObject_withPermission () throws Exception{
        genericDAO = mock(GenericDAO.class);
        authDAO = mock(AuthDAO.class);

        User validUser = new User("1","Usuario","Ejemplo","ejemplo",new ArrayList<Object>(Arrays.asList(1, 2)));
        when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);

        Object validObject = 3;
        when(genericDAO.updateSomeData(validUser, validObject)).thenReturn(true);

        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);

        SystemManager systemManager = new SystemManager(authDAO, genericDAO);

        //We can't assert anything since it's a void method, we'll just verify the execution order
        ordered.verify(authDAO, times(1)).getAuthData(validUser.getId());
        ordered.verify(genericDAO, times(1)).updateSomeData(validUser, validObject);
    }
}
