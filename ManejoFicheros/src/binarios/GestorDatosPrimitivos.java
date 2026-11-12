package binarios;

import java.io.*;

public class GestorDatosPrimitivos {

    // ЗАПИСЬ примитивных типов и строк в бинарный файл
    public void escribirPedidos(String ruta) {
        // Данные, которые мы хотим сохранить (описание, количество, цена)
        String[] descripciones = {"lapices", "mesas"};
        int[] unidades = {9, 30};
        double[] precios = {400.0, 8730.0};

        // FileOutputStream открывает файл для записи байтов.
        // DataOutputStream "оборачивает" его, чтобы мы могли записывать не просто сырые байты,
        // а конкретные типы данных Java (int, double, String).
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta))) {
            
            for (int i = 0; i < descripciones.length; i++) {
                // Важно! Мы записываем данные в определенном порядке: Строка -> Целое -> Дробное
                dos.writeUTF(descripciones[i]); // writeUTF используется для записи строк (текста)
                dos.writeInt(unidades[i]);      // записываем int (4 байта)
                dos.writeDouble(precios[i]);    // записываем double (8 байт)
            }
            System.out.println("Datos binarios del pedido guardados correctamente.");
            
        } catch (IOException e) {
            System.out.println("Error de escritura: " + e.getMessage());
        }
    }

    // ЧТЕНИЕ примитивных типов из бинарного файла
    public void leerPedidos(String ruta) {
        double total = 0.0; // Переменная для подсчета общей суммы заказа
        
        // FileInputStream читает сырые байты из файла.
        // DataInputStream позволяет собирать эти байты обратно в переменные Java (int, double, String).
        try (DataInputStream dis = new DataInputStream(new FileInputStream(ruta))) {
            
            // Бесконечный цикл. В бинарных файлах мы часто не знаем заранее, сколько там записей.
            while (true) {
                // ЗОЛОТОЕ ПРАВИЛО: Порядок чтения ДОЛЖЕН строго совпадать с порядком записи!
                // Писали String -> int -> double, значит и читаем String -> int -> double.
                // Если перепутать, программа прочитает кусок текста как число и выдаст ошибку или бред.
                String desc = dis.readUTF();
                int ud = dis.readInt();
                double precio = dis.readDouble();
                
                System.out.println("Producto: " + desc + ", cantidad: " + ud + ", precio: " + precio);
                
                // Считаем стоимость (количество * цена) и добавляем к итогу
                total += (ud * precio);
            }
            
        } catch (EOFException e) {
            // EOF (End Of File) - Конец файла. 
            // Когда dis.readUTF() попытается прочитать данные, а файл уже закончился, 
            // Java выбросит это исключение. Здесь это не ошибка, а нормальный способ выйти из while (true).
            System.out.println("Lectura completada. Total: " + total);
            
        } catch (IOException e) {
            System.out.println("Error de lectura del archivo: " + e.getMessage());
        }
    }

    // Точка входа в программу
    public static void main(String[] args) {
        GestorDatosPrimitivos gestor = new GestorDatosPrimitivos();
        
        // Расширение .dat или .bin часто используется для бинарных файлов (данных), 
        // чтобы показать, что это не обычный текстовый файл (.txt)
        String ruta = "pedidos.dat";

        System.out.println("--- Etapa 1: Escritura ---");
        gestor.escribirPedidos(ruta);

        System.out.println("\n--- Etapa 2: Lectura ---");
        gestor.leerPedidos(ruta);
        
        // Попробуй открыть созданный файл pedidos.dat в обычном блокноте.
        // Ты увидишь "кракозябры" - это потому, что числа записаны в машинном (бинарном) виде, а не как текст.
    }
}