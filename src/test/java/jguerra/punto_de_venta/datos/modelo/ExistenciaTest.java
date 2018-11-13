package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class ExistenciaTest {
	
	@Test
	public void equalsExistencia() {
		
		Producto prod1 = new Producto(5000,"COCA","COCA-COLA");
		Producto prod2 = new Producto(5078,"PEPSI","PEPSICO");
		
		Presentacion p1 = new Presentacion(1000,prod1,"LATA",
				new BigDecimal("3"),new BigDecimal("4"));
		Presentacion p2 = new Presentacion(1000,prod2,"LATA",
				new BigDecimal("3"),new BigDecimal("4"));
		
		Sucursal s1 = new Sucursal(5000,"LA DEMOCRACIA");
		Sucursal s2 = new Sucursal(5078,"CUESTA BLANCA");
		
		Existencia x1 = new Existencia(p1, s1, 5);
		Existencia x2 = new Existencia(p1, s1, 8);
		Existencia x3 = new Existencia(p2, s2, 5);
		
		assertEquals(x1, x2);
		assertNotEquals(x1, x3);
		
	}

}
