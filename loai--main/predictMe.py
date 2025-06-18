from flask import Flask, request, jsonify, flash, redirect
import cv2
import numpy as np
import tensorflow as tf
import pandas as pd
import os

app = Flask(__name__)
app.secret_key = 'your-very-secret-key'  # ضع هنا مفتاحًا سريًا وآمنًا

# Get the directory where this script is located
script_dir = os.path.dirname(os.path.abspath(__file__))

# Use absolute paths for model and CSV files
model_path = os.path.join(script_dir, 'myModel.h5')
csv_path = os.path.join(script_dir, 'p4.csv')

model = tf.keras.models.load_model('d:/grad2/loai--main/myModel.h5')
 # the path of the model

df = pd.read_csv(csv_path)  # the path of the csv file

def preprocess_image(image_path):
    image = cv2.imread(image_path)
    image = cv2.resize(image, (224, 224))
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = image.astype(np.float32)
    image = np.expand_dims(image, axis=0)
    return image

@app.route('/Loai', methods=['POST'])
def Loai():
    return "Hello World"
    
@app.route('/predict', methods=['POST'])
def predict():
    # check if the post request has the file part
    if 'file' not in request.files:
        return jsonify({
            'error': 'No file provided',
            'message': 'Please send an image file with the key "file"'
        }), 400
    
    image_file = request.files['file']
    
    # Check if file is empty
    if image_file.filename == '':
        return jsonify({
            'error': 'No file selected',
            'message': 'Please select a valid image file'
        }), 400

    # image_file = request.files['image']
    # return "www"
    image_path = os.path.join(script_dir, image_file.filename)
    image_file.save(image_path)
    print("image_path:", image_path)
    image = preprocess_image(image_path)
    prediction = model.predict(image)
    
    top3_indices = np.argsort(prediction[0])[-3:][::-1]
    top3_class_names = [df.iloc[i]['Label'] for i in top3_indices]
    top3_scores = prediction[0][top3_indices]
    top3_percentages = top3_scores / np.sum(top3_scores) * 100
    
    response = {}
    for i in range(3):
        index = top3_indices[i]
        treatment = df.iloc[index]['Treatment']
        if pd.isna(treatment):
            treatment = "No treatment needed"  

        response[f"prediction_{i+1}"] = {
            "class_name": top3_class_names[i],
            "confidence": f"{top3_percentages[i]:.2f}%",
            "example_picture": df.iloc[index]['Example Picture'],
            "description": df.iloc[index]['Description'],
            "prevention": df.iloc[index]['Prevention'],
            "treatment": treatment
        }
    
    return jsonify(response)

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=True)
        
            