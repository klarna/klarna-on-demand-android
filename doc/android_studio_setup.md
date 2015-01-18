## How to setup JUnit4 tests on Android Studio

Please follow these steps in order to setup JUnit4 support on Android Studio:
* Install 'Android Studio Unit Test' plugin:
  * On Android Studio, go to `Preferences`.
  * On the left panel, choose `Plugins`.
  * Click on `Browse Repositories` at the bottom of the right panel. a window is opened.
  * On the new opened window, search and choose `Android Studio Unit Test`.
  * Click `Install Plugin`.
  * When installation is finished, restart Android Studio.
* Create a new JUnit test configuration for Klarna On Demand project:
  * Click `Run` menu-item, and then choose `Edit Configurations`.
  * Click on `+` (Plus) sign on the upper-left corner of the window, and choose: `Gradle`.
  * Fill in the following fields:
    * Name: `Build test classes`.
    * Gradle project: `<project_dir>/klarna-on-demand/build.gradle`.
    * Click: `Apply`.
  * Click on `+` (Plus) sign and choose: `JUnit`.
  * Fill in the following fields:
    * Name: `All Tests`.
    * Test kind: `all in package`.
    * Package: `com.klarna.ondemand`.
    * Search for tests: `In single module`.
    * Working directory: `$MODULE_DIR$` (yes, write it with the dollar signs).
    * Use classpath of module: `klarna-on-demand`.
  * Click `OK`.


## Running the tests
  * Choose `All tests` as the active run-configuration.
  * Click `Run` (^ + R), or `Debug` (^ + D).

