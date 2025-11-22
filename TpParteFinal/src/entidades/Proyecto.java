package entidades;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
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
	private boolean retraso = false;

	//MÉTODOS-OPERACIONES	
	//CONSTRUCTOR
	public Proyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {
		if (domicilio == null || domicilio.equals("")) {
			throw new IllegalArgumentException("El domicilio no puede ser vacio");
		}
		
		if (fechaInicio == null || fechaInicio.equals("")) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser vacia");
		}
		
		if (fechaEstimadaFin == null || fechaInicio.equals("")) {
			throw new IllegalArgumentException("La fecha estimada de fin no puede ser vacia");
		}
		
		if (LocalDate.parse(fechaInicio).isAfter(LocalDate.parse(fechaEstimadaFin))) {
			throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
		}
		
		registrarTareas(titulos, descripciones, dias);
		this.domicilio = domicilio;	
		cliente = new Cliente(datosCliente);
		this.fechaInicio = LocalDate.parse(fechaInicio);
		this.fechaEstimadaFin = LocalDate.parse(fechaEstimadaFin);						
		ultimoNumeroProyecto ++;
		numeroProyecto = ultimoNumeroProyecto;	
	}
	
	public void asignarEmpleadoDisponibleATarea(String tituloTarea, Integer legajoEmpleado) throws Exception {
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
	    Tarea t = tareas.get(tituloTarea);
	    if (t == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }

	    if (t.retornarEmpleadoResponsable() != null) {
	        throw new Exception("La tarea ya tiene un empleado asignado.");
	    }

	    //Asignación
	    t.asignarEmpleado(legajoEmpleado);
	    
	    //Comprobacion sobre sobrepasar los dias en fechaEstimadaFin.
	    Long diasTarea = Math.round(t.retornarDiasNecesarios());
	    
	    if (t.retornarDiaInicioActividad() != null) {
		    if (t.retornarDiaInicioActividad().plusDays(diasTarea).isAfter(fechaEstimadaFin)) {
		    	fechaEstimadaFin = t.retornarDiaInicioActividad().now().plusDays(diasTarea);    	
		    }
	    }

	    // Si el proyecto estaba pendiente, lo activamos
	    if (Estado.pendiente.equals(this.estado)) {
	        this.estado = Estado.activo;
	    }
	}
	
	public Integer registrarRetrasoEnTarea(String tituloTarea, double cantidadDias) {
		Tarea t = tareas.get(tituloTarea);
		
	    if (t == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }

	    //Devuelve el legajo del empleado responsable
	    Integer legajoResponsable = t.retornarEmpleadoResponsable();	    
	    //Añadimos retraso a tarea
	    t.registrarRetraso(cantidadDias);	    
	    //Comprobacion sobre sobrepasar los dias en fechaEstimadaFin.
	    Long diasTarea = Math.round(t.retornarDiasNecesarios());
	    
	    if (t.retornarDiaInicioActividad() != null) {
		    if (t.retornarDiaInicioActividad().plusDays(diasTarea).isAfter(fechaEstimadaFin)) {
		    	fechaEstimadaFin = t.retornarDiaInicioActividad().now().plusDays(diasTarea);    	
		    }
	    }
	    
	    retraso = true;
	    return legajoResponsable;
	}
	
	// Devuelve cuánto ajustar el costo al reasignar un empleado
	public Integer reasignarEmpleado(String tituloTarea, Integer nuevoEmpleado) throws Exception {
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
		Tarea tarea  = tareas.get(tituloTarea);
		
	    if (tarea == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }
	    
	    return tarea.reasignarEmpleado(nuevoEmpleado);    
	}
	
	public void registrarTarea(String tituloTarea, String descripcionTarea,
			double diasNecesariosTarea) {		
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
		if (tituloTarea == null || tituloTarea.equals("")) {
			throw new IllegalArgumentException("El título ingresado es invalido.");
		}
		
//		if(descripcionTarea == null || descripcionTarea.equals("")) {
//			throw new IllegalArgumentException("La descripción ingresada es invalida.");
//		}
			
		if(diasNecesariosTarea <= 0) {
			throw new IllegalArgumentException("Los días necesarios deben ser mayores a 0.");
		}
		
		Tarea t = new Tarea(tituloTarea, descripcionTarea, diasNecesariosTarea);
		tareas.put(t.retornarTitulo(), t);
	}
	
	public void registrarTareas(String[] titulosTarea, String[] descripcionesTarea,
			double[] diasNecesariosTarea) {
		
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
		if (titulosTarea.length != descripcionesTarea.length || descripcionesTarea.length != diasNecesariosTarea.length) {
			throw new IllegalArgumentException("Deben ingresarse la misma cantidad de titulos, descripciones y días para la creación de tareas.");
		}
		
		for (int i = 0; i < titulosTarea.length; i++) {
			registrarTarea(titulosTarea[i], descripcionesTarea[i], diasNecesariosTarea[i]);
		}
	}
	
	public Integer finalizarTarea(String tituloTarea) {
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
		if (tituloTarea == null || tituloTarea.equals("")) {
	        throw new IllegalArgumentException("El título ingresado no puede ser nulo.");
		}
		
	    if (!tareas.containsKey(tituloTarea)) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + tituloTarea);
	    }
	    
	    return tareas.get(tituloTarea).finalizarTarea();	    
	}
		
	public Set<Integer> finalizarProyecto(String fechaFin) {
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
		if (fechaRealFin != null) {    	
			throw new IllegalArgumentException("Proyecto ya finalizado.");
		}
		    
		if (fechaFin == null || fechaFin.length() != 10) {	    	
			throw new IllegalArgumentException("La fecha ingresada debe estar en formato YYYY/MM/DD.");
		}
		
		if (fechaInicio.isAfter(LocalDate.parse(fechaFin))) {	    	
	        throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio.");
	    }
		
//		if (!fechaEstimadaFin.isEqual(LocalDate.parse(fechaFin))) {   	
//	        throw new IllegalArgumentException("La fecha de fin no coincide con la fecha estimada de fin");
//	    }
//		
		Set<Tarea> pendientes = retornarTareasPendientes();
		Set<Integer> empleadosADesocupar = new HashSet<>();
		
		if (!pendientes.isEmpty()) {
			for (Tarea t : pendientes) {
				empleadosADesocupar.add(finalizarTarea(t.retornarTitulo()));
			}
		}
		
		estado  = Estado.finalizado;
		fechaRealFin = LocalDate.parse(fechaFin);	
		return empleadosADesocupar;
	}

	public double calcularCostoProyecto() {
		if(retraso) {				 
			 return costoFinal * 1.25;
		}
		else {
			return costoFinal * 1.35; 
		}
	}
	
	public HashMap<String, Tarea> retornarTareas() {
	    return tareas;
	}
	
	public void incrementarCosto(double monto) {
		if (estado.equals(Estado.finalizado)) {
			throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
		}
		
        this.costoFinal += monto;
    }
	
	 public void reducirCosto(double monto) {
			if (estado.equals(Estado.finalizado)) {
				throw new IllegalArgumentException("El proyecto " + numeroProyecto + "está finalizado.");
			}
			
	        if (monto < 0) {
	            throw new IllegalArgumentException("El monto a reducir no puede ser negativo.");
	        }
	        
	        costoFinal -= monto;
	        
	        if (costoFinal < 0) {
	            costoFinal = 0;
	        }
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
	
	@Override
	public String toString() {		
		StringBuilder cadena = new StringBuilder();
		cadena.append("Número proyecto: " + numeroProyecto + "\n");
		cadena.append("Estado del proyecto: " + estado + "\n");
		cadena.append("Cantidad de tareas: " + tareas.size() + "\n");
		cadena.append(cliente.toString() + "\n");
		cadena.append("Fecha de inicio: " + fechaInicio + "\n");
		
		if (fechaRealFin != null) {
			cadena.append("Fecha real de fin " + fechaRealFin + "\n");
		}
		
		else {
			cadena.append("Fecha estimada de fin: " + fechaEstimadaFin + "\n");
		}
		
		if (estado == Estado.finalizado) {
			cadena.append("Costo: " + costoFinal + "\n");
		}
		
		return cadena.toString();
	}
	
	public double retornarDiasTarea(String tituloTarea) {
		Tarea t = tareas.get(tituloTarea);
		if (t == null) {
			throw new IllegalArgumentException("No existe tarea con titulo " + tituloTarea + ".");
		}
		
		return t.retornarDiasNecesarios();
	}
	
	public double retornarMediosDiasTarea(String tituloTarea) {
		Tarea t = tareas.get(tituloTarea);
		if (t == null) {
			throw new IllegalArgumentException("No existe tarea con titulo " + tituloTarea + ".");
		}
		
		return t.retornarMediosDiasNecesarios();
	}
	
	public boolean tareaConRetraso(String tituloTarea) {
		Tarea t = tareas.get(tituloTarea);
		
		if (t == null) {
			throw new IllegalArgumentException("La tarea " + tituloTarea + " no existe.");
		}
		
		return t.retornarDiasRetraso() > 0;
	}
	
}
