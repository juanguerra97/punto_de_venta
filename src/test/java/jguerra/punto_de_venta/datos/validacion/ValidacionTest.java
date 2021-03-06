package jguerra.punto_de_venta.datos.validacion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidacionTest {
	
	@Test
	public void validarTelefono() {
		// telefonos validos
		assertTrue(Validacion.validarTelefono("12345678"));
		assertTrue(Validacion.validarTelefono("88659721"));
		// telefonos invalidos
		assertFalse(Validacion.validarTelefono(""));// cadena vacia
		assertFalse(Validacion.validarTelefono("886597215"));// contiene 9 digitos
		assertFalse(Validacion.validarTelefono("9866962M"));// contiene una letra
	}
	
	@Test
	public void validarNit() {
		
		// NITs validos
		assertTrue(Validacion.validarNit("2630970-K"));
		assertTrue(Validacion.validarNit("278126-3"));
		assertTrue(Validacion.validarNit("3943697-7"));
		assertTrue(Validacion.validarNit("1510972-0"));
		
		// NITs invalidos
		assertFalse(Validacion.validarNit(""));// cadena vacia
		assertFalse(Validacion.validarNit("-"));// deben haber digitos alrededor del guion
//		assertFalse(Validacion.validarNit("9-8"));// digito validador no coincide con resultado de operacion matematica
		assertFalse(Validacion.validarNit("-7"));// debe haber algo en el lado izquierdo del guion
//		assertFalse(Validacion.validarNit("1234567-"));// debe haber algo en el lado derecho del guion
//		assertFalse(Validacion.validarNit(" 1234567-7 "));// no debe tener espacios al inicio ni al final
//		assertFalse(Validacion.validarNit("1234567 - 7"));// no deben haber espacios alrededor del guion
//		assertFalse(Validacion.validarNit("1234567-m"));// m no es valido como digito verificador
//		assertFalse(Validacion.validarNit("123456K-8"));// en el lado izquierdo del guion solo se permiten numeros
//		assertFalse(Validacion.validarNit("12345678-7"));// no es valido al hacer la operacion
	
	}

	@Test
	public void validarMoneda() {
		assertTrue(Validacion.validarMoneda("50"));
		assertTrue(Validacion.validarMoneda("80.5"));
		assertTrue(Validacion.validarMoneda("10.75"));
		assertTrue(Validacion.validarMoneda(".90"));
		assertTrue(Validacion.validarMoneda(".5"));
		
		assertFalse(Validacion.validarMoneda(""));
		assertFalse(Validacion.validarMoneda("hola"));
		assertFalse(Validacion.validarMoneda("."));
		assertFalse(Validacion.validarMoneda("80."));
		assertFalse(Validacion.validarMoneda("15.125"));
	}
	
}
