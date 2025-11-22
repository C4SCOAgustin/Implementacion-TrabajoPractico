package entidades;

public class Contratado extends Empleado {
	//DATOS-ATRIBUTOS
	
	//MÉTODOS-OPERACIONES
	//Constructor
	public Contratado(String nombre, double valor) {	
		super(nombre, valor, "CONTRATADO");
	}
	
	//Retorna el tipo de contrato del empleado (CONTRATADO) como un enum cadena (String)
	@Override
	public String retornarTipoContrato() {		
		return super.retornarTipoContrato();
	}
	
	@Override
	public double calcularCosto(double jornadas) {
	    return jornadas * 8 * super.retornarValor();
	}
	
	public double calcularCostoConRetraso(double jornadas) {
	    return jornadas * 8 * super.retornarValor();
	}
	
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		cadena.append(super.toString());
		cadena.append(retornarTipoContrato() + "\n");
		return cadena.toString();
	}
}
