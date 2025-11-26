#!/bin/bash

# Start backend in the background
cd backend
./mvnw spring-boot:run &

# Wait for a moment to ensure backend starts properly
sleep 5

# Start frontend in the foreground
cd ../frontend
npm install
npm run serve
