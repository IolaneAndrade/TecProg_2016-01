#!/bin/bash


# Script for installing Android Studio 2.1.1

  echo 'Downloading Android Studio 2.1.1...'

  curl -O https://dl.google.com/dl/android/studio/ide-zips/2.1.1.0/android-studio-ide-143.2821654-linux.zip

  echo 'Extraction Android Studio 2.1.1...'

  unzip android-studio*.zip


  # Create icon desktop

  echo 'Create icon desktop'

  touch android-studio.desktop

  echo "[Desktop Entry]" >> android-studio.desktop

  echo "Encoding=UTF-8" >> android-studio.desktop

  echo "Version=1.0" >> android-studio.desktop

  echo "Type=Application" >> android-studio.desktop

  echo "Terminal=false" >> android-studio.desktop

  echo "Name=Android Studio" >> android-studio.desktop

  echo "Exec=/opt/android-studio/bin/studio.sh" >> android-studio.desktop

  echo "Comment=Integrated Android developer tools for development and debugging." >> android-studio.desktop

  echo "Icon=/opt/android-studio/bin/studio.png" >> android-studio.desktop

  echo "Categories=GNOME;GTK;Development;IDE;" >> android-studio.desktop


  # Set Permission File

  echo 'Set Permission File...'

  echo 'Requires root privileges'

  sudo chmod +x android-studio.desktop

  sudo mv android-studio.desktop /usr/share/applications


  # Install package(s)

  echo 'Installing Android Studio 2.1.1...'

  echo 'Requires root privileges'

  sudo mv ./android-studio /opt/android-studio


  # Cleanup and finish instalation

  rm android-studio*.zip


  # Set Permission File

  echo 'Set Permission File...'

  cd /opt/android-studio/bin

  sudo chmod +x studio.sh


  # Done

  echo 'Done.'

  whiptail --title "Finished" --msgbox "Installation complete." 8 78

  thirdparty