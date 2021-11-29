package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestDeleteRemoteSystem{

    /**
     * RELLENAR POR EL ALUMNO
     */
    @InjectMocks
    private SystemManager systemManager;

    @Mock
    private AuthDAO authDAO;

    @Mock
    private GenericDAO genericDAO;

    @Test
    public void Test_invalidUID_invalidRID () throws Exception{
        String invalidUID = "";
        String invalidRID = "";
        User user = new User("","","","",new ArrayList<Object>());
        when(authDAO.getAuthData(invalidUID)).thenReturn(null);
        when(genericDAO.deleteSomeData(user, invalidRID)).thenReturn(null);
        assertThrows(SystemManagerException.class,()->systemManager.deleteRemoteSystem(invalidUID,invalidRID));
    }

    @Test
    public void Test_invalidUID_validRID () throws Exception{
        String invalidUID = "";
        String validRID = "1";
        User user = new User("","","","",new ArrayList<Object>());
        when(authDAO.getAuthData(invalidUID)).thenReturn(null);
        when(genericDAO.deleteSomeData(user, validRID)).thenReturn(null);
        assertThrows(SystemManagerException.class,()->systemManager.deleteRemoteSystem(invalidUID,validRID));
    }

    @Test
    public void Test_validUID_invalidRID () throws Exception{
        String validUID = "";
        String invalidRID = "";
        User user = new User("","","","",new ArrayList<Object>());
        when(authDAO.getAuthData(validUID)).thenReturn(null);
        when(genericDAO.deleteSomeData(user, invalidRID)).thenReturn(null);
        assertThrows(SystemManagerException.class,()->systemManager.deleteRemoteSystem(validUID,invalidRID));
    }
}

