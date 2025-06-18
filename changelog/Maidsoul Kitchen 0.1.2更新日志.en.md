# Maidsoul Kitchen 0.1.2 Changelog

## **Fixes**

- Fixed crash issues when using the official version of **Brewin' And Chewin'**. **Note**: You must now use the official version of **Brewin' And Chewin'**.
- Resolved “Bluetooth connection” issues with the maid's use the farmer's delight cutting_board.
- Maids will try to harvest fruits with the **AGE Property** (if existed) now.

------

## **Additions**

- Maids can now use the **Kettle** from **Farmer's Respite**.
- Added seasonal crop planting logic: maids will plant crops based on in-game seasons when the *Ecliptic Seasons Mod* is installed.
- Maids can now interact with the **Grill** and **Ingredients Basin** from **Barbeque's Delight**.
- Added support for the **Cuisine Skillet** from **Cuisine Delight**.
- Maids can harvest tea plants (green tea, yellow tea, black tea) from **Farmer's Respite**.
  - Requires enabling in the **task configuration interface** and the maid backpack has shears.
- Added the [`MaidMkTaskEnableEvent`](https://github.com/Wall-ev/MaidsoulKitchen/blob/1.0-dev/src/main/java/com/github/wallev/maidsoulkitchen/api/event/MaidMkTaskEnableEvent.java) for mod integration. Customize task  enable conditions via kubejs or other mods.

------

## **Notes**

This is an urgent update and may contain unintended issues. If you encounter problems, please report them via [GitHub Issues](https://github.com/Wall-ev/MaidsoulKitchen/issues).