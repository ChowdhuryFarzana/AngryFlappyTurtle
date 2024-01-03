package angryflappybird;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

/**
 * This class represents the Angry Flappy Bird game
 * It extends the JavaFX Application class and implements the game logic
 * 
 * @author Team 5
 */
//The Application layer 
public class AngryFlappyBird extends Application {
	
	private Defines DEF = new Defines();
	// for auto pilot mode
	private boolean autopilotMode = false;
	private long autopilotStartTime = 0;
	private final long AUTOPILOT_DURATION = 6000;
    
    // time related attributes
    private long clickTime, startTime, elapsedTime, lastDropTime, backgroundTime;   
    private AnimationTimer timer;
    
    // game components
    private Sprite blob;
    private ArrayList<Sprite> floors;
    private ArrayList<Sprite> columnsTop;
    private ArrayList<Sprite> columnsBottom;
    private ImageView background;
    private Sprite shark;
    private Sprite seashell;
    private Sprite seashellPearl;
    private String currentDifficulty; 
    
   
  
    // game flags
    private boolean CLICKED, GAME_START, GAME_OVER, FLOOR_COLLISION, PIPE_COLLISION, SHARK_COLLISION;
    
    // score and lives
    private int lives = 3;
    private int score = 0;
    
    // scene graphs
    private Group gameScene;	 // the left half of the scene
    private VBox gameControl;	 // the right half of the GUI (control)
    private GraphicsContext gc;		
    
    /**
    * The mandatory main method
    * @param args
    */
    public static void main(String[] args) { 
        launch(args);
    }
       
    /**
     * The start method sets the Stage layer
     *
     * @param primaryStage The primary stage for this application
     * @throws Exception If an error occurs during application startup
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	// initialize scene graphs and UIs
        resetGameControl();    // resets the gameControl
    	resetGameScene(true);  // resets the gameScene
    	
        HBox root = new HBox();
		HBox.setMargin(gameScene, new Insets(0,0,0,10));
		root.setSpacing(10);
		root.getChildren().add(gameScene);
		root.getChildren().add(gameControl);
		
		// add scene graphs to scene
        Scene scene = new Scene(root, DEF.APP_WIDTH, DEF.APP_HEIGHT);
        
        // finalize and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEF.STAGE_TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
    * The resetGameControl method sets up the game control UI elements.
    */
    private void resetGameControl() {
        
        DEF.startButton.setOnMouseClicked(this::mouseClickHandler);
        
        gameControl = new VBox();
        gameControl.setSpacing(10);
        
        VBox seashellBox = new VBox();
        VBox sharkBox = new VBox();
        VBox seashellPearlBox = new VBox();
       
        
        seashellBox.setAlignment(Pos.CENTER);
        seashellPearlBox.setAlignment(Pos.CENTER);
        sharkBox.setAlignment(Pos.CENTER);
       
        
        ImageView seashellImg = new ImageView(DEF.IMAGE.get("seashell"));
        ImageView seashellPearlImg = new ImageView(DEF.IMAGE.get("seashell_pearl"));
        ImageView sharkImage = new ImageView(DEF.IMAGE.get("shark"));
        
        VBox.setMargin(DEF.seashellDescript, new Insets(0, 10, 0,0 ));
        VBox.setMargin(DEF.seashellPearlDescript, new Insets(0, 10, 0,0 ));
        VBox.setMargin(DEF.sharkDescript, new Insets(0, 10, 0, 0));
        
        seashellBox.getChildren().add(DEF.seashellDescript);
        seashellBox.getChildren().add(seashellImg );
        
        seashellPearlBox.getChildren().add(DEF.seashellPearlDescript);
        seashellPearlBox.getChildren().add(seashellPearlImg );
      
        sharkBox.getChildren().add(DEF.sharkDescript);

        // Set the size of all images
        seashellImg.setFitWidth(50); 
        seashellImg.setFitHeight(50); 
        seashellPearlImg.setFitWidth(50); 
        seashellPearlImg.setFitHeight(50); 
        sharkImage.setFitHeight(50); 
        sharkImage.setFitWidth(50); 
        sharkImage.setFitHeight(50); 
        
        sharkBox.getChildren().add(sharkImage);
        
        // Set margins and padding for buttons
        Insets buttonInsets = new Insets(5); 
        
        // Add a button or control to select difficulty level and attach an event handler
        Button easyButton = new Button("Easy");
        easyButton.setOnAction(e -> setDifficulty("easy"));
        easyButton.setStyle("-fx-font-size: 12px;");

        Button mediumButton = new Button("Medium");
        mediumButton.setOnAction(e -> setDifficulty("medium"));
        mediumButton.setStyle("-fx-font-size: 12px;");

        Button hardButton = new Button("Hard");
        hardButton.setOnAction(e -> setDifficulty("hard"));
        hardButton.setStyle("-fx-font-size: 12px;");
       
        // add a text node for default level information
        Text defaultLevelText = new Text("<Default level is medium>");
        defaultLevelText.setStyle("-fx-font-size: 10px;");
        
        // add a text node for for choosing level
        Text difficultyLevel = new Text("Choose difficulty level:");
        difficultyLevel.setStyle("-fx-font-size: 15px;");
        
        
        gameControl.getChildren().addAll(DEF.startButton, defaultLevelText, difficultyLevel, easyButton, mediumButton, hardButton, seashellBox, seashellPearlBox, sharkBox);
    }

