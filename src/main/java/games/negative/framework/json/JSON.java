package games.negative.framework.json;

import games.negative.framework.database.annotation.DontSave;
import games.negative.framework.json.annotation.JSONConstructor;
import games.negative.framework.json.annotation.JSONValue;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

@Getter
@Setter
public class JSON {

    private final File file;

    /**
     * Constructor for this class
     * @param file The file to read from
     * @throws IllegalStateException if the file is a directory
     */
    @SneakyThrows(IOException.class)
    public JSON(File file) throws IllegalStateException {
        if (!file.exists())
            file.createNewFile();

        if (!file.getName().endsWith(".json"))
            file.renameTo(new File(file.getAbsolutePath() + ".json"));

        this.file = file;
    }

    /**
     * Write a JSON string to the file
     * @param json The JSON string to write
     */
    public void write(String json) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the file and return a {@link JSONObject}
     * @return The {@link JSONObject}
     */
    @NotNull
    public JSONObject read() {
        try {
            return new JSONObject(new java.util.Scanner(file).useDelimiter("\\Z").next());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    /**
     * Delete the file
     */
    public void delete() {
        file.delete();
    }

    /**
     * Write a {@code Java Object} to a JSON file
     * @param object
     * @throws IllegalAccessException
     */
    public void writeObject(Object object) throws IllegalAccessException {
        JSONObject json = new JSONObject();

        for (Field field : object.getClass().getFields()) {
            if (field.isAnnotationPresent(DontSave.class))
                continue;

            String fieldName = field.getName();

            if (field.isAnnotationPresent(JSONValue.class))
                fieldName = field.getAnnotation(JSONValue.class).value();

            field.setAccessible(true);
            json.put(fieldName, field.get(object));
        }

        write(json.toString());
    }

    /**
     * Read a {@code Java Object} from a JSON file
     * @param clazz The class of the object to read
     * @throws IllegalStateException If there isn't a valid constructor
     */
    @Nullable
    public Object readObject(Class<?> clazz) throws IllegalStateException {
        Constructor<?> constructor = locateConstructor(clazz);
        if (constructor == null)
            throw new IllegalStateException("[FRAMEWORK EXCEPTION] No constructor found for class " + clazz.getName());

        ArrayList<Object> parameters = new ArrayList<>();

        JSONObject json = read();
        for (Parameter p : constructor.getParameters()) {
            String name = p.getName();
            if (p.isAnnotationPresent(JSONValue.class))
                name = p.getAnnotation(JSONValue.class).value();
            parameters.add(json.get(name));
        }

        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Locates the correct constructor to use
     * @param clazz The class to use
     * @return The correct constructor
     */
    private Constructor<?> locateConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.isAnnotationPresent(JSONConstructor.class))
                return constructor;
        }
        return null;
    }



}