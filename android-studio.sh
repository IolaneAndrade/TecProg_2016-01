#!/bin/sh
echo -ne '\n' | sudo apt-add-repository ppa:paolorotolo/android-studio
sudo apt-get update
sudo apt-get install android-studio android-tools* -y