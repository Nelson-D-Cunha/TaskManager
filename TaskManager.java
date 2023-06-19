package application;
import javafx.application.Application;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;

public class TaskManager extends Application {
	private final ObservableList<Todo> todos = FXCollections.observableArrayList();
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Task Manager");
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(10));
		TableView<Todo> tableView = createTableView();
		root.setCenter(tableView);
		GridPane formPane = createFormPane(tableView);
		root.setBottom(formPane);
		Scene scene = new Scene(root, 600, 400,Color.AQUA);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private TableView<Todo> createTableView() {
		TableView<Todo> tableView = new TableView<>();
		tableView.setItems(todos);
		TableColumn<Todo, String> taskColumn = new TableColumn<>("Task");
		taskColumn.setCellValueFactory(cellData -> cellData.getValue().taskProperty());
		TableColumn<Todo, LocalDate> dueDateColumn = new TableColumn<>("Due Date");
		dueDateColumn.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
		TableColumn<Todo, Priority> priorityColumn = new TableColumn<>("Priority");
		priorityColumn.setCellValueFactory(cellData -> cellData.getValue().priorityProperty());
		tableView.getColumns().addAll(taskColumn, dueDateColumn, priorityColumn);
		return tableView;
	}
	private GridPane createFormPane(TableView<Todo> tableView) {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		TextField taskField = new TextField();
		DatePicker dueDatePicker = new DatePicker();
		ComboBox<Priority> priorityComboBox = new ComboBox<>(FXCollections.observableArrayList(Priority.values()));
		Button addButton = new Button("Add");
		addButton.setOnAction(event -> {
			String task = taskField.getText();
			LocalDate dueDate = dueDatePicker.getValue();
			Priority priority = priorityComboBox.getValue();
			if (task != null && dueDate != null && priority != null) {
				Todo newTodo = new Todo(task, dueDate, priority);
				todos.add(newTodo);
				clearForm(taskField, dueDatePicker, priorityComboBox);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Incomplete Form");
				alert.setContentText("Please fill in all fields.");
				alert.showAndWait();
			}
		});
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(event -> {
			Todo selectedTodo = tableView.getSelectionModel().getSelectedItem();
			if (selectedTodo != null) {
				todos.remove(selectedTodo);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("No Task Selected");
				alert.setContentText("Please select a task to delete.");
				alert.showAndWait();
			}
		});
		gridPane.add(new Label("Task:"), 0, 0);
		gridPane.add(taskField, 1, 0);
		gridPane.add(new Label("Due Date:"), 0, 1);
		gridPane.add(dueDatePicker, 1, 1);
		gridPane.add(new Label("Priority:"), 0, 2);
		gridPane.add(priorityComboBox, 1, 2);
		gridPane.add(addButton, 0, 3);
		gridPane.add(deleteButton, 1, 3);
		return gridPane;
	}
	private void clearForm(TextField taskField, DatePicker dueDatePicker, ComboBox<Priority> priorityComboBox) {
		taskField.clear();
		dueDatePicker.setValue(null);
		priorityComboBox.setValue(null);
	}
}
class Todo {
	private final String task;
	private final LocalDate dueDate;
	private final Priority priority;
	public Todo(String task, LocalDate dueDate, Priority priority) {
		this.task = task;
		this.dueDate = dueDate;
		this.priority = priority;
	}
	public String getTask() {
		return task;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public Priority getPriority() {
		return priority;
	}
	public StringProperty taskProperty() {
		return new SimpleStringProperty(task);
	}
	public ObjectProperty<LocalDate> dueDateProperty() {
		return new SimpleObjectProperty<>(dueDate);
	}
	public ObjectProperty<Priority> priorityProperty() {
		return new SimpleObjectProperty<>(priority);
	}
}
enum Priority {
	LOW, MEDIUM, HIGH
}
