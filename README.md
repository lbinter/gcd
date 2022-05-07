# GCD Configurator (General Constrained Dynamics Mathematica Configurator)

The GCD configurator is a helper program for the paper of Erhard Glötzl ([Macroeconomic General Constrained Dynamic models (GCD models)](https://mpra.ub.uni-muenchen.de/112385/)) to create and easly edit GCD Models, add and modify plots and generate mathematica files from it.

### Screenshot of the GCD configurator input
![gcd-model](https://user-images.githubusercontent.com/100148373/167151521-3488c00c-5552-4919-8216-0488d42ee1b2.png)

### Screenshot of the Mathematica output
![mathematica example 1](https://user-images.githubusercontent.com/100148373/167164577-69ec84df-fa46-4b77-9dac-9031a03752a3.png)

![mathematica example 2](https://user-images.githubusercontent.com/100148373/167164589-5348c5c8-5085-42d0-8f55-4ffd01f379f3.png)

# 1. Requirements
For program requirements please see https://github.com/lbinter/gcd/wiki/GCD-Requirements

# 2. Installation
Please ensure https://github.com/lbinter/gcd/wiki/Java-Installation-&-Env.-Variables-Requirements are followed before proceeding. 

## 2.1 Binaries
You can download prebuild binaries from 

 * https://drive.google.com/drive/folders/1-yb33aRRC4wR-n9_XAyCBPqlmWz8hrOW?usp=sharing

  
# 3. Starting the program
Unpack the downloaded binary to any location
* Windows
  - Double click _path\bin\gcd.bat_
* Linux
  - Execute _path\bin\gcd_
<br><br><br><br><br>

# Customizing the program
## Starting from Source code
You can start the program via terminal
```
  ./gradlew run
```

Please see the Wiki (https://github.com/lbinter/gcd/wiki/) for corecct usage.
  
## Self build project
* Clone / download project
  - ![github_download](https://user-images.githubusercontent.com/100148373/167261394-8b081f91-cb7d-452f-ad85-5b0d9c43798c.png)
* Unpack the Zip folder to any location
* navigate to the new folder
* To build project please run the following command in a terminal (shift + right click in folder)
```
./gradlew clean build
```
  - ![open_terminal](https://user-images.githubusercontent.com/100148373/167261834-948d19bf-2e8f-47b1-a6ed-72bfda138f36.png)
  - ![terminal](https://user-images.githubusercontent.com/100148373/167261824-f678d062-23fb-4b66-9ccc-977c05400455.png)


* the program can now be found als a _zip_ and _tar_ file in _\folder\build\distributions\gcd-2.0-<timestamp>.zip_

Zenodo: https://zenodo.org/record/6207454
