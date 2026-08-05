#!/bin/bash

# Script de Deploy para probros (sitio estático)
# Uso: ./deploy.sh

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

VPS_USER="linuxuser"
VPS_HOST="216.238.79.66"
LOCAL_DIR="/c/Users/bejar/Downloads/probros"
REMOTE_DIR="/var/www/top"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}   DEPLOY - PROBROS${NC}"
echo -e "${BLUE}========================================${NC}"

# PASO 1: Empaquetar sitio
echo -e "\n${GREEN}[1/4] Empaquetando archivos...${NC}"
cd "$LOCAL_DIR"
tar -czf site.tar.gz --exclude='site.tar.gz' --exclude='deploy.sh' --exclude='./admin' .
echo -e "${GREEN}✓ Archivos empaquetados${NC}"

# PASO 2: Subir al VPS
echo -e "\n${GREEN}[2/4] Subiendo archivos al VPS...${NC}"
scp site.tar.gz "$VPS_USER@$VPS_HOST:/home/linuxuser/site.tar.gz"
echo -e "${GREEN}✓ Archivos subidos${NC}"

# PASO 3: Desplegar en el VPS
echo -e "\n${GREEN}[3/4] Desplegando en el VPS...${NC}"
ssh "$VPS_USER@$VPS_HOST" << ENDSSH
sudo mkdir -p $REMOTE_DIR
sudo rm -rf $REMOTE_DIR/*
sudo tar -xzf /home/linuxuser/site.tar.gz -C $REMOTE_DIR
sudo chown -R www-data:www-data $REMOTE_DIR
sudo chmod -R 755 $REMOTE_DIR
rm /home/linuxuser/site.tar.gz
echo "Sitio desplegado en $REMOTE_DIR"
ENDSSH
echo -e "${GREEN}✓ Deploy completado${NC}"

# PASO 4: Limpiar temporal local
rm -f "$LOCAL_DIR/site.tar.gz"

echo -e "\n${GREEN}[4/4] Todo listo!${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ Sitio desplegado en $REMOTE_DIR${NC}"
echo -e "${BLUE}========================================${NC}"
