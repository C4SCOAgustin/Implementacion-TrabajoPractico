import entidades.Contratado;
import entidades.Empleado;
import entidades.Permanente;
import entidades.HomeSolution;
import gui.PanelManager;
import entidades.Tarea;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        HomeSolution homeSolution=new HomeSolution();
//        String titulos[] = {"Pintar","Instacion electrica","Trabajos jardineria","Instalar AA"};
//        String descripciones[] = {"", "", "", ""};
//        double duracion[] = {4,2,1,.5};
//        String cliente[] = {"Pedro Gomez", "", ""};
//        homeSolution.registrarProyecto(titulos, descripciones, duracion, "San Martin 1000", cliente, "2025-11-01", "2025-11-05");
//        homeSolution.registrarEmpleado("Juan", 15000);
//        homeSolution.registrarEmpleado("Luis", 80000, "EXPERTO");
//        homeSolution.registrarEmpleado("Julieta", 15000);
        PanelManager panelManager = new PanelManager(homeSolution);
  
        homeSolution.registrarEmpleado("Juan", 15000);
        homeSolution.registrarEmpleado("Hernan", 15000);
//        Long x = 1000l;
//        while (x > 0) {
//        	System.out.println(homeSolution.empleados().getLast().getValor2());
//        }
        String titulos[] = {"Pintar","Instalacion electrica"};
        String descripciones[] = {"pintar de rosa las paredes del cuarto", "Instalar el aire acondiciano de la oficina"};
        double duracion[] = {4,2.5};
        String cliente1[] = {"Pedro Gomez", "pedro@gmail.com", "1139383736"};
        
        homeSolution.registrarProyecto(titulos, descripciones, duracion, "Domicilio 10", cliente1, "2025-10-27", "2025-11-20" );
        
        homeSolution.finalizarTarea(1, "Pintar");
        homeSolution.finalizarTarea(1, "Instalacion electrica");
        
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[0]).retornarTitulo());
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[0]).retornarEmpleadoResponsable());
        
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[1]).retornarTitulo());
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[1]).retornarEmpleadoResponsable());
        
//        homeSolution.asignarResponsableEnTarea(1, titulos[0]);
//        homeSolution.asignarResponsableEnTarea(1, titulos[1]);
//        System.out.println(homeSolution.tareasDeUnProyecto(1));
//        for (Tarea t: (Tarea[]) (homeSolution.tareasDeUnProyecto(1))) {
//        	System.out.println(t.retornarTitulo());
//        	System.out.println(t);
//        }
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[0]).retornarEmpleadoResponsable());
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(homeSolution.proyectosPendientes().get(0).getValor1())[1]).retornarEmpleadoResponsable());
        //System.out.println(((Tarea) homeSolution.tareasDeUnProyecto(1)[0]).retornarTitulo());
//        
//        System.out.println(((Empleado) homeSolution.empleadosNoAsignados()[0]).retornarNombre());
//        System.out.println( homeSolution.empleadosAsignadosAProyecto(1).getFirst().getValor1());
//        System.out.println(homeSolution.empleadosAsignadosAProyecto(1).get(0).getValor1());
    }
}
