package pe.edu.pucp.salud360.mutex;

import org.springframework.stereotype.Component;

@Component
public class MutexRegistro {
    public static final Object LOCK = new Object();
}
