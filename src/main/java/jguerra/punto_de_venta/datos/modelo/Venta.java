package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Venta {
	
	private int numero;
	private String nitCliente;
	private int idSucursal;
	private LocalDate fecha;
	private BigDecimal total;
	
	public Venta(final int numero, final String nitCliente, final int idSucursal, 
			final LocalDate fecha, final BigDecimal total) {
		setNumero(numero);
		setNitCliente(nitCliente);
		setIdSucursal(idSucursal);
		setFecha(fecha);
		setTotal(total);
	}

	
	public Venta(final String nitCliente, final int idSucursal, 
			final LocalDate fecha, final BigDecimal total) {
		this(0,nitCliente,idSucursal,fecha,total);
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(final int numero) {
		if(numero < 0)
			throw new IllegalArgumentException("El número de la venta no puede ser negativo");
		this.numero = numero;
	}
	
	public String getNitCliente() {
		return nitCliente;
	}
	
	public void setNitCliente(final String nitCliente) {
		assert nitCliente != null;
		if(nitCliente.isEmpty())
			throw new IllegalArgumentException("El NIT del cliente no puede quedar vacío");
		this.nitCliente = nitCliente;
	}
	
	public int getIdSucursal() {
		return idSucursal;
	}
	
	public void setIdSucursal(final int idSucursal) {
		if(idSucursal < 0)
			throw new IllegalArgumentException("El ID de la sucursal no puede ser negativo");
		this.idSucursal = idSucursal;
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
		Venta other = (Venta) obj;
		if (numero != other.numero)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "#" + numero + " " + fecha + " " + total;
	}

}