    /**
    * The mouseClickHandler method handles mouse click events
    *
    * @param e The MouseEvent object representing the mouse click event
    */
    private void mouseClickHandler(MouseEvent e) {
    	if (GAME_OVER) {
            resetGameScene(false);
        }
    	else if (GAME_START){
            clickTime = System.nanoTime();
            
            //Play swimming effect
            DEF.TURTLE_MEDIA.stop();
            DEF.TURTLE_MEDIA.play();
            
            // Play background music
            DEF.BMG.setCycleCount(MediaPlayer.INDEFINITE);
            DEF.BMG.play();
        }
    	
    	GAME_START = true;
        CLICKED = true;   
    }
    
  
    /**
     * The resetGameScene method resets the game scene and initializes game elements.
     *
     * @param firstEntry Indicates whether it is the first entry to the game scene.
     */
    void resetGameScene(boolean firstEntry) {

    	// reset variables
        CLICKED = false;
        GAME_OVER = false;
        FLOOR_COLLISION = false;
        GAME_START = false;
        floors = new ArrayList<>();
        columnsTop = new ArrayList<>();
        columnsBottom = new ArrayList<>();
        background = DEF.IMVIEW.get("background1");
        
    	if(firstEntry) {
    		// create two canvases
            Canvas canvas = new Canvas(DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
            gc = canvas.getGraphicsContext2D();
            
            // create the game scene
            gameScene = new Group();
            gameScene.getChildren().addAll(background, canvas, DEF.score, DEF.lives);
    	}
    	
    	// update lives
    	DEF.lives.setText("\n Lives:" + String.valueOf(lives)); 
    	
    	// initialize floor
    	for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    		
    		int posX = i * DEF.FLOOR_WIDTH;
    		int posY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    		
    		Sprite floor = new Sprite(posX, posY, DEF.IMAGE.get("floor"));
    		floor.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
    		floor.render(gc);
    		
    		floors.add(floor);
    	}
    	
    	// initialize top columns
        for (int i=0; i < DEF.COLUMN_COUNT; i++) {           
            int posX = (i+1)*(DEF.SCENE_WIDTH);
            Sprite column = new Sprite(posX, DEF.TOP_COLUMN_POSY[i], DEF.IMAGE.get("column_top"));
            column.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            column.render(gc);
            columnsTop.add(column);
        }
        
        // initialize bottom columns
        for (int i=0; i < DEF.COLUMN_COUNT; i++) {
            int posX = (i+1)*(DEF.SCENE_WIDTH);
            int posY = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[i];
            Sprite column = new Sprite(posX, posY, DEF.IMAGE.get("column_bottom"));
            column.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
            column.render(gc);
            columnsBottom.add(column);
        }
        
        // initialize blob
        blob = new Sprite(DEF.BLOB_POS_X, DEF.BLOB_POS_Y,DEF.IMAGE.get("turtle0"));
        blob.render(gc);
        
        // initialize timers
        startTime = System.nanoTime();
        backgroundTime = System.nanoTime();
        timer = new MyTimer();
        timer.start();
        lastDropTime = System.nanoTime();
        
        // initialize shark
        int randomColumn = (int)Math.floor(Math.random() *(3 - 0 + 1) + 0);
        double SHARK_POS_X = (randomColumn+1)*(DEF.SCENE_WIDTH);
        shark = new Sprite(SHARK_POS_X, 0, DEF.IMAGE.get("shark"));
        shark.setVelocity(DEF.SCENE_SHIFT_INCR, 0.15);
        shark.render(gc);
        
        // initialize seashell
        int randomNum = 1 + (int)(Math.random() * ((2 - 1) + 1));
        double  SEASHELL_POS_X = (randomColumn+1)*(DEF.SCENE_WIDTH);
        double SEASHELL_POS_Y = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[randomColumn] - 66;
        seashell = new Sprite(SEASHELL_POS_X, SEASHELL_POS_Y,DEF.IMAGE.get("seashell"));
        seashell.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
        seashell.render(gc);
        
        // initialize seashellpearl
        int otherRandomColumn = (randomColumn + 2) % 4;
        double  SEASHELLPEARL_POS_X = (otherRandomColumn+1)*(DEF.SCENE_WIDTH);
        double SEASHELLPEARL_POS_Y = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[otherRandomColumn] - 70;  
        seashellPearl = new Sprite(SEASHELLPEARL_POS_X, SEASHELLPEARL_POS_Y,DEF.IMAGE.get("seashell_pearl"));
        seashellPearl.setVelocity(DEF.SCENE_SHIFT_INCR, 0);
        seashellPearl.render(gc);
        
        
       
        // After initializing other elements in resetGameScene
      if (!firstEntry) {
           // If it's not the first entry, retain the current difficulty level
            setDifficulty(currentDifficulty);
        } else {
            // If it's the first entry, initialize with the default difficulty
            setDifficulty("medium"); 
        }
      
    }

