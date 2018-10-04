package particles.brainsynder.cape;

import java.util.ArrayList;
import java.util.List;

public class CapePos {
    private List<String> characters = new ArrayList<>();

    public CapePos (List<String> characters){
        this.characters = characters;
    }

    public List<String> getCharacters() {
        return characters;
    }
}
