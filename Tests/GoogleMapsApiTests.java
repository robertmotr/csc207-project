package Tests;

import static org.junit.jupiter.api.Assertions.*;

import Map.GoogleMapsApi;
import org.junit.jupiter.api.Test;


public class GoogleMapsApiTests {

    @Test
    public void getDirectionAddress() {
        String actual = GoogleMapsApi.getDirections("7+Hart+House+Cir,+Toronto+ON+M5S+3H7",
                "5+Hoskin+Ave,+Toronto,+ON+M5S+1H7", "walking");
        String expected = "Head north on Tower Rd and continue for 1 min. Then, Turn right and your Destination will be on the right.";
        assertEquals(expected, actual);
    }

    @Test
    public void getDirectionName() {
        String actual = GoogleMapsApi.getDirections("Hart+House+Toronto+ON",
                "Wycliffe+College+Toronto+ON", "walking");
        String expected = "Head north on Tower Rd toward Hoskin Ave and continue for 1 min. Then, Turn right onto Hoskin Ave and your Destination will be on the right.";
        assertEquals(expected, actual);
    }
}
