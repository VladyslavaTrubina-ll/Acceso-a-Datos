package binarios;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArchivoApp7 {

    public static void main(String[] args) {
        // Для бинарных файлов (данных) принято использовать расширение .dat (от слова data), а не .txt
        String archivo = "pedido.dat"; 

        System.out.println("--- Etapa 1: Escritura ---");
        escribirPedido(archivo);
        
        System.out.println("\n--- Etapa 2: Lectura ---");
        leerPedido(archivo);
    }

    // МЕТОД ЗАПИСИ (Escritura)
    public static void escribirPedido(String nombreArchivo) {
        double[] precios = {1350, 400, 890, 6200, 8730};
        int[] unidades = {5, 7, 12, 8, 30};
        String[] descripciones = {"paquetes de papel", "lápices", "bolígrafos", "carteras", "mesas"};

        // DataOutputStream позволяет записывать примитивные типы Java (int, double, String) в бинарный файл.
        // Блок try-with-resources автоматически закроет поток (salida.close()) после записи.
        try (DataOutputStream salida = new DataOutputStream(new FileOutputStream(nombreArchivo))) {
            for (int i = 0; i < precios.length; i++) {
                
                // ЭКЗАМЕНАЦИОННЫЙ СЕКРЕТ 1: Используем writeUTF() для строк.
                // Метод writeUTF автоматически сохраняет невидимый маркер длины перед самим текстом. 
                // Благодаря этому Java сама поймет при чтении, где строка заканчивается. Никакие '\n' не нужны!
                salida.writeUTF(descripciones[i]);
                
                // ЭКЗАМЕНАЦИОННЫЙ СЕКРЕТ 2: Пишем числа впритык. 
                // Никаких пробелов или знаков табуляции '\t' между ними не нужно! Бинарный поток - это не текст.
                // int всегда занимает ровно 4 байта, а double - 8 байт. Машина сама отсчитает нужные куски.
                salida.writeInt(unidades[i]);
                salida.writeDouble(precios[i]);
            }
            System.out.println("Datos del pedido escritos correctamente."); // Данные заказа успешно записаны
            
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    // МЕТОД ЧТЕНИЯ (Lectura)
    public static void leerPedido(String nombreArchivo) {
        double total = 0.0;

        try (DataInputStream entrada = new DataInputStream(new FileInputStream(nombreArchivo))) {
            
            // ЭКЗАМЕНАЦИОННЫЙ СЕКРЕТ 3: Бесконечный цикл while(true).
            // В бинарных файлах мы часто не знаем заранее, сколько там записей. Метод readLine() здесь не работает.
            // Поэтому мы читаем бесконечно, пока не сработает исключение конца файла.
            while (true) {
                
                // ЭКЗАМЕНАЦИОННЫЙ СЕКРЕТ 4: Порядок чтения ДОЛЖЕН СТРОГО СОВПАДАТЬ с порядком записи!
                // Мы писали String -> int -> double. Значит, читаем точно так же.
                String descripcion = entrada.readUTF();
                int unidad = entrada.readInt();
                double precio = entrada.readDouble();
                
                System.out.println("Has pedido " + unidad + " " + descripcion + " a " + precio + " pts.");
                
                total += (unidad * precio);
            }
            
        } catch (EOFException e) {
            // ЭКЗАМЕНАЦИОННЫЙ СЕКРЕТ 5: EOFException (End Of File) — это НЕ ошибка в данном случае!
            // Это единственный штатный способ выйти из цикла while(true), когда данные закончились.
            // Именно здесь, когда файл дочитан, мы выводим финальный итог (Total).
            System.out.println("Lectura finalizada. Por un TOTAL de: " + total + " pts.");
            
        } catch (IOException e) {
            // А вот IOException - это настоящая системная ошибка (например, нет прав доступа к файлу)
            System.out.println("Error de entrada/salida al leer el archivo: " + e.getMessage());
        }
    }
}