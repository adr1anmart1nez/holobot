package modules.jikan;

import dev.zawarudo.holo.modules.jikan.JikanAPI;
import dev.zawarudo.holo.modules.jikan.model.Appearance;
import dev.zawarudo.holo.modules.jikan.model.Character;
import dev.zawarudo.holo.modules.jikan.model.MediaType;
import dev.zawarudo.holo.utils.exceptions.APIException;
import dev.zawarudo.holo.utils.exceptions.InvalidIdException;
import dev.zawarudo.holo.utils.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTests {

    @Test
    void testCharacterById() throws APIException, InvalidIdException {
        Character luffy = JikanAPI.getCharacter(40);

        assertNotNull(luffy);

        assertEquals(40, luffy.getId());
        assertEquals("Luffy Monkey D.", luffy.getName());
        assertTrue(luffy.getFavorites() != 0);
        assertNotNull(luffy.getNicknames());
        assertNotNull(luffy.getNameKanji());
        assertNotNull(luffy.getAbout());
        assertNotNull(luffy.getImages());
    }

    @Test
    void testCharacterAppearance1() throws APIException, InvalidIdException {
        List<Appearance> luffy = JikanAPI.getCharacter(40, MediaType.ANIME);
        assertNotNull(luffy);
        assertFalse(luffy.isEmpty());
        assertEquals("Main", luffy.get(0).getRole());
    }

    @Test
    void testCharacterAppearance2() throws APIException, InvalidIdException {
        List<Appearance> kaidou = JikanAPI.getCharacter(46109, MediaType.ANIME);

        assertNotNull(kaidou);
        assertFalse(kaidou.isEmpty());
        assertEquals("Supporting", kaidou.get(0).getRole());
    }

    @Test
    void testCharacterSearch() throws APIException, InvalidRequestException {
        List<Character> luffy = JikanAPI.searchCharacter("Luffy");

        assertNotNull(luffy);
        assertFalse(luffy.isEmpty());
    }
}