package korvus.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import korvus.Korvus;
import korvus.views.MainView;

public class AppUI extends Application implements UI {
    private Korvus korvus;
    private MainView mainView;
    //Korvus korvus = new Korvus(this)

    //say() -> maybe i am the mainpane lmao -> override;
    //listen() -> NO NEED, directly do korvus.sendReply(msg);
    //Launcher -> launch AppUI(Korvus, MainView) -> MainView (Korvus)

    @Override
    public void start(Stage stage) throws Exception {
        korvus = new Korvus(this, "data/");
        mainView = new MainView(korvus);

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(400);
        stage.setHeight(800);
        stage.setWidth(600);

        stage.show();

        korvus.initialise();
    }

    @Override
    public void say(String msg) {
        rawPrint(msg);
    }

    @Override
    public void divider() {
        return;
    }

    @Override
    public void rawPrint(String msg) {
        mainView.handleKorvusMessage(msg);
    }
}
