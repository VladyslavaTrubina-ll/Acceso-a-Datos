package random_access_file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ManejoAleatorio {

    public static void main(String[] args) {
        // Указываем файл. Расширение .dat говорит о том, что это бинарный файл данных.
        File fichero = new File("AleatorioEmple.dat");

        try {
            // RandomAccessFile - это главный герой этой темы.
            // Режим "rw" (read/write) означает, что мы можем и читать, и писать в этот файл.
            RandomAccessFile file = new RandomAccessFile(fichero, "rw");
            
            // --- 1. ЗАПИСЬ ДАННЫХ (Последовательная инициализация файла) ---
            String apellido[] = {"FERNANDEZ", "GIL", "LOPEZ", "RAMOS", "SEVILLA", "CASILLA", "REY"};
            int dep[] = {10, 20, 10, 10, 30, 30, 20};
            Double salario[] = {1000.45, 2400.60, 3000.0, 1500.56, 2200.0, 1435.87, 2000.0};
            
            StringBuffer buffer = null;
            int n = apellido.length;
            
            for (int i = 0; i < n; i++) {
                // Записываем ID (int занимает 4 байта)
                file.writeInt(i + 1); 
                
                // ВАЖНО ДЛЯ ЭКЗАМЕНА: Чтобы произвольный доступ работал, КАЖДАЯ ЗАПИСЬ 
                // ДОЛЖНА БЫТЬ ОДИНАКОВОГО РАЗМЕРА.
                // Поэтому мы жестко фиксируем длину фамилии в 10 символов.
                // Если фамилия короче ("GIL"), она добьется пробелами. 
                // Если длиннее — обрежется.
                buffer = new StringBuffer(apellido[i]);
                buffer.setLength(10); 
                // writeChars записывает строку как набор символов.
                // Каждый символ (char) в Java весит 2 байта. 10 символов * 2 байта = 20 байт.
                file.writeChars(buffer.toString());
                
                // Записываем номер отдела (int занимает 4 байта)
                file.writeInt(dep[i]);
                
                // Записываем зарплату (double занимает 8 байт)
                file.writeDouble(salario[i]);
                
                // ИТОГО размер одной записи: 4 (ID) + 20 (Фамилия) + 4 (Отдел) + 8 (Зарплата) = 36 байт!
            }
            System.out.println("Base de empleados creada."); // База сотрудников создана.


            // --- 2. ДОБАВЛЕНИЕ/ПЕРЕЗАПИСЬ КОНКРЕТНОГО РЕГИСТРА (Доступ по ID) ---
            int idNuevo = 20; // Хотим записать сотрудника сразу под номером 20
            
            // ВАЖНАЯ ФОРМУЛА: (ID - 1) * РАЗМЕР_ЗАПИСИ
            // Если мы хотим 1-го сотрудника: (1 - 1) * 36 = 0 (начало файла).
            // Если 2-го: (2 - 1) * 36 = 36 (пропускаем первого).
            // Для 20-го: (20 - 1) * 36 = 684 байт.
            long posicion = (idNuevo - 1) * 36; 
            
            // Метод seek() перемещает "курсор" внутри файла на нужный байт.
            // Между 7-м и 20-м сотрудником в файле образуется "пустота".
            /*.seek(long pos): перемещает указатель в файле. 
             * Всегда делай расчет (ID - 1) * размер_записи перед его использованием.*/
            file.seek(posicion); 
            
            // Записываем данные 20-го сотрудника (строго соблюдая структуру и размер 36 байт)
            file.writeInt(idNuevo);
            buffer = new StringBuffer("GONZALEZ");
            buffer.setLength(10);
            file.writeChars(buffer.toString());
            file.writeInt(10);
            file.writeDouble(1230.87);
            
            System.out.println("Empleado 20 anadido correctamente."); // Сотрудник 20 успешно добавлен.


            // --- 3. ЧТЕНИЕ КОНКРЕТНОГО РЕГИСТРА (Поиск по ID) ---
            int registroAConsultar = 20; // Ищем нашего 20-го сотрудника
            posicion = (registroAConsultar - 1) * 36;
            
            // Защита от ошибки: проверяем, не выходит ли вычисленная позиция за реальный размер файла.
            if (posicion < file.length()) {
                // Снова используем seek(), чтобы мгновенно прыгнуть к нужной записи, не читая предыдущие 19.
                file.seek(posicion);
                
                int id = file.readInt();
                
                // Чтение строки по символам. Мы знаем, что фамилия занимает ровно 10 символов (20 байт).
                char[] apellidoChar = new char[10];
                char aux;
                for (int i = 0; i < apellidoChar.length; i++) {
                    aux = file.readChar(); // Читаем по одной букве
                    apellidoChar[i] = aux; // Сохраняем в массив
                }
                // Превращаем массив символов в строку. 
                String apellidos = new String(apellidoChar);
                
                int departamento = file.readInt();
                Double sal = file.readDouble();
                
                // .trim() удаляет лишние пробелы в конце, если фамилия была короче 10 букв.
                System.out.println("ID: " + id + ", Apellido: " + apellidos.trim() + 
                                   ", Departamento: " + departamento + ", Salario: " + sal);
            } else {
                System.out.println("ID: " + registroAConsultar + ", NO EXISTE EMPLEADO...");
            }

            // Обязательно закрываем файл, чтобы освободить ресурсы операционной системы.
            file.close(); 
            
        } catch (IOException e) {
            System.out.println("Error al manejar el archivo: " + e.getMessage());
        }
    }
}