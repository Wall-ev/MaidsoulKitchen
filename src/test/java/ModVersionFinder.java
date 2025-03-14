import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModVersionFinder {
    private static final Properties properties = new Properties();
    public static final String[] SYNC_VERSIONS = new String[]{"1.18.2", "1.19.2", "1.20.1", "1.21.1"};
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(ModVersionFinder.class.getName());

    public static String getVersionLoader(String mcVer) {
        return switch (mcVer) {
            case "1.21.1" -> "neoforge";
            case "1.20.1", "1.19.2", "1.18.2" -> "forge";
            default -> StringUtils.EMPTY;
        };
    }

    public Map<String, String> getSyncModVersionsFromModrinthMaven(String modId, String modVersion) throws IOException {
        String url = String.format("https://api.modrinth.com/v2/project/%s/version", modId);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                JsonNode versions = objectMapper.readTree(response.getEntity().getContent());
                if (versions.isArray() && !versions.isEmpty()) {
                    Map<String, List<ModFileInfo>> modVersionFiles = new HashMap<>();

                    for (JsonNode version : versions) {
                        if (version.get("game_versions") instanceof ArrayNode modVersions) {
                            for (String syncVersion : SYNC_VERSIONS) {
                                if (modVersions.toString().contains(syncVersion)) {
                                    String versionNumber = version.get("version_number").asText();
                                    String versionLoader = getVersionLoader(syncVersion);
                                    String loaders = version.get("loaders").toString().contains(versionLoader) ? versionLoader : StringUtils.EMPTY;
                                    modVersionFiles.merge(syncVersion, Lists.newArrayList(new ModFileInfo(modId, versionNumber, loaders)), (old, newV) -> {
                                        List<ModFileInfo> list = new ArrayList<>(old);
                                        list.addAll(newV);
                                        return list;
                                    });
                                    break;
                                }
                            }
                        }
                    }

                    return getMatchVersion(modVersion, modVersionFiles);
                }
            } catch (IOException e) {
                System.out.println("Failed to fetch version for " + modId + ":" + modVersion);
                e.printStackTrace();
            }

        }
        return Maps.newHashMap();
    }

    public Map<String, String> getMatchVersion(String modVersion, Map<String, List<ModFileInfo>> modVersionFiles) {
        Map<String, String> matchVersion = new HashMap<>();
        for (Map.Entry<String, List<ModFileInfo>> values : modVersionFiles.entrySet()) {
            String versionLoader = getVersionLoader(values.getKey());

            boolean hasMatchVerFile = false;
            for (ModFileInfo modFileInfo : values.getValue()) {
                String version = modFileInfo.version;
                String modLoader = modFileInfo.modLoader;
                if (version.equals(modVersion) && modLoader.equals(versionLoader)) {
                    matchVersion.put(values.getKey(), modVersion);
                    hasMatchVerFile = true;
                    break;
                }
            }

            if (!hasMatchVerFile) {
                for (ModFileInfo modFileInfo : values.getValue()) {
                    if (modFileInfo.modLoader.equals(versionLoader)) {
                        matchVersion.put(values.getKey(), modFileInfo.version);
                        break;
                    }
                }
            }
        }

        for (String syncVersion : SYNC_VERSIONS) {
            if (!matchVersion.containsKey(syncVersion)) {
                matchVersion.put(syncVersion, StringUtils.EMPTY);
            }
        }

        return matchVersion;
    }

    public void loadProperties(String propertiesFilePath) throws IOException {
        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);
        }
    }

    public List<ModInfo> collectModrinthDependencies(String gradleFilePath) throws IOException {
        String gradleContent = new String(Files.readAllBytes(Paths.get(gradleFilePath)));

        Pattern pattern = Pattern.compile("(implementation|compileOnly|runtimeOnly) fg\\.deobf\\(\"([\\w\\.]+):([\\w-]+):\\$\\{([\\w-]+)\\}.*?\"");
        Matcher matcher = pattern.matcher(gradleContent);

        List<ModInfo> modrinthMod = Lists.newArrayList();

        while (matcher.find()) {
            String dependencyType = matcher.group(1);
            String sourceMaven = matcher.group(2);
            String modId = matcher.group(3);
            String versionKey = matcher.group(4);
            String modVersion = properties.getProperty(versionKey);
            String gameVersion = properties.getProperty("minecraft_version");

            System.out.println("Dependency Type: " + dependencyType);
            System.out.println("Source Maven: " + sourceMaven);
            System.out.println("Mod ID: " + modId);
            System.out.println("Mod Version: " + modVersion);
            System.out.println("Game Version: " + gameVersion);
            System.out.println();


            if ("maven.modrinth".equals(sourceMaven)) {
                modrinthMod.add(new ModInfo(modId, modVersion));
            }
        }

        return modrinthMod;
    }

    public void load(String modId, String version) throws IOException {
        this.loadProperties("setting/version/1.20.1/gradle.properties");
        List<ModInfo> modInfos = this.collectModrinthDependencies("setting/common/dependencies.gradle");

        System.out.println("processing " + modId + ":" + version);
        Map<String, Map<String, String>> modVerInfo = new HashMap<>();
        Map<String, String> versions = this.getSyncModVersionsFromModrinthMaven(modId, version);
        modVerInfo.put(modId, versions);

        for (String syncVersion : SYNC_VERSIONS) {
            System.out.println("> " + syncVersion);
            for (Map.Entry<String, Map<String, String>> entry : modVerInfo.entrySet()) {
                String modId1 = entry.getKey();
                String modVer1 = entry.getValue().get(syncVersion);
                System.out.println(modId1.replace("-", "_").concat("_version") + "=" + modVer1);
            }
            System.out.println();
        }
    }

    public void loadAllModrinth() {
        try {
            this.loadProperties("setting/version/1.20.1/gradle.properties");
            List<ModInfo> modInfos = this.collectModrinthDependencies("setting/common/dependencies.gradle");

            Map<String, Map<String, String>> modVerInfo = new HashMap<>();
            for (ModInfo modInfo : modInfos) {
                System.out.println("processing " + modInfo.modId + ":" + modInfo.version);
                Map<String, String> versions = this.getSyncModVersionsFromModrinthMaven(modInfo.modId, modInfo.version);
                modVerInfo.put(modInfo.modId, versions);
                Thread.sleep(1000); // Wait for 1 second
            }

            int a = 1;
            for (String syncVersion : SYNC_VERSIONS) {
                System.out.println("> " + syncVersion);
                for (Map.Entry<String, Map<String, String>> entry : modVerInfo.entrySet()) {
                    String modId = entry.getKey();
                    String modVer = entry.getValue().get(syncVersion);
                    System.out.println(modId.replace("-", "_").concat("_version") + "=" + modVer);
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public ModVersionFinder() {
    }

    public static void main(String[] args) throws IOException {
//        new ModVersionFinder().loadAllModrinth();
        new ModVersionFinder().load("kotlin-for-forge", "4.10.0");
    }

    public record ModInfo(String modId, String version) {
    }
    public record ModFileInfo(String modId, String version, String modLoader) {
    }
}
