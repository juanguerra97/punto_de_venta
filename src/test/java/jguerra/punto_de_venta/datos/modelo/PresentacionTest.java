package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class PresentacionTest {
	
	@Test
	public void equalsPresentacion() {
		
		Producto prod1 = new Producto(2,"COCA","COCA-COLA");
		Producto prod2 = new Producto(20,"PEPSI","PEPSICO");
		
		Presentacion p1 = new Presentacion(1, prod1, "BOTELLA 500ML", BigDecimal.valueOf(16), BigDecimal.valueOf(13.50));
		Presentacion p2 = new Presentacion(1, prod1, "LATA", BigDecimal.valueOf(14), BigDecimal.valueOf(10));
		Presentacion p3 = new Presentacion(2, prod1, "BOTELLA 500ML", BigDecimal.valueOf(16), BigDecimal.valueOf(13.50));
		Presentacion p4 = new Presentacion(5, prod2, "BOTELLA 500ML", BigDecimal.valueOf(16), BigDecimal.valueOf(13.50));
		
		assertEquals(p1, p2);
		assertEquals(p1, p3);
		assertNotEquals(p1, p4);
		
	}

}
