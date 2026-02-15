#!/bin/bash

set -e

echo "=========================================="
echo "Student Management System - Startup Script"
echo "=========================================="

# Step 1: Start Docker containers
echo ""
echo "Step 1: Starting Docker containers..."
cd /Users/nahianzarif/Downloads/student-management-system
docker-compose down -v 2>/dev/null || true
sleep 2
docker-compose up -d
echo "✓ Docker containers started"

# Step 2: Wait for database to be ready
echo ""
echo "Step 2: Waiting for PostgreSQL to be ready..."
for i in {1..30}; do
  if docker exec student-management-system-postgres-1 pg_isready -U myuser > /dev/null 2>&1; then
    echo "✓ PostgreSQL is ready"
    break
  fi
  echo "  Attempt $i/30 - Waiting for database..."
  sleep 2
done

# Step 3: Kill any existing Java processes
echo ""
echo "Step 3: Cleaning up old Java processes..."
pkill -9 -f "student-management-system" 2>/dev/null || true
sleep 2

# Step 4: Start the Spring Boot application
echo ""
echo "Step 4: Starting Spring Boot application..."
echo "Application will run on: http://localhost:9090"
echo ""
java -jar target/student-management-system-0.0.1-SNAPSHOT.jar

