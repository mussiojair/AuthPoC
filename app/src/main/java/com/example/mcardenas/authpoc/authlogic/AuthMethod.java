package com.example.mcardenas.authpoc.authlogic;

import java.util.Map;

/**
 * Created by mcardenas on 05/05/2017.
 */

public interface AuthMethod {

    /**
     * Inicia el proceso de login definido en la clase
     *
     * @author: Mussio Cárdenas
     *
     */
    void login() throws CredentialsException;






    /**
     * Inicia el proceso de login las credenciales definidas en el parámetro del método
     *
     * @author: Mussio Cárdenas
     * @param: params
     */
    void login(String url, Map params, AuthListener listener) throws CredentialsException;





    /**
     * Guarda permanentemente el token generado por la acción de login.
     * @author: Mussio Cárdenas
     */
    void storeToken();





    /**
     * Elimina el token de acceso y sale de la sesión
     *
     * @author: Mussio Cárdenas
     */
    void clearToken();



    /**
     * Obtiene el token almacenado en el dispositivo
     *
     * @author: Mussio Cárdenas
     */
    String getToken();
}
