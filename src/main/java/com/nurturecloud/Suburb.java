package com.nurturecloud;

import java.io.Serializable;

public class Suburb implements Serializable {
    
        private int Pcode;
        private String Locality;
        private String State;
        private String Comments;
        private String Category;
        private Double Longitude;
        private Double Latitude;
        private Double Distance;
       
        // Getter Methods 
       
        public int getPcode() {
         return Pcode;
        }
       
        public String getLocality() {
         return Locality;
        }
       
        public String getState() {
         return State;
        }
       
        public String getComments() {
         return Comments;
        }
       
        public String getCategory() {
         return Category;
        }
       
        public Double getLongitude() {
         return Longitude;
        }
       
        public Double getLatitude() {
         return Latitude;
        }

        public Double getDistance() {
         return Distance;
        }
       
        // Setter Methods 
       
        public void setPcode(int Pcode) {
         this.Pcode = Pcode;
        }
       
        public void setLocality(String Locality) {
         this.Locality = Locality;
        }
       
        public void setState(String State) {
         this.State = State;
        }
       
        public void setComments(String Comments) {
         this.Comments = Comments;
        }
       
        public void setCategory(String Category) {
         this.Category = Category;
        }
       
        public void setLongitude(Double Longitude) {
         this.Longitude = Longitude;
        }
       
        public void setLatitude(Double Latitude) {
         this.Latitude = Latitude;
        }

        public void setDistance(Double Distance) {
            this.Distance = Distance;
           }
       
    
}
