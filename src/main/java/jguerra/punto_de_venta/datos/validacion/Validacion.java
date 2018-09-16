package jguerra.punto_de_venta.datos.validacion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validacion {
	
	public static final Pattern TELEFONO = Pattern.compile("^\\d{8}$");
	
	public static final Pattern NIT = Pattern.compile("^(\\d+)-([\\dk])$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * Metodo para saber si un telefono es valido o no
	 * @param telefono cadena con el telefono a validar
	 * @return true si el telefono es valido(la cadena contiene exactamente 8 digitos),
	 * false si no es valido
	 */
	public static boolean validarTelefono(final String telefono) {
		assert telefono != null;
		return TELEFONO.matcher(telefono).find();
	}
	
	/**
	 * Metodo para saber si un NIT es valido o no
	 * @param nit cadena con el NIT a validar, no debe ser null
	 * @return true si el NIT es valido, false si no
	 */
	public static boolean validarNit(final String nit) {
		assert nit != null;
		
		Matcher matcher = NIT.matcher(nit);
		
		if(matcher.find()) {
			String leftPart = matcher.group(1);// parte izquierda del guion(sólo números)
			String rightPart = matcher.group(2);// parte derecha del guion(número o letra 'k')
			
			int digitoVerificador = rightPart.equalsIgnoreCase("k") ? 10 : Integer.parseInt(rightPart);
			
			long leftNum = Long.parseLong(leftPart);
			int suma = 0, pos = 2;
			while(leftNum > 9) {
				int digito = (int) (leftNum % 10);
				suma += digito * pos;
				leftNum /= 10;
				++pos;
			}
			suma += leftNum * pos;
			
			int resultado = (11 - suma % 11) % 11;
			
			return resultado == digitoVerificador && resultado != 0;
		}
		
		return false;
	}

}
