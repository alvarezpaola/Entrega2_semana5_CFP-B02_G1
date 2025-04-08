package ventas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateInfoFiles {
    private static final String[][] SALESMEN = {
            {"CC", "001", "JUAN", "ALEGRIAS"},
            {"CC", "002", "KEVIN", "ALEJO"},
            {"CC", "003", "PAOLA", "ALVAREZ"},
            {"CC", "004", "DANIELA", "ALZATE"},
            {"CC", "005", "DAYRA", "ARAGON"}
    };

    private static final String[][] PRODUCTS = {
            {"P001", "Mouse", "30000"},
            {"P002", "Teclado", "60000"},
            {"P003", "Audifonos", "45000"},
            {"P004", "Cargador de Portatil", "70000"},
            {"P005", "Laptop", "2500000"},
            {"P006", "Pantalla", "800000"},
            {"P007", "PC de Torre", "2000000"},
            {"P008", "PacMouse", "25000"}
    };

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        try {
            createProductsFile(PRODUCTS.length);
            createSalesManInfoFile(SALESMEN.length);
            for (String[] salesman : SALESMEN) {
                String name = salesman[2] + " " + salesman[3];
                long id = Long.parseLong(salesman[1]);
                createSalesMenFile(3 + RANDOM.nextInt(5), name, id);
            }
            System.out.println("Archivos generados exitosamente.");
        } catch (IOException e) {
            System.err.println("Error generando archivos: " + e.getMessage());
        }
    }

    public static void createProductsFile(int productsCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("productos.csv"))) {
            for (int i = 0; i < productsCount; i++) {
                String[] prod = PRODUCTS[i];
                writer.write(prod[0] + ";" + prod[1] + ";" + prod[2] + "\n");
            }
        }
    }

    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vendedores.csv"))) {
            for (int i = 0; i < salesmanCount; i++) {
                String[] s = SALESMEN[i];
                writer.write(s[0] + ";" + s[1] + ";" + s[2] + ";" + s[3] + "\n");
            }
        }
    }

    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "ventas_" + id + ".csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("CC;" + id + ";");
            for (int i = 0; i < randomSalesCount; i++) {
                int prodIndex = RANDOM.nextInt(PRODUCTS.length);
                String productId = PRODUCTS[prodIndex][0];
                int cantidad = 1 + RANDOM.nextInt(5);
                writer.write(productId + ";" + cantidad + ";");
            }
        }
    }
}
