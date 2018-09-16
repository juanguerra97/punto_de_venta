package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;

public final class DetalleVenta {
	
	private int numeroVenta;
	private String nombreProducto;
	private String marcaProducto;
	private String presentacionProducto;
	private BigDecimal costo;
	private BigDecimal precio;
	private int cantidad;
	
	public DetalleVenta(final int numeroVenta, final String nombreProducto, final String marcaProducto, 
			final String presentacionProducto, final BigDecimal costo, final BigDecimal precio, 
			final int cantidad) {
		setNumeroVenta(numeroVenta);
		setNombreProducto(nombreProducto);
		setMarcaProducto(marcaProducto);
		setPresentacionProducto(presentacionProducto);
		setCosto(costo);
		setPrecio(precio);
		setCantidad(cantidad);
	}
	
	public DetalleVenta(final String nombreProducto, final String marcaProducto, 
			final String presentacionProducto, final BigDecimal costo, final BigDecimal precio, 
			final int cantidad) {
		this(0,nombreProducto,marcaProducto,presentacionProducto,costo,precio,cantidad);
	}

	public int getNumeroVenta() {
		return numeroVenta;
	}
	
	public void setNumeroVenta(final int numeroVenta) {
		if(numeroVenta < 0)
			throw new IllegalArgumentException("El numero de la venta no puede ser negativo");
		this.numeroVenta = numeroVenta;
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	public void setNombreProducto(final String nombreProducto) {
		assert nombreProducto != null;
		if(nombreProducto.isEmpty())
			throw new IllegalArgumentException("El nombre del producto no puede quedar vacío");
		this.nombreProducto = nombreProducto;
	}
	
	public String getMarcaProducto() {
		return marcaProducto;
	}
	
	public void setMarcaProducto(final String marcaProducto) {
		assert marcaProducto != null;
		if(marcaProducto.isEmpty())
			throw new IllegalArgumentException("La marca no puede quedar vacía");
		this.marcaProducto = marcaProducto;
	}
	
	public String getPresentacionProducto() {
		return presentacionProducto;
	}
	
	public void setPresentacionProducto(final String presentacionProducto) {
		assert presentacionProducto != null;
		if(presentacionProducto.isEmpty())
			throw new IllegalArgumentException("La presentación no puede quedar vacía");
		this.presentacionProducto = presentacionProducto;
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
	
	public BigDecimal getPrecio() {
		return precio;
	}
	
	public void setPrecio(final BigDecimal precio) {
		assert precio != null;
		if(precio.compareTo(BigDecimal.ZERO) < 0)
			throw new IllegalArgumentException("El precio no puede ser negativo");
		this.precio = precio;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(final int cantidad) {
		if(cantidad < 0)
			throw new IllegalArgumentException("La cantidad no puede ser negativa");
		this.cantidad = cantidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((marcaProducto == null) ? 0 : marcaProducto.hashCode());
		result = prime * result + ((nombreProducto == null) ? 0 : nombreProducto.hashCode());
		result = prime * result + numeroVenta;
		result = prime * result + ((presentacionProducto == null) ? 0 : presentacionProducto.hashCode());
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
		DetalleVenta other = (DetalleVenta) obj;
		if (marcaProducto == null) {
			if (other.marcaProducto != null)
				return false;
		} else if (!marcaProducto.equals(other.marcaProducto))
			return false;
		if (nombreProducto == null) {
			if (other.nombreProducto != null)
				return false;
		} else if (!nombreProducto.equals(other.nombreProducto))
			return false;
		if (numeroVenta != other.numeroVenta)
			return false;
		if (presentacionProducto == null) {
			if (other.presentacionProducto != null)
				return false;
		} else if (!presentacionProducto.equals(other.presentacionProducto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "#" + numeroVenta + " " + presentacionProducto +" " 
				+ nombreProducto + " " + marcaProducto;
	}
	
}
