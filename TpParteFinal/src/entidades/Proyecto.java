package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Proyecto {	
	//DATOS-ATRIBUTOS
	private HashMap<String, Tarea> tareas = new HashMap<>();
	private final String domicilio;
	private final Cliente cliente;
	private LocalDate fechaInicio, fechaEstimadaFin, fechaRealFin;
	private static Integer ultimoNumeroProyecto = 0;
	private final Integer numeroProyecto;
	private double costoFinal;
	private String estado = Estado.pendiente;

	//MÉTODOS-OPERACIONES	
	//CONSTRUCTOR
	public Proyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {	
		if (datosCliente[0] == null || datosCliente[0].equals("")) {
			throw new IllegalArgumentException("El nombre de cliente ingresado es invalido.");
		}
		
		if (datosCliente[1] == null || datosCliente[1].equals("")) {
			throw new IllegalArgumentException("El email de cliente ingresado es invalido.");
		}
		
		if (datosCliente[2] == null || datosCliente[2].equals("")) {
			throw new IllegalArgumentException("El teléfono de cliente ingresado es invalido.");
		}
		
//		if (LocalDate.parse(fechaEstimadaFin).isBefore(LocalDate.now())) {
//			throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha actual.");
//		}
		
		if (LocalDate.parse(fechaInicio).isAfter(LocalDate.parse(fechaEstimadaFin))) {
			throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
		}
		
		for (int i = 0; i < titulos.length; i++) {		
			registrarTarea(titulos[i], descripciones[i], dias[i]);
		}
	
		this.domicilio = domicilio;		
		cliente = new Cliente(datosCliente[0], datosCliente[1], datosCliente[2]);			
		this.fechaInicio = LocalDate.parse(fechaInicio);
		this.fechaEstimadaFin = LocalDate.parse(fechaEstimadaFin);	
		ultimoNumeroProyecto ++;
		numeroProyecto = ultimoNumeroProyecto;	
	}
	
	public void asignarEmpleadoATarea(String tituloTarea, Integer nLegajo) throws Exception {
		if (!tareas.containsKey(tituloTarea)) {
			throw new Exception("La tarea no existe.");
		}
		
		if (tareas.get(tituloTarea).retornarFinalizada()) {
			throw new Exception("La tarea esta finalizada.");
		}
		
		if (tareas.get(tituloTarea).retornarEmpleadoResponsable() != null) {
			throw new Exception("La tarea ya tiene un empleado asignado.");
		}
		
		tareas.get(tituloTarea).asignarEmpleado(nLegajo);
		estado = Estado.activo;
	}
	
	public Integer registrarRetrasoTarea(String tituloTarea, double diasRetraso) {
		
		Integer empleadoResp = tareas.get(tituloTarea).registrarRetraso(diasRetraso);		
		Integer diferenciaInicioProyectoFinTarea = tareas.get(tituloTarea).retornarRetrasoAgregar(fechaInicio).compareTo(fechaEstimadaFin);
		
		if (diferenciaInicioProyectoFinTarea > 0) {
			fechaEstimadaFin.plusDays(diferenciaInicioProyectoFinTarea);
		}
		
		return empleadoResp;
	}
	
	public void registrarTarea(String tituloTarea, String descripcionTarea,
			double diasNecesariosTarea) {	
		if (tituloTarea == null || tituloTarea.equals("")) {
			throw new IllegalArgumentException("El título ingresado es invalido.");
		}
		
//		if(descripcionTarea == null || descripcionTarea.equals("")) {
//			throw new IllegalArgumentException("La descripción ingresada es invalida.");
//		}
			
		if(diasNecesariosTarea <= 0) {
			throw new IllegalArgumentException("Los días necesarios deben ser mayores a 0.");
		}
		
		Tarea t = new Tarea(tituloTarea, descripcionTarea, diasNecesariosTarea, LocalDate.now());
		tareas.put(t.retornarTitulo(), t);
		
		if (LocalDate.now().plusDays(Math.round(diasNecesariosTarea)).compareTo(fechaEstimadaFin) > 0) {
			fechaEstimadaFin.plusDays(LocalDate.now().plusDays(Math.round(diasNecesariosTarea)).compareTo(fechaEstimadaFin));
		}
	}
	
	public Integer finalizarTarea(String tituloTarea) {
		
		if (tituloTarea == null || tituloTarea.equals("")) {
	        throw new IllegalArgumentException("El titulo ingresado no puede ser nula");
		}
		
	    if (!tareas.containsKey(tituloTarea)) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }
	    
	    return tareas.get(tituloTarea).finalizarTarea();	    
	}
		
	public Set<Integer> finalizarProyecto(String fechaFin) {		
		Set<Tarea> pendientes = retornarTareasPendientes();
		Set<Integer> empleadosADesocupar = new HashSet<>();
		
		if (fechaRealFin != null) {    	
			throw new IllegalArgumentException("Proyecto ya finalizado");
		}
		    
		if (fechaFin == null || fechaFin.length() != 10) {	    	
			throw new IllegalArgumentException("La fecha ingresada debe estar en formato YYYY/MM/DD.");
		}
		
		if (fechaInicio.isAfter(LocalDate.parse(fechaFin))) {	    	
	        throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
	    }
		
		if (!pendientes.isEmpty()) {
			for (Tarea t : pendientes) {
				empleadosADesocupar.add(finalizarTarea(t.retornarTitulo()));
			}
		}
		
		estado  = Estado.finalizado;
		fechaRealFin = LocalDate.parse(fechaFin);
		costoFinal = calcularCostoProyecto(); 
		return empleadosADesocupar;
	}

	public double calcularCostoProyecto() {	
		 return costoFinal;
	}
	
	public HashMap<String, Tarea> retornarTareas() {
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
	 
	 public Tarea retornarTareaPorTitulo(String titulo) {		 
		    return tareas.get(titulo);
		}

		public String retornarDomicilio() {		
		    return domicilio;
		}
	
	public Set<Tarea> retornarTareasPendientes() {	
        Set<Tarea> pendientes = new HashSet<>();
		    for (Tarea t : tareas.values()) {
		        if (!t.retornarFinalizada()) {
		            pendientes.add(t);
		        }
		    }
		    
		    return pendientes;
		}
	
	public Integer retornarNumeroProyecto() {		
		return numeroProyecto;
	}
	
	public Set<Integer> retornarEmpleados() {
		Set<Integer> empleados = new HashSet<>();
		
		for (Tarea t : tareas.values()) {
			if (t.retornarEmpleadoResponsable() != null) {
				empleados.add(t.retornarEmpleadoResponsable());
			}
		}
		
		return empleados;
	}
	
	@Override
	public String toString() {
		return "El número de proyecto es: " + numeroProyecto;
	}

	public Set<Tarea> retornarTareasNoAsignadas() {	
    Set<Tarea> noAsignadas = new HashSet<>();
    
    if (estado == Estado.finalizado) {
    	throw new IllegalArgumentException("El proyecto " + numeroProyecto + " esta finalizado");
    }
    
	for (Tarea t : tareas.values()) {
		if (t.retornarEmpleadoResponsable() == null) {
			noAsignadas.add(t);
		}
	}
	
	return noAsignadas;
	}
	
	public void establecerPendiente() {
		estado = Estado.pendiente;
	}
	
	public String retornarEstado() {
		if (retornarTareasPendientes().isEmpty() && estado != Estado.finalizado) {
			estado = Estado.activo;
		}
		
		return estado;
	}
	
}
