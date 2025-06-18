import requests
import numpy as np
from PIL import Image
import io
import os

def create_test_image():
    """Create a simple test image"""
    # Create a 224x224 RGB image with some random data
    img_array = np.random.randint(0, 255, (224, 224, 3), dtype=np.uint8)
    img = Image.fromarray(img_array)
    
    # Save to bytes
    img_bytes = io.BytesIO()
    img.save(img_bytes, format='JPEG')
    img_bytes.seek(0)
    
    return img_bytes

def test_api():
    """Test the prediction API"""
    url = "http://127.0.0.1:5000/predict"
    
    # Create test image
    test_image = create_test_image()
    
    # Prepare the request
    files = {'file': ('test_image.jpg', test_image, 'image/jpeg')}
    
    try:
        print("Sending request to API...")
        response = requests.post(url, files=files)
        
        if response.status_code == 200:
            print("✅ API request successful!")
            print("Response:")
            print(response.json())
        else:
            print(f"❌ API request failed with status code: {response.status_code}")
            print("Response:", response.text)
            
    except requests.exceptions.ConnectionError:
        print("❌ Connection error: Make sure the Flask server is running on http://127.0.0.1:5000")
    except Exception as e:
        print(f"❌ Error: {e}")

if __name__ == "__main__":
    test_api() 