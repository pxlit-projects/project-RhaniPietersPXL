#!/bin/bash
echo "changing api url to ${APIURL}"
sed -i "s|APIURL|${APIURL}|g" /usr/share/nginx/html/main*.js
exec "$@"