package com.entity;

public class User {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String active;
	
    // constructors
	public User() {
	}
	
	public User(int userId, String username, String password, String fullName, String active) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.active = active;
	}
	
	    // getters and setters
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getUserId() {
        return userId;
    }
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
        return username;
    }
	
	public void setPassword(String password) {
		this.password = password;
		
	}
	
	public String getPassword() {
        return password;
    }
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getFullName() {
        return fullName;
    }
	
	public void setActive(String active) {
		this.active = active;
		
	}
	
	public String getActive() {
        return active;
    }
	
	    @Override
	    
	        public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", active='" + active + '\'' +
                '}';
        }
	    }