    /**
     * Method to set difficulty levels
     * 
     * @param level The level the user choose
     */
    private void setDifficulty(String level) {

    	//currentDifficulty = level.toLowerCase();
        switch (level.toLowerCase()) {
        // updating level to easy
            case "easy":
            	currentDifficulty = "easy";
            	shark.setVelocity(DEF.SCENE_SHIFT_INCR, 0.13);
                break;
        // updating level to medium 
            case "medium":
            	currentDifficulty = "medium";
            	shark.setVelocity(DEF.SCENE_SHIFT_INCR, 0.16);
                break;
        // updating level to hard 
            case "hard":
            	currentDifficulty = "hard";
            	shark.setVelocity(DEF.SCENE_SHIFT_INCR, 0.185);
            	break;
            	
        // default level sets to medium
            default:
            	currentDifficulty = "medium";
            	shark.setVelocity(DEF.SCENE_SHIFT_INCR, 0.16);
                break;
        } 
    }
    

    /**
     * The class containing the game logic, the game timer and animation loop
     */
    class MyTimer extends AnimationTimer {
    	// counter for animation frames
    	int counter = 0;
    	
    	/**
         * Handles each frame in the animation loop.
         *
         * @param now The timestamp of the current frame.
         */
    	 @Override
    	 public void handle(long now) {   		 
    		 // time keeping
    	     elapsedTime = now - startTime;
    	     startTime = now;
    	   
    	     // clear current scene
    	     gc.clearRect(0, 0, DEF.SCENE_WIDTH, DEF.SCENE_HEIGHT);
    	     
    	     // check if the game has started
    	     if (GAME_START) {	         
    	    	 // step1: update floor and columns
    	    	 moveFloor();
    	    	 moveColumnsTop();
    	    	 moveColumnsBottom();
    	    	 
    	    	 // step2: update blob
    	    	 moveBlob();
    	    	 checkCollision();
    	    	 
    	    	 // step3: update shark
    	    	 dropShark();
    	    	 
    	    	 // step4: update seashells
    	    	 appearSeashell();
    	    	 
    	    	 // step5: update background
    	    	 updateBackground();
    	    	 
    	    	 //fixed the bounce back animation by fixed the render
    	    	 blob.render(gc);
    	    	 
    	     }  
    	 }
    	 
