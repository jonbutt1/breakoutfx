import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A graphical view of the game using JavaFX.
 * @author Almas Baimagambetov
 */
public class View implements Observer
{
  private final static int RESET_AFTER = 200; // Farme rate
  private Canvas canvas;        // Actual canvas that game drawn on
  private GraphicsContext g;    // Graphics context 
  private Font font = new Font( 24 );

  private GameObj   bat;        // The bat
  private GameObj   ball;       // The ball
  private List<GameObj> bricks; // The bricks

  private int frames = 0;       // Frames output
  private int score =  0;       // The score

  public View(Canvas canvas )
  {
    this.canvas = canvas;               // Canvas used
    g = canvas.getGraphicsContext2D();  // Get graphics context from Canvas
    g.setFont( font );                  // System in 24 point
    Timer.startTimer();                 // Start Timer
  }

  /**
   * Called when the model has changed,
   * @param m The model that has changed
   * @param arg Optional arguments (Not used)
   */
  @Override
  public void update(Observable m, Object arg)
  {
    frames++;
    Debug.trace("update");
    synchronized( Model.class )   // Make thread safe
    {
      Model model = (Model) m;
      // Get state of model
      bat    = model.getBat();    // Bat
      ball   = model.getBall();   // Ball
      bricks = model.getBricks(); // Bricks
      score  = model.getScore();  // Score
    }
    drawActualPicture();          // Now display
  }

  /**
   * Draw the actual picture of the games current state
   */
  private void drawActualPicture()
  {
    if ( bricks == null ) return;       // Race condition
    
    synchronized( Model.class )   // Make thread safe
    {
    g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    displayGameObj( bat );
    displayGameObj( ball);
      
    String fmt = "BreakOut: Score = [%6d] fps=%5.1f";
    String text = String.format(fmt, score, 
                                frames/(Timer.timeTaken()/1000.0)
                   );
    if ( frames > RESET_AFTER ) 
    { 
      frames = 0; Timer.startTimer(); 
    }

    g.fillText( text, 50, 50, 400 );

    // Display the bricks that make up the game
    // *[4]******************************************************[4]*
    // * Fill in code to display bricks                             *
    // **************************************************************
    for (GameObj brick : bricks) 
        {
        if (brick != null) // if there is a brick
        {
            displayGameObj ( brick ); //display it
        }
    }
        }
  }

  /**
   * Display a game object as a rectangle
   * @param go The game object
   */
  private void displayGameObj( GameObj go )
  {
    g.setFill ( go.getColour().forFX() );
    g.fillRect( go.getX(),     go.getY(), 
                go.getWidth(), go.getHeight() );
  }
} 
