package entidades;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Proyecto {
	

	//DATOS-ATRIBUTOS
	
	private HashMap<String, Tarea> tareas;
	private String domicilio;
	private Cliente cliente;
	private Date fechaInicio;
	private Date fechaEstimadaFin;
	private static int ultimoNumeroProyecto = 1;
	private int numeroProyecto;
	private Date fechaRealDeFin;
	private double costoFinal;
	private boolean finalizado;


	//MÉTODOS-OPERACIONES
	
	//CONSTRUCTOR
	public Proyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) {
		
		for(int i = 0; i < titulos.length; i++) {
			
			Tarea t = new Tarea(titulos[i], descripciones[i], dias[i]);
			tareas.put(t.retornarTitulo(), t);
		}
	
		this.domicilio = domicilio;
		
		cliente = new Cliente(datosCliente[0], datosCliente[1], datosCliente[2]);
		
		//CAMBIAR-NO SIRVE-DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		this.fechaInicio = new Date(fechaInicio);
		this.fechaEstimadaFin = new Date(fechaEstimadaFin);
		
		ultimoNumeroProyecto ++;
		numeroProyecto = ultimoNumeroProyecto;
		
	}
	
	public void asignarEmpleadoATarea(String tituloTarea, int nLegajo) {
		
		tareas.get(tituloTarea).asignarEmpleado(nLegajo);	
	}
	
	public int registrarRetrasoTarea(String tituloTarea, double diasRetraso) {
		
		return tareas.get(tituloTarea).registrarRetraso(diasRetraso);
	}
	
	public void registrarTarea(String tituloTarea, String descripcionTarea,
			double diasNecesariosTarea) {
		
	}
	
	public void finalizarTarea(String tituloTarea) {
		
	}
	
	public void finalizarProyecto() {
		
	}
	
	public double calcularCostoProyecto() {
		
	}
	
	public boolean estaFinalizado() {
		
	}
	
	public Set<Tarea> listarTareasPendientes() {
		
	}
	
	public int retornarNumeroProyecto() {
		
		return numeroProyecto;
	}
}
