# Final project

## Run Configurations

- main class
    ```
    finalProject.Main
    ```

- VM arguments
    ```
    --module-path "<javafx lib path>" --add-modules javafx.controls,javafx.fxml,javafx.media
    ```

## 操控

```
WASD：操控蛇
上下左右：操控自由相機位置（要先切換相機）
滑鼠拖移：操控自由相機角度

C：切換相機
```

## 已知問題

如果在 run 的時候有出現奇怪的 classNotFound Error，請把 javafx 從 buildpath 移除，再裝回來。
