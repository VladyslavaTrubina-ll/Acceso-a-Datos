package json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

public class LecturaJsonCoches {

    public static void main(String[] args) {
        // Предполагается, что файл coches.json выглядит так (массив объектов):
        // [ {"Marca": "Ford", "Modelo": "Focus", "Precio": 15000, "Venta": true}, ... ]
        
        // JsonParser - это инструмент, который читает текст и превращает его в структуру данных Java.
        JsonParser parser = new JsonParser();
        final String url = "coches.json";

        try {
            FileReader fr = new FileReader(url);
            
            // --- ШАГ 1: Чтение базового элемента ---
            // JsonElement - это "корень" всего в GSON. Он может быть массивом, объектом или простым значением.
            // На данном этапе Java еще не знает, что именно лежит в файле, просто какой-то "элемент".
            JsonElement datos = parser.parse(fr);
            
            // --- ШАГ 2: Преобразование в массив ---
            // Поскольку наш JSON начинается с квадратных скобок [...], мы точно знаем, 
            // что это массив. Поэтому смело преобразуем базовый JsonElement в JsonArray.
            JsonArray array = datos.getAsJsonArray();
            
            // Создаем итератор для перебора всех автомобилей в массиве
            Iterator<JsonElement> iter = array.iterator();

            System.out.println("--- LISTA DE COCHES ---");

            // Проходим по каждому элементу массива (каждому автомобилю)
            while (iter.hasNext()) {
                JsonElement entrada = iter.next(); // Берем следующий автомобиль
                
                // --- ШАГ 3: Работа с объектом ---
                // Каждый автомобиль внутри {...} - это объект. Преобразуем его в JsonObject.
                JsonObject objeto = entrada.getAsJsonObject();
                
                // Создаем итератор для перебора свойств внутри ОДНОГО автомобиля (Марка, Модель, Цена...)
                // ИСПРАВЛЕНА ОШИБКА: метод entrySet() возвращает пары "Ключ-Значение", 
                // поэтому итератор должен быть типа Map.Entry<String, JsonElement>
                Iterator> iter2 = objeto.entrySet().iterator();

                // ВАЖНО ДЛЯ ЭКЗАМЕНА: Этот способ чтения (через next() 4 раза подряд) сработает ТОЛЬКО если 
                // свойства в JSON записаны строго в одном и том же порядке во всех объектах!
                // На экзамене так делают часто, но в реальной разработке лучше искать по ключу: 
                // objeto.get("Marca").getAsString()

                // 1. Читаем Марку (ожидаем текст - String)
                // iter2.next() берет пару ("Marca":"Ford")
                // .getValue() достает значение ("Ford" в виде JsonElement)
                // .getAsJsonPrimitive() говорит: "это простое значение (строка, число, boolean), а не массив/объект"
                JsonPrimitive valor = iter2.next().getValue().getAsJsonPrimitive();
                System.out.println("\tMarca: " + valor.getAsString());

                // 2. Читаем Модель (ожидаем текст - String)
                JsonPrimitive valor2 = iter2.next().getValue().getAsJsonPrimitive();
                System.out.println("\tModelo: " + valor2.getAsString());

                // 3. Читаем Цену (ожидаем число - Number)
                JsonPrimitive valor3 = iter2.next().getValue().getAsJsonPrimitive();
                System.out.println("\tPrecio: " + valor3.getAsNumber());

                // 4. Читаем Статус продажи (ожидаем логическое значение - Boolean)
                JsonPrimitive valor4 = iter2.next().getValue().getAsJsonPrimitive();
                System.out.println("\tVenta: " + valor4.getAsBoolean());
                
                System.out.println("-----------------------");
            }
            
            fr.close(); // Обязательно закрываем файл

        } catch (Exception e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }
}