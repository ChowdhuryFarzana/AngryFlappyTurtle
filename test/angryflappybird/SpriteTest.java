package angryflappybird;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
/**
 * JUNIT test for the sprite class
 * 
 * @author Team 5
 */
class SpriteTest {

    @BeforeAll
    static void initJfxRuntime() { 
        Platform.startup(() -> {
        });
    }

    @Test
    /**
     * Purpose: Test ability to create a Sprite and set its X,Y positions and velocity
     */
    void testSpriteCreationAndSettings() {
        Defines DEF = new Defines();
        
        Sprite object = new Sprite();
        assertEquals(object.getPositionX(), 0);
        assertEquals(object.getPositionY(), 0);
        
        object.setPositionXY(50, 50);
        assertEquals(object.getPositionX(), 50);
        assertEquals(object.getPositionY(), 50);
        
        object.setPositionY(60);
        assertEquals(object.getPositionY(), 60);
        
        Sprite floor = new Sprite(0, 0, DEF.IMAGE.get("floor"));
        floor.setVelocity(20, 20);
        floor.addVelocity(30, -10);
        assertEquals(floor.getVelocityX(), 50);
        assertEquals(floor.getVelocityY(), 10);
    }

}