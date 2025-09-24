import java.util.Scanner;
import javax.swing.*;

// ------------------------- BRIDGE ----------------------------

/**
 * Interface TransporteDisplay
 * Define la interfaz del implementador en el patron Bridge.
 * Permite mostrar informacion de un transporte en diferentes formas (consola, frame, web).
 */
interface TransporteDisplay {
    void mostrar(Transporte transporte);
}

/**
 * Implementacion concreta de TransporteDisplay.
 * Muestra la informacion en la consola.
 */
class ConsoleDisplay implements TransporteDisplay {
    @Override
    public void mostrar(Transporte transporte) {
        System.out.println("Informacion del transporte:");
        System.out.println("Tipo: " + transporte.getTipo());
        System.out.println("Modelo: " + transporte.getModelo());
        System.out.println("Fabricante: " + transporte.getFabricante());
        System.out.println("Capacidad: " + transporte.getCapacidadPasajeros() + " pasajeros");
        System.out.println("Autonomia: " + transporte.getAutonomia() + " km");
        System.out.println("--------------------------------------");
    }
}

/**
 * Implementacion concreta de TransporteDisplay.
 * Muestra la informacion en una ventana de escritorio usando JFrame.
 */
class FrameDisplay implements TransporteDisplay {
    @Override
    public void mostrar(Transporte transporte) {
        JFrame frame = new JFrame("Informacion del transporte");
        JTextArea textArea = new JTextArea();
        textArea.setText("Tipo: " + transporte.getTipo() + "\n" +
                         "Modelo: " + transporte.getModelo() + "\n" +
                         "Fabricante: " + transporte.getFabricante() + "\n" +
                         "Capacidad: " + transporte.getCapacidadPasajeros() + " pasajeros\n" +
                         "Autonomia: " + transporte.getAutonomia() + " km");
        textArea.setEditable(false);
        frame.add(textArea);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}

/**
 * Implementacion concreta de TransporteDisplay.
 * Muestra la informacion simulando una salida HTML en consola.
 */
class WebDisplay implements TransporteDisplay {
    @Override
    public void mostrar(Transporte transporte) {
        String html = "<html>\n" +
                "<head><title>Transporte</title></head>\n" +
                "<body>\n" +
                "<h1>Informacion del transporte</h1>\n" +
                "<p><b>Tipo:</b> " + transporte.getTipo() + "</p>\n" +
                "<p><b>Modelo:</b> " + transporte.getModelo() + "</p>\n" +
                "<p><b>Fabricante:</b> " + transporte.getFabricante() + "</p>\n" +
                "<p><b>Capacidad:</b> " + transporte.getCapacidadPasajeros() + " pasajeros</p>\n" +
                "<p><b>Autonomia:</b> " + transporte.getAutonomia() + " km</p>\n" +
                "</body></html>";
        System.out.println(html);
        System.out.println("--------------------------------------");
    }
}

// ---------------------- ABSTRACCION --------------------------

/**
 * Clase abstracta Transporte
 * Representa la abstraccion en el patron Bridge.
 * Contiene la informacion comun de cualquier transporte.
 */
abstract class Transporte {
    private String modelo;
    private String fabricante;
    private int capacidadPasajeros;
    private double autonomia;
    private TransporteDisplay display; // Referencia al implementador del Bridge

    protected Transporte(Builder<?> builder) {
        this.modelo = builder.modelo;
        this.fabricante = builder.fabricante;
        this.capacidadPasajeros = builder.capacidadPasajeros;
        this.autonomia = builder.autonomia;
        this.display = builder.display;
    }

    // Getters
    public String getModelo() { return modelo; }
    public String getFabricante() { return fabricante; }
    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public double getAutonomia() { return autonomia; }

    // Metodo que debe implementar cada transporte concreto
    public abstract String getTipo();

    // Muestra la informacion usando la implementacion seleccionada
    public void mostrarInformacion() {
        if (display != null) display.mostrar(this);
        else System.out.println("No hay un metodo definido para mostrar la informacion.");
    }

    // Permite cambiar la estrategia de visualizacion en tiempo de ejecucion
    public void setDisplay(TransporteDisplay display) {
        this.display = display;
    }

