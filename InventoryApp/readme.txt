Inventory App

Purpose:  
- App is designed to allow an inventory to be built with Products and parts to be associated with each product.



In order to run: 

- Make sure to attach the provided sdk.In Intelij File<Project Structure<Libraries<'location of SDK folder'<lib
- For Java version beyond 8 in intellij( versions before 8 will probably run application but it is untested)

Run<Edit Configurations<Modify options(drop down) < add vm options(check it) 
< insert --module-path "ENTER YOU LOCATION FOR SDK"  --add-modules javafx.controls,javafx.fxml
- example --module-path "C:\Program Files\JetBrains\javafx-sdk-11.0.2\lib"  --add-modules javafx.controls,javafx.fxml

https://www.jetbrains.com/help/idea/javafx.html#vm-options for step by step guide