package dev.negativekb.api.util.cache;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

@RequiredArgsConstructor
@SuppressWarnings("all")
public abstract class ObjectHashCache<K, V> {

    private final String path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Save the Cache to the JSON file
     *
     * @param cacheHashMap Class Type HashMap
     */
    public void save(@NotNull HashMap<K, V> cacheHashMap) throws IOException {
        File file = getFile(path);
        file.getParentFile().mkdir();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(cacheHashMap, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Load the Cache from the JSON file
     *
     * @return A new instance of an ArrayList with the new Cache
     */
    @NotNull
    public HashMap<K, V> load() throws IOException {
        File file = getFile(path);
        HashMap<K, V> loaded = new HashMap<>();
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<K, V>>(){}.getType();
            loaded = gson.fromJson(reader, type);
            return loaded;
        }
        return loaded;
    }

    private File getFile(@NotNull String path) {
        return new File(path);
    }

}