    /**
     * Clase Builder generica para construir transportes
     */
    public static abstract class Builder<T extends Builder<T>> {
        private String modelo;
        private String fabricante;
        private int capacidadPasajeros;
        private double autonomia;
        private TransporteDisplay display;

        public T setModelo(String modelo) { this.modelo = modelo; return self(); }
        public T setFabricante(String fabricante) { this.fabricante = fabricante; return self(); }
        public T setCapacidadPasajeros(int capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; return self(); }
        public T setAutonomia(double autonomia) { this.autonomia = autonomia; return self(); }
        public T setDisplay(TransporteDisplay display) { this.display = display; return self(); }

        protected abstract T self();
        public abstract Transporte build();
    }
}

// ---------------------- PRODUCTOS CONCRETOS --------------------------

/**
 * Clase concreta Avion que extiende Transporte
 */
class Avion extends Transporte {
    private Avion(Builder builder) { super(builder); }
    @Override public String getTipo() { return "Avion"; }
    public static class Builder extends Transporte.Builder<Builder> {
        @Override protected Builder self() { return this; }
        @Override public Avion build() { return new Avion(this); }
    }
}

/**
 * Clase concreta Barco que extiende Transporte
 */
class Barco extends Transporte {
    private Barco(Builder builder) { super(builder); }
    @Override public String getTipo() { return "Barco"; }
    public static class Builder extends Transporte.Builder<Builder> {
        @Override protected Builder self() { return this; }
        @Override public Barco build() { return new Barco(this); }
    }
}

// ---------------------- FACTORIES --------------------------

/**
 * Interfaz de fabrica abstracta para crear transportes
 */
interface TransporteFactory {
    Transporte crearTransporte(String modelo, String fabricante, int capacidad, double autonomia, TransporteDisplay display);
}

/**
 * Fabrica concreta para crear aviones
 */
class AvionFactory implements TransporteFactory {
    @Override
    public Transporte crearTransporte(String modelo, String fabricante, int capacidad, double autonomia, TransporteDisplay display) {
        return new Avion.Builder()
            .setModelo(modelo)
            .setFabricante(fabricante)
            .setCapacidadPasajeros(capacidad)
            .setAutonomia(autonomia)
            .setDisplay(display)
            .build();
    }
}

/**
 * Fabrica concreta para crear barcos
 */
class BarcoFactory implements TransporteFactory {
    @Override
    public Transporte crearTransporte(String modelo, String fabricante, int capacidad, double autonomia, TransporteDisplay display) {
        return new Barco.Builder()
            .setModelo(modelo)
            .setFabricante(fabricante)
            .setCapacidadPasajeros(capacidad)
            .setAutonomia(autonomia)
            .setDisplay(display)
            .build();
    }
}

// ---------------------- CLIENTE --------------------------

/**
 * Clase Main
 * Contiene el menu iterativo que permite al usuario elegir el tipo de visualizacion.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Creamos fabricas
        TransporteFactory avionFactory = new AvionFactory();
        TransporteFactory barcoFactory = new BarcoFactory();

        // Creamos transportes de ejemplo
        Transporte avion = avionFactory.crearTransporte("737 MAX", "Boeing", 210, 6570, null);
        Transporte barco = barcoFactory.crearTransporte("Symphony of the Seas", "Royal Caribbean", 6680, 8000, null);

        boolean continuar = true;

        // Menu iterativo
        while (continuar) {
            System.out.println("Seleccione como desea mostrar la informacion:");
            System.out.println("1. Consola");
            System.out.println("2. Frame");
            System.out.println("3. Web (HTML)");
            System.out.println("4. Salir");
            System.out.print("Opcion: ");
            int opcion = sc.nextInt();

            TransporteDisplay display;
            switch (opcion) {
                case 1 -> display = new ConsoleDisplay();
                case 2 -> display = new FrameDisplay();
                case 3 -> display = new WebDisplay();
                case 4 -> {
                    continuar = false;
                    continue;
                }
                default -> {
                    System.out.println("Opcion no valida.");
                    continue;
                }
            }

            // Aplicamos el Bridge (estrategia de visualizacion)
            avion.setDisplay(display);
            barco.setDisplay(display);

            // Mostramos informacion
            avion.mostrarInformacion();
            barco.mostrarInformacion();
        }

        System.out.println("Programa finalizado.");
        sc.close();
    }