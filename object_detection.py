from imageai.Detection import VideoObjectDetection
import os
import cv2

current_directory = os.getcwd()

camera = cv2.VideoCapture(0)

detector = VideoObjectDetection()
detector.setModelTypeAsRetinaNet()

detector.setModelPath(os.path.join(current_directory , "resnet50_coco_best_v2.0.1.h5"))
detector.loadModel()

detections = detector.detectObjectsFromVideo(
                camera_input = camera,
                output_file_path = os.path.join(current_directory, "camera_detected_video"),
                frames_per_second = 20, log_progress=True)