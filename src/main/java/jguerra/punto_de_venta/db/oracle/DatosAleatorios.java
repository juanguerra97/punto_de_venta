package jguerra.punto_de_venta.db.oracle;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;

import jguerra.punto_de_venta.datos.dao.oracle.DAOManager;
import jguerra.punto_de_venta.datos.modelo.Cliente;
import jguerra.punto_de_venta.datos.modelo.Producto;
import jguerra.punto_de_venta.datos.modelo.Proveedor;
import jguerra.punto_de_venta.datos.modelo.Sucursal;

public class DatosAleatorios {
	
	public static final SecureRandom RAND = new SecureRandom();
	
	public static final int NUM_SUCURSALES = 50;
	public static final int NUM_CLIENTES = 60;
	public static final int NUM_PROVEEDORES = 60;
	//public static final int NUM_PRODUCTOS = 150;
	
	public static final List<Producto> productos = new LinkedList<>();
	static {
		productos.add(new Producto("HELADO","HOLANDA"));
		productos.add(new Producto("MAYONESA","HELLMANN'S"));
		productos.add(new Producto("SAZONADOR DE POLLO","KNORR"));
		productos.add(new Producto("CREMA PARA LA PIEL","DOVE"));
		productos.add(new Producto("DESODORANTE","AXE"));
		productos.add(new Producto("HELADO","CORNETTO"));
		productos.add(new Producto("HELADO","MAGNUM"));
		productos.add(new Producto("TE FRIO","LIPTON"));
		productos.add(new Producto("DESODORANTE","REXONA"));
		productos.add(new Producto("CREMA PARA LA PIEL","POND'S"));
		productos.add(new Producto("LECHE","NIDO"));
		productos.add(new Producto("LECHE","FOREMOST"));
		productos.add(new Producto("NACHOS","DIANA"));
		productos.add(new Producto("JALAPEÑOS","DIANA"));
		productos.add(new Producto("TORTILLITA","SEÑORIAL"));
		productos.add(new Producto("GALLETA PICNIC","DIANA"));
		productos.add(new Producto("CRECIMAX","INCAPARINA"));
		productos.add(new Producto("INCAPARINA","INCAPARINA"));
		productos.add(new Producto("AVENA MOSH","QUAKER"));
		productos.add(new Producto("PASTILLAS FRUTA","DIANA"));
		productos.add(new Producto("CEREZA","DIANA"));
		productos.add(new Producto("CANDY MAÍZ","DIANA"));
		productos.add(new Producto("ALBOROTO","DIANA"));
		productos.add(new Producto("CARAMELO DE LECHE","DIANA"));
		productos.add(new Producto("QUESITOS","DIANA"));
		productos.add(new Producto("MANÍ SALADO","DIANA"));
		productos.add(new Producto("YUCATEKAS","DIANA"));
		productos.add(new Producto("POP CORN","SEÑORIAL"));
		productos.add(new Producto("RUFITAS","SEÑORIAL"));
		productos.add(new Producto("CHOBIX","SEÑORIAL"));
		productos.add(new Producto("QUESIFRITOS","SEÑORIAL"));
		productos.add(new Producto("CHICHARRONES","SEÑORIAL"));
		productos.add(new Producto("SALSA DE TOMATE","DEL MONTE"));
		productos.add(new Producto("FRIJOL","DUCAL"));
		productos.add(new Producto("JUGO","DEL MONTE"));
		productos.add(new Producto("JUGO","TAMPICO"));
		productos.add(new Producto("CEREAL CHOCO-CHIWI","GRAN DÍA"));
		productos.add(new Producto("CEREAL COCOA FLAKES","GRAN DÍA"));
		productos.add(new Producto("CEREAL CORN FLAKES","GRAN DÍA"));
		productos.add(new Producto("CEREAL CHOCO KRISPIS","KELLOG'S"));
		productos.add(new Producto("CEREAL CORN FLAKES","KELLOG'S"));
		productos.add(new Producto("MASECA","MASECA"));
		productos.add(new Producto("COCA-COLA","COCA-COLA"));
		productos.add(new Producto("SPRITE","COCA-COLA"));
		productos.add(new Producto("FANTA","COCA-COLA"));
		productos.add(new Producto("CIEL","COCA-COLA"));
		productos.add(new Producto("POWERADE","COCA-COLA"));
		productos.add(new Producto("CAFÉ NESCAFÉ","NESTLÉ"));
		productos.add(new Producto("CEREAL CORN FLAKES","NESTLÉ"));
		productos.add(new Producto("CEREAL FITNESS","NESTLÉ"));
		productos.add(new Producto("JUGO","DEL FRUTAL"));
		productos.add(new Producto("JUGO FRUTA FRESCA","PETTIT"));
		productos.add(new Producto("PEPSI","PEPSICO"));
		productos.add(new Producto("7 UP","PEPSICO"));
		productos.add(new Producto("ARROZ","MACARENA"));
		productos.add(new Producto("FRIJOL","MACARENA"));
		productos.add(new Producto("ARROZ","EL MOLINERO"));
		productos.add(new Producto("JUGO DE NARANJA","DE LA GRANJA"));
		productos.add(new Producto("LIMPIADOR LÍQUIDO","PINOL"));
		productos.add(new Producto("LIMPIADOR LÍQUIDO","FABULOSO"));
		productos.add(new Producto("AROMATIZANTE AMBIENTAL","GLADE"));
		productos.add(new Producto("HARINA PARA HOT CAKES","GAMESA"));
		productos.add(new Producto("MAYONESA","MCCORMICK"));
		productos.add(new Producto("LECHE CONDENSADA","NESTLÉ"));
		productos.add(new Producto("LECHE","LALA"));
		productos.add(new Producto("ELOTES TIERNOS","SAN MIGUEL"));
		productos.add(new Producto("CHILES JALAPEÑOS","CLEMENTE JACQUES"));
		productos.add(new Producto("DURAZNOS EN ALMÍBAR","LA COSTEÑA"));
		productos.add(new Producto("CLORO","MAGIA BLANCA"));
		productos.add(new Producto("PAKETAXO","SABRITAS"));
		productos.add(new Producto("CACAHUATES HOT NUTS","BARCEL"));
		productos.add(new Producto("PAPAS","SABRITAS"));
		productos.add(new Producto("CHICHARRÓN DE CERDO","SABRITAS"));
		productos.add(new Producto("CHURRUMAIS","CHURRUMAIS"));
		productos.add(new Producto("CAFÉ LA JARRILLITA","LA JARRILLITA"));
		productos.add(new Producto("DETERGENTE","ARIEL"));
		productos.add(new Producto("JABÓN","PROTEX"));
		
	}

