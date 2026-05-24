package gamemasters;

import model.IRiddle;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public abstract class GameMaster {
    private final String name;
    private final List<IRiddle> riddlePool;
    private IRiddle currentRiddle;
    private static final Random random = new Random();

    protected static final Properties env = new Properties();
    static {
        try(FileReader r = new FileReader(".env")){
            env.load(r);
        }catch (IOException ignored){}
    }

    public GameMaster(String name, List<IRiddle> riddlePool) {
        this.name = name;
        this.riddlePool = new ArrayList<>();

        // Filter out invalid riddles
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
            if(value.startsWith("\"") && value.endsWith("\"")){
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
            // Fallback riddle if pool is empty due to .env errors
            this.currentRiddle = new model.RiddleImpl("Error loading riddles.", "error", "Check .env file.");
        }
    }

    public String getName() { return name; }
    public IRiddle getRiddle() { return currentRiddle; }
    public void rerollRiddle() { selectRandomRiddle(); }
    public abstract String greet();
    public abstract void startGame();
}