package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Venta {
	
	private int numero;
	private Cliente cliente;
	private Sucursal sucursal;
	private LocalDate fecha;
	private BigDecimal total;
	
	public Venta(final int numero, final Cliente cliente, 
			final Sucursal sucursal, final LocalDate fecha, 
			final BigDecimal total) {
		setNumero(numero);
		setCliente(cliente);
		setSucursal(sucursal);
		setFecha(fecha);
		setTotal(total);
	}

	
	public Venta(final Cliente cliente, final Sucursal sucursal, 
			final LocalDate fecha, final BigDecimal total) {
		this(0,cliente,sucursal,fecha,total);
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(final int numero) {
		if(numero < 0)
			throw new IllegalArgumentException(
					"El número de la venta no puede ser negativo");
		this.numero = numero;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(final Cliente cliente) {
		assert cliente != null;
		this.cliente = cliente;
	}
	
	public Sucursal getSucursal() {
		return sucursal;
	}
	
	public void setSucursal(final Sucursal sucursal) {
		assert sucursal != null;
		this.sucursal = sucursal;
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
			throw new IllegalArgumentException(
					"El total no puede ser negativo");
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
