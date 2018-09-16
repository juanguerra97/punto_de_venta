package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ClienteTest {
	
	@Test
	public void equalsCliente() {
		
		Cliente c1 = new Cliente("2630970-K", "Mariano", "Gálvez", "12345678");
		Cliente c2 = new Cliente("278126-3", "Mariano", "Gálvez", "12345678");
		Cliente c3 = new Cliente("2630970-K", "Miguel", "Cervantes", "12348778");
		
		assertEquals(c1, c3);
		assertNotEquals(c2, c1);
	}

}
