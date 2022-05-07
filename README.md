# GCD Configurator (General Constrained Dynamics Mathematica Configurator)

The GCD configurator is a helper program for the paper of Erhard Glötzl ([Macroeconomic General Constrained Dynamic models (GCD models)](https://mpra.ub.uni-muenchen.de/112385/)) to create and easly edit GCD Models, add and modify plots and generate mathematica files from it.

### Screenshot of the GCD configurator input
![gcd-model](https://user-images.githubusercontent.com/100148373/167151521-3488c00c-5552-4919-8216-0488d42ee1b2.png)

### Screenshot of the Mathematica output
![mathematica example 1](https://user-images.githubusercontent.com/100148373/167164577-69ec84df-fa46-4b77-9dac-9031a03752a3.png)

![mathematica example 2](https://user-images.githubusercontent.com/100148373/167164589-5348c5c8-5085-42d0-8f55-4ffd01f379f3.png)

# Requirements
For program requirements please see https://github.com/lbinter/gcd/wiki/GCD-Requirements

# Installation
Please ensure https://github.com/lbinter/gcd/wiki/Java-Installation-&-Env.-Variables-Requirements are followed before proceeding. 

## Self build project
* Clone / download project
  - ![github_download](https://user-images.githubusercontent.com/100148373/167261394-8b081f91-cb7d-452f-ad85-5b0d9c43798c.png)
* To build project please run the following command in a terminal
```
./gradlew clean build
```
* the program can now be found als a _zip_ and _tar_ file in _\folder\build\distributions\gcd-2.0-<timestamp>.zip_

## Binaries
You can download prebuild binaries from 

 * https://drive.google.com/drive/folders/1-yb33aRRC4wR-n9_XAyCBPqlmWz8hrOW?usp=sharing

  
## Starting the program
### Source code
You can start the program via terminal
```
  ./gradlew run
```
### Linux
  Execute _<path>\bin\gcd_
### Windows
  Double click _<path>\bin\gcd.bat_

Please see the Wiki (https://github.com/lbinter/gcd/wiki/) for corecct usage.

Zenodo: https://zenodo.org/record/6207454
