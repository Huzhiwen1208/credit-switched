#!/bin/bash

# 关闭 8080 和 8081 端口的进程
lsof -ti:8080 | xargs -r kill -9
lsof -ti:8081 | xargs -r kill -9

# Start backend in the background
cd backend
./mvnw spring-boot:run &

# Wait for a moment to ensure backend starts properly
sleep 5

# Start frontend in the foreground
cd ../frontend
npm install
npm run serve
