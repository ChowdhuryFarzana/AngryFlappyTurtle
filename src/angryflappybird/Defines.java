package angryflappybird;

import java.io.File;
import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.media.*;

/**
 * The `Defines` class encapsulates various constants and configurations
 * used throughout the Angry Flappy Bird game. It includes dimensions,
 * coefficients, file paths, images, sounds, and scene nodes.
 * 
 * @author Team 5
 */
public class Defines {
	
	// dimension of the GUI application
    final int APP_HEIGHT = 600;
    final int APP_WIDTH = 600;
    final int SCENE_HEIGHT = 570;
    final int SCENE_WIDTH = 400;

    // coefficients related to the blob
    final int BLOB_WIDTH = 70;
    final int BLOB_HEIGHT = 70;
    final int BLOB_POS_X = 70;
    final int BLOB_POS_Y = 200;
    final int BLOB_DROP_TIME = 300000000;  	// the elapsed time threshold before the blob starts dropping
    final int BLOB_DROP_VEL = 300;    		// the blob drop velocity
    final int BLOB_FLY_VEL = -40;
    final int BLOB_IMG_LEN = 4;
    final int BLOB_IMG_PERIOD = 5;
    
    // coefficients related to the floors
    final int FLOOR_WIDTH = 400; 
    final int FLOOR_HEIGHT = 150;
    final int FLOOR_COUNT = 2;
    
    // coefficients related to the columns
    final int COLUMN_WIDTH = 85;
    final int COLUMN_HEIGHT = 200;
    int COLUMN_COUNT = 4;
    final int[] TOP_COLUMN_POSY = {-50, -70, 0, -10};
    final int[] BOTTOM_COLUMN_POSY = {30, 0, 0, 10};
    
    // coefficients related to game_over sign
    final int SIGN_WIDTH = 200;
    final int SIGN_HEIGHT = 150;
    
    // coefficients related to time
    final int SCENE_SHIFT_TIME = 5;
    final double SCENE_SHIFT_INCR = -0.4;
    final double NANOSEC_TO_SEC = 1.0 / 1000000000.0;
    final double TRANSITION_TIME = 0.1;
    final int TRANSITION_CYCLE = 2;

    // coefficients related to the shark
    final int SHARK_WIDTH = 90;
    final int SHARK_HEIGHT = 90;
    final int SHARK_POS_X = (int)Math.floor(Math.random() *(300 - 100 + 1) + 100);
    final int SHARK_POS_Y = 0;
    final int SHARK_DROP_VEL = 200;          
    
 // coefficients related to seashell
    final int SEASHELL_WIDTH = 85;
    final int SEASHELL_HEIGHT = 70;
    final int SEASHELL_POS_X = 0;
    final int SEASHELL_POS_Y = 0;
    
    // coefficients related to seashell pearl
    final int SEASHELLPEARL_WIDTH = 100;
    final int SEASHELLPEARL_HEIGHT = 100;
    final int SEASHELLPEARL_POS_X = 0;
    final int SEASHELLPEARL_POS_Y = 0;
    
    // coefficients related to media display
    final String STAGE_TITLE = "Angry Flappy Turtle";
	private final String IMAGE_DIR = "../resources/images/";
    final String[] IMAGE_FILES = {"background1", "background2", "turtle0", "turtle1", "turtle2", "turtle3", 
            "floor", "column_top", "column_bottom", "game_over", "shark", "seashell" , "seashell_pearl"};

    final HashMap<String, ImageView> IMVIEW = new HashMap<String, ImageView>();
    final HashMap<String, Image> IMAGE = new HashMap<String, Image>();
    
    // media player for the sound effects
    private final String AUDIO_DIR = "src/resources/sounds/";
    final MediaPlayer TURTLE_MEDIA;
    final MediaPlayer PIPE_MEDIA;
    final MediaPlayer BMG;
    final MediaPlayer COLLECT_MEDIA;
    
    // nodes on the scene graph: button and lives display
    Button startButton;
    Button DifficultyButton;
    Label lives;
    Label score;
    Label seashellDescript;
    Label seashellPearlDescript;
    Label sharkDescript;
    Label DefaultText;
    
    // coefficients related to the frequency of appearance for various objects
	public int SHARK_APPEAR_FREQ;
	public int SEASHELL_APPEAR_FREQ;
	public int SEASHELLPEARL_APPEAR_FREQ;
	
