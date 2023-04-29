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

## TODOS

- 世界生成
- 將世界鑲入空間分割資料結構中
- 處理蛇與世界互動
- 處理/顯示計分
- 設計遊戲初始/設定介面
- 測試不同views之間的切換/分層

## 已知問題

如果在 run 的時候有出現奇怪的 classNotFound Error，請把 javafx 從 buildpath 移除，再裝回來。
