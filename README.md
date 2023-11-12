# csci6461
csci6461 java cisc cpu vm

This is a simple CPU simulator set up with a Model-View-Controller pattern. Each View consumes a model, and transforms it from the controller.

Ex.:
```
App
 |-- CPU View + .fxml
 |   |-- CPU Controller
 |   `-- CPU Model
 `-- Register View + .fxml
     |-- Register Controller
     `-- Register Model
```

State management is handled by a Manager class. The manager is a singleton (only one ever exists) which manages all state throughout the application. This includes behavior such as starrting, stopping, and resetting the application.

Ex.:
```
App
 `-- Program Manager
```