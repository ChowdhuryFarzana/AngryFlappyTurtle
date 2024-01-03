package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The `Sprite` class represents a game sprite with properties such as
 * position, velocity, image, and dimensions. It provides methods for
 * rendering, updating, and detecting collisions with other sprites
 * 
 * @author Team 5
 */
public class Sprite {  
	
    // coefficients related to the sprite
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    
    /**
     * Default constructor for the `Sprite` class
     * Initializes the sprite with default position and velocity
     */
    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Constructor for the `Sprite` class with specified position and image
     *
     * @param pX     Initial X-coordinate of the sprite
     * @param pY     Initial Y-coordinate of the sprite
     * @param image  Image associated with the sprite
     */
    public Sprite(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    /**
     * Sets the image associated with the sprite
     *
     * @param image The image to be set for the sprite
     */
    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    
    /**
     * Sets the X and Y coordinates of the sprite
     *
     * @param positionX The X-coordinate to be set
     * @param positionY The Y-coordinate to be set
     */
    public void setPositionXY(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    /**
     * Sets the Y coordinate of the sprite
     *
     * @param positionY The Y-coordinate to be set
     */
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
   
    /**
     * Get the X-coordinate of the sprite
     *
     * @return The X-coordinate of the sprite
     */
    public double getPositionX() {
        return positionX;
    }
    
    /**
     * Get the Y-coordinate of the sprite
     *
     * @return The Y-coordinate of the sprite
     */
    public double getPositionY() {
        return positionY;
    }
    
    /**
     * Sets the velocity of the sprite in both X and Y directions
     *
     * @param velocityX The velocity in the X direction
     * @param velocityY The velocity in the Y direction
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    /**
     * Adds to the current velocity of the sprite in both X and Y directions
     *
     * @param x The amount to be added to the X velocity
     * @param y The amount to be added to the Y velocity
     */
    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }
    
    /**
     * Get the X velocity of the sprite
     *
     * @return The X velocity of the sprite
     */
    public double getVelocityX() {
        return velocityX;
    }
    
    /**
     * Get the Y velocity of the sprite
     *
     * @return The Y velocity of the sprite
     */
    public double getVelocityY() {
        return velocityY;
    }
    
    /**
     * Get the width of the sprite
     *
     * @return The width of the sprite
     */
    public double getWidth() {
        return width;
    }
    
    /**
     * Renders the sprite on the provided `GraphicsContext`
     *
     * @param gc The `GraphicsContext` on which the sprite is rendered
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }
    
    /**
     * Retrieves the boundary (rectangle) of the sprite
     *
     * @return The `Rectangle2D` representing the boundary of the sprite
     */
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }
    
    /**
     * Checks if the sprite intersects with another sprite
     *
     * @param s The other sprite to check for intersection
     * @return `true` if the sprites intersect, otherwise `false`
     */
    public boolean intersectsSprite(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }
    
    /**
     * Updates the position of the sprite based on its velocity and elapsed time
     *
     * @param time The elapsed time since the last update
     */
    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
}
