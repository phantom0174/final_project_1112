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
```

## TODOS

- 世界生成
- 將世界鑲入空間分割資料結構中
- 處理蛇與世界互動
- 處理/顯示計分
- 設計遊戲初始/設定介面
- 測試不同views之間的切換/分層

## 設計想法

- 增加難度的方式：提高速度、星球大小/密度、所需吃到的蘋果數量
- buff道具：吃到後可無視障礙物、一段時間內分數 double
- debuff道具（一坨粉塵）：吃到後會加速
- random 道具：隨機獲得效果，外觀採用mario cart

## 已知問題

如果在 run 的時候有出現奇怪的 classNotFound Error，請把 javafx 從 buildpath 移除，再裝回來。
