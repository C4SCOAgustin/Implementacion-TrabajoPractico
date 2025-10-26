package entidades;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeSolution implements IHomeSolution {
	
	HashMap<Integer, Empleado> empleados = new HashMap<>();
	HashMap<Integer, Proyecto> proyectos = new HashMap<>();

	@Override
	public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
		
		if(nombre.equals(null) || nombre.equals("") || valor < 0)
			throw new IllegalArgumentException("Los argumentos ingresados no son validos");
		
		Empleado e = new Contratado(nombre, valor);
		empleados.put(e.retornarLegajo(), e);
	}

	@Override
	public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
		
		if(nombre.equals(null)|| nombre.equals("") || valor < 0 || !Categoria.contains(categoria))
			throw new IllegalArgumentException("Los argumentos ingresados no son validos");
		
		Empleado e = new Permanente(nombre, valor, categoria);
		empleados.put(e.retornarLegajo(), e);		
	}

	@Override
	public void registrarProyecto(String[] titulos, String[] descripciones, double[] dias, String domicilio,
			String[] datosCliente, String fechaInicio, String fechaEstimadaFin) throws IllegalArgumentException {
		
		Proyecto p = new Proyecto(titulos, descripciones, dias, domicilio, datosCliente, fechaInicio, fechaEstimadaFin);
		proyectos.put(p.retornarNumeroProyecto(), p);
			
	}

	@Override
	public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
				
		for(int key:empleados.keySet()) {
			
			if(empleados.get(key).retornarDisponibilidad()) {
				
				proyectos.get(numero).asignarEmpleadoATarea(titulo, key);
			}
		}
	}

	@Override
	public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {
		
		int numLegajo = obtenerMenosRetrasos(); //SI DEVUELVE - 1 DEBERIA DAR UN ERROR
		
		if(numLegajo < 0) throw new IllegalArgumentException("No hay empleados disponibles");
		
		proyectos.get(numero).asignarEmpleadoATarea(titulo, numLegajo);
	}

	@Override
	public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias)
			throws IllegalArgumentException {
		
		int empleadoResponsable = proyectos.get(numero).registrarRetrasoTarea(titulo, cantidadDias);
		empleados.get(empleadoResponsable).añadirRetraso();
	}

	@Override
	public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias)
			throws IllegalArgumentException {
		
		proyectos.get(numero).registrarTarea(titulo, descripcion, dias);
	}

	@Override
	public void finalizarTarea(Integer numero, String titulo) throws Exception {
		Proyecto proyecto = proyectos.get(numero);

	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }

	    proyecto.finalizarTarea(titulo); 
	}

		
		
		
	

	@Override
	public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {
		Proyecto proyecto = proyectos.get(numero);

		   if (proyecto == null) {
		       throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
		   }

		   proyecto.finalizarProyecto();
			
	}


	@Override
	public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
		
	    Proyecto proyecto = proyectos.get(numero);
	    if (proyecto == null) {
	        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
	    }

	    
	    Tarea tarea = proyecto.getTareas().get(titulo);
	    if (tarea == null) {
	        throw new IllegalArgumentException("No existe la tarea con título: " + titulo);
	    }

	    
	    int legajoAnterior = tarea.retornarEmpleadoResponsable();
	    if (legajoAnterior == 0) { 
	        throw new Exception("La tarea no tiene empleado asignado previamente.");
	    }

	    
	    Empleado empleadoNuevo = empleados.get(legajo);
	    if (empleadoNuevo == null) {
	        throw new IllegalArgumentException("El empleado reemplazante no existe: " + legajo);
	    }

	   
	    tarea.asignarEmpleado(legajo);

	    
	    Empleado empleadoAnterior = empleados.get(legajoAnterior);
	    if (empleadoAnterior != null) empleadoAnterior.desocuparEmpleado();
	    empleadoNuevo.ocuparEmpleado();

	    
	    double diasTotales = tarea.retornarDiasNecesarios() + tarea.retornarDiasRetraso();
	    if (empleadoAnterior != null) proyecto.reducirCosto(diasTotales * empleadoAnterior.retornarValor());
	    proyecto.incrementarCosto(diasTotales * empleadoNuevo.retornarValor());

	    System.out.println("Empleado reasignado correctamente en la tarea '" + titulo + "'");
    }

	@Override
	public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
		 Proyecto proyecto = proyectos.get(numero);
		    if (proyecto == null) {
		        throw new IllegalArgumentException("No existe el proyecto con número: " + numero);
		    }

		    
		    Tarea tarea = proyecto.obtenerTareaPorTitulo(titulo);
		    if (tarea == null) {
		        throw new IllegalArgumentException("No existe la tarea con título: " + titulo);
		    }

		    
		    int legajoActual = tarea.retornarEmpleadoResponsable();
		    if (legajoActual == 0) { 
		        throw new Exception("La tarea no tiene un empleado asignado actualmente.");
		    }

		    Empleado empleadoActual = empleados.get(legajoActual);
		    if (empleadoActual == null) {
		        throw new Exception("El empleado actual asignado a la tarea no existe en el sistema.");
		    }

		    
		    Empleado mejorEmpleado = null;
		    for (Empleado e : empleados.values()) {
		        if (!e.retornarDisponibilidad()) continue; 
		        if (mejorEmpleado == null || e.retornarRetrasos() < mejorEmpleado.retornarRetrasos()) {
		            mejorEmpleado = e;
		        }
		    }

		    if (mejorEmpleado == null) {
		        throw new Exception("No hay empleados disponibles para reasignar.");
		    }

		    
		    empleadoActual.desocuparEmpleado();

		    
		    tarea.asignarEmpleado(mejorEmpleado.retornarLegajo());
		    mejorEmpleado.ocuparEmpleado();

		    
		    proyecto.reducirCosto(empleadoActual.retornarValor());
		    proyecto.incrementarCosto(mejorEmpleado.retornarValor());

		    System.out.println("Empleado '" + empleadoActual.retornarNombre() + 
		                       "' fue reemplazado por '" + mejorEmpleado.retornarNombre() +
		                       "' en la tarea '" + titulo + "'.");
		}
		
	

	@Override
	public double costoProyecto(Integer numeroProyecto) {
		Proyecto proyecto = proyectos.get(numeroProyecto);
        return proyecto.calcularCostoProyecto(); // O(1)
	}
		
		
	@Override
	public List<Tupla<Integer, String>> proyectosFinalizados() {
		
		return null;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosPendientes() {
		
		return null;
	}

	@Override
	public List<Tupla<Integer, String>> proyectosActivos() {
		
		return null;
	}

	@Override
	public Object[] empleadosNoAsignados() {
		
		return null;
	}

	@Override
	public boolean estaFinalizado(Integer numero) {
		
		return false;
	}

	@Override
	public int consultarCantidadRetrasosEmpleado(Integer legajo) {
		
		return 0;
	}

	@Override
	public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
		
		return null;
	}

	@Override
	public Object[] tareasProyectoNoAsignadas(Integer numero) {
		
		return null;
	}

	@Override
	public Object[] tareasDeUnProyecto(Integer numero) {
		
		return null;
	}

	@Override
	public String consultarDomicilioProyecto(Integer numero) {
		
		return null;
	}

	@Override
	public boolean tieneRestrasos(Integer legajo) {
		
		return false;
	}

	@Override
	public List<Tupla<Integer, String>> empleados() {
		
		return null;
	}

	@Override
	public String consultarProyecto(Integer numero) {
		
		return null;
	}
	
	//Método auxiliar que devuelve el numero de legajo del empleado disponible con menos retrasos.
	//En caso de no haber ningun empleado disponible devuelve -1.
	private int obtenerMenosRetrasos() {
		
		int menosRetrasos = -1;
		int empleadoMenosRetrasos = -1;
		
		for(int key:empleados.keySet()) {
			
			if(empleados.get(key).retornarDisponibilidad()) {
				
				if(menosRetrasos == -1) {
					
					menosRetrasos = empleados.get(key).retornarRetrasos();
					empleadoMenosRetrasos = key;
				}
	
				if(empleados.get(key).retornarRetrasos() < menosRetrasos) {
					
					menosRetrasos = empleados.get(key).retornarRetrasos();
					empleadoMenosRetrasos = key;
				}
			}
		}
		
		return empleadoMenosRetrasos;	
	}
}
