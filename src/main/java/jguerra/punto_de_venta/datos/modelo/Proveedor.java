package jguerra.punto_de_venta.datos.modelo;

public final class Proveedor {
	
	private int id;
	private String nombre;
	private String telefono;
	
	public Proveedor(final int id, final String nombre, final String telefono) {
		setId(id);
		setNombre(nombre);
		setTelefono(telefono);
	}
	
	public Proveedor(final String nombre, final String telefono) {
		this(0,nombre,telefono);
	}

	public int getId() {
		return id;
	}
	
	private void setId(final int id) {
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
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(final String telefono) {
		assert telefono != null;
		if(telefono.isEmpty())
			throw new IllegalArgumentException("El teléfono no puede quedar vacío");
		if(telefono.length() != 8)
			throw new IllegalArgumentException("El teléfono debe tener 8 dígitos");
		this.telefono = telefono;
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
		Proveedor other = (Proveedor) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