    	 /**
	     * Method to update the floor position and renders it on the canvas
	     * If a floor segment moves out of the scene, it is repositioned to
	     * create a continuous floor appearance
	     */
    	 private void moveFloor() {
    		
    		for(int i=0; i<DEF.FLOOR_COUNT; i++) {
    			if (floors.get(i).getPositionX() <= -DEF.FLOOR_WIDTH) {
    				double nextX = floors.get((i+1)%DEF.FLOOR_COUNT).getPositionX() + DEF.FLOOR_WIDTH;
    	        	double nextY = DEF.SCENE_HEIGHT - DEF.FLOOR_HEIGHT;
    	        	floors.get(i).setPositionXY(nextX, nextY);
    			}
    			floors.get(i).render(gc);
    			floors.get(i).update(DEF.SCENE_SHIFT_TIME);
    		}
    	 }
    	 
    	 /**
	     * Updates the position of the blob based on user input or time elapsed
	     * If the user has clicked, the blob moves upward with animation
	     * Otherwise, the blob drops after a certain period without clicking
	     */
    	 private void moveBlob() {   		 
			long diffTime = System.nanoTime() - clickTime;
			
			// blob flies upward with animation
			if (CLICKED && diffTime <= DEF.BLOB_DROP_TIME) {
				int imageIndex = Math.floorDiv(counter++, DEF.BLOB_IMG_PERIOD);
				imageIndex = Math.floorMod(imageIndex, DEF.BLOB_IMG_LEN);
				blob.setImage(DEF.IMAGE.get("turtle"+String.valueOf(imageIndex)));
				blob.setVelocity(0, DEF.BLOB_FLY_VEL);
			}
		
			// blob drops after a period of time without button click
			else {
			    blob.setVelocity(0, DEF.BLOB_DROP_VEL); 
			    CLICKED = false;
			}

			// render blob on GUI
			blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    	 }
         
    	 /**
    	  * Updates the position of the top columns, adds score when the blob passes a column,
    	  * and renders the columns 
    	  */
         private void moveColumnsTop() {
            
            for(int i=0; i<DEF.COLUMN_COUNT; i++) {
                double posX = columnsTop.get(i).getPositionX();
                
                // add score when blob passes column
                if(posX - 2 == 70) {
                    score++;
                    DEF.score.setText(" " + String.valueOf(score));
                    
                    // play sound effect
                    DEF.PIPE_MEDIA.stop();
                    DEF.PIPE_MEDIA.play();
                
                }
                // loop columns
                if (posX <= -(3*DEF.FLOOR_WIDTH)) {
                    double nextX = columnsTop.get((i+1)%DEF.COLUMN_COUNT).getPositionX() + 3*DEF.FLOOR_WIDTH;
                    double nextY = DEF.TOP_COLUMN_POSY[i];
                    columnsTop.get(i).setPositionXY(nextX, nextY);
                }
                
                columnsTop.get(i).render(gc);
                columnsTop.get(i).update(DEF.SCENE_SHIFT_TIME);
            }
         }
         
         /**
          * Updates the position of the bottom columns and renders them 
          * If a column moves out of the scene, it is repositioned to 
          * create a continuous appearance
          */
         private void moveColumnsBottom() {
             
             for(int i=0; i<DEF.COLUMN_COUNT; i++) {
                 if (columnsBottom.get(i).getPositionX() <= -(3*DEF.FLOOR_WIDTH)) {
                     double nextX = columnsBottom.get((i+1)%DEF.COLUMN_COUNT).getPositionX() + 3*DEF.FLOOR_WIDTH;
                     double nextY = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[i];
                     columnsBottom.get(i).setPositionXY(nextX, nextY);
                 }
                 
                 columnsBottom.get(i).render(gc);
                 columnsBottom.get(i).update(DEF.SCENE_SHIFT_TIME);
             }
          }
    	 
         /**
          * Updates the position of the shark, repositioning it 
          * when it goes out of play screen
          * Renders the shark 
          */
    	 private void dropShark() {
    	     // variable hold a generated index of a random column
    	     int randomColumn = (int)Math.floor(Math.random() *(3 - 0 + 1) + 0);
    	     
    	     // when the shark reach the screen boundary
    	     if (shark.getPositionX() == DEF.SCENE_WIDTH) {
    	         
    	         // set the shark to top of the screen again
    	         shark.setPositionY(0);
             } 
    	     
    	     // when the shark is out of the play screen
    	     else if(shark.getPositionX()<=0 || shark.getPositionY()>570  ) {
    	         
    	         // reposition the shark with a new random position
                 double nextX = columnsTop.get((randomColumn+1)%DEF.COLUMN_COUNT).getPositionX() + 3*DEF.FLOOR_WIDTH;
                 double nextY = 0;
                 shark.setPositionXY(nextX, 0);    
             }
    	     
    	     shark.update(DEF.SCENE_SHIFT_TIME);
    	     shark.render(gc);
         }
    	     
