package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;

public final class Presentacion {
	
	private int id;
	private Producto producto;
	private String nombre;
	private BigDecimal precio;
	private BigDecimal costo;
	
	public Presentacion(final int id, final Producto producto, 
			final String nombre, final BigDecimal precio, 
			final BigDecimal costo) {
		setId(id);
		setProducto(producto);
		setNombre(nombre);
		setPrecio(precio);
		setCosto(costo);
	}
	
	public Presentacion(final Producto producto, final String nombre, 
			final BigDecimal precio, final BigDecimal costo) {
		this(0,producto,nombre,precio,costo);
	}

	public int getId() {
		return id;
	}
	
	public void setId(final int id) {
		if(id < 0)
			throw new IllegalArgumentException("El ID no puede ser negativo");
		this.id = id;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto(final Producto producto) {
		assert producto != null;
		this.producto = producto;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(final String nombre) {
		assert nombre != null;
		if(nombre.isEmpty())
			throw new IllegalArgumentException("El nombre no puede quedar vacío");
		this.nombre = nombre;
	}
	
	public BigDecimal getPrecio() {
		return precio;
	}
	
	public void setPrecio(final BigDecimal precio) {
		assert precio != null;
		if(precio.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("El precio no puede ser negativo");
		this.precio = precio;
	}
	
	public BigDecimal getCosto() {
		return costo;
	}
	
	public void setCosto(final BigDecimal costo) {
		assert costo != null;
		if(costo.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("El costo no puede ser negativo");
		this.costo = costo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Presentacion other = (Presentacion) obj;
		if (id != other.id)
			return nombre.equalsIgnoreCase(other.nombre) && producto == other.producto;
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
}
