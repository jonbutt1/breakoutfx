import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;

/**
 * Controller
 * @author Almas Baimagambetov
 */

public class Controller implements Initializable 
{
  //public static final boolean IS_ANDROID = "android".equals(System.getProperty("javafx.platform")) ||
  //                                         "Dalvik".equals(System.getProperty("java.vm.name"));

  // with @FXML annotation FXML loader injects elements from ui.fxml into Controller
  // if fx:id matches the variable name

  @FXML
  private Pane root;
  @FXML
  private Canvas canvas;

  private Scene scene;
  private Model model;                                   // The model of the Game

  public Controller(Scene scene) 
  {
    this.scene = scene;
  }

  // called automatically when FXML load completes
  
  @Override
  public void initialize(URL location, ResourceBundle resources) 
  {
    //if (IS_ANDROID) {
    //  Bounds bounds = Screen.getPrimary().getVisualBounds();
    //  root.setPrefSize(bounds.getWidth(), bounds.getHeight());
    //}
    // we ask the root its size, as desktop and mobile screen sizes are different
    model = new Model((int)root.getPrefWidth(), (int)root.getPrefHeight());
    model.createGameObjects();

    // reset canvas in case different size for mobile screens
    canvas.setWidth(root.getPrefWidth());
    canvas.setHeight(root.getPrefHeight());

    View view = new View(canvas);

    model.addObserver(view);                             // Observer of the model

    // There is more to this than you expect
    // On each mouse event the lambda function below
    //  is executed.
    EventHandler<KeyEvent> keyHandler = event ->     //
    {                                                // handler for key events
      switch( event.getCode() )
      {
        case LEFT :
          model.moveBat( -1);
          break;
        case RIGHT :
          model.moveBat( +1 );
          break;
        case F :             // Fast speed
          model.setFast( true );
          break;
        case N :             // Normal speed
          model.setFast( false );
          break;
        default :
          String name = event.getCode().getName();
          Debug.trace( "Ch typed = %s", name );
      }
    };

    //scene.setOnMouseMoved(mouseHandler);          // register handler above for mouse movement events
    //scene.setOnMouseDragged(mouseHandler);        // also for mouse drag events
    scene.setOnKeyPressed(keyHandler);              // also for mouse drag events

    model.startGame();                              // start the game
  } 
}
