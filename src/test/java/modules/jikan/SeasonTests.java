package modules.jikan;

import dev.zawarudo.holo.modules.jikan.JikanAPI;
import dev.zawarudo.holo.modules.jikan.model.Anime;
import dev.zawarudo.holo.modules.jikan.model.Season;
import dev.zawarudo.holo.utils.exceptions.APIException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeasonTests {

    @Test
    void testSeasonNow() throws APIException {
        List<Anime> now = JikanAPI.getSeason();

        assertNotNull(now);
        assertFalse(now.isEmpty());
    }

    @Test
    void testSeasonSpring2022() throws APIException {
        List<Anime> spring2022 = JikanAPI.getSeason(Season.SPRING, 2022);

        assertNotNull(spring2022);
        assertFalse(spring2022.isEmpty());

        // Checks that season contains specific animes
        assertTrue(spring2022.stream().anyMatch(a -> a.getTitle().equals("Spy x Family")));
        assertTrue(spring2022.stream().anyMatch(a -> a.getTitle().equals("Summertime Render")));
    }

    @Test
    void testSeasonFuture() throws APIException {
        List<Anime> spring2069 = JikanAPI.getSeason(Season.SPRING, 2069);

        assertNotNull(spring2069);
        assertTrue(spring2069.isEmpty());
    }
}