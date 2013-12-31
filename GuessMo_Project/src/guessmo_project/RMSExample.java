package guessmo_project;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/**
 * @author userad
 */
public class RMSExample extends MIDlet implements CommandListener {

    Display display;
    RecordStore rs = null;
    int count = 0;
    Command exit = new Command("Exit", Command.EXIT, 0);
    Command newRecord = new Command("Record", Command.OK, 0);
    ChoiceGroup items = new ChoiceGroup("Items", Choice.MULTIPLE);
    
    public void startApp() {
        if(display == null) {
           display = Display.getDisplay(this);
           display.setCurrent(new RmsForm());
           
           loadStore();
        }
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        
    }
    
    public void loadStore(){
        byte[] data = new byte[16];
        int recLength;
        
        items.deleteAll();
        
        try {
            rs = RecordStore.openRecordStore("Rms Example", true);
            
            for(int recID=1; recID <= rs.getNumRecords(); recID++){
                recLength = rs.getRecord(recID, data, 0);
                String item = new String(data, 0, recLength);
                items.append(item, null);
            }
        }catch(Exception e){
            
        }
       
    }
    
    class RmsForm extends Form{
            RmsForm(){
                super("RmsExample");
                addCommand(exit);
                addCommand(newRecord);
                setCommandListener(RMSExample.this);
                append(items);
            }
        }

    public void commandAction(Command c, Displayable d) {
            if(c == exit){
                Quit();
                notifyDestroyed();
            }
            if(c == newRecord){
                try {
                    String newItem = "Record #" + rs.getNextRecordID();
                    
                    byte[] bytes = newItem.getBytes();
                    
                    rs.addRecord(bytes, 0, bytes.length);
                    
                    items.append(newItem, null);
                    
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
            }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
    }
    
    public void Quit(){
        if(rs != null){
            try{
                rs.closeRecordStore();
            }catch(RecordStoreNotOpenException e) {
                
            }catch(Exception e){
                
            }
        }
    }
}
