package game.save_load;
/*
public class SaveData implements java.io.Serializable {

    private static final long serialVersionUID = 1L; // kan være hva som helst men må være long

    // variabler som skal lagres
}




-------------------
// Dette har han i Main

Button btnSave = new Button("SAVE");
        btnSave.setOnAction(event -> {
            SaveData data = new SaveData();
            data.name = fieldName.getText();
            data.hp = Integer.parseInt(fieldHP.getText());
            try {
                ResourceManager.save(data, "1.save");
            }
            catch (Exception e) {
                System.out.println("Couldn't save: " + e.getMessage());
            }
        });


Button btnLoad = new Button("LOAD");
        btnLoad.setOnAction(event -> {
            try {
                SaveData data = (SaveData) ResourceManager.load("1.save");
                fieldName.setText(data.name);
                fieldHP.setText(String.valueOf(data.hp));
            }
            catch (Exception e) {
                System.out.println("Couldn't load save data: " + e.getMessage());
            }
        });
*/
