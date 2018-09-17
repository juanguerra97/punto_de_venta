package jguerra.punto_de_venta.datos.dao.oracle.sequences;

/**
 * Interfaz para representar una secuencia de oracle
 * @author juang
 *
 */
@FunctionalInterface
public interface Sequence {
	
	/**
	 * Metodo para obtener el siguiente valor de la secuencia
	 * @return siguiente valor de la secuencia
	 */
	int next();

}
