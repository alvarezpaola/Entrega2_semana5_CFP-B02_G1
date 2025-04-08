package ventas;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static class Vendedor {
        String tipoDoc;
        String id;
        String nombreCompleto;
        long totalVentas = 0;

        Vendedor(String tipoDoc, String id, String nombre, String apellido) {
            this.tipoDoc = tipoDoc;
            this.id = id;
            this.nombreCompleto = nombre + " " + apellido;
        }
    }

    static class Producto {
        String id;
        String nombre;
        int precio;
        int cantidadVendida = 0;

        Producto(String id, String nombre, int precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
    }

    public static void main(String[] args) {
        try {
            Map<String, Producto> productos = cargarProductos("productos.csv");
            Map<String, Vendedor> vendedores = cargarVendedores("vendedores.csv");

            File carpeta = new File(".");
            File[] archivosVentas = carpeta.listFiles((dir, name) -> name.startsWith("ventas_"));
            if (archivosVentas != null) {
                for (File archivo : archivosVentas) {
                    procesarArchivoVentas(archivo, vendedores, productos);
                }
            }

            generarReporteVendedores(vendedores);
            generarReporteProductos(productos);

            System.out.println("Reportes generados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error procesando archivos: " + e.getMessage());
        }
    }

    static Map<String, Producto> cargarProductos(String archivo) throws IOException {
        Map<String, Producto> mapa = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                mapa.put(partes[0], new Producto(partes[0], partes[1], Integer.parseInt(partes[2])));
            }
        }
        return mapa;
    }

    static Map<String, Vendedor> cargarVendedores(String archivo) throws IOException {
        Map<String, Vendedor> mapa = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                mapa.put(partes[1], new Vendedor(partes[0], partes[1], partes[2], partes[3]));
            }
        }
        return mapa;
    }

    static void procesarArchivoVentas(File archivo, Map<String, Vendedor> vendedores, Map<String, Producto> productos) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();
            if (linea == null) return;
            String[] partes = linea.split(";");
            String id = partes[1];
            Vendedor vendedor = vendedores.get(id);
            if (vendedor == null) return;

            for (int i = 2; i < partes.length - 1; i += 2) {
                String prodId = partes[i];
                int cantidad = Integer.parseInt(partes[i + 1]);
                Producto prod = productos.get(prodId);
                if (prod != null && cantidad > 0) {
                    vendedor.totalVentas += (long) prod.precio * cantidad;
                    prod.cantidadVendida += cantidad;
                }
            }
        }
    }

    static void generarReporteVendedores(Map<String, Vendedor> vendedores) throws IOException {
        List<Vendedor> lista = new ArrayList<>(vendedores.values());
        lista.sort((a, b) -> Long.compare(b.totalVentas, a.totalVentas));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
            for (Vendedor v : lista) {
                writer.write(v.nombreCompleto + ";" + v.totalVentas + "\n");
            }
        }
    }

    static void generarReporteProductos(Map<String, Producto> productos) throws IOException {
        List<Producto> lista = new ArrayList<>(productos.values());
        lista.sort((a, b) -> Integer.compare(b.cantidadVendida, a.cantidadVendida));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
            for (Producto p : lista) {
                writer.write(p.nombre + ";" + p.precio + "\n");
            }
        }
    }
}
