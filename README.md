# ChatUpProject

PURPOSE OF THE PROJECT :  
It is really useful  for communication in the costly wold of phone calls and SMS.  Although, many chatting applications are 
already there, but we want to get real time experience of how this much fast communication is done in real time for many users 
simultaneously. We want to learn messaging and networking protocols in vey secured environment with very lightweight and 
attractive layouts. 
 
OVERALL DESCRIPTION :
Our approach is to include following features in the app. 
1) App is completely secured with Login phone number and Password. 
2) While doing signup process, there will be OTP verification process for phone number. Application can be used in only one 
   device at a time by each user. 
3) Authentication is completely handled under Firebase, So, any third person can never access the built APIs because access of 
   APIs needs Login details of Admins. 
4)  Our app will basically perform messaging individually, in groups and even in broadcast. 
5) There will be unique ChatUp QR code for each user. One can be a friend by just scanning the QR code of that person. 
6) We can put profile image as well status in profile page. 
7) We are adding Internet Calling feature in the app.  
8) We have implemented Analytics and Crashlytics which will give the information of the active users at any time, which user 
   has spent how much time on which screen of the app, which type of crashes occurred in app. 
9) Storage of the data of each user is made dynamically in Firebase using JSON. 
10) App uses faster and latest data communication on Retrofit basis. 
11) We have implemented Swiping android views which is not implemented till now by any chatting app in android.  
 
DATABASE FORMAT : 
1) phone_number : This will be unique for each user. 
2) user_name : this name is like small name or pet name for the user. 
3) full_name : this is full name of user. 
4) password : this is password for the user to login. 
5) profile_image: this is user changeable image for each user. 
6) qr_code : this is encypted 64 bit encoded bit string for QR code for each user. 
7) list_chat : this contains list of people with whom the user is communicating with encypted messages. 
8) group_list : this contains the goups in which user is included. 
 
Some images taken from the project under construction and attached with the repository: 
