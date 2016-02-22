import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.applet.Applet;
import java.awt.*;

/**
 * Model of the game of breakout
 * @author Mike Smith University of Brighton
 */

public class Model extends Observable
{
  // Boarder
  private static final int B              = 6;  // Border offset
  private static final int M              = 40; // Menu offset
  
  // Size of things
  private static final float BALL_SIZE    = 30; // Ball side
  private static final float BRICK_WIDTH  = 50; // Brick size
  private static final float BRICK_HEIGHT = 30;

  private static final int BAT_MOVE       = 5; // Distance to move bat
  
  float brickXPosition = 10; // starting point of positioning bricks
  float brickYPosition = 70;
   
  // Scores
  private static final int HIT_BRICK      = 50;  // Score
  private static final int HIT_BOTTOM     = -200;// Score

  private GameObj ball;          // The ball
  private List<GameObj> bricks;  // The bricks
  private GameObj bat;           // The bat
  
  private boolean runGame = true; // Game running
  private boolean fast = false;   // Sleep in run loop

  private int score = 0;

  private final float W;         // Width of area
  private final float H;         // Height of area

  public Model( int width, int height )
  {
    this.W = width; this.H = height;
  }

  /**
   * Create in the model the objects that form the game
   */

  public void createGameObjects()
  {
    synchronized( Model.class )
    {
      ball   = new GameObj(W/2, H/2, BALL_SIZE, BALL_SIZE, Colour.PURPLE );
      bat    = new GameObj(W/2, H - BRICK_HEIGHT*1.5f, BRICK_WIDTH*3, 
                              BRICK_HEIGHT/4, Colour.GRAY);
      bricks = new ArrayList<>();
      // *[1]******************************************************[1]*
      // * Fill in code to place the bricks on the board              *
      // **************************************************************
      for (int i = 0; i<5; i++) //for 5 lines
      {
          addBricks();  
      
          brickXPosition = 10; //adjusting x & y for next line
          brickYPosition += (BRICK_HEIGHT + 1);
      
        
     }
    }
  }
  
  private ActivePart active  = null;

  /**
   * Start the continous updates to the game
   */
  public void startGame()
  {
    synchronized ( Model.class )
    {
      stopGame();
      active = new ActivePart();
      Thread t = new Thread( active::runAsSeparateThread );
      t.setDaemon(true);   // So may die when program exits
      t.start();
    }
  }

  /**
   * Stop the continous updates to the game
   * Will freeze the game, and let the thread die.
   */
  public void stopGame()
  {  
    synchronized ( Model.class )
    {
      if ( active != null ) { active.stop(); active = null; }
    }
  }

  public GameObj getBat()             { return bat; }

  public GameObj getBall()            { return ball; }

  public List<GameObj> getBricks()    { return bricks; }

  /**
   * Add to score n units
   * @param n units to add to score
   */
  protected void addToScore(int n)    { score += n; }
  
  public int getScore()               { return score; }

  /**
   * Set speed of ball to be fast (true/ false)
   * @param fast Set to true if require fast moving ball
   */
  public void setFast(boolean fast)   
  { 
    this.fast = fast; 
  }

  /**
   * Move the bat. (-1) is left or (+1) is right
   * @param direction - The direction to move
   */
  public void moveBat( int direction )
  {
    // *[2]******************************************************[2]*
    // * Fill in code to prevent the bat being moved off the screen *
    // **************************************************************
    float dist = 0; // actual distance to move (direction + distance)
    float howFar = 0; // the distance to travel
    
    if ((bat.getX() - BAT_MOVE < 0) && direction == -1) // dealing with the bat going off the left hand side of play area
    {
        howFar = BAT_MOVE + (bat.getX() - BAT_MOVE - 1);
    }
    else if ((bat.getX() + (BRICK_WIDTH*3)+ BAT_MOVE) > W && direction == 1) //dealing with bat going off the right hand side of play area
    {
        howFar = W - (bat.getX() + (BRICK_WIDTH * 3) + 1);
    }
    else // normal behaviour - i.e. if the bat is in the 'middle section of board
    {
        howFar = BAT_MOVE;
    }
    
    dist = direction * howFar  ;  // Actual distance to move
    Debug.trace( "Model: Move bat = %6.2f", dist );
    bat.moveX(dist);
  }
  
  /**
   * This method is run in a separate thread
   * Consequence: Potential concurrent access to shared variables in the class
   */
  class ActivePart
  {
    private boolean runGame = true;

    public void stop()
    {
      runGame = false;
    }

    public void runAsSeparateThread()
    {
      final float S = 3; // Units to move (Speed)
      try
      {
        synchronized ( Model.class ) // Make thread safe
        {
          GameObj       ball   = getBall();     // Ball in game
          GameObj       bat    = getBat();      // Bat
          List<GameObj> bricks = getBricks();   // Bricks
        }
  
        while (runGame)
        {
          synchronized ( Model.class ) // Make thread safe
          {
            float x = ball.getX();  // Current x,y position
            float y = ball.getY();
            // Deal with possible edge of board hit
            if (x >= W - B - BALL_SIZE)  ball.changeDirectionX();
            if (x <= 0 + B            )  ball.changeDirectionX();
            if (y >= H - B - BALL_SIZE)  // Bottom
            { 
              ball.changeDirectionY(); addToScore( HIT_BOTTOM ); 
            }
            if (y <= 0 + M            )  ball.changeDirectionY();

            // As only a hit on the bat/ball is detected it is 
            //  assumed to be on the top or bottom of the object.
            // A hit on the left or right of the object
            //  has an interesting affect
    
            boolean hit = false;
              // *[3]******************************************************[3]*
              // * Fill in code to check if a brick has been hit              *
              // *  Remember to remove a brick from the ArrayList (if hit)    *
              // *  Can not use a foreach style loop, as the control variable *
              // *    is a copy of the ArrayList element                      *
              // **************************************************************
            for (int i = 0; i < bricks.size(); i++)
            {
                Rectangle collisionBrick = new Rectangle (Math.round(bricks.get(i).getX()), Math.round(bricks.get(i).getY()), Math.round(bricks.get(i).getWidth()), Math.round(bricks.get(i).getHeight()));
                Rectangle collisionBall = new Rectangle (Math.round(ball.getX()), Math.round(ball.getY()), Math.round(ball.getWidth()), Math.round(ball.getHeight()));
                
                if (collisionBall.intersects(collisionBrick))
                    {
                        bricks.remove(i);
                        hit = true;
                        score += HIT_BRICK;
                    }
            }
              
            if (hit)
              ball.changeDirectionY();
    
            if ( ball.hitBy(bat) )
              ball.changeDirectionY();
              
            
          }
          modelChanged();      // Model changed refresh screen
          Thread.sleep( fast ? 2 : 20 );
          ball.moveX(S);  ball.moveY(S);
        }
      } catch (Exception e) 
      { 
        Debug.error("Model.runAsSeparateThread - Error\n%s", 
                    e.getMessage() );
      }
    }
  }
  
  /**
   * Model has changed so notify observers so that they
   *  can redraw the current state of the game
   */
  public void modelChanged()
  {
    setChanged(); notifyObservers();
  }
  
  /*
   * adding bricks to the board
   */
  public void addBricks()
  {
     while (brickXPosition < (W - BRICK_WIDTH )) 
      {
          bricks.add(new GameObj (brickXPosition, brickYPosition, BRICK_WIDTH, BRICK_HEIGHT, Colour.RED));
          brickXPosition += (BRICK_WIDTH + 1);
        } 
    }
}
