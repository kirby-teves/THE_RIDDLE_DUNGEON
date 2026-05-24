package gamemasters;
import model.IRiddle;
import model.RiddleImpl;

import java.util.List;
import java.util.stream.IntStream;
public class Kirby extends GameMaster {private static final List<IRiddle> RIDDLE_POOL = IntStream.rangeClosed(1, 10)
        .mapToObj(i -> new RiddleImpl(getEnv("KIRBY_RIDDLE_" + i), getEnv("KIRBY_ANSWER_" + i), getEnv("KIRBY_HINT_" + i)))
        .map(r -> (IRiddle) r)
        .toList();
    public Kirby() {
        super(RIDDLE_POOL);
    }
    @Override
    public String greet() {
        return "Kirby welcomes you with a cheerful riddle...";
    }
}
