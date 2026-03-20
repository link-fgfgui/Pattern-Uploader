# Pattern Uploader

## Key Features

### Smart Pattern Distribution
The mod introduces an **Automated Upload Logic** designed to optimize AE2 network configuration:
*   **Network-wide Scanning:** Automatically scans the AE2 network for all Pattern Providers.
*   **Intelligent Mapping:** Categorizes and maps providers based on their existing recipe category of the pattern and the specific machines they are connected to.
*   **Automatic Routing:** Iterates through processing patterns in the player's inventory and automatically inserts them into the most suitable provider. The matching logic prioritizes the recipe category first, followed by the specific machine compatibility.

### UI & UX Improvements
*   **Batch Upload Shortcut:** Holding `Alt` while clicking the **Encode** button in the Pattern Encoding Terminal triggers a batch upload, sending all compatible patterns from your inventory to the network instantly.
*   **Contextual Tooltips:** Encoded processing patterns now display their associated machine (e.g., "Craftable by Metallurgic Infuser"), making the purpose of each pattern clear at a glance.

### EMI Integration
*   **Recipe ID Tracking:** When filling a recipe from EMI to the Pattern Encoding Terminal, the mod automatically captures the Recipe ID and stores it within the pattern's NBT data.
*   **Data-Driven Routing:** The stored Recipe ID is used to accurately identify which machines can handle the recipe, powering both the auto-upload logic and the contextual tooltips.

---

## 核心功能

### 智能样板分发
本模组引入了一套**自动上传逻辑**，用于优化 AE2 网络的自动化配置：
*   **全网扫描：** 自动扫描 AE2 网络中所有的样板供应器。
*   **智能映射：** 根据供应器内已有的样板类别及其连接的机器进行分类映射。
*   **自动路由：** 遍历玩家背包中的处理样板，并将其自动插入到最合适的供应器中。匹配逻辑优先考虑配方类型，其次匹配具体的机器。

### 交互与体验优化
*   **批量上传快捷键：** 在样板编码终端中，按住 `Alt` 键的同时点击“**编码**”按钮，即可触发批量上传，将背包中所有兼容的样板一键发送至网络。
*   **上下文工具提示：** 编码后的处理样板现在会显示其对应的机器（例如：“可由冶金灌注机合成”），让样板用途一目了然。

### EMI 集成
*   **配方 ID 追踪：** 从 EMI 转移配方到编码终端时，系统会自动捕获该配方 ID 并将其存储在样板的 NBT 数据中。
*   **数据驱动路由：** 利用配方 ID 精确识别可处理该样板的机器，从而驱动自动上传逻辑和工具提示的显示。
