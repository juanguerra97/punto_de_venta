package jguerra.punto_de_venta.datos.modelo;

public final class Existencia {
	
	private Presentacion presentacion;
	private Sucursal sucursal;
	private int cantidad;
	
	public Existencia(final Presentacion presentacion, 
			final Sucursal sucursal, final int cantidad) {
		setPresentacion(presentacion);
		setSucursal(sucursal);
		setCantidad(cantidad);
	}

	public Presentacion getPresentacion() {
		return presentacion;
	}
	
	public void setPresentacion(final Presentacion presentacion) {
		assert presentacion != null;
		this.presentacion = presentacion;
	}
	
	public Sucursal getSucursal() {
		return sucursal;
	}
	
	public void setSucursal(final Sucursal sucursal) {
		assert sucursal != null;
		this.sucursal = sucursal;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(final int cantidad) {
		if(cantidad < 0)
			throw new IllegalArgumentException(
					"La cantidad no puede ser negativa");
		this.cantidad = cantidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + presentacion.getId();
		result = prime * result + sucursal.getId();
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
		if (!presentacion.equals(other.presentacion))
			return false;
		if (!sucursal.equals(other.sucursal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Integer.toString(cantidad);
	}

}
