package jguerra.punto_de_venta.datos.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProductoTest {
	
	@Test
	public void equalsProducto() {
		
		Producto p1 = new Producto(1, "Arroz", "El Molinero");
		Producto p2 = new Producto(1, "Frijol", "Macarena");
		Producto p3 = new Producto(2, "Arroz", "El Molinero");
		Producto p4 = new Producto(3, "Arroz", "Macarena");
		
		assertEquals(p1, p2);
		assertEquals(p1, p3);
		assertNotEquals(p1, p4);
		
	}

}
