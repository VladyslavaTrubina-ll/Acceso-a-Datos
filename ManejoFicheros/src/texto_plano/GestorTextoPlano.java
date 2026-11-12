package texto_plano;

import java.io.*;

public class GestorTextoPlano {
    // ЗАПИСЬ текста
    public void escribirTexto(String ruta) {
        // Параметр 'true' в FileWriter означает добавление текста в конец файла (append).
        // Если убрать 'true', файл будет перезаписываться каждый раз.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, true))) {
            bw.write("Fila numero: 2"); 
            bw.newLine(); // Перенос на новую строку
            System.out.println("Текст успешно записан.");
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }

    // ЧТЕНИЕ текста
    public void leerTexto(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(ruta)))) {
            String linea;
            // Читаем построчно, пока не встретим null (конец файла)
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
        } catch (IOException e) {
            System.out.println("Ошибка чтения.");
        }
    }
    
    public static void main(String[] args) {
        // Создаем объект нашего класса
        GestorTextoPlano gestor = new GestorTextoPlano();
        
        // Указываем путь к файлу (если указать просто имя, он создастся в корне проекта)
        String ruta = "archivo_prueba.txt"; 

        // 1. Записываем текст
        System.out.println("--- Запуск записи ---");
        gestor.escribirTexto(ruta);

        // 2. Читаем текст
        System.out.println("\n--- Запуск чтения ---");
        gestor.leerTexto(ruta);
    }
}
