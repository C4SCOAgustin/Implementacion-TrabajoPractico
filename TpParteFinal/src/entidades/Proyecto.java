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
			
			registrarTarea(titulos[i], descripciones[i], dias[i]);
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
		Tarea tarea = tareas.get(tituloTarea); 

	    if (tarea == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }

	    boolean exito = tarea.finalizarTarea(); 

	    if (!exito) {
	        System.out.println("La tarea '" + tituloTarea + "' ya estaba finalizada.");
	    } else {
	        System.out.println("Tarea '" + tituloTarea + "' finalizada correctamente.");
	    }

	    
	}
		
	
	
	public void finalizarProyecto() {
		boolean todasFinalizadas = tareas.values().stream().allMatch(Tarea::retornarEstadoTarea);

		    if (!todasFinalizadas) {
		        System.out.println("No se puede finalizar el proyecto. Todavía hay tareas pendientes.");
		        return; 
		    }

		    
		    finalizado = true;
		    fechaRealDeFin = new Date(); 
		    costoFinal = calcularCostoProyecto(); 

		    System.out.println("Proyecto #" + numeroProyecto + " finalizado correctamente.");
			
		}

	
	public double calcularCostoProyecto() {
		 return costoFinal;
	}
	
	public boolean estaFinalizado() {
		
	}
	
	public Set<Tarea> listarTareasPendientes() {
		
	}
	
	public int retornarNumeroProyecto() {
		
		return numeroProyecto;
	}
}
