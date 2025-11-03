package entidades;

import java.util.Date;
import java.util.HashMap;

public class Cliente {
	//DATOS-ATRIBUTOS
	private final String nombre;
	private final String email;
	private final String telefono;
	
	//MÉTODOS-OPERACIONES
	//Constructor.
	public Cliente(String nombre, String email, String telefono) {	
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
	}
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		cadena.append("Nombre: " + nombre + "\nEmail: " + email + "\nTeléfono: " + telefono);
		return cadena.toString();
	}
}
