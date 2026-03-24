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

