package jguerra.punto_de_venta.gui.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Producto;

public final class FiltradorProductos {
	
	public static final Pattern ID = Pattern.compile("i:\\s*(\\d+)\\s*,?");
	public static final Pattern MARCA = Pattern.compile("m:\\s*([^,]+)");
	public static final Pattern NOMBRE = Pattern.compile("n:\\s*([^,]+)");
	
	public static List<Producto> filtrar(final String filtro){
		assert filtro != null;
		List<Producto> productos = new LinkedList<>();
		if(filtro.length() < 3)
			return productos;
		Matcher matcherId = ID.matcher(filtro);
		if(matcherId.find()) {
			int id = Integer.parseInt(matcherId.group(1));
			DAOManager.instance().producto().ifPresent(dao -> {
				dao.selectById(id).ifPresent(prod -> productos.add(prod));
			});
		} else {
			Matcher matcherMarca = MARCA.matcher(filtro);
			Matcher matcherNombre = NOMBRE.matcher(filtro);
			if(matcherMarca.find()) {
				final String marca = matcherMarca.group(1).toUpperCase();
				if(matcherNombre.find()) {
					final String nombre = matcherNombre.group(1).toUpperCase();
					DAOManager.instance().producto().ifPresent(dao -> {
						dao.selectByNombreMarca(nombre, marca).ifPresent(prod -> {
							productos.add(prod);
						});
					});
				}else {
					DAOManager.instance().producto().ifPresent(dao -> {
						productos.addAll(dao.selectAllByMarca(marca));
					});
				}
			} else if (matcherNombre.find()) {
				final String nombre = matcherNombre.group(1).toUpperCase();
				DAOManager.instance().producto().ifPresent(dao -> {
					productos.addAll(dao.selectAllByNombre(nombre));
				});
			}
		}
		return productos;
	}
	
}
