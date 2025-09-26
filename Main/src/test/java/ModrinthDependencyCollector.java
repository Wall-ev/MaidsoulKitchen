//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ModrinthDependencyCollector {
//
//    private static final Properties properties = new Properties();
//
//    public static void loadProperties(String propertiesFilePath) throws IOException {
//        try (FileInputStream input = new FileInputStream(propertiesFilePath)) {
//            properties.load(input);
//        }
//    }
//
//    public static void collectModrinthDependencies(String gradleFilePath) throws IOException {
//        String gradleContent = new String(Files.readAllBytes(Paths.get(gradleFilePath)));
//
//        Pattern pattern = Pattern.compile("(implementation|compileOnly|runtimeOnly) fg\\.deobf\\(\"([\\w\\.]+):([\\w-]+):\\$\\{([\\w-]+)\\}.*?\"");
//        Matcher matcher = pattern.matcher(gradleContent);
//
//        while (matcher.find()) {
//            String dependencyType = matcher.group(1);
//            String sourceMaven = matcher.group(2);
//            String modId = matcher.group(3);
//            String versionKey = matcher.group(4);
//            String modVersion = properties.getProperty(versionKey);
//            String gameVersion = properties.getProperty("minecraft_version");
//
//            System.out.println("Dependency Type: " + dependencyType);
//            System.out.println("Source Maven: " + sourceMaven);
//            System.out.println("Mod ID: " + modId);
//            System.out.println("Mod Version: " + modVersion);
//            System.out.println("Game Version: " + gameVersion);
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        try {
//            loadProperties("setting/version/1.20.1/gradle.new.properties");
//            collectModrinthDependencies("setting/version/1.20.1/dependencies.new.gradle");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}