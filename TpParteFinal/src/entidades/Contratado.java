package entidades;

public class Contratado extends Empleado {
	//DATOS-ATRIBUTOS
	
	//MÉTODOS-OPERACIONES
	//Constructor
	public Contratado(String nombre, double valor) {	
		super(nombre, valor);
	}
	
	//Retorna el tipo de contrato del empleado (CONTRATADO) como un enum cadena (String)
	@Override
	public TipoContrato retornarTipoContrato() {		
		return TipoContrato.CONTRATADO;
	}
	
	@Override
	public double calcularCosto(double dias) {
	    double horas = dias * 8;
	    return retornarValor() * horas;
	}
	
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		cadena.append(super.toString());
		cadena.append(retornarTipoContrato() + "\n");
		return cadena.toString();
	}
}
