package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestDeleteRemoteSystem{

    @InjectMocks
    private static SystemManager systemManager;

    @Mock
    private static AuthDAO authDAO;

    @Mock
    private static GenericDAO genericDAO;

    @Test
    public void Test_invalidUser_invalidRID () throws Exception{
        User invalidUser = new User("","","","",new ArrayList<Object>());
        String invalidRID = "";
        when(authDAO.getAuthData(invalidUser.getId())).thenReturn(null);
        when(genericDAO.deleteSomeData(invalidUser, invalidRID)).thenReturn(false);

        assertThrows(SystemManagerException.class, () -> systemManager.deleteRemoteSystem(invalidUser.getId(),invalidRID));
        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);
        ordered.verify(authDAO).getAuthData(invalidUser.getId());
        ordered.verify(genericDAO).deleteSomeData(invalidUser, "where id=" + invalidRID);
    }

    @Test
    public void Test_invalidUser_validRID () throws Exception{
        User invalidUser = new User("","","","",new ArrayList<Object>());
        String validRID = "";
        when(authDAO.getAuthData(invalidUser.getId())).thenReturn(null);
        when(genericDAO.deleteSomeData(invalidUser, validRID)).thenReturn(false);

        assertThrows(SystemManagerException.class, () -> systemManager.deleteRemoteSystem(invalidUser.getId(),validRID));
        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);
        ordered.verify(authDAO).getAuthData(invalidUser.getId());
        ordered.verify(genericDAO).deleteSomeData(invalidUser, "where id=" + validRID);
    }

    @Test
    public void Test_validUser_invalidRID () throws Exception{
        User validUser = new User("","","","",new ArrayList<Object>());
        String invalidRID = "";
        when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
        when(genericDAO.deleteSomeData(validUser, invalidRID)).thenReturn(false);

        assertThrows(SystemManagerException.class, () -> systemManager.deleteRemoteSystem(validUser.getId(),invalidRID));
        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);
        ordered.verify(authDAO).getAuthData(validUser.getId());
        ordered.verify(genericDAO).deleteSomeData(validUser, "where id=" + invalidRID);
    }

    @Test
    public void Test_validUser_validRID_withPermission () throws Exception{
        User validUser = new User("1","Usuario","Ejemplo","ejemplo",new ArrayList<Object>());
        String validRID = "2";
        when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
        when(genericDAO.deleteSomeData(validUser, validRID)).thenThrow(javax.naming.OperationNotSupportedException.class);

        assertThrows(SystemManagerException.class, () -> systemManager.deleteRemoteSystem(validUser.getId(),validRID));
        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);
        ordered.verify(authDAO).getAuthData(validUser.getId());
        ordered.verify(genericDAO).deleteSomeData(validUser, "where id=" + validRID);
    }

    @Test
    public void Test_validUser_validRID_noPermission () throws Exception{
        User validUser = new User("1","Usuario","Ejemplo","ejemplo",new ArrayList<Object>(Arrays.asList(1, 2)));
        String validRID = "2";
        when(authDAO.getAuthData(validUser.getId())).thenReturn(validUser);
        when(genericDAO.deleteSomeData(validUser, validRID)).thenReturn(true);

        //We can't assert anything since it's a void method, we'll just verify the execution order
        InOrder ordered = Mockito.inOrder(authDAO, genericDAO);
        ordered.verify(authDAO).getAuthData(validUser.getId());
        ordered.verify(genericDAO).deleteSomeData(validUser, "where id=" + validRID);
    }
}
