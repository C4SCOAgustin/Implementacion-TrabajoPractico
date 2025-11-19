package entidades;

public class Cliente {
	//DATOS-ATRIBUTOS
	private final String nombre;
	private final String email;
	private final String telefono;
	
	//MÉTODOS-OPERACIONES
	//Constructor.
	public Cliente(String[] datosCliente) {
		if (datosCliente.length <= 0 || datosCliente.length >= 4) {
			throw new IllegalArgumentException("Los datos del cliente deben ser 'nombre', 'email' y 'teléfono'.");
		}
		
		if (datosCliente[0] == null || datosCliente[0].equals("")) {
			throw new IllegalArgumentException("El nombre del cliente ingresado es invalido.");
		}
		
//		if (datosCliente[1] == null || datosCliente[1].equals("")) {
//			throw new IllegalArgumentException("El email del cliente ingresado es invalido.");
//		}
		
		if (datosCliente[2] == null || datosCliente[2].equals("")) {
			throw new IllegalArgumentException("El teléfono del cliente ingresado es invalido.");
		}
		
		this.nombre = datosCliente[0];
		this.email = datosCliente[1];
		this.telefono = datosCliente[2];
	}
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		cadena.append("Nombre: " + nombre + "\nEmail: " + email + "\nTeléfono: " + telefono);
		return cadena.toString();
	}
}