    	 /**
    	  * Updates the position of the seashell and seashell pearl, rendering them
    	  * If both seashell and seashell pearl are out of the scene, 
    	  * repositions them randomly into the screen again
    	  */
    	 private void appearSeashell() {
    	     
    	     // variable hold a generated index of a random column
    	     int randomColumn = (int)Math.floor(Math.random() *(3 - 0 + 1) + 0);
    	     
    	     // variable hold a generated index of an another random column except the previous one
    	     int otherRandomColumn = (randomColumn + 2) % 4;
    	     
    	         // when the seashell and the seashell pearl are out of the screen
    	         if (seashell.getPositionX() <= 0 && seashellPearl.getPositionX() <= 0) {
    	             
    	             // reposition seashell with random positon
                     double nextSeashellX = columnsBottom.get((randomColumn+1)%DEF.COLUMN_COUNT).getPositionX() + 3*DEF.FLOOR_WIDTH;
                     double nextSeashellY = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[randomColumn] - 66;
                     seashell.setPositionXY(nextSeashellX, nextSeashellY);
                     
                     // reposition seashell pearl with random positon
                     double nextSeashellPearlX = columnsBottom.get((otherRandomColumn+1)%DEF.COLUMN_COUNT).getPositionX() + 3*DEF.FLOOR_WIDTH;
                     double nextSeashellPearlY = DEF.SCENE_HEIGHT - DEF.COLUMN_HEIGHT + DEF.BOTTOM_COLUMN_POSY[otherRandomColumn] - 70;
                     seashellPearl.setPositionXY(nextSeashellPearlX, nextSeashellPearlY);
    	         }
           
             seashell.render(gc);
             seashell.update(DEF.SCENE_SHIFT_TIME);
             seashellPearl.render(gc);
             seashellPearl.update(DEF.SCENE_SHIFT_TIME);
    	 }
    	 
    	 /**
    	  * Updates the background image based on elapsed time
    	  * The background image changes every 10 seconds
    	  */
    	 private void updateBackground() {
             long backgroundTime2 = System.nanoTime();
             
             if((backgroundTime2 - backgroundTime)*DEF.NANOSEC_TO_SEC > 10) {
                 background.setImage(DEF.IMAGE.get("background2"));
             }           
             if((backgroundTime2 - backgroundTime)*DEF.NANOSEC_TO_SEC > 20) {
                 background.setImage(DEF.IMAGE.get("background1"));
                 backgroundTime = System.nanoTime();
             }
         }
    	 
    	 /**
    	  * Initiates the autopilot mode for the game
    	  * Autopilot mode enables automated control of the blob
    	  * The autopilot mode duration is tracked for later termination
    	  */
    	 private void triggerAutopilotMode() {
    	     autopilotMode = true;
    	     autopilotStartTime = System.currentTimeMillis();
    	     
    	 }
    	 
    	 /**
    	  * Method to handles collisions during autopilot mode
    	  */
    	 private void handleCollisionsDuringAutopilot() { 
    		    if (autopilotMode) {
    		        long currentTime = System.currentTimeMillis();
    		        
    		        if (currentTime - autopilotStartTime >= AUTOPILOT_DURATION) {
    		            
    		            // Autopilot duration has elapsed, reset autopilot mode
    		            autopilotMode = false;
    		            blob.setVelocity(0,0); // to stop blob when autopilot mode ends
    		        } else {
    		            
    		            // During autopilot mode, collisions don't trigger game over
    		            FLOOR_COLLISION = false;
    		            PIPE_COLLISION = false;
    		            SHARK_COLLISION = false;
    		            GAME_OVER = false;
    		            
    		            // Move the blob during autopilot mode
    		            blob.setPositionXY(70, 200);
    		            blob.setVelocity(100, 0);
    		            blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
    		        }
    		    }
    	 }

