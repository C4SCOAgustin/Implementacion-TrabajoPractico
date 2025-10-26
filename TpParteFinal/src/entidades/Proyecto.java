package entidades;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Proyecto {
	
	//DATOS-ATRIBUTOS

	private HashMap<String, Tarea> tareas = new HashMap<>();
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
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {
		
		if(datosCliente[0] == null || datosCliente[0].equals(""))
			throw new IllegalArgumentException("El nombre de cliente ingresado es invalido.");
		
		if(datosCliente[1] == null || datosCliente[1].equals(""))
			throw new IllegalArgumentException("El email de cliente ingresado es invalido.");
		
		if(datosCliente[2] == null || datosCliente[2].equals(""))
			throw new IllegalArgumentException("El teléfono de cliente ingresado es invalido.");
		
		for(int i = 0; i < titulos.length; i++) {
			
			registrarTarea(titulos[i], descripciones[i], dias[i]);
		}
	
		this.domicilio = domicilio;
		
		cliente = new Cliente(datosCliente[0], datosCliente[1], datosCliente[2]);
		
		//CAMBIAR-NO SIRVE-DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//this.fechaInicio = new Date(fechaInicio);
		//this.fechaEstimadaFin = new Date(fechaEstimadaFin);
		
		ultimoNumeroProyecto ++;
		numeroProyecto = ultimoNumeroProyecto;		
	}
	
	public void asignarEmpleadoATarea(String tituloTarea, int nLegajo) throws Exception {
		
		if(!tareas.containsKey(tituloTarea) || tareas.get(tituloTarea).retornarEmpleadoResponsable() != 0)
			throw new Exception("La tarea no existe o ya tiene un empleado asignado");
		
		tareas.get(tituloTarea).asignarEmpleado(nLegajo);		
	}
	
	public int registrarRetrasoTarea(String tituloTarea, double diasRetraso) {
		
		return tareas.get(tituloTarea).registrarRetraso(diasRetraso);
	}
	
	public void registrarTarea(String tituloTarea, String descripcionTarea,
			double diasNecesariosTarea) {
		
		if(tituloTarea == null || tituloTarea.equals(""))
			throw new IllegalArgumentException("El título ingresado es invalido.");
		
		if(descripcionTarea == null || descripcionTarea.equals(""))
			throw new IllegalArgumentException("La descripción ingresada es invalida.");
		
		if(diasNecesariosTarea <= 0)
			throw new IllegalArgumentException("Los días necesarios deben ser mayores a 0.");
		
		Tarea t = new Tarea(tituloTarea, descripcionTarea, diasNecesariosTarea);
		tareas.put(t.retornarTitulo(), t);
	}
	
	public void finalizarTarea(String tituloTarea) {
		Tarea tarea = tareas.get(tituloTarea); 

	    if (tarea == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }

	    boolean exito = tarea.finalizarTarea(); 

	    if (!exito) {
	    	
	        System.out.println("La tarea '" + tituloTarea + "' ya estaba finalizada.");
	    }
	    
	    else {
	    	
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
	
	public HashMap<String, Tarea> getTareas() {
	    return tareas;
	}
	
	public void incrementarCosto(double monto) {
        this.costoFinal += monto;
    }
	
	 public void reducirCosto(double monto) {
	        if (monto < 0) {
	            throw new IllegalArgumentException("El monto a reducir no puede ser negativo.");
	        }

	        costoFinal -= monto;
	        
	        if (costoFinal < 0) {
	            costoFinal = 0; 
	        }
	    }
	 
	 public Tarea obtenerTareaPorTitulo(String titulo) {
		    return tareas.get(titulo);
		}

	 public boolean estaFinalizado() {
		 
		    return finalizado;
		}

		public String retornarDomicilio() {
			
		    return domicilio;
		}
	
	public Set<Tarea> listarTareasPendientes() {
		
        Set<Tarea> pendientes = new HashSet<>();
		    for (Tarea t : tareas.values()) {
		        if (!t.retornarEstadoTarea()) {
		            pendientes.add(t);
		        }
		    }
		    return pendientes;
		}
	
	public int retornarNumeroProyecto() {
		
		return numeroProyecto;
	}
}
