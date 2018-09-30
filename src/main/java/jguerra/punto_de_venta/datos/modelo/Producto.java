package jguerra.punto_de_venta.datos.modelo;

public final class Producto {
	
	private int id;
	private String nombre;
	private String marca;
	
	public Producto(final int id, final String nombre, final String marca) {
		setId(id);
		setNombre(nombre);
		setMarca(marca);
	}
	
	public Producto(final String nombre, final String marca) {
		this(0,nombre,marca);
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
	
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(final String marca) {
		assert marca != null;
		if(marca.isEmpty())
			throw new IllegalArgumentException("La marca no puede quedar vacía");
		this.marca = marca;
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
		Producto other = (Producto) obj;
		if (id != other.id)
			return nombre.equalsIgnoreCase(other.nombre) && marca.equalsIgnoreCase(other.marca);
		return true;
	}

	@Override
	public String toString() {
		return nombre + " " + marca;
	}

}