    	 /**
    	  * Checks for collisions between the blob and various game elements,
    	  * updating game state accordingly.
    	  */
    	 public void checkCollision() {
    		// Check collision with floors 
			for (Sprite floor: floors) {
			    if(blob.intersectsSprite(floor)) {
			        FLOOR_COLLISION = true;
			        GAME_OVER = true;
			    }
			}
			
			// Check collision with top columns
			for (Sprite column: columnsTop) {
			    if(blob.intersectsSprite(column)) {
			        PIPE_COLLISION = true;
                    GAME_OVER = true;
			    }
            }
			
			// Check collision with bottom columns
			for (Sprite column: columnsBottom) {
			    if(blob.intersectsSprite(column)) {
                    PIPE_COLLISION = true;
                    GAME_OVER = true;
                }
            }
			
			// Check collision with seashell
			if(blob.intersectsSprite(seashell)) {
			    // play sound effect
			    DEF.COLLECT_MEDIA.stop();
                DEF.COLLECT_MEDIA.play();
                
                // increase score and update score
			    score+=6;
			    DEF.score.setText(" " + String.valueOf(score));
			    
			    // move seashell off-screen
			    seashell.setPositionXY(-100, -100);
			}
			
		    
		    // Check collision with seashellPearl
		    if (blob.intersectsSprite(seashellPearl)) {
		        
		        // trigger autopilot mode when colliding with seashellPearl
		        triggerAutopilotMode();
		        
		        // move seashell pearl off-screen
		        seashellPearl.setPositionXY(-100, -100);
		    }
		    // handle collisions during auto pilot mode
			if (autopilotMode) {
		        handleCollisionsDuringAutopilot();  
		        
		    } 
		        
			// Check collision of the seashell with the shark and update score
			if(shark.intersectsSprite(seashell)) {
			    
			    // if the shark is in the play screen
			    if(shark.getPositionX()>= 0 && shark.getPositionX() <400) {
			    
			    // decrease score
                score-=3;
                DEF.score.setText(" " + String.valueOf(score));
                
                // set the seashell off-screen
                seashell.setPositionXY(-100, -100);
			    }
            }
			
			// Check collision with shark 
			if(blob.intersectsSprite(shark)) {
			    if(shark.getPositionX()>= 0 && shark.getPositionX() <400) {
			        GAME_OVER = true;
			        SHARK_COLLISION = true;
			    }
            }
			
			// Check collision with shark during the autopilot mode
			if(blob.intersectsSprite(shark)) {
				if(autopilotMode) {
			    GAME_OVER = false;
			    SHARK_COLLISION = false;
				}
            }
			
			// Blob bounces back when hits column
			if(PIPE_COLLISION) {
			    blob.setVelocity(-4700, 2000);
			}

			// Blob bounces back when hits column
            if(SHARK_COLLISION) {
                blob.setVelocity(-1200, 800);
            }

			// End the game when blob hit stuff
			if (GAME_OVER) {
			    // check number of lives to see if player still has more remaining
			    if(lives == 1 || FLOOR_COLLISION || SHARK_COLLISION) {
			        // show "Game Over" sign and reset score and lives
			        Sprite game_over = new Sprite(100, 190, DEF.IMAGE.get("game_over"));
			        game_over.render(gc);
			        lives = 3;
			        score = 0;
			        DEF.score.setText(" " + String.valueOf(score));
			        
			        //stop the music
			        DEF.TURTLE_MEDIA.stop();
			        DEF.BMG.stop();
			    } else {
			        
			    	// decrease lives
			        lives--;    
			    }
			    
			    blob.update(elapsedTime * DEF.NANOSEC_TO_SEC);
                blob.render(gc);
			   
				showHitEffect();
				
				// stop floor movement
				for (Sprite floor: floors) {
					floor.setVelocity(0, 0);
				}
				
				// stop the timer to pause the game
				timer.stop();
			}
    	 }
	 }
			

        /**
         * Initiates a visual hit effect on the game scene, creating a fading animation
         * to indicate a collision or impactful event
         */
	     private void showHitEffect() {
	         
	         // create a parallel transition to combine multiple transitions
	        ParallelTransition parallelTransition = new ParallelTransition();
	        
	        // create a fade transition for the game scene
	        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(DEF.TRANSITION_TIME), gameScene);
	        fadeTransition.setToValue(0);
	        fadeTransition.setCycleCount(DEF.TRANSITION_CYCLE);
	        fadeTransition.setAutoReverse(true);
	        
	        parallelTransition.getChildren().add(fadeTransition);
	        parallelTransition.play();
	        

	     }

		
		
		 
    	 
    } // End of AngryFlappyBird Class


