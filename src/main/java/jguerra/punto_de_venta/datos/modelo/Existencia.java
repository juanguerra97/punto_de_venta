package jguerra.punto_de_venta.datos.modelo;

public final class Existencia {
	
	private int idPresentacion;
	private int idSucursal;
	private int cantidad;
	
	public Existencia(final int idPresentacion, final int idSucursal, final int cantidad) {
		setIdPresentacion(idPresentacion);
		setIdSucursal(idSucursal);
		setCantidad(cantidad);
	}

	public int getIdPresentacion() {
		return idPresentacion;
	}
	
	public void setIdPresentacion(final int idPresentacion) {
		if(idPresentacion < 0)
			throw new IllegalArgumentException("El ID de la presentacion no puede ser negativo");
		this.idPresentacion = idPresentacion;
	}
	
	public int getIdSucursal() {
		return idSucursal;
	}
	
	public void setIdSucursal(final int idSucursal) {
		if(idSucursal < 0)
			throw new IllegalArgumentException("El ID de la sucursal no puede ser negativo");
		this.idSucursal = idSucursal;
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
		result = prime * result + idPresentacion;
		result = prime * result + idSucursal;
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
		Existencia other = (Existencia) obj;
		if (idPresentacion != other.idPresentacion)
			return false;
		if (idSucursal != other.idSucursal)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Integer.toString(cantidad);
	}

}
