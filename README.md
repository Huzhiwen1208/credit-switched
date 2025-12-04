# Environment Establishment Instructions
This project consists of a frontend and a backend service. Follow the steps below to set up and run the environment.

```shell
sudo apt update

sudo apt install -y openjdk-17-jdk nodejs npm

curl -fsSL https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # This loads nvm
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm bash_completion

npm install -g @vue/cli
npm install -g element-ui axios vue-router@3 --save
```

# Running the Services
To run both the frontend and backend services, execute the following script:
```shell
./run.sh
```