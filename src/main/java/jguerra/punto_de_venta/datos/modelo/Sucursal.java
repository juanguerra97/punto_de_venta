package jguerra.punto_de_venta.datos.modelo;

public final class Sucursal {

	private int id;
	private String nombre;
	
	public Sucursal(final int id,final String nombre) {
		setId(id);
		setNombre(nombre);
	}
	
	public Sucursal(final String nombre) {
		this(0,nombre);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(final int id) {
		if(id < 0)
			throw new IllegalArgumentException("El ID no puede ser negativo");
		this.id = id;
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
		Sucursal other = (Sucursal) obj;
		if (id != other.id)
			return nombre.equalsIgnoreCase(other.nombre);
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
}