	public static void main(String[] args) {
		
		DAOManager manager = DAOManager.instance();
		Faker faker = new Faker(new Locale("es-MX"));
		
		List<Sucursal> sucursales = new LinkedList<>();
		List<Cliente> clientes = new LinkedList<>();
		List<Proveedor> proveedores = new LinkedList<>();
		
		manager.sucursal().ifPresent(daoSuc -> {
			for(int i = 0; i < NUM_SUCURSALES; ++i) {
				final Sucursal sucursal = new Sucursal(faker.address().cityName());
				try {
					final int id = daoSuc.insert(sucursal);
					sucursal.setId(id);
					sucursales.add(sucursal);
				} catch (SQLException e) {
					--i;
					e.printStackTrace();
				}
			}
		});
		
		manager.cliente().ifPresent(daoCli -> {
			for(int i = 0; i < NUM_CLIENTES; ++i) {
				final Cliente cliente = new Cliente(generarNit(11), faker.name().firstName(), 
						faker.name().lastName(), generarTelefono());
				try {
					daoCli.insert(cliente);
					clientes.add(cliente);
				} catch (SQLException e) {
					--i;
					e.printStackTrace();
				}
			}
		});
		
		manager.proveedor().ifPresent(daoProv -> {
			for(int i = 0; i < NUM_PROVEEDORES; ++i) {
				final Proveedor proveedor = new Proveedor(faker.name().firstName() 
						+ " " + faker.name().lastName(), generarTelefono());
				try {
					daoProv.insert(proveedor);
					proveedores.add(proveedor);
				} catch (SQLException e) {
					--i;
					e.printStackTrace();
				}
			}
		});
		
		manager.producto().ifPresent(daoProd -> {
			for(Producto producto : productos) {
				try {
					int id = daoProd.insert(producto);
					producto.setId(id);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public static String generarNit(final int maxDigits) {
		final int tam = 3 + RAND.nextInt(maxDigits - 2);
		final int verificador = RAND.nextInt(11);
		StringBuilder nit = new StringBuilder(tam + 2);
		for(int i = 0; i < tam; ++i) {
			nit.append(RAND.nextInt(10));
		}
		nit.append('-');
		nit.append(verificador < 10 ? verificador : "K");
		return nit.toString();
	}
	
	public static String generarTelefono() {
		StringBuilder telefono = new StringBuilder(8);
		telefono.append(2 + RAND.nextInt(8));
		for(int i = 0; i < 7; ++i)
			telefono.append(RAND.nextInt(10));
		return telefono.toString();
	}

}
