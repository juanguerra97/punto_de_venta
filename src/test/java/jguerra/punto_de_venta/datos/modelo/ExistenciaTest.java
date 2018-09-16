package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExistenciaTest {
	
	@Test
	public void equalsExistencia() {
		
		Existencia x1 = new Existencia(1000, 5000, 5);
		Existencia x2 = new Existencia(1000, 5000, 8);
		Existencia x3 = new Existencia(1000, 5078, 5);
		
		assertEquals(x1, x2);
		assertNotEquals(x1, x3);
		
	}

}
