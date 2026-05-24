package gamemasters;
import model.IRiddle;
import model.RiddleImpl;

import java.util.List;
import java.util.stream.IntStream;
public class Awit extends GameMaster {
    private static final List<IRiddle> RIDDLE_POOL = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> new RiddleImpl(getEnv("AWIT_RIDDLE_" + i), getEnv("AWIT_ANSWER_" + i), getEnv("AWIT_HINT_" + i)))
            .map(r -> (IRiddle) r)
            .toList();
    public Awit() {
        super(RIDDLE_POOL);
    }
    @Override
    public String greet() {
        return "Awit whispers, 'Listen closely to my riddle...'";
    }
}
