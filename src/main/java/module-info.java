module edu.uopeople.cs1102 {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens edu.uopeople.cs1102 to javafx.fxml;
    exports edu.uopeople.cs1102;
}
