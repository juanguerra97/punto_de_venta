package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Compra {

	private int numero;
	private int idProveedor;
	private LocalDate fecha;
	private BigDecimal total;
	
	public Compra(final int numero, final int idProveedor,  
			final LocalDate fecha, final BigDecimal total) {
		setNumero(numero);
		setIdProveedor(idProveedor);
		setFecha(fecha);
		setTotal(total);
	}
	
	public Compra(final int idProveedor, final LocalDate fecha, final BigDecimal total) {
		this(0,idProveedor,fecha,total);
	}

	public int getNumero() {
		return numero;
	}
	
	public void setNumero(final int numero) {
		if(numero < 0)
			throw new IllegalArgumentException("El número de la compra no puede ser negativo");
		this.numero = numero;
	}
	
	public int getIdProveedor() {
		return idProveedor;
	}
	
	public void setIdProveedor(final int idProveedor) {
		if(idProveedor < 0)
			throw new IllegalArgumentException("El ID del proveedor no puede ser negativo");
		this.idProveedor = idProveedor;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setFecha(final LocalDate fecha) {
		assert fecha != null;
		this.fecha = fecha;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(final BigDecimal total) {
		assert total != null;
		if(total.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("El total no puede ser negativo");
		this.total = total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numero;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compra other = (Compra) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "#" + numero + " " + fecha + " " + total;
	}	
	
}
