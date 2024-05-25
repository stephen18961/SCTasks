#!/bin/sh

# Perform Flask-Migrate commands before starting the Flask app
flask db init
flask db migrate
flask db upgrade

# Start the Flask app
exec "$@"
