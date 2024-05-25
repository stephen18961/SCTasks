from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_migrate import Migrate
from datetime import datetime
import os

app = Flask(__name__)
if os.getenv('FLASK_ENV') == 'docker':
    app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://root:rootpassword@db/tasks'
db = SQLAlchemy(app)
CORS(app, resources={r"/*":{'origins':'*'}})
# CORS(app, resources={r'/*':{'origins': 'http://localhost:8080',"allow_headers": "Access-Control-Allow-Origin"}})
app.config.from_object(__name__)
migrate = Migrate(app, db)

class Task(db.Model):
    __tablename__ = 'tasks'  # Specify the table name explicitly
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(120), nullable=False)
    description = db.Column(db.String(120), nullable=False)
    category = db.Column(db.String(120), nullable=False)
    status = db.Column(db.String(120), nullable=False)
    createdTime = db.Column(db.BigInteger, nullable=False)
    finishedTime = db.Column(db.BigInteger, nullable=True)
    duration = db.Column(db.BigInteger, nullable=True)

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


@app.route('/tasks', methods=['GET'])
def get_tasks():
    tasks = Task.query.all()
    return jsonify([task.to_dict() for task in tasks])

@app.route('/tasks/<int:id>', methods=['GET'])
def get_task(id):
    task = Task.query.get_or_404(id)
    return jsonify(task.to_dict())

@app.route('/tasks', methods=['POST'])
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
    return jsonify(task.to_dict()), 201

@app.route('/tasks/<int:id>', methods=['PUT'])
def update_task(id):
    data = request.get_json()
    task = Task.query.get_or_404(id)
    task.title = data['title']
    task.description = data['description']
    task.category = data['category']
    task.status = data['status']
    task.createdTime = data['createdTime']
    task.finishedTime = data.get('finishedTime')
    task.duration = data.get('duration')
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
    app.run(host='0.0.0.0', port=5000)
