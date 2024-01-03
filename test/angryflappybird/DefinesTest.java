package angryflappybird;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;

class DefinesTest {
	@BeforeAll
    static void initJfxRuntime() { 
        Platform.startup(() -> {
        });
    }

// Test case for checking dimensions in Defines class
	@Test
    public void testDimensions() {
        Defines defines = new Defines();

        // Check if the dimension constants are set as expected
        assertEquals(600, defines.APP_HEIGHT);
        assertEquals(600, defines.APP_WIDTH);
        assertEquals(570, defines.SCENE_HEIGHT);
        assertEquals(400, defines.SCENE_WIDTH);
    }

 // Test case for checking blob settings in Defines class
	@Test
    public void testBlobSettings() {
        Defines defines = new Defines();

        // Check if the blob settings are correctly initialized
        assertEquals(70, defines.BLOB_WIDTH);
        assertEquals(70, defines.BLOB_HEIGHT);
        assertEquals(70, defines.BLOB_POS_X);
        assertEquals(200, defines.BLOB_POS_Y);
    }

}
