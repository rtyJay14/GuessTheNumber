package guessmo_project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class GuessMoMidlet extends MIDlet implements CommandListener {

    private Display disp;
    private TextField textF, playerTextField;
    private Image myimage, alertImage, alertImageWin;
    int Lives = 10;
    Alert alert = null;
    int n, ctr, image = 1;
    int x = new Random().nextInt(200); //Range
    private Form dashboard, start_game, menu, aboutP, creditsP, player, listPlayerScore;
    //Dashboard
    private Command start = new Command("Start Game", Command.OK, 2);
    private Command ok = new Command("Ok", Command.OK, 2);
    private Command about = new Command("About", Command.OK, 2);
    private Command credits = new Command("Credits", Command.OK, 2);
    private Command scores = new Command("Scores", Command.OK, 2);
    private Command exit = new Command("Exit", Command.OK, 2);
    //Game
    private Command backCommand = new Command("Back", Command.EXIT, 0);
    private Command okCommand = new Command("Check", Command.OK, 0);
    //Player
    String p = "";
    Player bm;
    //List Players
    private List listOfPlayerScores;
    //Scoring
    static int score = 10;
    int container = 0;
    //Scores
    private Command addPSCommand = new Command("Add", Command.OK, 2);
    //RMS
    RecordStore rs = null;
    ChoiceGroup items = new ChoiceGroup("Players: ", Choice.MULTIPLE);
    //Other
    Ticker ticker = new Ticker("JEDI - Java Education and Development Initiative");
    Alert soundAlert = new Alert("sound Alert");

    public void startApp() {
        dashboard = new Form("Guess Mo");
        dashboard.addCommand(start);
        dashboard.addCommand(about);
        dashboard.addCommand(credits);
        dashboard.addCommand(scores);
        dashboard.addCommand(exit);

        try {
            myimage = Image.createImage("/res/team_logo.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        dashboard.append(myimage);
        dashboard.setCommandListener(this);
        disp = Display.getDisplay(this);
        disp.setCurrent(dashboard);

//        Display.getDisplay(this).setCurrent(new MyCanvas());
    }

    void loadScores() {
        Form rms = new Form("List of Player Scores");
        rms.addCommand(ok);
        rms.setCommandListener(GuessMoMidlet.this);
        rms.append(items);
        disp = Display.getDisplay(this);
        disp.setCurrent(rms);

        rmsScores();
    }

    public void rmsScores() {
        byte[] data = new byte[16];
        int recLength;

//        items.deleteAll();

        try {
            rs = RecordStore.openRecordStore("Scores", true);

            for (int recID = 1; recID <= rs.getNumRecords(); recID++) {
                recLength = rs.getRecord(recID, data, 0);
                String item = new String(data, 0, recLength);
                items.append(item, null);
            }
        } catch (Exception e) {
        }

    }

    void RmsForm() {
        Form rms = new Form("List of Player Scores");
        rms.addCommand(ok);
        rms.setCommandListener(GuessMoMidlet.this);
//        rms.append(items);
        disp = Display.getDisplay(this);
        disp.setCurrent(rms);

        rmsScores();

    }

//    public class MyCanvas extends Canvas {
//
//        private Image image;
//
//        public MyCanvas() {
//            try {
//                image = Image.createImage("/res/hangman image/menu.png");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        protected void paint(Graphics g) {
//            g.drawImage(image, 10, 10, Graphics.TOP | Graphics.LEFT);
//        }
    public void Game() {
        start_game = new Form("Guess Mo");
        textF = new TextField("Enter a number between 0 - 200", "", 25, TextField.NUMERIC);
        start_game.append(textF);

        start_game.addCommand(backCommand);
        start_game.addCommand(okCommand);

        start_game.setCommandListener(this);
        disp = Display.getDisplay(this);
        disp.setCurrent(start_game);
    }

    private void algo() {
        if (textF.getString().trim().length() == 0) {
            return;
        }

        n = Integer.parseInt(textF.getString());

        ctr++;
        Lives--;

        if (n < x) {
            Game();
            start_game.append("Not quite, Try a larger value. ");
            start_game.append("Remaining Life: " + Lives);

            try {
                myimage = Image.createImage("/res/hangman image/" + image++ + ".png");
            } catch (Exception e) {
            }
            start_game.append(myimage);

            if (Lives == 0) {
                NotifyDeath();
            }

            alert = new Alert("Not quite", "Try a larger value " + x, null, AlertType.WARNING);

        } else if (n > x) {
            Game();
            start_game.append("Not quite, Try a smaller value.");
            start_game.append("Remaining Life: " + Lives);

            try {
                myimage = Image.createImage("/res/hangman image/" + image++ + ".png");
            } catch (Exception e) {
            }
            start_game.append(myimage);

            if (Lives == 0) {
                NotifyDeath();
            }

            alert = new Alert("Not quite", "Try a smaller value ", null, AlertType.WARNING);

        } else {
            NewGame();
            playerScore();
//            Game();
//            algo();

        }
        if (n > 200 || n < 0) {
            alert = new Alert("Invalid input Number.");
        }
        alert.setTimeout(Alert.FOREVER);
        disp.setCurrent(alert);
    }

//    public void menu() {
//        menu = new Form("Play");
//
//        disp.setCurrent(menu);
//        try {
//            myimage = Image.createImage("/res/hangman image/youwin.png");
//        } catch (Exception e) {
//        }
//        menu.append(myimage);
//
//    }
    public void about() {
        aboutP = new Form("About");

        try {
            myimage = Image.createImage("/res/hangman image/help.png");
        } catch (Exception e) {
        }

        aboutP.addCommand(ok);
        aboutP.setCommandListener(this);
        aboutP.append(myimage);
        disp.setCurrent(aboutP);
    }

    public void credits() {
        creditsP = new Form("Credits");

        try {
            myimage = Image.createImage("/res/hangman image/credits.png");
        } catch (Exception e) {
        }

        creditsP.addCommand(ok);
        creditsP.setCommandListener(this);
        creditsP.append(myimage);
        disp.setCurrent(creditsP);
    }

//    public void run() {
//        for(int i =1; i < 1000; i++){
//            System.out.println(this.p + " : " + i);
//            try {
//                if(this.p == "Kana"){
//                    Thread.sleep(1000);
//                }else {
//                    Thread.sleep(500);
//                }
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
    public void tryAgain() {
        Alert error = new Alert("Login Incorrect", "Please try again", null, AlertType.ERROR);
        error.setTimeout(Alert.FOREVER);

        disp.setCurrent(error, dashboard);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }

    public void NewGame() {
        try {
            alertImageWin = Image.createImage("/res/hangman image/youwin.png");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        alert = new Alert("You Got It", "Congrats!!! You have made it in " + ctr + " attempts.", alertImageWin, AlertType.INFO);
        x = new Random().nextInt(200);
        ctr = 0;
        Lives = 10;
        image = 1;
    }

    public void NotifyDeath() {
        try {
            alertImage = Image.createImage("/res/hangman image/10.png");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        alert = new Alert("Game Over!", "Loser!", alertImage, AlertType.ALARM);
        ctr = 0;
        Lives = 10;
        image = 1;
        Game();
        algo();
        disp.setCurrent(alert);
    }

    public void testAlert() {
        soundAlert.setType(AlertType.ERROR);
        soundAlert.setString("** ERROR **");
        disp.setCurrent(soundAlert);
    }

    public void commandAction(Command c, Displayable d) {

        //Dashboard Commands
        String label = c.getLabel();

        if (label.equals("Exit")) {
            destroyApp(true);
        } else if (label.equals("Start Game")) {
            //Thread
            int ti = 0;
            int tj = 0;

            //Start Game Thread
            new Thread("" + ti) {
                public void run() {
                    Game();
                }
            }.start();

            //Background Music Thread
            new Thread("" + tj) {
                public void run() {
                    try {
                        InputStream is = getClass().getResourceAsStream("/res/destination.mp3");
                        bm = Manager.createPlayer(is, "audio/mp3");
                        bm.start();
                    } catch (IOException ioe) {
                    } catch (MediaException me) {
                    }
                }
            }.start();
        } else if (label.equals("About")) {
            about();
        } else if (label.equals("Ok")) {
            try {
                startApp();
                bm.stop();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
        } else if (label.equals("Credits")) {
            credits();
        } else if (label.equals("Scores")) {
            loadScores();
        }

        //Game Commands
        if (c == backCommand) {
            startApp();
        } else if (c == okCommand) {
            String text = textF.getString().trim();
            if (!text.equalsIgnoreCase("")) {
                algo();
            } else {
                Game();
                start_game.append("No Value Input. ");
            }
        } else if (c == backCommand) {
            Game();
        }

        //Scores Command
        if (c == addPSCommand) {
            String text = playerTextField.getString().trim();
            if (!text.equals("")) {
                ListOfPlayers();

//                Game();
//                algo();
//                startApp();
            } else {
                playerScore();
                player.append("No String Input");
            }
        }
    }

    void ListOfPlayers() {
//        listOfPlayerScores = new List("List of Player Scores", List.IMPLICIT);
//        listOfPlayerScores.addCommand(ok);
//
//        listOfPlayerScores.setCommandListener(this);
//        disp = Display.getDisplay(this);
//        disp.setCurrent(listOfPlayerScores);        

        try {
            String newItem = playerTextField.getString() + " " + rs.getNextRecordID();
            byte[] bytes = newItem.getBytes();
            rs.addRecord(bytes, 0, bytes.length);

//            listOfPlayerScores.append(playerTextField.getString(), null);
            items.append(newItem, null);

        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

        RmsForm();

    }

    void playerScore() {
        player = new Form("Players");
        playerTextField = new TextField("Enter your name: ", "", 25, TextField.PLAIN);
        player.append(playerTextField);

        player.addCommand(addPSCommand);
        player.setCommandListener(this);

        disp = Display.getDisplay(this);
        disp.setCurrent(player);

    }
}
