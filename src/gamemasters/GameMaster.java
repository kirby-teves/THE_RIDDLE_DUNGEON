package gamemasters;
import model.IRiddle;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
public abstract class GameMaster {
    private final List<IRiddle> riddlePool;
    private IRiddle currentRiddle;
    private static final Random random = new Random();

    protected static final Properties env = new Properties();
    static {
        loadEnv();
    }
    private static void loadEnv() {
        String[] paths = {".env", "src/.env"};

        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    env.load(reader);
                    return;
                } catch (IOException e) {
                    System.err.println("Failed to load riddles from " + file.getAbsolutePath() + ": " + e.getMessage());
                }
            }
        }
        try (InputStream stream = GameMaster.class.getClassLoader().getResourceAsStream(".env")) {
            if (stream != null) {
                env.load(stream);
            } else {
                System.err.println("No .env file found. Checked .env, src/.env, and classpath .env.");
            }
        } catch (IOException e) {
            System.err.println("Failed to load riddles from classpath .env: " + e.getMessage());
        }
    }
    public GameMaster(List<IRiddle> riddlePool) {
        this.riddlePool = new ArrayList<>();

        for (IRiddle riddle : riddlePool) {
            if (riddle != null &&
                    riddle.getQuestion() != null && !riddle.getQuestion().trim().isEmpty() &&
                    riddle.getAnswer() != null && !riddle.getAnswer().trim().isEmpty()) {
                this.riddlePool.add(riddle);
            }
        }

        selectRandomRiddle();
    }
    public static String getEnv(String key){
        String value = env.getProperty(key);
        if(value != null){
            value = value.trim();
            if((value.startsWith("\"") && value.endsWith("\"")) ||
                    (value.startsWith("'") && value.endsWith("'"))){
                value = value.substring(1, value.length() - 1);
            }
            return value.trim();
        }
        return "";
    }
    private void selectRandomRiddle(){
        if(!riddlePool.isEmpty()){
            this.currentRiddle = riddlePool.get(random.nextInt(riddlePool.size()));
        } else {
            this.currentRiddle = new model.RiddleImpl("Error loading riddles.", "error", "Check .env file.");
        }
    }
    public IRiddle getRiddle() { return currentRiddle; }
    public abstract String greet();
}
