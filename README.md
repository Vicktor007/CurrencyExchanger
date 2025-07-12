**CurrencyExchanger** Java project, complete with setup instructions for IntelliJ IDEA and links to necessary libraries.

---

```markdown
# 💱 CurrencyExchanger

A JavaFX desktop application that enables users to convert currencies using live exchange rate APIs. This project supports multiple data sources for currency exchange rates.

## 🌟 Features

- JavaFX-based GUI
- Integration with two external currency exchange APIs
- Dynamic input and conversion output
- Error handling for API failures and connectivity issues

## 🧰 Technologies Used

- Java 24
- JavaFX SDK 24.0.1
- IntelliJ IDEA
- Maven (via `pom.xml`)

## 📦 External Libraries

1. **JavaFX SDK**
    - Download from: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
    - After download, extract and place it in a known location on your system.
2. **JSON SDK**
    - Download from: [https://search.maven.org/remotecontent?filepath=org/json/json/20250517/json-20250517.jar](https://search.maven.org/remotecontent?filepath=org/json/json/20250517/json-20250517.jar/)
    - After download, extract and place it in a known location on your system.

2. **HTTP Client (Built-in with Java 11+)**
    - No need for external dependencies; Java's built-in `java.net.http` is used.

## 🛠️ Setup Instructions (IntelliJ IDEA)

### 1. Configure JavaFX SDK

- Open IntelliJ IDEA and your CurrencyExchanger project.
- Go to `File > Project Structure > Libraries`.
- Click `+`, then select the path to `javafx-sdk-24.0.1/lib`.
- Apply changes.

### 2. Set VM Options for Running JavaFX

To run the app with the correct module path and access permissions:

1. Go to `Run > Edit Configurations`.
2. Add the following to VM Options:

```text
--module-path "(path to your javafx lib folder)" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```

Make sure to adjust the path to where  your SDK is located on your computer.

### 3. Running the App

- Build the project using IntelliJ's build system or Maven.
- Run the main class (often something like `Main.java` or `App.java`).

## 🧪 Troubleshooting

- **JavaFX Runtime Components Missing**: Verify the module path and SDK version.
- **API Requests Failing**: Check your internet connection or the API endpoints.

## 📌 Todo

- Add dark mode for UI
- Switchable data sources
- Export converted currency data to CSV

---

