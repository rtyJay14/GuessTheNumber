package guessmo_project;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.media.Manager;
import javax.microedition.midlet.MIDlet;

public class toneMidlet extends MIDlet implements CommandListener {

  Form displayForm = new Form("Playing all tones");

  StringItem info = new StringItem("", "");

  Command exit = new Command("Exit", Command.EXIT, 1);

  Thread runner;

  boolean stop = false;

  public void startApp() {
    displayForm.append(info);
    Display.getDisplay(this).setCurrent(displayForm);
    displayForm.addCommand(exit);
    displayForm.setCommandListener(this);

    runner = new Thread(new TonePlayer(info, this));
    runner.start();
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
    if (runner != null)
      stop = true;
  }

  public void commandAction(Command cmd, Displayable disp) {
    destroyApp(true);
    notifyDestroyed();
  }

}

class TonePlayer implements Runnable {

  StringItem info;

  toneMidlet midlet;

  public TonePlayer(StringItem info, toneMidlet midlet) {
    this.info = info;
    this.midlet = midlet;
  }

  public void run() {
    try {
      for (int i = 0; i < 128; i++) {
        Thread.sleep(1000);
        info.setText("Playing: " + i);
        Manager.playTone(i, 500, 100);
        if (midlet.stop)
          break;
      }

    } catch (Exception me) {
      System.err.println(me);
    }
  }
}