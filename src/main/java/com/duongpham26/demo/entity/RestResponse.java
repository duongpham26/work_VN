package com.duongpham26.demo.entity;

public class RestResponse<T> {
   private int statusCode;
   private String error;

   private Object message;
   private T Data;

   public int getStatusCode() {
      return statusCode;
   }

   public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
   }

   public String getError() {
      return error;
   }

   public void setError(String error) {
      this.error = error;
   }

   public Object getMessage() {
      return message;
   }

   public void setMessage(Object message) {
      this.message = message;
   }

   public T getData() {
      return Data;
   }

   public void setData(T data) {
      Data = data;
   }
}
