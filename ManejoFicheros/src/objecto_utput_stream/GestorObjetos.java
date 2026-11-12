package objecto_utput_stream;

import java.io.*;

public class GestorObjetos {
    
    // ЗАПИСЬ ОБЪЕКТА (Сериализация)
    public void guardarPersona(String ruta, Persona p) {
        // FileOutputStream открывает файл для записи байтов.
        // ObjectOutputStream - это "волшебная труба", которая умеет брать целый объект Java, 
        // разбирать его на байты (сериализовать) и передавать в FileOutputStream для сохранения.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            
            // В этом главная прелесть сериализации! Нам не нужно писать отдельно имя, потом возраст.
            // Мы сохраняем ВЕСЬ объект одной короткой командой.
            oos.writeObject(p); 
            
            System.out.println("Objeto serializado correctamente."); // Объект успешно сериализован
            
        } catch (IOException e) {
            System.out.println("Error al guardar el objeto: " + e.getMessage());
        }
    }

    // ЧТЕНИЕ ОБЪЕКТА (Десериализация)
    public void leerPersonas(String ruta) {
        // ObjectInputStream читает байты из файла и собирает их обратно в полноценные объекты Java.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            
            // Запускаем бесконечный цикл на случай, если в файле сохранено несколько человек
            while (true) {
                // Метод readObject() возвращает тип "Object" (общий тип для всего в Java).
                // Но мы-то знаем, что сохраняли туда Persona. Поэтому мы делаем "кастинг" 
                // (преобразование типа): пишем (Persona) перед методом.
                Persona persona = (Persona) ois.readObject();
                
                System.out.println("Persona leída: " + persona.getNombre() + ", edad: " + persona.getEdad());
            }
            
        } catch (EOFException e) {
            // Как и с бинарными файлами, это нормальный способ выйти из цикла while(true).
            // Когда объекты в файле закончатся, Java выбросит EOFException.
            System.out.println("Fin del archivo alcanzado.");
            
        } catch (ClassNotFoundException e) {
            // Это новое исключение! Оно сработает, если мы прочитаем байты, поймем, 
            // что это объект "Persona", но самого класса Persona в нашем проекте не окажется.
            System.out.println("Clase Persona no encontrada en el proyecto.");
            
        } catch (IOException e) {
            System.out.println("Error de lectura: " + e.getMessage());
        }
    }

    // Точка входа для проверки того, как это работает
    public static void main(String[] args) {
        GestorObjetos gestor = new GestorObjetos();
        String ruta = "personas.obj"; // Расширение может быть любым (часто используют .dat, .bin, .obj)

        System.out.println("--- Etapa 1: Guardar Objeto ---");
        // Создаем объект человека
        Persona empleado = new Persona("12345678A", "Carl", 28);
        // Сохраняем его в файл
        gestor.guardarPersona(ruta, empleado);

        System.out.println("\n--- Etapa 2: Leer Objeto ---");
        // Читаем из файла
        gestor.leerPersonas(ruta);
    }
}