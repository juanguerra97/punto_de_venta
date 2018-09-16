package jguerra.punto_de_venta.datos.modelo;

import java.math.BigDecimal;

public final class DetalleCompra {
	
	private int numeroCompra;
	private String nombreProducto;
	private String marcaProducto;
	private String presentacionProducto;
	private String nombreSucursal;
	private BigDecimal costo;
	private int cantidad;
	
	public DetalleCompra(final int numeroCompra, final String nombreProducto, 
			final String marcaProducto, final String presentacionProducto, 
			final String nombreSucursal, final BigDecimal costo, 
			final int cantidad) {
		setNumeroCompra(numeroCompra);
		setNombreProducto(nombreProducto);
		setMarcaProducto(marcaProducto);
		setPresentacionProducto(presentacionProducto);
		setNombreSucursal(nombreSucursal);
		setCosto(costo);
		setCantidad(cantidad);
	}
	
	public DetalleCompra(final String nombreProducto, final String marcaProducto,
			final String presentacionProducto, final String nombreSucursal, 
			final BigDecimal costo, final int cantidad) {
		this(0,nombreProducto,marcaProducto,presentacionProducto,nombreSucursal,costo,cantidad);
	}

	public int getNumeroCompra() {
		return numeroCompra;
	}
	
	public void setNumeroCompra(final int numeroCompra) {
		if(numeroCompra < 0)
			throw new IllegalArgumentException("El numero de la compra no puede ser negativo");
		this.numeroCompra = numeroCompra;
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
	
	public String getNombreSucursal() {
		return nombreSucursal;
	}
	
	public void setNombreSucursal(final String nombreSucursal) {
		assert nombreSucursal != null;
		if(nombreSucursal.isEmpty())
			throw new IllegalArgumentException("El nombre de la sucursal no puede quedar vacío");
		this.nombreSucursal = nombreSucursal;
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
		result = prime * result + ((nombreSucursal == null) ? 0 : nombreSucursal.hashCode());
		result = prime * result + numeroCompra;
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
		DetalleCompra other = (DetalleCompra) obj;
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
		if (nombreSucursal == null) {
			if (other.nombreSucursal != null)
				return false;
		} else if (!nombreSucursal.equals(other.nombreSucursal))
			return false;
		if (numeroCompra != other.numeroCompra)
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
		return "#" + numeroCompra + " " + nombreProducto + " " + marcaProducto + " " 
				+ presentacionProducto + " " + nombreSucursal;
	}

}
