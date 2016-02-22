import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main start off
 * @author Almas Baimagambetov
 */
public class Main extends Application 
{
  @Override
  public void start(Stage primaryStage) throws Exception 
  {
    Scene scene = new Scene( new Pane() );

    FXMLLoader loader = new FXMLLoader(getClass().getResource("ui.fxml"));
    loader.setControllerFactory( type -> new Controller(scene) );

    scene.setRoot(loader.load());

    primaryStage.setScene(scene);
    primaryStage.sizeToScene();
    primaryStage.show();
  }

  public static void main(String[] args)
  {
    Debug.set(false);
    launch(args);
  }
}
