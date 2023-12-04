/*
Elvis Imamura
CSC 171 Final Project
Door class

handles the navigation between levels using an associated level destination
*/

package mypackage;
import mypackage.shapes.*;

public class Door {
        private String destination;
        private ImageShape img;
        public Door(String destination, ImageShape img) {
                this.destination = destination;
                this.img = img;
        }
        public String getDestination() {
                return destination;
        }
        public void setDestination(String destination) {
                this.destination = destination;
        }
        public ImageShape getImage() {
                return img;
        }
        public void setImage(ImageShape img) {
                this.img = img;
        }
        public boolean contains(VectorShape other) {
                return (other.detectCollision(img));
        }
}                          