	/**
     * Constructor for the `Defines` class.
     * Initializes images, image views, sounds, and scene nodes.
     */
	Defines() {
		
		// initialize images
		for(int i=0; i<IMAGE_FILES.length; i++) {
			Image img;
			if (i == 0 || i == 1) {
			    img = new Image(pathImage(IMAGE_FILES[i]), SCENE_WIDTH, SCENE_HEIGHT, false, false);
			}
			else if (i == 2 || i == 3 || i == 4 || i == 5){
				  img = new Image(pathImage(IMAGE_FILES[i]), BLOB_WIDTH, BLOB_HEIGHT, false, false);
			}
			else if (i == 7 || i == 8){
			    img = new Image(pathImage(IMAGE_FILES[i]), COLUMN_WIDTH, COLUMN_HEIGHT, false, false);
			}
			else if (i == 9){
			    img = new Image(pathImage(IMAGE_FILES[i]), SIGN_WIDTH, SIGN_HEIGHT, false, false);
			}
			else if (i == 10){
			    img = new Image(pathImage(IMAGE_FILES[i]), SHARK_WIDTH, SHARK_HEIGHT, false, false);
			}
			else if (i == 11){
			    img = new Image(pathImage(IMAGE_FILES[i]), SEASHELL_WIDTH, SEASHELL_HEIGHT, false, false);
			}
			else if (i == 12){
	            img = new Image(pathImage(IMAGE_FILES[i]), SEASHELLPEARL_WIDTH, SEASHELLPEARL_HEIGHT, false, false);
			}
			else {
			      img = new Image(pathImage(IMAGE_FILES[i]), FLOOR_WIDTH, FLOOR_HEIGHT, false, false);
			}
			
    	IMAGE.put(IMAGE_FILES[i],img);
    }
		
		// initialize image views
		for(int i=0; i<IMAGE_FILES.length; i++) {
    		ImageView imgView = new ImageView(IMAGE.get(IMAGE_FILES[i]));
    		IMVIEW.put(IMAGE_FILES[i],imgView);
    	}
		
		// initialize sounds
		String turtleEffect = AUDIO_DIR + "turtleSwim.mp3";
		String pipeEffect = AUDIO_DIR + "pipeSound.mp3";
		String backgroundEffect = AUDIO_DIR+"bmg.mp3";
		String collectEffect = AUDIO_DIR+"collect.mp3";
		
		Media turtleSound = new Media(new File(turtleEffect).toURI().toString());
		Media pipeSound = new Media(new File(pipeEffect).toURI().toString());
		Media backgroundSound = new Media(new File(backgroundEffect).toURI().toString());
		Media collectSound = new Media(new File(collectEffect).toURI().toString());
		
		TURTLE_MEDIA = new MediaPlayer(turtleSound);
		PIPE_MEDIA = new MediaPlayer(pipeSound);
		BMG = new MediaPlayer(backgroundSound);
		COLLECT_MEDIA = new MediaPlayer(collectSound);
		
		BMG.setVolume(0.5);
		
		// initialize scene nodes
		startButton = new Button("Start game!");
		DifficultyButton = new Button ("Difficulty:");
	    
	    score = new Label(" 0");
	    score.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 50));

	    lives = new Label("");
        lives.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 40));
        
        // adding description for seashell
        seashellDescript = new Label("Bonus Points");
        seashellDescript.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        
        // adding description for seashellpearl 
        seashellPearlDescript = new Label("Autopilot Mode");
        seashellPearlDescript.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
        
        // adding description for shark
        sharkDescript = new Label("Avoid Sharks");
        sharkDescript.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 20));
	}
	
	/**
     * Converts a relative image file path to an absolute path
     *
     * @param filepath The relative path of the image file
     * @return The absolute path of the image file
     */
	public String pathImage(String filepath) {
    	String fullpath = getClass().getResource(IMAGE_DIR+filepath+".png").toExternalForm();
    	return fullpath;
    }
	
	/**
     * Resizes an image and updates the image cache
     *
     * @param filepath The relative path of the image file
     * @param width The target width of the resized image
     * @param height The target height of the resized image
     * @return The resized `Image` object
     */
	public Image resizeImage(String filepath, int width, int height) {
    	IMAGE.put(filepath, new Image(pathImage(filepath), width, height, false, false));
    	return IMAGE.get(filepath);
    }
}