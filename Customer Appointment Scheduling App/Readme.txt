Scheduling Apppliation
Author Kamron Zell
kzell3@wgu.edu
DATE: 7/18/2021
IntelliJ Community 2021.1.2
JDK 11.0.10
Driver: com.mysql.cj.jdbc.Driver




Purpose: Application is designed to interface with MySQL Database and peform varying tasks.
- Login into Database (password:test,Username:test) and validate for accurate password.
- Login screen switches to Alt language if different LOCALE is detected(French is the only option as of now).
- Provide the ability to add, delete, modify for both customers and appointments.
- Provide the means to filter appointmets based on month,week, and range selected.
- Provide reports on total appointments in system, total appointments based on contact + type, and generate schedule for selected contact.
- Validation of input to ensure added/modified appointmets cannot be overlapped or occur after business hours or on days/times already passed.
- Alert user of upcoming appointments at log in (15minutes notice).
Additional Report:  
- Populates automatically when reports is clicked on mainmenu and will display total number of appointments in the system.

In order to run: 
- add they provided mysql driver lib.
- Make sure to attach the provided sdk.   In Intelij File<Project Structure<Libraries<'location of SDK folder'<lib
- For Java version beyond 8 in intellij( versions before 8 will probably run application but it is untested)

Run<Edit Configurations<Modify options(drop down) < add vm options(check it) 
< insert --module-path "ENTER YOU LOCATION FOR SDK"  --add-modules javafx.controls,javafx.fxml
- example --module-path "C:\Program Files\JetBrains\javafx-sdk-11.0.2\lib"  --add-modules javafx.controls,javafx.fxml

https://www.jetbrains.com/help/idea/javafx.html#vm-options for step by step guide