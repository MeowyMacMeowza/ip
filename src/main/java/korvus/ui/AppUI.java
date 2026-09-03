package korvus.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import korvus.bot.AppKorvus;
import korvus.views.MainView;

public class AppUI extends Application implements UI {
    private AppKorvus korvus;
    private MainView mainView;
    //Korvus korvus = new Korvus(this)

    //say() -> maybe i am the mainpane lmao -> override;
    //listen() -> NO NEED, directly do korvus.sendReply(msg);
    //Launcher -> launch AppUI(Korvus, MainView) -> MainView (Korvus)

    @Override
    public void start(Stage stage) throws Exception {
        korvus = new AppKorvus(this, "data/");
        mainView = new MainView(korvus);
        korvus.initialise();

        Scene scene = new Scene(mainView, 600, 800);
        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(400);

        stage.show();
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
