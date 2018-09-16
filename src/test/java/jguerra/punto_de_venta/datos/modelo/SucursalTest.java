package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class SucursalTest {
	
	@Test
	public void equalsSucursal() {
		
		Sucursal s1 = new Sucursal(3000, "CUESTA BLANCA");
		Sucursal s2 = new Sucursal(3000, "LA DEMOCRACIA");
		Sucursal s3 = new Sucursal(3090, "CUESTA BLANCA");
		Sucursal s4 = new Sucursal(4700, "MONT BLANC");
		
		assertEquals(s1, s2);
		assertEquals(s1, s3);
		assertNotEquals(s1, s4);
		
		
	}

}
