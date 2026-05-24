package gamemasters;
import model.IRiddle;
import model.RiddleImpl;

import java.util.List;
import java.util.stream.IntStream;
public class Deanver extends GameMaster {
    private static final List<IRiddle> RIDDLE_POOL = IntStream.rangeClosed(1, 10)
            .mapToObj(i -> new RiddleImpl(getEnv("DEANVER_RIDDLE_" + i), getEnv("DEANVER_ANSWER_" + i), getEnv("DEANVER_HINT_" + i)))
            .map(r -> (IRiddle) r)
            .toList();
    public Deanver() {
        super(RIDDLE_POOL);
    }
    @Override
    public String greet(){
        return "Denver challenges you with a riddle...";
    }
}
