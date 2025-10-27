package entidades;

import java.util.Date;
import java.util.HashMap;

public class Cliente {
	//DATOS-ATRIBUTOS
	private String nombre;
	private String email;
	private String telefono;
	
	//MÉTODOS-OPERACIONES
	//Constructor.
	public Cliente(String nombre, String email, String telefono) {	
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
	}
	
	@Override
	public String toString() {
		return nombre + " " + email + " " + telefono;
	}
}
