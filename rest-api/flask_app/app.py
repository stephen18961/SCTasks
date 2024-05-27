from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_migrate import Migrate
from datetime import datetime
from sqlalchemy import create_engine
from sqlalchemy_utils import database_exists, create_database
import os

db = SQLAlchemy()

class Task(db.Model):
    __tablename__ = 'tasks'  # Specify the table name explicitly
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    title = db.Column(db.String(120), nullable=False)
    description = db.Column(db.String(120), nullable=False)
    category = db.Column(db.String(120), nullable=False)
    status = db.Column(db.String(120), nullable=False)
    createdTime = db.Column(db.String(30))
    finishedTime = db.Column(db.String(30))
    duration = db.Column(db.String(120))

    def to_dict(self):
        return {
            'id': self.id,
            'title': self.title,
            'description': self.description,
            'category': self.category,
            'status': self.status,
            'createdTime': self.createdTime,
            'finishedTime': self.finishedTime,
            'duration': self.duration
        }

def create_app():
    
    app = Flask(__name__)
    if os.environ.get('FLASK_ENV') == 'docker':
        db_url = "mysql://root:steve@db/tasks"
    else:
        db_url = "mysql://root:steve@localhost/tasks"
    
    app.config["SQLALCHEMY_DATABASE_URI"] = db_url
    app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
    
    
    engine = create_engine(app.config["SQLALCHEMY_DATABASE_URI"])
    
    if not database_exists(engine.url):
        create_database(engine.url)
        print(f"Database created.")
    
    db.init_app(app)
    
    with app.app_context():
        db.create_all()
        
    return app
    
    
app = create_app()


@app.route('/tasks', methods=['GET'])
def get_tasks():
    tasks = Task.query.all()
    return jsonify({'tasks': [task.to_dict() for task in tasks]})

@app.route('/tasks/<int:id>', methods=['GET'])
def get_task(id):
    task = Task.query.get_or_404(id)
    return jsonify({'task' : task.to_dict()})

@app.route('/new_task', methods=['POST'])
def create_task():
    data = request.get_json()
    task = Task(
        title=data['title'],
        description=data['description'],
        category=data['category'],
        status=data['status'],
        createdTime=data['createdTime'],
        finishedTime=data.get('finishedTime'),
        duration=data.get('duration')
    )   
    db.session.add(task)
    db.session.commit()
    return jsonify({'message': 'Task successfully created.'}), 201 

@app.route('/tasks/<int:id>', methods=['PUT'])
def update_task(id):
    data = request.get_json()
    print('RECEIVED DATA:', data)
    task = Task.query.get_or_404(id)
    task.status = data['status']

    if data['status'] == 'Done':
        task.finishedTime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        start = datetime.strptime(task.createdTime, "%Y-%m-%d %H:%M:%S")
        finish = datetime.strptime(task.finishedTime, "%Y-%m-%d %H:%M:%S")

        # Calculate the duration
        duration = finish - start

        # Get the total seconds of the duration
        total_seconds = int(duration.total_seconds())

        # Convert the total seconds into days, hours, minutes, and seconds
        days, remainder = divmod(total_seconds, 86400)
        hours, remainder = divmod(remainder, 3600)
        minutes, seconds = divmod(remainder, 60)

        # Format the duration string conditionally
        if days > 0:
            formatted_duration = f"{days} days, {hours} hours, {minutes} minutes, and {seconds} seconds"
        elif hours > 0:
            formatted_duration = f"{hours} hours, {minutes} minutes, and {seconds} seconds"
        elif minutes > 0:
            formatted_duration = f"{minutes} minutes, and {seconds} seconds"
        else:
            formatted_duration = f"{seconds} seconds"

        task.duration = formatted_duration

    db.session.commit()
    return jsonify(task.to_dict())

@app.route('/tasks/<int:id>', methods=['DELETE'])
def delete_task(id):
    task = Task.query.get_or_404(id)
    db.session.delete(task)
    db.session.commit()
    return '', 204

if __name__ == '__main__':
    db.create_all()
    app.run(host='0.0.0.0', port=5000, debug=True)