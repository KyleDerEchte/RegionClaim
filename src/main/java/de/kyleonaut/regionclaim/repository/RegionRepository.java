package de.kyleonaut.regionclaim.repository;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.entity.Region;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@RequiredArgsConstructor
public class RegionRepository {
    private final RegionClaimPlugin plugin;

    public List<Region> load() {
        try {
            final File dir = plugin.getDataFolder();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, "region.json");
            if (!file.exists()) {
                file.createNewFile();
                save(new ArrayList<>());
            }
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            return mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void save(List<Region> regions) {
        try {
            final File dir = plugin.getDataFolder();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, "region.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(new File(plugin.getDataFolder() + "/region.json"), regions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
