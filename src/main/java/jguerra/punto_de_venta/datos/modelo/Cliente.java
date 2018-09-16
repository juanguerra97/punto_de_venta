package jguerra.punto_de_venta.datos.modelo;

import jguerra.punto_de_venta.datos.validacion.Validacion;

public final class Cliente {
	
	private String nit;
	private String nombre;
	private String apellido;
	private String telefono;
	
	public Cliente(final String nit, final String nombre, final String apellido, final String telefono) {
		setNit(nit);
		setNombre(nombre);
		setApellido(apellido);
		setTelefono(telefono);
	}

	public String getNit() {
		return nit;
	}
	
	private void setNit(final String nit) {
		assert nit != null;
		if(nit.isEmpty())
			throw new IllegalArgumentException("El NIT no puede quedar vacío");
		if(!Validacion.validarNit(nit))
			throw new IllegalArgumentException("El NIT es inválido");
		this.nit = nit;
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
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(final String apellido) {
		assert apellido != null;
		if(apellido.isEmpty())
			throw new IllegalArgumentException("El apellido no puede quedar vacío");
		this.apellido = apellido;
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
		if(!Validacion.validarTelefono(telefono))
			throw new IllegalArgumentException("El teléfono sólo puede contener dígitos");
		this.telefono = telefono;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
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
		Cliente other = (Cliente) obj;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equalsIgnoreCase(other.nit))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre + " " + apellido;
	}

}
