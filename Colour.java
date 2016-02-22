import javafx.scene.paint.Color;

/**
 * Hide the specific internal representation of colours
 *  from most of the program.
 * Map to Java FX color when required.
 */
public enum Colour  
{ 
  RED(Color.RED), BLUE(Color.BLUE), GRAY(Color.GRAY), PURPLE(Color.PURPLE);

  private final Color c;

  Colour( Color c ) { this.c = c; }

  public Color forFX() { return c; }
}


