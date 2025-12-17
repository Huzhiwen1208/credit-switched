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

# Development Notes
## Branch format
```shell
BranchName: jackhu/[bugfix/feature/refactor]/implement-login
  1. eg: jackhu/bugfix/fix-login-passwd-error
  2. eg: jackhu/feature/add-user-name
  3. eg: jackhu/refactor/backend-code
```
## Commit message format
```shell
# Commit Message Format eg: 
Backend: entity: add User entity

Details: xxxxxx
```
## How to Commit your code
```shell
git checkout master
git pull

git checkout -b <your-branch-name>
# developing
git push -f
# Make pull request(MR, PR)
```
