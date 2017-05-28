# Android-Exam

GET @ "/users"
  
  // Returns all users from mongodb
  
  Request: None
  
  Response: Returns all users saved in the userdata mongodb collection
  
POST @ "/register"
  
  // Registers a FB user and saves the complete user in mongodb
  
  Request: {email, password, token, phone, displayName}
  
  Response: OK: {message, FBRes, user}
  
  ERROR: {error, code (FB Code), email}  
            
POST @ "/updateToken"
  
  // Updates either phone or/and token by email
  
  Request: {email, phone, token}
  
  Response: Example: {"n":0,"nModified":0,"ok":1}
  

POST @ "/match"

  // Takes a phone or email and returns the complete user

  Request: {email &&|| phone}

  Response: OK: {_id, email, token, phone, displayName}
  
  BAD REQ: {error: "Bad request body"}
  
  NOT FOUND: {error: "No user found"}
            

POST @ "/send"

// Sends message through FB to the requested req token by email

Request: {from, to, msg}

Response: OK: {}

FB ERROR: {error: {code, message}}

BAD RECIEVER: {error: "Reciever not found"}
            
  
